package com.ershijin.esjadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ershijin.esjadmin.dao.ArticleMapper;
import com.ershijin.esjadmin.model.PageResult;
import com.ershijin.esjadmin.model.entity.Article;
import com.ershijin.esjadmin.model.query.ArticleQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    public PageResult list(ArticleQuery query, Page<Article> page) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(query.getCategoryId() != null, "category_id", query.getCategoryId());
        queryWrapper.and(!StringUtils.isEmpty(query.getKeyword()), i -> {
            i.like("title", query.getKeyword())
                    .or().like("summary", query.getKeyword());
        });

        queryWrapper.orderByDesc("id");

        IPage<Article> result = articleMapper.selectPage(page, queryWrapper);
        return new PageResult(result.getTotal(), result.getRecords());
    }

    public Article get(long id) {
        return articleMapper.selectById(id);
    }

    public void save(Article article) {
        articleMapper.insert(article);
    }

    public void update(Article article) {
        articleMapper.updateById(article);
    }

    public void remove(Long id) {
        articleMapper.deleteById(id);
    }
}
