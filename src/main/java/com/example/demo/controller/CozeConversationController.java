package com.example.demo.controller;

import com.example.demo.resp.CreateConversationResp;
import com.example.demo.resp.ResultUtil;
import com.example.demo.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/conversation")
public class CozeConversationController {

    private final ConversationService conversationService;

    /**
     * 创建新会话
     */
    @PostMapping("/create")
    public ResultUtil<CreateConversationResp> create(@RequestParam(required = false) String userId) {
        return ResultUtil.success(conversationService.createConversation(userId));
    }
}
