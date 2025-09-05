package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 使用 patterns，支持 credentials
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // 允许所有域名，同时支持 credentials

        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        // 可选：允许前端获取响应头中的字段（如你需要）
        config.setExposedHeaders(java.util.Arrays.asList("Authorization", "content-type"));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
