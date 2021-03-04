package com.ershijin.quartz.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Properties;

/**
 * 定时任务配置
 */
@Configuration
public class TaskConfig {

	@Autowired
	private TaskSchedulerFactory taskSchedulerFactory;

	@Autowired
	Environment env;

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() throws SchedulerException {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setJobFactory(taskSchedulerFactory);
		schedulerFactoryBean.setSchedulerFactory(new StdSchedulerFactory(quartzProperties()));
		return schedulerFactoryBean;
	}

	@Bean
	public Scheduler scheduler() throws SchedulerException {
		return schedulerFactoryBean().getScheduler();
	}

	private Properties quartzProperties() {
		Properties properties = new Properties();
		properties.put("org.quartz.scheduler.instanceName", env.getProperty("quartz.org.quartz.scheduler.instanceName"));
		properties.put("org.quartz.scheduler.instanceId", env.getProperty("quartz.org.quartz.scheduler.instanceId"));
		properties.put("org.quartz.jobStore.isClustered", env.getProperty("quartz.org.quartz.jobStore.isClustered"));
		properties.put("org.quartz.jobStore.class", env.getProperty("quartz.org.quartz.jobStore.class"));
		properties.put("org.quartz.jobStore.clusterCheckinInterval", env.getProperty("quartz.org.quartz.jobStore.clusterCheckinInterval"));
		properties.put("org.quartz.jobStore.driverDelegateClass", env.getProperty("quartz.org.quartz.jobStore.driverDelegateClass"));
		properties.put("org.quartz.jobStore.tablePrefix", env.getProperty("quartz.org.quartz.jobStore.tablePrefix"));
		properties.put("org.quartz.threadPool.threadCount", env.getProperty("quartz.org.quartz.threadPool.threadCount"));

		String dataSource = env.getProperty("quartz.org.quartz.jobStore.dataSource");
		properties.put("org.quartz.jobStore.dataSource", dataSource);
		properties.put(String.format("org.quartz.dataSource.%s.driver", dataSource), env.getProperty(String.format("quartz.org.quartz.dataSource.%s.driver", dataSource)));
		properties.put(String.format("org.quartz.dataSource.%s.connectionProvider.class", dataSource), env.getProperty(String.format("quartz.org.quartz.dataSource.%s.connectionProvider.class", dataSource)));
		properties.put(String.format("org.quartz.dataSource.%s.URL", dataSource), env.getProperty(String.format("quartz.org.quartz.dataSource.%s.URL", dataSource)));
		properties.put(String.format("org.quartz.dataSource.%s.user", dataSource), env.getProperty(String.format("quartz.org.quartz.dataSource.%s.user", dataSource)));
		properties.put(String.format("org.quartz.dataSource.%s.password", dataSource), env.getProperty(String.format("quartz.org.quartz.dataSource.%s.password", dataSource)));
		properties.put(String.format("org.quartz.dataSource.%s.maxConnections", dataSource), env.getProperty(String.format("quartz.org.quartz.dataSource.%s.maxConnections", dataSource)));


		return properties;
	}

}
