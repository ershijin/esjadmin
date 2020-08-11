package com.ershijin.esjadmin.constant;

/**
 * 全局状态吗
 */
public class ResultCode {
    // 成功
    public static final int SUCCESS = 0;

    // 没有权限访问
    public static final int FORBIDDEN = 403;

    // 请求的接口没找到
    public static final int NOT_FOUND = 404;

    // 请求接口访问方法不允许
    public static final int METHOD_NOT_ALLOWED = 405;

    // 参数校验不通过
    public static final int ARGUMENT_NOT_VALID = 422;

    //操作失败,默认状态码
    public static final int ERROR = 500;

}
