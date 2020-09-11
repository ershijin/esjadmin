package com.ershijin.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ershijin.dao.FileMapper;
import com.ershijin.exception.ApiException;
import com.ershijin.model.entity.File;
import com.ershijin.util.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {

    @Autowired
    private FileMapper fileMapper;

    public String save(String directory, MultipartFile multipartFile, String name) {
        try {
            if (multipartFile.getSize() > (2 * 1024 * 1024)) {
                throw new ApiException("文件超出规定大小");
            }
            String hashCode = FileUtils.getMd5(multipartFile.getBytes());
            File dbFile = fileMapper.selectOne(new QueryWrapper<File>().eq("hash_code", hashCode));
            if (dbFile != null) {
                return dbFile.getPath();
            }
            name = StringUtils.isBlank(name) ? FileUtils.getFileNameNoEx(multipartFile.getOriginalFilename()) : name;

            String postfix = FileUtils.getExtension(multipartFile.getOriginalFilename());
            String type = FileUtils.getFileType(postfix);
            String uploadPath = ResourceUtils.getURL("classpath:").getPath() + "upload/" + directory + "/";
            String savedFile = FileUtils.upload(multipartFile, uploadPath);
            if(ObjectUtil.isNull(savedFile)){
                throw new ApiException("上传失败");
            }
            try {
                File file = new File();
                file.setTitle(name);
                file.setPostfix(postfix);
                file.setType(type);
                file.setPath(directory + "/" + savedFile);
                file.setSize(multipartFile.getSize());
                file.setHashCode(hashCode);

                fileMapper.insert(file);
            }catch (Exception e){
                FileUtils.del(uploadPath + savedFile);
                throw e;
            }

            return directory + '/' + savedFile;
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }
    }
}
