package com.ershijin.esjadmin.controller;

import com.ershijin.esjadmin.annotation.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @GetMapping("/{id}")
    @Log("浏览文章")
    public String load(@PathVariable Long id) {
        return "This is my first blog";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority(@config.GENERAL_PERMISSION)")
    @Log("添加文章")
//    @PreAuthorize("hasAuthority(@config.get('GENERAL_PERMISSION'))")
    public void create() {

    }

}