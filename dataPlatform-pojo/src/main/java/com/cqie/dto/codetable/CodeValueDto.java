package com.cqie.dto.codetable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

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
public class CodeValueDto implements Serializable {

    @ApiModelProperty("码值名称")
    @Pattern(regexp = "^(?!\\s*$)[A-Za-z\u4e00-\u9fa5]+$", message = "码值名称只能包含中文、大小写英文且不能为空或空格")
    @Length(max = 30, message = "最大长度为30")
    private String codeValueName;

    @NotEmpty
    @ApiModelProperty("码值取值")
    private String codeValueValue;

    @ApiModelProperty("码值含义")
    private String codeValueDesc;

}
