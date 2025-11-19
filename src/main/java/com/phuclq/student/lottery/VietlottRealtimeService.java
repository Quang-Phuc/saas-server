package com.phuclq.student.lottery.service;

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
    private final HttpClient httpClient;

    // Key đã xử lý để tránh trùng (product + date)
    private String lastProcessedKey = "";

    public VietlottRealtimeService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    // ==================================================================
    // 1. Polling realtime tự động (latest) - chạy mỗi ngày lúc 18h-19h30
    // ==================================================================
    @Scheduled(fixedDelay = 60000) // 60s/lần
    public void checkLatestVietlottResult() {
        if (!isInDrawTime()) return;

        fetchAndProcessResult("https://api.viettelstore.vn/lottery/result/latest", "REALTIME_LATEST");
    }

    // ==================================================================
    // 2. Hàm bạn cần: Lấy kết quả theo ngày + loại (Mega, Power, Keno...)
    // ==================================================================
    @Scheduled(cron = "0 0 19 * * *") // Chạy tự động mỗi ngày lúc 19:00
    public void fetchTodayAllProducts() {
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        fetchByProductAndDate("mega645", dateStr);
        fetchByProductAndDate("power655", dateStr);
        fetchByProductAndDate("max3d", dateStr);
        fetchByProductAndDate("max4d", dateStr);
        fetchByProductAndDate("keno", dateStr);
    }

    // Hàm công khai để gọi thủ công
    public void fetchByProductAndDate(String productCode, String dateDDMMYYYY) {
        String productName = getProductName(productCode);

        String url = String.format(
                "https://vietlott.vn/vi/trung-thuong/ket-qua-trung-thuong/winning-number/%s?date=%s",
                getProductId(productCode), dateDDMMYYYY.replace("/", "%2F")
        );

        String key = productName + "_" + dateDDMMYYYY;
        if (key.equals(lastProcessedKey)) {
            log.info("Đã xử lý trước đó: {} ngày {}", productName, dateDDMMYYYY);
            return;
        }

        fetchAndProcessResult(url, key, productName);
    }

    // ==================================================================
    // Helper methods - ĐÃ FIX CHO JAVA 11
    // ==================================================================
    private boolean isInDrawTime() {
        LocalTime now = LocalTime.now();
        return !now.isBefore(LocalTime.of(18, 0)) && !now.isAfter(LocalTime.of(19, 30));
    }

    private String getProductName(String productCode) {
        if (productCode == null) return "UNKNOWN";
        String code = productCode.toLowerCase();

        switch (code) {
            case "mega645":
                return "Mega 6/45";
            case "power655":
                return "Power 6/55";
            case "max3d":
            case "max3dpro":
                return "Max 3D";
            case "max4d":
                return "Max 4D";
            case "keno":
                return "Keno";
            default:
                return productCode.toUpperCase();
        }
    }

    private String getProductId(String code) {
        if (code == null) return "645";
        String lower = code.toLowerCase();

        switch (lower) {
            case "mega645":
                return "645";
            case "power655":
                return "655";
            case "max3d":
            case "max3dpro":
                return "max-3d";
            case "max4d":
                return "max-4d";
            case "keno":
                return "keno";
            default:
                return "645";
        }
    }

    private void fetchAndProcessResult(String url, String uniqueKey) {
        fetchAndProcessResult(url, uniqueKey, null);
    }

    private void fetchAndProcessResult(String url, String uniqueKey, String forceProductName) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Mozilla/5.0")
                    .header("Referer", "https://vietlott.vn/")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                log.warn("Không có dữ liệu từ: {}", url);
                return;
            }

            JsonNode root = mapper.readTree(response.body());
            JsonNode draw = root.path("Draws").isArray() && root.path("Draws").size() > 0
                    ? root.path("Draws").get(0)
                    : null;

            if (draw == null || draw.isMissingNode()) {
                return; // chưa có kết quả
            }

            String product = forceProductName != null ? forceProductName : draw.path("ProductName").asText();
            String drawDate = draw.path("DrawDate").asText("");
            if (drawDate.isEmpty() || drawDate.length() < 10) return;
            String formattedDate = LocalDate.parse(drawDate.substring(0, 10))
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            String numbers = draw.path("WinningNumber").asText().trim();
            String jackpot = draw.path("Jackpot").asText("0");

            lastProcessedKey = product + "_" + formattedDate;

            log.info("CÓ KẾT QUẢ VIETLOTT MỚI");
            log.info("   Loại: {}", product);
            log.info("   Ngày quay: {}", formattedDate);
            log.info("   Bộ số: {}", numbers);
            if (Long.parseLong(jackpot) > 0) {
                log.info("   Jackpot: {} tỷ", Long.parseLong(jackpot) / 1_000_000_000.0);
            }

            saveToDatabase(product, formattedDate, numbers, jackpot);

        } catch (Exception e) {
            log.error("Lỗi khi lấy kết quả Vietlott từ {}: {}", url, e.getMessage());
        }
    }

    private void saveToDatabase(String product, String date, String numbers, String jackpot) {
        log.info("Đã lưu kết quả Vietlott {} ngày {} vào database!", product, date);
        // TODO: Gọi service thật để insert DB
    }
}