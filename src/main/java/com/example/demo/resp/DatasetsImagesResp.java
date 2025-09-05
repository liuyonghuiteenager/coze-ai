package com.example.demo.resp;

import lombok.Data;

import java.util.List;

@Data
public class DatasetsImagesResp {

    /**
     *
     */
    private Integer total_count;

    private List<photoInfos> photo_infos;

    @Data
    public static class photoInfos {
        private String caption;
        private Integer create_time;
        private String creator_id;
        private String document_id;
        private String name;
        private Integer size;
        private Integer source_type;
        private Integer status;
        private String type;
        private Integer update_time;
        private String url;
    }
}
