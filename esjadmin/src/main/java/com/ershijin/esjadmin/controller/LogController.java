package com.ershijin.esjadmin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ershijin.esjadmin.annotation.Log;
import com.ershijin.esjadmin.model.PageResult;
import com.ershijin.esjadmin.model.query.LogQuery;
import com.ershijin.esjadmin.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/logs")
@RestController
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping("")
    @Log("操作日志列表")
    @PreAuthorize("hasAuthority('logs:list')")
    PageResult list(LogQuery logQuery, Page page) {
        return logService.list("INFO", logQuery, page);
    }

    @GetMapping("/error")
    @Log("异常日志列表")
    @PreAuthorize("hasAuthority('logs:error')")
    PageResult errorList(LogQuery logQuery, Page page) {
        return logService.list("ERROR", logQuery, page);
    }

    @GetMapping("/error/{id}")
    @PreAuthorize("hasAuthority(@config.GENERAL_PERMISSION)")
    com.ershijin.esjadmin.model.entity.Log getError(@PathVariable long id) {
        return logService.get(id);
    }

}
