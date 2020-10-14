package ${package}.model.query;

import com.ershijin.annotation.Query;
import lombok.Data;
<#if queryHasTimestamp>
import java.time.LocalDateTime;
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

<#if column.queryType = '='>
    /** 精确 */
    @Query(tableField = "${column.columnName}")
    private ${column.columnType} ${column.changeColumnName};
</#if>
<#if column.queryType = 'Like'>
    /** 模糊 */
    @Query(type = Query.Type.LIKE, tableField = "${column.columnName}")
    private ${column.columnType} ${column.changeColumnName};
</#if>
<#if column.queryType = '!='>
    /** 不等于 */
    @Query(type = Query.Type.NE, tableField = "${column.columnName}")
    private ${column.columnType} ${column.changeColumnName};
</#if>
<#if column.queryType = 'NotNull'>
    /** 不为空 */
    @Query(type = Query.Type.IS_NOT_NULL, tableField = "${column.columnName}")
    private ${column.columnType} ${column.changeColumnName};
</#if>
<#if column.queryType = '>='>
    /** 大于等于 */
    @Query(type = Query.Type.GE, tableField = "${column.columnName}")
    private ${column.columnType} ${column.changeColumnName};
</#if>
<#if column.queryType = '<='>
    /** 小于等于 */
    @Query(type = Query.Type.LE, tableField = "${column.columnName}")
    private ${column.columnType} ${column.changeColumnName};
</#if>
    </#list>
</#if>

<#if betweens??>
    <#list betweens as column>
    /** BETWEEN */
    @Query(type = Query.Type.BETWEEN, tableField = "${column.columnName}")
    private List<${column.columnType}> createTime;
    </#list>
</#if>
}