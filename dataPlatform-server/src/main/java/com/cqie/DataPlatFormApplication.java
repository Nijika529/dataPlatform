package com.cqie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-15 16:04
 **/
@SpringBootApplication
@MapperScan("com.cqie.mapper")
public class DataPlatFormApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataPlatFormApplication.class, args);
    }


}
