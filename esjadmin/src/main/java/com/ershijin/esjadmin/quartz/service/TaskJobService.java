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
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskJobService {

    private final TaskJobMapper taskJobMapper;
    private final TaskLogMapper taskLogMapper;
    private final TaskManage taskManage;

    public PageResult list(JobQuery query, Page page){
        QueryWrapper<TaskJob> queryWrapper = new QueryWrapper<>();

        queryWrapper.ge(!StringUtils.isEmpty(query.getStartTime()), "create_time", query.getStartTime());
        queryWrapper.le(!StringUtils.isEmpty(query.getEndTime()), "create_time", query.getEndTime());
        queryWrapper.and(!StringUtils.isEmpty(query.getKeyword()), i -> {
            i.like("bean_name", query.getKeyword())
                    .or().like("description", query.getKeyword())
                    .or().like("method_name", query.getKeyword())
                    .or().like("job_name", query.getKeyword());
        });


        queryWrapper.select();
        queryWrapper.orderByDesc("id");

        IPage<Log> result = taskJobMapper.selectPage(page, queryWrapper);
        return new PageResult(result.getTotal(), result.getRecords());
    }

    public List<TaskJob> list(JobQuery jobQuery) {
        QueryWrapper<TaskJob> queryWrapper = new QueryWrapper<>();

        queryWrapper.select();
        queryWrapper.orderByDesc("id");
        return taskJobMapper.selectList(queryWrapper);
    }

    public PageResult listLog(JobQuery query, Page page){
        QueryWrapper<TaskLog> queryWrapper = new QueryWrapper<>();

        queryWrapper.ge(!StringUtils.isEmpty(query.getStartTime()), "create_time", query.getStartTime());
        queryWrapper.le(!StringUtils.isEmpty(query.getEndTime()), "create_time", query.getEndTime());
        queryWrapper.and(!StringUtils.isEmpty(query.getKeyword()), i -> {
            i.like("bean_name", query.getKeyword())
                    .or().like("method_name", query.getKeyword())
                    .or().like("job_name", query.getKeyword());
        });

        queryWrapper.select();
        queryWrapper.orderByDesc("id");

        IPage<Log> result = taskLogMapper.selectPage(page, queryWrapper);
        return new PageResult(result.getTotal(), result.getRecords());
    }

    public List<TaskLog> listLog(JobQuery jobQuery) {
        QueryWrapper<TaskLog> queryWrapper = new QueryWrapper<>();

        return taskLogMapper.selectList(queryWrapper);
    }

    public TaskJob getById(Long id) {
        TaskJob taskJob = taskJobMapper.selectById(id);
        if (taskJob == null || taskJob.getId() == null) {
            throw new ApiException("taskJob 不存在: id is "+ id);
        }
        return taskJob;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(TaskJob taskJob) {
        if (!CronExpression.isValidExpression(taskJob.getCronExpression())){
            throw new ApiException("cron表达式格式错误");
        }
        taskJobMapper.insert(taskJob);
        taskManage.addJob(taskJob);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(TaskJob taskJob) {
        if (!CronExpression.isValidExpression(taskJob.getCronExpression())){
            throw new ApiException("cron表达式格式错误");
        }
        taskJobMapper.updateById(taskJob);
        taskManage.updateJobCron(taskJob);
    }

    public void enable(Long id) {
        TaskJob taskJob = getById(id);
        taskManage.resumeJob(taskJob);
        taskJob.setPause(false);
        taskJobMapper.updateById(taskJob);
    }

    public void disable(Long id) {
        TaskJob taskJob = getById(id);
        taskManage.pauseJob(taskJob);
        taskJob.setPause(true);
        taskJobMapper.updateById(taskJob);
    }

    public void execute(TaskJob taskJob) {
        taskManage.runJobNow(taskJob);
    }

    @Transactional(rollbackFor = Exception.class)
    public void remove(Set<Long> ids) {
        for (Long id : ids) {
            TaskJob taskJob = getById(id);
            taskManage.deleteJob(taskJob);
            taskJobMapper.deleteById(taskJob);
        }
    }

}
