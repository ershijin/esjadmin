package com.ershijin.esjadmin.validation.constraints;

import com.ershijin.esjadmin.validation.validators.NotDuplicateValidator;

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
@Constraint(validatedBy = {NotDuplicateValidator.class}) // 指定校验器
public @interface NotDuplicate {
    String table(); // 目标数据表
    String column(); // 目标字段
    String message() default "字段值不能重复";

    // 下面两行是自定义注解需要添加的
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
