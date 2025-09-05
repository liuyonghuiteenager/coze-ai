package com.example.demo.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CozeChatResp {

    private String content;
    @JsonProperty("conversation_id")
    private String conversationId;
    @JsonProperty("chat_id")
    private String chatId;
    @JsonProperty("created_at")
    private Long timestamp;

}
