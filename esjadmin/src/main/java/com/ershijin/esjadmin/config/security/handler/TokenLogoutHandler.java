package com.ershijin.esjadmin.config.security.handler;

import com.ershijin.esjadmin.config.security.AuthenticationToken;
import com.ershijin.esjadmin.model.ApiResult;
import com.ershijin.esjadmin.service.UserService;
import com.ershijin.esjadmin.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * 退出登录handler
 */
public class TokenLogoutHandler implements LogoutHandler {
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(TokenLogoutHandler.class);

    public TokenLogoutHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logger.debug("------ logout -------");
        clearToken(authentication);

        response.setContentType("application/json; charset=utf-8");
        try {
            Writer writer = response.getWriter();
            writer.write(JsonUtils.toJsonString(ApiResult.success()));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出登录，清楚数据库中缓存的登录信息
     * @param authentication
     */
    protected void clearToken(Authentication authentication) {
        if(authentication == null)
            return;
        AuthenticationToken authenticationToken = (AuthenticationToken) authentication;
        String token = authenticationToken.getToken();
        userService.deleteLoginInfo(token);
    }

}
