package com.zyx.manager.page;


import org.springframework.stereotype.Component;
/*
* 分页内容类封装
* */
@Component
public class Page {
    private int page;//当前页数
    private int rows;//数据库查询条数，即每页多少
    private int offset;//数据库查询的偏移量

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getOffset() {
        this.offset = (page-1)*rows;
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
