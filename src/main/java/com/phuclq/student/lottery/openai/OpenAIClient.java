package com.phuclq.student.lottery.openai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIClient {
    private final RestTemplate rest = new RestTemplate();
    @Value("${openai.api.key:}")
    private String apiKey;
    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    public String chat(String sys, String usr) {
        if (apiKey == null || apiKey.isBlank()) return "[OpenAI key missing]";
        String url = "https://api.openai.com/v1/chat/completions";
        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        List<Map<String, String>> msgs = new ArrayList<>();
        msgs.add(Map.of("role", "system", "content", sys));
        msgs.add(Map.of("role", "user", "content", usr));
        body.put("messages", msgs);
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.setBearerAuth(apiKey);
        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, h);
        Map<?, ?> resp = rest.postForObject(url, req, Map.class);
        List<?> choices = (List<?>) resp.get("choices");
        Map<?, ?> m = (Map<?, ?>) choices.get(0);
        return (String) ((Map<?, ?>) m.get("message")).get("content");
    }
}
