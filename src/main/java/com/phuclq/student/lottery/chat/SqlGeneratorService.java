package com.phuclq.student.lottery.chat;

import com.phuclq.student.lottery.openai.OpenAIClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SqlGeneratorService {

    private final OpenAIClient openAIClient;

    public SqlGeneratorService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    // Từ khóa cấm (mở rộng hơn)
    private static final Set<String> BANNED = Set.of(
            "insert","update","delete","alter","drop","create","grant","revoke",
            "truncate","replace","call","load","outfile","infile",
            "lock","unlock","set","use","rename","analyze","optimize"
    );

    // Prompt: thêm quy tắc tránh DISTINCT + ORDER BY sai
    private static final String SYSTEM_PROMPT =
            "Bạn là trợ lý sinh SQL cho MySQL.\n" +
                    "YÊU CẦU:\n" +
                    "- Chỉ trả về đúng một câu lệnh SQL duy nhất, bắt đầu bằng SELECT.\n" +
                    "- Không bao gồm giải thích, không kèm ``` hoặc ngôn ngữ.\n" +
                    "- Chỉ dùng các bảng/cột sau:\n" +
                    "  - draws(id, region, province, draw_date, game)\n" +
                    "  - results(id, draw_id, prize_name, seq, number, digits, last2, last3)\n" +
                    "  - n2(n)\n" +
                    "Quy ước:\n" +
                    "  - 'đề' = last2 của prize_name='DB'\n" +
                    "  - 'lô 2 số' = last2 của mọi prize_name\n" +
                    "  - 'xiên 2' = 2 số last2 khác nhau cùng xuất hiện trong 1 kỳ (tính DISTINCT trong 1 kỳ)\n" +
                    "Mặc định:\n" +
                    "  - Nếu người dùng không nói rõ: region='MB', thời gian 30 ngày gần nhất.\n" +
                    "Ràng buộc an toàn & cú pháp:\n" +
                    "  - CHỈ SELECT; cấm mọi lệnh sửa dữ liệu.\n" +
                    "  - Không dùng SELECT DISTINCT kèm ORDER BY theo cột KHÔNG có trong SELECT (MySQL sẽ lỗi 3065).\n" +
                    "    Nếu cần lọc unique rồi vẫn sắp theo 'lần xuất hiện gần nhất', hãy dùng GROUP BY và\n" +
                    "    MAX(d.draw_date) AS last_seen, sau đó ORDER BY last_seen DESC.\n";

    private static final Pattern FIRST_SELECT = Pattern.compile("(?is)\\bselect\\b[\\s\\S]*");
    private static final int MAX_SQL_CHARS = 12000;

    public String generateSqlFromQuestion(String question) {
        String raw = openAIClient.chat(SYSTEM_PROMPT, question);

        String sql = stripFencesAndExtractFirstSelect(raw);
        sql = removeTrailingSemicolon(sql);
        assertSingleStatement(sql);
        blockDangerousKeywords(sql);

        // cố gắng sửa lỗi DISTINCT + ORDER BY (nếu có)
        sql = fixDistinctOrderByIfNeeded(sql);

        // nếu vẫn chưa có LIMIT → thêm LIMIT 200 (để query không nổ do quá lớn)
        if (!hasLimit(sql)) {
            sql = sql + " LIMIT 200";
        }

        // tránh quá dài (bảo vệ)
        if (sql.length() > MAX_SQL_CHARS) {
            throw new RuntimeException("Generated SQL too long.");
        }
        return sql;
    }

    /* -------------------------- Helpers -------------------------- */

    private static String stripFencesAndExtractFirstSelect(String s) {
        if (s == null) throw new RuntimeException("Empty response from LLM.");
        String out = s.replace("```sql", "").replace("```", "").trim();
        Matcher m = FIRST_SELECT.matcher(out);
        if (m.find()) {
            out = m.group().trim();
        }
        if (!out.toLowerCase(Locale.ROOT).startsWith("select")) {
            throw new RuntimeException("Only SELECT is allowed.");
        }
        return out;
    }

    private static String removeTrailingSemicolon(String s) {
        String x = s.trim();
        while (x.endsWith(";")) x = x.substring(0, x.length()-1).trim();
        return x;
    }

    private static void assertSingleStatement(String s) {
        // Không cho ; ở giữa (tránh multi-statement)
        int semi = s.indexOf(';');
        if (semi >= 0) {
            throw new RuntimeException("Multiple statements are not allowed.");
        }
    }

    private static void blockDangerousKeywords(String sql) {
        String lower = sql.toLowerCase(Locale.ROOT);
        for (String b : BANNED) {
            // match theo từ, hoặc các pattern đặc thù (outfile/infile đã thêm ở set)
            if (lower.matches("(?is).*\\b" + Pattern.quote(b) + "\\b.*")) {
                throw new RuntimeException("Blocked SQL keyword detected: " + b);
            }
        }
    }

    private static boolean hasLimit(String sql) {
        return sql.toLowerCase(Locale.ROOT).matches("(?s).*\\blimit\\s+\\d+\\s*$");
    }

    /**
     * Auto-fix lỗi kiểu:
     *  SELECT DISTINCT <cols> ... ORDER BY <expr not in select> [LIMIT n]
     * Chiến lược:
     *  - Bóc tách SELECT list, FROM... (đến trước ORDER BY/LIMIT), ORDER BY expr(s), LIMIT.
     *  - Dựng subquery:
     *      SELECT DISTINCT <selectList>, (<obExpr1>) AS _ob1, ... FROM <fromPart>
     *    rồi bọc ngoài:
     *      SELECT * FROM (...) t ORDER BY t._ob1 [ASC|DESC], ... [LIMIT ...]
     *  - Nếu không tách được → fallback: nếu có DISTINCT + ORDER BY → đổi ORDER BY thành ORDER BY 1
     */
    private static String fixDistinctOrderByIfNeeded(String sql) {
        String lower = sql.toLowerCase(Locale.ROOT);
        if (!(lower.contains("select distinct") && lower.contains(" order by "))) {
            return sql; // không phải case cần sửa
        }

        try {
            // vị trí chính
            int selectIdx = lower.indexOf("select distinct");
            int fromIdx = indexOfWord(lower, " from ", selectIdx);
            if (fromIdx < 0) return sql; // không tìm thấy FROM -> bỏ qua

            // select list là phần giữa 'select distinct' và 'from'
            String selectList = sql.substring(selectIdx + "select distinct".length(), fromIdx).trim();

            // tìm ORDER BY và LIMIT (cuối cùng — ưu tiên order by/limit cuối câu)
            int orderIdx = lastIndexOfWord(lower, " order by ");
            int limitIdx = lastIndexOfWord(lower, " limit ");

            // fromPart = "FROM ... (đến trước ORDER/LIMIT)"
            int endOfFromPart = (orderIdx >= 0 ? orderIdx : (limitIdx >= 0 ? limitIdx : sql.length()));
            String fromPart = sql.substring(fromIdx, endOfFromPart).trim();

            // lấy orderExpr (không gồm LIMIT)
            String orderExpr = (orderIdx >= 0)
                    ? sql.substring(orderIdx + " order by ".length(), (limitIdx > orderIdx ? limitIdx : sql.length())).trim()
                    : "";

            // lấy limit (nếu có)
            String limitClause = (limitIdx >= 0) ? sql.substring(limitIdx).trim() : "";

            if (orderExpr.isEmpty()) {
                // không có ORDER BY thực sự -> không cần sửa
                return sql;
            }

            // tách các biểu thức ORDER BY (theo dấu phẩy, tôn trọng ngoặc)
            List<String> orderItems = splitOrderItems(orderExpr);
            // danh sách alias và select thêm cho inner
            List<String> obAliasesInSelect = new ArrayList<>();
            List<String> obAliasesInOrder = new ArrayList<>();
            for (int i = 0; i < orderItems.size(); i++) {
                String item = orderItems.get(i).trim();
                // tách direction
                String dir = "";
                String itemNoDir = item;
                int sp = lastSpaceOutsideParens(item);
                if (sp > 0) {
                    String tail = item.substring(sp + 1).trim();
                    if (tail.equalsIgnoreCase("asc") || tail.equalsIgnoreCase("desc")) {
                        dir = " " + tail.toUpperCase(Locale.ROOT);
                        itemNoDir = item.substring(0, sp).trim();
                    }
                }
                String alias = "_ob" + (i + 1);
                obAliasesInSelect.add(itemNoDir + " AS " + alias);
                obAliasesInOrder.add("t." + alias + dir);
            }

            // Lắp inner + outer
            String inner = "SELECT DISTINCT " + selectList +
                    (obAliasesInSelect.isEmpty() ? "" : ", " + String.join(", ", obAliasesInSelect)) +
                    " " + fromPart;

            String outer = "SELECT * FROM (" + inner + ") t ORDER BY " +
                    String.join(", ", obAliasesInOrder) +
                    (limitClause.isEmpty() ? "" : " " + limitClause);

            return outer;

        } catch (Exception ex) {
            // Fallback: thay ORDER BY ... -> ORDER BY 1 (đảm bảo không lỗi)
            // (ưu tiên không nổ lỗi hơn là giữ nguyên thứ tự)
            int orderIdx = lastIndexOfWord(lower, " order by ");
            if (orderIdx >= 0) {
                int limitIdx = lastIndexOfWord(lower, " limit ");
                String head = sql.substring(0, orderIdx);
                String tail = (limitIdx >= 0) ? sql.substring(limitIdx) : "";
                return head + " ORDER BY 1" + (tail.isEmpty() ? "" : " " + tail.trim());
            }
            return sql;
        }
    }

    /* -------------------------- Tiny parsing utils -------------------------- */

    private static int indexOfWord(String lowerHaystack, String lowerNeedle, int fromIndex) {
        int i = lowerHaystack.indexOf(lowerNeedle, fromIndex);
        return i;
    }

    private static int lastIndexOfWord(String lowerHaystack, String lowerNeedle) {
        return lowerHaystack.lastIndexOf(lowerNeedle);
    }

    // Tách ORDER BY theo dấu phẩy nhưng tôn trọng ngoặc
    private static List<String> splitOrderItems(String orderExpr) {
        List<String> out = new ArrayList<>();
        int depth = 0;
        StringBuilder cur = new StringBuilder();
        for (int i = 0; i < orderExpr.length(); i++) {
            char c = orderExpr.charAt(i);
            if (c == '(') depth++;
            if (c == ')') depth = Math.max(0, depth - 1);
            if (c == ',' && depth == 0) {
                out.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        if (cur.length() > 0) out.add(cur.toString());
        return out;
    }

    // Tìm khoảng trắng cuối cùng ngoài ngoặc — để bắt 'ASC/DESC'
    private static int lastSpaceOutsideParens(String s) {
        int depth = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if (c == ')') depth++;
            else if (c == '(') depth = Math.max(0, depth - 1);
            else if (c == ' ' && depth == 0) return i;
        }
        return -1;
    }
}
