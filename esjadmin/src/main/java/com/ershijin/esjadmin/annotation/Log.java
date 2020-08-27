package com.ershijin.esjadmin.annotation;

import com.ershijin.esjadmin.constant.LogType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    String value() default "";

    /**
     * 是否启用
     *
     * @return
     */
    boolean enable() default true;

    String type() default LogType.SELECT;
}
