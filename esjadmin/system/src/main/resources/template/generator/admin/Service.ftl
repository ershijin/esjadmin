package ${package}.service;

import com.ershijin.exception.NotFoundException;
import com.ershijin.util.MyBeanUtils;
import com.ershijin.model.PageResult;
import com.ershijin.util.FileUtils;
import ${package}.model.entity.${className};
<#if columns??>
    <#list columns as column>
        <#if column.columnKey = 'UNI'>
            <#if column_index = 1>
import me.zhengjie.exception.EntityExistException;
            </#if>
        </#if>
    </#list>
</#if>
import ${package}.model.vo.${className}VO;
import ${package}.model.query.${className}Query;
import ${package}.dao.${className}Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
<#if !auto && pkColumnType = 'Long'>
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
</#if>
<#if !auto && pkColumnType = 'String'>
import cn.hutool.core.util.IdUtil;
</#if>
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
    * 查询数据分页
    * @param query 条件
    * @param page 分页参数
    * @return Map<String,Object>
    */
    public PageResult list(${className}Query query, Page<${className}> page){
        QueryWrapper<${className}> queryWrapper = new QueryWrapper<>();
        IPage<${className}> result = ${changeClassName}Mapper.selectPage(page, queryWrapper);
        return new PageResult(result.getTotal(), MyBeanUtils.convert(result.getRecords(), ${className}VO.class));
    }

    /**
    * 查询所有数据不分页
    * @param query 条件参数
    * @return List<${className}VO>
    */
    public List<${className}VO> list(${className}Query query){
        QueryWrapper<${className}> queryWrapper = new QueryWrapper<>();
        return MyBeanUtils.convert(${changeClassName}Mapper.selectList(queryWrapper), ${className}VO.class);
    }

    /**
       * 根据ID查询
       * @param ${pkChangeColName} ID
       * @return ${className}VO
       */
    @Transactional
    public ${className}VO findById(${pkColumnType} ${pkChangeColName}) {
        ${className} ${changeClassName} = ${changeClassName}Mapper.selectById(${pkChangeColName});
        if (${changeClassName} == null) {
            throw new NotFoundException("${className} 不存在");
        }
        ${className}VO ${changeClassName}VO = new ${className}VO();
        return (${className}VO) MyBeanUtils.convert(${changeClassName}, ${className}VO.class);
    }

    /**
    * 创建
    * @param resources /
    * @return ${className}VO
    */
    @Transactional(rollbackFor = Exception.class)
    public void save(${className} resources) {
<#if !auto && pkColumnType = 'Long'>
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.set${pkCapitalColName}(snowflake.nextId()); 
</#if>
<#if !auto && pkColumnType = 'String'>
        resources.set${pkCapitalColName}(IdUtil.simpleUUID()); 
</#if>
<#if columns??>
    <#list columns as column>
    <#if column.columnKey = 'UNI'>
        if(${changeClassName}Repository.findBy${column.capitalColumnName}(resources.get${column.capitalColumnName}()) != null){
            throw new EntityExistException(${className}.class,"${column.columnName}",resources.get${column.capitalColumnName}());
        }
    </#if>
    </#list>
</#if>
        ${changeClassName}Mapper.insert(resources);
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
    public void download(List<${className}VO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (${className}VO ${changeClassName} : all) {
            Map<String,Object> map = new LinkedHashMap<>();
        <#list columns as column>
            <#if column.columnKey != 'PRI'>
            <#if column.remark != ''>
            map.put("${column.remark}", ${changeClassName}.get${column.capitalColumnName}());
            <#else>
            map.put(" ${column.changeColumnName}",  ${changeClassName}.get${column.capitalColumnName}());
            </#if>
            </#if>
        </#list>
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }
}