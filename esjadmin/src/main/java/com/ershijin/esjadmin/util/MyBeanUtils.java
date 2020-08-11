package com.ershijin.esjadmin.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyBeanUtils {
    /**
     * 转换集合类型的Beans，并返回
     * @param source 源Beans集合
     * @param targetElementClass 目标集合元素Bean类型
     * @return
     */
    public static List convert(List source, Class targetElementClass) throws IOException {
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

}
