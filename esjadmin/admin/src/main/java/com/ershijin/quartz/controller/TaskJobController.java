package com.ershijin.quartz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ershijin.annotation.Log;
import com.ershijin.exception.ApiException;
import com.ershijin.model.PageResult;
import com.ershijin.quartz.model.JobQuery;
import com.ershijin.quartz.model.TaskJob;
import com.ershijin.quartz.service.TaskJobService;
import com.ershijin.validation.groups.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/taskJobs")
public class TaskJobController {

    private final TaskJobService taskJobService;

    @Log("查询定时任务")
    @GetMapping
    @PreAuthorize("hasAuthority('taskJobs:list')")
    public PageResult list(JobQuery jobQuery, Page page){
        return taskJobService.list(jobQuery,page);
    }

    @Log("新增定时任务")
    @PostMapping
    @PreAuthorize("hasAuthority('taskJobs:save')")
    public void create(@Validated @RequestBody TaskJob taskJob){
        if (taskJob.getId() != null) {
            throw new ApiException("新的定时任务不能有 ID");
        }
        taskJobService.save(taskJob);
    }

    @Log("修改定时任务")
    @PutMapping
    @PreAuthorize("hasAuthority('taskJobs:update')")
    public void update(@Validated(Update.class) @RequestBody TaskJob taskJob){
        taskJobService.update(taskJob);
    }

    @Log("启用定时任务")
    @PostMapping("/{id}/enable")
    @PreAuthorize("hasAuthority('taskJobs:enable')")
    public void enable(@PathVariable Long id){
        taskJobService.enable(id);
    }

    @Log("通用定时任务")
    @PostMapping("/{id}/disable")
    @PreAuthorize("hasAuthority('taskJobs:disable')")
    public void disable(@PathVariable Long id){
        taskJobService.disable(id);
    }

    @Log("执行定时任务")
    @PostMapping("/{id}/execute")
    @PreAuthorize("hasAnyAuthority('taskJobs:save','taskJobs:update','taskJobs:execute')")
    public void execute(@PathVariable Long id){
        taskJobService.execute(taskJobService.getById(id));
    }

    @Log("删除定时任务")
    @DeleteMapping
    @PreAuthorize("hasAuthority('taskJobs:remove')")
    public void remove(@RequestBody Set<Long> ids){
        taskJobService.remove(ids);
    }

    @GetMapping(value = "/logs")
    @PreAuthorize("hasAuthority('taskJobs:list')")
    public PageResult listJobLog(JobQuery jobQuery, Page page){
        return taskJobService.listLog(jobQuery,page);
    }
}
