package com.ershijin.esjadmin.controller;

import com.ershijin.esjadmin.model.entity.ArticleCategory;
import com.ershijin.esjadmin.service.ArticleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articleCategories")
public class ArticleCategoryController {
    @Autowired
    private ArticleCategoryService articleCategoryService;

    @GetMapping
    private List<ArticleCategory> list() {
        return articleCategoryService.list();
    }

    @PostMapping
    private void save(@RequestBody ArticleCategory articleCategory) {
        articleCategoryService.save(articleCategory);
    }

    @DeleteMapping("/{id}")
    private void delete(@PathVariable Long id) {
        articleCategoryService.remove(id);
    }

    @PutMapping("/{id}")
    private void update(@RequestBody ArticleCategory articleCategory) {
        articleCategoryService.update(articleCategory);
    }
}
