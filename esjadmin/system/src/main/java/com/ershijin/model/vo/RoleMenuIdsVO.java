package com.ershijin.model.vo;

import com.ershijin.model.entity.Role;
import lombok.Data;

import javax.validation.Valid;
import java.util.Set;
@Data
public class RoleMenuIdsVO {
    @Valid
    private Role role;
    private Set<Long> menuIds;

}
