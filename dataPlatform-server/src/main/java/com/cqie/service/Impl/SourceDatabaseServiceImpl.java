package com.cqie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqie.dto.sourceDatabase.SourceDataUpdateDto;
import com.cqie.dto.sourceDatabase.SourceDatabaseDto;
import com.cqie.dto.sourceDatabase.SourceDatabaseLinkDto;
import com.cqie.dto.sourceDatabase.SourceDatabasePageDto;
import com.cqie.entity.SourceDatabase;
import com.cqie.mapper.SourceDatabaseMapper;
import com.cqie.service.SourceDatabaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqie.util.AES.EncryptionUtil;
import com.cqie.util.R.Result;
import com.cqie.vo.sourcedatabasevo.SourceDatabasePageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;

/**
 * <p>
 * 数据库数据源 服务实现类
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@Service
@Slf4j
public class SourceDatabaseServiceImpl extends ServiceImpl<SourceDatabaseMapper, SourceDatabase> implements SourceDatabaseService {

    @Resource
    private SourceDatabaseMapper sourceDatabaseMapper;


    @Override
    public Result<Object> add(SourceDatabaseDto sourceDatabaseDto) {

        //校验“数据源名称”与“连接信息两个字段的唯一性
        Long count = lambdaQuery().eq(SourceDatabase::getDatabaseSourceName, sourceDatabaseDto.getDatabaseSourceUsername()).count();
        Long countUrl = lambdaQuery().eq(SourceDatabase::getDatabaseSourceUrl, sourceDatabaseDto.getDatabaseSourceUrl()).count();
        if (count != 0) {
            return Result.failed(null, 0, "数据库数据源名称已存在");
        }
        if (countUrl != 0) {
            return Result.failed(null, 0, "此数据库连接信息已存在");
        }
        StringBuilder username = new StringBuilder();
        StringBuilder password = new StringBuilder();
        //序列化字符串
        //加密
        try {
            username.append(EncryptionUtil.encrypt(sourceDatabaseDto.getDatabaseSourceName()));
            password.append(EncryptionUtil.encrypt(sourceDatabaseDto.getDatabaseSourcePassword()));
        } catch (Exception e){
            log.info("error:{}", e.getMessage());
        }
        //存入数据
        SourceDatabase sourceDatabase = SourceDatabase.builder()
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .databaseSourceName(sourceDatabaseDto.getDatabaseSourceName())
                .databaseSourceDesc(sourceDatabaseDto.getDatabaseSourceDesc())
                .databaseSourceUrl(sourceDatabaseDto.getDatabaseSourceUrl())
                .databaseSourceType(sourceDatabaseDto.getDatabaseSourceType())
                .databaseSourceUsername(username.toString())
                .databaseSourcePassword(password.toString())
                .build();
        //插入
        sourceDatabaseMapper.insert(sourceDatabase);
        return Result.success(null);

    }

    @Override
    public Result<Object> state(Integer id, Integer state) {
       return lambdaUpdate()
               .set(SourceDatabase::getDatabaseSourceState, state)
               .set(SourceDatabase::getUpdateTime, LocalDateTime.now())
               .eq(SourceDatabase::getId, id)
               .update()
               ?Result.success(null)
               :Result.failed(null, 0, "修改状态失败");
    }

    @Override
    public Result<String> deleteId(Integer id) {
        Long count = lambdaQuery().eq(SourceDatabase::getId, id).count();
        if (count == 0) {
            Result.failed(0, null, "数据库不存在");
        }
        sourceDatabaseMapper.deleteById(id);
        return Result.success(null);
    }

    @Override
    public Result<Object> pageByDatabase(SourceDatabasePageDto sourceDatabasePageDto) {
        Page<SourceDatabasePageVO> page =new Page<>(sourceDatabasePageDto.getPageNumber(), sourceDatabasePageDto.getPageSize());
        QueryWrapper<SourceDatabase> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(StringUtils.isNotEmpty(sourceDatabasePageDto.getDatabaseSourceName()), SourceDatabase::getDatabaseSourceName, sourceDatabasePageDto.getDatabaseSourceName())
                .eq(sourceDatabasePageDto.getDatabaseSourceState() != null, SourceDatabase::getDatabaseSourceState, sourceDatabasePageDto.getDatabaseSourceState());
        sourceDatabaseMapper.page(page, wrapper);
        page.getRecords().forEach(records -> {
            try {
                String decrypt = EncryptionUtil.decrypt(records.getDatabaseSourceUsername());
                records.setDatabaseSourceUsername(decrypt);
            }catch (Exception e) {
                log.info("error:{}", e.getMessage());
            }
        });
        return Result.success(page);
    }

    @Override
    public Result<Object> editDatabase(SourceDataUpdateDto sourceDataUpdateDto) {
        Long count = lambdaQuery().eq(SourceDatabase::getDatabaseSourceName, sourceDataUpdateDto.getDatabaseSourceName())
                .ne(SourceDatabase::getId, sourceDataUpdateDto.getId()).count();
        if (count != 0) {
            return Result.failed(null, 0, "数据源名称已存在");
        }
        Long count1 = lambdaQuery().eq(SourceDatabase::getDatabaseSourceUrl, sourceDataUpdateDto.getDatabaseSourceUrl())
                .ne(SourceDatabase::getId, sourceDataUpdateDto.getId()).count();
        if (count1 != 0) {
            return Result.failed(null, 0, "JDBC URL已存在");
        }
        //更改的数据
        SourceDatabase sourceDatabase = SourceDatabase.builder()
                .databaseSourcePassword(sourceDataUpdateDto.getDatabaseSourcePassword())
                .databaseSourceName(sourceDataUpdateDto.getDatabaseSourceName())
                .databaseSourceDesc(sourceDataUpdateDto.getDatabaseSourceDesc())
                .databaseSourceUrl(sourceDataUpdateDto.getDatabaseSourceUrl())
                .updateTime(LocalDateTime.now())
                .id(sourceDataUpdateDto.getId())
                .build();
        //判断密码更改和加密
        SourceDatabase database = lambdaQuery().eq(SourceDatabase::getId, sourceDataUpdateDto.getId()).one();
        if (!database.getDatabaseSourcePassword().equals(sourceDataUpdateDto.getDatabaseSourcePassword())) {
            try {
                String password = EncryptionUtil.encrypt(sourceDataUpdateDto.getDatabaseSourcePassword());
                sourceDatabase.setDatabaseSourcePassword(password);
            }catch (Exception e) {
                log.info("error:{}", e.getMessage());
            }
        }
        //用户名加密
        try {
            String username = EncryptionUtil.encrypt(sourceDataUpdateDto.getDatabaseSourceUsername());
            sourceDatabase.setDatabaseSourceUsername(username);
        }catch (Exception e) {
            log.info("error:{}", e.getMessage());
        }
        //修改
        sourceDatabaseMapper.updateById(sourceDatabase);

        return Result.success(null);
    }
    //新增的连接测试
    @Override
    public Result<Object> addLink(SourceDatabaseLinkDto sourceDatabaseLinkDto) {
        try {
            Connection connection = DriverManager.getConnection(sourceDatabaseLinkDto.getDatabaseSourceUrl(),
                    sourceDatabaseLinkDto.getDatabaseSourceUsername(),
                    sourceDatabaseLinkDto.getDatabaseSourcePassword());
            if (!connection.isClosed()) {
                connection.close();
                return Result.success(null);
            }
        }catch (Exception e) {
            log.info("error:{}", e.getMessage());
        }
        return Result.failed(null, 0, "连接mysql数据库错误");
    }
    //查询的连接测试
    @Override
    public Result<Object> link(SourceDatabaseLinkDto sourceDatabaseLinkDto) {
        try {
            String password = EncryptionUtil.decrypt(sourceDatabaseLinkDto.getDatabaseSourcePassword());
            sourceDatabaseLinkDto.setDatabaseSourcePassword(password);
        }catch (Exception e) {
            log.info("error:{}", e.getMessage());
        }
        return addLink(sourceDatabaseLinkDto);
    }
    //修改的连接测试
    @Override
    public Result<Object> updateLink(SourceDatabaseLinkDto sourceDatabaseLinkDto) {
        SourceDatabase database = lambdaQuery().eq(SourceDatabase::getId, sourceDatabaseLinkDto.getId()).one();
        try {
            if (database.getDatabaseSourcePassword().equals(sourceDatabaseLinkDto.getDatabaseSourcePassword())) {
                String password = EncryptionUtil.decrypt(sourceDatabaseLinkDto.getDatabaseSourcePassword());
                sourceDatabaseLinkDto.setDatabaseSourcePassword(password);
            }
        }catch (Exception e) {
            log.info("error:{}", e.getMessage());
            return Result.failed(null,0, e.getMessage());
        }
        return addLink(sourceDatabaseLinkDto);
    }
}
