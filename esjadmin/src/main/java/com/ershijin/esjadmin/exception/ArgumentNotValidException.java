package com.ershijin.esjadmin.exception;

import com.ershijin.esjadmin.constant.ResultCode;

public class ArgumentNotValidException extends GlobalException {
    public static int code = ResultCode.ARGUMENT_NOT_VALID;
    public ArgumentNotValidException(String message) {
        super(message, code);
    }
}
