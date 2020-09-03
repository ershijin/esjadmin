package com.ershijin.esjadmin.quartz.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ershijin.esjadmin.quartz.dao.TaskJobMapper;
import com.ershijin.esjadmin.quartz.model.TaskJob;
import com.ershijin.esjadmin.quartz.utils.TaskManage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobRunner implements ApplicationRunner {
    private final TaskJobMapper quartzJobMapper;
    private final TaskManage quartzManage;

    /**
     * 项目启动时重新激活启用的定时任务
     *
     * @param applicationArguments /
     */
    @Override
    public void run(ApplicationArguments applicationArguments) {
        log.info("--------------------注入定时任务---------------------");

        List<TaskJob> quartzJobs = quartzJobMapper.selectList(new QueryWrapper<TaskJob>().eq("is_pause", false));
        quartzJobs.forEach(quartzManage::addJob);

        log.info("--------------------定时任务注入结束---------------------");
    }
}
