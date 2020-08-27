package com.ershijin.esjadmin.service;

import cn.hutool.json.JSONObject;
import com.ershijin.esjadmin.dao.LogMapper;
import com.ershijin.esjadmin.model.entity.Log;
import com.ershijin.esjadmin.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
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
     * @param log
     * @param joinPoint
     */
    public void save(Log log, ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.ershijin.esjadmin.annotation.Log aopLog = method.getAnnotation(com.ershijin.esjadmin.annotation.Log.class);

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
}
