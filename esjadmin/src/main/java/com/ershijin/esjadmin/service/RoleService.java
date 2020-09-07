package com.ershijin.esjadmin.service;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ershijin.esjadmin.dao.RoleMapper;
import com.ershijin.esjadmin.dao.RoleMenuMapper;
import com.ershijin.esjadmin.model.PageResult;
import com.ershijin.esjadmin.model.entity.Role;
import com.ershijin.esjadmin.model.entity.RoleMenu;
import com.ershijin.esjadmin.model.form.RoleMenuIdsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    public PageResult list(String keyword, Page<Role> page) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(keyword), "name", keyword);
        IPage<Role> result = roleMapper.selectPage(page, queryWrapper);
        return new PageResult(result.getTotal(), result.getRecords());
    }

    @Transactional
    public void save(RoleMenuIdsForm roleMenuIds) {
        // 保存角色
        roleMapper.insert(roleMenuIds.getRole());
        // 保存角色对应的菜单权限
        bachInsertRoleMenu(roleMenuIds.getRole().getId(), roleMenuIds.getMenuIds());
    }

    /**
     * 保存角色对应的菜单权限
     * @param roleId
     * @param menuIds
     */
    private void bachInsertRoleMenu(Long roleId, Set<Long> menuIds) {
        if (!menuIds.isEmpty()) {
            Set<RoleMenu> roleMenus = new HashSet<>();
            for (Long menuId : menuIds) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenus.add(roleMenu);
            }
            roleMenuMapper.batchInsert(roleMenus);
        }
    }

    public Set<Long> listMenuIdsById(Long id) {
        return roleMenuMapper.listMenuIdsByRoleId(id);
    }

    public void update(RoleMenuIdsForm roleMenuIds) {
        // 保存角色
        roleMapper.updateById(roleMenuIds.getRole());
        // 删除旧的关系
        roleMenuMapper.deleteByRoleId(roleMenuIds.getRole().getId());
        // 保存角色对应的菜单权限
        bachInsertRoleMenu(roleMenuIds.getRole().getId(), roleMenuIds.getMenuIds());
    }

    @Transactional
    public void deleteById(Long id) {
        roleMapper.deleteById(id);
        roleMenuMapper.deleteByRoleId(id);
    }
}
