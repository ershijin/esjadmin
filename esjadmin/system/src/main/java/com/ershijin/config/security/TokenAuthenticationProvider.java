package com.ershijin.config.security;

import com.ershijin.config.security.bean.SecurityProperties;
import com.ershijin.model.dto.OnlineUserDTO;
import com.ershijin.service.UserService;
import com.ershijin.util.RedisUtils;
import com.ershijin.util.SpringContextHolder;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.NonceExpiredException;

import java.time.LocalDateTime;

/**
 * 认证的具体实现类，每个provider通过实现一个supports方法来表示自己支持那种Token的认证
 */

public class TokenAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;

    public TokenAuthenticationProvider(UserService userService) {
        this.userService = userService;
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
//        com.ershijin.model.entity.Authentication dbAuthentication = authenticationService.getAuthenticationByToken(token);

        SecurityProperties properties = SpringContextHolder.getBean(SecurityProperties.class);
        OnlineUserDTO onlineUserDTO = (OnlineUserDTO) RedisUtils.get(properties.getOnlineKey() + token);

        if (ObjectUtils.isEmpty(onlineUserDTO)) {
            throw new BadCredentialsException("无效的token或token已过期");
        }

        // token验证通过，创建凭证
        UserDetails user = userService.getLoginInfo(onlineUserDTO);
        AuthenticationToken authenticationToken = new AuthenticationToken(user, token, user.getAuthorities());
            return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(AuthenticationToken.class);
    }

}
