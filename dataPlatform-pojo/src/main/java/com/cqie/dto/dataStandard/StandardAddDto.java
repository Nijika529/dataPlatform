package com.cqie.dto.dataStandard;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-25 09:13
 **/

@Data
public class StandardAddDto implements Serializable {

    @ApiModelProperty("中文名称")
    @NotBlank(message = "中文名称不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5A-Za-z]+$", message = "只能有中文及英文大小写")
    @Length(max = 30, message = "中文名称长度不能超过30")
    private String dataStandardCnName;

    @ApiModelProperty("英文名称")
    @NotBlank(message = "英文名称不能为空")
    @Length(max = 30, message = "英文名称长度不能超过30")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]*$", message = "只能有英文大小写、数字及下划线且只能英文开头")
    private String dataStandardEnName;

    @ApiModelProperty("标准说明")
    @Length(max = 200, message = "标准说明长度不能超过30")
    private String dataStandardExplain;

    @ApiModelProperty("来源机构编码")
    @NotNull(message = "来源机构编码不能为空")
    private String dataStandardSourceOrganization;

    @ApiModelProperty("数据类型：类型字典项编码")
    @NotNull(message = "数据类型不能为空")
    private String dataStandardType;

    @ApiModelProperty("数据长度")
    @Min(value = 0, message = "数据长度只能为正整数")
    private Long dataStandardLength;

    @ApiModelProperty("数据精度")
    @Min(value = 1, message = "数据精度只能为非负整数")
    private Integer dataStandardAccuracy;

    @ApiModelProperty("默认值")
    private String dataStandardDefaultValue;

    @ApiModelProperty("取值范围-最大值")
    private String dataStandardValueMax;

    @ApiModelProperty("取值范围-最小值")
    private String dataStandardValueMin;

    @ApiModelProperty("枚举范围：字典组编码")
    private String dataStandardEnumerationRange;

    @ApiModelProperty("是否可为空：0不可为空，1可为空")
    @NotNull(message = "是否可为空不能为空")
    private String dataStandardIsBlank;


//    @AssertTrue(message = "数据类型必须为String, Int, Enum, Float")
//    public boolean isDataStandardType() {
//        String[] types = {"String", "Int", "Enum", "Float"};
//        for (String type : types) {
//            if (type.equals(dataStandardType)) {
//                return true;
//            }
//        }
//        return false;
//    }

}
