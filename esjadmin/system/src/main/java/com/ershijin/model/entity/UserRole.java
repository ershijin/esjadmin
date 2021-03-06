package com.ershijin.model.entity;

import com.ershijin.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserRole extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long roleId;


}
