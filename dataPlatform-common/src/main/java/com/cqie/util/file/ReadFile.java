package com.cqie.util.file;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
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

@Slf4j
public class ReadFile {
    public static <T> List<T> readWrite(String path, Class<?> clazz) throws Exception {
        log.info("开始读取表格");
        FileInputStream fileInputStream = new FileInputStream(path);
        ImportParams importParams = new ImportParams();
        //创建xlsx的工作薄
        Workbook workbook = new XSSFWorkbook(Files.newInputStream(Paths.get(path)));
        //获取第一个表
        Sheet sheet = workbook.getSheetAt(0);
        int headRow = 1;
        //for循坏获取行数
        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
        if (!mergedRegions.isEmpty()) {
            // 遍历合并区域
            for (CellRangeAddress  mergedRegion:mergedRegions) {
                int firstRow = mergedRegion.getFirstRow();
                int lastRow = mergedRegion.getLastRow();
                int rowCount = lastRow - firstRow + 1;
                // 如果合并单元格的起始行号小于当前的 headRow，则更新 headRow
                if (headRow < rowCount) {
                    headRow = rowCount;
                }
            }
        }
        importParams.setHeadRows(headRow);
        importParams.setTitleRows(0);
        List<T> res = ExcelImportUtil.importExcel(fileInputStream, clazz, importParams);
        fileInputStream.close();
        return res;
    }
}