package com.cqie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqie.dto.dataStandard.StandardAddDto;
import com.cqie.dto.dataStandard.StandardGetAllDto;
import com.cqie.dto.dataStandard.StandardTemplate;
import com.cqie.entity.CodeValue;
import com.cqie.entity.DataStandard;
import com.cqie.enums.IsDelete;
import com.cqie.mapper.CodeTableMapper;
import com.cqie.mapper.CodeValueMapper;
import com.cqie.mapper.DataStandardMapper;
import com.cqie.service.DataStandardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqie.util.R.Result;
import com.cqie.util.file.FileUtil;
import com.cqie.util.file.ReadFile;
import com.cqie.vo.codetablevo.CodeTableValueVo;
import com.cqie.vo.codetablevo.CodeValueValueVo;
import com.cqie.vo.dataStandard.DataStandardPageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 数据标准表 服务实现类
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@Slf4j
@Service
public class DataStandardServiceImpl extends ServiceImpl<DataStandardMapper, DataStandard> implements DataStandardService {

    @Resource
    private CodeValueMapper codeValueMapper;

    @Resource
    private CodeTableMapper codeTableMapper;

    @Resource
    private DataStandardMapper dataStandardMapper;

    private String standardNumber() {
        Long count = lambdaQuery().count();
        return "BZ" + String.format("%05d", ++count);
    }
    //新增
    @Override
    public Result<Object> addStandard(StandardAddDto standardAddDto) {
        //存入数据
        DataStandard dataStandard = new DataStandard();
        BeanUtils.copyProperties(standardAddDto, dataStandard);
        dataStandard.setCreateTime(LocalDateTime.now());
        dataStandard.setUpdateTime(LocalDateTime.now());
        dataStandard.setDataStandardCode(standardNumber());
        dataStandardMapper.insert(dataStandard);
        return Result.success(null);
    }
    //分页查询
    @Override
    public Result<Object> getAll(StandardGetAllDto standardGetAllDto) {
        Page<DataStandardPageVO> page = new Page<>(standardGetAllDto.getPageNumber(), standardGetAllDto.getPageSize());
        //查询条件
        QueryWrapper<DataStandard> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(standardGetAllDto.getDataStandardSourceOrganization() != null, DataStandard::getDataStandardSourceOrganization, standardGetAllDto.getDataStandardSourceOrganization())
                .eq(standardGetAllDto.getDataStandardState() != null, DataStandard::getDataStandardState, standardGetAllDto.getDataStandardState())
                .like(StringUtils.isNotEmpty(standardGetAllDto.getDataStandardCode()), DataStandard::getDataStandardCode, standardGetAllDto.getDataStandardCode())
                .like(StringUtils.isNotEmpty(standardGetAllDto.getDataStandardCnName()), DataStandard::getDataStandardCnName, standardGetAllDto.getDataStandardCnName())
                .like(StringUtils.isNotEmpty(standardGetAllDto.getDataStandardEnName()), DataStandard::getDataStandardEnName, standardGetAllDto.getDataStandardEnName())
                .eq(DataStandard::getDeleteFlag, IsDelete.NOT_DELETE.ordinal())
                .orderByDesc(DataStandard::getUpdateTime);

        dataStandardMapper.page(page, queryWrapper);
        System.out.println(page);
        List<DataStandardPageVO> records = page.getRecords();
        records.forEach(record -> {
            //查询来源机构
            CodeValue standardSourceOrganization = codeValueMapper.selectById(record.getDataStandardSourceOrganization());
            record.setDataStandardSourceOrganization(standardSourceOrganization.getCodeValueValue());
            //查询数据类型
            CodeValue standardType = codeValueMapper.selectById(record.getDataStandardType());
            record.setDataStandardType(standardType.getCodeValueValue());
            //查询标准状态
            CodeValue codeValue = codeValueMapper.selectById(record.getDataStandardState());
            record.setDataStandardState(Integer.valueOf(codeValue.getCodeValueValue()));
            //查询码表编号
            DataStandard dataStandard = dataStandardMapper.selectById(record.getId());
            //码表
            if (StringUtils.isNotEmpty(dataStandard.getDataStandardEnumerationRange())) {
                CodeTableValueVo codeTableValueVo = codeTableMapper.selectDataStandard(dataStandard.getDataStandardEnumerationRange());
                QueryWrapper<CodeValue> codeValueQueryWrapper = new QueryWrapper<>();
                codeValueQueryWrapper.lambda()
                        .eq(CodeValue::getCodeTableNumber, dataStandard.getDataStandardEnumerationRange());
                List<CodeValueValueVo> codeValueValueVos = codeValueMapper.selectValueList(codeValueQueryWrapper);
                codeTableValueVo.setCodeValueList(codeValueValueVos);
                record.setCodeTable(codeTableValueVo);
            }
        });

        return Result.success(page);
    }
    //逻辑删除
    @Override
    public Result<Object> delete(String dataStandardCode) {
        lambdaUpdate().eq(DataStandard::getDataStandardCode, dataStandardCode)
                .set(DataStandard::getDeleteFlag, IsDelete.DELETE.ordinal())
                .update();

        return Result.success(null);
    }
    //批量修改
    @Override
    public Result<Object> updateState(List<String> dataStandardCode, Integer dataStandardState) {
        dataStandardCode.forEach(code -> lambdaUpdate().eq(DataStandard::getDataStandardCode, dataStandardCode)
                .set(DataStandard::getDataStandardState, dataStandardState)
                .update());
        return Result.success(null);
    }
    //读取数据
    @Override
    public Result<Object> addTemplate(MultipartFile file) {
        try {
            String path = FileUtil.uploadFile(file, FileUtil.DATA_STANDARD_TEMPLATE);
            List<StandardTemplate> standardTemplate = ReadFile.readWrite(path, StandardTemplate.class);
        }catch (Exception e) {
            log.info(e.getMessage());
        }
        
        return Result.success(null);
    }
}