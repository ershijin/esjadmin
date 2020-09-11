package com.ershijin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ershijin.annotation.Log;
import com.ershijin.model.PageResult;
import com.ershijin.model.form.RoleMenuIdsForm;
import com.ershijin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 角色列表
     * @param keyword
     * @param page
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAuthority('roles:list')")
    @Log("角色列表")
    PageResult list(String keyword, Page page) {
        return roleService.list(keyword, page);
    }

    /**
     * 新增角色
     */
    @PostMapping
    @PreAuthorize("hasAuthority('roles:save')")
    @Log("新增角色")
    void save(@Valid @RequestBody RoleMenuIdsForm roleMenuIds) {
        roleService.save(roleMenuIds);
    }

    /**
     * 更新角色
     */
    @PutMapping
    @PreAuthorize("hasAuthority('roles:update')")
    @Log("更新角色")
    void updateRole(@RequestBody RoleMenuIdsForm roleMenuIds) {
        roleService.update(roleMenuIds);
    }

    /**
     * 获取角色拥有的权限 id 合集
     * @param id
     * @return
     */
    @GetMapping("/{id}/menuIds")
    @PreAuthorize("hasAuthority(@config.GENERAL_PERMISSION)")
    Set<Long> listMenuIds(@PathVariable Long id) {
        return roleService.listMenuIdsById(id);
    }

    /**
     * 删除角色
     * @param id
     */
    @DeleteMapping
    @PreAuthorize("hasAuthority('roles:remove')")
    @Log("删除角色")
    void remove(Long id) {
        roleService.deleteById(id);
    }


}
