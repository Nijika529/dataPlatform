package com.cqie.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqie.entity.DataStandard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqie.vo.dataStandard.DataStandardPageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据标准表 Mapper 接口
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */

public interface DataStandardMapper extends BaseMapper<DataStandard> {

    Page<DataStandardPageVO> page(IPage<DataStandardPageVO> page, @Param(Constants.WRAPPER) QueryWrapper<DataStandard> standardPageVO);

}
