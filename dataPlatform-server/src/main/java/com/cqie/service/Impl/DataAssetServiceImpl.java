package com.cqie.service.Impl;

import com.cqie.dto.dataAsset.DataAssetDto;
import com.cqie.entity.DataAsset;
import com.cqie.mapper.DataAssetMapper;
import com.cqie.service.DataAssetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据资产表 服务实现类
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@Service
public class DataAssetServiceImpl extends ServiceImpl<DataAssetMapper, DataAsset> implements DataAssetService {


    /**
     * 新增资产表
     */
    @Override
    public Object addStandard(DataAssetDto dataAssetDto) {
        //中文名称和英文名称不能重复

        //中文名称和英文名称

        //中英文字段不能重复

        //中英文字段与数据库不能重复

        //存入数据库

        return null;
    }
}
