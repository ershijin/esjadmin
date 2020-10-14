package com.ershijin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    // 表中的列名
    String tableField() default "";

    // 查询方式
    Type type() default Type.EQ;

    /**
     * 多字段模糊搜索，仅支持String类型字段，多个用逗号隔开, 如@Query(blurry = "email,username")
     */
    String blurry() default "";

    enum Type {

        EQ, // 相等
        NE, // 不等于
        GT, // 大于
        GE, // 大于等于
        LT, // 小于
        LE, // 小于等于
        BETWEEN, // between

        LIKE, // 中模糊查询
        LIKE_LEFT, // 左模糊查询
        LIKE_RIGHT, // 右模糊查询
        IS_NULL, // 为空
        IS_NOT_NULL, // 不为空
        IN, // 包含


    }

}

