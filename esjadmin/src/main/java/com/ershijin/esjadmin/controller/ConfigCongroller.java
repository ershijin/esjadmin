package com.ershijin.esjadmin.controller;

import com.ershijin.esjadmin.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/configs")
public class ConfigCongroller {
    @Autowired
    private ConfigService configService;

    @GetMapping("")
    public Map<String, String> list() {
        return configService.get();
    }
}
