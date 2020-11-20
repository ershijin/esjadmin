package com.ershijin.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 分页返回的实体类
 */
public class PageResult<T> {
    @ApiModelProperty(example = "1")
    private long total;
    private List<T> rows;

    public PageResult(long total, List rows) {
        this.total = total;
        this.rows = rows;
    }
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

}
