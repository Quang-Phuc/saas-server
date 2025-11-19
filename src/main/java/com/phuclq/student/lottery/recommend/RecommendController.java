package com.phuclq.student.lottery.recommend;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lottery")
@CrossOrigin(origins = "*")
public class RecommendController {
    private final RecommendService svc;

    public RecommendController(RecommendService s) {
        this.svc = s;
    }

    @GetMapping("/recommend")
    public List<Map<String, Object>> recommend(@RequestParam(defaultValue = "MB") String region, @RequestParam(defaultValue = "30") int days, @RequestParam(defaultValue = "10") int k) {
        return svc.recommend(region, days, k);
    }
}
