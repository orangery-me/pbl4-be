package com.wsserver.pbl4.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${SERVER_IP}")
    public static String server;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // .allowedOrigins("http://" + server + ":5173")
                .allowedOrigins("http://172.20.1.74:5173")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*") // Cho phép tất cả các phương thức HTTP (GET, POST, PUT, DELETE, v.v.)
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
