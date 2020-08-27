package com.ershijin.esjadmin.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FileUtils {
    /**
     * 获取文件扩展名
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 上传文件，返回文件相对路径
     * @param file
     * @param uploadPath
     * @return
     * @throws IOException
     */
    public static String upload(MultipartFile file, String uploadPath) throws IOException {
        String fileName = file.getOriginalFilename();
        int rannum = (int) (new Random().nextDouble() * (99999 - 10000 + 1)) + 10000; // 获取随机数
        String nowTimeStr = new SimpleDateFormat(
                "HHmmss").format(new Date()); // 当前时间
        String destFileName = nowTimeStr + rannum + getExtension(fileName);

        String subDir = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
        String filePath = uploadPath + subDir;
        File path = new File(filePath);
        File dest = new File(filePath + destFileName);
        synchronized (path) {
            if (!path.exists()) {
                if (!path.mkdirs()) {
                    throw new IOException("目录创建失败:" + path.getParent());
                }
            }
        }
        file.transferTo(dest);
        return subDir + destFileName;
    }
    public static boolean del(String pathName) {
        return new File(pathName).delete();
    }
}
