package com.ershijin.model.query;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LogQuery {
    private String username;
    private String keyword;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
