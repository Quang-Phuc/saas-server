package com.phuclq.student.lottery.chat;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private final SqlGeneratorService sqlGenerator;
    private final DatabaseQueryService db;
    private final ResultExplainService explainer;

    public ChatService(SqlGeneratorService sqlGenerator, DatabaseQueryService db, ResultExplainService explainer) {
        this.sqlGenerator = sqlGenerator;
        this.db = db;
        this.explainer = explainer;
    }

    public Map<String, Object> handleMessage(String message, boolean includeSql) {
        String sql = sqlGenerator.generateSqlFromQuestion(message);
        List<Map<String,Object>> rows = db.executeSelect(sql);
        String answer = explainer.explain(message, rows);

        Map<String,Object> res = new HashMap<>();
        res.put("answer", answer);
        res.put("rowCount", rows.size());
        if (includeSql) res.put("sql", sql);
        return res;
    }
}
