package com.cqie.vo.sourcedatabasevo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @create: 2024-03-22 16:45
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SourceDatabasePageVO implements Serializable {

    private Integer id;

    @ApiModelProperty("数据库数据源名称")
    private String databaseSourceName;

    @ApiModelProperty("数据库数据源类型（例如：mysql、mongodb等）")
    private String databaseSourceType;

    @ApiModelProperty("数据库数据源描述")
    private String databaseSourceDesc;

    @ApiModelProperty("数据库数据源路径")
    private String databaseSourceUrl;

    @ApiModelProperty("数据库数据源状态")
    private Integer databaseSourceState;

    @ApiModelProperty("数据用户名")
    private String databaseSourceUsername;

    @ApiModelProperty("密码")
    private String databaseSourcePassword;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;


}
