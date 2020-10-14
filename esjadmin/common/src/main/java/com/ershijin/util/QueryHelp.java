package com.ershijin.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import com.ershijin.annotation.Query;
import org.apache.commons.lang3.ObjectUtils;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class QueryHelp {
    public static <T> QueryWrapper<T> buildQueryWrapper(Object query) {
        QueryWrapper<T> queryWrapper = Wrappers.query();
        try {
            List<Field> fields = getAllFields(query.getClass(), new ArrayList<>());
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                // 设置对象的访问权限，保证对private的属性的访
                field.setAccessible(true);
                Query q = field.getAnnotation(Query.class);
                if (q != null) {
                    String tableField = q.tableField();
                    String blurry = q.blurry();
                    String column = isBlank(tableField) ? field.getName() : tableField;
                    Object val = field.get(query);
                    if (ObjectUtils.isEmpty(val)) {
                        continue;
                    }
                    // 模糊多字段
                    if (ObjectUtils.isNotEmpty(blurry)) {
                        String[] blurrys = blurry.split(",");
                        queryWrapper.and(wrapper -> {
                            for (String tableColumn : blurrys) {
                                try {
                                    wrapper.or().like(tableColumn, field.get(query).toString());
                                } catch (IllegalAccessException e) {
                                    log.error(e.getMessage(), e);
                                }
                            }
                        });

                        continue;
                    }
                    switch (q.type()) {
                        case EQ:
                            queryWrapper.eq(column, val);
                            break;
                        case NE:
                            queryWrapper.ne(column, val);
                            break;
                        case GT:
                            queryWrapper.gt(column, val);
                            break;
                        case GE:
                            queryWrapper.ge(column, val);
                            break;
                        case LT:
                            queryWrapper.lt(column, val);
                            break;
                        case LE:
                            queryWrapper.le(column, val);
                            break;
                        case BETWEEN:
                            List<Object> between = new ArrayList<>((List<Object>)val);
                            queryWrapper.between(column, between.get(0), between.get(1));
                            break;
                        case LIKE:
                            queryWrapper.like(column, val);
                            break;
                        case LIKE_LEFT:
                            queryWrapper.likeLeft(column, val);
                            break;
                        case LIKE_RIGHT:
                            queryWrapper.likeRight(column, val);
                            break;
                        case IS_NULL:
                            queryWrapper.isNull(column);
                            break;
                        case IS_NOT_NULL:
                            queryWrapper.isNotNull(column);
                            break;
                        case IN:
                            if (ObjectUtils.isNotEmpty(val)) {
                                queryWrapper.in(column, val);
                            }
                            break;
                        default: break;
                    }
                }
                field.setAccessible(accessible);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return queryWrapper;
    }


    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static List<Field> getAllFields(Class clazz, List<Field> fields) {
        if (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getAllFields(clazz.getSuperclass(), fields);
        }
        return fields;
    }
}
