package com.ershijin.controller;

import com.ershijin.annotation.NoApiResult;
import com.ershijin.exception.ApiException;
import com.ershijin.model.entity.ColumnInfo;
import com.ershijin.service.GenConfigService;
import com.ershijin.service.GeneratorServiceImpl;
import com.ershijin.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/generator")
// 系统：代码生成管理
public class GeneratorController {

    private final GeneratorServiceImpl generatorService;
    private final GenConfigService genConfigService;

    @Value("${generator.enabled}")
    private Boolean generatorEnabled;

    // 查询数据库数据
    @GetMapping(value = "/tables/all")
    public Object queryTables(){
        return generatorService.getTables();
    }

    // 查询数据库数据
    @GetMapping(value = "/tables")
    public Object queryTables(@RequestParam(defaultValue = "") String name,
                                    @RequestParam(defaultValue = "0")Integer page,
                                    @RequestParam(defaultValue = "10")Integer size){
        int[] startEnd = PageUtil.transToStartEnd(page, size);
        return generatorService.getTables(name,startEnd);
    }

    // 查询字段数据
    @GetMapping(value = "/columns")
    public Object queryColumns(@RequestParam String tableName){
        List<ColumnInfo> columnInfos = generatorService.getColumns(tableName);
        return PageUtil.toPage(columnInfos,columnInfos.size());
    }

    // 保存字段数据
    @PutMapping
    public void save(@RequestBody List<ColumnInfo> columnInfos){
        generatorService.save(columnInfos);
    }

    // 同步字段数据
    @PostMapping(value = "sync")
    public void sync(@RequestBody List<String> tables){
        for (String table : tables) {
            generatorService.sync(generatorService.getColumns(table), generatorService.query(table));
        }
    }

    // 生成代码
    @PostMapping(value = "/{tableName}/generate")
    public void generate(@PathVariable String tableName){
        if(!generatorEnabled){
            throw new ApiException("此环境不允许生成代码，请选择预览或者下载查看！");
        }

        generatorService.generator(genConfigService.find(tableName), generatorService.getColumns(tableName));

    }
    // 预览
    @PostMapping(value = "/{tableName}/preview")
    public Object preview(@PathVariable String tableName){
        return generatorService.preview(genConfigService.find(tableName), generatorService.getColumns(tableName));
    }

    // 下载代码
    @PostMapping(value = "/{tableName}/download")
    public void download(@PathVariable String tableName, HttpServletRequest request,
                           HttpServletResponse response){
        generatorService.download(genConfigService.find(tableName), generatorService.getColumns(tableName), request, response);
    }

}
