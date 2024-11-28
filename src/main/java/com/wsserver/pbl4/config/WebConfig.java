package com.wsserver.pbl4.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
       registry.addMapping("/**")
                .allowedOrigins("http://192.168.1.6:5173")  // Thêm URL frontend chính xác
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Thêm tất cả các phương thức HTTP mà bạn sử dụng
                .allowedHeaders("*")
                .allowCredentials(true);
        
    }
}
