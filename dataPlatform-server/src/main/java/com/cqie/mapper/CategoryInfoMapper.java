package com.cqie.mapper;

import com.cqie.entity.CategoryInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 分类信息表 Mapper 接口
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
public interface CategoryInfoMapper extends BaseMapper<CategoryInfo> {

    @Select("select * FROM category_info WHERE parent_code = #{parentCode}")
    List<CategoryInfo> selectChildrenByParentCode(Integer categoryCode);
}
