package com.ershijin.config.security.handler;

import com.ershijin.config.security.AuthenticationToken;
import com.ershijin.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * token 验证成功处理
 */

public class TokenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationSuccessHandler.class);

    private static final int tokenRefreshInterval = 30;  //刷新间隔5分钟

    private UserService userService;

    public TokenAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    /**
     * @todo 超时刷新token, 或者延期token到期时间，刷新缓存的权限？
     */
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
//        DecodedJWT jwt = ((JwtAuthenticationToken) authentication).getToken();
//        boolean shouldRefresh = shouldTokenRefresh(jwt.getIssuedAt());
//        if(shouldRefresh) {
//            String newToken = userService.saveUserLoginInfo((UserDetails)authentication.getPrincipal());
//            response.setHeader("Authorization", newToken);
//            System.out.println("-------- 刷新 token -------");
//            System.out.println("新token= " + newToken);
//        }
        logger.debug("------ token 验证成功 -----------");
        logger.debug(((AuthenticationToken) authentication).getToken());
    }

    protected boolean shouldTokenRefresh(Date issueAt){
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
    }

}
