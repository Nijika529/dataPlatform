package com.cqie.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqie.entity.SourceDatabase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqie.vo.sourcedatabasevo.SourceDatabasePageVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 数据库数据源 Mapper 接口
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
public interface SourceDatabaseMapper extends BaseMapper<SourceDatabase> {
    @Select("select * from source_database ${ew.customSqlSegment}")
    Page<SourceDatabasePageVO> page(Page<SourceDatabasePageVO> page,@Param(Constants.WRAPPER) QueryWrapper<SourceDatabase> wrapper);
}
