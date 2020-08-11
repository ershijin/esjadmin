package com.ershijin.esjadmin.config.security;

import com.ershijin.esjadmin.config.security.filter.AuthenticationFilter;
import com.ershijin.esjadmin.config.security.handler.TokenAuthenticationFailureHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

public class AuthenticationConfig<T extends AuthenticationConfig<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

    private AuthenticationFilter authFilter;

    public AuthenticationConfig() {
        this.authFilter = new AuthenticationFilter();
    }

    @Override
    public void configure(B http) throws Exception {
        authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        authFilter.setAuthenticationFailureHandler(new TokenAuthenticationFailureHandler());
        //将filter放到LogoutFilter之前
        AuthenticationFilter filter = postProcess(authFilter);
        http.addFilterBefore(filter, LogoutFilter.class);
    }

    // 设置匿名用户可访问url
    public AuthenticationConfig<T, B> permissiveRequestUrls(String ... urls){
        authFilter.setPermissiveUrl(urls);
        return this;
    }

    public AuthenticationConfig<T, B> tokenValidSuccessHandler(AuthenticationSuccessHandler successHandler){
        authFilter.setAuthenticationSuccessHandler(successHandler);
        return this;
    }

}
