package com.cqie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqie.dto.codetable.*;
import com.cqie.entity.CodeTable;
import com.cqie.entity.CodeValue;
import com.cqie.entity.ValidationError;
import com.cqie.mapper.CodeTableMapper;
import com.cqie.mapper.CodeValueMapper;
import com.cqie.service.CodeTableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqie.util.R.Result;
import com.cqie.util.file.FileUtil;
import com.cqie.util.file.ReadFile;
import com.cqie.vo.codetablevo.CodeTablePageVo;
import com.cqie.vo.codetablevo.CodeTableValueVo;
import com.cqie.vo.codetablevo.CodeValueValueVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 码表管理 服务实现类
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@Service
@Slf4j
@Transactional
public class CodeTableServiceImpl<T,K> extends ServiceImpl<CodeTableMapper, CodeTable> implements CodeTableService {

    @Resource
    private CodeTableMapper codeTableMapper;

    @Resource
    private CodeValueMapper codeValueMapper;

    @Resource
    private Validator validator;

    public static  final Integer NOT_DELETE = 0;


    public List<K> findDuplicateValues(List<T> list, Function<T, K> key) {
        // 使用流API来分组元素，键是属性值，值是元素列表
        Map<K,List<T>> collect = list.stream()
                .collect(Collectors.groupingBy((Function<T, K>) key));
        // 找出那些有多个元素的分组，即重复的属性值
       return collect.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    //校验并把错误的输出
    private List<ValidationError> validateList(List<CodeTableTemplate> list) {
        //错误类
        List<com.cqie.entity.ValidationError> errors = new ArrayList<>();
        // 这里添加您的校验逻辑
        for (CodeTableTemplate item : list) {
            // 使用Bean Validation API进行校验
            Set<ConstraintViolation<CodeTableTemplate>> violations = validator.validate(item);
            if (!violations.isEmpty()) {
               for (ConstraintViolation<CodeTableTemplate> v: violations) {
                   com.cqie.entity.ValidationError validationError = new com.cqie.entity.ValidationError();
                   validationError.setMessage(v.getMessage());
                   validationError.setCodeTableTemplate(item);
                   errors.add(validationError);
               }
            }
        }
        return errors;
    }

    //去除错误的类
    private List<CodeTableTemplate> filterValidTemplates(List<CodeTableTemplate> templates, List<ValidationError> errors) {
        if (errors == null || errors.isEmpty()) {
            // 如果没有错误，所有模板都是有效的
            return templates;
        }

        List<CodeTableTemplate> validTemplates = new ArrayList<>();
        for (CodeTableTemplate template : templates) {
            boolean hasValidationError = false;
            for (ValidationError error : errors) {
                if (error.getCodeTableTemplate() != null && error.getCodeTableTemplate().getCodeTableName().equals(template.getCodeTableName())) {
                    hasValidationError = true;
                    break; // 找到相关错误，跳出内层循环
                }
            }
            if (!hasValidationError) {
                // 只有当模板没有相关错误时，才将其添加到有效模板列表中
                validTemplates.add(template);
            }
        }
        return validTemplates;
    }


    //过滤掉相同的字段
    private List<CodeTableTemplate> filterDuplicateCodeTableNames(List<CodeTableTemplate> list) {
        //利用Map来去除相同的字段
        Map<String, CodeTableTemplate> filter = new HashMap<>();
        List<CodeTableTemplate> filterlist = new ArrayList<>();
        for (CodeTableTemplate s:list) {
            //判断与数据库数据是否重复
            LambdaQueryWrapper<CodeTable> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(CodeTable::getCodeTableName, s.getCodeTableName())
                    .eq(CodeTable::getDeleteFlag, NOT_DELETE);
            CodeTable codeTable = codeTableMapper.selectOne(lambdaQueryWrapper);
            //只有唯一的key  检查是否是唯一的key   不是则跳过
            if (!filter.containsKey(s.getCodeTableName()) && codeTable == null) {
                filter.put(s.getCodeTableName(), s);
                filterlist.add(s);
            }
        }
        //过滤对应码表的码值重复项
        filterlist.forEach(f -> {
            Map<String, CodeTableTemplateValue> valueHashMap = new HashMap<>();
            Map<String, CodeTableTemplateValue> valueValueHashMap = new HashMap<>();
            List<CodeTableTemplateValue> valuesList = new ArrayList<>();
            //过滤tableValue
            for (CodeTableTemplateValue  ff:f.getCodeValues()) {
                if (!valueHashMap.containsKey(ff.getCodeValueName()) && !valueValueHashMap.containsKey(ff.getCodeValueValue())) {
                    valueValueHashMap.put(ff.getCodeValueValue(), null);
                    valueHashMap.put(ff.getCodeValueName(), null);
                    valuesList.add(ff);
                }
            }
            //更改后插入进码表码值中
            f.setCodeValues(valuesList);
        });
        return filterlist;
    }

    public String getCodeTableCount() {
        Long number = codeTableMapper.selectCount(null);
        return "MZB" + String.format("%05d" , ++number);
    }


    @Override
    public Result<Object> saveT(CodeTableDto codeTableDto) {
        //新增码表
        // 生成MZB五位数
        //码表的中文名称与系统列表中已存在的码表中的名称不可重复
        LambdaQueryWrapper<CodeTable> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CodeTable::getCodeTableName, codeTableDto.getCodeTableName())
                .eq(CodeTable::getDeleteFlag, 0);
        Long l = codeTableMapper.selectCount(lambdaQueryWrapper);
        //判断当前码值名称是否重复
        List<CodeValueDto> codeValueList = codeTableDto.getCodeValueList();
            // 使用流API来分组元素，键是字段值，值是元素列表
            Map<String, List<CodeValueDto>> collect = codeValueList.stream()
                    .collect(Collectors.groupingBy(CodeValueDto::getCodeValueName));
            // 找出那些有多个元素的分组，即重复的字段值
            List<String> repeat1 = collect.entrySet().stream()
                    .filter(entry -> entry.getValue().size() > 1)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        //判断当前码值取值是否重复
            // 使用流API来分组元素，键是字段值，值是元素列表
            Map<String, List<CodeValueDto>> collect1 = codeValueList.stream()
                    .collect(Collectors.groupingBy(CodeValueDto::getCodeValueValue));
            List<String> repeat2 = collect1.entrySet().stream()
                    .filter(entry -> entry.getValue().size() > 1)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            if (!repeat2.isEmpty() || !repeat1.isEmpty() || l != 0) {
                Map<String, Object> resultHash = new HashMap<>();
                if (l > 0) {
                    resultHash.put("codeTableName", codeTableDto.getCodeTableName());
                }
                if (!repeat1.isEmpty()) {
                    resultHash.put("getCodeValueValue", repeat1);
                }
                if (!repeat2.isEmpty()) {
                    resultHash.put("getCodeValueName", repeat2);
                }
                return Result.failed(resultHash, 0, "有重复字段");
            }
        //当前码表中编码取值、编码中文名称2个字段中也不能存在重复的数据
        String codeTableCount = getCodeTableCount();
        // 加入CodeTable对象中
        CodeTable codeTable = CodeTable.builder()
                .codeTableNumber(codeTableCount)
                .codeTableName(codeTableDto.getCodeTableName())
                .codeTableDesc(codeTableDto.getCodeTableDesc())
                .codeTableState(codeTableDto.getCodeTableState())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        //判断是否为发布状态
        if (codeTableDto.getCodeTableState() != null) {
            codeTable.setCodeTableState(codeTableDto.getCodeTableState());
        }
        // 加入数据库
        codeTableMapper.insert(codeTable);
        // 拿出多组数据
        List<CodeValueDto> codeValueDto = codeTableDto.getCodeValueList();
        // 分组插入
        for (CodeValueDto dto:codeValueDto) {
            CodeValue codeValue = CodeValue.builder()
                    .codeValueName(dto.getCodeValueName())
                    .codeValueDesc(dto.getCodeValueDesc())
                    .codeTableNumber(codeTableCount)
                    .codeValueValue(dto.getCodeValueValue())
                    .build();
            codeValueMapper.insert(codeValue);
        }
        //判断是否成功
        return Result.success(null);
    }

    @Override
    public Result<String> deleteId(Integer id) {
        //逻辑删除
        CodeTable codeTable = codeTableMapper.selectById(id);
        if (codeTable.getCodeTableState() != 0) {
            return Result.failed(null, 0, "已发布或已停用状态的数据项不能删除");
        }
        LambdaUpdateWrapper<CodeTable> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(CodeTable::getDeleteFlag, 1)
                .eq(CodeTable::getId, id)
                .set(CodeTable::getUpdateTime, LocalDateTime.now());
        codeTableMapper.update(null, lambdaUpdateWrapper);
        return Result.success(null);
    }
    //分页查询
    @Override
    public Result<Object> pageT(CodeTablePageDto pageDto) {
        //获取分页条件
        Page<CodeTablePageVo> codeTablePage = new Page<>(pageDto.getPageNumber(), pageDto.getPageSize());
        //条件查询
        QueryWrapper<CodeTable> pageVoLambdaQueryWrapper = new QueryWrapper<>();
        pageVoLambdaQueryWrapper.lambda()
                .like(StringUtils.isNotEmpty(pageDto.getCodeTableName()), CodeTable::getCodeTableName, pageDto.getCodeTableName())
                .eq(pageDto.getCodeTableState() != null, CodeTable::getCodeTableState, pageDto.getCodeTableState())
                .eq(CodeTable::getDeleteFlag, 0)
                .orderByAsc(CodeTable::getCodeTableState)
                .orderByDesc(CodeTable::getUpdateTime);
        //查询
        codeTableMapper.selectCodeTablePage(codeTablePage, pageVoLambdaQueryWrapper);
        return Result.success(codeTablePage);
    }
    //批量修改
    @Override
    public Result<Object> state(CodeTableStateDto codeTableStateDto) {
        //foreach循环使用创建对象
        codeTableStateDto.getIds().forEach(id -> {
            CodeTable codeTable = CodeTable.builder()
                    .id(id)
                    .codeTableState(codeTableStateDto.getState())
                    .updateTime(LocalDateTime.now())
                    .build();
            codeTableMapper.updateById(codeTable);
        });
        return Result.success(null);
    }
    //根据id获取码表所有
    @Override
    public Result<CodeTableValueVo> getByIdId(Integer id) {

        if (id == null) {
            return Result.failed(null, 0, "id不能为空");
        }

        //根据id获取码表所有
        CodeTableValueVo res = codeTableMapper.getByIdTable(id);
        List<CodeValueValueVo> resValue = codeTableMapper.getById(id);
        res.setCodeValueList(resValue);
        return Result.success(res);
    }
    //修改码表
    @Override
    public Result<Object> updateTable(CodeTableUpdateDto codeTableUpdateDto) {
        System.out.println(codeTableUpdateDto);
        //码表修改
        //判断是否修改字段相同
        CodeTable one = lambdaQuery()
                .eq(CodeTable::getCodeTableName, codeTableUpdateDto.getCodeTableName())
                .eq(CodeTable::getDeleteFlag, 0)
                .ne(CodeTable::getId, codeTableUpdateDto.getId()).one();
        //判断修改名称是否相同
        List<CodeValueUpdateDto> codeValue1 = codeTableUpdateDto.getCodeValueList();
        Map<String, List<CodeValueUpdateDto>> collect = codeValue1.stream()
                .collect(Collectors.groupingBy(CodeValueUpdateDto::getCodeValueName));
        List<String> repeat1 = collect.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        //判断取值是否相同
        Map<String, List<CodeValueUpdateDto>> collect2 = codeValue1.stream()
                .collect(Collectors.groupingBy(CodeValueUpdateDto::getCodeValueValue));
        List<String> repeat2 = collect2.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (!repeat1.isEmpty() || !repeat2.isEmpty() || one != null){
            Map<String, Object> resultHash = new HashMap<>();
            if (one != null) {
                return Result.failed(null, 0, "码表名称不能重复");
            }
            if (!repeat1.isEmpty()) {
                resultHash.put("getCodeValueValue", repeat1);
            }
            if (!repeat2.isEmpty()) {
                resultHash.put("getCodeValueName", repeat2);
            }
            return Result.failed(resultHash, 0, "有重复字段");
        }
        //码表修改条件;
        LambdaUpdateWrapper<CodeTable> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(CodeTable::getId, codeTableUpdateDto.getId())
                .set(CodeTable::getCodeTableName, codeTableUpdateDto.getCodeTableName())
                .set(CodeTable::getCodeTableDesc, codeTableUpdateDto.getCodeTableDesc())
                .set(CodeTable::getUpdateTime, LocalDateTime.now());
        codeTableMapper.update(null, lambdaUpdateWrapper);
        //码值删除
        LambdaQueryWrapper<CodeValue> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CodeValue::getCodeTableNumber, codeTableUpdateDto.getCodeTableNumber());
        codeValueMapper.delete(lambdaQueryWrapper);
        //码值增加
        codeTableUpdateDto.getCodeValueList().forEach(value -> {
            CodeValue codeValue = new CodeValue();
            System.out.println("value==============" + value + "codeTableUpdateDto.getCodeTableNumber()========" + codeTableUpdateDto.getCodeTableNumber());
            BeanUtils.copyProperties(value, codeValue);
            codeValue.setCodeTableNumber(codeTableUpdateDto.getCodeTableNumber());
            codeValue.setCodeValueDesc(value.getCodeValueDesc());
            codeValueMapper.insert(codeValue);
        });
        //返回
        return Result.success(null);
    }
    //获取未停用和逻辑删除的所有码表
    @Override
    public Result<Object> selectAllTable() {
        List<CodeTableValueVo> codeTables = codeTableMapper.selectTableList();
        codeTables.forEach(s->{
            CodeTable codeTable = codeTableMapper.selectById(s.getId());
            QueryWrapper<CodeValue> codeValueQueryWrapper = new QueryWrapper<>();
            codeValueQueryWrapper.lambda().eq(CodeValue::getCodeTableNumber, codeTable.getCodeTableNumber());
            List<CodeValueValueVo> codeValueValueVo = codeValueMapper.selectValueList(codeValueQueryWrapper);
            s.setCodeValueList(codeValueValueVo);
        });
        System.out.println(codeTables);
        return Result.success(codeTables);
    }
    //上传
    @Override
    public Result<Object> upload(MultipartFile file) throws Exception {
        // 获取文件名
        String path = FileUtil.uploadFile(file, FileUtil.MOULD_CODE);
        //读取Excel表中的数据
        List<CodeTableTemplate> res = ReadFile.readWrite(path, CodeTableTemplate.class);
        //销毁临时文件
        FileUtil.destroyFile(path);
        //文件校验
        List<ValidationError> validationErrors = validateList(res);
        //过滤错误的值
        List<CodeTableTemplate> list = filterValidTemplates(res, validationErrors);
        //过滤相同的值
        List<CodeTableTemplate> resList = filterDuplicateCodeTableNames(list);
        //新增
        resList.forEach(codeTableTemplate -> {
            List<CodeTableTemplateValue> codeValues = codeTableTemplate.getCodeValues();
            String codeTableCount = getCodeTableCount();
            CodeTable codeTable = CodeTable.builder()
                    .codeTableName(codeTableTemplate.getCodeTableName())
                    .codeTableDesc(codeTableTemplate.getCodeTableDesc())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .codeTableNumber(codeTableCount)
                    .build();
            codeTableMapper.insert(codeTable);
            codeValues.forEach(codeTableTemplateValue -> {
                CodeValue codeValue = CodeValue.builder()
                        .codeTableNumber(codeTableCount)
                        .codeValueName(codeTableTemplateValue.getCodeValueName())
                        .codeValueDesc(codeTableTemplateValue.getCodeValueDesc())
                        .codeValueValue(codeTableTemplateValue.getCodeValueValue())
                        .build();
                codeValueMapper.insert(codeValue);
            });
        });
        return Result.success(null);
    }
}
