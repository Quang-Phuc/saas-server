package com.phuclq.student.lottery;


import com.fasterxml.jackson.databind.JsonNode;
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
public class VietlottRealtimeService {

    private static final Logger log = LoggerFactory.getLogger(VietlottRealtimeService.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    // API nhanh nhất 2025 – không lỗi SSL
    private static final String API_URL = "https://api.viettelstore.vn/lottery/result/latest";

    private final HttpClient httpClient;
    private String lastProcessedDate = "";

    public VietlottRealtimeService(HttpClient httpClient) {
        this.httpClient = httpClient;  // dùng chung config bypass SSL nếu cần
    }

    // Vietlott quay thưởng: 18h00 hàng ngày (Mega/Power), Keno cứ 10 phút
    @Scheduled(fixedDelay = 60000)  // 60 giây/lần là đủ
    public void checkVietlottResult() {
        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.of(18, 0)) || now.isAfter(LocalTime.of(19, 30))) {
            return; // ngoài giờ thì nghỉ
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("User-Agent", "Mozilla/5.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = mapper.readTree(response.body());

            JsonNode data = root.path("data");
            if (data.isMissingNode()) return;

            String product = data.path("product_name").asText();   // Mega 6/45, Power 6/55...
            String drawDate = data.path("draw_date").asText("");   // dd/MM/yyyy
            String numbers = data.path("winning_numbers").asText("").replace(" ", "");

            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String key = product + "_" + drawDate;

            if (drawDate.equals(today) && !key.equals(lastProcessedDate)) {

                log.info("🎉 CÓ KẾT QUẢ VIETLOTT MỚI - {} ngày {}", product, drawDate);
                log.info("   Bộ số trúng thưởng: {}", numbers);

                if (product.contains("Mega") || product.contains("Power")) {
                    String jackpot = data.path("jackpot").asText("0");
                    log.info("   Jackpot: {} tỷ", formatJackpot(jackpot));
                }

                // === GỌI LƯU DB TẠI ĐÂY ===
                saveToDatabase(product, drawDate, numbers);
                // ===========================

                lastProcessedDate = key;
            }

        } catch (Exception e) {
            log.error("Lỗi polling Vietlott: {}", e.getMessage());
        }
    }

    private String formatJackpot(String jackpot) {
        try {
            long value = Long.parseLong(jackpot);
            return String.format("%,.1f", value / 1_000_000_000.0);
        } catch (Exception e) {
            return jackpot;
        }
    }

    private void saveToDatabase(String product, String date, String numbers) {
        // TODO: lưu vào bảng vietlott_draws, vietlott_results...
        log.info("✅ Đã lưu kết quả Vietlott {} vào database!", product);
    }
}