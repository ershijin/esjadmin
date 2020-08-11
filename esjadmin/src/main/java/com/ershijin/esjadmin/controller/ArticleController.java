package com.ershijin.esjadmin.controller;

import com.ershijin.esjadmin.dao.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private MenuMapper menuDao;

    @GetMapping("/{id}")
    public String load(@PathVariable Long id) {
        return "This is my first blog";
    }

    @PostMapping("/add")
    public void create(@AuthenticationPrincipal UserDetails user) {

    }

}