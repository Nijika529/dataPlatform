package com.cqie.mapper;

import com.cqie.entity.DataAssetRelationCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 数据资产分类表 Mapper 接口
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
public interface DataAssetRelationCategoryMapper extends BaseMapper<DataAssetRelationCategory> {

    @Select("select data_asset_code from data_asset_relation_category where category_code = #{categoryCode}")
    int selectByIdAsset(Integer categoryCode);
}
