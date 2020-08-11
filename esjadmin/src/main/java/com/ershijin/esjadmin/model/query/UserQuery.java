package com.ershijin.esjadmin.model.query;

import lombok.Data;

@Data
public class UserQuery {

    // 关键字
    private String keyword;

    // 角色id
    private Long role_id;

    // 激活状态
    private Boolean enabled;


}
