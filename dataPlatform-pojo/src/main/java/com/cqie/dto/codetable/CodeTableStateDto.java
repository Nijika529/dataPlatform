package com.cqie.dto.codetable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-18 14:58
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CodeTableStateDto implements Serializable {

    @NotEmpty(message = "选择不能为空")
    private List<Integer> ids;

    private Integer state;

}
