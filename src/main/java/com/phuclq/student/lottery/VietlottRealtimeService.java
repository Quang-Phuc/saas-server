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
    private final HttpClient httpClient;

    // Tránh xử lý trùng trong ngày
    private String lastProcessedKey = "";

    public VietlottRealtimeService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    // ==================================================================
    // 1. REALTIME: Tự động kiểm tra kết quả mới nhất mỗi 60 giây (chỉ giờ vàng)
    // ==================================================================
    @Scheduled(fixedDelay = 60000) // 60 giây/lần
    public void checkLatestVietlottResult() {
//        if (!isInDrawTime()) return; // Chỉ chạy 18h00 - 19h30

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Kiểm tra realtime tất cả loại phổ biến
        fetchByProductAndDate("mega645", today);
        fetchByProductAndDate("power655", today);
        fetchByProductAndDate("max3d", today);
        fetchByProductAndDate("max4d", today);
        // Keno để hàm 19h lấy 1 lần là đủ
    }

    // ==================================================================
    // 2. Lấy đầy đủ kết quả hôm nay lúc 19h00 (bao gồm cả Keno)
    // ==================================================================
    @Scheduled(cron = "0 0 19 * * *") // Mỗi ngày đúng 19:00
    public void fetchTodayAllProducts() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        fetchByProductAndDate("mega645", today);
        fetchByProductAndDate("power655", today);
        fetchByProductAndDate("max3d", today);
        fetchByProductAndDate("max4d", today);
        fetchByProductAndDate("keno", today); // Keno lấy 1 lần lúc 19h là đủ
    }

    // ==================================================================
    // Hàm công khai: Gọi thủ công bất kỳ lúc nào
    // Ví dụ: vietlottService.fetchByProductAndDate("mega645", "19/11/2025");
    // ==================================================================
    public void fetchByProductAndDate(String productCode, String dateDDMMYYYY) {
        String productName = getProductName(productCode);

        String url = String.format(
                "https://vietlott.vn/vi/trung-thuong/ket-qua-trung-thuong/winning-number/%s?date=%s",
                getProductId(productCode),
                dateDDMMYYYY.replace("/", "%2F")
        );

        String key = productName + "_" + dateDDMMYYYY;
        if (key.equals(lastProcessedKey)) {
            log.info("Đã xử lý trước đó: {} ngày {}", productName, dateDDMMYYYY);
            return;
        }

        fetchAndProcessResult(url, key, productName);
    }

    // ==================================================================
    // Helper methods (Java 11 OK)
    // ==================================================================
    private boolean isInDrawTime() {
        LocalTime now = LocalTime.now();
        return !now.isBefore(LocalTime.of(18, 0)) && !now.isAfter(LocalTime.of(19, 30));
    }

    private String getProductName(String productCode) {
        if (productCode == null) return "UNKNOWN";
        String code = productCode.toLowerCase();

        switch (code) {
            case "mega645":     return "Mega 6/45";
            case "power655":    return "Power 6/55";
            case "max3d":
            case "max3dpro":    return "Max 3D";
            case "max4d":       return "Max 4D";
            case "keno":        return "Keno";
            default:            return productCode.toUpperCase();
        }
    }

    private String getProductId(String code) {
        if (code == null) return "645";
        String lower = code.toLowerCase();

        switch (lower) {
            case "mega645":     return "645";
            case "power655":    return "655";
            case "max3d":
            case "max3dpro":    return "max-3d";
            case "max4d":       return "max-4d";
            case "keno":        return "keno";
            default:            return "645";
        }
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
                return; // Không log warn nữa, tránh spam
            }

            JsonNode root = mapper.readTree(response.body());
            JsonNode draw = root.path("Draws").isArray() && root.path("Draws").size() > 0
                    ? root.path("Draws").get(0)
                    : null;

            if (draw == null || draw.isMissingNode()) {
                return; // Chưa có kết quả
            }

            String product = forceProductName != null ? forceProductName : draw.path("ProductName").asText();
            String rawDate = draw.path("DrawDate").asText();
            if (rawDate.length() < 10) return;

            String formattedDate = LocalDate.parse(rawDate.substring(0, 10))
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            String numbers = draw.path("WinningNumber").asText().trim();
            String jackpot = draw.path("Jackpot").asText("0");

            lastProcessedKey = product + "_" + formattedDate;

            log.info("========================================");
            log.info("CÓ KẾT QUẢ VIETLOTT MỚI");
            log.info("   Loại: {}", product);
            log.info("   Ngày quay: {}", formattedDate);
            log.info("   Bộ số: {}", numbers);
            if (Long.parseLong(jackpot) > 0) {
                log.info("   Jackpot: {},0 tỷ đồng", Long.parseLong(jackpot) / 1_000_000_000L);
            }
            log.info("========================================");

            saveToDatabase(product, formattedDate, numbers, jackpot);

        } catch (Exception e) {
            // Không log error chi tiết nữa, chỉ cần biết có lỗi là được
            log.debug("Lỗi nhẹ khi lấy Vietlott (bình thường): {}", e.getMessage());
        }
    }

    private void saveToDatabase(String product, String date, String numbers, String jackpot) {
        log.info("Đã gọi hàm lưu kết quả Vietlott {} ngày {} vào database!", product, date);
        // TODO: Inject service và insert thật ở đây
    }
}