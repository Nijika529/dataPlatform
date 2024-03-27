package com.cqie.excepition;

import com.cqie.util.R.Result;
import com.cqie.util.R.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: nongmuhui001
 * @description:
 * @author: zlx
 * @create: 2023-12-14 09:35
 **/


@ControllerAdvice
@Slf4j
public class BasicExceptionHandler {


    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<Object> errorHandler(MethodArgumentNotValidException exception){
        StringBuilder message= new StringBuilder();
        for (ObjectError error:exception.getAllErrors()){
         message.append("{").append(error.getDefaultMessage()).append("}");
        }
        return Result.failed(null, ResultCodeEnum.REQUEST_PARAM_ERROR.getCode(), message.toString());
    }



    /**
     * 处理全局异常
     * @param exception 异常
     * @return
     */

//    @ResponseBody
//    @ExceptionHandler(value = Exception.class)
//    public Result errorHandler(Exception exception){
//        return Result.Failed(null,ResultCodeEnum.SYSTEM_EXCEPTION.getCode(),exception.getMessage());
//    }
//


}
