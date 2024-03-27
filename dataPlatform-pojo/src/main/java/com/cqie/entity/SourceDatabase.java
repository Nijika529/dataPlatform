package com.cqie.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 数据库数据源
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("source_database")
@ApiModel(value = "SourceDatabase对象", description = "数据库数据源")
public class SourceDatabase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;


}
