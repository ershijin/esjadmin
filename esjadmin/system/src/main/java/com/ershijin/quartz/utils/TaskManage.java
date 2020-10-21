package com.ershijin.quartz.utils;

import com.ershijin.exception.ApiException;
import com.ershijin.quartz.model.TaskJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Component
public class TaskManage {

    private static final String JOB_NAME = "TASK_";

    @Resource(name = "scheduler")
    private Scheduler scheduler;

    public void addJob(TaskJob taskJob){
        try {
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ExecutionJob.class).
                    withIdentity(JOB_NAME + taskJob.getId()).build();

            //通过触发器名和cron 表达式创建 Trigger
            Trigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(JOB_NAME + taskJob.getId())
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(taskJob.getCronExpression()))
                    .build();

            cronTrigger.getJobDataMap().put(TaskJob.JOB_KEY, taskJob);

            //重置启动时间
            ((CronTriggerImpl)cronTrigger).setStartTime(new Date());

            //执行定时任务
            scheduler.scheduleJob(jobDetail,cronTrigger);

            // 暂停任务
            if (taskJob.isPause()) {
                pauseJob(taskJob);
            }
        } catch (ObjectAlreadyExistsException e) {
            log.warn("定时任务 {} 已经创建过", taskJob.getJobName());
        } catch (Exception e){
            System.out.println(e.getStackTrace());
            log.error("创建定时任务失败", e);
            throw new ApiException("创建定时任务失败");
        }
    }

    /**
     * 更新job cron表达式
     * @param taskJob /
     */
    public void updateJobCron(TaskJob taskJob){
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + taskJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if(trigger == null){
                addJob(taskJob);
                trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            }
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(taskJob.getCronExpression());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //重置启动时间
            ((CronTriggerImpl)trigger).setStartTime(new Date());
            trigger.getJobDataMap().put(TaskJob.JOB_KEY,taskJob);

            scheduler.rescheduleJob(triggerKey, trigger);
            // 暂停任务
            if (taskJob.isPause()) {
                pauseJob(taskJob);
            }
        } catch (Exception e){
            log.error("更新定时任务失败", e);
            throw new ApiException("更新定时任务失败");
        }

    }

    /**
     * 删除一个job
     * @param taskJob /
     */
    public void deleteJob(TaskJob taskJob){
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME + taskJob.getId());
            scheduler.pauseJob(jobKey);
            scheduler.deleteJob(jobKey);
        } catch (Exception e){
            log.error("删除定时任务失败 {}", e);
            throw new ApiException("删除定时任务失败");
        }
    }

    /**
     * 恢复一个job
     * @param taskJob /
     */
    public void resumeJob(TaskJob taskJob){
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + taskJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if(trigger == null) {
                addJob(taskJob);
            }
            JobKey jobKey = JobKey.jobKey(JOB_NAME + taskJob.getId());
            scheduler.resumeJob(jobKey);
        } catch (Exception e){
            log.error("恢复定时任务失败", e);
            throw new ApiException("恢复定时任务失败");
        }
    }

    /**
     * 立即执行job
     * @param taskJob /
     */
    public void runJobNow(TaskJob taskJob){
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + taskJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if(trigger == null) {
                addJob(taskJob);
            }
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(TaskJob.JOB_KEY, taskJob);
            JobKey jobKey = JobKey.jobKey(JOB_NAME + taskJob.getId());
            scheduler.triggerJob(jobKey,dataMap);
        } catch (Exception e){
            log.error("定时任务执行失败", e);
            throw new ApiException("定时任务执行失败");
        }
    }

    /**
     * 暂停一个job
     * @param taskJob /
     */
    public void pauseJob(TaskJob taskJob){
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME + taskJob.getId());
            scheduler.pauseJob(jobKey);
        } catch (Exception e){
            log.error("定时任务暂停失败", e);
            throw new ApiException("定时任务暂停失败");
        }
    }
}
