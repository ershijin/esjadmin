package com.ershijin.quartz.task;

import com.ershijin.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationTask {
    @Autowired
    private AuthenticationService authenticationService;

    public void run() {
        authenticationService.deleteExpired();
    }
}
