package com.ershijin.esjadmin.quartz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ershijin.esjadmin.exception.ApiException;
import com.ershijin.esjadmin.model.PageResult;
import com.ershijin.esjadmin.model.entity.Log;
import com.ershijin.esjadmin.quartz.dao.TaskJobMapper;
import com.ershijin.esjadmin.quartz.dao.TaskLogMapper;
import com.ershijin.esjadmin.quartz.model.JobQuery;
import com.ershijin.esjadmin.quartz.model.TaskJob;
import com.ershijin.esjadmin.quartz.model.TaskLog;
import com.ershijin.esjadmin.quartz.utils.TaskManage;
import lombok.RequiredArgsConstructor;
import org.quartz.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskJobService {

    private final TaskJobMapper quartzJobMapper;
    private final TaskLogMapper quartzLogMapper;
    private final TaskManage quartzManage;

    public PageResult list(JobQuery jobQuery, Page page){
        QueryWrapper<TaskJob> queryWrapper = new QueryWrapper<>();

        queryWrapper.select();
        queryWrapper.orderByDesc("id");

        IPage<Log> result = quartzJobMapper.selectPage(page, queryWrapper);
        return new PageResult(result.getTotal(), result.getRecords());
    }

    public List<TaskJob> list(JobQuery jobQuery) {
        QueryWrapper<TaskJob> queryWrapper = new QueryWrapper<>();

        queryWrapper.select();
        queryWrapper.orderByDesc("id");
        return quartzJobMapper.selectList(queryWrapper);
    }

    public PageResult listLog(JobQuery jobQuery, Page page){
        QueryWrapper<TaskLog> queryWrapper = new QueryWrapper<>();

        queryWrapper.select();
        queryWrapper.orderByDesc("id");

        IPage<Log> result = quartzLogMapper.selectPage(page, queryWrapper);
        return new PageResult(result.getTotal(), result.getRecords());
    }

    public List<TaskLog> listLog(JobQuery jobQuery) {
        QueryWrapper<TaskLog> queryWrapper = new QueryWrapper<>();

        return quartzLogMapper.selectList(queryWrapper);
    }

    public TaskJob getById(Long id) {
        TaskJob quartzJob = quartzJobMapper.selectById(id);
        if (quartzJob == null || quartzJob.getId() == null) {
            throw new ApiException("QuartzJob 不存在: id is "+ id);
        }
        return quartzJob;
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(TaskJob quartzJob) {
        if (!CronExpression.isValidExpression(quartzJob.getCronExpression())){
            throw new ApiException("cron表达式格式错误");
        }
        quartzJobMapper.insert(quartzJob);
        quartzManage.addJob(quartzJob);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(TaskJob quartzJob) {
        if (!CronExpression.isValidExpression(quartzJob.getCronExpression())){
            throw new ApiException("cron表达式格式错误");
        }
        quartzJobMapper.updateById(quartzJob);
        quartzManage.updateJobCron(quartzJob);
    }

    public void updateIsPause(TaskJob quartzJob) {
        if (quartzJob.getIsPause()) {
            quartzManage.resumeJob(quartzJob);
            quartzJob.setIsPause(false);
        } else {
            quartzManage.pauseJob(quartzJob);
            quartzJob.setIsPause(true);
        }
        quartzJobMapper.updateById(quartzJob);
    }

    public void execution(TaskJob quartzJob) {
        quartzManage.runJobNow(quartzJob);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            TaskJob quartzJob = getById(id);
            quartzManage.deleteJob(quartzJob);
            quartzJobMapper.deleteById(quartzJob);
        }
    }

}
