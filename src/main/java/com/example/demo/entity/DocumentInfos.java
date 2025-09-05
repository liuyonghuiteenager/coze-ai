package com.example.demo.entity;

import lombok.Data;

@Data
public class DocumentInfos {

    private Integer char_count;

    private ChunkStrategy chunk_strategy;

    private Long create_time;

    private String document_id;

    private String document_id_new;

    private Integer format_type;

    private Integer hit_count;

    private String name;

    private Long size;

    private Integer slice_count;

    private Integer source_type;

    private Integer status;

    private String tos_uri;

    private String type;

    private Integer update_interval;

    private Long update_time;

    private Integer update_type;

    private String web_url;

    @Data
    public static class ChunkStrategy {
        /**
         * 分段设置: 0：自动分段与清洗, 1：自定义
         */
        private Integer caption_type;

        /**
         * 分段设置: 0：自动分段与清洗, 1：自定义
         */
        private Integer chunk_type;

        /**
         * 最大分段长度
         */
        private Integer max_tokens;

        /**
         * 是否自动过滤连续的空格
         */
        private Boolean remove_extra_spaces;

        /**
         * 是否自动过滤所有 URL 和电子邮箱地址
         */
        private Boolean remove_urls_emails;

        /**
         * 分段标识符
         */
        private String separator;

    }
}
