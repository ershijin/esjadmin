package com.ershijin.component;

import com.ershijin.service.ConfigService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * 全局配置
 */
@Component
public class Config implements InitializingBean, ServletContextAware {
    /**
     * http头信息中Authorization(凭证)的名字
     */
    public static final String AUTHORIZATION_NAME = "token";

    /**
     * 登录用户拥有的权限
     */
    public static final String GENERAL_PERMISSION = "general";

    /**
     * 系统配置属性，从数据库中读取的
     */
    public static Map<String, String> configMap;

    @Autowired
    private ConfigService configService;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        configMap = configService.getConfigMap();
    }

    /**
     * 获取系统配置属性
     * @param code 配置属性key
     * @return
     */
    public static String get(String code) {
        return configMap.get(code);
    }

    /**
     * 全部配置属性
     * @return
     */
    public static Map<String, String> get() {
        return configMap;
    }
}
