package com.ershijin.esjadmin.config.security;

import com.ershijin.esjadmin.config.security.filter.UsernamePasswordAuthenticationFilter;
import com.ershijin.esjadmin.config.security.handler.LoginFailureHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;

public class LoginConfig<T extends LoginConfig<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

    private UsernamePasswordAuthenticationFilter authFilter;

    public LoginConfig() {
        this.authFilter = new UsernamePasswordAuthenticationFilter();
    }

    @Override
    public void configure(B http) throws Exception {
        // 设置Filter使用的AuthenticationManager，这里取公共的
        authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        // 设置失败的Handler
        authFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
        // 不将认证后的context放入session
        authFilter.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());

        UsernamePasswordAuthenticationFilter filter = postProcess(authFilter);
        // 指定Filter的位置
        http.addFilterAfter(filter, LogoutFilter.class);
    }

    public LoginConfig<T,B> loginSuccessHandler(AuthenticationSuccessHandler authSuccessHandler){
        authFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        return this;
    }

}
