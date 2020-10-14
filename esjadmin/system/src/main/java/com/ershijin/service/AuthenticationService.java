package com.ershijin.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ershijin.dao.AuthenticationMapper;
import com.ershijin.model.entity.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationMapper authenticationMapper;

    public Authentication insertAuthentication(Authentication token) {
        authenticationMapper.insert(token);
        return token;
    }

    public void deleteByToken(String token) {
        authenticationMapper.delete(new QueryWrapper<Authentication>().eq("token", token));
    }


    public Authentication getAuthenticationByToken(String token) {
        return authenticationMapper.selectOne(new QueryWrapper<Authentication>().eq("token", token));
    }

    /**
     * 删除过期凭证
     */
    public void deleteExpired() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = dateTimeFormatter.format(LocalDateTime.now());
        authenticationMapper.delete(Wrappers.
                <Authentication>lambdaQuery()
                .lt(Authentication::getExpireTime, now));
    }
}
