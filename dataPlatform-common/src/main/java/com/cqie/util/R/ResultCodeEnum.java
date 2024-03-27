package com.cqie.util.R;

import lombok.Data;
import lombok.Getter;

/**
 * @author cx
 */

@Getter
public enum ResultCodeEnum {
    SUCCESS(200, "返回成功"),
    SYSTEM_EXCEPTION(500, "系统异常"),
    REQUEST_PARAM_ERROR(401, "请求参数检验错误"),
    REQUEST_OUT_OVERTIME(408, "请求超时"),
    REQUEST_NOT_FOUND(404, "请求的资源或服务未找到"),
    REQUEST_LENGTH_LIMIT(414, "请求URI太长"),
    REQUEST_Format_NOT_SUPPORTED(415, "请求的格式不支持"),
    ALREADY_EXISTS(416, "已存在此中文")
    ;
    /**
     * 枚举值
     */
    private Integer code;
    private String message;
    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
