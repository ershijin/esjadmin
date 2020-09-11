package com.ershijin.constant;

/**
 * 全局状态吗
 */
public class ResultCode {
    // 成功
    public static final int SUCCESS = 0;

    // 客户端通用错误
    public static final int CLIENT_ERROR = 40000;

    // 凭证验证失败
    public static final int UNAUTHORIZED = 40001;

    // 没有权限访问
    public static final int FORBIDDEN = 40003;

    // 请求的接口没找到
    public static final int NOT_FOUND = 40004;

    // 参数校验不通过
    public static final int ARGUMENT_NOT_VALID = 40022;

    //操作失败,默认状态码
    public static final int ERROR = 50000;

}
