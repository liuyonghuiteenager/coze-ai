package com.example.demo.service;

import com.example.demo.config.CozeConfig;
import com.example.demo.req.CreateConversationReq;
import com.example.demo.resp.CozeResp;
import com.example.demo.resp.CreateConversationResp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final CozeConfig cozeConfig;

    private final RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 创建新会话
     */
    public CreateConversationResp createConversation(CreateConversationReq req) {
        String url = cozeConfig.getApiUrl()+"/v1/conversation/create";

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(cozeConfig.getApiKey());

        // 组装请求参数
        req.setBot_id(cozeConfig.getBotId());

        HttpEntity<CreateConversationReq> entity = new HttpEntity<>(req, headers);

        try {
            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            CozeResp<CreateConversationResp> result = objectMapper.readValue(response.getBody(),
                    new TypeReference<CozeResp<CreateConversationResp>>() {
                    }
            );

            // 1. 空响应检查
            if (result == null) {
                throw new RuntimeException("Coze API 返回空响应");
            }

            // 2. 业务状态码检查（Coze 的 code 字段）
            if (result.getCode() != 0) {
                String errorMsg = result.getMsg() != null ? result.getMsg() : "Unknown error";
                String logid = result.getDetail() != null ? result.getDetail().getLogid() : "N/A";
                throw new RuntimeException("Coze API Error [code=" + result.getCode() + "]: " + errorMsg + " [LogID: " + logid + "]");
            }

            return result.getData();

        } catch (RuntimeException re) {
            // 已包装的业务异常，直接抛出
            throw re;
        } catch (Exception e) {
            // 其他异常（网络、JSON 解析等）
            throw new RuntimeException("调用 Coze API 失败: " + e.getMessage(), e);
        }
    }
}
