package com.phuclq.student.exception;


import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
public class ErrorMessageLoader {

    private static Map<String, Messages> errorMessageMap;

    public ErrorMessageLoader() {
        try {
            Charset charset = Charset.forName("UTF-8");
            Properties englishMessages = new Properties();
            englishMessages.load(new InputStreamReader(getClass().getResourceAsStream("/en.properties"), charset));
            errorMessageMap = englishMessages.entrySet().stream().collect(
                    Collectors.toMap(
                            e -> e.getKey().toString(),
                            e -> {
                                Messages errorMessage = new Messages();
                                errorMessage.setEn(e.getValue().toString());
                                return errorMessage;
                            }
                    )
            );

            Properties vietnameseMessages = new Properties();
            vietnameseMessages.load(new InputStreamReader(getClass().getResourceAsStream("/vn.properties"), charset));
            errorMessageMap.forEach((key, value) -> value.setVn(vietnameseMessages.getProperty(key)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Messages getMessage(String errorCode) {
        return errorMessageMap.get(errorCode);
    }
}
