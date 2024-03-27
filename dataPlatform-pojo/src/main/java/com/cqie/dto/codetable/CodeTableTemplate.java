package com.cqie.dto.codetable;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-19 17:48
 **/

@Data
public class CodeTableTemplate implements Serializable {
    @Excel(name = "码表名称", needMerge = true)
    @Pattern(regexp = "^(?!\\s*$)[A-Za-z\u4e00-\u9fa5]+$", message = "码值名称只能包含中文、大小写英文且不能为空或空格")
    @NotEmpty
    @Length(max = 30, message = "最大长度为30")
    private String codeTableName;

    @Excel(name = "码表说明", needMerge = true)
    @Length(max = 200, message = "最大长度为200")
    private String codeTableDesc;

    @ExcelCollection(name = "编码信息", orderNum = "3")
    @Valid
    private List<com.cqie.dto.codetable.CodeTableTemplateValue> codeValues;
}
