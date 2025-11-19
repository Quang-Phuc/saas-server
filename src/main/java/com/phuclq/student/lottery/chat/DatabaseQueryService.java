package com.phuclq.student.lottery.chat;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DatabaseQueryService {

    private final JdbcTemplate jdbc;

    public DatabaseQueryService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Map<String,Object>> executeSelect(String sql) {
        try {
            return jdbc.queryForList(sql);
        } catch (DataAccessException e) {
            throw new RuntimeException("SQL error: " + e.getMessage());
        }
    }
}
