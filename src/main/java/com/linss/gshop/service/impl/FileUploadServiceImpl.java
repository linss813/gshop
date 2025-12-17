package com.linss.gshop.service.impl;

import com.linss.gshop.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${image.upload.dir}")  // 从配置读取路径
    private String UPLOAD_DIR;

    @Override
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            // 创建目录（如果不存在）
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.write(path, file.getBytes());
            return fileName; // 仅返回文件名，URL由Controller拼接
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
