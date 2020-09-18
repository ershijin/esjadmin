package ${package}.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
<#if isNotNullColumns??>
import javax.validation.constraints.*;
</#if>

<#if hasDateAnnotation>
import org.hibernate.annotations.*;
</#if>
<#if hasTimestamp>
import java.sql.Timestamp;
</#if>
<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>
import java.io.Serializable;

/**
* @description /
* @author ${author}
* @date ${date}
**/
@Data
@TableName("${tableName}")
public class ${className} implements Serializable {
<#if columns??>
    <#list columns as column>

    <#if column.columnKey = 'PRI'>
    @TableId
    </#if>
    @TableField(value = "${column.columnName}"<#if column.columnKey = 'UNI'>,unique = true</#if>)
    <#if column.istNotNull && column.columnKey != 'PRI'>
        <#if column.columnType = 'String'>
    @NotBlank
        <#else>
    @NotNull
        </#if>
    </#if>
    <#if (column.dateAnnotation)??>
    <#if column.dateAnnotation = 'CreationTimestamp'>
    @CreationTimestamp
    <#else>
    @UpdateTimestamp
    </#if>
    </#if>
    <#if column.remark != ''>
    @ApiModelProperty(value = "${column.remark}")
    <#else>
    @ApiModelProperty(value = "${column.changeColumnName}")
    </#if>
    private ${column.columnType} ${column.changeColumnName};
    </#list>
</#if>

}