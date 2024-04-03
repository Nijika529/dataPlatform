package com.cqie.dto.dataStandard;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-04-02 17:19
 **/


@Getter
public class StandardUpdateDto extends StandardAddDto implements Serializable {
    private Integer id;
}
