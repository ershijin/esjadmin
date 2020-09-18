package com.ershijin.quartz.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ershijin.model.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class TaskJob extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String JOB_KEY = "JOB_KEY";

    // 定时器名称
    private String jobName;

    // Bean名称
    private String beanName;

    @NotBlank
    // 方法名称
    private String methodName;

    // 参数
    private String params;

    @NotBlank
    // cron表达式
    private String cronExpression;

    // 状态，暂时或启动
    @TableField("is_pause")
    private boolean pause = false;

    // 失败后暂停
    @TableField("is_pause_after_failure")
    private boolean pauseAfterFailure;

    @NotBlank
    // 备注
    private String description;
}