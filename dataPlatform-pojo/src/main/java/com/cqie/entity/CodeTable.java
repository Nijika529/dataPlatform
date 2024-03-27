package com.cqie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 * 码表管理
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("code_table")
@ApiModel(value = "CodeTable对象", description = "码表管理")
public class CodeTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("码表编号")
    private String codeTableNumber;

    @ApiModelProperty("码表名称")
    private String codeTableName;

    @ApiModelProperty("码表描述")
    private String codeTableDesc;

    @ApiModelProperty("码表状态（0：未发布，1：已发布，2：已停用）")
    private Integer codeTableState;

    @ApiModelProperty("0未删除，其他值为删除")
    private Integer deleteFlag;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;


}
