package com.cqie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqie.constant.IsBlankConstant;
import com.cqie.constant.IsPublish;
import com.cqie.dto.dataStandard.StandardAddDto;
import com.cqie.dto.dataStandard.StandardGetAllDto;
import com.cqie.dto.dataStandard.StandardTemplate;
import com.cqie.dto.dataStandard.StandardUpdateDto;
import com.cqie.entity.CodeTable;
import com.cqie.entity.CodeValue;
import com.cqie.entity.DataStandard;
import com.cqie.enums.*;
import com.cqie.mapper.CodeTableMapper;
import com.cqie.mapper.CodeValueMapper;
import com.cqie.mapper.DataStandardMapper;
import com.cqie.service.DataStandardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqie.util.R.Result;
import com.cqie.util.file.FileUtil;
import com.cqie.util.file.ReadFile;
import com.cqie.util.validator.ValidationUtils;
import com.cqie.vo.codetablevo.CodeTableValueVo;
import com.cqie.vo.codetablevo.CodeValueValueVo;
import com.cqie.vo.dataStandard.DataStandardPageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
@Transactional(rollbackFor = Exception.class)
public class DataStandardServiceImpl extends ServiceImpl<DataStandardMapper, DataStandard> implements DataStandardService {

    @Resource
    private CodeValueMapper codeValueMapper;

    @Resource
    private CodeTableMapper codeTableMapper;

    @Resource
    private DataStandardMapper dataStandardMapper;

    @Resource
    private Validator validator;


    private static final String TYPE_NUMBER = "MZB00002";

    //方法：新增的编号
    private Integer standardNumber() {
        List<DataStandard> list = lambdaQuery()
                .eq(DataStandard::getDeleteFlag, IsDeleteEnum.NOT_DELETE.ordinal())
                .orderByDesc(DataStandard::getDataStandardCode)
                .list();
        if (ObjectUtils.isEmpty(list)) {
            return 1;
        }
        DataStandard dataStandard = list.get(0);
        return Integer.parseInt(dataStandard.getDataStandardCode().substring(2));
    }

    //方法：对比数据库中的中文与英文是否重复
    private List<StandardTemplate> compareNames(List<StandardTemplate> list) {
        List<StandardTemplate> res = new ArrayList<>();
        //获取中文名
        List<String> names= dataStandardMapper.selectCnName();
        //获取英文名
        List<String> enNames = dataStandardMapper.selectEnName();
        list.forEach(data -> {
            if (!names.contains(data.getDataStandardCnName()) && !enNames.contains(data.getDataStandardEnName())) {
                res.add(data);
            }
        });
        return res;
    }

    //方法：判断不同类型其字段是否为空
    private Result<Object> typeIsEmpty(String type, StandardAddDto standardAddDto) {
        switch (type) {
            //当数据类型为Int时 数据长度、数据精度、枚举范围（导入时为“引用码表编号”）必须为空；
            case "Int":
                if (ObjectUtils.isNotEmpty(standardAddDto.getDataStandardLength())
                        && ObjectUtils.isNotEmpty(standardAddDto.getDataStandardEnumerationRange())
                        && ObjectUtils.isNotEmpty(standardAddDto.getDataStandardAccuracy())) {
                    return Result.failed(null, 0, "数据长度、数据精度、枚举范围必须为空");
                }
                break;
            //当数据类型为Float时，数据长度、枚举范围（导入时为“引用码表编号”）必须为空；
            case "Float":
                if (ObjectUtils.isNotEmpty(standardAddDto.getDataStandardLength())
                        && ObjectUtils.isNotEmpty(standardAddDto.getDataStandardEnumerationRange())
                ) {
                    return Result.failed(null, 0, "数据长度、枚举范围必须为空");
                }
                break;
            //当数据类型为Enum时，数据长度、数据精度、取值范围最小值、取值范围最大值必须为空；
            case "Enum":
                if (ObjectUtils.isNotEmpty(standardAddDto.getDataStandardLength())
                        && ObjectUtils.isNotEmpty(standardAddDto.getDataStandardValueMin())
                        && ObjectUtils.isNotEmpty(standardAddDto.getDataStandardValueMax())
                        && ObjectUtils.isNotEmpty(standardAddDto.getDataStandardAccuracy())) {
                    return Result.failed(null, 0, "数据长度、数据精度、取值范围最小值、取值范围最大值必须为空");
                }
                break;
            //当数据类型为String时 数据精度、取值范围最小值、取值范围最大值、枚举范围（导入时为“引用码表编号”）必须为空；
            case "String":
                if (ObjectUtils.isNotEmpty(standardAddDto.getDataStandardEnumerationRange())
                        && ObjectUtils.isNotEmpty(standardAddDto.getDataStandardValueMin())
                        && ObjectUtils.isNotEmpty(standardAddDto.getDataStandardValueMax())
                        && ObjectUtils.isNotEmpty(standardAddDto.getDataStandardAccuracy())) {
                    return Result.failed(null, 0, "数据精度、取值范围最小值、取值范围最大值、枚举范围必须为空；");
                }
                break;
            default:
                return Result.failed(null, 0, "未知数据类型");
        }
        //取值范围最小值与取值范围最大值对于标准的数据类型为Int时只能取整数，对于标准的数据类型为Float时可以填整数与实数。
        //正则表达式
        String intPattern = "^\\d+$";
        String floatPattern = "[+-]?\\d+(\\.\\d+)?";
        //进行筛选
        switch (type) {
            //取值范围最小值与取值范围最大值对于标准的数据类型为Int时只能取整数
            case "Int":
                if (!standardAddDto.getDataStandardValueMin().matches(intPattern)
                        && !standardAddDto.getDataStandardValueMax().matches(floatPattern)) {
                    return Result.failed(null, 0, "取值范围最小值与取值范围最大值对于标准的数据类型为Int时只能取整数");
                }
                break;
            //对于标准的数据类型为Float时可以填整数与实数
            case "Float":
                if (!standardAddDto.getDataStandardValueMin().matches(floatPattern)
                        && !standardAddDto.getDataStandardValueMax().matches(floatPattern)) {
                    return Result.failed(null, 0, "对于标准的数据类型为Float时可以填整数与实数");
                }
                break;
        }
        return Result.success(null);
    }



    //新增
    /***
     * 1。判断新增中英文是否与数据库中的数据重复
     * 2. 判断数据类型是否存在
     * 2. 判断枚举类型在数据库中是否存在
     * 3. 判断不同类型其字段是否为空
     * 4. 码表必须在发布状态才能引用
     * 5. 取值范围最小值与取值范围最大值对于标准的数据类型为Int时只能取整数，
     *    对于标准的数据类型为Float时可以填整数与实数。
     */
    @Override
    public Result<Object> addStandard(StandardAddDto standardAddDto) {
        //获取中文名
        List<String> names= dataStandardMapper.selectCnName();
        //获取英文名
        List<String> enNames = dataStandardMapper.selectEnName();
        //判断是否重复
        if (names.contains(standardAddDto.getDataStandardCnName()) || enNames.contains(standardAddDto.getDataStandardEnName())) {
            return Result.failed(null, 0, "英文名或中文名重复");
        }

        //判断数据类型是否存在
        List<String> types = codeValueMapper.selectType(TYPE_NUMBER);
        if (!types.contains(standardAddDto.getDataStandardType())) {
            return Result.failed(null, 0, "此数据类型不存在");
        }

        //判断枚举类型在数据库中是否存在
        LambdaQueryWrapper<CodeTable> codeTableLambdaQueryWrapper = new LambdaQueryWrapper<>();
        codeTableLambdaQueryWrapper.eq(CodeTable::getCodeTableNumber, standardAddDto.getDataStandardEnumerationRange());
        CodeTable codeTable = codeTableMapper.selectOne(codeTableLambdaQueryWrapper);
        if (codeTable == null) {
            return Result.failed(null, 0, "此枚举类型不存在");
        }

        //枚举类型必须在发布状态才能引用
        if (!codeTable.getCodeTableState().equals(IsPublishEnum.Published.ordinal())) {
            return Result.failed(null, 0, "只有发布的枚举类型才能使用");
        }


        //判断不同类型其字段是否为空
            //判断类型是否为空
            //判断取值范围最小值与取值范围最大值对于标准的数据类型为Int时只能取整数，对于标准的数据类型为Float时可以填整数与实数
            String dataStandardType = standardAddDto.getDataStandardType();
            Result<Object> stringResult = typeIsEmpty(dataStandardType, standardAddDto);
            if (stringResult.getCode() == 0) {
                return stringResult;
            }

        //获取码值表类型编号
        LambdaQueryWrapper<CodeValue> valueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        valueLambdaQueryWrapper.eq(CodeValue::getCodeValueName, standardAddDto.getDataStandardType());
        Integer codeValueNumber = codeValueMapper.selectOne(valueLambdaQueryWrapper).getCodeValueNumber();

        //获取来源机构
        LambdaQueryWrapper<CodeValue> organizationQueryWrapper = new LambdaQueryWrapper<>();
        organizationQueryWrapper.eq(CodeValue::getCodeValueName, standardAddDto.getDataStandardSourceOrganization());
        Integer dataStandardSourceOrganization = codeValueMapper.selectOne(organizationQueryWrapper).getCodeValueNumber();



        //存入数据
        DataStandard dataStandard = new DataStandard();
        BeanUtils.copyProperties(standardAddDto, dataStandard);
        dataStandard.setDataStandardType(codeValueNumber);
        dataStandard.setDataStandardSourceOrganization(dataStandardSourceOrganization);
        dataStandard.setCreateTime(LocalDateTime.now());
        dataStandard.setUpdateTime(LocalDateTime.now());
        dataStandard.setDataStandardCode("BZ" + String.format("%05d", standardNumber()));
        //获取是否为空
        //获取是否可以为空插入
        if (standardAddDto.getDataStandardIsBlank().equals(IsBlankConstant.NOT_CAN_EMPTY_CN)) {
            dataStandard.setDataStandardIsBlank(IsBlankConstant.NOT_CAN_EMPTY_CODE);
        }else {
            dataStandard.setDataStandardIsBlank(IsBlankConstant.CAN_EMPTY_CODE);
        }


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
                .eq(DataStandard::getDeleteFlag, IsDeleteEnum.NOT_DELETE.ordinal())
                .orderByAsc(DataStandard::getDataStandardState)
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
        //判断是否Code为空
        if (ObjectUtils.isEmpty(dataStandardCode)) {
            return Result.failed("code不能为空");
        }
        //判断是否处于未发布状态
        DataStandard dataStandard = lambdaQuery().eq(DataStandard::getDataStandardCode, dataStandardCode).one();
        if (dataStandard.getDataStandardState() == IsPublishValueEnum.Unpublished.ordinal()) {
            return Result.failed(null, 0, "只有未发布的才能进行删除");
        }
        //逻辑删除
        lambdaUpdate().eq(DataStandard::getDataStandardCode, dataStandardCode)
                .set(DataStandard::getDeleteFlag, IsDeleteEnum.DELETE.ordinal())
                .update();

        return Result.success(null);
    }

    /**
     * 批量修改
     */
    @Override
    public Result<Object> updateState(List<String> dataStandardCode, Integer dataStandardState) {
        //判断两个值是否为空
        if (ObjectUtils.isEmpty(dataStandardCode)) {
            Result.failed("标准编号不能为空");
        }
        if (ObjectUtils.isEmpty(dataStandardState)) {
            Result.failed("标准状态不能为空");
        }

        //编号在数据库中是否存在
        for (String data : dataStandardCode) {
            DataStandard standard = lambdaQuery().eq(DataStandard::getDataStandardCode, data).eq(DataStandard::getDeleteFlag, IsEmptyEnum.EMPTY.ordinal()).one();
            if (standard == null) {
                return Result.failed(dataStandardCode + "数据表中编号不存在数据库中");
            }
        }

        //跨越  未发布（1）不能到已停用（3）  发布（2）和停用（3）不能到未发布（1）
        switch (dataStandardState) {
            case IsPublish.UNPUBLISHED:
                return Result.failed("发布和停用不能改为未发布");
            case IsPublish.PUBLISHED:
                break;
            case IsPublish.DISABLED:
                List<String> res = dataStandardMapper.selectPublish(dataStandardCode);
                if (res != null) {
                    return Result.failed(res + "不能从未发布改为已停用");
                }
        }

        //批量修改
        int i = dataStandardMapper.updateBatch(dataStandardCode, dataStandardState);
        if (i == IsEmptyEnum.EMPTY.ordinal()) {
            return Result.failed("未修改成功");
        }
        return Result.success(null);
    }

    //读取数据
    @Override
    public Result<Object> addTemplate(MultipartFile file) {
        List<StandardTemplate> standardTemplates = new ArrayList<>();
        try {

            //先下载文件
            String path = FileUtil.uploadFile(file, FileUtil.DATA_STANDARD_TEMPLATE);

            //读取文件
            List<StandardTemplate> standardTemplate = ReadFile.readWrite(path, StandardTemplate.class);

            //过滤校验失败的和去除相同和长度等
            standardTemplates = ValidationUtils.validateStandard(standardTemplate, validator);
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        //判断数据类型不存在的
        List<StandardTemplate> standardTemplate = new ArrayList<>();
        LambdaQueryWrapper<CodeValue> typeWrapper = new LambdaQueryWrapper<>();
        typeWrapper.eq(CodeValue::getCodeTableNumber, TYPE_NUMBER);
        List<CodeValue> codeValues = codeValueMapper.selectList(typeWrapper);
        List<String> typeStandard = codeValues.stream().map(CodeValue::getCodeValueValue).collect(Collectors.toList());
        standardTemplates.forEach(data -> {
            if (typeStandard.contains(data.getDataStandardType())) {
                standardTemplate.add(data);
            }
        });

        //与数据库中的数据进行比对
        List<StandardTemplate> standardTemplates1 = compareNames(standardTemplate);

        //导入时的“引用码表编号”必须是系统中已存在且已发布的码表对应的码表编码；
        List<StandardTemplate> standardTemplates2 = new ArrayList<>();
        standardTemplates1.forEach(data -> {
            LambdaQueryWrapper<CodeTable> codeTableLambdaQueryWrapper = new LambdaQueryWrapper<>();
            codeTableLambdaQueryWrapper.eq(CodeTable::getCodeTableName, data.getDataStandardEnumerationRange()).eq(CodeTable::getDeleteFlag, IsDeleteEnum.NOT_DELETE.ordinal());
            CodeTable codeTable = codeTableMapper.selectOne(codeTableLambdaQueryWrapper);
            if (codeTable == null) {
                standardTemplates2.add(data);
            }
        });

        //定义插入对象
        List<DataStandard> dataStandards = new ArrayList<>();

        //定义code
        Integer code = standardNumber();

        for (StandardTemplate standard : standardTemplates2) {

            //机构来源编号
            LambdaQueryWrapper<CodeValue> organizationQueryWrapper = new LambdaQueryWrapper<>();
            organizationQueryWrapper.eq(CodeValue::getCodeValueName, standard.getDataStandardSourceOrganization());
            Integer dataStandardSourceOrganization = codeValueMapper.selectOne(organizationQueryWrapper).getCodeValueNumber();

            //获取数据类型
            LambdaQueryWrapper<CodeValue> typeQueryWrapper = new LambdaQueryWrapper<>();
            typeQueryWrapper.eq(CodeValue::getCodeValueName, standard.getDataStandardType());
            Integer typeTableNumber = codeValueMapper.selectOne(typeQueryWrapper).getCodeValueNumber();


            //插入数据
            DataStandard dataStandard = new DataStandard();
            dataStandard.setDataStandardCnName(standard.getDataStandardCnName());
            dataStandard.setDataStandardEnName(standard.getDataStandardEnName());
            dataStandard.setDataStandardExplain(standard.getDataStandardExplain());
            dataStandard.setDataStandardState(IsPublishValueEnum.Unpublished.ordinal());

            //获取是否可以为空插入
            if (standard.getDataStandardIsBlank().equals(IsBlankConstant.NOT_CAN_EMPTY_CN)) {
                dataStandard.setDataStandardIsBlank(IsBlankConstant.NOT_CAN_EMPTY_CODE);
            } else {
                dataStandard.setDataStandardIsBlank(IsBlankConstant.CAN_EMPTY_CODE);
            }

            dataStandard.setDataStandardSourceOrganization(dataStandardSourceOrganization);
            dataStandard.setDataStandardDefaultValue(standard.getDataStandardDefaultValue());
            dataStandard.setDataStandardType(typeTableNumber);
            dataStandard.setCreateTime(LocalDateTime.now());
            dataStandard.setUpdateTime(LocalDateTime.now());
            dataStandard.setDataStandardCode("BZ" + String.format("%05d", ++code));

            //插入对应的数据
            switch (typeTableNumber) {
                //当数据类型为String时，可以编辑数据长度，
                case 4:
                    dataStandard.setDataStandardLength(standard.getDataStandardLength());
                    break;
                //当数据类型为Int时，可以编辑取值范围最小值及取值范围最大值
                case 5:
                    dataStandard.setDataStandardValueMin(standard.getDataStandardValueMin());
                    dataStandard.setDataStandardValueMax(standard.getDataStandardValueMax());
                    break;
                //当数据类型为Float时，可以编辑数据精度、取值范围最小值、取值范围最大值，
                case 6:
                    dataStandard.setDataStandardAccuracy(standard.getDataStandardAccuracy());
                    dataStandard.setDataStandardValueMin(standard.getDataStandardValueMin());
                    dataStandard.setDataStandardValueMax(standard.getDataStandardValueMax());
                    break;
                //当数据类型为Enum时，可以编辑枚举范围（导入时为“引用码表编号”）
                case 7:
                    dataStandard.setDataStandardEnumerationRange(standard.getDataStandardEnumerationRange());
                    break;
            }
            dataStandards.add(dataStandard);
        }
        //批量插入
        if (ObjectUtils.isNotEmpty(dataStandards)) {
            dataStandardMapper.insertForeach(dataStandards);
        }
        return Result.success(null);
    }

    //修改数据标准目录
    @Override
    public Result<Object> updateStandard(StandardUpdateDto standardUpdateDto) {
        //判断中文名和英文名是否重复
        LambdaQueryWrapper<DataStandard> standardLambdaQueryWrapper = new LambdaQueryWrapper<>();
        standardLambdaQueryWrapper.eq(DataStandard::getDataStandardCnName, standardUpdateDto.getDataStandardCnName())
                .eq(DataStandard::getDataStandardEnName, standardUpdateDto.getDataStandardEnName())
                .ne(DataStandard::getId, standardUpdateDto.getId());
        List<DataStandard> dataStandards = dataStandardMapper.selectList(standardLambdaQueryWrapper);
        if (dataStandards != null) {
            return Result.failed("中文名或英文名不能重复");
        }

        //判断数据类型是否存在
        List<String> types = codeValueMapper.selectType(TYPE_NUMBER);
        if (!types.contains(standardUpdateDto.getDataStandardType())) {
            return Result.failed(null, 0, "此数据类型不存在");
        }

        //判断枚举类型在数据库中是否存在
        LambdaQueryWrapper<CodeTable> codeTableLambdaQueryWrapper = new LambdaQueryWrapper<>();
        codeTableLambdaQueryWrapper.eq(CodeTable::getCodeTableNumber, standardUpdateDto.getDataStandardEnumerationRange());
        CodeTable codeTable = codeTableMapper.selectOne(codeTableLambdaQueryWrapper);
        if (codeTable == null) {
            return Result.failed(null, 0, "此枚举类型不存在");
        }

        //枚举类型必须在发布状态才能引用
        if (!codeTable.getCodeTableState().equals(IsPublishEnum.Published.ordinal())) {
            return Result.failed(null, 0, "只有发布的枚举类型才能使用");
        }


        //判断不同类型其字段是否为空
        //判断类型是否为空
        //判断取值范围最小值与取值范围最大值对于标准的数据类型为Int时只能取整数，对于标准的数据类型为Float时可以填整数与实数
        String dataStandardType = standardUpdateDto.getDataStandardType();
        Result<Object> stringResult = typeIsEmpty(dataStandardType, standardUpdateDto);
        if (stringResult.getCode() == 0) {
            return stringResult;
        }

        //获取码值表类型编号
        LambdaQueryWrapper<CodeValue> valueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        valueLambdaQueryWrapper.eq(CodeValue::getCodeValueName, standardUpdateDto.getDataStandardType());
        Integer codeValueNumber = codeValueMapper.selectOne(valueLambdaQueryWrapper).getCodeValueNumber();

        //获取来源机构
        LambdaQueryWrapper<CodeValue> organizationQueryWrapper = new LambdaQueryWrapper<>();
        organizationQueryWrapper.eq(CodeValue::getCodeValueName, standardUpdateDto.getDataStandardSourceOrganization());
        Integer dataStandardSourceOrganization = codeValueMapper.selectOne(organizationQueryWrapper).getCodeValueNumber();



        //存入数据
        DataStandard dataStandard = new DataStandard();
        BeanUtils.copyProperties(standardUpdateDto, dataStandard);
        dataStandard.setDataStandardType(codeValueNumber);
        dataStandard.setDataStandardSourceOrganization(dataStandardSourceOrganization);
        dataStandard.setCreateTime(LocalDateTime.now());
        dataStandard.setUpdateTime(LocalDateTime.now());
        dataStandard.setDataStandardCode("BZ" + String.format("%05d", standardNumber()));
        //获取是否为空
        //获取是否可以为空插入
        if (standardUpdateDto.getDataStandardIsBlank().equals(IsBlankConstant.NOT_CAN_EMPTY_CN)) {
            dataStandard.setDataStandardIsBlank(IsBlankConstant.NOT_CAN_EMPTY_CODE);
        }else {
            dataStandard.setDataStandardIsBlank(IsBlankConstant.CAN_EMPTY_CODE);
        }


        dataStandardMapper.update(dataStandard, null);

        return Result.success(null);
    }
}