package com.ershijin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ershijin.converter.ArticleConverter;
import com.ershijin.dao.ArticleMapper;
import com.ershijin.model.PageResult;
import com.ershijin.model.dto.ArticleDTO;
import com.ershijin.model.entity.Article;
import com.ershijin.model.query.ArticleQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleConverter converter;

    public PageResult list(ArticleQuery query, IPage<Article> page) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(query.getCategoryId() != null, "category_id", query.getCategoryId());
        queryWrapper.and(!StringUtils.isEmpty(query.getKeyword()), i -> {
            i.like("title", query.getKeyword())
                    .or().like("summary", query.getKeyword());
        });

        queryWrapper.orderByDesc("id");

        IPage<Article> result = articleMapper.selectPage(page, queryWrapper);
        return new PageResult(result.getTotal(), converter.toDto(result.getRecords()));
    }

    public ArticleDTO get(long id) {
        Article article = articleMapper.selectById(id);
        ArticleDTO articleDTO = converter.toDto(article);
        return articleDTO;
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
