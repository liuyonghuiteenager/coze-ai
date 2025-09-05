package com.example.demo.controller;

import com.example.demo.req.CreateDatasetsReq;
import com.example.demo.req.UpdateDatasetsReq;
import com.example.demo.resp.DatasetPageResp;
import com.example.demo.resp.DatasetsImagesResp;
import com.example.demo.resp.DatasetsProcessResp;
import com.example.demo.resp.ResultUtil;
import com.example.demo.service.CozeDatasetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/datasets")
public class CozeDatasetsController {

    private final CozeDatasetsService cozeDatasetsService;

    /**
     * 创建知识库
     * @param createDatasetsReq
     * @return
     */
    @PostMapping("/create")
    public ResultUtil<String> createDatasets(@RequestBody CreateDatasetsReq createDatasetsReq) {
        return ResultUtil.success(cozeDatasetsService.createDatasets(createDatasetsReq));
    }

    /**
     * 查看知识库列表
     * @param spaceId
     * @return
     */
    @GetMapping("/view")
    public ResultUtil<DatasetPageResp> viewDatasets(@RequestParam String spaceId) {
        return ResultUtil.success(cozeDatasetsService.viewDatasets(spaceId));
    }

    /**
     * 更新知识库
     * @param datasetId
     * @param updateDatasetsReq
     * @return
     */
    @PutMapping("/update/{datasetId}")
    public ResultUtil<String> updateDatasets(@PathVariable String datasetId, @RequestBody UpdateDatasetsReq updateDatasetsReq) {
        return ResultUtil.success(cozeDatasetsService.updateDatasets(datasetId, updateDatasetsReq));
    }

    /**
     * 删除知识库
     * @param datasetId
     * @return
     */
    @DeleteMapping("/delete/{datasetId}")
    public ResultUtil<String> deleteDatasets(@PathVariable String datasetId) {
        return ResultUtil.success(cozeDatasetsService.deleteDatasets(datasetId));
    }

    /**
     * 查看知识库文件上传进度
     * @param datasetId
     * @param documentIds
     * @return
     */
    @PostMapping("/process/{datasetId}")
    public ResultUtil<DatasetsProcessResp> processDatasets(@PathVariable String datasetId, @RequestBody List<String> documentIds) {
        return ResultUtil.success(cozeDatasetsService.processDatasets(datasetId, documentIds));
    }

    /**
     * 查看知识库图片列表
     * @param datasetId
     * @param page_num
     * @param page_size
     * @param keyword
     * @param has_caption
     * @return
     */
    @GetMapping("/images/{datasetId}")
    public ResultUtil<DatasetsImagesResp> imagesDatasets(@PathVariable String datasetId,
                                                         @RequestParam(required = false) Integer page_num,
                                                         @RequestParam(required = false) Integer page_size,
                                                         @RequestParam(required = false) String keyword,
                                                         @RequestParam(required = false) Boolean has_caption) {
        return ResultUtil.success(cozeDatasetsService.listDatasetImages(datasetId, page_num, page_size, keyword, has_caption));
    }
}
