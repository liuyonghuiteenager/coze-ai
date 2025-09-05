package com.example.demo.service;

import com.example.demo.config.CozeConfig;
import com.example.demo.req.CreateDatasetsReq;
import com.example.demo.req.CreateDocumentReq;
import com.example.demo.req.UpdateDatasetsReq;
import com.example.demo.resp.CozeResp;
import com.example.demo.resp.DatasetPageResp;
import com.example.demo.resp.DatasetsImagesResp;
import com.example.demo.resp.DatasetsProcessResp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CozeDatasetsService {

    private final CozeConfig cozeConfig;

    private final RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public String createDatasets(CreateDatasetsReq createDatasetsReq) {
        String url = cozeConfig.getApiUrl() + "/v1/datasets";

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(cozeConfig.getApiKey());

        HttpEntity<CreateDatasetsReq> entity = new HttpEntity<>(createDatasetsReq, headers);

        try {
            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            // 结果处理
            CozeResp<Map> result = objectMapper.readValue(response.getBody(),
                    new TypeReference<CozeResp<Map>>() {
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
            return result.getData().get("dataset_id").toString();

        } catch (RuntimeException re) {
            // 已包装的业务异常，直接抛出
            throw re;
        } catch (Exception e) {
            // 其他异常（网络、JSON 解析等）
            throw new RuntimeException("调用 Coze API 失败: " + e.getMessage(), e);
        }
    }

    public DatasetPageResp viewDatasets(String spaceId) {
        String url = cozeConfig.getApiUrl() + "/v1/datasets?space_id=" + spaceId;

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(cozeConfig.getApiKey());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            CozeResp<DatasetPageResp> result = objectMapper.readValue(response.getBody(),
                    new TypeReference<CozeResp<DatasetPageResp>>() {
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

    public String updateDatasets(String datasetId, UpdateDatasetsReq updateDatasetsReq) {
        String url = cozeConfig.getApiUrl() + "/v1/datasets/" + datasetId;

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(cozeConfig.getApiKey());

        HttpEntity<UpdateDatasetsReq> entity = new HttpEntity<>(updateDatasetsReq, headers);

        try {
            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

            CozeResp<String> result = objectMapper.readValue(response.getBody(),
                    new TypeReference<CozeResp<String>>() {
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

    public String deleteDatasets(String datasetId) {
        String url = cozeConfig.getApiUrl() + "/v1/datasets/" + datasetId;

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(cozeConfig.getApiKey());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

            CozeResp<String> result = objectMapper.readValue(response.getBody(),
                    new TypeReference<CozeResp<String>>() {
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

    public DatasetsProcessResp processDatasets(String datasetId, List<String> documentIds) {
        String url = cozeConfig.getApiUrl() + "/v1/datasets/" + datasetId + "/process";

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(cozeConfig.getApiKey());

        // 组装请求参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("document_ids", documentIds);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(paramMap, headers);

        try {
            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            CozeResp<DatasetsProcessResp> result = objectMapper.readValue(response.getBody(),
                    new TypeReference<CozeResp<DatasetsProcessResp>>() {
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

    public DatasetsImagesResp listDatasetImages(String datasetId, Integer pageNum, Integer pageSize, String keyword, Boolean hasCaption) {
        // 构建 URL
        String url = cozeConfig.getApiUrl() + "/v1/datasets/" + datasetId + "/images";

        // 构建查询参数
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParamIfPresent("page_num", Optional.ofNullable(pageNum))
                .queryParamIfPresent("page_size", Optional.ofNullable(pageSize))
                .queryParamIfPresent("keyword", Optional.ofNullable(keyword))
                .queryParamIfPresent("has_caption", Optional.ofNullable(hasCaption));

        URI uri = builder.buildAndExpand(datasetId).encode().toUri();

        // 设置 Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(cozeConfig.getApiKey());
        // GET 请求不需要 Content-Type

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            CozeResp<DatasetsImagesResp> result = objectMapper.readValue(response.getBody(),
                    new TypeReference<CozeResp<DatasetsImagesResp>>() {
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
