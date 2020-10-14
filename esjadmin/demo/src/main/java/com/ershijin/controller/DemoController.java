package com.ershijin.controller;

import com.ershijin.model.Page;
import com.ershijin.annotation.Log;
import com.ershijin.model.PageResult;
import com.ershijin.model.entity.Demo;
import com.ershijin.model.dto.DemoDTO;
import com.ershijin.service.DemoService;
import com.ershijin.model.query.DemoQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author ershijin
* @date 2020-10-15
**/
@RestController
@Api(tags = "demo管理")
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping
    @Log("查询demo")
    @ApiOperation("查询demo")
    @PreAuthorize("hasAuthority('demo:list')")
    public PageResult list(DemoQuery query, Page page){
        return demoService.list(query,page);
    }

    @GetMapping("/{id}")
    @Log("查看demo")
    @ApiOperation("查看demo")
    @PreAuthorize("hasAuthority('demo:list')")
    public DemoDTO get(@PathVariable Long id) {
        return demoService.get(id);
    }

    @PostMapping
    @Log("新增demo")
    @ApiOperation("新增demo")
    @PreAuthorize("hasAuthority('demo:save')")
    public void save(@Validated @RequestBody Demo resources){
        demoService.save(resources);
    }

    @PutMapping
    @Log("修改demo")
    @ApiOperation("修改demo")
    @PreAuthorize("hasAuthority('demo:update')")
    public void update(@Validated @RequestBody Demo resources){
        demoService.update(resources);
    }

    @Log("删除demo")
    @ApiOperation("删除demo")
    @PreAuthorize("hasAuthority('demo:remove')")
    @DeleteMapping
    public void remove(@RequestBody Long[] ids) {
        demoService.removeAll(ids);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("hasAuthority('demo:list')")
    public void download(HttpServletResponse response, DemoQuery query) throws IOException {
        demoService.download(demoService.list(query), response);
    }
}