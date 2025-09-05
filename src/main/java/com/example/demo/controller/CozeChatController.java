package com.example.demo.controller;

import com.example.demo.req.CozeChatReq;
import com.example.demo.resp.CozeChatResp;
import com.example.demo.resp.ResultUtil;
import com.example.demo.service.CozeChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/chat")
public class CozeChatController {

    private final CozeChatService cozeChatService;

//    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<ServerSentEvent<String>> chatStream(@RequestParam String conversationId,
//                                                    @RequestParam String userId,
//                                                    @RequestParam String message) {
//
//        return cozeStreamService.chatStream(conversationId, userId, message)
//                .map(content -> {
//                    if (content.equals("[DONE]")) {
//                        return ServerSentEvent.<String>builder()
//                                .event("end")
//                                .data("【AI：回复结束】")
//                                .build();
//                    }
//                    return ServerSentEvent.<String>builder()
//                            .event("message")
//                            .data(content)
//                            .build();
//                });
//    }


    /**
     * 发起对话
     */
    @PostMapping("/stream")
    public ResultUtil<CozeChatResp> chatStream(@RequestBody CozeChatReq cozeChatReq) {
        CozeChatResp cozeChatResp = cozeChatService.chat(cozeChatReq);
        return ResultUtil.success(cozeChatResp);
    }
}
