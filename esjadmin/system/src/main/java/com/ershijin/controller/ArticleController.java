package com.ershijin.controller;

import com.ershijin.model.Page;
import com.ershijin.annotation.Log;
import com.ershijin.model.PageResult;
import com.ershijin.model.dto.ArticleDTO;
import com.ershijin.model.entity.Article;
import com.ershijin.model.query.ArticleQuery;
import com.ershijin.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    @Log("文章列表")
    @PreAuthorize("hasAuthority('articles:list')")
    public PageResult list(ArticleQuery query, Page page) {
        return articleService.list(query, page);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('articles:list')")
    public ArticleDTO get(@PathVariable Long id) {
        return articleService.get(id);
    }

    @PostMapping
    @Log("添加文章")
    @PreAuthorize("hasAuthority('articles:save')")
    public void save(@RequestBody Article article) {
        articleService.save(article);
    }

    @PutMapping("/{id}")
    @Log("更新文章")
    @PreAuthorize("hasAuthority('articles:update')")
    public void update(@PathVariable long id, @RequestBody Article article) {
        articleService.update(article);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('articles:remove')")
    @Log("删除文章")
    public void remove(@PathVariable Long id) {
        articleService.remove(id);
    }
}