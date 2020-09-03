package com.ershijin.esjadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.ershijin.esjadmin.dao", "com.ershijin.esjadmin.*.dao"})
public class EsjAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsjAdminApplication.class, args);
    }

}
