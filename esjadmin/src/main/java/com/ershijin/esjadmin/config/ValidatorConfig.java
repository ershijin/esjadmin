package com.ershijin.esjadmin.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 校验模式配置，默认会校验完所有属性，然后将错误一起返回，
 * 这里配置为一个校验失败了，立即返回，其它的就不必校验了
 */

@Configuration
public class ValidatorConfig {

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory =
                Validation.byProvider(HibernateValidator.class).configure().failFast(false).buildValidatorFactory();
        return validatorFactory.getValidator();
    }
}
