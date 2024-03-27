package com.cqie.util.validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-20 14:29
 * 开启校验类
 **/
@Configuration
public class AppConfig {

    @Bean
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

}
