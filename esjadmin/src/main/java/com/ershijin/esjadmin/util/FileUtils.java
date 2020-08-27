package com.ershijin.esjadmin.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FileUtils {
    /**
     * 系统临时目录
     * <br>
     * windows 包含路径分割符，但Linux 不包含,
     * 在windows \\==\ 前提下，
     * 为安全起见 同意拼装 路径分割符，
     * <pre>
     *       java.io.tmpdir
     *       windows : C:\Users/xxx\AppData\Local\Temp\
     *       linux: /temp
     * </pre>
     */
    public static final String SYS_TEM_DIR = System.getProperty("java.io.tmpdir") + File.separator;

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

    /**
     * inputStream 转 File
     */
    static File inputStreamToFile(InputStream ins, String name) throws Exception {
        File file = new File(SYS_TEM_DIR + name);
        if (file.exists()) {
            return file;
        }
        OutputStream os = new FileOutputStream(file);
        int bytesRead;
        int len = 8192;
        byte[] buffer = new byte[len];
        while ((bytesRead = ins.read(buffer, 0, len)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
        return file;
    }

}
