package com.ershijin.esjadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ershijin.esjadmin.dao.ConfigMapper;
import com.ershijin.esjadmin.model.entity.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置服务
 * @todo 启动加载配置调整为使用加载，使用缓存
 */
@Service
public class ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    private Map<String, String> configMap;

    @PostConstruct
    public void init() {
        QueryWrapper<Config> queryWrapper = new QueryWrapper();
        queryWrapper.select("code", "value");
        List<Config> configs = configMapper.selectList(queryWrapper);
        configMap = new HashMap<>();
        for (Config config : configs) {
            configMap.put(config.getCode(), config.getValue());
        }
    }
    public String get(String code) {
        return configMap.get(code);
    }
    public Map<String, String> getConfigMap() {
        return configMap;
    }

    public Map<String, String> get() {
        return configMap;
    }
}
