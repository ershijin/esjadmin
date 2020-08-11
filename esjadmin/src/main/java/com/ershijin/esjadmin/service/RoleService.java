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

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    public PageResult list(int pageNum, int pageSize, Map condition) {
        Page<Role> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty((String) condition.get("keyword"))) {
            queryWrapper.like("name", condition.get("keyword"));
        }
        IPage<Role> result = roleMapper.selectPage(page, queryWrapper);
        return new PageResult(result.getTotal(), result.getRecords());
    }

    @Transactional
    public void save(RoleMenuIdsForm roleMenuIds) {
        roleMenuIds.getRole().setCreateTime(new Date());
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
                roleMenu.setCreateTime(new Date());
                roleMenus.add(roleMenu);
            }
            roleMenuMapper.batchInsert(roleMenus);
        }
    }

    public Set<Long> listMenuIdsById(Long id) {
        return roleMenuMapper.listMenuIdsByRoleId(id);
    }

    public void update(RoleMenuIdsForm roleMenuIds) {
        roleMenuIds.getRole().setUpdateTime(new Date());
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
