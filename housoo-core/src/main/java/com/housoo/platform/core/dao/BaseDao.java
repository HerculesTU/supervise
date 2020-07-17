package com.housoo.platform.core.dao;

import com.housoo.platform.core.model.PagingBean;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.model.TableColumn;
import com.housoo.platform.core.model.TableInfo;

import java.util.List;
import java.util.Map;

/**
 * 描述 基础接口,封装大部分数据库操作接口
 *
 * @author housoo
 * @created 2017年1月22日 上午9:43:51
 */
public interface BaseDao {
    /**
     * 描述 根据sql语句和查询参数获取单条数据库信息
     *
     * @param sql
     * @param params
     * @return
     * @例子: Map<String   ,   Object> map = dao.getBySql("SELECT * FROM T_SYSTEM_USER U WHERE U.USERID=?",new Object[]{"1"});
     * @created 2016年2月20日 上午9:32:12
     */
    public Map<String, Object> getBySql(String sql, Object[] params);

    /**
     * 描述 根据表名、列名、列的值获取数据库单条记录
     *
     * @param tableName
     * @param colNames
     * @param colValues
     * @return
     * @例子 Map<String   ,   Object> map = user2Service.getRecord("JBPM6_TASK",
     * new String[]{"TASK_ID"},new Object[]{"402881c352ee10e20152ee391cea001b"});
     * @created 2016年2月20日 上午11:55:59
     */
    public Map<String, Object> getRecord(String tableName, String[] colNames, Object[] colValues);

    /**
     * 描述 根据数据库表名称获取主键的名称
     *
     * @param tableName
     * @return
     * @created 2016年2月20日 下午1:04:29
     */
    public List<String> findPrimaryKeyNames(String tableName);

    /**
     * 描述 新增或者修改单表记录,单表必须存在主键,如果表记录不存在则进行新增操作,存在进行修改操作
     *
     * @param tableName:表名称
     * @param colValues:列的MAP
     * @param idGenerator                                主键生成策略,可传值说明
     *                                                   1:使用UUID 2:自增长(面向MYSQL数据库) 3:分配模式 4:序列(面向ORACLE数据库)
     * @param seqName:只针对oracle的序列存储,为序列名称,并且主键生成方式为序列模式
     * @return
     * @created 2016年2月21日 下午6:14:52
     */
    public Map<String, Object> saveOrUpdate(String tableName, Map<String, Object> colValues,
                                            int idGenerator, String seqName);

    /**
     * 描述 判断数据库中是否存在记录
     *
     * @param tableName
     * @param colValues
     * @return
     * @created 2016年2月20日 下午1:53:42
     */
    public boolean isExists(String tableName, Map<String, Object> colValues);

    /**
     * 描述 根据表名称获取列的名称列表
     *
     * @param tableName
     * @return
     * @created 2016年2月21日 下午4:37:23
     */
    public List<String> findColumnName(String tableName);

    /**
     * 描述 删除单表数据库记录信息
     *
     * @param tableName:表名称
     * @param colNames:列名数组
     * @param colValues:列值
     * @created 2016年2月22日 上午9:15:04
     */
    public void deleteRecord(String tableName, String[] colNames, Object[] colValues);

    /**
     * 描述 根据sql语句获取记录列表
     *
     * @param sql:sql语句
     * @param colValues:查询的值参数
     * @param pb:分页对象,如果不传,那么不进行分页
     * @return
     * @created 2016年2月22日 上午9:37:35
     */
    public List<Map<String, Object>> findBySql(String sql, Object[] colValues, PagingBean pb);

    /**
     * 描述 保存树形结构表数据
     *
     * @param tableName
     * @param colValues
     * @param idGenerator
     * @param seqName
     * @return
     * @created 2016年2月27日 上午11:32:44
     */
    public Map<String, Object> saveOrUpdateTreeData(String tableName,
                                                    Map<String, Object> colValues, int idGenerator, String seqName);

    /**
     * 描述 获取树形结构表的最大排序值
     *
     * @param tableName
     * @return
     * @created 2016年2月27日 上午11:35:41
     */
    public int getMaxTreeSortSn(String tableName, String treeSnName);

    /**
     * 获取数据库表信息列表
     *
     * @return
     */
    public List<TableInfo> findDbTables();

    /**
     * 把传递进来的SQL语句进行重新构建 构建成查询的SQL语句
     *
     * @param sqlFilter :sql过滤器
     * @param oldSql    :原本的SQL语句
     * @param params    :查询参数
     * @return
     */
    public String getQuerySql(SqlFilter sqlFilter, String oldSql, List<Object> params);

    /**
     * 执行sql语句
     *
     * @param sql:sql语句
     * @param params:查询参数
     */
    public void executeSql(String sql, Object[] params);

    /**
     * 获取树形的JSON数据
     *
     * @param params
     * @return
     */
    public String getTreeJson(Map<String, Object> params);

    /**
     * 获取树形的数据
     *
     * @param params
     * @return
     */
    public Object getTreeData(Map<String, Object> params);

    /**
     * 更新树形排序字段
     *
     * @param tableName
     * @param dragTreeNodeId
     * @param dragTreeNodeNewLevel
     * @param targetNodeId
     * @param targetNodeLevel
     */
    public void updateTreeSn(String tableName, String dragTreeNodeId, int dragTreeNodeNewLevel, String targetNodeId,
                             int targetNodeLevel, String moveType);

    /**
     * 根据表名称和字段名称删除记录
     *
     * @param tableName
     * @param fieldName
     * @param colValues
     */
    public void deleteRecords(String tableName, String fieldName, String[] colValues);

    /**
     * 根据表名称和字段名称获取注释
     *
     * @param tableName
     * @param colName
     * @return
     */
    public String getColComment(String tableName, String colName);

    /**
     * 根据SQL语句获取查询列名称集合
     *
     * @param sql
     * @return
     */
    public List<String> findQueryColNamesBySql(String sql);

    /**
     * 根据表名称获取数据库列的集合列表
     *
     * @param tableName
     * @return
     */
    public List<TableColumn> findTableColumnByTableName(String tableName);

    /**
     * 获取oracle的列信息集合
     *
     * @param tableName
     * @return
     */
    public List<Map<String, Object>> findOracleTableColumns(String tableName, String columnName);

    /**
     * @param map
     * @return
     */
    public TableColumn getOracleTableColumn(Map<String, Object> map);

    /**
     * 描述 批量插入数据
     *
     * @param datas
     * @param tableName
     * @created 2016年5月25日 上午11:23:43
     */
    public void saveBatch(final List<Map<String, Object>> datas, String tableName);

    /**
     * 获取拷贝表数据的SQL
     *
     * @param tableName
     * @param replaceColumn
     * @return
     */
    public String getCopyTableSql(String tableName, Map<String, String> replaceColumn);

    /**
     * 根据SQL语句获取唯一对象
     *
     * @param sql
     * @param classType
     * @return
     */
    public <T> T getUniqueObj(String sql, Object[] params, Class<T> classType);

    /**
     * 获取int类型的值
     *
     * @param sql
     * @param params
     * @return
     */
    public int getIntBySql(String sql, Object[] params);

    /**
     * 根据表名获取列的名称和注释
     *
     * @param tableName
     * @return
     */
    public Map<String, String> getTableColumnComment(String tableName);

    /**
     * 获取MySQL的列信息集合
     *
     * @param tableName
     * @return
     */
    public List<Map<String, Object>> findMySqlTableColumns(String tableName, String columnName);

    /**
     * 获取sqlServer的列信息集合
     *
     * @param tableName
     * @param columnName
     * @return
     */
    public List<Map<String, Object>> findSqlServerTableColumns(String tableName, String columnName);

    /**
     * 获取MySQL的表格列字段信息
     *
     * @param map
     * @return
     */
    public TableColumn getMySqlTableColumn(Map<String, Object> map);

    /**
     * 获取SqlServer的表格列字段信息
     *
     * @param map
     * @return
     */
    public TableColumn getSqlServerTableColumn(Map<String, Object> map);

    /**
     * 获取表字段记录,按拼接返回结果
     *
     * @param tableName        表名
     * @param targetFieldName  目标返回字段
     * @param queryFieldNames  查询参数字段名
     * @param queryFieldValues 查询参数字段值
     * @param splitSymbol      分割符
     * @return
     */
    public String getTableFieldValues(String tableName, String targetFieldName,
                                      String[] queryFieldNames, Object[] queryFieldValues, String splitSymbol);

    /**
     * 获取非键值对的结果列表
     *
     * @param sql
     * @param colValues
     * @param pb
     * @return
     */
    public List<List<String>> findListBySql(String sql, Object[] colValues, PagingBean pb);
}
