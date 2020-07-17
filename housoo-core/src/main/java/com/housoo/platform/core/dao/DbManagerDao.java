package com.housoo.platform.core.dao;

import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author housoo
 * @created 2017年1月25日 上午9:24:44
 */
public interface DbManagerDao extends BaseDao {
    /**
     * 判断一张表是否存在数据库中
     *
     * @param tableName:表名称
     * @param isIncludeThisTable:判断是否包含该传入参数表
     * @return
     */
    public boolean isExistsTable(String tableName, boolean isIncludeThisTable);

    /**
     * 判断一个视图是否存在数据库中
     *
     * @param viewName
     * @param isIncludeThisView
     * @return
     */
    public boolean isExistsView(String viewName, boolean isIncludeThisView);

    /**
     * 获取oracle的约束类型
     *
     * @param tableName
     * @param columnName
     * @return
     */
    public String getOracleConstraintType(String tableName, String columnName);

    /**
     * 获取oracle的表约束
     *
     * @param tableName
     * @return
     */
    public List<Map<String, Object>> findOracleConstraints(String tableName);

    /**
     * 根据列名称和表获取这个列的注释
     *
     * @param tableNames
     * @param colName
     * @return
     */
    public String getColumnComments(List<String> tableNames, String colName);
}
