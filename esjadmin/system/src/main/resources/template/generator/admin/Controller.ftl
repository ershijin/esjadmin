package ${package}.controller;

import com.ershijin.model.Page;
import com.ershijin.annotation.Log;
import com.ershijin.model.PageResult;
import ${package}.model.entity.${className};
import ${package}.model.dto.${className}DTO;
import ${package}.service.${className}Service;
import ${package}.model.query.${className}Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author ${author}
* @date ${date}
**/
@RestController
@Api(tags = "${apiAlias}管理")
@RequestMapping("/${changeClassName}")
public class ${className}Controller {

    @Autowired
    private ${className}Service ${changeClassName}Service;

    @GetMapping
    @Log("查询${apiAlias}")
    @ApiOperation("查询${apiAlias}")
    @PreAuthorize("hasAuthority('${changeClassName}:list')")
    public PageResult list(${className}Query query, Page page){
        return ${changeClassName}Service.list(query,page);
    }

    @GetMapping("/{id}")
    @Log("查看${apiAlias}")
    @ApiOperation("查看${apiAlias}")
    @PreAuthorize("hasAuthority('${changeClassName}:list')")
    public ${className}DTO get(@PathVariable Long id) {
        return ${changeClassName}Service.get(id);
    }

    @PostMapping
    @Log("新增${apiAlias}")
    @ApiOperation("新增${apiAlias}")
    @PreAuthorize("hasAuthority('${changeClassName}:save')")
    public void save(@Validated @RequestBody ${className} resources){
        ${changeClassName}Service.save(resources);
    }

    @PutMapping
    @Log("修改${apiAlias}")
    @ApiOperation("修改${apiAlias}")
    @PreAuthorize("hasAuthority('${changeClassName}:update')")
    public void update(@Validated @RequestBody ${className} resources){
        ${changeClassName}Service.update(resources);
    }

    @Log("删除${apiAlias}")
    @ApiOperation("删除${apiAlias}")
    @PreAuthorize("hasAuthority('${changeClassName}:remove')")
    @DeleteMapping
    public void remove(@RequestBody ${pkColumnType}[] ids) {
        ${changeClassName}Service.removeAll(ids);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("hasAuthority('${changeClassName}:list')")
    public void download(HttpServletResponse response, ${className}Query query) throws IOException {
        ${changeClassName}Service.download(${changeClassName}Service.list(query), response);
    }
}