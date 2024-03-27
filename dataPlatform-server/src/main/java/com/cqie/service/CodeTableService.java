package com.cqie.service;

import com.cqie.dto.codetable.CodeTableDto;
import com.cqie.dto.codetable.CodeTablePageDto;
import com.cqie.dto.codetable.CodeTableStateDto;
import com.cqie.dto.codetable.CodeTableUpdateDto;
import com.cqie.entity.CodeTable;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqie.util.R.Result;
import com.cqie.vo.codetablevo.CodeTableValueVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 码表管理 服务类
 * </p>
 *
 * @author zlx
 * @since 2024-03-15
 */
public interface CodeTableService extends IService<CodeTable> {

    Result<Object> saveT(CodeTableDto codeTableDto);

    Result<String> deleteId(Integer id);

    Result<Object> pageT(CodeTablePageDto pageDto);

    Result<Object> state(CodeTableStateDto codeTableStateDto);

    Result<CodeTableValueVo> getByIdId(Integer id);

    Result<Object> updateTable(CodeTableUpdateDto codeTableUpdateDto);

    Result<Object> selectAllTable();

    Result<Object> upload(MultipartFile file) throws Exception;


}
