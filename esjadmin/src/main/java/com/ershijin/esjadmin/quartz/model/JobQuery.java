package com.ershijin.esjadmin.quartz.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class JobQuery {

    private String jobName;

    private Boolean isSuccess;

    private List<Timestamp> createTime;
}
