package com.ershijin.controller;

import cn.hutool.core.util.IdUtil;
import com.ershijin.config.security.bean.LoginProperties;
import com.ershijin.config.security.bean.SecurityProperties;
import com.ershijin.util.RedisUtils;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final SecurityProperties properties;

    @Resource
    private LoginProperties loginProperties;

    @ApiOperation("获取验证码")
    @GetMapping(value = "/code")
    public Map<String, Object> getCode() {
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        RedisUtils.set(uuid, captcha.text(), loginProperties.getLoginCode().getExpiration(), TimeUnit.SECONDS);

        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return imgResult;
    }
}
