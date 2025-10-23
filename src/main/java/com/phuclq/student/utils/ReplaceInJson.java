package com.phuclq.student.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ReplaceInJson {

    // Hàm chung để thay thế khoảng trắng trong tất cả các giá trị chuỗi của JSON
    public static String replaceInJson(String jsonInput) throws IOException {
        // Tạo ObjectMapper instance
        jsonInput = jsonInput.replace("\r", "").replace("\n", "").replace("\\", "");
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Phân tích cú pháp chuỗi JSON đầu vào
            JsonNode rootNode = mapper.readTree(jsonInput);

            // Gọi hàm thay thế ký tự
            replaceSpacesInNode(rootNode);

            // Trả về chuỗi JSON đã được chỉnh sửa
            return mapper.writeValueAsString(rootNode);
        } catch (Exception e) {
            log.error("replaceInJson  {}", e.getMessage(), e);
            return jsonInput;
        }
    }

    // Hàm đệ quy để thay thế khoảng trắng trong tất cả các giá trị chuỗi của JSON Node
    private static void replaceSpacesInNode(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            objectNode.fieldNames().forEachRemaining(fieldName -> {
                JsonNode childNode = objectNode.get(fieldName);
                if (childNode.isTextual() && shouldReplace(childNode.asText())) {
                    String originalValue = childNode.asText();
                    String modifiedValue = originalValue.replace(" ", "");
                    objectNode.put(fieldName, modifiedValue);
                } else {
                    replaceSpacesInNode(childNode);
                }
            });
        } else if (node.isArray()) {
            for (JsonNode arrayElement : node) {
                replaceSpacesInNode(arrayElement);
            }
        }
    }

    // Hàm kiểm tra giá trị có nên thay thế khoảng trắng hay không
    private static boolean shouldReplace(String value) {
        // Thêm logic để xác định giá trị nào nên được thay thế
        // Ví dụ: bỏ qua các giá trị có chứa ký tự không phải số hoặc chữ
        return value.chars().allMatch(Character::isLetterOrDigit);
    }


}
