import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;

import com.cqie.dto.codetable.CodeTableTemplate;
import com.cqie.excelCreate.ExcelCreateData;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;



/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-14 14:47
 **/

public class test {

//    @Test
//    public void complexHeadWrite() {
//        ExcelCreateData excelCreateData = new ExcelCreateData();
//        String fileName = "D:\\" + "complexHeadWrite" + System.currentTimeMillis() + ".xlsx";
//        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
//        EasyExcel.write(fileName, CodeTableTemplate.class).sheet("码表").doWrite(excelCreateData.data());
//    }

    String path = "E:\\springClound\\Redis\\shizhan\\dataPlatform\\dataPlatform-server";
    String file ="码表.xls";



    @Test
    public void headWrite() throws Exception {
        ExcelCreateData excelCreateData = new ExcelCreateData();
        FileOutputStream fileOutputStream = new FileOutputStream(path+file);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(),
                CodeTableTemplate.class, excelCreateData.data());
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        workbook.close();

    }

    @Test
    public void readWrite() throws Exception {
        FileInputStream fileInputStream = new FileInputStream(path + file);
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(2);
        importParams.setTitleRows(0);
        List<CodeTableTemplate> res = ExcelImportUtil.importExcel(fileInputStream, CodeTableTemplate.class, importParams);
        fileInputStream.close();
        System.out.println(res);
    }

    @Test
    public void test() {
        URL resourceUrl = test.class.getClassLoader().getResource("mapper/CodeTableMapper.xml");
        if (resourceUrl != null) {
            System.out.println(resourceUrl);
        }
    }
}
