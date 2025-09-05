package com.example.demo.resp;

import lombok.Data;

import java.util.List;

@Data
public class DatasetsProcessResp {

    /**
     * 文件的上传进度详情。
     */
    private List<ProcessInfo> data;

    @Data
    public static class ProcessInfo {
        private String document_id;
        private String document_name;
        private Integer progress;
        private Integer remaining_time;
        private Integer size;
        private Integer status;
        private String status_descript;
        private String type;
        private Integer update_interval;
        private Integer update_type;
        private String url;
    }
}
