package com.example.demo.req;

import lombok.Data;

import java.util.List;

@Data
public class CreateDocumentReq {

    /**
     * 知识库id
     */
    private String dataset_id;

    /**
     * 知识库类型: 0：文本类型, 2：图片类型
     */
    private Integer format_type;

    /**
     * 待上传文件的元数据信息
     */
    private List<DocumentBase> document_bases;

    /**
     * 分段信息
     */
    private ChunkStrategy chunk_strategy;

    @Data
    public static class DocumentBase {
        /**
         * 文件名称
         */
        private String name;

        /**
         * 上传信息
         */
        private SourceInfo source_info;

        @Data
        public static class SourceInfo {
            /**
             * 本地文件的 Base64 编码
             */
            private String file_base64;

            /**
             * 本地文件格式, 例如 txt。格式支持 pdf、txt、doc、docx 类型
             */
            private String file_type;

            /**
             * 文件的上传方式: 0: 本地文件上传, 1: 表示上传在线网页。
             */
            private Integer document_source;
        }
    }

    @Data
    public static class ChunkStrategy {
        /**
         * 知识库类型: 0：文本类型, 2：图片类型
         */
        private Integer chunk_type;
    }
}
