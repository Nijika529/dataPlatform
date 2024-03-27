package com.cqie.util.file;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.apache.poi.ss.formula.functions.T;

import java.io.FileInputStream;
import java.util.List;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-20 13:29
 **/

public class ReadFile {
    public static <T> List<T> readWrite(String path, Class<?> clazz) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(path);
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(2);
        importParams.setTitleRows(0);
        List<T> res = ExcelImportUtil.importExcel(fileInputStream, clazz, importParams);
        fileInputStream.close();
        return res;
    }
}
