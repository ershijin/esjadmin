package com.ershijin.esjadmin.model.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

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
