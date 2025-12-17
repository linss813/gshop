package com.linss.gshop.controller;

import com.linss.gshop.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @Value("${image.base.url}")  // 使用配置的base URL
    private String BASE_URL;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileUploadService.uploadFile(file);
        if (fileName != null) {
            return ResponseEntity.ok("{\"url\":\"" + BASE_URL + fileName + "\"}");
        }
        return ResponseEntity.status(500).body("上传失败");
    }
}
