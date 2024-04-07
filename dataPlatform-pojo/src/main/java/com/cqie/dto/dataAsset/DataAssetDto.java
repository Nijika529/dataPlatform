package com.cqie.dto.dataAsset;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cqie.dto.dataAssetField.DataAssetFieldDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-04-07 20:18
 **/
@Data
@Validated
@TableName("data_asset")
@ApiModel(value = "DataAsset对象", description = "数据资产表")
public class DataAssetDto implements Serializable {

    private Integer id;

    @ApiModelProperty("数据资产编码")
    private String dataAssetCode;

    @ApiModelProperty("数据资产名称")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5A-Za-z]+$")
    @NotBlank(message = "中文名称不能为空")
    private String assetName;

    @ApiModelProperty("数据资产英文名称")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]*$")
    @NotBlank(message = "英文名称不能为空")
    private String assetNameEn;

    @ApiModelProperty("数据资产表描述")
    private String assetDesc;

    @ApiModelProperty("数据资产字段")
    private ArrayList<DataAssetFieldDto> dataAssetField;

    @ApiModelProperty("所属目录")
    @NotBlank
    private String path;



}
