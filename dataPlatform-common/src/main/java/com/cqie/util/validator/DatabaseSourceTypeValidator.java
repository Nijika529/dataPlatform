package com.cqie.util.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-21 11:00
 **/



public class DatabaseSourceTypeValidator implements ConstraintValidator<DatabaseSourceValidator, String> {
    private String[] supportedTypes;

    @Override
    public void initialize(DatabaseSourceValidator constraintAnnotation) {
        // 获取注解中定义的支持的数据库类型
        supportedTypes = constraintAnnotation.supportedTypes();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 允许为空的值
        }

        // 检查value是否在支持的数据库类型列表中
        for (String supportedType : supportedTypes) {
            if (supportedType.equalsIgnoreCase(value)) {
                return true; // 如果匹配，则验证通过
            }
        }

        // 如果没有匹配的类型，则记录错误信息
        context.disableDefaultConstraintViolation();
//        context.buildConstraintViolationWithTemplate(constraintAnnotation.message())
//                .addConstraintViolation();
        return false; // 验证不通过
    }
}