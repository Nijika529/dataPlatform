package com.cqie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 数据标准表
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("data_standard")
@ApiModel(value = "DataStandard对象", description = "数据标准表")
public class DataStandard implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("数据标准id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("标准编号")
    private String dataStandardCode;

    @ApiModelProperty("中文名称")
    private String dataStandardCnName;

    @ApiModelProperty("英文名称")
    private String dataStandardEnName;

    @ApiModelProperty("标准说明")
    private String dataStandardExplain;

    @ApiModelProperty("来源机构编码")
    private Integer dataStandardSourceOrganization;

    @ApiModelProperty("数据类型：类型字典项编码")
    private Integer dataStandardType;

    @ApiModelProperty("数据长度")
    private Long dataStandardLength;

    @ApiModelProperty("数据精度")
    private Integer dataStandardAccuracy;

    @ApiModelProperty("默认值")
    private String dataStandardDefaultValue;

    @ApiModelProperty("取值范围-最大值")
    private String dataStandardValueMax;

    @ApiModelProperty("取值范围-最小值")
    private String dataStandardValueMin;

    @ApiModelProperty("枚举范围：字典组编码")
    private String dataStandardEnumerationRange;

    @ApiModelProperty("标准状态: 状态字典项编码")
    private Integer dataStandardState;

    @ApiModelProperty("是否可为空：0不可为空，1可为空")
    private Integer dataStandardIsBlank;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("删除标识符: 0未删除，时间戳已删除")
    private Integer deleteFlag;


}
