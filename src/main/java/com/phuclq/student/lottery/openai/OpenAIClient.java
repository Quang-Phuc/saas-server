package com.phuclq.student.lottery.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientResponseException;

import java.util.*;

@Service
public class OpenAIClient {

    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${openai.api.key:}")
    private String apiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String defaultModel;

    public String chat(String systemPrompt, String userMessage) {
        return chat(systemPrompt, userMessage, defaultModel);
    }

    public String chat(String systemPrompt, String userMessage, String model) {
        if (apiKey == null || apiKey.isBlank()) {
            return "[OpenAI API key is not configured]";
        }

        String url = "https://api.openai.com/v1/chat/completions";

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        messages.add(Map.of("role", "user", "content", userMessage));
        body.put("messages", messages);
        body.put("temperature", 0.2);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            Map<?, ?> response = rest.postForObject(url, request, Map.class);
            if (response == null) return "[Empty OpenAI response]";
            List<?> choices = (List<?>) response.get("choices");
            if (choices == null || choices.isEmpty()) return "[No choices from OpenAI]";
            Map<?, ?> choice0 = (Map<?, ?>) choices.get(0);
            Map<?, ?> message = (Map<?, ?>) choice0.get("message");
            return (String) message.get("content");
        } catch (RestClientResponseException e) {
            return "[OpenAI error] " + e.getRawStatusCode() + " " + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "[OpenAI error] " + e.getMessage();
        }
    }
}
