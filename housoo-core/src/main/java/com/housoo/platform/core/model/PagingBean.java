package com.housoo.platform.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述 分页对象
 *
 * @author housoo
 * @version 1.0
 * @created 2016年2月22日 上午9:26:26
 */
public class PagingBean implements Serializable {
    /**
     * 缺省的分页数量
     */
    public static final Integer DEFAULT_PAGE_SIZE = 30;
    /**
     * 每页开始的索引号
     */
    public Integer start;

    /**
     * 页码大小
     */
    private Integer pageSize;

    /**
     * 总记录数
     */
    private Integer totalItems;
    /**
     * 当前页码
     */
    private Integer currentPage;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     *
     */
    private List pageItems = new ArrayList();

    /**
     * 描述
     *
     * @param start
     * @param limit
     * @created 2014年9月6日 上午7:20:32
     */
    public PagingBean(int start, int limit) {
        this.pageSize = limit;
        this.start = start;
    }

    /**
     * @return the pageItems
     */
    public List getPageItems() {
        return pageItems;
    }

    /**
     * @param pageItems the pageItems to set
     */
    public void setPageItems(List pageItems) {
        this.pageItems = pageItems;
    }

    /**
     * 描述
     *
     * @return
     * @created 2014年9月6日 上午7:20:36
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 描述
     *
     * @param pageSize
     * @created 2014年9月6日 上午7:20:40
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 描述
     *
     * @return
     * @created 2014年9月6日 上午7:20:43
     */
    public int getTotalItems() {
        return totalItems;
    }

    /**
     * 描述
     *
     * @param totalItems
     * @created 2014年9月6日 上午7:20:46
     */
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;

    }

    /**
     * 描述
     *
     * @return
     * @created 2014年9月6日 上午7:20:57
     */
    public Integer getStart() {
        return start;
    }

    /**
     * 描述
     *
     * @param start
     * @created 2014年9月6日 上午7:21:06
     */
    public void setStart(Integer start) {
        this.start = start;
    }

    /**
     * @return the currentPage
     */
    public Integer getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage the currentPage to set
     */
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * @return the totalPage
     */
    public Integer getTotalPage() {
        return totalPage;
    }

    /**
     * @param totalPage the totalPage to set
     */
    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

}
