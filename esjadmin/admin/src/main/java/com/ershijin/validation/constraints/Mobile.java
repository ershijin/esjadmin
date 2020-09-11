package com.ershijin.validation.constraints;

import com.ershijin.validation.validators.MobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {MobileValidator.class}) // 指定校验器
public @interface Mobile {
    // 是否是必须子段，默认为true
    boolean required() default true;

    String message() default "手机号码格式错误";

    // 下面两行是自定义注解需要添加的
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
