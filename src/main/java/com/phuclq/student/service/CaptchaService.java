package com.phuclq.student.service;


import com.phuclq.student.dto.Theme;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
public class CaptchaService {


    private final double SKEW = 0.45;
    private final Logger LOGGER = LoggerFactory.getLogger(CaptchaService.class);
    @Value("${captcha.secureKey}")
    private String secureKey;
    @Value("${captcha.imageWidth}")
    private int imageWidth;
    @Value("${captcha.imageHeight}")
    private int imageHeight;
    @Value("${captcha.fontSize}")
    private int fontSize;
    @Value("${captcha.expireInMinite}")
    private int expireInMinite;
    @Value("${captcha.length}")
    private int length;

    public String generateId() {
        String randomStr = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        String hashStr = DigestUtils.sha256Hex(randomStr + secureKey);
        return getSubStringFromString(hashStr) + randomStr;
    }

    public String generateImage(String userName, String captchaId, String theme) throws Exception {
        try {
            BufferedImage image = new BufferedImage(imageWidth, imageHeight,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            setBackgroundColor(g2d, theme);
            g2d.fillRect(0, 0, imageWidth, imageHeight);

            AffineTransform affineTransform = new AffineTransform();

            String captchaText = generateCaptcha(userName, captchaId, secureKey);
            for (int i = 0; i < length; i++) {
                double angle;
                double randNum = Math.random();
                if (randNum * 2 > 1) {
                    angle = randNum * SKEW;
                } else {
                    angle = randNum * -SKEW;
                }
                affineTransform.rotate(angle, (fontSize * i) + (fontSize / 2), imageHeight / 2);
                g2d.setTransform(affineTransform);
                setRandomFont(g2d);
                setFontColor(g2d, theme);
                g2d.drawString(captchaText.substring(i, i + 1), (i * fontSize) + (int) (randNum * (fontSize - 3)), fontSize + (int) (randNum * 12));
                affineTransform.rotate(-angle, (fontSize * i) + (fontSize / 2), fontSize / 2);
            }
            return "data:image/png;base64," + encodeToString(image, "png");
        } catch (Exception e) {
            LOGGER.error("CaptchaGenerator generate captcha image exception: {}", e);
            throw new Exception(e);
        }
    }

    public String generateCaptcha(String userName, String captchaId, String secureKey) {
        long currentTimeInMinute = System.currentTimeMillis() / 1000 / 60;
        String iStr;
        iStr = userName + captchaId + secureKey + currentTimeInMinute;
        String secureStr = DigestUtils.sha256Hex(iStr);
        secureStr = removeSpecChars(secureStr);
        return getSubStringFromString(secureStr);
    }

    public boolean isValidCaptcha(String userName, String captchaId, String captcha) {
        long currentTimeInMinute = System.currentTimeMillis() / 1000 / 60;
        for (int i = 0; i < expireInMinite; i++) {
            String tmpSecureStr = userName + captchaId + secureKey + currentTimeInMinute;
            String tmpStr = removeSpecChars(DigestUtils.sha256Hex(tmpSecureStr));
            if (getSubStringFromString(tmpStr).equalsIgnoreCase(captcha)) {
                return true;
            }
            currentTimeInMinute -= 1;
        }
        return false;
    }

    private String removeSpecChars(String iStr) {
        return iStr.replace("0", "N")
                .replace("1", "T")
                .replace("7", "H");
    }

    private String getSubStringFromString(String iStr) {
        return iStr.substring(6, 6 + length).toUpperCase();
    }

    private void setFontColor(Graphics2D g2d, String theme) {
        if (StringUtils.isEmpty(theme) || Theme.LIGHT.getName().equalsIgnoreCase(theme)) {
            g2d.setColor(new Color(0x42236A, false));
        } else {
            g2d.setColor(new Color(0xF6F3F8, false));
        }
    }

    private void setBackgroundColor(Graphics2D g2d, String theme) {
        g2d.clearRect(0, 0, imageWidth, imageHeight);

        if (StringUtils.isEmpty(theme) || Theme.LIGHT.getName().equalsIgnoreCase(theme)) {
            g2d.setColor(new Color(0xffffff, false));
        } else {
            g2d.setColor(new Color(0x22172C, false));
        }
    }

    private void setRandomFont(Graphics2D g2d) {
        Font font = new Font(Font.DIALOG, Font.BOLD, fontSize);
        g2d.setFont(font);
    }

    private String encodeToString(BufferedImage image, String type) throws IOException {
        String imageString;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            Base64 encoder = new Base64();
            imageString = encoder.encodeToString(imageBytes);
            bos.close();
        } catch (IOException e) {
            LOGGER.error("CaptchaGenerator encodeToString exception: {}", e);
            throw new IOException(e);
        }
        return imageString;
    }
}

