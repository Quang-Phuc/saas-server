package com.phuclq.student.lottery.chat;

import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    public static class ChatRequest {
        @NotBlank
        public String message;
        public Boolean includeSql;
    }

    @PostMapping
    public Map<String,Object> chat(@RequestBody ChatRequest body) {
        boolean includeSql = body.includeSql != null && body.includeSql;
        return chatService.handleMessage(body.message, includeSql);
    }
}
