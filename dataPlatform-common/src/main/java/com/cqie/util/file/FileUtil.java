package com.cqie.util.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @program: dataPlatform
 * @description:
 * @author: zlx
 * @create: 2024-03-20 11:52
 **/
@Slf4j
public class FileUtil {

    public static final String MOULD_CODE = "template/code/";

    public static final String MOULD_SCRIPT = "template/script/";

    public static final String DATA_CODE = "data/code/";

    public static final String DATA_SCRIPT = "data/script/";

    public static final String DATA_STANDARD_CODE = "data/dataStandard/";

    public static final String DATA_STANDARD_TEMPLATE = "template/dataStandard/";

    private static final String PATH_DIR = System.getProperty("user.dir");

    private static final String PATH_PATH = "/dataPlatform-common/src/main/resources/static/";
    public static String uploadFile(MultipartFile file, String mouldName) throws IOException {
        //获取文件名字
        String filename = file.getOriginalFilename();
        //存储路径
        String path = PATH_DIR + PATH_PATH + mouldName + "/";
        File saveFile = new File(path + filename);
        if (!saveFile.exists() && !saveFile.mkdirs()) {
            log.info("无法创建目录: " + saveFile);
        }
        //存储
        file.transferTo(saveFile);
        //
        return path + filename;
    }

    public static void destroyFile(String filePath) {
        File file = new File(filePath);
        //判断文件是否存在
        if (file.exists() && file.isFile()) {
            //删除文件
            if (file.delete()) {
                log.info("文件销毁成功: " + file.getAbsolutePath());
            } else {
                log.info("文件销毁失败: " + file.getAbsolutePath());
            }
        } else {
            log.info("文件不存在: " + file.getAbsolutePath());
        }
    }

    public static ResponseEntity<Resource> downloadFile(String filename, String code) throws IOException {
        String path = PATH_DIR + PATH_PATH + code + filename;
        Resource resource = new FileSystemResource(path);
        // 检查文件是否存在
        if (!resource.exists()) {
            // 如果文件不存在，抛出FileNotFoundException异常
            throw new FileNotFoundException("未找到文件 " + path);
        }
        HttpHeaders headers = new HttpHeaders();
        String encodedFileName = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString());
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encodedFileName);
        // 设置Content-Type为通用的二进制流类型
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        // 创建响应实体，包含文件内容和之前设置的头信息
        return ResponseEntity.ok()
                .headers(headers)
                // 设置响应体为文件的资源对象，Spring会自动处理输入流的读取和关闭
                .body(new InputStreamResource(resource.getInputStream()));
    }
}
