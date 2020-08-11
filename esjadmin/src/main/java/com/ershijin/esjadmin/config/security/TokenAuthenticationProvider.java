package com.ershijin.esjadmin.config.security;

import com.ershijin.esjadmin.service.AuthenticationService;
import com.ershijin.esjadmin.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.NonceExpiredException;

import java.util.Date;

/**
 * 认证的具体实现类，每个provider通过实现一个supports方法来表示自己支持那种Token的认证
 */

public class TokenAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;

    private AuthenticationService authenticationService;

    public TokenAuthenticationProvider(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    /**
     * 校验凭证
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = ((AuthenticationToken) authentication).getToken();
        // 判断token是否有效
        com.ershijin.esjadmin.model.entity.Authentication dbAuthentication = authenticationService.getAuthenticationByToken(token);
        if (dbAuthentication == null) {
            throw new BadCredentialsException("无效的token");
        }
        // 判断token是否过期
        if ((new Date()).getTime() > dbAuthentication.getExpireTime().getTime()) {
            throw new NonceExpiredException("token已过期");
        }
        // token验证通过，创建凭证
        UserDetails user = userService.getLoginInfo(dbAuthentication);
        AuthenticationToken authenticationToken = new AuthenticationToken(user, token, user.getAuthorities());
            return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(AuthenticationToken.class);
    }

}
