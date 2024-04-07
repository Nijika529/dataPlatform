package com.cqie.dto.category;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-04-07 09:35
 **/
@Data
@Validated
@TableName("category_info")
@ApiModel(value = "CategoryInfo对象", description = "分类信息表")
public class CategoryDto implements Serializable {

    @TableId(value = "category_code", type = IdType.AUTO)
    private Integer categoryCode;

    @NotNull(message = "父级分类编号不能为空")
    @ApiModelProperty("父级分类编码")
    private Integer parentCode;

    @NotEmpty(message = "分类名称不能为空")
    @ApiModelProperty("分类名称")
    private String categoryName;

    @NotNull(message = "分类类型不能为空")
    private Integer categoryType;

}
