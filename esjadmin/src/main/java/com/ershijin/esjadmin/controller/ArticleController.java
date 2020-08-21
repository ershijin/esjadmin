package com.ershijin.esjadmin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @GetMapping("/{id}")
    public String load(@PathVariable Long id) {
        return "This is my first blog";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority(@config.GENERAL_PERMISSION)")
    public void create() {

    }

}