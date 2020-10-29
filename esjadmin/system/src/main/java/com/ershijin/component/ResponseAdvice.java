package com.ershijin.component;

import com.ershijin.annotation.NoApiResult;
import com.ershijin.constant.ResultCode;
import com.ershijin.exception.ApiException;
import com.ershijin.exception.ArgumentNotValidException;
import com.ershijin.exception.NotFoundException;
import com.ershijin.model.ApiResult;
import com.ershijin.util.JsonUtils;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 统一异常拦截
 * 统一包装返回数据
 */
@RestControllerAdvice(basePackages = "com.ershijin")
@Slf4j
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResult Exception(Exception e, HttpServletResponse response) {
        if (log.isErrorEnabled()) {
            log.error(e.getMessage(), e);
        }

        if (e instanceof AccessDeniedException) {
            return ApiResult.error(ResultCode.FORBIDDEN, "您没有执行该操作的权限");
        }

        if (e instanceof NotFoundException) {
            return ApiResult.error(ResultCode.NOT_FOUND, e.getMessage());
        }

        if (e instanceof ArgumentNotValidException) {
            return ApiResult.error(ResultCode.ARGUMENT_NOT_VALID, e.getMessage());
        }

        if (e instanceof TypeMismatchException || e instanceof HttpMessageNotReadableException || e instanceof InvalidFormatException) {
            return ApiResult.error(ResultCode.ARGUMENT_NOT_VALID, "参数类型错误");
        }

        // 参数校验不通过的异常
        if (e instanceof ConstraintViolationException) {
            StringBuffer sb = new StringBuffer();
            for (ConstraintViolation violation : ((ConstraintViolationException)e).getConstraintViolations()) {
                sb.append(violation.getMessage() + "\n");
            }
            return ApiResult.error(ResultCode.ARGUMENT_NOT_VALID, sb.toString());
        }

        // Validated参数校验不通过的异常
        if (e instanceof MethodArgumentNotValidException) {
            BindingResult result = ((MethodArgumentNotValidException)e).getBindingResult();
            StringBuffer sb = new StringBuffer();
            for (FieldError fieldError : result.getFieldErrors()) {
                sb.append(fieldError.getDefaultMessage() + "\n");
            }
            return ApiResult.error(ResultCode.ARGUMENT_NOT_VALID, sb.toString());

        }

        if (e instanceof ApiException) {
            return ApiResult.error(((ApiException)e).getCode(), e.getMessage());
        }

        return ApiResult.error(ResultCode.ERROR, e.getMessage());
    }

    /**
     * return类型不为ApiResult，不带@NoApiResult注解的类或方法自动包装返回结构
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getParameterType() != ApiResult.class
                && AnnotationUtils.findAnnotation(returnType.getMethod(), NoApiResult.class) == null
                && AnnotationUtils.findAnnotation(returnType.getDeclaringClass(), NoApiResult.class) == null;
    }

    /**
     * 自动包装后端接口返回结果
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof String) {
            return JsonUtils.toJsonString(ApiResult.success(body));
        }
        return body == null ? ApiResult.success() : ApiResult.success(body);
    }
}
