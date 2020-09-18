package com.ershijin.quartz.config;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 定时任务配置
 */
@Configuration
public class TaskConfig {

	@Autowired
	private TaskSchedulerFactory taskSchedulerFactory;

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setJobFactory(taskSchedulerFactory);
		schedulerFactoryBean.setSchedulerFactory(new StdSchedulerFactory());
		return schedulerFactoryBean;
	}

	@Bean
	public Scheduler scheduler() {
		return schedulerFactoryBean().getScheduler();
	}

}
