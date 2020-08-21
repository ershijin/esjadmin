package com.ershijin.esjadmin.config.security.handler;

import com.ershijin.esjadmin.component.Config;
import com.ershijin.esjadmin.model.ApiResult;
import com.ershijin.esjadmin.model.entity.User;
import com.ershijin.esjadmin.service.UserService;
import com.ershijin.esjadmin.util.JsonUtils;
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
        String token = userService.saveLoginInfo((User) authentication.getPrincipal());
        response.setHeader(Config.AUTHORIZATION_NAME, token);
        response.setContentType("application/json; charset=utf-8");
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        Writer writer = response.getWriter();
        writer.write(JsonUtils.toJsonString(ApiResult.success(result)));
        writer.close();
    }

}
