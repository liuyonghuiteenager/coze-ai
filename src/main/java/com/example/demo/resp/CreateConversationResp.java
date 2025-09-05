package com.example.demo.resp;

import lombok.Data;

@Data
public class CreateConversationResp {
    private int code;
    private String msg;
    private ConversationData  data;

    @Data
    public static class ConversationData  {
        private String id;        // conversation_id
        private String bot_id;
        private String user_id;
        private int status;
        private long create_time;
        private long update_time;
    }
}
