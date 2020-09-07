package com.ershijin.esjadmin.quartz.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ershijin.esjadmin.validation.groups.Update;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TaskJob implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String JOB_KEY = "JOB_KEY";

    @NotNull(groups = {Update.class})
    private Long id;

    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

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