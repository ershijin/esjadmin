package com.ershijin.esjadmin.model.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Alias("Role")
@Data
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Date createTime;

    private Date updateTime;

    @NotBlank(message = "角色名不能为空")
    private String name;

    private String remark;

}
