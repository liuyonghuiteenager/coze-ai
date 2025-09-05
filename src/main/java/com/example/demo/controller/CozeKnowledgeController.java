package com.example.demo.controller;

import com.example.demo.req.CreateDocumentReq;
import com.example.demo.req.ViewDocumentReq;
import com.example.demo.entity.DocumentInfos;
import com.example.demo.resp.ResultUtil;
import com.example.demo.service.CozeKnowledgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/knowledge/document")
public class CozeKnowledgeController {

    private final CozeKnowledgeService cozeKnowledgeService;


    @PostMapping("/create")
    public ResultUtil<List<DocumentInfos>> createDocument(@RequestBody CreateDocumentReq createDocumentReq) {
        return ResultUtil.success(cozeKnowledgeService.createDocument(createDocumentReq));
    }

    @PostMapping("/view")
    public ResultUtil<List<DocumentInfos>> viewDocument(@RequestBody ViewDocumentReq viewDocumentReq) {
        return ResultUtil.success(cozeKnowledgeService.viewDocument(viewDocumentReq));
    }

    @PostMapping("/delete")
    public ResultUtil<String> deleteDocument(@RequestBody List<String> documentIds) {
        return ResultUtil.success(cozeKnowledgeService.deleteDocument(documentIds));
    }
}
