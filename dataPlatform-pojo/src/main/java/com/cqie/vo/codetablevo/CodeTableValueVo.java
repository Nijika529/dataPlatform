package com.cqie.vo.codetablevo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-18 15:59
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CodeTableValueVo implements Serializable {

    private Integer id;

    @ApiModelProperty("码表编号")
    private String codeTableNumber;

    @ApiModelProperty("码表名称")
    private String codeTableName;

    @ApiModelProperty("码表描述")
    private String codeTableDesc;

    @ApiModelProperty("码表")
    private List<CodeValueValueVo> codeValueList;

}
