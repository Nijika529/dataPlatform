package com.cqie.util.file;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        Workbook workbook = new XSSFWorkbook(Files.newInputStream(Paths.get(path)));
        Sheet sheet = workbook.getSheetAt(0);
        for (int i=0;i<sheet.getLastRowNum();i++){
            Row row = sheet.getRow(i);
            if(row!=null&&row.getRowStyle()!=null){
                importParams.setHeadRows(i+1);
            }
        }

        importParams.setTitleRows(0);
        List<T> res = ExcelImportUtil.importExcel(fileInputStream, clazz, importParams);
        fileInputStream.close();
        return res;
    }
}