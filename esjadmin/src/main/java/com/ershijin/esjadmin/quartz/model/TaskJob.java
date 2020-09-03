package com.ershijin.esjadmin.quartz.model;

import com.ershijin.esjadmin.validation.groups.Update;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
public class TaskJob implements Serializable {

    public static final String JOB_KEY = "JOB_KEY";

    @NotNull(groups = {Update.class})
    private Long id;

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
    private Boolean isPause = false;

    // 负责人
    private String personInCharge;

    // 报警邮箱
    private String email;

    // 失败后暂停
    private Boolean pauseAfterFailure;

    @NotBlank
    // 备注
    private String description;
}