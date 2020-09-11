package com.ershijin.model.entity;

import com.ershijin.model.BaseEntity;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Alias("Role")
@Data
public class Role extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "角色名不能为空")
    private String name;

    private String remark;

}
