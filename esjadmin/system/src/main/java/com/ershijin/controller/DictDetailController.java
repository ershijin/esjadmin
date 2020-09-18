package com.ershijin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ershijin.annotation.Log;
import com.ershijin.constant.ResultCode;
import com.ershijin.exception.ApiException;
import com.ershijin.model.PageResult;
import com.ershijin.model.entity.DictDetail;
import com.ershijin.model.query.DictDetailQuery;
import com.ershijin.service.DictDetailService;
import com.ershijin.validation.groups.Update;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dictDetail")
public class DictDetailController {

    private final DictDetailService dictDetailService;
    private static final String ENTITY_NAME = "dictDetail";

    @Log("查询字典详情")
    // 查询字典详情
    @GetMapping
    public PageResult query(DictDetailQuery query, Page page){
        return dictDetailService.queryAll(query, page);
    }

    @Log("查询多个字典详情")
    // 查询多个字典详情
    @GetMapping(value = "/map")
    public Map<String, List<DictDetail>> getDictDetailMaps(@RequestParam String dictName){
        String[] names = dictName.split("[,，]");
        Map<String, List<DictDetail>> dictMap = new HashMap<>(16);
        for (String name : names) {
            dictMap.put(name, dictDetailService.getDictByName(name));
        }
        return dictMap;
    }

    @Log("新增字典详情")
    // 新增字典详情
    @PostMapping
    @PreAuthorize("hasAuthority('dict:add')")
    public void save(@Validated @RequestBody DictDetail dictDetail){
        if (dictDetail.getId() != null) {
            throw new ApiException("A new "+ ENTITY_NAME +" cannot already have an ID", ResultCode.ARGUMENT_NOT_VALID);
        }
        dictDetailService.save(dictDetail);
    }

    @Log("修改字典详情")
    // 修改字典详情
    @PutMapping
    @PreAuthorize("hasAuthority('dict:edit')")
    public void update(@Validated(Update.class) @RequestBody DictDetail dictDetail){
        dictDetailService.update(dictDetail);
    }

    @Log("删除字典详情")
    // 删除字典详情
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('dict:del')")
    public void delete(@PathVariable Long id){
        dictDetailService.delete(id);
    }
}