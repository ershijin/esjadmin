package com.ershijin.esjadmin.component;

import com.ershijin.esjadmin.annotation.NoApiResult;
import com.ershijin.esjadmin.constant.ResultCode;
import com.ershijin.esjadmin.exception.ApiException;
import com.ershijin.esjadmin.exception.ArgumentNotValidException;
import com.ershijin.esjadmin.exception.NotFoundException;
import com.ershijin.esjadmin.model.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
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
    public ApiResult handleResourceNotFoundException(NotFoundException e) {
        if (log.isErrorEnabled()) {
            log.error(e.getMessage(), e);
        }
        return ApiResult.error(ResultCode.NOT_FOUND, e.getMessage());
    }

    /**
     * Validated参数校验不通过的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
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
    public ApiResult handleResourceArgumentNotValidException(ArgumentNotValidException e) {
        if (log.isErrorEnabled()) {
            log.error(e.getMessage(), e);
        }
        return ApiResult.error(ResultCode.ARGUMENT_NOT_VALID, e.getMessage());
    }

    @ExceptionHandler(value = ApiException.class)
    @ResponseBody
    public ApiResult handleApiException(ApiException e) {
        if (log.isErrorEnabled()) {
            log.error(e.getMessage(), e);
        }
        return ApiResult.error(e.getCode(), e.getMessage());
    }

    /**
     * 其它的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public ApiResult handleResourceException(RuntimeException e) {
        if (log.isErrorEnabled()) {
            log.error(e.getMessage(), e);
        }
        return ApiResult.error(ResultCode.ERROR, e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResult Exception(Exception e) {
        if (log.isErrorEnabled()) {
            log.error(e.getMessage(), e);
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
        return body == null ? ApiResult.success() : ApiResult.success(body);
    }
}
