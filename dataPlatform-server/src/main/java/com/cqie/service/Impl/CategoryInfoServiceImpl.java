package com.cqie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.cqie.constant.IsAssetTypeConstant;
import com.cqie.dto.category.CategoryDto;
import com.cqie.entity.CategoryInfo;
import com.cqie.entity.DataAssetRelationCategory;
import com.cqie.enums.IsDeleteEnum;
import com.cqie.mapper.CategoryInfoMapper;
import com.cqie.mapper.DataAssetRelationCategoryMapper;
import com.cqie.service.CategoryInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqie.util.R.Result;
import com.cqie.vo.category.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 分类信息表 服务实现类
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@Service
public class CategoryInfoServiceImpl extends ServiceImpl<CategoryInfoMapper, CategoryInfo> implements CategoryInfoService {


    @Resource
    private CategoryInfoMapper categoryInfoMapper;

    @Resource
    private DataAssetRelationCategoryMapper dataAssetRelationCategoryMapper;

    private static final String TYPE_SYMBOL= "/";


    /**
     * 新增分类
     */
    @Override
    @Transactional
    public Result<Object> add(CategoryDto categoryDto) {
        //判断分类是否重复
        if (isNameUnique(categoryDto)) {
            return Result.failed(null, 0, "分类有重复");
        }
        //已有资产表的资产目录下不可以再新建下级目录
        if (Objects.equals(categoryDto.getCategoryType(), IsAssetTypeConstant.ASSET_TYPE_CODE)) {
            LambdaQueryWrapper<DataAssetRelationCategory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DataAssetRelationCategory::getCategoryCode, categoryDto.getParentCode());
            List<DataAssetRelationCategory> dataAssetRelationCategories = dataAssetRelationCategoryMapper.selectList(lambdaQueryWrapper);
            if (ObjectUtils.isNotEmpty(dataAssetRelationCategories)) {
                return Result.failed(null, 0, "已有资产表的资产目录下不可以再新建下级目录");
            }
        }

        CategoryInfo categoryInfo = new CategoryInfo();
        BeanUtils.copyProperties(categoryDto, categoryInfo);
        categoryInfo.setCreateTime(LocalDateTime.now());
        categoryInfo.setUpdateTime(LocalDateTime.now());
        categoryInfoMapper.insert(categoryInfo);

        return Result.success(null);
    }

    /**
     * 根据Code查询全部数据
     */
    public Result<List<CategoryVO>> getCategoryById(Integer categoryCode) {
        //获取子级
        List<CategoryVO> allSubCategories = getAllSubCategories(categoryCode);
        List<CategoryVO> categoryTree = getCategoryTree(allSubCategories);
        return Result.success(categoryTree);
    }


    /**
     * 修改分类
     */
    @Override
    public Result<Object> updateCategory(CategoryDto categoryDto) {

        //子级判断是否重复
        List<CategoryVO> allSubCategories = getAllSubCategories(categoryDto.getCategoryCode());
        //判断分类是否重复
        boolean isUnique = allSubCategories.stream().anyMatch(data -> Objects.equals(data.getCategoryName(), categoryDto.getCategoryName()));
        if (isNameUnique(categoryDto) || isUnique) {
            return Result.failed(null, 0, "分类有重复");
        }
        //修改
        CategoryInfo categoryInfo = new CategoryInfo();
        BeanUtils.copyProperties(categoryDto, categoryInfo);
        categoryInfo.setUpdateTime(LocalDateTime.now());
        categoryInfoMapper.update(categoryInfo, null);
        return Result.success(null);
    }

    /**
     * 删除分类目录
     */
     @Override
    public Result<Object> delete(Integer categoryCode, Integer categoryType) {
         //获取子级
         List<CategoryVO> allSubCategories = getAllSubCategories(categoryCode);

         //获取子集
         switch (categoryType) {
             case IsAssetTypeConstant.API_TYPE_CODE :
             case IsAssetTypeConstant.SCRIPT_TYPE_CODE: {
                 if (allSubCategories.stream()
                         .anyMatch(data -> dataAssetRelationCategoryMapper.selectByIdAsset(data.getCategoryCode()) > 0)) {
                     return Result.failed(null, 0, "子类有数据不能删除");
                 }
             }
             case IsAssetTypeConstant.ASSET_TYPE_CODE:
                 break;
         }

         dataAssetRelationCategoryMapper.deleteById(categoryCode);

         return Result.success(null);
    }

    /**
     * 方法
     * 判断是否重名
     */
    private boolean isNameUnique (CategoryDto categoryDto) {
        //对于每个分类，同一上级分类的多个下级分类不允许重名
        List<CategoryInfo> categoryInfoList = lambdaQuery().eq(CategoryInfo::getParentCode, categoryDto.getParentCode())
                .eq(CategoryInfo::getDeleteFlag, IsDeleteEnum.NOT_DELETE.ordinal()).list();

        if (categoryInfoList.stream().anyMatch(data -> Objects.equals(data.getCategoryName(), categoryDto.getCategoryName()))) {
            return true;
        }

        // 单条路径下的各级分类不允许有重名
        Integer parentCode = categoryDto.getParentCode();
        while (parentCode != 0) {
            CategoryInfo categoryInfo = lambdaQuery().eq(CategoryInfo::getCategoryCode, parentCode)
                    .eq(CategoryInfo::getDeleteFlag, IsDeleteEnum.NOT_DELETE.ordinal()).one();
            if (Objects.equals(categoryDto.getCategoryName(), categoryInfo.getCategoryName())) {
                return true;
            }
            parentCode = categoryInfo.getParentCode();
        }

        return false;
    }

    /**
     * 递归获取子集list型
     */
    public List<CategoryVO> getAllSubCategories(Integer categoryCode) {
        List<CategoryVO> subCategories = new ArrayList<>();
        // 首先获取当前分类
        CategoryInfo currentCategory = categoryInfoMapper.selectById(categoryCode);
        if (currentCategory != null) {
            // 将当前分类转换为VO对象，并添加到子分类列表中
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(currentCategory, categoryVO);
            subCategories.add(categoryVO);

            // 递归获取所有子分类
            List<CategoryInfo> children = categoryInfoMapper.selectChildrenByParentCode(categoryCode);
            if (children != null) {
                for (CategoryInfo child : children) {
                    // 对每个子分类递归调用此方法
                    List<CategoryVO> childSubCategories = getAllSubCategories(child.getCategoryCode());
                    // 将子分类的子分类添加到结果列表中
                    subCategories.addAll(childSubCategories);
                }
            }
        }
        return subCategories;
    }

    /**
     * 封装为树状图
     */
    public List<CategoryVO> getCategoryTree(List<CategoryVO> allCategories) {
        List<CategoryVO> tree = new ArrayList<>();
        Map<Integer, CategoryVO> categoryMap = new HashMap<>();

        // 创建分类VO映射
        for (CategoryVO info : allCategories) {
            categoryMap.put(info.getCategoryCode(), info);
        }

        // 构建树状结构
        for (CategoryVO info : allCategories) {
            CategoryVO vo = categoryMap.get(info.getCategoryCode());
            Integer parentCode = info.getParentCode();
            if (parentCode != null && categoryMap.containsKey(parentCode)) {
                // 如果存在父分类，则将当前分类添加到父分类的children中
                CategoryVO parentVo = categoryMap.get(parentCode);
                if (parentVo.getChildren() == null) {
                    parentVo.setChildren(new ArrayList<>());
                }
                parentVo.getChildren().add(vo);
                vo.setPath(parentVo.getPath() + vo.getCategoryName() + TYPE_SYMBOL);
            } else {
                // 如果没有父分类，则将当前分类添加到树的根节点列表中
                tree.add(vo);
                vo.setPath(vo.getCategoryName() + TYPE_SYMBOL);
            }
        }

        return tree;
    }


}
