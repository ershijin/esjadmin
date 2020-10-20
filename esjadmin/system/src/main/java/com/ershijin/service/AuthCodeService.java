package com.ershijin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ershijin.controller.AuthController;
import com.ershijin.converter.ArticleConverter;
import com.ershijin.dao.ArticleMapper;
import com.ershijin.dao.AuthCodeMapper;
import com.ershijin.model.PageResult;
import com.ershijin.model.dto.ArticleDTO;
import com.ershijin.model.entity.Article;
import com.ershijin.model.entity.AuthCode;
import com.ershijin.model.query.ArticleQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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

}
