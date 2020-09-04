package com.ershijin.esjadmin.quartz.utils;

import com.ershijin.esjadmin.config.thread.ThreadPoolExecutorUtils;
import com.ershijin.esjadmin.quartz.dao.TaskLogMapper;
import com.ershijin.esjadmin.quartz.model.TaskJob;
import com.ershijin.esjadmin.quartz.model.TaskLog;
import com.ershijin.esjadmin.quartz.service.TaskJobService;
import com.ershijin.esjadmin.util.SpringContextHolder;
import com.ershijin.esjadmin.util.ThrowableUtils;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Async
public class ExecutionJob extends QuartzJobBean {

    private final static ThreadPoolExecutor EXECUTOR = ThreadPoolExecutorUtils.getPoll();

    @Override
    public void executeInternal(JobExecutionContext context) {
        TaskJob taskJob = (TaskJob) context.getMergedJobDataMap().get(TaskJob.JOB_KEY);
        // 获取spring bean
        TaskLogMapper taskLogMapper = SpringContextHolder.getBean(TaskLogMapper.class);
        TaskJobService taskJobService = SpringContextHolder.getBean(TaskJobService.class);

        TaskLog log = new TaskLog();
        log.setJobName(taskJob.getJobName());
        log.setBeanName(taskJob.getBeanName());
        log.setMethodName(taskJob.getMethodName());
        log.setParams(taskJob.getParams());
        long startTime = System.currentTimeMillis();
        log.setCronExpression(taskJob.getCronExpression());
        try {
            // 执行任务
            System.out.println("--------------------------------------------------------------");
            System.out.println("任务开始执行，任务名称：" + taskJob.getJobName());
            TaskRunnable task = new TaskRunnable(taskJob.getBeanName(), taskJob.getMethodName(),
                    taskJob.getParams());
            Future<?> future = EXECUTOR.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            // 任务状态
            log.setIsSuccess(true);
            System.out.println("任务执行完毕，任务名称：" + taskJob.getJobName() + ", 执行时间：" + times + "毫秒");
            System.out.println("--------------------------------------------------------------");
        } catch (Exception e) {
            System.out.println("任务执行失败，任务名称：" + taskJob.getJobName());
            System.out.println(ThrowableUtils.getStackTrace(e));
            System.out.println("--------------------------------------------------------------");
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            // 任务状态 0：成功 1：失败
            log.setIsSuccess(false);
            log.setExceptionDetail(ThrowableUtils.getStackTrace(e));
            // 任务如果失败了则暂停
            if(taskJob.isPauseAfterFailure()){
                //更新状态
                taskJobService.disable(taskJob.getId());
            }
        } finally {
            taskLogMapper.insert(log);
        }
    }

}
