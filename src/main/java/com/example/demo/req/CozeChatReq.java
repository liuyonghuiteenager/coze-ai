package com.example.demo.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CozeChatReq {

    @JsonProperty("bot_id")
    private String bot_id;

    @JsonProperty("user_id")
    private String user_id;

    private String conversationId;

    private String message;

    @JsonProperty("stream")
    private boolean stream = true;

    @JsonProperty("auto_save_history")
    private boolean auto_save_history = true;

    @JsonProperty("additional_messages")
    private List<Message> additional_messages;

    @Data
    public static class Message {
        private String role;
        private String content;
        @JsonProperty("type")
        private String type = "question";
        @JsonProperty("content_type")
        private String content_type = "text";

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
