package com.ershijin.esjadmin.model.query;

import lombok.Data;

@Data
public class LogQuery {
    private String username;
    private String keyword;
    private String startTime;
    private String endTime;
}
