package com.ershijin.esjadmin.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Map toMap(String json) {
        try {
            return objectMapper.readValue(json, LinkedHashMap.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json转List 使用示例:
     *  String json = "[1, 2, 4, 5]";
     *  List result = JsonUtil.toList(json);
     * @param json
     * @return
     */
    public static List toList(String json) {
        try {
            return objectMapper.readValue(json, LinkedList.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json转List 使用示例:
     * json = "[{"id": 1,"username": "yidasanqian"},{"id": 2,"username": "yidasanqian2"}]"
     *  TypeReference<List<User>> typeReference = new TypeReference<List<User>>() {};
     *  List<User> result = JsonUtil.toList(json, typeReference.getType());
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(String json, Type type) {
        TypeReference<T> typeReference = new TypeReference<T>() {
            @Override
            public Type getType() {
                return type;
            }
        };
        try {
            return (List<T>) objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对象转Json字符串
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJsonWithDateFormat(Object object, String dateFormatPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatPattern);
        try {
            return objectMapper.writer(sdf).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toPojo(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public static Map convertToMap(Object fromValue) {
        return objectMapper.convertValue(fromValue, LinkedHashMap.class);
    }

    public static <T> T convertFromMap(Map fromMap, Class<T> toValueType) {
        return objectMapper.convertValue(fromMap, toValueType);
    }
}
