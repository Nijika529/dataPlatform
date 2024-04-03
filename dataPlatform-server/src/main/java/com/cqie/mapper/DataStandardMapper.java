package com.cqie.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqie.entity.DataStandard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqie.vo.dataStandard.DataStandardPageVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    //获取中文名
    @Select("select data_standard_cn_name from data_standard where delete_flag = 0")
    List<String> selectCnName();

    //获取英文名
    @Select("select data_standard_en_name from data_standard where delete_flag = 0")
    List<String> selectEnName();

    int insertForeach(List<DataStandard> standardTemplates2);

    int updateBatch(@Param("dataStandardCode") List<String> dataStandardCode, @Param("dataStandardState") Integer dataStandardState);

    List<String> selectPublish(@Param("dataStandardCode") List<String> dataStandardCode);
}
