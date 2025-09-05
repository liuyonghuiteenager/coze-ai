package com.example.demo.req;

import lombok.Data;

@Data
public class UpdateDatasetsReq {

    /**
     * 知识库名称
     */
    private String name;

    /**
     * 知识库图标
     */
    private String file_id;

    /**
     * 知识库描述信息
     */
    private String description;

}
