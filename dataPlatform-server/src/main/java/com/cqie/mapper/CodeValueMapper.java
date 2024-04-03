package com.cqie.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.cqie.entity.CodeValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqie.vo.codetablevo.CodeValueValueVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 码值表 Mapper 接口
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
public interface CodeValueMapper extends BaseMapper<CodeValue> {

    void insertBatch(CodeValue codeValue);

    List<CodeValueValueVo> selectValueList(@Param(Constants.WRAPPER)QueryWrapper<CodeValue> queryWrapper);

    //查询在码值表中对应的数据类型

    @Select("select code_value_value from code_value where code_table_number = #{codeTableNumber}")
    List<String> selectType(String codeTableNumber);
}
