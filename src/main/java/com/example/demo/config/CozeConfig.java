package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "coze")
@Data
public class CozeConfig {

    /**
     * coze路径
     */
    private String apiUrl;

    /**
     * coze token
     */
    private String apiKey;

    /**
     * votId
     */
    private String botId;

}
