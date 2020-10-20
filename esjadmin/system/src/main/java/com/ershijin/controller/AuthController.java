package com.ershijin.controller;

import cn.hutool.core.util.IdUtil;
import com.ershijin.config.security.bean.LoginProperties;
import com.ershijin.config.security.bean.SecurityProperties;
import com.ershijin.model.entity.AuthCode;
import com.ershijin.service.AuthCodeService;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final SecurityProperties properties;

    @Resource
    private LoginProperties loginProperties;

    @Autowired
    private AuthCodeService authCodeService;

    @ApiOperation("获取验证码")
    @GetMapping(value = "/code")
    public Map<String, Object> getCode() {
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        // @todo 保存到数据库，后期调整为redis
        AuthCode authCode = new AuthCode();
        authCode.setUuid(uuid);
        authCode.setCode(captcha.text());
        authCode.setCreateTime(LocalDateTime.now());
        authCode.setExpireTime(LocalDateTime.now().plus(loginProperties.getLoginCode().getExpiration(),
                ChronoUnit.MINUTES));
        authCodeService.save(authCode);

        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return imgResult;
    }
}
