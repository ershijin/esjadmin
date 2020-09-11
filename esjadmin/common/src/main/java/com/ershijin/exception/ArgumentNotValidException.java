package com.ershijin.exception;

import com.ershijin.constant.ResultCode;

public class ArgumentNotValidException extends ApiException {
    public static int code = ResultCode.ARGUMENT_NOT_VALID;
    public ArgumentNotValidException(String message) {
        super(message, code);
    }
}
