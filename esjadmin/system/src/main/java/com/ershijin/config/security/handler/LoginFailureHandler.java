package com.ershijin.config.security.handler;

import com.ershijin.constant.ResultCode;
import com.ershijin.model.ApiResult;
import com.ershijin.util.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * 登录失败处理
 */
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        Writer writer = response.getWriter();
        writer.write(JsonUtils.toJsonString(ApiResult.error(ResultCode.CLIENT_ERROR, "用户名或密码错误")));
        writer.close();
    }

}
