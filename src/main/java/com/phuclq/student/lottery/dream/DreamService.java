package com.phuclq.student.lottery.dream;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phuclq.student.lottery.openai.OpenAIClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DreamService {
    private final JdbcTemplate jdbc;
    private final OpenAIClient ai;
    private final ObjectMapper m = new ObjectMapper();

    public DreamService(JdbcTemplate j, OpenAIClient a){ this.jdbc=j; this.ai=a; }

    public Map<String,Object> interpretAndRecommend(String text, String region, int days, int topK, boolean verbose) {
        String lower = text.toLowerCase(Locale.ROOT);

        // 1) Match synonyms -> symbols
        String sql = String.join(" ",
                "SELECT ds.id AS symbol_id,",
                "       ds.symbol,",
                "       COALESCE(ds.category, '') AS category,",
                "       s.phrase",
                "FROM dream_synonyms s",
                "JOIN dream_symbols ds ON ds.id = s.symbol_id",
                "WHERE ds.active = 1"
        );

        List<Map<String, Object>> syns = jdbc.queryForList(sql);

        Set<Long> symbolIds = new LinkedHashSet<>();
        List<Map<String,String>> matched = new ArrayList<>();
        for (var row : syns) {
            long id = ((Number)row.get("symbol_id")).longValue();
            String phrase = String.valueOf(row.get("phrase")).toLowerCase(Locale.ROOT);
            String sym = String.valueOf(row.get("symbol"));
            if (phrase.length() >= 2 && lower.contains(phrase)) {
                symbolIds.add(id);
                matched.add(Map.of("symbol", sym, "phrase", phrase));
            }
        }

        // 2) Build candidates from dictionary weights
        Map<String, Double> cand = new HashMap<>();
        if (!symbolIds.isEmpty()) {
            String in = symbolIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            for (var row : jdbc.queryForList("SELECT symbol_id,n2,weight FROM dream_symbol_n2 WHERE symbol_id IN ("+in+")")) {
                String n2 = String.valueOf(row.get("n2"));
                double w = ((Number)row.get("weight")).doubleValue();
                cand.merge(n2, w, Double::sum);
            }
        }

        // 3) Fallback AI if nothing matched
        if (cand.isEmpty()) {
            String sys = "Bạn là trợ lý giải mã giấc mơ. Trả lời ngắn gọn, tiếng Việt. " +
                    "Chỉ xuất JSON: {\"symbols\":[{\"name\":\"...\",\"reason\":\"...\"}],\"n2_candidates\":[{\"n2\":\"12\",\"reason\":\"...\"}]}";
            String raw = ai.chat(sys, text);
            try {
                Map<String,Object> j = m.readValue(raw, new TypeReference<Map<String,Object>>(){});
                List<Map<String,Object>> n2s = (List<Map<String,Object>>) j.getOrDefault("n2_candidates", List.of());
                for (var it : n2s) {
                    Object val = it.get("n2");
                    if (val != null) {
                        String n2 = String.valueOf(val);
                        if (n2.matches("\\d{2}")) cand.merge(n2, 1.0, Double::sum);
                    }
                }
            } catch (Exception ignore) { }
        }

        // 4) Overlay hotness (freq in last N days)
        Map<String,Integer> freq = new HashMap<>();
        for (var row : jdbc.queryForList("SELECT r.last2 AS n2, COUNT(*) AS freq " +
                "FROM results r " +
                "JOIN draws d ON d.id = r.draw_id " +
                "WHERE d.region = ? " +
                "  AND d.draw_date >= CURDATE() - INTERVAL ? DAY " +
                "GROUP BY r.last2", region, days)) {
            freq.put(String.valueOf(row.get("n2")), ((Number)row.get("freq")).intValue());
        }

        // 4.1) Overdue (days since last seen) for each last2 in region (no time bound)
        Map<String,Integer> overdueDays = new HashMap<>();
        for (var row : jdbc.queryForList("SELECT r.last2 AS n2, DATEDIFF(CURDATE(), MAX(d.draw_date)) AS days_overdue " +
                "FROM results r " +
                "JOIN draws d ON d.id = r.draw_id " +
                "WHERE d.region = ? " +
                "GROUP BY r.last2", region)) {
            overdueDays.put(String.valueOf(row.get("n2")), ((Number)row.get("days_overdue")).intValue());
        }

        // 5) Score normalization
        int minF = Integer.MAX_VALUE, maxF = Integer.MIN_VALUE;
        for (int f : freq.values()) { minF = Math.min(minF,f); maxF = Math.max(maxF,f); }
        if (minF == Integer.MAX_VALUE) { minF = 0; maxF = 0; }

        double minW = Double.MAX_VALUE, maxW = -Double.MAX_VALUE;
        for (double w : cand.values()) { minW = Math.min(minW,w); maxW = Math.max(maxW,w); }
        if (minW == Double.MAX_VALUE) { minW = 0; maxW = 0; }

        List<Map<String,Object>> scored = new ArrayList<>();
        for (var e : cand.entrySet()) {
            String n2 = e.getKey();
            double w = e.getValue();
            int f = freq.getOrDefault(n2, 0);
            double zW = (maxW==minW) ? (w>0?1:0) : (w-minW)/(maxW-minW);
            double zF = (maxF==minF) ? 0 : (f-minF)*1.0/(maxF-minF);
            // Bonus nhẹ cho "đã lâu chưa về" (overdue lớn) nếu bạn muốn cân cold-number
            int od = overdueDays.getOrDefault(n2, 0);
            double zO = Math.min(1.0, od / 60.0); // cap ở 60 ngày => 1.0
            double score = 0.60*zW + 0.30*zF + 0.10*zO;

            Map<String,Object> row = new HashMap<>();
            row.put("n2", n2);
            row.put("dictWeight", w);
            row.put("freq", f);
            row.put("overdueDays", od);
            row.put("score", score);
            scored.add(row);
        }

        scored.sort((a,b)->Double.compare((Double)b.get("score"), (Double)a.get("score")));
        List<Map<String,Object>> top = scored.subList(0, Math.min(topK, scored.size()));

        String answer = verbose
                ? buildNarrative(text, matched, region, days, top, !symbolIds.isEmpty())
                : buildShort(top);

        // 6) Log (best-effort)
        try {
            String matchedJson = m.writeValueAsString(matched);
            String candJson = m.writeValueAsString(scored);
            jdbc.update("INSERT INTO dream_user_entries(region,user_id,dream_text,symbols_json,candidates_json,answer_text) VALUES (?,?,?,?,?,?)",
                    region, null, text, matchedJson, candJson, answer);
        } catch (Exception ignore) {}

        Map<String,Object> res = new HashMap<>();
        res.put("type","dream");
        res.put("top", top);
        res.put("matchedSymbols", matched);
        res.put("answer", answer);
        return res;
    }

    private static String buildShort(List<Map<String,Object>> t) {
        if (t.isEmpty()) return "Chưa tìm được gợi ý phù hợp từ dữ liệu.";
        StringBuilder sb = new StringBuilder("Gợi ý 2 số: ");
        for (int i = 0; i < t.size(); i++) {
            var r = t.get(i);
            sb.append(r.get("n2"));
            if (i < t.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    private static String joinSymbols(List<Map<String,String>> matched){
        if (matched.isEmpty()) return "";
        // nhóm theo symbol -> liệt kê phrase
        Map<String,List<String>> by = new LinkedHashMap<>();
        for (var m: matched){
            by.computeIfAbsent(m.get("symbol"), k-> new ArrayList<>()).add(m.get("phrase"));
        }
        List<String> parts = new ArrayList<>();
        for (var e: by.entrySet()){
            String sym = e.getKey();
            String phrases = e.getValue().stream().distinct().collect(Collectors.joining(", "));
            parts.add(sym + " (" + phrases + ")");
        }
        return String.join("; ", parts);
    }

    private static String buildNarrative(String userText,
                                         List<Map<String,String>> matched,
                                         String region, int days,
                                         List<Map<String,Object>> top,
                                         boolean hasDictMatch) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bạn mơ thấy gì: \"").append(userText).append("\".\n");

        if (hasDictMatch && !matched.isEmpty()){
            sb.append("Mình nhận ra các biểu tượng liên quan: ")
                    .append(joinSymbols(matched))
                    .append(".\n");
            sb.append("Dựa trên từ điển giấc mơ nội bộ (mapping biểu tượng → lô 2 số, có trọng số),\n");
        } else {
            sb.append("Không tìm thấy biểu tượng khớp trực tiếp trong từ điển, mình dùng suy luận và thống kê để gợi ý.\n");
        }

        sb.append("Ngoài ra, mình tham chiếu thống kê gần đây tại vùng ").append(region)
                .append(" trong ").append(days).append(" ngày để cân bằng giữa độ liên quan và tần suất/độ \"lâu chưa về\".\n\n");

        if (top.isEmpty()){
            sb.append("Hiện chưa có gợi ý phù hợp từ dữ liệu.");
            return sb.toString();
        }

        sb.append("**Phân tích nhanh từng số ứng viên**:\n");
        int rank = 1;
        for (var r : top){
            String n2 = String.valueOf(r.get("n2"));
            double w  = ((Number)r.get("dictWeight")).doubleValue();
            int f     = ((Number)r.get("freq")).intValue();
            int od    = ((Number)r.get("overdueDays")).intValue();
            double sc = ((Number)r.get("score")).doubleValue();
            sb.append("- #").append(rank++).append(" → ").append(n2)
                    .append(": weight=").append(String.format(Locale.US,"%.2f", w))
                    .append(", freq=").append(f)
                    .append(", overdue≈").append(od).append(" ngày")
                    .append(", score=").append(String.format(Locale.US,"%.3f", sc))
                    .append(".\n");
        }

        sb.append("\nKết luận: Nên theo tính toán tổng hợp, gợi ý 2 số là: ");
        for (int i=0;i<top.size();i++){
            sb.append(top.get(i).get("n2"));
            if (i<top.size()-1) sb.append(", ");
        }
        sb.append(". Chúc may mắn!\n");

        return sb.toString();
    }
}
