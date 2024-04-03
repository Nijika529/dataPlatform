package com.cqie.util.validator;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.cqie.dto.dataStandard.StandardTemplate;
import com.cqie.enums.IsEmptyEnum;
import com.cqie.enums.IsEmptyStringEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-26 18:06
 **/
@Component
public class ValidationUtils {

    public static  <T> Map<String, List<T>> validateData(List<T> list, Validator validator){
        List<T> failList = new ArrayList<>();
        List<T> successList = new ArrayList<>();
        list.forEach(data->{
            Set<ConstraintViolation<T>> violations = validator.validate(data);
            if(!violations.isEmpty()){
                failList.add(data);
            }else{
                successList.add(data);
            }
        });
        Map<String,List<T>> map = new HashMap<>();
        map.put("failList",failList);
        map.put("successList",successList);
        return map;
    }

    public static List<StandardTemplate> validateStandard(List<StandardTemplate> list, Validator validator) {
        List<StandardTemplate> standardTemplates = new ArrayList<>();

        //去除校验失败的
        list.forEach(data -> {
            Set<ConstraintViolation<StandardTemplate>> validate = validator.validate(data);
            if (validate.isEmpty()) {
                standardTemplates.add(data);
            }
        });

        //去除相同的元素
        List<StandardTemplate> res = new ArrayList<>();
        standardTemplates.forEach(l-> {
            if (!res.contains(l)) {
                res.add(l);
            }
        });

        //取值范围最小值与取值范围最大值对于标准的数据类型为Int时只能取整数，对于标准的数据类型为Float时可以填整数与实数。
            //正则表达式
        List<StandardTemplate> res1 = new ArrayList<>();
        String intPattern=  "^\\d+$";
        String floatPattern = "[+-]?\\d+(\\.\\d+)?";
        res.forEach(data -> {
            //进行筛选
            switch (data.getDataStandardType()) {
                //取值范围最小值与取值范围最大值对于标准的数据类型为Int时只能取整数
                case "Int":
                      if (data.getDataStandardValueMin().matches(intPattern)
                              && data.getDataStandardValueMax().matches(floatPattern)) {
                          res1.add(data);
                      }
                      break;
                //对于标准的数据类型为Float时可以填整数与实数
                case "Float":
                    if (data.getDataStandardValueMin().matches(floatPattern)
                            && data.getDataStandardValueMax().matches(floatPattern)) {
                        res1.add(data);
                    }
                    break;
            }
        });

        //不同数据类型字段为空校验
        List<StandardTemplate> res2 = new ArrayList<>();
        res1.forEach(data -> {
            //进行筛选
            switch (data.getDataStandardType()) {
                //当数据类型为Int时 数据长度、数据精度、枚举范围（导入时为“引用码表编号”）必须为空；
                case "Int":
                    if (ObjectUtils.isEmpty(data.getDataStandardLength())
                            && ObjectUtils.isEmpty(data.getDataStandardEnumerationRange())
                            && ObjectUtils.isEmpty(data.getDataStandardAccuracy())) {
                        res2.add(data);
                    }
                    break;
                //当数据类型为Float时，数据长度、枚举范围（导入时为“引用码表编号”）必须为空；
                case "Float":
                    if (ObjectUtils.isEmpty(data.getDataStandardLength())
                            && ObjectUtils.isEmpty(data.getDataStandardEnumerationRange())
                           ) {
                        res2.add(data);
                    }
                    break;
                //当数据类型为Enum时，数据长度、数据精度、取值范围最小值、取值范围最大值必须为空；
                case "Enum":
                    if (ObjectUtils.isEmpty(data.getDataStandardLength())
                            && ObjectUtils.isEmpty(data.getDataStandardValueMin())
                            && ObjectUtils.isEmpty(data.getDataStandardValueMax())
                            && ObjectUtils.isEmpty(data.getDataStandardAccuracy())) {
                        res2.add(data);
                    }
                    break;
                //当数据类型为String时 数据精度、取值范围最小值、取值范围最大值、枚举范围（导入时为“引用码表编号”）必须为空；
                case "String":
                    if (ObjectUtils.isEmpty(data.getDataStandardEnumerationRange())
                            && ObjectUtils.isEmpty(data.getDataStandardValueMin())
                            && ObjectUtils.isEmpty(data.getDataStandardValueMax())
                            && ObjectUtils.isEmpty(data.getDataStandardAccuracy())) {
                        res2.add(data);
                    }
                    break;
            }
        });

        List<StandardTemplate> res3 = new ArrayList<>();
        res2.forEach(data -> {
//            //获取可以为空和不可为空
//            if (!data.getDataStandardIsBlank().equals(IsEmptyStringEnum.CAN_EMPTY) && !data.getDataStandardIsBlank().equals(IsEmptyStringEnum.NOT_CAN_EMPTY)) {
//                return Result.failed(null, 0, "必须为可以为空和不可为空")
//            }
            //判断可以为空和不可为空
            if (Objects.equals(data.getDataStandardIsBlank(), IsEmptyStringEnum.NOT_CAN_EMPTY)) {
                res3.add(data);
            }
            if (Objects.equals(data.getDataStandardIsBlank(), IsEmptyStringEnum.CAN_EMPTY)) {
                res3.add(data);
            }
        });


        return res3;
    }

}
