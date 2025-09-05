package com.example.demo.service;

import com.example.demo.config.CozeConfig;
import com.example.demo.req.CreateDocumentReq;
import com.example.demo.req.ViewDocumentReq;
import com.example.demo.resp.CozeResp;
import com.example.demo.entity.DocumentInfos;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CozeKnowledgeService {

    private final CozeConfig cozeConfig;

    private final RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public List<DocumentInfos> createDocument(CreateDocumentReq createDocumentReq) {
        String url = cozeConfig.getApiUrl() + "/open_api/knowledge/document/create";

        try {
            String requestJson = objectMapper.writeValueAsString(createDocumentReq);
            log.info("调用 Coze 创建文档接口 - 请求参数: {}", requestJson);
        } catch (Exception e) {
            log.warn("无法序列化请求参数: {}", e.getMessage());
        }

        // 设置 Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(cozeConfig.getApiKey());
        headers.add("Agw-Js-Conv", "str");

        HttpEntity<CreateDocumentReq> entity = new HttpEntity<>(createDocumentReq, headers);

        try {
            // 发送 POST 请求
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            CozeResp<String> result = objectMapper.readValue(response.getBody(),
                    new TypeReference<CozeResp<String>>() {
                    }
            );

            // 校验业务状态码
            if (result == null) {
                throw new RuntimeException("Coze API 返回空响应");
            }

            if (result.getCode() != 0) {
                String errorMsg = result.getMsg() != null ? result.getMsg() : "Unknown error";
                String logid = result.getDetail() != null ? result.getDetail().getLogid() : "N/A";
                throw new RuntimeException("Coze API Error [code=" + result.getCode() + "]: " + errorMsg + " [LogID: " + logid + "]");
            }

            return result.getDocument_infos();

        } catch (RuntimeException re) {
            // 已包装的业务异常，直接抛出
            throw re;
        } catch (Exception e) {
            // 其他异常（网络、JSON 解析等）
            throw new RuntimeException("调用 Coze API 失败: " + e.getMessage(), e);
        }
    }

    public List<DocumentInfos> viewDocument(ViewDocumentReq viewDocumentReq) {
        String url = cozeConfig.getApiUrl() + "/open_api/knowledge/document/list";

        // 设置 Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(cozeConfig.getApiKey());
        headers.add("Agw-Js-Conv", "str");

        HttpEntity<ViewDocumentReq> entity = new HttpEntity<>(viewDocumentReq, headers);

        try {
            // 发送 POST 请求
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            CozeResp<String> result = objectMapper.readValue(response.getBody(),
                    new TypeReference<CozeResp<String>>() {
                    }
            );

            // 校验业务状态码
            if (result == null) {
                throw new RuntimeException("Coze API 返回空响应");
            }

            if (result.getCode() != 0) {
                String errorMsg = result.getMsg() != null ? result.getMsg() : "Unknown error";
                String logid = result.getDetail() != null ? result.getDetail().getLogid() : "N/A";
                throw new RuntimeException("Coze API Error [code=" + result.getCode() + "]: " + errorMsg + " [LogID: " + logid + "]");
            }

            return result.getDocument_infos();

        } catch (RuntimeException re) {
            // 已包装的业务异常，直接抛出
            throw re;
        } catch (Exception e) {
            // 其他异常（网络、JSON 解析等）
            throw new RuntimeException("调用 Coze API 失败: " + e.getMessage(), e);
        }
    }

    public String deleteDocument(List<String> documentIds) {
        String url = cozeConfig.getApiUrl() + "/open_api/knowledge/document/delete";

        // 设置 Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(cozeConfig.getApiKey());
        headers.add("Agw-Js-Conv", "str");

        // 组装请求参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("document_ids", documentIds);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(paramMap, headers);

        try {
            // 发送 POST 请求
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            CozeResp<String> result = objectMapper.readValue(response.getBody(),
                    new TypeReference<CozeResp<String>>() {
                    }
            );

            // 校验业务状态码
            if (result == null) {
                throw new RuntimeException("Coze API 返回空响应");
            }

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
