package com.cqie.service;

import com.cqie.dto.sourceDatabase.SourceDataUpdateDto;
import com.cqie.dto.sourceDatabase.SourceDatabaseDto;
import com.cqie.dto.sourceDatabase.SourceDatabaseLinkDto;
import com.cqie.dto.sourceDatabase.SourceDatabasePageDto;
import com.cqie.entity.SourceDatabase;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqie.util.R.Result;

import java.sql.SQLException;

/**
 * <p>
 * 数据库数据源 服务类
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
public interface SourceDatabaseService extends IService<SourceDatabase> {

    Result<Object> add(SourceDatabaseDto sourceDatabaseDto);

    Result<Object> state(Integer id, Integer state);

    Result<String> deleteId(Integer id);

    Result<Object> pageByDatabase(SourceDatabasePageDto sourceDatabasePageDto);

    Result<Object> editDatabase(SourceDataUpdateDto sourceDataUpdateDto);

    Result<Object> addLink(SourceDatabaseLinkDto sourceDatabaseLinkDto) throws SQLException;

    Result<Object> link(SourceDatabaseLinkDto sourceDatabaseLinkDto);

    Result<Object> updateLink(SourceDatabaseLinkDto sourceDatabaseLinkDto);
}
