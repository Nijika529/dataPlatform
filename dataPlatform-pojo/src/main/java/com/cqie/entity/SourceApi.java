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
 * 接口数据源
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@Getter
@Setter
@TableName("source_api")
@ApiModel(value = "SourceApi对象", description = "接口数据源")
public class SourceApi implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("接口编码")
    private String apiCode;

    @ApiModelProperty("接口分类编码")
    private Integer categoryCode;

    @ApiModelProperty("接口来源")
    private String apiOrigin;

    @ApiModelProperty("接口名称")
    private String apiName;

    @ApiModelProperty("接口描述")
    private String apiDesc;

    @ApiModelProperty("接口协议（0：http，1：https）	")
    private Integer apiProtocol;

    @ApiModelProperty("接口端口")
    private String apiPort;

    @ApiModelProperty("接口路径")
    private String apiPath;

    @ApiModelProperty("接口请求方式（0：GET，1：POST）")
    private Integer apiRequestMethod;

    @ApiModelProperty("接口超时时间")
    private Integer apiTimeoutTime;

    @ApiModelProperty("输入参数")
    private String apiRequestParams;

    @ApiModelProperty("请求body")
    private String apiRequestBody;

    @ApiModelProperty("返回参数")
    private String apiResponse;

    @ApiModelProperty("接口状态（0：未发布，1：已发布，2已停用）")
    private Integer apiState;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("删除标记  是否删除，0正常，其余为删除")
    private Integer deleteFlag;


}
