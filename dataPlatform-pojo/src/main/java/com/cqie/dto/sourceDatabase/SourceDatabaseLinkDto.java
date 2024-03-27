package com.cqie.dto.sourceDatabase;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-24 20:34
 **/
@Data
public class SourceDatabaseLinkDto implements Serializable {

    private Integer id;

    @ApiModelProperty("数据库数据源路径")
    @NotBlank(message = "jdbc-url不能为空")
    @Pattern(regexp = "^jdbc:mysql://([a-zA-Z0-9_.-]+(:[0-9]+)?/)?[\\w.]+(:[0-9]+)/[\\w-]+(\\?.*)?$",
            message = "url格式不规范"
    )
    private String databaseSourceUrl;


    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String databaseSourceUsername;

    @ApiModelProperty("密码")
    private String databaseSourcePassword;

}
