package com.cqie.service;

import com.cqie.dto.category.CategoryDto;
import com.cqie.entity.CategoryInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqie.util.R.Result;
import com.cqie.vo.category.CategoryVO;

import java.util.List;

/**
 * <p>
 * 分类信息表 服务类
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
public interface CategoryInfoService extends IService<CategoryInfo> {

    /**
     * 新增分类
     */
    Result<Object> add(CategoryDto categoryDto);

    /**
     * 修改分类
     */
    Result<Object> updateCategory(CategoryDto categoryDto);


    /**
     * 获取ID获取分类目录接口
     */
    Result<List<CategoryVO>> getCategoryById(Integer categoryCode);

    /**
     *删除分类接口
     */
    Result<Object> delete(Integer categoryCode, Integer categoryType);
}
