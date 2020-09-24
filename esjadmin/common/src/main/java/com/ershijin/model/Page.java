package com.ershijin.model;

public class Page extends com.baomidou.mybatisplus.extension.plugins.pagination.Page {
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page setPage(long page) {
        return super.setCurrent(page);
    }

}
