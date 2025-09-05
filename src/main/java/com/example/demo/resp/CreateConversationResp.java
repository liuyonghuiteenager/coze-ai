package com.example.demo.resp;

import lombok.Data;

@Data
public class CreateConversationResp {

    private String Id;
    private String name;
    private String connector_id;
    private String last_section_id;
    private Object meta_data;
    private String creator_id;
    private Integer created_at;
    private Integer updated_at;
}
