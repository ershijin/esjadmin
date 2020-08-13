package com.ershijin.esjadmin.component;

import com.ershijin.esjadmin.annotation.NoApiResult;
import com.ershijin.esjadmin.constant.ResultCode;
import com.ershijin.esjadmin.exception.ArgumentNotValidException;
import com.ershijin.esjadmin.exception.NotFoundException;
import com.ershijin.esjadmin.model.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
    /**
     * 无访问权限的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResult handleAccessDeniedException(AccessDeniedException e) {
        return ApiResult.error(ResultCode.FORBIDDEN, "没有访问权限");
    }

    /**
     * 资源不存在的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult handleResourceNotFoundException(NotFoundException e) {
        if (log.isErrorEnabled()) {
            log.error(e.getMessage(), e);
        }
        return ApiResult.error(ResultCode.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ApiResult.error(ResultCode.METHOD_NOT_ALLOWED, "请求方式 " + e.getMethod() + "不允许");
    }

    /**
     * Validated参数校验不通过的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiResult handleResourceArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        StringBuffer sb = new StringBuffer();
        for (FieldError fieldError : result.getFieldErrors()) {
            sb.append(fieldError.getDefaultMessage() + "\n");
        }
        return ApiResult.error(ResultCode.ARGUMENT_NOT_VALID, sb.toString());
    }

    /**
     * Valida 参数校验不通过的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiResult handleResourceConstraintViolationException(ConstraintViolationException e) {
        StringBuffer sb = new StringBuffer();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            sb.append(violation.getMessage() + "\n");
        }
        return ApiResult.error(ResultCode.ARGUMENT_NOT_VALID, sb.toString());
    }

    /**
     * 参数校验不通过的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = ArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiResult handleResourceArgumentNotValidException(ArgumentNotValidException e) {
        if (log.isErrorEnabled()) {
            log.error(e.getMessage(), e);
        }
        return ApiResult.error(ResultCode.ARGUMENT_NOT_VALID, e.getMessage());
    }

    /**
     * 其它的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult handleResourceException(RuntimeException e) {
        if (log.isErrorEnabled()) {
            log.error(e.getMessage(), e);
        }
        return ApiResult.error(ResultCode.ERROR, e.getMessage());
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getParameterType() != ApiResult.class
                && AnnotationUtils.findAnnotation(returnType.getMethod(), NoApiResult.class) == null
                && AnnotationUtils.findAnnotation(returnType.getDeclaringClass(), NoApiResult.class) == null;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return body == null ? ApiResult.success() : ApiResult.success(body);
    }
}
