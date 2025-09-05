package com.example.demo.service;

import com.example.demo.config.CozeConfig;
import com.example.demo.exception.BizException;
import com.example.demo.req.CozeChatReq;
import com.example.demo.resp.CozeChatResp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class CozeChatService {

    private final CozeConfig cozeConfig;
    private final WebClient webClient = WebClient.builder().baseUrl("https://api.coze.cn").build();

    @Autowired
    private ObjectMapper objectMapper;

    public Flux<String> chatStream(String conversationId, String userId, String userMessage) {

        CozeChatReq.Message msg = new CozeChatReq.Message("user", userMessage);
        List<CozeChatReq.Message> messages = List.of(msg);

        CozeChatReq cozeChatReq = new CozeChatReq();
        cozeChatReq.setUser_id(userId);
        cozeChatReq.setBot_id(cozeConfig.getBotId());
        cozeChatReq.setStream(true);
        cozeChatReq.setAuto_save_history(true);
        cozeChatReq.setAdditional_messages(messages);

        // 打印请求体
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(cozeChatReq);
            System.out.println("发送的 JSON 请求体：\n" + json);
        } catch (Exception e) {
            System.err.println("序列化失败：" + e.getMessage());
        }

        return webClient.post()
                .uri("/v3/chat?conversation_id=" + conversationId)
                .header("Authorization", "Bearer " + cozeConfig.getApiKey())
                .header("Content-Type", "application/json")
                .bodyValue(cozeChatReq)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(line -> System.out.println("[RAW] " + line))
                .map(this::extractContent)
                .handle((String content, SynchronousSink<String> sink) -> {
                    // 过滤空值
                    if (content == null || content.trim().isEmpty()) {
                        return;
                    }
                    System.out.println(content);
                    // 发射有效内容
                    sink.next(content);
                    // 收到结束信号时终止流
                    if ("[DONE]".equals(content)) {
                        sink.complete();
                    }
                })
                .onErrorResume(e -> Flux.just("【错误】" + e.getMessage()));
    }

    // 解析 event-stream 行，提取 content
    private String extractContent(String line) {
        try {
            line = line.trim();
            if (line.isEmpty()) return "";

            // 情况1：结束标记 "[DONE]"
            if ("\"[DONE]\"".equals(line) || "[DONE]".equals(line.replace("\"", ""))) {
                return "[DONE]";
            }

            // 情况2：正常消息（JSON 对象）
            if (line.startsWith("{")) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(line);

                String role = node.path("role").asText();
                String type = node.path("type").asText();
                String contentType = node.path("content_type").asText();
                String createdAt = node.path("created_at").asText();

                // 只提取助手的文本消息
                if ("assistant".equals(role) &&
                        "text".equals(contentType) &&
                        "answer".equals(type) &&
                        StringUtils.isNoneBlank(createdAt)) {
                    return node.toString();
                }
            }

            // 其他情况（元数据、心跳等）忽略
            return "";

        } catch (Exception e) {
            System.err.println("解析失败: " + line);
            e.printStackTrace();
            return "【解析错误】";
        }
    }


    public CozeChatResp chat(CozeChatReq cozeChatReq) {
        try {
            // 使用现有的 chatStream 方法
            AtomicReference<CozeChatResp> cozeChatResp = new AtomicReference<>(new CozeChatResp());

            chatStream(cozeChatReq.getConversationId(), cozeChatReq.getUser_id(), cozeChatReq.getMessage())
                    .takeWhile(content -> !"【错误】".startsWith(content)) // 遇到错误提前终止
                    .doOnNext(content -> {
                        if ("[DONE]".equals(content)) {
                            // 忽略 [DONE]
                        } else if (!content.startsWith("【解析错误】")) {
                            ObjectMapper mapper = new ObjectMapper();
                            try {
                                JsonNode node = mapper.readTree(content);
                                cozeChatResp.set(objectMapper.treeToValue(node, CozeChatResp.class));
                            } catch (JsonProcessingException e) {
                                throw new BizException(500, e.getMessage());
                            }
                        }
                    })
                    .blockLast(Duration.ofSeconds(30)); // ⏱️ 等待流结束，最多 30 秒

            // 返回完整结果
            return cozeChatResp.get();

        } catch (Exception e) {
            throw new BizException(500, "请求超时或失败: " + e.getMessage());
        }
    }
}
