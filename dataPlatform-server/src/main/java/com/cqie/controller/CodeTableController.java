package com.cqie.controller;


import com.cqie.dto.codetable.*;
import com.cqie.service.CodeTableService;
import com.cqie.util.R.Result;
import com.cqie.util.file.FileUtil;
import com.cqie.vo.codetablevo.CodeTableValueVo;
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
public class CodeTableController {

    @Resource
    private CodeTableService codeTableService;

    //新增
    @PostMapping("add")
    public Result<Object> save(@RequestBody @Valid CodeTableDto codeTableDto) {
        return codeTableService.saveT(codeTableDto);
    }

    //逻辑删除
    @DeleteMapping("{id}")
    public Result<String> deleteId(@PathVariable("id") Integer id) {
        return codeTableService.deleteId(id);
    }

    //分页查询
    @GetMapping("page")
    public Result<Object> page(CodeTablePageDto pageDto) {
        return codeTableService.pageT(pageDto);
    }

    //批量发布
    @PutMapping("state")
    public Result<Object> state(@RequestBody @Valid CodeTableStateDto codeTableStateDto) {
        return codeTableService.state(codeTableStateDto);
    }

    //根据id查询码表
    @GetMapping("getById/{id}")
    public Result<CodeTableValueVo> getById(@PathVariable("id") Integer id) {
        return codeTableService.getByIdId(id);
    }

    //修改码表
    @PutMapping
    private Result<Object> updateTable(@RequestBody @Valid CodeTableUpdateDto codeTableUpdateDto) {
        return codeTableService.updateTable(codeTableUpdateDto);
    }

    //查询所有码表
    @GetMapping("selectAllTable")
    private Result<Object> selectAllTable() {
        return codeTableService.selectAllTable();
    }

    //读取文件
    @PostMapping("upload")
    private Result<Object> upload(@RequestParam("file") MultipartFile file) throws Exception {
        return codeTableService.upload(file);
    }

    //模板下载
    @GetMapping("download")
    private ResponseEntity<org.springframework.core.io.Resource> download() throws IOException {
        String fileName = "码表模板.xlsx";
        return FileUtil.downloadFile(fileName, FileUtil.DATA_CODE);
    }
}

