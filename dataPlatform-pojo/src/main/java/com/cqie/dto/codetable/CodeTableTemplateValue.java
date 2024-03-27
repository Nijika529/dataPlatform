package com.cqie.dto.codetable;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-19 17:48
 **/

@Data
public class CodeTableTemplateValue implements Serializable {

    @Excel(name = "编码名称")
    @Pattern(regexp = "^(?!\\s*$)[A-Za-z\u4e00-\u9fa5]+$", message = "码值名称只能包含中文、大小写英文且不能为空或空格")
    @NotEmpty
    @Length(max = 30, message = "最大长度为30")
    private String codeValueName;

    @Excel(name = "编码取值")
    @NotEmpty
    private String codeValueValue;

    @Excel(name = "编码含义")
    private String codeValueDesc;
}
