package com.ershijin.esjadmin.model.entity;

import com.ershijin.esjadmin.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class RoleMenu extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long roleId;

    private Long menuId;

}
