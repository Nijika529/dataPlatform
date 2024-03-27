package com.cqie.dto.codetable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-18 13:05
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeTablePageDto implements Serializable {

    @ApiModelProperty("码表状态（0：未发布，1：已发布，2：已停用）")
    private Integer codeTableState;

    @ApiModelProperty("码表名称")
    private String codeTableName;

    private Integer pageNumber = 1;

    private Integer pageSize = 20;

}
