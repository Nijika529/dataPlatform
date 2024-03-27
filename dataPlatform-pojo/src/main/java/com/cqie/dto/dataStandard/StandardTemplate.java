package com.cqie.dto.dataStandard;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-26 17:40
 **/
@Data
public class StandardTemplate implements Serializable {

    @Excel(name = "中文名称")
    @NotBlank(message = "中文名称不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5A-Za-z]+$", message = "只能有中文及英文大小写")
    @Length(max = 30, message = "中文名称长度不能超过30")
    private String dataStandardCnName;

    @Excel(name = "英文名称")
    @NotBlank(message = "英文名称不能为空")
    @Length(max = 30, message = "英文名称长度不能超过30")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]*$", message = "只能有英文大小写、数字及下划线且只能英文开头")
    private String dataStandardEnName;

    @Excel(name = "标准说明")
    @Length(max = 200, message = "标准说明长度不能超过30")
    private String dataStandardExplain;

    @Excel(name = "是否可为空")
    private Integer dataStandardIsBlank;

    @Excel(name = "来源机构")
    @NotNull(message = "来源机构编码不能为空")
    private Integer dataStandardSourceOrganization;

    @Excel(name = "默认值")
    private String dataStandardDefaultValue;

    @Excel(name = "数据类型")
    @NotNull(message = "数据类型不能为空")
    private Integer dataStandardType;

    @Excel(name = "数据长度")
    @Min(value = 0, message = "数据长度只能为正整数")
    private Long dataStandardLength;

    @Excel(name = "数据精度")
    @Min(value = 1, message = "数据精度只能为非负整数")
    private Integer dataStandardAccuracy;

    @Excel(name = "取值范围最小值")
    private String dataStandardValueMin;

    @Excel(name = "取值范围最大值")
    private String dataStandardValueMax;

    @Excel(name = "引用码表编号")
    private String dataStandardEnumerationRange;


}
