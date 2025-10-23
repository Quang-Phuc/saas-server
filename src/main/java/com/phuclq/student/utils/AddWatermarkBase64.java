package com.phuclq.student.utils;

import com.phuclq.student.common.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
@Service
@Transactional
public class AddWatermarkBase64 {

    @Value("${image.logo.svshare}")
    private String logoSVShare;



    public String addLogoInMage(String base64Image) {
        try {
            // Giải mã dữ liệu base64 thành dữ liệu hình ảnh
            BufferedImage image = decodeBase64ToImage(base64Image);
            BufferedImage logo = decodeBase64ToImage(logoSVShare);

            // Tính toán kích thước mới của logo
            int logoWidth = image.getWidth() / 3; // Chiều rộng của logo
            int logoHeight = (logo.getHeight() * logoWidth) / logo.getWidth(); // Chiều cao của logo

            // Tạo ảnh mới có kích thước tương ứng với ảnh gốc
            BufferedImage resizedLogo = new BufferedImage(logoWidth, logoHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedLogo.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(logo, 0, 0, logoWidth, logoHeight, null);
            g2.dispose();

            // Vẽ logo lên trên ảnh gốc với độ trong suốt
            Graphics2D graphics = image.createGraphics();
            AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f); // Độ trong suốt 50%
            graphics.setComposite(alphaComposite);
            int x = image.getWidth() - logoWidth - 10; // Vị trí x: 10 pixel từ biên phải
            int y = 10; // Vị trí y: 10 pixel từ biên trên
            graphics.drawImage(resizedLogo, x, y, null);
            graphics.dispose();

            // Mã hóa ảnh kết quả thành dữ liệu base64
            String base64Result = encodeImageToBase64(image, base64Image.substring(base64Image.indexOf('/') + 1, base64Image.indexOf(';')));

            System.out.println("Đã thêm logo vào ảnh như một background và mã hóa kết quả thành base64!");
            return base64Result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Image;
    }

    private static BufferedImage decodeBase64ToImage(String base64Image) throws IOException {
        String[] parts = base64Image.split(",");
        String imageString = parts[1];
        byte[] imageBytes = Base64.getDecoder().decode(imageString);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        return ImageIO.read(bis);
    }

    private static String encodeImageToBase64(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, format, bos);
        byte[] bytes = bos.toByteArray();
        return "data:image/" + format + ";base64," + Base64.getEncoder().encodeToString(bytes);
    }
}
