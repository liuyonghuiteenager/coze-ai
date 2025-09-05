package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "coze")
@Data
public class CozeConfig {

    private String apiUrl;
    private String apiKey;
    private String botId;

}
