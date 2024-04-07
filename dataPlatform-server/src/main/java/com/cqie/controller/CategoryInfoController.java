package com.cqie.controller;


import com.cqie.dto.category.CategoryDto;
import com.cqie.service.CategoryInfoService;
import com.cqie.util.R.Result;
import com.cqie.vo.category.CategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 分类信息表 前端控制器
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@RestController
@RequestMapping("/categoryInfo")
@Api("分类管理")
@Slf4j
public class CategoryInfoController {

    @Resource
    private CategoryInfoService categoryInfoService;

    /**
     * 新增分类
     */
    @PostMapping
    @ApiOperation("新增分类")
    private Result<Object> add(@RequestBody @Validated CategoryDto categoryDto) {
        log.info("开始新增分类 {}", categoryDto);
        return categoryInfoService.add(categoryDto);
    }

    /**
     * 获取ID获取分类目录接口
     */
    @GetMapping("getCategoryById")
    @ApiOperation("获取ID获取分类目录接口")
    private Result<List<CategoryVO>> getCategoryById(Integer categoryCode) {
        log.info("开始根据ID获取分类目录接口 {}", categoryCode);
        return categoryInfoService.getCategoryById(categoryCode);
    }

    /**
     * 修改分类
     */
    @PutMapping
    @ApiOperation("修改分类")
    private Result<Object> update(@RequestBody @Validated CategoryDto categoryDto) {
        log.info("开始修改分类 {}", categoryDto);
        return categoryInfoService.updateCategory(categoryDto);
    }

    /**
     *删除分类接口
     */
    @DeleteMapping
    @ApiOperation("删除分类接口")
    private Result<Object> delete(Integer categoryCode,Integer categoryType) {
        log.info("删除分类接口 {} {}", categoryCode, categoryType);
        return categoryInfoService.delete(categoryCode, categoryType);
    }

}

