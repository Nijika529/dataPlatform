package com.cqie.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * <p>
 * 分类信息表
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
@Data
@Validated
@TableName("category_info")
@ApiModel(value = "CategoryInfo对象", description = "分类信息表")
public class CategoryInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer categoryCode;

    @ApiModelProperty("父级分类编码")
    private String parentCode;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("分类描述")
    private String information;

    @ApiModelProperty("逻辑删除标识 0 未删除 1 已删除")
    private Integer deleteFlag;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;


}
