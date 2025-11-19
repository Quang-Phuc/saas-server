package com.phuclq.student.lottery.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phuclq.student.lottery.openai.OpenAIClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ResultExplainService {

    private final OpenAIClient openAIClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public ResultExplainService(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    public String explain(String question, List<Map<String,Object>> rows) {
        String json;
        try {
            json = mapper.writeValueAsString(rows);
        } catch (JsonProcessingException e) {
            json = "[]";
        }

        String systemPrompt =
                "Bạn là trợ lý phân tích dữ liệu. Trả lời bằng tiếng Việt, ngắn gọn, dễ hiểu.\n" +
                        "Chỉ dựa trên dữ liệu JSON (là kết quả truy vấn DB).\n" +
                        "Nếu dữ liệu rỗng, hãy nói rõ không tìm thấy kết quả cho truy vấn.\n" +
                        "\n";


        String userMessage = "Câu hỏi người dùng: " + question + "\nDữ liệu JSON: " + json;
        return openAIClient.chat(systemPrompt, userMessage);
    }
}
