package com.cqie.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 码值表
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("code_value")
@ApiModel(value = "CodeValue对象", description = "码值表")
public class CodeValue implements Serializable {

    @TableId
    @ApiModelProperty("码值编号")
    private Integer codeValueNumber;

    @ApiModelProperty("码值名称")
    private String codeValueName;

    @ApiModelProperty("码值取值")
    private String codeValueValue;

    @ApiModelProperty("码值含义")
    private String codeValueDesc;

    @ApiModelProperty("码表编号")
    private String codeTableNumber;

}
