package com.cqie.util.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-21 10:57
 *
 * 验证数据库源类型是否有效。
 *
 **/
@Constraint(validatedBy = DatabaseSourceTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseSourceValidator {
    String message() default "Invalid database source type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    // 定义支持的数据库类型
    String[] supportedTypes() default {};
}