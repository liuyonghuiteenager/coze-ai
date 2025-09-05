package com.example.demo.service;

import com.example.demo.config.CozeConfig;
import com.example.demo.req.CreateConversationReq;
import com.example.demo.resp.CreateConversationResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final CozeConfig cozeConfig; // 包含 api-key, bot-id 等

    private final WebClient webClient = WebClient.builder().baseUrl("https://api.coze.cn").build();

    /**
     * 创建新会话
     */
    public String createConversation(String userId) {
        String url = "/v1/conversation/create";

        CreateConversationReq request = new CreateConversationReq();
        request.setBot_id(cozeConfig.getBotId());
        request.setUser_id(userId);

        try {
            CreateConversationResp response = webClient.post()
                    .uri(url)
                    .header("Authorization", "Bearer " + cozeConfig.getApiKey())
                    .header("Content-Type", "application/json")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(CreateConversationResp.class)
                    .block(); // 阻塞等待结果（简单场景可用）

            if (response != null && response.getCode() == 0) {
                return response.getData().getId(); // 返回 conversation_id
            } else {
                throw new RuntimeException("创建会话失败: " + response.getMsg());
            }

        } catch (Exception e) {
            throw new RuntimeException("调用创建会话接口异常: " + e.getMessage(), e);
        }
    }
}
