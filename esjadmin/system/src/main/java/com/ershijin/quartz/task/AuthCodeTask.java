package com.ershijin.quartz.task;

import com.ershijin.service.AuthCodeService;
import com.ershijin.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthCodeTask {
    @Autowired
    private AuthCodeService authCodeService;

    public void run() {
        authCodeService.deleteExpired();
    }
}
