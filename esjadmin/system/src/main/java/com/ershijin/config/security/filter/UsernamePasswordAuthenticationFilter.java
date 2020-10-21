package com.ershijin.config.security.filter;

import com.ershijin.constant.ResultCode;
import com.ershijin.model.ApiResult;
import com.ershijin.util.JsonUtils;
import com.ershijin.util.RedisUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * 登录认证类，从json body中取出username，password交给AuthenticationManager
 */
public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public UsernamePasswordAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(getAuthenticationManager(), "AuthenticationManager must be specified");
        Assert.notNull(getSuccessHandler(), "AuthenticationSuccessHandler must be specified");
        Assert.notNull(getFailureHandler(), "AuthenticationFailureHandler must be specified");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String body = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
        if (!checkAuthCode(response, body)) {
            return null;
        }
        String username = null, password = null;
        if(StringUtils.isNotBlank(body)) {
            JsonNode jsonNode = new ObjectMapper().readTree(body);
            username = jsonNode.get("username").asText();
            password = jsonNode.get("password").asText();
        }

        if (username == null)
            username = "";
        if (password == null)
            password = "";
        username = username.trim();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);
        return this.getAuthenticationManager()
                .authenticate(authRequest);
    }

    /**
     * 校验验证码
     * @param response
     * @return
     */
    private boolean checkAuthCode(HttpServletResponse response, String body) throws AuthenticationException, IOException {
        String uuid = null, code = null;
        if(StringUtils.isNotBlank(body)) {
            JsonNode jsonNode = new ObjectMapper().readTree(body);

            uuid = jsonNode.get("uuid").asText();
            code = jsonNode.get("code").asText();

        }
        if (StringUtils.isEmpty(code)) {
            response.setContentType("application/json; charset=utf-8");
            Writer writer = response.getWriter();
            writer.write(JsonUtils.toJsonString(ApiResult.error(ResultCode.CLIENT_ERROR, "请输入验证码")));
            writer.close();
            return false;
        }
        Object authCode = RedisUtils.get(uuid);
        if (ObjectUtils.isEmpty(authCode)) {
            response.setContentType("application/json; charset=utf-8");
            Writer writer = response.getWriter();
            writer.write(JsonUtils.toJsonString(ApiResult.error(ResultCode.CLIENT_ERROR, "验证码已失效")));
            writer.close();
            return false;
        }
        if (!code.equals(authCode.toString())) {
            response.setContentType("application/json; charset=utf-8");
            Writer writer = response.getWriter();
            writer.write(JsonUtils.toJsonString(ApiResult.error(ResultCode.CLIENT_ERROR, "请输入正确的验证码")));
            writer.close();
            return false;
        }

        RedisUtils.del(uuid);
        return true;
    }

}
