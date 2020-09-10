package com.ershijin.esjadmin.controller;

import com.ershijin.esjadmin.component.Config;
import com.ershijin.esjadmin.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/images")
    public Map<String, String> images(MultipartFile file) {
        String fileName = fileService.save("images", file, null);
        return result(fileName);
    }

    private Map<String, String> result(String fileName) {
        Map<String, String> result = new HashMap<>();
        result.put("location", Config.get("upload_base_url") + fileName);
        return result;
    }
}
