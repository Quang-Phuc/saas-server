package com.phuclq.student.lottery.dream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phuclq.student.lottery.openai.OpenAIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DreamService {
    private final JdbcTemplate jdbc;
    private final OpenAIClient ai;
    private final ObjectMapper m = new ObjectMapper();

    // Flags cấu hình
    private final boolean useAiBeforeDict;  // AI trích symbol/n2 trước khi dò từ điển
    private final boolean useAiNarrative;   // AI viết narrative cuối
    private final double aiWeightScale;     // trọng số cho n2 từ AI

    public DreamService(
            JdbcTemplate j,
            OpenAIClient a,
            @Value("${ai.dream.useAiBeforeDict:true}") boolean useAiBeforeDict,
            @Value("${ai.dream.useAiNarrative:false}") boolean useAiNarrative,
            @Value("${ai.dream.aiWeightScale:1.0}") double aiWeightScale
    ) {
        this.jdbc = j;
        this.ai = a;
        this.useAiBeforeDict = useAiBeforeDict;
        this.useAiNarrative = useAiNarrative;
        this.aiWeightScale = aiWeightScale <= 0 ? 1.0 : aiWeightScale;
    }

    /**
     * @param text    mô tả giấc mơ người dùng
     * @param region  MB|MN|MT
     * @param days    số ngày tính hotness
     * @param topK    số lượng n2 trả về
     * @param verbose có in phân tích chi tiết không (nếu useAiNarrative=false)
     */
    public Map<String,Object> interpretAndRecommend(String text, String region, int days, int topK, boolean verbose) {
        String lower = safe(text).toLowerCase(Locale.ROOT).trim();

        // ========= 0) (tuỳ chọn) AI trích symbol & n2 trước =========
        // candAi: key=n2, value=weight (đã nhân aiWeightScale)
        Map<String, Double> candAi = new HashMap<>();
        List<String> aiSymbols = new ArrayList<>();
        if (useAiBeforeDict) {
            extractFromAI(lower, candAi, aiSymbols);
        }

        // ========= 1) Match synonyms -> symbols =========
        String sql = String.join(" ",
                "SELECT ds.id AS symbol_id, ds.symbol, COALESCE(ds.category,'') AS category, s.phrase",
                "FROM dream_synonyms s",
                "JOIN dream_symbols ds ON ds.id = s.symbol_id",
                "WHERE ds.active = 1"
        );
        List<Map<String, Object>> syns = jdbc.queryForList(sql);

        Set<Long> symbolIds = new LinkedHashSet<>();
        List<Map<String,String>> matched = new ArrayList<>();
        for (var row : syns) {
            long id = ((Number)row.get("symbol_id")).longValue();
            String phrase = safe(row.get("phrase")).toLowerCase(Locale.ROOT);
            String sym = safe(row.get("symbol"));
            if (phrase.length() >= 2 && lower.contains(phrase)) {
                symbolIds.add(id);
                matched.add(Map.of("symbol", sym, "phrase", phrase));
            }
        }

        // 1.1) Map AI symbols -> symbolIds (match theo symbol & synonym)
        if (!aiSymbols.isEmpty()) {
            // match trực tiếp by symbol
            List<Map<String,Object>> symRows = safeInLowerList(
                    "SELECT id, symbol FROM dream_symbols WHERE active=1 AND LOWER(symbol) IN (%s)",
                    aiSymbols
            );
            for (var row : symRows) {
                long id = ((Number)row.get("id")).longValue();
                String sym = safe(row.get("symbol"));
                symbolIds.add(id);
                matched.add(Map.of("symbol", sym, "phrase", "[AI-symbol]"));
            }
            // match theo synonym
            List<Map<String,Object>> synRows = safeInLowerList(
                    "SELECT ds.id AS symbol_id, ds.symbol, s.phrase " +
                            "FROM dream_synonyms s JOIN dream_symbols ds ON ds.id=s.symbol_id " +
                            "WHERE ds.active=1 AND LOWER(s.phrase) IN (%s)",
                    aiSymbols
            );
            for (var row : synRows) {
                long id = ((Number)row.get("symbol_id")).longValue();
                String sym = safe(row.get("symbol"));
                String phrase = safe(row.get("phrase"));
                symbolIds.add(id);
                matched.add(Map.of("symbol", sym, "phrase", phrase + " [AI]"));
            }
        }

        // ========= 2) Build candidates từ dictionary weights =========
        Map<String, Double> cand = new HashMap<>(candAi); // bắt đầu với AI candidates nếu có
        if (!symbolIds.isEmpty()) {
            String inIds = symbolIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            for (var row : jdbc.queryForList(
                    "SELECT n2, weight FROM dream_symbol_n2 WHERE symbol_id IN (" + inIds + ")")) {
                String n2 = safe(row.get("n2"));
                double w = asDouble(row.get("weight"), 0.0);
                if (n2.matches("\\d{2}")) {
                    cand.merge(n2, w, Double::sum);
                }
            }
        }

        // ========= 3) Fallback AI nếu chưa có candidate nào =========
        if (cand.isEmpty()) {
            extractFromAI(lower, cand, aiSymbols); // thêm lần nữa (an toàn)
        }

        // ========= 4) Overlay hotness (freq trong N ngày) & overdue =========
        Map<String,Integer> freq = new HashMap<>();
        for (var row : jdbc.queryForList(
                "SELECT r.last2 AS n2, COUNT(*) AS freq " +
                        "FROM results r " +
                        "JOIN draws d ON d.id = r.draw_id " +
                        "WHERE d.region = ? AND d.draw_date >= CURDATE() - INTERVAL ? DAY " +
                        "GROUP BY r.last2", region, days)) {
            freq.put(safe(row.get("n2")), asInt(row.get("freq"), 0));
        }

        Map<String,Integer> overdueDays = new HashMap<>();
        for (var row : jdbc.queryForList(
                "SELECT r.last2 AS n2, DATEDIFF(CURDATE(), MAX(d.draw_date)) AS days_overdue " +
                        "FROM results r JOIN draws d ON d.id = r.draw_id " +
                        "WHERE d.region = ? GROUP BY r.last2", region)) {
            overdueDays.put(safe(row.get("n2")), asInt(row.get("days_overdue"), 0));
        }

        // ========= 5) Chuẩn hoá & chấm điểm =========
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
            int od = overdueDays.getOrDefault(n2, 0);
            double zO = Math.min(1.0, od / 60.0); // cap 60 ngày
            double score = 0.60*zW + 0.30*zF + 0.10*zO;

            Map<String,Object> row = new LinkedHashMap<>();
            row.put("n2", n2);
            row.put("dictWeight", w);
            row.put("freq", f);
            row.put("overdueDays", od);
            row.put("score", score);
            scored.add(row);
        }

        scored.sort((a,b)->Double.compare((Double)b.get("score"), (Double)a.get("score")));
        List<Map<String,Object>> top = scored.subList(0, Math.min(topK, scored.size()));

        // ========= 6) Render answer =========
        String answer;
        if (useAiNarrative) {
            answer = buildAiNarrative(text, region, days, matched, top);
        } else {
            answer = verbose ? buildNarrative(text, matched, region, days, top, !symbolIds.isEmpty() || !aiSymbols.isEmpty())
                    : buildShort(top);
        }

        // ========= 7) Log (best-effort) =========
        try {
            String matchedJson = m.writeValueAsString(matched);
            String candJson = m.writeValueAsString(scored);
            jdbc.update("INSERT INTO dream_user_entries(region,user_id,dream_text,symbols_json,candidates_json,answer_text) " +
                            "VALUES (?,?,?,?,?,?)",
                    region, null, text, matchedJson, candJson, answer);
        } catch (Exception ignore) {}

        Map<String,Object> res = new LinkedHashMap<>();
        res.put("type","dream");
        res.put("top", top);
        res.put("matchedSymbols", matched);
        res.put("answer", answer);
        return res;
    }

    // ======================= Helpers =======================

    private void extractFromAI(String userTextLower,
                               Map<String, Double> candOut,
                               List<String> aiSymbolsOut) {
        try {
            String sys = "Bạn là trợ lý giải mã giấc mơ. Chỉ trả JSON đúng schema: " +
                    "{\"symbols\":[{\"name\":\"...\",\"reason\":\"...\",\"confidence\":0..1}]," +
                    "\"n2_candidates\":[{\"n2\":\"12\",\"reason\":\"...\",\"confidence\":0..1}]}";
            String raw = ai.chat(sys, userTextLower);
            if (raw == null || raw.isBlank()) return;

            Map<String,Object> j = m.readValue(raw, new TypeReference<Map<String,Object>>(){});
            List<Map<String,Object>> syms =
                    (List<Map<String,Object>>) j.getOrDefault("symbols", List.of());
            for (var it : syms) {
                String name = safe(it.get("name")).toLowerCase(Locale.ROOT);
                if (!name.isBlank()) aiSymbolsOut.add(name);
            }
            List<Map<String,Object>> n2s =
                    (List<Map<String,Object>>) j.getOrDefault("n2_candidates", List.of());
            for (var it : n2s) {
                String n2 = safe(it.get("n2"));
                if (n2.matches("\\d{2}")) {
                    double conf = asDouble(it.get("confidence"), 1.0);
                    double weight = Math.max(0.1, Math.min(1.0, conf)) * aiWeightScale;
                    candOut.merge(n2, weight, Double::sum);
                }
            }
        } catch (Exception ignore) {
            // Không làm gián đoạn pipeline
        }
    }

    private List<Map<String,Object>> safeInLowerList(String sqlFmt, List<String> values) {
        if (values.isEmpty()) return List.of();
        String placeholders = values.stream().map(v->"?").collect(Collectors.joining(","));
        String sql = String.format(sqlFmt, placeholders);

        List<Object> args = values.stream()
                .map(v -> v == null ? "" : v.toLowerCase(Locale.ROOT))
                .collect(Collectors.toList());

        return jdbc.queryForList(sql, args.toArray());
    }

    private static String safe(Object o){
        return (o == null) ? "" : String.valueOf(o);
    }
    private static int asInt(Object o, int def){
        if (o instanceof Number) return ((Number)o).intValue();
        try { return Integer.parseInt(String.valueOf(o)); } catch(Exception e){ return def; }
    }
    private static double asDouble(Object o, double def){
        if (o instanceof Number) return ((Number)o).doubleValue();
        try { return Double.parseDouble(String.valueOf(o)); } catch(Exception e){ return def; }
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
        Map<String,List<String>> by = new LinkedHashMap<>();
        for (var mm: matched){
            by.computeIfAbsent(mm.get("symbol"), k-> new ArrayList<>()).add(mm.get("phrase"));
        }
        List<String> parts = new ArrayList<>();
        for (var e: by.entrySet()){
            String sym = e.getKey();
            String phrases = e.getValue().stream().filter(Objects::nonNull).map(String::valueOf)
                    .distinct().collect(Collectors.joining(", "));
            parts.add(sym + " (" + phrases + ")");
        }
        return String.join("; ", parts);
    }

    private static String buildNarrative(String userText,
                                         List<Map<String,String>> matched,
                                         String region, int days,
                                         List<Map<String,Object>> top,
                                         boolean hasDictOrAiMatch) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bạn mơ thấy gì: \"").append(userText).append("\".\n");

        if (hasDictOrAiMatch && !matched.isEmpty()){
            sb.append("Nhận diện biểu tượng: ")
                    .append(joinSymbols(matched))
                    .append(".\nDựa trên từ điển giấc mơ (cộng dồn trọng số) và thống kê gần đây,\n");
        } else {
            sb.append("Không khớp trực tiếp nhiều biểu tượng từ điển; dùng thống kê & suy luận để gợi ý.\n");
        }

        sb.append("Tham chiếu thống kê vùng ").append(region)
                .append(" trong ").append(days).append(" ngày để cân bằng giữa độ liên quan và tần suất/độ \"lâu chưa về\".\n\n");

        if (top.isEmpty()){
            sb.append("Hiện chưa có gợi ý phù hợp từ dữ liệu.");
            return sb.toString();
        }

        sb.append("**Phân tích nhanh**:\n");
        int rank = 1;
        for (var r : top){
            String n2 = String.valueOf(r.get("n2"));
            double w  = asDouble(r.get("dictWeight"), 0.0);
            int f     = asInt(r.get("freq"), 0);
            int od    = asInt(r.get("overdueDays"), 0);
            double sc = asDouble(r.get("score"), 0.0);
            sb.append("- #").append(rank++).append(" ").append(n2)
                    .append(" | weight=").append(String.format(Locale.US,"%.2f", w))
                    .append(" | freq=").append(f)
                    .append(" | overdue≈").append(od).append(" ngày")
                    .append(" | score=").append(String.format(Locale.US,"%.3f", sc))
                    .append("\n");
        }

        sb.append("\nKết luận: Gợi ý 2 số: ");
        for (int i=0;i<top.size();i++){
            sb.append(top.get(i).get("n2"));
            if (i<top.size()-1) sb.append(", ");
        }
        sb.append(".");
        return sb.toString();
    }

    private String buildAiNarrative(String userText,
                                    String region, int days,
                                    List<Map<String,String>> matched,
                                    List<Map<String,Object>> top) {
        try {
            Map<String,Object> payload = new LinkedHashMap<>();
            payload.put("userText", userText);
            payload.put("region", region);
            payload.put("days", days);
            payload.put("matched", matched);
            payload.put("top", top);

            String sys = "Bạn là trợ lý giải mã giấc mơ. Viết ngắn gọn, rõ ràng, tiếng Việt; " +
                    "CHỈ dựa trên JSON được cung cấp, không bịa thêm.";
            String user = "DỮ LIỆU:\n" + m.writeValueAsString(payload) +
                    "\n\nYÊU CẦU:\n- Tóm tắt giấc mơ, nêu symbol đã nhận diện (nếu có)." +
                    "\n- Giải thích tiêu chí chọn (trọng số, tần suất, overdue nếu có)." +
                    "\n- Xuất danh sách 3–5 số đầu tiên, định dạng gọn.";

            String out = ai.chat(sys, user);
            if (out != null && !out.isBlank()) return out.trim();
        } catch (Exception ignore) {}
        // fallback nếu AI lỗi
        return buildNarrative(userText, matched, region, days, top, !matched.isEmpty());
    }
}
