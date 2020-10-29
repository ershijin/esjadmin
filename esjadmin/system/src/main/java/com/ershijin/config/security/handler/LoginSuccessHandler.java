package com.ershijin.config.security.handler;

import com.ershijin.component.Config;
import com.ershijin.config.security.bean.LoginProperties;
import com.ershijin.model.ApiResult;
import com.ershijin.model.entity.User;
import com.ershijin.service.OnlineUserService;
import com.ershijin.service.UserService;
import com.ershijin.util.JsonUtils;
import com.ershijin.util.SpringContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录成功处理
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private UserService userService;

    public LoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String token = userService.saveLoginInfo(user, request);
        response.setHeader(Config.AUTHORIZATION_NAME, token);
        response.setContentType("application/json; charset=utf-8");
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        Writer writer = response.getWriter();
        writer.write(JsonUtils.toJsonString(ApiResult.success(result)));
        writer.close();

        LoginProperties loginProperties = SpringContextHolder.getBean(LoginProperties.class);
        if (loginProperties.isSingleLogin()) {
            System.out.println(token);
            SpringContextHolder.getBean(OnlineUserService.class).checkLoginOnUser(user.getUsername(), token);
        }
    }

}
