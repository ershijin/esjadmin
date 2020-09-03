package com.ershijin.esjadmin.quartz.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TaskLog implements Serializable {

    private Long id;

    // 任务名称
    private String jobName;

    // bean名称
    private String beanName;

    // 方法名称
    private String methodName;

    // 参数
    private String params;

    // cron表达式
    private String cronExpression;

    // 状态
    private Boolean isSuccess;

    // 异常详情
    private String exceptionDetail;

    // 执行耗时
    private Long time;

    // 创建时间
    private Date createTime;
}
