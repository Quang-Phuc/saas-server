package com.phuclq.student.lottery.chat;

import com.phuclq.student.lottery.dream.DreamService;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins="*")
public class ChatController {
    private final ChatService stats;
    private final DreamService dream;

    public ChatController(ChatService s, DreamService d){ this.stats=s; this.dream=d; }

    public static class ChatRequest {
        public String message;
        public String type;
        public String region;
        public Integer days;
        public Integer topK;
        public Boolean includeSql;
        public Boolean verbose; // <-- NEW
    }

    @PostMapping
    public Map<String,Object> chat(@RequestBody ChatRequest b){
        String type = b.type==null? "stats" : b.type.trim().toLowerCase();
        if ("dream".equals(type)) {
            String region = b.region==null? "MB" : b.region;
            int days = b.days==null? 30 : b.days;
            int topK = b.topK==null? 10 : Math.max(1, Math.min(b.topK, 20));
            boolean verbose = b.verbose != null && b.verbose;
            return dream.interpretAndRecommend(b.message, region, days, topK, verbose); // <-- pass verbose
        }
        boolean includeSql = b.includeSql!=null && b.includeSql;
        return stats.handleStatsMessage(b.message, includeSql);
    }
}
