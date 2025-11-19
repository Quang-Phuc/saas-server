package com.phuclq.student.lottery.ingest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/lottery")
@CrossOrigin(origins = "*")
public class LotteryIngestController {
    private final JdbcTemplate jdbc;

    public LotteryIngestController(JdbcTemplate j) {
        this.jdbc = j;
    }

    @PostMapping("/draws")
    public Map<String, Object> ingest(@Valid @RequestBody DrawPayload body) {
        String sql = "INSERT INTO draws(region,province,draw_date,game) VALUES (?,?,?, 'XSTT')";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, body.region);
            if (body.province == null) ps.setNull(2, Types.VARCHAR);
            else ps.setString(2, body.province);
            ps.setDate(3, java.sql.Date.valueOf(body.drawDate));
            return ps;
        }, kh);
        Number d = kh.getKey();
        if (d == null) throw new RuntimeException("No draw id");
        for (var r : body.results) {
            jdbc.update("INSERT INTO results(draw_id,prize_name,seq,number) VALUES (?,?,?,?)", d.longValue(), r.prize, r.seq == null ? 1 : r.seq, r.number);
        }
        Map<String, Object> res = new HashMap<>();
        res.put("drawId", d.longValue());
        res.put("message", "OK");
        return res;
    }
}
