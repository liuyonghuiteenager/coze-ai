package com.example.demo.controller;

import com.example.demo.resp.CozeResp;
import com.example.demo.resp.FileUploadResp;
import com.example.demo.resp.ResultUtil;
import com.example.demo.service.CozeFilesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/files")
public class CozeFilesController {

    private final CozeFilesService cozeFilesService;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResultUtil<FileUploadResp> uploadFiles(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResultUtil.error("文件不能为空");
        }

        try {
            // 保存到临时文件
            File tempFile = File.createTempFile("upload_", "_" + file.getOriginalFilename());
            file.transferTo(tempFile);

            // 调用 Coze 上传
            CozeResp<FileUploadResp> result = cozeFilesService.uploadFiles(tempFile.getAbsolutePath());

            // 删除临时文件
            tempFile.deleteOnExit();

            if (result.getCode() != 0) {
                return ResultUtil.error("Coze 上传失败: " + result.getMsg());
            }

            return ResultUtil.success(result.getData(), "上传成功");

        } catch (Exception e) {
            log.error("文件处理失败", e);
            return ResultUtil.error("上传失败: " + e.getMessage());
        }
    }

    /**
     * 查看文件详情
     * @param fileId
     * @return
     */
    @GetMapping("/retrieve/{fileId}")
    public ResultUtil<FileUploadResp> retrieveFiles(@PathVariable String fileId) {
        return ResultUtil.success(cozeFilesService.retrieveFiles(fileId));
    }
}
