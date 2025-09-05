package com.example.demo.req;

import lombok.Data;

@Data
public class ViewDocumentReq extends BasePageReq {

    /**
     * 知识库ID
     */
    private String dataset_id;

}
