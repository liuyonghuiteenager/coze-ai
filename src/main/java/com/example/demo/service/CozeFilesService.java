package com.example.demo.service;

import com.example.demo.config.CozeConfig;
import com.example.demo.resp.CozeResp;
import com.example.demo.resp.FileUploadResp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class CozeFilesService {

    private final CozeConfig cozeConfig;

    private final RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 上传本地文件到 Coze
     *
     * @param filePath 本地文件路径
     * @return Coze 文件信息
     */
    public CozeResp<FileUploadResp> uploadFiles(String filePath) {
        String url = cozeConfig.getApiUrl() + "/v1/files/upload";

        // 创建 multipart 请求体
        LinkedMultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();

        // 添加文件 part
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("文件不存在: " + filePath);
        }

        formData.add("file", new FileSystemResource(file));

        // 设置 headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(cozeConfig.getApiKey());
        // Content-Type 由 RestTemplate 自动设置为 multipart

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(formData, headers);

        try {
            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            // 结果处理
            CozeResp<FileUploadResp> result = objectMapper.readValue(response.getBody(),
                    new TypeReference<CozeResp<FileUploadResp>>() {
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

            // 3. 成功：返回数据
            return result;

        } catch (RuntimeException re) {
            // 已包装的业务异常，直接抛出
            throw re;
        } catch (Exception e) {
            // 其他异常（网络、JSON 解析等）
            throw new RuntimeException("调用 Coze API 失败: " + e.getMessage(), e);
        }
    }

    public FileUploadResp retrieveFiles(String fileId) {
        String url = cozeConfig.getApiUrl() + "/v1/files/retrieve?file_id=" + fileId;

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(cozeConfig.getApiKey());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            CozeResp<FileUploadResp> result = objectMapper.readValue(response.getBody(),
                    new TypeReference<CozeResp<FileUploadResp>>() {
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

            // 3. 成功：返回数据
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
