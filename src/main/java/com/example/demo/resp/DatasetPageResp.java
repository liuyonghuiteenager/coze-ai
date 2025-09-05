package com.example.demo.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Coze 知识库（Dataset）实体类
 */
@Data
public class DatasetPageResp {

    @JsonProperty("total_count")
    private Integer totalCount;
    @JsonProperty("dataset_list")
    private List<Dataset> datasetList;


    @Data
    public static class Dataset {

        /**
         * 知识库 ID
         */
        @JsonProperty("dataset_id")
        private String datasetId;

        /**
         * 所属空间 ID
         */
        @JsonProperty("space_id")
        private String spaceId;

        /**
         * 知识库名称
         */
        private String name;

        /**
         * 描述
         */
        private String description;

        /**
         * 创建时间（Unix 时间戳秒）
         */
        @JsonProperty("create_time")
        private Long createTime;

        /**
         * 创建时间（转换为 LocalDateTime，非 JSON 字段）
         */
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private LocalDateTime createTimeFormatted;

        /**
         * 更新时间（Unix 时间戳秒）
         */
        @JsonProperty("update_time")
        private Long updateTime;

        /**
         * 更新时间（转换为 LocalDateTime）
         */
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private LocalDateTime updateTimeFormatted;

        /**
         * 创建者用户 ID
         */
        @JsonProperty("creator_id")
        private String creatorId;

        /**
         * 创建者用户名
         */
        @JsonProperty("creator_name")
        private String creatorName;

        /**
         * 图标 URL
         */
        @JsonProperty("icon_url")
        private String iconUrl;

        /**
         * 图标 URI（内部使用）
         */
        @JsonProperty("icon_uri")
        private String iconUri;

        /**
         * 头像 URL（创建者）
         */
        @JsonProperty("avatar_url")
        private String avatarUrl;

        /**
         * 状态：1=正常，其他=异常或处理中
         */
        private Integer status;

        /**
         * 是否可编辑
         */
        @JsonProperty("can_edit")
        private Boolean canEdit;

        /**
         * 文档数量
         */
        @JsonProperty("doc_count")
        private Integer docCount;

        /**
         * 总文件大小（字节）
         */
        @JsonProperty("all_file_size")
        private Long allFileSize;

        /**
         * 切片数量
         */
        @JsonProperty("slice_count")
        private Integer sliceCount;

        /**
         * 命中次数（被 Bot 调用次数）
         */
        @JsonProperty("hit_count")
        private Integer hitCount;

        /**
         * 被 Bot 使用的次数
         */
        @JsonProperty("bot_used_count")
        private Integer botUsedCount;

        /**
         * 格式类型（0=通用，其他值可能代表不同文档类型）
         */
        @JsonProperty("format_type")
        private Integer formatType;

        /**
         * 项目 ID（可能为空）
         */
        @JsonProperty("project_id")
        private String projectId;

        // =============== 嵌套对象（简化为忽略或字符串）===============
        // 以下字段为对象或列表类型，如需完整解析可进一步拆分
        // 当前简化处理，避免复杂依赖

        /**
         * 处理中的文件 ID 列表（忽略，暂不解析）
         */
        @JsonProperty("processing_file_id_list")
        private Object processingFileIdList;

        /**
         * 处理中的文件列表（忽略）
         */
        @JsonProperty("processing_file_list")
        private Object processingFileList;

        /**
         * 失败的文件列表（忽略）
         */
        @JsonProperty("failed_file_list")
        private Object failedFileList;

        /**
         * 分块策略（忽略）
         */
        @JsonProperty("chunk_strategy")
        private Object chunkStrategy;
    }
}
