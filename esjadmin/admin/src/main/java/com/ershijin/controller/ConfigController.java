package com.ershijin.controller;

import com.ershijin.model.entity.Config;
import com.ershijin.service.ConfigService;
import com.ershijin.validation.groups.Save;
import com.ershijin.validation.groups.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/configs")
public class ConfigController {
    @Autowired
    private ConfigService configService;

    @GetMapping("kv")
    public Map<String, String> kv() {
        return configService.get();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('configs:list')")
    public List<Config> list(String keyword) {
        return configService.listAll(keyword);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('configs:save')")
    public void save(@Validated({Save.class}) @RequestBody Config config) {
        configService.save(config);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('configs:update')")
    public void update(@Validated({Update.class}) @RequestBody Config config) {
        configService.update(config);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('configs:remove')")
    public void remove(@PathVariable Long id) {
        configService.remove(id);
    }
}
