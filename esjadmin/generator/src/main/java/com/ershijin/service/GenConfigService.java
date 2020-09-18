package com.ershijin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ershijin.dao.GenConfigMapper;
import com.ershijin.model.entity.GenConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Service
@RequiredArgsConstructor
public class GenConfigService {

    private final GenConfigMapper genConfigMapper;

    public GenConfig find(String tableName) {
        QueryWrapper<GenConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("table_name", tableName);
        GenConfig genConfig = genConfigMapper.selectOne(queryWrapper);
        if(genConfig == null){
            return new GenConfig(tableName);
        }
        return genConfig;
    }

    @Transactional
    public GenConfig update(String tableName, GenConfig genConfig) {
        // 如果 api 路径为空，则自动生成路径
        if(StringUtils.isBlank(genConfig.getApiPath())){
            String separator = File.separator;
            String[] paths;
            String symbol = "\\";
            if (symbol.equals(separator)) {
                paths = genConfig.getPath().split("\\\\");
            } else {
                paths = genConfig.getPath().split(File.separator);
            }
            StringBuilder api = new StringBuilder();
            for (String path : paths) {
                api.append(path);
                api.append(separator);
                if ("src".equals(path)) {
                    api.append("api");
                    break;
                }
            }
            genConfig.setApiPath(api.toString());
        }
        // 判断记录是否存在
        if (genConfig.getId() != null) {
            genConfigMapper.updateById(genConfig);
        } else {
            genConfigMapper.insert(genConfig);
        }
        return genConfig;
    }
}
