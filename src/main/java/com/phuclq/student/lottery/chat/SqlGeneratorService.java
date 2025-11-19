package com.phuclq.student.lottery.chat;

import com.phuclq.student.lottery.openai.OpenAIClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SqlGeneratorService {
    private static final Set<String> BANNED = Set.of("insert", "update", "delete", "alter", "drop", "create", "grant", "revoke", "truncate", "replace", "call", "load", "outfile", "infile", "lock", "unlock", "set", "use", "rename", "analyze", "optimize");
    private static final String PROMPT = "Bạn là trợ lý sinh SQL cho MySQL.\n- Trả về đúng 1 SELECT.\n- Không giải thích, không code fence.\n- Bảng/cột hợp lệ: draws(id,region,province,draw_date,game), results(id,draw_id,prize_name,seq,number,digits,last2,last3), n2(n).\n- Quy ước: 'đề'=last2 của DB; 'lô 2 số'=last2 mọi giải; 'xiên 2' = 2 last2 khác nhau cùng 1 kỳ.\n- Mặc định region='MB', 30 ngày gần nhất.\n- Không dùng DISTINCT + ORDER BY cột không có trong SELECT (MySQL 3065). Nếu cần unique và sắp theo ngày gần nhất thì GROUP BY ... ORDER BY MAX(d.draw_date) DESC.";
    private final OpenAIClient ai;
    public SqlGeneratorService(OpenAIClient ai) {
        this.ai = ai;
    }

    private static String strip(String s) {
        if (s == null) throw new RuntimeException("Empty");
        s = s.replace("```sql", "").replace("```", "").trim();
        Matcher m = Pattern.compile("(?is)\\bselect\\b[\\s\\S]*").matcher(s);
        if (m.find()) s = m.group().trim();
        if (!s.toLowerCase().startsWith("select")) throw new RuntimeException("Only SELECT");
        return s;
    }

    private static String removeSemi(String s) {
        while (s.endsWith(";")) s = s.substring(0, s.length() - 1).trim();
        return s;
    }

    private static void assertSingle(String s) {
        if (s.indexOf(';') >= 0) throw new RuntimeException("Multiple statements");
    }

    private static void block(String s) {
        String l = s.toLowerCase();
        for (String b : BANNED) {
            if (l.matches("(?is).*\\b" + Pattern.quote(b) + "\\b.*")) throw new RuntimeException("Blocked: " + b);
        }
    }

    private static boolean hasLimit(String s) {
        return s.toLowerCase().matches("(?s).*\\blimit\\s+\\d+\\s*$");
    }

    private static String fixDistinctOrder(String sql) {
        String l = sql.toLowerCase();
        if (!(l.contains("select distinct") && l.contains(" order by "))) return sql;
        try {
            int sel = l.indexOf("select distinct");
            int from = l.indexOf(" from ", sel);
            if (from < 0) return sql;
            String selList = sql.substring(sel + 15, from).trim();
            int order = l.lastIndexOf(" order by ");
            int limit = l.lastIndexOf(" limit ");
            int end = (order >= 0 ? order : (limit >= 0 ? limit : sql.length()));
            String fromPart = sql.substring(from, end).trim();
            String orderExpr = (order >= 0) ? sql.substring(order + 10, (limit > order ? limit : sql.length())).trim() : "";
            String limitClause = (limit >= 0) ? sql.substring(limit).trim() : "";
            if (orderExpr.isEmpty()) return sql;
            List<String> items = split(orderExpr);
            List<String> selAlias = new ArrayList<>(), ordAlias = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                String item = items.get(i).trim();
                String dir = "";
                int sp = lastSpaceOut(item);
                if (sp > 0) {
                    String tail = item.substring(sp + 1).trim();
                    if (tail.equalsIgnoreCase("asc") || tail.equalsIgnoreCase("desc")) {
                        dir = " " + tail.toUpperCase();
                        item = item.substring(0, sp).trim();
                    }
                }
                String a = "_ob" + (i + 1);
                selAlias.add(item + " AS " + a);
                ordAlias.add("t." + a + dir);
            }
            String inner = "SELECT DISTINCT " + selList + (selAlias.isEmpty() ? "" : " ," + String.join(", ", selAlias)) + " " + fromPart;
            String outer = "SELECT * FROM (" + inner + ") t ORDER BY " + String.join(", ", ordAlias) + (limitClause.isEmpty() ? "" : " " + limitClause);
            return outer;
        } catch (Exception e) {
            int order = l.lastIndexOf(" order by ");
            if (order >= 0) {
                int limit = l.lastIndexOf(" limit ");
                String head = sql.substring(0, order);
                String tail = (limit >= 0) ? sql.substring(limit) : "";
                return head + " ORDER BY 1" + (tail.isEmpty() ? "" : " " + tail.trim());
            }
            return sql;
        }
    }

    private static List<String> split(String s) {
        List<String> out = new ArrayList<>();
        int depth = 0;
        StringBuilder cur = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                depth++;
                cur.append(ch);
                continue;
            }
            if (ch == ')') {
                depth = Math.max(0, depth - 1);
                cur.append(ch);
                continue;
            }
            if (ch == ',' && depth == 0) {
                out.add(cur.toString().trim());
                cur.setLength(0);
            } else {
                cur.append(ch);
            }
        }
        if (cur.length() > 0) {
            out.add(cur.toString().trim());
        }
        return out;
    }


    private static int max(int a, int b) {
        return a > b ? a : b;
    }

    private static int lastSpaceOut(String s) {
        int d = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if (c == ')') d++;
            else if (c == '(') d = max(0, d - 1);
            else if (c == ' ' && d == 0) return i;
        }
        return -1;
    }

    public String generateSqlFromQuestion(String q) {
        String raw = ai.chat(PROMPT, q);
        String sql = strip(raw);
        sql = removeSemi(sql);
        assertSingle(sql);
        block(sql);
        sql = fixDistinctOrder(sql);
        if (!hasLimit(sql)) sql += " LIMIT 200";
        return sql;
    }
}