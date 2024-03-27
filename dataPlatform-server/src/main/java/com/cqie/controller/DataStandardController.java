package com.cqie.controller;


import com.cqie.dto.dataStandard.StandardAddDto;
import com.cqie.dto.dataStandard.StandardGetAllDto;
import com.cqie.service.DataStandardService;
import com.cqie.util.R.Result;
import com.cqie.util.file.FileUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 数据标准表 前端控制器
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@RestController
@RequestMapping("/dataStandard")
public class DataStandardController {

    @Resource
    private DataStandardService dataStandardService;
    //新增
    @PostMapping("addStandard")
    private Result<Object> addStandard(@RequestBody @Valid StandardAddDto standardAddDto) {
        return dataStandardService.addStandard(standardAddDto);
    }

    //查询
    @PostMapping("getAll")
    private Result<Object> getAll(@RequestBody StandardGetAllDto standardGetAllDto) {
        return dataStandardService.getAll(standardGetAllDto);
    }

    //删除
    @PutMapping("delete/{dataStandardCode}")
    private Result<Object> delete(@PathVariable("dataStandardCode") String dataStandardCode) {
        return dataStandardService.delete(dataStandardCode);
    }
    //批量修改
    @PutMapping("updateState")
    private Result<Object> updateState(@Param("dataStandardCode") List<String> dataStandardCode, @Param("dataStandardState") Integer dataStandardState) {
        return dataStandardService.updateState(dataStandardCode, dataStandardState);
    }
    //下载模板
    @PostMapping("downloadTemplateFile")
    private ResponseEntity<org.springframework.core.io.Resource> downloadTemplateFile() throws IOException {
        String fileName = "数据标准.xlsx";
        return FileUtil.downloadFile(fileName, FileUtil.DATA_STANDARD_CODE);
    }
    //导入上传的模板
    @PostMapping("addTemplate")
    private Result<Object> addTemplate(@RequestParam("file") MultipartFile file) {
        return dataStandardService.addTemplate(file);
    }

}

