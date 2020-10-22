package com.ershijin.config.security;

import com.ershijin.config.security.handler.AuthenticationAccessDeniedHandler;
import com.ershijin.config.security.handler.LoginSuccessHandler;
import com.ershijin.config.security.handler.TokenAuthenticationSuccessHandler;
import com.ershijin.config.security.handler.TokenLogoutHandler;
import com.ershijin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

/**
 * Spring Security 配置类
 */

@EnableWebSecurity
// 开启Controller层的访问方法权限，与注解@PreAuthorize("hasAuthority('users:list')")配合
// 如果使用hasRole('ROLE_admin') 判断权限，权限关键字必须带前缀 ROLE_
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;
//    @Autowired
//    UrlAccessDecisionManager urlAccessDecisionManager;
    @Autowired
    AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
        // 配置 provider
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());

        auth.authenticationProvider(daoAuthenticationProvider)
                .authenticationProvider(new TokenAuthenticationProvider(userService))
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/index.html", "/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests();

        http.formLogin().disable()
                .csrf().disable().cors();

        // 添加登录filter
        http.apply(new LoginConfig<>())
                .loginSuccessHandler(new LoginSuccessHandler(userService));

        // 添加token的filter
        http.apply(new AuthenticationConfig<>())
                .tokenValidSuccessHandler(new TokenAuthenticationSuccessHandler(userService))
                .permissiveRequestUrls("/logout");


        http.logout().permitAll()
                .addLogoutHandler(new TokenLogoutHandler(userService)) // logout时清除token
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()); // logout成功后返回200()

        // 添加自定义未授权处理器
        http.exceptionHandling()
                .accessDeniedHandler(authenticationAccessDeniedHandler);

        // 禁用 session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


}
