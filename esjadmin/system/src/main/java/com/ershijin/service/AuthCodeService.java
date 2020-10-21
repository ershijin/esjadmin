package com.ershijin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ershijin.dao.AuthCodeMapper;
import com.ershijin.model.entity.AuthCode;
import com.ershijin.model.entity.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AuthCodeService {
    @Autowired
    private AuthCodeMapper authCodeMapper;

    public void save(AuthCode authCode) {
        authCodeMapper.insert(authCode);
    }

    public AuthCode get(String uuid) {
        return authCodeMapper.selectById(uuid);
    }

    public void remove(String uuid) {
        authCodeMapper.deleteById(uuid);
    }

    /**
     * 删除过期凭证
     */
    public void deleteExpired() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = dateTimeFormatter.format(LocalDateTime.now());
        authCodeMapper.delete(Wrappers.
                <AuthCode>lambdaQuery()
                .lt(AuthCode::getExpireTime, now));
    }

}
