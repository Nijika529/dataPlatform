package com.cqie.vo.dataStandard;

import com.baomidou.mybatisplus.annotation.TableId;
import com.cqie.vo.codetablevo.CodeTableValueVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-25 10:21
 **/
@Data
public class DataStandardPageVO implements Serializable {

    @TableId
    @ApiModelProperty("数据标准id")
    private Integer id;

    @ApiModelProperty("标准编号")
    private String dataStandardCode;

    @ApiModelProperty("中文名称")
    private String dataStandardCnName;

    @ApiModelProperty("来源机构")
    private String dataStandardSourceOrganization;

    @ApiModelProperty("英文名称")
    private String dataStandardEnName;

    @ApiModelProperty("标准说明")
    private String dataStandardExplain;

    @ApiModelProperty("数据类型：类型字典项编码")
    private String dataStandardType;

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

    @ApiModelProperty("枚举范围")
    private CodeTableValueVo codeTable;

    @ApiModelProperty("标准状态: 状态字典项编码")
    private Integer dataStandardState;

    @ApiModelProperty("是否可为空：0不可为空，1可为空")
    private Integer dataStandardIsBlank;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

}
