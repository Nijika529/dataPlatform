package com.cqie.dto.dataAssetField;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-04-07 20:21
 **/
@Getter
@Setter
@TableName("data_asset_field")
@ApiModel(value = "DataAssetField对象", description = "数据资产字段表")
public class DataAssetFieldDto implements Serializable {


    @ApiModelProperty("字段名称")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]*$")
    @NotBlank(message = "英文名称不能为空")
    private String fieldName;

    @ApiModelProperty("对应中文名称")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5A-Za-z]+$")
    @NotBlank(message = "中文名称不能为空")
    private String fieldCnName;

    @ApiModelProperty("字段描述")
    private String fieldDescription;

    @ApiModelProperty("数据标准编码")
    private String dataStandardCode;

}
