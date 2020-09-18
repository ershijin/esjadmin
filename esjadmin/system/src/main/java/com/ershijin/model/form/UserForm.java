package com.ershijin.model.form;

import com.ershijin.validation.constraints.NotDuplicate;
import com.ershijin.validation.groups.Save;
import com.ershijin.validation.groups.Update;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Data
public class UserForm {

    @NotNull(groups = {Update.class}, message = "id不能为空")
    private Long id;

    @NotBlank(groups = {Save.class, Update.class}, message = "账号不能为空")
    @NotDuplicate(groups = {Save.class}, table = "user", column = "username", message = "账号已经被使用")
    private String username;

    @NotBlank(groups = {Save.class}, message = "密码不能为空")
    private String password;

    @NotBlank(groups = {Save.class, Update.class}, message = "名字不能为空")
    private String name;

    private String avatar;
    private boolean enabled;
    private String remark;

    private List<Long> roleIds;

}
