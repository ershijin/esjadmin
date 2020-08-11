package com.ershijin.esjadmin.model.form;

import com.ershijin.esjadmin.model.entity.Role;
import lombok.Data;

import javax.validation.Valid;
import java.util.Set;
@Data
public class RoleMenuIdsForm {
    @Valid
    private Role role;
    private Set<Long> menuIds;

}
