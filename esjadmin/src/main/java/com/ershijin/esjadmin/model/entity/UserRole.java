package com.ershijin.esjadmin.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Date createTime;
    private Date updateTime;

    private Long userId;
    private Long roleId;


}
