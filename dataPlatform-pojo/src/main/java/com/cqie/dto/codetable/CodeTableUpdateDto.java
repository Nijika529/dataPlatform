package com.cqie.dto.codetable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-18 16:49
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeTableUpdateDto implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("码表名称")
    @Pattern(regexp = "^(?!\\s*$)[A-Za-z\u4e00-\u9fa5]+$", message = "码值名称只能包含中文、大小写英文且不能为空或空格")
    @Length(max = 30, message = "最大长度为30")
    private String codeTableName;

    @ApiModelProperty("码表描述")
    @Length(max = 200, message = "最大长度为200")
    private String codeTableDesc;

    private String codeTableNumber;

    private List<CodeValueUpdateDto> codeValueList;
}
