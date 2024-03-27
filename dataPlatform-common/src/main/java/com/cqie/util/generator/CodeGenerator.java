package com.cqie.util.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-14 15:02
 **/

public class CodeGenerator {

    private final static String url = "jdbc:mysql://localhost:3306/data_factory?serverTimezone=UTC";

    private final static String username = "root";

    private final static String password = "123456";

    //获取项目路径
    private static final String PARENT_DIR = System.getProperty("user.dir");

    //java下的文件路径
    private static final String filePath = PARENT_DIR + "/dataPlatform-pojo/src/main/java";

    public static void main(String[] args) {
        List<String>  tables = new ArrayList<>();

        tables.add("category_info");
        tables.add("code_table");
        tables.add("code_value");
        tables.add("data_asset");
        tables.add("data_asset_field");
        tables.add("data_asset_relation_category");
        tables.add("data_standard");
        tables.add("script");
        tables.add("source_api");
        tables.add("source_database");

        FastAutoGenerator.create(url, username, password)
                //全局配置
                .globalConfig(builder -> {
                    builder.author("zlx") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .disableOpenDir()//生成后不打开目录
                            .fileOverride()
                            .dateType(DateType.TIME_PACK)
                            .commentDate("yyyy-MM-dd")
                            .outputDir(filePath);
                })
                .packageConfig(builder -> {
                    builder.parent("")
                            .xml("mappers")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, PARENT_DIR + "/dataPlatform-server/src/main/resources/mapper"))
                            .entity("com.cqie.entity")
                            .pathInfo(Collections.singletonMap(OutputFile.entity, PARENT_DIR + "/dataPlatform-pojo/src/main/java/com/cqie/entity"))
                            .controller("com.cqie.controller")
                            .pathInfo(Collections.singletonMap(OutputFile.controller, PARENT_DIR + "/dataPlatform-server/src/main/java/com/cqie/controller"))
                            .service("com.cqie.service")
                            .pathInfo(Collections.singletonMap(OutputFile.service, PARENT_DIR + "/dataPlatform-server/src/main/java/com/cqie/service"))
                            .serviceImpl("com.cqie.service.Impl")
                            .pathInfo(Collections.singletonMap(OutputFile.serviceImpl, PARENT_DIR + "/dataPlatform-server/src/main/java/com/cqie/service/Impl"))
                            .mapper("com.cqie.mapper")
                            .pathInfo(Collections.singletonMap(OutputFile.mapper, PARENT_DIR + "/dataPlatform-server/src/main/java/com/cqie/mapper"));
                })




                .strategyConfig(builder -> {
                    builder.addInclude(tables) // 设置需要生成的表名
                            .addTablePrefix("b_", "t_"); // 设置过滤表前缀
                    builder.entityBuilder()
                            .enableLombok()
                            .enableRemoveIsPrefix()
                            .build();
                    builder.serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl");
                })
                .templateEngine(new VelocityTemplateEngine())  // 使用 Freemarker 引擎模板
                .execute();
    }
}
