package com.ershijin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ershijin.dao.AuthenticationMapper;
import com.ershijin.model.entity.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
