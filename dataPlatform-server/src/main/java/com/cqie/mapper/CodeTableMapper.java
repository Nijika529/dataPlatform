package com.cqie.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqie.entity.CodeTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqie.vo.codetablevo.CodeTablePageVo;
import com.cqie.vo.codetablevo.CodeTableValueVo;
import com.cqie.vo.codetablevo.CodeValueValueVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 码表管理 Mapper 接口
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
public interface CodeTableMapper extends BaseMapper<CodeTable> {

    Page<CodeTablePageVo> selectCodeTablePage(Page<CodeTablePageVo> page, @Param(Constants.WRAPPER) QueryWrapper<CodeTable> queryWrapper);

    CodeTableValueVo getByIdTable(Integer id);

    List<CodeValueValueVo> getById(Integer id);

    List<CodeTableValueVo> selectTableList();


    CodeTableValueVo selectDataStandard(@Param("codeTableNumber") String dataStandardEnumerationRange);

}
