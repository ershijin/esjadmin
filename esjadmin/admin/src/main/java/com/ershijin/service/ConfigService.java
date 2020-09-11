package com.ershijin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ershijin.dao.ConfigMapper;
import com.ershijin.model.entity.Config;
import org.apache.commons.lang3.StringUtils;
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
        QueryWrapper<Config> queryWrapper = new QueryWrapper<>();
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

    public List<Config> listAll(String keyword) {
        QueryWrapper<Config> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(Config.class, i -> !i.getColumn().equals("create_time") && !i.getColumn().equals(
                "update_time"));
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like("name", keyword)
                    .or().like("code", keyword)
                    .or().like("value", keyword);
        }
        return configMapper.selectList(queryWrapper);
    }

    public void save(Config config) {
        configMapper.insert(config);
    }

    public void update(Config config) {
        configMapper.updateById(config);
    }

    public void remove(Long id) {
        configMapper.deleteById(id);
    }
}
