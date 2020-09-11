package com.ershijin.quartz.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 测试任务
 */
@Slf4j
@Component
public class TestTask {

    public void run1(){
        TestTask.log.info("run1 执行成功" + new Date());
    }

    public void run2(String str){
        log.info("run2 执行成功，参数为： {} - {}", str, new Date());
    }

}
