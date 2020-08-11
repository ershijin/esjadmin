package com.ershijin.esjadmin.exception;

import com.ershijin.esjadmin.constant.ResultCode;

public class NotFoundException extends GlobalException {
    public static int code = ResultCode.NOT_FOUND;
    public NotFoundException(String message) {
        super(message, code);
    }
}
