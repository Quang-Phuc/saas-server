package com.phuclq.student.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AwsS3Configuration {
    @Value("${aws-s3.access-key-id}")
    private String accessKeyId;

    @Value("${aws-s3.secret-access-key}")
    private String secretAccessKey;

    @Bean
    public AmazonS3 generateS3Client() {
        log.info("[LOG AwsS3Configuration]:: accessKeyId: " + accessKeyId + " || " + "secretAccessKey: " + secretAccessKey);
        return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey))).withRegion(Regions.AP_SOUTHEAST_1).build();
    }
}
