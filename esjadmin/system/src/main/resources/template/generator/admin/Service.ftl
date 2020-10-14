package ${package}.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ershijin.exception.NotFoundException;
import com.ershijin.model.PageResult;
import com.ershijin.util.FileUtils;
import com.ershijin.util.QueryHelp;
import ${package}.model.entity.${className};

<#if columns??>
    <#assign uni_imported=false/>
    <#list columns as column>
        <#if column.columnKey = 'UNI'>
            <#if !uni_imported>
import com.ershijin.exception.ApiException;
import com.ershijin.constant.ResultCode;
                <#assign uni_imported=true/>
            </#if>
        </#if>
    </#list>
</#if>
import ${package}.model.dto.${className}DTO;
import ${package}.model.query.${className}Query;
import ${package}.converter.${className}Converter;
import ${package}.dao.${className}Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @description 服务实现
* @author ${author}
* @date ${date}
**/
@Service
public class ${className}Service {
    
    @Autowired
    private ${className}Mapper ${changeClassName}Mapper;

    @Autowired
    private ${className}Converter ${changeClassName}Converter;

    /**
    * 查询数据分页
    * @param query 条件
    * @param page 分页参数
    * @return Map<String,Object>
    */
    public PageResult list(${className}Query query, IPage<${className}> page){
        QueryWrapper<${className}> queryWrapper = QueryHelp.buildQueryWrapper(query);
        queryWrapper.orderByDesc("${pkColName}");
        IPage<${className}> result = ${changeClassName}Mapper.selectPage(page,  queryWrapper);
        return new PageResult(result.getTotal(), ${changeClassName}Converter.toDto(result.getRecords()));
    }

    /**
    * 查询所有数据不分页
    * @param query 条件参数
    * @return List<${className}DTO>
    */
    public List<${className}DTO> list(${className}Query query){
        QueryWrapper<${className}> queryWrapper = QueryHelp.buildQueryWrapper(query);
        queryWrapper.orderByDesc("${pkColName}");
        return ${changeClassName}Converter.toDto(${changeClassName}Mapper.selectList(queryWrapper));
    }

    /**
       * 根据ID查询
       * @param ${pkChangeColName} ID
       * @return ${className}VO
       */
    @Transactional
    public ${className}DTO get(${pkColumnType} ${pkChangeColName}) {
        ${className} ${changeClassName} = ${changeClassName}Mapper.selectById(${pkChangeColName});
        if (${changeClassName} == null) {
            throw new NotFoundException("${className} 不存在");
        }
        return ${changeClassName}Converter.toDto(${changeClassName});
    }

    /**
    * 创建
    * @param resources /
    * @return ${className}DTO
    */
    @Transactional(rollbackFor = Exception.class)
    public ${className}DTO save(${className} resources) {
<#if columns??>
    <#list columns as column>
    <#if column.columnKey = 'UNI'>
        if(${changeClassName}Mapper.selectOne(Wrappers.<${className}>lambdaQuery().eq(${className}::get${column
        .capitalColumnName}, resources.get${column.capitalColumnName}())) !=
        null){
            throw new ApiException("${className} 列 ${column.columnName} 值 " + resources.get${column
            .capitalColumnName}() + " 已经存在", ResultCode.ENTITY_EXISTS);
        }
    </#if>
    </#list>
</#if>
        ${changeClassName}Mapper.insert(resources);
        return ${changeClassName}Converter.toDto(resources);
    }

    /**
    * 编辑
    * @param ${changeClassName} /
    */
    @Transactional(rollbackFor = Exception.class)
    public void update(${className} ${changeClassName}) {
        ${changeClassName}Mapper.updateById(${changeClassName});
    }

    /**
    * 多选删除
    * @param ids /
    */
    public void removeAll(${pkColumnType}[] ids) {
        for (${pkColumnType} ${pkChangeColName} : ids) {
            ${changeClassName}Mapper.deleteById(${pkChangeColName});
        }
    }

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    public void download(List<${className}DTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (${className}DTO ${changeClassName} : all) {
            Map<String,Object> map = new LinkedHashMap<>();
        <#list columns as column>
            <#if column.remark != ''>
            map.put("${column.remark}", ${changeClassName}.get${column.capitalColumnName}());
            <#else>
            map.put(" ${column.changeColumnName}",  ${changeClassName}.get${column.capitalColumnName}());
            </#if>
        </#list>
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }
}