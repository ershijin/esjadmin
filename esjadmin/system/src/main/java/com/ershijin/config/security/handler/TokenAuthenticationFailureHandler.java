package com.ershijin.config.security.handler;

import com.ershijin.constant.ResultCode;
import com.ershijin.model.ApiResult;
import com.ershijin.util.JsonUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * token验证失败处理
 */
public class TokenAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        Writer writer = response.getWriter();
        writer.write(JsonUtils.toJsonString(ApiResult.error(ResultCode.UNAUTHORIZED, "token验证失败")));
        writer.close();
    }

}
