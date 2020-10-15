package com.ershijin.util;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 访问token生成工具
 */
public class TokenUtils {
    public static String generateTokenCode(){
//        String value = System.currentTimeMillis()+new Random().nextInt()+"";
        String value = UUID.randomUUID().toString();

        //获取数据指纹，指纹是唯一的
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] b = md.digest(value.getBytes());//产生数据的指纹
            //Base64编码
            BASE64Encoder be = new BASE64Encoder();
            return be.encode(b);//制定一个编码
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }
}
