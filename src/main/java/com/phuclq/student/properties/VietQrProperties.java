package com.phuclq.student.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "vietqr")
public class VietQrProperties {
    private String bankId;
    private String accountNo;
    private String template;
    private String description;
    private String accountName;


}

