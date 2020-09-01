package com.ershijin.esjadmin.controller;

import com.ershijin.esjadmin.annotation.Log;
import com.ershijin.esjadmin.model.entity.Menu;
import com.ershijin.esjadmin.model.vo.TreeNodeMenu;
import com.ershijin.esjadmin.service.MenuService;
import com.ershijin.esjadmin.validation.groups.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menus")
public class MenuController {
    @Autowired
    MenuService menuService;

    @GetMapping
    @Log("菜单列表")
    @PreAuthorize("hasAuthority('menus:list')")
    List<Menu> listMenu() {
        return menuService.listAll();
    }

    @GetMapping("/tree")
    @PreAuthorize("hasAuthority(@config.GENERAL_PERMISSION)")
    List<TreeNodeMenu> menuTree() {
        return menuService.getMenuTree();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('menus:save')")
    @Log("创建菜单")
    void save(@RequestBody Menu menu) {
        menuService.save(menu);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('menus:update')")
    @Log("更新菜单")
    void update(@Validated({Update.class}) @RequestBody Menu menu) {
        menuService.update(menu);
    }

    /**
     * 启用菜单
     * @param id
     */
    @PostMapping("/{id}/enable")
    @PreAuthorize("hasAuthority('menus:enable')")
    @Log("启用菜单")
    void enable(@PathVariable Long id) {
        menuService.enableById(id);
    }

    /**
     * 禁用菜单
     * @param id
     */
    @PostMapping("/{id}/disable")
    @PreAuthorize("hasAuthority('menus:disable')")
    @Log("禁用菜单")
    void disable(@PathVariable Long id) {
        menuService.disableById(id);
    }

    /**
     * 删除菜单
     * @param id
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('menus:remove')")
    @Log("删除菜单")
    void remove(@PathVariable Long id) {
        menuService.removeById(id);
    }
}
