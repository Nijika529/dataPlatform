package com.cqie.controller;


import com.cqie.dto.codetable.*;
import com.cqie.service.CodeTableService;
import com.cqie.util.R.Result;
import com.cqie.util.file.FileUtil;
import com.cqie.vo.codetablevo.CodeTableValueVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;

/**
 * <p>
 * 码表管理 前端控制器
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@RestController
@RequestMapping("/codeTable")
@Slf4j
public class CodeTableController {

    @Resource
    private CodeTableService codeTableService;

    //新增
    @PostMapping("add")
    public Result<Object> save(@RequestBody @Valid CodeTableDto codeTableDto) {
        log.info("开始新增码表:{}", codeTableDto);
        return codeTableService.saveT(codeTableDto);
    }

    //逻辑删除
    @DeleteMapping("{id}")
    public Result<String> deleteId(@PathVariable("id") Integer id) {
        log.info("开始逻辑删除码表:{}", id);
        return codeTableService.deleteId(id);
    }

    //分页查询
    @GetMapping("page")
    public Result<Object> page(CodeTablePageDto pageDto) {
        log.info("开始分页查询码表:{}", pageDto);
        return codeTableService.pageT(pageDto);
    }

    //批量发布
    @PutMapping("state")
    public Result<Object> state(@RequestBody @Valid CodeTableStateDto codeTableStateDto) {
        log.info("开始批量发布码表:{}", codeTableStateDto);
        return codeTableService.state(codeTableStateDto);
    }

    //根据id查询码表
    @GetMapping("getById/{id}")
    public Result<CodeTableValueVo> getById(@PathVariable("id") Integer id) {
        log.info("开始根据id查询码表:{}", id);
        return codeTableService.getByIdId(id);
    }

    //修改码表
    @PutMapping
    private Result<Object> updateTable(@RequestBody @Valid CodeTableUpdateDto codeTableUpdateDto) {
        log.info("开始修改码表:{}", codeTableUpdateDto);
        return codeTableService.updateTable(codeTableUpdateDto);
    }

    //查询所有码表
    @GetMapping("selectAllTable")
    private Result<Object> selectAllTable() {
        log.info("开始查询所有码表");
        return codeTableService.selectAllTable();
    }

    //读取文件
    @PostMapping("upload")
    private Result<Object> upload(@RequestParam("file") MultipartFile file) throws Exception {
        log.info("开始读取码表文件");
        return codeTableService.upload(file);
    }

    //模板下载
    @GetMapping("download")
    private ResponseEntity<org.springframework.core.io.Resource> download() throws IOException {
        log.info("开始模板下载");
        String fileName = "码表模板.xlsx";
        return FileUtil.downloadFile(fileName, FileUtil.DATA_CODE);
    }
}

