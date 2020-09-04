package com.ershijin.esjadmin.quartz.model;

import lombok.Data;

@Data
public class JobQuery {

    private String keyword;

    private String startTime;

    private String endTime;
}
