package com.ershijin.controller;

import com.ershijin.model.entity.GenConfig;
import com.ershijin.service.GenConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genConfig")
public class GenConfigController {

    private final GenConfigService genConfigService;

    @GetMapping(value = "/{tableName}")
    public GenConfig query(@PathVariable String tableName){
        return genConfigService.find(tableName);
    }

    @PutMapping
    public GenConfig update(@Validated @RequestBody GenConfig genConfig){
        return genConfigService.update(genConfig.getTableName(), genConfig);
    }
}
