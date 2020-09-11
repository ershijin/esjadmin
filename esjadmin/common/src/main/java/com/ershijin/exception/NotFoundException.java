package com.ershijin.exception;

import com.ershijin.constant.ResultCode;

public class NotFoundException extends ApiException {
    public static int code = ResultCode.NOT_FOUND;
    public NotFoundException(String message) {
        super(message, code);
    }
}
