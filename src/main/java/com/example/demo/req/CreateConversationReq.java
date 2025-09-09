package com.example.demo.req;

import lombok.Data;

@Data
public class CreateConversationReq {

    private String bot_id;
    private String user_id;
    private String name;
    private String connector_id;
}
