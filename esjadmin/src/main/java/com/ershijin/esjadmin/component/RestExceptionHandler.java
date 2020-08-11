package com.ershijin.esjadmin.component;

import com.ershijin.esjadmin.constant.ResultCode;
import com.ershijin.esjadmin.exception.ArgumentNotValidException;
import com.ershijin.esjadmin.exception.NotFoundException;
import com.ershijin.esjadmin.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestExceptionHandler {
    private Logger logger =  LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * 无访问权限的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result handleAccessDeniedException(AccessDeniedException e) {
        return Result.error(ResultCode.FORBIDDEN, "没有访问权限");
    }

    /**
     * 资源不存在的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result handleResourceNotFoundException(NotFoundException e) {
        if (logger.isErrorEnabled()) {
            logger.error(e.getMessage(), e);
        }
        return Result.error(ResultCode.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return Result.error(ResultCode.METHOD_NOT_ALLOWED, "请求方式 " + e.getMethod() + "不允许");
    }

    /**
     * Validated参数校验不通过的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Result handleResourceArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        StringBuffer sb = new StringBuffer();
        for (FieldError fieldError : result.getFieldErrors()) {
            sb.append(fieldError.getDefaultMessage() + "\n");
        }
        return Result.error(ResultCode.ARGUMENT_NOT_VALID, sb.toString());
    }

    /**
     * Valida 参数校验不通过的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Result handleResourceConstraintViolationException(ConstraintViolationException e) {
        StringBuffer sb = new StringBuffer();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            sb.append(violation.getMessage() + "\n");
        }
        return Result.error(ResultCode.ARGUMENT_NOT_VALID, sb.toString());
    }

    /**
     * 参数校验不通过的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = ArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Result handleResourceArgumentNotValidException(ArgumentNotValidException e) {
        if (logger.isErrorEnabled()) {
            logger.error(e.getMessage(), e);
        }
        return Result.error(ResultCode.ARGUMENT_NOT_VALID, e.getMessage());
    }

    /**
     * 其它的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleResourceException(RuntimeException e) {
        if (logger.isErrorEnabled()) {
            logger.error(e.getMessage(), e);
        }
        return Result.error(ResultCode.ERROR, e.getMessage());
    }
}
