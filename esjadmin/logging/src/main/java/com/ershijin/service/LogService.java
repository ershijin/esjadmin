package com.ershijin.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ershijin.dao.LogMapper;
import com.ershijin.model.PageResult;
import com.ershijin.model.entity.Log;
import com.ershijin.model.query.LogQuery;
import com.ershijin.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class LogService {
    @Autowired
    private LogMapper logMapper;

    /**
     * 保存日志
     *
     * @param log
     * @param joinPoint
     */
    public void save(Log log, ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.ershijin.annotation.Log aopLog = method.getAnnotation(com.ershijin.annotation.Log.class);

        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";

        StringBuilder params = new StringBuilder("{");
        //参数值
        List<Object> argValues = new ArrayList<>(Arrays.asList(joinPoint.getArgs()));
        //参数名称
        for (Object argValue : argValues) {
            params.append(argValue).append(" ");
        }
        // 描述
        if (log != null) {
            log.setDescription(aopLog.value());
        }
        assert log != null;

        if ("login".equals(signature.getName())) {
            try {
                String username = new JSONObject(argValues.get(0)).get("username").toString();
                log.setUsername(username);
            } catch (Exception e) {
                this.log.error(e.getMessage(), e);
            }
        }
        log.setAddress(RequestUtils.getCityInfo(log.getIp()));
        log.setMethod(methodName);
        log.setParams(params.toString() + "}");

        logMapper.insert(log);
    }

    public PageResult list(String type, LogQuery query, Page<Log> page) {
        QueryWrapper<Log> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!StringUtils.isEmpty(type), "type", type);
        queryWrapper.eq(!StringUtils.isEmpty(query.getUsername()), "username", query.getUsername());
        queryWrapper.ge(!StringUtils.isEmpty(query.getStartTime()), "create_time", query.getStartTime());
        queryWrapper.le(!StringUtils.isEmpty(query.getEndTime()), "create_time", query.getEndTime());
        queryWrapper.and(!StringUtils.isEmpty(query.getKeyword()), i -> {
            i.eq("username", query.getKeyword())
                    .or().eq("description", query.getKeyword())
                    .or().like("method", query.getKeyword())
                    .or().like("address", query.getKeyword());
        });

        queryWrapper.select(Log.class, i -> !i.getColumn().equals("exception_detail"));
        queryWrapper.orderByDesc("id");

        IPage<Log> result = logMapper.selectPage(page, queryWrapper);
        return new PageResult(result.getTotal(), result.getRecords());
    }

    public Log get(long id) {
        return logMapper.selectById(id);
    }
}
