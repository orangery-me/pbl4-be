package com.wsserver.pbl4.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // .allowedOrigins("*")
                .allowedOrigins("http://192.168.1.12:5173")
        .allowedMethods("*") // Cho phép tất cả các phương thức HTTP (GET, POST, PUT, DELETE, v.v.)
        .allowedHeaders("*")
        .allowCredentials(true);
    }
}
