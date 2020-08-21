package com.ershijin.esjadmin.component;

import org.springframework.stereotype.Component;

/**
 * 全局配置
 */
@Component
public class Config {
    /**
     * http头信息中Authorization(凭证)的名字
     */
    public static final String AUTHORIZATION_NAME = "token";

    /**
     * 登录用户拥有的权限
     */
    public static final String GENERAL_PERMISSION = "general";
}
