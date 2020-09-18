package ${package}.model.query;

import lombok.Data;
<#if queryHasTimestamp>
import java.sql.Timestamp;
</#if>
<#if queryHasBigDecimal>
import java.math.BigDecimal;
</#if>
<#if betweens??>
import java.util.List;
</#if>

/**
* @author ${author}
* @date ${date}
**/
@Data
public class ${className}Query{
<#if queryColumns??>
    <#list queryColumns as column>

    private ${column.columnType} ${column.changeColumnName};
    </#list>
</#if>
}