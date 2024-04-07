package com.cqie.controller;


import com.cqie.dto.dataAsset.DataAssetDto;
import com.cqie.service.DataAssetService;
import com.cqie.util.R.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 数据资产表 前端控制器
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@RestController
@RequestMapping("/dataAsset")
@Slf4j
public class DataAssetController {

    @Resource
    private DataAssetService dataAssetService;

    /**
     * 新增资产表
     */
    @PostMapping("addAsset")
    @ApiOperation("新增资产表")
    private Result<Object> addStandard(@RequestBody DataAssetDto dataAssetDto) {
        log.info("新增资产表{}", dataAssetDto);
        return Result.success(dataAssetService.addStandard(dataAssetDto));
    }
}

