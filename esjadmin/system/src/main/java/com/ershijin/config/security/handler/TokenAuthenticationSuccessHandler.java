package com.ershijin.config.security.handler;

import com.ershijin.config.security.AuthenticationToken;
import com.ershijin.config.security.bean.SecurityProperties;
import com.ershijin.service.UserService;
import com.ershijin.util.RedisUtils;
import com.ershijin.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token 验证成功处理
 */

public class TokenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationSuccessHandler.class);

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
//            String newToken = userService.saveUserLoginInfo((UserDetails) authentication.getPrincipal());
//            response.setHeader("Authorization", newToken);
//            System.out.println("-------- 刷新 token -------");
//            System.out.println("新token= " + newToken);
//        }
        logger.debug("------ token 验证成功: {}", ((AuthenticationToken) authentication).getToken());
        String token = ((AuthenticationToken) authentication).getToken();
        checkRenewToken(token);
    }

    /**
     * 检查token到期时间，如果续期检查的范围内，则续期
     * @param token
     */
    protected void checkRenewToken(String token){
        SecurityProperties properties = SpringContextHolder.getBean(SecurityProperties.class);
        // 判断是否续期token,计算token的过期时间
        long expireTime = RedisUtils.getExpire(properties.getOnlineKey() + token);
        // 如果在续期检查的范围内，则续期
        if (expireTime <= properties.getDetect()) {
            long renew = expireTime + properties.getRenew();
            RedisUtils.expire(properties.getOnlineKey() + token, renew);
        }
    }

}
