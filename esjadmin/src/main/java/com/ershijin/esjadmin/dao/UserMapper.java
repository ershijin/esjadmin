package com.ershijin.esjadmin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ershijin.esjadmin.model.entity.Role;
import com.ershijin.esjadmin.model.entity.User;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    User getByUsername(String username);
    User getById(Long id);
    List<Role> listRolesById(Long id);
    int insert(User user);

    int deleteById(Long id);
}
