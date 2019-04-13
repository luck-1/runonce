package com.runonce.httpbean.assets;

import lombok.Data;

/**
 * @author swq
 * @date 2018/10/29 0029
 * @description
 */
@Data
public class PageBean {

    /**
     * 当前页
     */
    private int currentPage;

    /**
     * 分页大小
     */
    private int pageSize;


}
