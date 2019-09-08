package com.qjkji.orderproject.core.controller;


import com.qjkji.orderproject.core.entity.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.UUID;

@Api(tags = "文件管理类API")
@Controller
@RequestMapping("/file")
public class FileController {

    @Value("${file-path}")
    private String filePath;

    /**
     * 获取服务器完整文件路径
     * @param name
     * @return
     */
    private String getTempFilePath(String name){
        StringBuilder builder = new StringBuilder();
        builder.append(filePath).append(File.separator).append(name);
        return builder.toString();
    }

    /**
     * 创建一个随机UUID命名的完整路径文件名
     * @param ext 文件类型如 jpg png zip
     * @return
     */
    private String createTempFileName(String ext){
        StringBuilder builder = new StringBuilder();
        builder.append(filePath).append(File.separator)
                .append(UUID.randomUUID().toString().replaceAll("-",""))
                .append(".").append(ext);
        return builder.toString();
    }

    @ApiOperation("上传文件")
    @ResponseBody
    @PostMapping("/upload")
    public Result uploadFile(MultipartFile file){
        if(file == null || file.isEmpty()){
            return Result.error("未上传任何文件");
        }
        File tempFile = new File(createTempFileName(FilenameUtils.getExtension(file.getOriginalFilename())));
        try {
            file.transferTo(tempFile);
        }catch(IOException e){
            e.printStackTrace();
            return Result.error("文件上传失败!");
        }
        return Result.success("文件上传成功", FilenameUtils.getName(tempFile.getName()));
    }

    @ApiOperation("获得文件")
    @GetMapping("/get/{name}")
    public void getImg(@ApiParam("文件名称") @PathVariable("name") String name, HttpServletResponse response){
        File tempFile = new File(getTempFilePath(name));
        if(tempFile.exists()){
            String fileName = FilenameUtils.getName(tempFile.getName());
            String contentType = new MimetypesFileTypeMap().getContentType(fileName);
            response.setContentType(contentType);
            response.addHeader("Content-Disposition",
                    "attachment;fileName=" + fileName);// 设置文件名
            try (OutputStream stream = response.getOutputStream()) {
                stream.write(Files.readAllBytes(tempFile.toPath()));
               // stream.write(FileUtils.readFileToByteArray(img));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }






}
