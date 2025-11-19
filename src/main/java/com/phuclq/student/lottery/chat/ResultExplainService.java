package com.phuclq.student.lottery.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phuclq.student.lottery.openai.OpenAIClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ResultExplainService {
    private final OpenAIClient ai;
    private final ObjectMapper m = new ObjectMapper();

    public ResultExplainService(OpenAIClient a) {
        this.ai = a;
    }

    public String explain(String q, List<Map<String, Object>> rows) {
        String json;
        try {
            json = m.writeValueAsString(rows);
        } catch (Exception e) {
            json = "[]";
        }
        String sys = "Bạn là trợ lý phân tích dữ liệu. Trả lời ngắn gọn, trực tiếp, tiếng Việt. Chỉ dựa trên JSON (kết quả truy vấn DB). Nếu dữ liệu rỗng thì nêu không tìm thấy. Không thêm chú thích hay tuyên bố miễn trừ.";
        String user = "Câu hỏi: " + q + "\nDữ liệu JSON: " + json;
        return ai.chat(sys, user);
    }
}
