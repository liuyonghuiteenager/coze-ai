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

    @PostMapping("/create")
    public ResultUtil<String> createDatasets(@RequestBody CreateDatasetsReq createDatasetsReq) {
        return ResultUtil.success(cozeDatasetsService.createDatasets(createDatasetsReq));
    }

    @GetMapping("/view")
    public ResultUtil<DatasetPageResp> viewDatasets(@RequestParam String spaceId) {
        return ResultUtil.success(cozeDatasetsService.viewDatasets(spaceId));
    }

    @PutMapping("/update/{datasetId}")
    public ResultUtil<String> updateDatasets(@PathVariable String datasetId, @RequestBody UpdateDatasetsReq updateDatasetsReq) {
        return ResultUtil.success(cozeDatasetsService.updateDatasets(datasetId, updateDatasetsReq));
    }

    @DeleteMapping("/delete/{datasetId}")
    public ResultUtil<String> deleteDatasets(@PathVariable String datasetId) {
        return ResultUtil.success(cozeDatasetsService.deleteDatasets(datasetId));
    }

    @PostMapping("/process/{datasetId}")
    public ResultUtil<DatasetsProcessResp> processDatasets(@PathVariable String datasetId, @RequestBody List<String> documentIds) {
        return ResultUtil.success(cozeDatasetsService.processDatasets(datasetId, documentIds));
    }

    @GetMapping("/images/{datasetId}")
    public ResultUtil<DatasetsImagesResp> imagesDatasets(@PathVariable String datasetId,
                                                         @RequestParam(required = false) Integer page_num,
                                                         @RequestParam(required = false) Integer page_size,
                                                         @RequestParam(required = false) String keyword,
                                                         @RequestParam(required = false) Boolean has_caption) {
        return ResultUtil.success(cozeDatasetsService.listDatasetImages(datasetId, page_num, page_size, keyword, has_caption));
    }
}
