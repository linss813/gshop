package com.linss.gshop.util;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private long total;
    private List<T> list;
    private int pageNum;
    private int pageSize;
}