package com.phuclq.student.lottery.chat;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DatabaseQueryService {
    private final JdbcTemplate jdbc;

    public DatabaseQueryService(JdbcTemplate j) {
        this.jdbc = j;
    }

    public List<Map<String, Object>> executeSelect(String sql) {
        return jdbc.queryForList(sql);
    }
}
