package com.housoo.platform.core.model;

import java.io.Serializable;

/**
 * 描述 数据库表信息
 *
 * @author housoo
 * @created 2017年1月25日 上午8:53:51
 */
public class TableInfo implements Serializable {
    /**
     * 数据库表名称
     */
    private String tableName;
    /**
     * 数据库表注释
     */
    private String tableComments;

    /**
     * 空构造器
     */
    public TableInfo() {

    }

    /**
     * 带参数的构造器
     *
     * @param tableName
     * @param tableComments
     */
    public TableInfo(String tableName, String tableComments) {
        this.tableName = tableName;
        this.tableComments = tableComments;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the tableComments
     */
    public String getTableComments() {
        return tableComments;
    }

    /**
     * @param tableComments the tableComments to set
     */
    public void setTableComments(String tableComments) {
        this.tableComments = tableComments;
    }
}
