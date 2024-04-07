package com.cqie.service;

import com.cqie.dto.dataAsset.DataAssetDto;
import com.cqie.entity.DataAsset;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 数据资产表 服务类
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
public interface DataAssetService extends IService<DataAsset> {

    /**
     * 新增资产表
     */
    Object addStandard(DataAssetDto dataAssetDto);
}
