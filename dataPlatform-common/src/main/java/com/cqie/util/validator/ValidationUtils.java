package com.cqie.util.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
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

}
