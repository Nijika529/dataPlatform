package com.cqie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@Getter
@Setter
@ApiModel(value = "Script对象", description = "")
public class Script implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("脚本名称，支持中英文大小写")
    private String name;

    @ApiModelProperty("脚本分类编码")
    private Integer categoryCode;

    @ApiModelProperty("脚本类型，0-python")
    private Integer type;

    @ApiModelProperty("类名，只支持英文大小写、数字及下划线且只能英文开头")
    private String className;

    @ApiModelProperty("函数名，只支持英文大小写、数字及下划线且只能英文开头")
    private String functionName;

    @ApiModelProperty("脚本描述")
    private String describe;

    @ApiModelProperty("脚本状态，0-未发布 1-已发布 2-已停用")
    private Integer status;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("输入参数")
    private String scriptInputParams;

    @ApiModelProperty("输出参数")
    private String scriptOutParams;

    @ApiModelProperty("文件路径")
    private String fileUrl;

    @ApiModelProperty("逻辑删除 0删除1未删除")
    private Integer isDelete;


}
