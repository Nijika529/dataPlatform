package com.cqie.excelCreate;

import com.cqie.dto.codetable.CodeTableTemplate;
import com.cqie.dto.codetable.CodeTableTemplateValue;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-20 09:22
 **/
@Data
public class ExcelCreateData {
    public List<CodeTableTemplate> data() {
        List<CodeTableTemplate> codeTableTemplate = new ArrayList<>();
        for (int i = 0 ; i<10; i++) {
            CodeTableTemplate codeTableTemplate1 = new CodeTableTemplate();
            List<CodeTableTemplateValue> codeTableTemplateValue = new ArrayList<>();
            for (int j = 0 ; j<5; j++) {
                CodeTableTemplateValue codeTableTemplateValue1 = new CodeTableTemplateValue();
                codeTableTemplateValue1.setCodeValueName("何龙" + j);
                codeTableTemplateValue1.setCodeValueValue("是" + j);
                codeTableTemplateValue.add(codeTableTemplateValue1);
            }
            codeTableTemplate1.setCodeTableName("我" + i);
            codeTableTemplate1.setCodeTableDesc("dog");
            codeTableTemplate1.setCodeValues(codeTableTemplateValue);
            codeTableTemplate.add(codeTableTemplate1);
        }
        return codeTableTemplate;
    }
}
