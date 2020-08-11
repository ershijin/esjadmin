package com.ershijin.esjadmin.config.security.handler;

import com.ershijin.esjadmin.util.JsonUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json; charset=utf-8");
        Map<String, Object> result = new HashMap<>();
        result.put("message", "权限不足");
        Writer writer = response.getWriter();
//        new ObjectMapper().writeValue(writer, result);
        writer.write(JsonUtils.toJsonString(result));
        writer.close();
    }
}
