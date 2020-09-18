package com.ershijin.util;

import cn.hutool.core.util.ObjectUtil;
import com.ershijin.constant.ResultCode;
import com.ershijin.exception.ApiException;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

public class ValidationUtil {

    /**
     * 验证空
     */
    public static void isNull(Object obj, String entity, String parameter , Object value){
        if(ObjectUtil.isNull(obj)){
            String msg = entity + " 不存在: "+ parameter +" is "+ value;
            throw new ApiException(msg, ResultCode.NOT_FOUND);
        }
    }

    /**
     * 验证是否为邮箱
     */
    public static boolean isEmail(String email) {
        return new EmailValidator().isValid(email, null);
    }
}
