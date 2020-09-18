package com.ershijin.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class MyBeanUtils {
    /**
     * 转换集合类型的Beans，并返回
     * @param source 源Beans集合
     * @param targetElementClass 目标集合元素Bean类型
     * @return
     */
    @SneakyThrows
    public static List convert(List source, Class targetElementClass) {
        if (CollectionUtils.isEmpty(source)) {
            return new ArrayList<>();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String jsonStr = objectMapper.writeValueAsString(source);
        return  (List) objectMapper.readValue(jsonStr,
                objectMapper.getTypeFactory().constructParametricType(List.class,
                        targetElementClass));
    }

    @SneakyThrows
    public static Object convert(Object source, Class targetClass) {
        Object o = targetClass.newInstance();
        BeanUtils.copyProperties(source, o);
        return o;
    }

    /**
     * 复制source bean属性到target bean
     * @param source 原始bean
     * @param target 目标bean
     */
    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

}
