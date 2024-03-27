package com.cqie.dto.codetable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

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
public class CodeTableDto implements Serializable {


    @ApiModelProperty("码表名称")
    @Pattern(regexp = "^(?!\\s*$)[A-Za-z\u4e00-\u9fa5]+$", message = "码值名称只能包含中文、大小写英文且不能为空或空格")
    @Length(max = 30, message = "最大长度为30")
    private String codeTableName;

    @ApiModelProperty("码表描述")
    @Length(max = 200, message = "最大长度为200")
    private String codeTableDesc;

    private Integer codeTableState;

    private List<CodeValueDto> codeValueList;

}
