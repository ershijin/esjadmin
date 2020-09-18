package com.ershijin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ershijin.annotation.Log;
import com.ershijin.constant.ResultCode;
import com.ershijin.exception.ApiException;
import com.ershijin.model.PageResult;
import com.ershijin.model.query.DictQuery;
import com.ershijin.service.DictService;
import com.ershijin.validation.groups.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
* 系统：字典管理
*/
@RestController
@RequestMapping("/dict")
public class DictController {
    @Autowired
    private DictService dictService;
    private static final String ENTITY_NAME = "dict";

    @Log("导出字典数据")
    // 导出字典数据
    @GetMapping(value = "/download")
    @PreAuthorize("hasAuthority('dict:list')")
    public void download(HttpServletResponse response, DictQuery query) throws IOException {
        dictService.download(dictService.queryAll(query), response);
    }

    @Log("查询字典")
    // 查询字典
    @GetMapping(value = "/all")
    @PreAuthorize("hasAuthority('dict:list')")
    public List<com.ershijin.model.entity.Dict> queryAll(){
        return dictService.queryAll(new DictQuery());
    }

    @Log("查询字典")
    // 查询字典
    @GetMapping
    @PreAuthorize("hasAuthority('dict:list')")
    public PageResult query(DictQuery query, Page pageable){
        return dictService.queryAll(query,pageable);
    }

    @Log("新增字典")
    // 新增字典
    @PostMapping
    @PreAuthorize("hasAuthority('dict:add')")
    public void create(@Validated @RequestBody com.ershijin.model.entity.Dict resources){
        if (resources.getId() != null) {
            throw new ApiException("A new "+ ENTITY_NAME +" cannot already have an ID", ResultCode.ARGUMENT_NOT_VALID);
        }
        dictService.create(resources);
    }

    @Log("修改字典")
    // 修改字典
    @PutMapping
    @PreAuthorize("hasAuthority('dict:edit')")
    public void update(@Validated(Update.class) @RequestBody com.ershijin.model.entity.Dict resources){
        dictService.update(resources);
    }

    @Log("删除字典")
    // 删除字典
    @DeleteMapping
    @PreAuthorize("hasAuthority('dict:del')")
    public void delete(@RequestBody Set<Long> ids){
        dictService.delete(ids);
    }
}