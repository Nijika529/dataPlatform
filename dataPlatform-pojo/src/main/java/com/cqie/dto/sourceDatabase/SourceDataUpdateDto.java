package com.cqie.dto.sourceDatabase;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-23 20:57
 **/
@Data
public class SourceDataUpdateDto {

    private Integer id;

    @ApiModelProperty("数据库数据源名称")
    @NotBlank(message = "数据库数据源名称不能为空")
    @Length(max = 30,message = "数据源名称长度在1-30之间")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5A-Za-z0-9_]+$",message = "数据源名称字段支持中英文大小写、数字、下划线，不支持特殊符号及空格")
    private String databaseSourceName;


    @ApiModelProperty("数据库数据源描述")
    @Length(max = 200,message = "数据库描述长度在1-200之间")
    private String databaseSourceDesc;

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
