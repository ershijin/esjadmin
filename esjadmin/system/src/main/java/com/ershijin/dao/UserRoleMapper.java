package com.ershijin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ershijin.model.entity.UserRole;

import java.util.Set;

public interface UserRoleMapper extends BaseMapper<UserRole> {
    void batchInsert(Set<UserRole> userRoles);

    Set<Long> listRoleIdsByUserId(Long userId);

    int deleteByUserId(Long id);
}
