package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.model.TableColumn;
import com.housoo.platform.core.model.TableInfo;

import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author 高飞
 * @created 2017年1月25日 上午9:30:10
 */
public interface DbManagerService extends BaseService {
    /**
     * 根据filter和配置信息获取数据列表
     *
     * @param filter
     * @param fieldInfo
     * @return
     */
    public List<Map<String, Object>> findList(SqlFilter filter, Map<String, Object> fieldInfo);

    /**
     * @param sqlFilter
     * @return
     */
    public List<TableInfo> findBySqlFilter(SqlFilter sqlFilter);

    /**
     * 根据表名称获取表信息
     *
     * @param tableName
     * @return
     */
    public TableInfo getTableInfo(String tableName);

    /**
     * 根据sqlfilter获取数据库的字段列表
     *
     * @param filter
     * @return
     */
    public List<Map> findTableColumnByFilter(SqlFilter filter);

    /**
     * 根据表名称和列名称获取列信息
     *
     * @param tableName
     * @param columnName
     * @return
     */
    public TableColumn getTableColumn(String tableName, String columnName);

    /**
     * 判断一张表是否存在数据库中
     *
     * @param tableName:表名称
     * @param isIncludeThisTable:判断是否包含该传入参数表
     * @return
     */
    public boolean isExistsTable(String tableName, boolean isIncludeThisTable);

    /**
     * 判断是否存在该列
     *
     * @param columnName:列名称
     * @param isIncludeThisColumnName:是否包含该列
     * @return
     */
    public boolean isExistsColumn(String columnName, boolean isIncludeThisColumnName);

    /**
     * 新增或者修改表结构
     *
     * @param isEdit
     * @param tableName
     * @param tableComments
     * @param tableColumns
     */
    public void saveOrUpdateTable(String isEdit, String tableName,
                                  String tableComments, List<TableColumn> tableColumns, String oldTableName);

    /**
     * 删除数据库表
     *
     * @param tableNames
     */
    public void deleteTable(String tableNames);

    /**
     * 获取所有的表
     *
     * @param queryParam
     * @return
     */
    public List<Map<String, Object>> findAllTables(String queryParam);

    /**
     * 根据表名称获取字段列表
     *
     * @param tableName
     * @return
     */
    public List<Map<String, Object>> findTableColumns(String tableName);

    /**
     * 根据sql语句获取字段列表
     *
     * @param sqlContent
     * @return
     */
    public List<Map<String, Object>> findFieldColumnsBySql(String sqlContent);

    /**
     * 根据接口获取字段列表
     *
     * @param fieldInfo
     * @param sqlFilter
     * @return
     */
    public List<Map<String, Object>> findFieldColumnsByInterface(Map<String, Object> fieldInfo,
                                                                 SqlFilter sqlFilter);

    /**
     * 判断一个视图是否存在数据库中
     *
     * @param viewName
     * @param isIncludeThisView
     * @return
     */
    public boolean isExistsView(String viewName, boolean isIncludeThisView);

    /**
     * 根据视图名称获取视图信息
     *
     * @param viewName
     * @return
     */
    public Map<String, Object> getViewInfo(String viewName);

    /**
     * 根据SQL语句获取查询了多少张表
     *
     * @param sql
     * @return
     */
    public List<String> getSelectTableNames(String sql);

    /**
     * 获取MYSQL数据库所有创建表相关语句
     *
     * @param tableName
     * @param tableComments
     * @param tableColumns
     * @return
     */
    public List<String> getMysqlCreateTableAllSql(String tableName, String tableComments
            , List<TableColumn> tableColumns);

    /**
     * 获取sqlServer数据库所有创建表语句
     *
     * @param tableName
     * @param tableComments
     * @param tableColumns
     * @return
     */
    public List<String> getSqlServerCreateTableAllSql(String tableName, String tableComments
            , List<TableColumn> tableColumns);

    /**
     * 创建MYSQL表
     *
     * @param sqlList
     */
    public void createMySqlTable(List<String> sqlList);

    /**
     * 创建SQLSERVER表
     *
     * @param sqlList
     */
    public void createSqlServerTable(List<String> sqlList);

    /**
     * 保存MYSQL的数据
     *
     * @param dataList
     */
    public void saveMySqlTableDatas(List<Map<String, Object>> dataList, String tableName);

    /**
     * 保存SqlServer的数据
     *
     * @param dataList
     */
    public void saveSqlServerTableDatas(List<Map<String, Object>> dataList, String tableName);
}
