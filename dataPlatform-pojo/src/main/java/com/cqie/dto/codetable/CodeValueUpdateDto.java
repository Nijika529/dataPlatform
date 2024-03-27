package com.cqie.dto.codetable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import java.io.Serializable;


/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-18 17:10
 **/
@Data
public class CodeValueUpdateDto implements Serializable {

    @ApiModelProperty("码值编号")
    private String codeValueNumber;


    @Length(max = 30, message = "最大长度为30")
    @ApiModelProperty("码值名称")
    private String codeValueName;

    @ApiModelProperty("码值取值")
    private String codeValueValue;

    @ApiModelProperty("码值含义")
    private String codeValueDesc;

}
