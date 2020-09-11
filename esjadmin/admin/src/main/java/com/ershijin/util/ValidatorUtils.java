package com.ershijin.util;

import com.ershijin.dao.CommonMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtils {
    /**
     * 是否是手机号
     *
     * @param value
     * @return
     */
    public static boolean isMobile(String value) {
        Pattern mobilePattern = Pattern.compile("1[3|4|5|7|8][0-9]\\d{4,8}");
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        Matcher m = mobilePattern.matcher(value);
        return m.matches();
    }

    /**
     * 是否数据库中重复值
     *
     * @param value  值
     * @param table  数据表
     * @param column 表字段
     * @return
     */
    public static boolean isNotDuplicate(String value, String table, String column) {
        CommonMapper commonDao = SpringContextHolder.getBean(CommonMapper.class);
        int count = commonDao.count(value, table, column);
        if (count <= 0) {
            return true;
        }
        return false;
    }
}
