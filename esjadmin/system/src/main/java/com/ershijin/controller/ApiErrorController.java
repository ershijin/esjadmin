package com.ershijin.controller;

import com.ershijin.constant.ResultCode;
import com.ershijin.model.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 统一错误处理
 */
@RestController
public class ApiErrorController implements ErrorController {
    private ErrorAttributes errorAttributes;
    private static final String ERROR_PATH = "/error";

    @Autowired
    public ApiErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
    @RequestMapping(ERROR_PATH)
    public ApiResult handlerError(HttpServletResponse response, final WebRequest req) {
        Map<String, Object> attr = this.errorAttributes.getErrorAttributes(req, false);
        if (response.getStatus() >= 400 && response.getStatus() <= 499) {
            return ApiResult.error(ResultCode.CLIENT_ERROR, attr.get("error").toString());
        }
        return ApiResult.error(ResultCode.ERROR, attr.get("error").toString());
    }
}
