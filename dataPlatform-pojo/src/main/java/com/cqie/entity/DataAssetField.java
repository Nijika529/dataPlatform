package com.cqie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 数据资产字段表
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@Getter
@Setter
@TableName("data_asset_field")
@ApiModel(value = "DataAssetField对象", description = "数据资产字段表")
public class DataAssetField implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("数据资产表编码")
    private String dataAssetCode;

    @ApiModelProperty("字段名称")
    private String fieldName;

    @ApiModelProperty("对应中文名称")
    private String fieldCnName;

    @ApiModelProperty("字段描述")
    private String fieldDescription;

    @ApiModelProperty("数据标准编码")
    private String dataStandardCode;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;


}
