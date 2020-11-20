package com.ershijin.controller;

import com.ershijin.model.Page;
import com.ershijin.model.PageResult;
import com.ershijin.service.OnlineUserService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/auth/online")
public class OnlineController {

    @Autowired
    private OnlineUserService onlineUserService;

    @GetMapping
    @PreAuthorize("hasAuthority('onlineuser:list')")
    public PageResult list(@ApiParam("模糊搜索关键字") String keyword, Page page) {
        return onlineUserService.list(keyword, page);
    }

    /**
     * 踢出用户
     * @param keys /
     */
    @DeleteMapping
    @PreAuthorize("hasAuthority('onlineuser:kickout')")
    public void kickOut(@RequestBody Set<String> keys){
        keys.forEach(key -> onlineUserService.kickOut(key));
    }
}
