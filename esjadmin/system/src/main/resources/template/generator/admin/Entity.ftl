package ${package}.model.entity;

import com.baomidou.mybatisplus.annotation.*;
<#if hasDateAnnotation>
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
</#if>

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
<#if isNotNullColumns??>
import javax.validation.constraints.*;
</#if>

<#if hasTimestamp>
import java.time.LocalDateTime;
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
    <#if column.istNotNull && column.columnKey != 'PRI'>
        <#if column.columnType = 'String'>
    @NotBlank(message="<#if column.remark?default("")?trim?length gt 1>${column.remark}<#else>${column
    .changeColumnName}</#if> 不能为空")
        <#else>
    @NotNull(message="<#if column.remark?default("")?trim?length gt 1>${column.remark}<#else>${column
                          .changeColumnName}</#if> 不能为空")
        </#if>
    </#if>
    <#if (column.dateAnnotation)??>
    <#if column.dateAnnotation = 'CreationTimestamp'>
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    <#elseif column.dateAnnotation = 'UpdateTimestamp'>
    @TableField(fill = FieldFill.UPDATE)
    </#if>
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
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