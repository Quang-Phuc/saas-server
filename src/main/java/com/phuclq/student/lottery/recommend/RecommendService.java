package com.phuclq.student.lottery.recommend;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendService {

    private final JdbcTemplate jdbc;

    public RecommendService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Map<String,Object>> recommend(String region, int days, int k) {
        // Frequency in the last N days
        List<Map<String,Object>> freq = jdbc.queryForList(
                "SELECT r.last2 AS n2, COUNT(*) AS freq\n" +
                        "FROM results r JOIN draws d ON d.id = r.draw_id\n" +
                        "WHERE d.region = ? AND d.draw_date >= CURDATE() - INTERVAL ? DAY\n" +
                        "GROUP BY r.last2",
                region, days
        );


        // Overdue (days since last seen across full history)
        List<Map<String,Object>> overdue = jdbc.queryForList(
                "SELECT n.n AS n2, DATEDIFF(CURDATE(), MAX(d.draw_date)) AS days_since\n" +
                        "FROM n2 n\n" +
                        "LEFT JOIN results r ON r.last2 = n.n\n" +
                        "LEFT JOIN draws d ON d.id = r.draw_id AND d.region = ?\n" +
                        "GROUP BY n.n",
                region
        );


        Map<String,Integer> fMap = new HashMap<>();
        int minF = Integer.MAX_VALUE, maxF = Integer.MIN_VALUE;
        for (var row : freq) {
            String n2 = String.valueOf(row.get("n2"));
            int v = ((Number) row.get("freq")).intValue();
            fMap.put(n2, v);
            minF = Math.min(minF, v); maxF = Math.max(maxF, v);
        }

        Map<String,Integer> dMap = new HashMap<>();
        int minD = Integer.MAX_VALUE, maxD = Integer.MIN_VALUE;
        for (var row : overdue) {
            String n2 = String.valueOf(row.get("n2"));
            int v = row.get("days_since") == null ? 0 : ((Number) row.get("days_since")).intValue();
            dMap.put(n2, v);
            minD = Math.min(minD, v); maxD = Math.max(maxD, v);
        }

        double wF = 0.7, wD = 0.3;
        List<Map<String,Object>> scored = new ArrayList<>();
        for (var n2 : dMap.keySet()) {
            double zf = (maxF==minF)? 0.0 : ((fMap.getOrDefault(n2,0) - minF)*1.0/(maxF-minF));
            double zd = (maxD==minD)? 0.0 : ((dMap.get(n2) - minD)*1.0/(maxD-minD));
            double score = wF*zf + wD*zd;
            Map<String,Object> m = new HashMap<>();
            m.put("n2", n2);
            m.put("freqIn"+days+"Days", fMap.getOrDefault(n2,0));
            m.put("daysSince", dMap.get(n2));
            m.put("score", score);
            scored.add(m);
        }
        scored.sort((a,b) -> Double.compare((Double)b.get("score"), (Double)a.get("score")));
        return scored.subList(0, Math.min(k, scored.size()));
    }
}
