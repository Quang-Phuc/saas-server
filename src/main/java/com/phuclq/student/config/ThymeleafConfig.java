package com.phuclq.student.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafConfig {
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/"); // Set the prefix for template files
        templateResolver.setSuffix(".html"); // Set the suffix for template files
        templateResolver.setTemplateMode("HTML"); // Set the template mode (HTML in this case)
        templateResolver.setCharacterEncoding("UTF-8"); // Set character encoding
        templateResolver.setOrder(1); // Template resolver order (you can adjust this if needed)
        return templateResolver;
    }
}
