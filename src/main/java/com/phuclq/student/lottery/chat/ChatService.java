package com.phuclq.student.lottery.chat;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {
    private final SqlGeneratorService gen;
    private final DatabaseQueryService db;
    private final ResultExplainService exp;

    public ChatService(SqlGeneratorService g, DatabaseQueryService d, ResultExplainService e) {
        this.gen = g;
        this.db = d;
        this.exp = e;
    }

    public Map<String, Object> handleStatsMessage(String msg, boolean includeSql) {
        String sql = gen.generateSqlFromQuestion(msg);
        List<Map<String, Object>> rows = db.executeSelect(sql);
        String answer = exp.explain(msg, rows);
        Map<String, Object> r = new HashMap<>();
        r.put("type", "stats");
        r.put("answer", answer);
        r.put("rowCount", rows.size());
        if (includeSql) r.put("sql", sql);
        return r;
    }
}