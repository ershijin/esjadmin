package com.ershijin.esjadmin.controller;

import com.ershijin.esjadmin.annotation.Log;
import com.ershijin.esjadmin.model.PageResult;
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
    PageResult list(int page, int pageSize, String startTime, String endTime) {
        return logService.list("INFO", page, pageSize, startTime, endTime);
    }
    @GetMapping("/error")
    @Log("异常日志列表")
    PageResult errorList(int page, int pageSize, String startTime, String endTime) {
        return logService.list("ERROR", page, pageSize, startTime, endTime);
    }
    @GetMapping("/error/{id}")
    @PreAuthorize("hasAuthority(@config.GENERAL_PERMISSION)")
    com.ershijin.esjadmin.model.entity.Log getError(@PathVariable long id) {
        return logService.get(id);
    }

}
