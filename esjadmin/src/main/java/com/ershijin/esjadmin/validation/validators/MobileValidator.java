package com.ershijin.esjadmin.validation.validators;

import com.ershijin.esjadmin.util.ValidatorUtils;
import com.ershijin.esjadmin.validation.constraints.Mobile;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MobileValidator implements ConstraintValidator<Mobile, String> {
    private boolean required = false;

    @Override
    public void initialize(Mobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) {
            // 调用工具类对手机号进行校验
            return ValidatorUtils.isMobile(value);
        } else {
            if (StringUtils.isEmpty(value)) {
                return true;
            } else {
                return ValidatorUtils.isMobile(value);
            }
        }
    }
}
