package com.phuclq.student.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlIdInserter {
    public static String addUniqueIdsToHeadersIfAbsent(String htmlContent) {
        // Tìm tất cả các thẻ từ h1 đến h6
        Pattern pattern = Pattern.compile("<h[1-6](\\s+[^>]*)?>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(htmlContent);

        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String headerTag = matcher.group();

            // Kiểm tra xem thẻ có chứa ID chưa
            if (!headerTag.contains(" id=")) {
                // Tạo ID duy nhất
                String uniqueId = UUID.randomUUID().toString();

                // Chèn ID vào thẻ
                String replacement = headerTag.replaceFirst(">", " id=\"" + uniqueId + "\">");
                matcher.appendReplacement(sb, replacement);
            }
        }

        matcher.appendTail(sb);
        return sb.toString();
    }
}
