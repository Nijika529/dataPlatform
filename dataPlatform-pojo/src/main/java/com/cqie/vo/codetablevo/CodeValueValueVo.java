package com.cqie.vo.codetablevo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-18 16:01
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeValueValueVo implements Serializable {

    @ApiModelProperty("码值名称")
    @Pattern(regexp = "^(?!\\s*$)[A-Za-z\u4e00-\u9fa5]+$", message = "码值名称只能包含中文、大小写英文且不能为空或空格")
    private String codeValueName;

    @NotEmpty
    @ApiModelProperty("码值取值")
    private String codeValueValue;

    @ApiModelProperty("码值含义")
    private String codeValueDesc;

    private Integer codeValueNumber;
}
