package com.ershijin.esjadmin.dao;

import com.ershijin.esjadmin.model.entity.UserRole;

import java.util.Set;

public interface UserRoleMapper {
    void batchInsert(Set<UserRole> userRoles);

    Set<Long> listRoleIdsByUserId(Long userId);

    int deleteByUserId(Long id);
}
