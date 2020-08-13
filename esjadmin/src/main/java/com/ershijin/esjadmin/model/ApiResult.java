package com.ershijin.esjadmin.model;

import com.ershijin.esjadmin.constant.ResultCode;

import java.util.ArrayList;

public class ApiResult {
    private int code;
    private String message = "";
    private Object data = new ArrayList<>();

    /**
     * 处理成功，无业务数据返回
     * @return
     */
    public static ApiResult success() {
        return new ApiResult();
    }

    /**
     * 处理成功，有业务数据返回
     * @param data
     * @return
     */
    public static ApiResult success(Object data) {
        return new ApiResult(data);
    }

    /**
     * 调用失败
     * @param code
     * @param message
     * @return
     */
    public static ApiResult error(int code, String message) {
        return new ApiResult(code, message);
    }

    /**
     * 默认错误
     * @param message
     * @return
     */
    public static ApiResult error(String message) {
        return new ApiResult(ResultCode.ERROR, message);
    }



    /**
     * 调用成功，不需要返回业务数据
     */
    public ApiResult() {
        this.code = ResultCode.SUCCESS;
        this.message = "ok";
    }


    /**
     * 调用成功，带返回数据构造器
     * @param data
     */
    public ApiResult(Object data) {
        this.code = ResultCode.SUCCESS;
        this.data = data;
    }

    /**
     * 调用失败，返回code和message
     */
    public ApiResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
