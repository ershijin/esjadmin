package com.ershijin.esjadmin.exception;

import com.ershijin.esjadmin.constant.ResultCode;

public class ApiException extends RuntimeException {
    private int code = ResultCode.ERROR;

    public ApiException(String message) {
        super(message);
    }
    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}
