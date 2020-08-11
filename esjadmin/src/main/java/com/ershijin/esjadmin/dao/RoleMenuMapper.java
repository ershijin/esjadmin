package com.ershijin.esjadmin.dao;

import com.ershijin.esjadmin.model.entity.RoleMenu;

import java.util.Set;

public interface RoleMenuMapper {
    void batchInsert(Set<RoleMenu> roleMenus);

    Set<Long> listMenuIdsByRoleId(Long roleId);

    void deleteByRoleId(Long id);
}
