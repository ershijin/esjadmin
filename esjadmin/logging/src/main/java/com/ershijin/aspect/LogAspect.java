/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.ershijin.aspect;

import com.ershijin.model.entity.Log;
import com.ershijin.service.LogService;
import com.ershijin.util.ObjectUtils;
import com.ershijin.util.RequestUtils;
import com.ershijin.util.ThrowableUtils;
import com.ershijin.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
@Aspect
@Slf4j
public class LogAspect {

    private final LogService logService;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    public LogAspect(LogService logService) {
        this.logService = logService;
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.ershijin.annotation.Log)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        currentTime.set(System.currentTimeMillis());
        result = joinPoint.proceed();
        Log log = new Log();
        log.setType("INFO");
        log.setTime(System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        log.setUsername(getUsername());
        HttpServletRequest request = ObjectUtils.getHttpServletRequest();
        log.setBrowser(RequestUtils.getBrowser(request));
        log.setIp(RequestUtils.getIp(request));
        logService.save(log, joinPoint);
        return result;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Log log = new Log();
        log.setType("ERROR");
        log.setTime(System.currentTimeMillis() - currentTime.get());
        currentTime.remove();
        log.setExceptionDetail(ThrowableUtils.getStackTrace(e));
        log.setUsername(getUsername());
        HttpServletRequest request = ObjectUtils.getHttpServletRequest();
        log.setBrowser(RequestUtils.getBrowser(request));
        log.setIp(RequestUtils.getIp(request));

        logService.save(log, (ProceedingJoinPoint)joinPoint);
    }

    public String getUsername() {
        try {
            return UserUtils.getCurrentUser().getUsername();
        }catch (Exception e){
            return "";
        }
    }
}
