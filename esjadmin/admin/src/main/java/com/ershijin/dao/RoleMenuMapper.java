package com.ershijin.dao;

import com.ershijin.model.entity.RoleMenu;

import java.util.Set;

public interface RoleMenuMapper {
    void batchInsert(Set<RoleMenu> roleMenus);

    Set<Long> listMenuIdsByRoleId(Long roleId);

    void deleteByRoleId(Long id);
}
