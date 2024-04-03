package com.cqie.controller;


import com.cqie.dto.sourceDatabase.SourceDataUpdateDto;
import com.cqie.dto.sourceDatabase.SourceDatabaseDto;
import com.cqie.dto.sourceDatabase.SourceDatabaseLinkDto;
import com.cqie.dto.sourceDatabase.SourceDatabasePageDto;
import com.cqie.service.SourceDatabaseService;
import com.cqie.util.R.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.sql.SQLException;

/**
 * <p>
 * 数据库数据源 前端控制器
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@RestController
@RequestMapping("/database")
@Slf4j
public class SourceDatabaseController {

    @Resource
    private SourceDatabaseService sourceDatabaseService;
    //新增数据库
    @PostMapping("add")
    private Result<Object> add(@RequestBody @Valid SourceDatabaseDto sourceDatabaseDto) {
        log.info("开始新增数据库:{}", sourceDatabaseDto);
        return sourceDatabaseService.add(sourceDatabaseDto);
    }
    //改变数据库状态
    @PutMapping("state")
    private Result<Object> state(@RequestParam(name = "id") Integer id,
                                 @RequestParam(name = "state") Integer state ) {
        log.info("开始数据库状态修改:{}，{}", id, state);
        return sourceDatabaseService.state(id, state);
    }
    //删除数据库
    @DeleteMapping("{id}")
    private Result<String> deleteId(@PathVariable("id") Integer id) {
        log.info("开始删除数据库:{}", id);
        return sourceDatabaseService.deleteId(id);
    }
    //分页查询数据源
    @PostMapping("pageByDatabase")
    private Result<Object> pageByDatabase(@RequestBody SourceDatabasePageDto sourceDatabasePageDto) {
        log.info("开始分页查询数据源:{}", sourceDatabasePageDto);
        return sourceDatabaseService.pageByDatabase(sourceDatabasePageDto);
    }

    //编辑数据库
    @PutMapping("editDatabase")
    private Result<Object> editDatabase(@RequestBody @Valid SourceDataUpdateDto sourceDataUpdateDto) {
        log.info("开始编辑数据库:{}", sourceDataUpdateDto);
        return sourceDatabaseService.editDatabase(sourceDataUpdateDto);
    }

    //连接测试-新增界面
    @PostMapping("addLink")
    private Result<Object> addLink(@RequestBody @Valid SourceDatabaseLinkDto sourceDatabaseLinkDto) throws SQLException {
        log.info("开始连接测试-新增界面:{}", sourceDatabaseLinkDto);
        return sourceDatabaseService.addLink(sourceDatabaseLinkDto);
    }

    //连接测试-查询界面
    @PostMapping("link")
    private Result<Object> link(@RequestBody @Valid SourceDatabaseLinkDto sourceDatabaseLinkDto) {
        log.info("开始连接测试-查询界面:{}", sourceDatabaseLinkDto);
        return sourceDatabaseService.link(sourceDatabaseLinkDto);
    }

    //连接测试-修改界面
    @PostMapping("updateLink")
    private Result<Object> updateLink(@RequestBody @Valid SourceDatabaseLinkDto sourceDatabaseLinkDto) {
        log.info("开始连接测试-修改界面:{}", sourceDatabaseLinkDto);
        return sourceDatabaseService.updateLink(sourceDatabaseLinkDto);
    }
}

