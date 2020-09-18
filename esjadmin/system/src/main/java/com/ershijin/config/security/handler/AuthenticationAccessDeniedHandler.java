package com.ershijin.config.security.handler;

import com.ershijin.constant.ResultCode;
import com.ershijin.model.ApiResult;
import com.ershijin.util.JsonUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

@Component
public class AuthenticationAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        Writer writer = response.getWriter();
        writer.write(JsonUtils.toJsonString(ApiResult.error(ResultCode.FORBIDDEN, "权限不足")));
        writer.close();
    }
}
