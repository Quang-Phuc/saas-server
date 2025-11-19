package com.phuclq.student.lottery.recommend;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecommendService {
    private final JdbcTemplate jdbc;

    public RecommendService(JdbcTemplate j) {
        this.jdbc = j;
    }

    public List<Map<String, Object>> recommend(String region, int days, int k) {
        List<Map<String, Object>> freq = jdbc.queryForList("SELECT r.last2 AS n2, COUNT(*) AS freq FROM results r JOIN draws d ON d.id=r.draw_id WHERE d.region=? AND d.draw_date >= CURDATE() - INTERVAL ? DAY GROUP BY r.last2", region, days);
        Map<String, Integer> fMap = new HashMap<>();
        int minF = Integer.MAX_VALUE, maxF = Integer.MIN_VALUE;
        for (var row : freq) {
            String n2 = String.valueOf(row.get("n2"));
            int v = ((Number) row.get("freq")).intValue();
            fMap.put(n2, v);
            minF = Math.min(minF, v);
            maxF = Math.max(maxF, v);
        }
        List<Map<String, Object>> scored = new ArrayList<>();
        for (var e : fMap.entrySet()) {
            String n2 = e.getKey();
            int f = e.getValue();
            double z = (maxF == minF) ? 0.0 : ((f - minF) * 1.0 / (maxF - minF));
            Map<String, Object> m = new HashMap<>();
            m.put("n2", n2);
            m.put("freqIn" + days + "Days", f);
            m.put("score", z);
            scored.add(m);
        }
        scored.sort((a, b) -> Double.compare((Double) b.get("score"), (Double) a.get("score")));
        return scored.subList(0, Math.min(k, scored.size()));
    }
}
