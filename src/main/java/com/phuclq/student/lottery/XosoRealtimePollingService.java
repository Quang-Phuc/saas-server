package com.phuclq.student.lottery.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class XosoRealtimePollingService {

    private static final Logger log = LoggerFactory.getLogger(XosoRealtimePollingService.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    // Nguồn siêu nhanh + ổn định 2025
    private static final String API_URL = "https://xskt.com.vn/rss-feed/mien-bac-xsmb.rss";

    private final HttpClient httpClient;
    private String lastProcessedDate = "";  // tránh xử lý trùng

    public XosoRealtimePollingService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    // Chỉ poll vào khung giờ quay thưởng MB: 18h00 → 19h00
    @Scheduled(fixedDelay = 30000) // 30 giây/lần
    public void checkNewResult() {
        LocalTime now = LocalTime.now();
//        if (now.isBefore(LocalTime.of(18, 0)) || now.isAfter(LocalTime.of(19, 0))) {
//            return; // ngoài giờ quay thì nghỉ
//        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("User-Agent", "Mozilla/5.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String rssContent = response.body();

            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            // Nếu RSS có chứa ngày hôm nay và chưa xử lý
            if (rssContent.contains(today) && !today.equals(lastProcessedDate)) {

                // Lấy giải đặc biệt từ RSS
                String giaiDB = extract(rssContent, "Đặc biệt:", "<");
                if (giaiDB == null || giaiDB.isBlank()) {
                    giaiDB = extract(rssContent, "Giải đặc biệt:", "<");
                }

                if (giaiDB != null && giaiDB.trim().length() >= 5) {
                    giaiDB = giaiDB.trim();
                    String last2 = giaiDB.substring(giaiDB.length() - 2);

                    log.info("🎉🎉 CÓ KẾT QUẢ XỔ SỐ MIỀN BẮC NGÀY {} 🎉🎉", today);
                    log.info("   Giải đặc biệt: {}  →  Đề về: {}", giaiDB, last2);

                    // GỌI HÀM LƯU DB TẠI ĐÂY
                    saveResultToDatabase(giaiDB, today);

                    lastProcessedDate = today; // đánh dấu đã xử lý
                }
            }

        } catch (Exception e) {
            log.error("Lỗi khi polling kết quả XSMB: {}", e.getMessage(), e);
        }
    }

    // ==================================================================
    // Helper methods (phải đặt trong class, không được ngoài class)
    // ==================================================================

    private String extract(String text, String startMarker, String endMarker) {
        int start = text.indexOf(startMarker);
        if (start == -1) return null;
        start += startMarker.length();
        int end = text.indexOf(endMarker, start);
        return end == -1 ? null : text.substring(start, end).trim();
    }

    private void saveResultToDatabase(String giaiDB, String dateStr) {
        // TODO: Inject LotteryService và gọi lưu DB thật ở đây
        // lotteryService.saveRealtimeResult("MB", dateStr, giaiDB);

        log.info("Đã gọi hàm lưu kết quả vào database! Ngày: {} | Đề: {}", dateStr, giaiDB);
    }
}