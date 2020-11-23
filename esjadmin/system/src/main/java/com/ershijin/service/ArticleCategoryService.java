package com.ershijin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ershijin.dao.ArticleCategoryMapper;
import com.ershijin.model.entity.ArticleCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleCategoryService {
    @Autowired
    private ArticleCategoryMapper articleCategoryMapper;

    public List<ArticleCategory> list() {
        QueryWrapper<ArticleCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort_id");
        return articleCategoryMapper.selectList(queryWrapper);
    }

    public void save(ArticleCategory articleCategory) {
        articleCategoryMapper.insert(articleCategory);
    }

    public void remove(Long id) {
        articleCategoryMapper.deleteById(id);
    }

    public void update(ArticleCategory articleCategory) {
        articleCategoryMapper.updateById(articleCategory);
    }
}
