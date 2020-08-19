package com.ershijin.esjadmin.model.form;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Validated
@Data
public class UserChangePasswordForm {
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Min(value = 6, message = "密码长度不能低于6位")
    private String newPassword;
}
