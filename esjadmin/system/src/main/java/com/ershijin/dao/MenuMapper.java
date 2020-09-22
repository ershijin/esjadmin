package com.ershijin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ershijin.model.entity.Menu;
import com.ershijin.model.entity.Role;
import com.ershijin.model.dto.TreeNodeMenuDTO;

import java.util.List;
import java.util.Set;

public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> listAll();

    List<Menu> listByParentId(Integer parentId);

//    void save(Menu menu);

    /**
     * 获取所有的树节点形式菜单
     * @return
     */
    List<TreeNodeMenuDTO> listTreeNodeMenu();

    /**
     * 查询角色用后的菜单权限列表
     * @param roles
     * @return
     */
    List<TreeNodeMenuDTO> listTreeNodeMenuByRoles(List<Role> roles);

    /**
     * 启用菜单
     * @param menu
     */
    void enable(Menu menu);

    /**
     * 禁用菜单
     * @param menu
     */
    void disable(Menu menu);

    void removeByIds(Set<Long> ids);
}
