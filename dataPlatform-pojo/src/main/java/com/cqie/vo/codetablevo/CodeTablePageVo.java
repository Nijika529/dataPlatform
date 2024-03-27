package com.cqie.vo.codetablevo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-18 13:02
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CodeTablePageVo implements Serializable {

    private Integer id;

    @ApiModelProperty("码表名称")
    private String codeTableName;

    @ApiModelProperty("码表编号")
    private String codeTableNumber;

    @ApiModelProperty("码表描述")
    private String codeTableDesc;

    @ApiModelProperty("码表状态（0：未发布，1：已发布，2：已停用）")
    private Integer codeTableState;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
