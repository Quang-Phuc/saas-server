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

    public DreamService(JdbcTemplate j, OpenAIClient a) {
        this.jdbc = j;
        this.ai = a;
    }

    private static String build(List<Map<String, Object>> t) {
        if (t.isEmpty()) return "Chưa tìm được gợi ý phù hợp từ dữ liệu.";
        StringBuilder sb = new StringBuilder("Gợi ý 2 số: ");
        for (int i = 0; i < t.size(); i++) {
            var r = t.get(i);
            sb.append(r.get("n2"));
            if (i < t.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public Map<String, Object> interpretAndRecommend(String text, String region, int days, int topK) {
        String lower = text.toLowerCase(Locale.ROOT);
        List<Map<String, Object>> syns = jdbc.queryForList(
                "SELECT ds.id AS symbol_id, ds.symbol, COALESCE(ds.category,'') AS category, s.phrase\n" +
                        "FROM dream_synonyms s\n" +
                        "JOIN dream_symbols ds ON ds.id = s.symbol_id\n" +
                        "WHERE ds.active = 1"
        );
        Set<Long> symIds = new LinkedHashSet<>();
        List<Map<String, String>> matched = new ArrayList<>();
        for (var row : syns) {
            long id = ((Number) row.get("symbol_id")).longValue();
            String phrase = String.valueOf(row.get("phrase")).toLowerCase(Locale.ROOT);
            String sym = String.valueOf(row.get("symbol"));
            if (phrase.length() >= 2 && lower.contains(phrase)) {
                symIds.add(id);
                matched.add(Map.of("symbol", sym, "phrase", phrase));
            }
        }
        Map<String, Double> cand = new HashMap<>();
        if (!symIds.isEmpty()) {
            String in = '(' + symIds.stream().map(String::valueOf).collect(Collectors.joining(",")) + ')';
            for (var row : jdbc.queryForList("SELECT symbol_id,n2,weight FROM dream_symbol_n2 WHERE symbol_id IN " + in)) {
                String n2 = String.valueOf(row.get("n2"));
                double w = ((Number) row.get("weight")).doubleValue();
                cand.merge(n2, w, Double::sum);
            }
        }
        if (cand.isEmpty()) {
            String sys = "Bạn là trợ lý giải mã giấc mơ. Trả lời ngắn gọn, trực tiếp, tiếng Việt. Chỉ xuất JSON: {\"symbols\":[{\"name\":\"...\",\"reason\":\"...\"}],\"n2_candidates\":[{\"n2\":\"12\",\"reason\":\"...\"}]}";
            String raw = ai.chat(sys, text);
            try {
                Map<String, Object> j = m.readValue(raw, new TypeReference<Map<String, Object>>() {
                });
                List<Map<String, Object>> n2s = (List<Map<String, Object>>) j.getOrDefault("n2_candidates", List.of());
                for (var it : n2s) {
                    String n2 = String.valueOf(it.get("n2"));
                    if (n2 != null && n2.matches("\\d{2}")) cand.merge(n2, 1.0, Double::sum);
                }
            } catch (Exception ignore) {
            }
        }
        Map<String, Integer> freq = new HashMap<>();
        for (var row : jdbc.queryForList("SELECT r.last2 AS n2, COUNT(*) AS freq\n" +
                "FROM results r\n" +
                "JOIN draws d ON d.id = r.draw_id\n" +
                "WHERE d.region = ?\n" +
                "  AND d.draw_date >= CURDATE() - INTERVAL ? DAY\n" +
                "GROUP BY r.last2", region, days)) {
            freq.put(String.valueOf(row.get("n2")), ((Number) row.get("freq")).intValue());
        }
        int minF = Integer.MAX_VALUE, maxF = Integer.MIN_VALUE;
        for (int f : freq.values()) {
            minF = Math.min(minF, f);
            maxF = Math.max(maxF, f);
        }
        if (minF == Integer.MAX_VALUE) {
            minF = 0;
            maxF = 0;
        }
        double minW = Double.MAX_VALUE, maxW = -Double.MAX_VALUE;
        for (double w : cand.values()) {
            minW = Math.min(minW, w);
            maxW = Math.max(maxW, w);
        }
        if (minW == Double.MAX_VALUE) {
            minW = 0;
            maxW = 0;
        }
        List<Map<String, Object>> scored = new ArrayList<>();
        for (var e : cand.entrySet()) {
            String n2 = e.getKey();
            double w = e.getValue();
            int f = freq.getOrDefault(n2, 0);
            double zW = (maxW == minW) ? (w > 0 ? 1 : 0) : (w - minW) / (maxW - minW);
            double zF = (maxF == minF) ? 0 : (f - minF) * 1.0 / (maxF - minF);
            double score = 0.65 * zW + 0.35 * zF;
            Map<String, Object> row = new HashMap<>();
            row.put("n2", n2);
            row.put("dictWeight", w);
            row.put("freq", f);
            row.put("score", score);
            scored.add(row);
        }
        scored.sort((a, b) -> Double.compare((Double) b.get("score"), (Double) a.get("score")));
        List<Map<String, Object>> top = scored.subList(0, Math.min(topK, scored.size()));
        String answer = build(top);
        try {
            String matchedJson = m.writeValueAsString(matched);
            String candJson = m.writeValueAsString(scored);
            jdbc.update("INSERT INTO dream_user_entries(region,user_id,dream_text,symbols_json,candidates_json,answer_text) VALUES (?,?,?, ?, ?, ?)", region, null, text, matchedJson, candJson, answer);
        } catch (Exception ignore) {
        }
        Map<String, Object> res = new HashMap<>();
        res.put("type", "dream");
        res.put("top", top);
        res.put("matchedSymbols", matched);
        res.put("answer", answer);
        return res;
    }
}
