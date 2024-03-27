package com.cqie.entity;

import com.cqie.dto.codetable.CodeTableTemplate;
import lombok.Data;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-20 17:22
 *
 * 校验错误类
 **/
@Data
public class ValidationError {
    private String message;
    private CodeTableTemplate codeTableTemplate;
}
