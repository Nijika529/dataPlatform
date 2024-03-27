package com.cqie.service;

import com.cqie.dto.dataStandard.StandardAddDto;
import com.cqie.dto.dataStandard.StandardGetAllDto;
import com.cqie.entity.DataStandard;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqie.util.R.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 数据标准表 服务类
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
public interface DataStandardService extends IService<DataStandard> {

    Result<Object> addStandard(StandardAddDto standardAddDto);

    Result<Object> getAll(StandardGetAllDto standardGetAllDto);

    Result<Object> delete(String dataStandardCode);

    Result<Object> updateState(List<String> dataStandardCode, Integer dataStandardState);

    Result<Object> addTemplate(MultipartFile file);
}
