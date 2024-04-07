package com.cqie.dto.dataStandard;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-25 10:16
 **/
@Data
public class StandardGetAllDto implements Serializable {

    @ApiModelProperty("标准编号")
    private String dataStandardCode;

    @ApiModelProperty("中文名称")
    private String dataStandardCnName;

    @ApiModelProperty("英文名称")
    private String dataStandardEnName;

    @ApiModelProperty("来源机构编码")
    private Integer dataStandardSourceOrganization;

    private String dataStandardEnumerationRange;

    @ApiModelProperty("标准状态: 状态字典项编码")
    private Integer dataStandardState;

    private Integer pageNumber = 1;

    private Integer pageSize = 10;

}
