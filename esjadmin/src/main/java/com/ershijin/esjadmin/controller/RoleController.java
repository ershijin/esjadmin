package com.ershijin.esjadmin.controller;

import com.ershijin.esjadmin.model.PageResult;
import com.ershijin.esjadmin.model.form.RoleMenuIdsForm;
import com.ershijin.esjadmin.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 角色列表
     * @param page
     * @param pageSize
     * @param keyword
     * @return
     */
    @GetMapping("list")
    PageResult list(int page, int pageSize, String keyword) {
        Map<String, String> condition = new HashMap<>();
        if (StringUtils.isNotBlank(keyword)) {
            condition.put("keyword", keyword);
        }
        return roleService.list(page, pageSize, condition);
    }

    /**
     * 新增角色
     */
    @PostMapping("save")
    void save(@Valid @RequestBody RoleMenuIdsForm roleMenuIds) {
        roleService.save(roleMenuIds);
    }

    /**
     * 更新角色
     */
    @PostMapping("update")
    void updateRole(@RequestBody RoleMenuIdsForm roleMenuIds) {
        roleService.update(roleMenuIds);
    }

    /**
     * 获取角色拥有的权限 id 合集
     * @param id
     * @return
     */
    @GetMapping("list_menu_ids")
    Set<Long> listMenuIds(Long id) {
        return roleService.listMenuIdsById(id);
    }

    /**
     * 删除角色
     * @param id
     */
    @PostMapping("remove")
    void remove(Long id) {
        roleService.deleteById(id);
    }


}
