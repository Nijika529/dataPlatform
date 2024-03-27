package com.cqie.dto.sourceDatabase;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-22 16:32
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SourceDatabasePageDto implements Serializable {

    private Integer pageNumber = 1;

    private Integer pageSize = 10;

    @ApiModelProperty("数据库数据源名称")
    private String name;

    @ApiModelProperty("数据库数据源状态")
    private Integer state;


}
