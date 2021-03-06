package com.ershijin.validation.validators;

import com.ershijin.util.ValidatorUtils;
import com.ershijin.validation.constraints.NotDuplicate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotDuplicateValidator implements ConstraintValidator<NotDuplicate, String> {
    private String table;
    private String column;

    @Override
    public void initialize(NotDuplicate constraintAnnotation) {
        table = constraintAnnotation.table();
        column = constraintAnnotation.column();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return ValidatorUtils.isNotDuplicate(value, table, column);
    }
}
