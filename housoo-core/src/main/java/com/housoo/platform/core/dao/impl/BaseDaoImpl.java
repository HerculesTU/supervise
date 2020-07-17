package com.housoo.platform.core.dao.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.PagingBean;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.model.TableColumn;
import com.housoo.platform.core.model.TableInfo;
import com.housoo.platform.core.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * 描述
 *
 * @author housoo
 * @created 2017年1月3日 上午10:32:04
 */
abstract public class BaseDaoImpl extends NamedParameterJdbcDaoSupport implements
        BaseDao {

    /**
     * 描述 根据sql语句和查询参数获取单条数据库信息
     *
     * @param sql
     * @param params
     * @return
     * @例子: Map<String   ,   Object> map = dao.getBySql("SELECT * FROM T_SYSTEM_USER U WHERE U.USERID=?",new Object[]{"1"});
     * @created 2016年2月20日 上午9:32:12
     */
    @Override
    public Map<String, Object> getBySql(String sql, Object[] params) {
        try {
            if (params != null && params.length > 0) {
                return this.getJdbcTemplate().queryForMap(sql, params);
            } else {
                return this.getJdbcTemplate().queryForMap(sql);
            }
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
            return null;
        }
    }

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
    @Override
    public Map<String, Object> getRecord(String tableName, String[] colNames, Object[] colValues) {
        StringBuffer sql = new StringBuffer("select * from ");
        sql.append(tableName).append(" BUS WHERE ");
        for (int i = 0; i < colNames.length; i++) {
            if (i > 0) {
                sql.append(" AND ");
            }
            sql.append("BUS.").append(colNames[i]).append("=? ");
        }
        StringBuffer countSql = new StringBuffer("SELECT COUNT(*) FROM (");
        countSql.append(sql).append(") ");
        String dbType = PlatAppUtil.getDbType();
        if ("MYSQL".equals(dbType) || "SQLSERVER".equals(dbType)) {
            countSql.append(" AS COUNT ");
        }
        int resultCount = this.getIntBySql(countSql.toString(), colValues);
        if (resultCount == 0) {
            return null;
        } else {
            try {
                return this.getJdbcTemplate().queryForMap(sql.toString(), colValues);
            } catch (Exception e) {
                PlatLogUtil.printStackTrace(e);
                return null;
            }
        }
    }

    /**
     * 描述 根据表名称获取主键名称,面向MYSQL数据库
     *
     * @param tableName
     * @return
     * @created 2016年2月20日 下午1:32:37
     */
    private List<String> findPrimaryKeyNamesForMySql(String tableName) {
        StringBuffer sql = new StringBuffer("select upper(T.COLUMN_NAME) from information_schema.columns");
        sql.append(" T where T.table_schema =?  and T.table_name = ? ");
        sql.append(" AND T.COLUMN_KEY='PRI' ORDER BY T.ORDINAL_POSITION ASC");
        //获取表的schema
        String schemaName = PlatAppUtil.getDbSchema();
        return this.getJdbcTemplate().queryForList(sql.toString(), new Object[]{schemaName, tableName}, String.class);
    }

    /**
     * 根据表名获取主键名称列表,面向sqlserver数据库
     *
     * @param tableName
     * @return
     */
    private List<String> findPrimaryKeyNamesForSqlServer(String tableName) {
        StringBuffer sql = new StringBuffer("SELECT col.name ");
        sql.append("FROM sys.indexes idx JOIN sys.index_columns idxCol ");
        sql.append("ON (idx.object_id = idxCol.object_id ");
        sql.append("AND idx.index_id = idxCol.index_id ");
        sql.append("AND idx.is_primary_key = 1)");
        sql.append(" JOIN sys.tables tab ON (idx.object_id = tab.object_id) ");
        sql.append("JOIN sys.columns col ON (idx.object_id = col.object_id");
        sql.append(" AND idxCol.column_id = col.column_id)");
        sql.append(" AND TAB.NAME=? ");
        return this.getJdbcTemplate().queryForList(sql.toString(), new Object[]{tableName}, String.class);
    }

    /**
     * 描述 根据表名称获取主键名称,面向ORACLE数据库
     *
     * @param tableName
     * @return
     * @created 2016年2月20日 下午1:32:37
     */
    private List<String> findPrimaryKeyNamesForOracle(String tableName) {
        Map<String, List<String>> keyHashMap = PlatAppUtil.getPrimaryKeyMap();
        if (keyHashMap.get(tableName) != null) {
            return keyHashMap.get(tableName);
        } else {
            StringBuffer sql = new StringBuffer(
                    "select cu.column_name from user_cons_columns cu")
                    .append(", user_constraints au where cu.constraint_name = au.constraint_name ")
                    .append("and au.constraint_type = 'P' and au.table_name=? ");
            List<String> primaryKeyNames = this.getJdbcTemplate().queryForList(
                    sql.toString(), String.class,
                    new Object[]{tableName.toUpperCase()});
            keyHashMap.put(tableName, primaryKeyNames);
            PlatAppUtil.setPrimaryKeyMap(keyHashMap);
            return primaryKeyNames;
        }

    }

    /**
     * 描述 根据数据库表名称获取主键的名称
     *
     * @param tableName
     * @return
     * @created 2016年2月20日 下午1:04:29
     */
    @Override
    public List<String> findPrimaryKeyNames(String tableName) {
        tableName = tableName.toUpperCase();
        //获取数据库类型
        String dbType = PlatAppUtil.getDbType();
        if ("MYSQL".equals(dbType)) {
            return this.findPrimaryKeyNamesForMySql(tableName);
        } else if ("ORACLE".equals(dbType)) {
            return this.findPrimaryKeyNamesForOracle(tableName);
        } else if ("SQLSERVER".equals(dbType)) {
            return this.findPrimaryKeyNamesForSqlServer(tableName);
        } else {
            return null;
        }
    }

    /**
     * 描述 判断数据库中是否存在记录
     *
     * @param tableName
     * @param colValues
     * @return
     * @created 2016年2月20日 下午1:53:42
     */
    @Override
    public boolean isExists(String tableName, Map<String, Object> colValues) {
        tableName = tableName.toUpperCase();
        //获取主键列表
        List<String> pkNames = this.findPrimaryKeyNames(tableName);
        //获取KEYS
        Set<String> colKeys = colValues.keySet();
        List<Object> params = new ArrayList<Object>();
        for (String colKey : colKeys) {
            if (pkNames.contains(colKey.toUpperCase())) {
                params.add(colValues.get(colKey));
            }
        }
        if (params.size() > 0) {
            StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
            sql.append(tableName).append(" T WHERE 1=1 ");
            for (String pkName : pkNames) {
                sql.append(" AND T.").append(pkName).append("=? ");
            }
            int count = this.getIntBySql(sql.toString(), params.toArray());
            if (count != 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 描述 更新数据
     *
     * @param tableName
     * @param colValues
     * @return
     * @created 2016年2月21日 下午4:28:52
     */
    private Map<String, Object> updateData(String tableName, Map<String, Object> colValues) {
        //获取列名称
        List<String> columnNames = this.findColumnName(tableName);
        //获取主键名称
        List<String> primaryKeyNames = this.findPrimaryKeyNames(tableName);
        StringBuffer sql = new StringBuffer("UPDATE ");
        sql.append(tableName).append(" SET ");
        // 设置更新目标列
        List<String> targetCols = new ArrayList<String>();
        // 定义更新的目标值
        List<Object> targetVals = new ArrayList<Object>();
        Iterator it = colValues.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            String fieldName = (String) entry.getKey();
            Object val = entry.getValue();
            if (columnNames.contains(fieldName.toUpperCase())
                    && !primaryKeyNames.contains(fieldName.toUpperCase())) {
                if (!targetCols.contains(fieldName.toUpperCase())) {
                    targetCols.add(fieldName.toUpperCase());
                    targetVals.add(val);
                }

            }
        }
        for (String targetCol : targetCols) {
            sql.append(targetCol).append("=?,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" WHERE ");
        for (int i = 0; i < primaryKeyNames.size(); i++) {
            if (i > 0) {
                sql.append(" AND ");
            }
            sql.append(primaryKeyNames.get(i)).append("=? ");
            targetVals.add(colValues.get(primaryKeyNames.get(i)));
        }
        if (targetCols.size() > 0) {
            this.getJdbcTemplate().update(sql.toString(), targetVals.toArray());
        }
        return colValues;
    }

    /**
     * 描述 根据表名称获取列的名称列表,面向MYSQL
     *
     * @param tableName
     * @return
     * @created 2016年2月21日 下午4:44:12
     */
    private List<String> findColumnNameForMySql(String tableName) {
        String schemaName = PlatAppUtil.getDbSchema();
        StringBuffer sql = new StringBuffer("SELECT upper(T.COLUMN_NAME) from information_schema.columns T");
        sql.append(" where T.table_schema =?  and T.table_name = ? ");
        sql.append(" ORDER BY T.ORDINAL_POSITION ASC");
        return this.getJdbcTemplate().queryForList(sql.toString(), new Object[]{schemaName, tableName}, String.class);
    }

    /**
     * 根据表名称获取列的名称列表,面向sqlServer
     *
     * @param tableName
     * @return
     */
    private List<String> findColumnNameForSqlServer(String tableName) {
        StringBuffer sql = new StringBuffer("select T.COLUMN_NAME ");
        sql.append(" from information_schema.columns T");
        sql.append(" WHERE T.TABLE_NAME=? ");
        return this.getJdbcTemplate().queryForList(sql.toString(), new Object[]{tableName}, String.class);
    }

    /**
     * 描述 根据表名称获取列的名称列表,面向ORACLE
     *
     * @param tableName
     * @return
     * @created 2016年2月21日 下午4:44:12
     */
    private List<String> findColumnNameForOracle(String tableName) {
        Map<String, List<String>> columnMap = PlatAppUtil.getTableColumnMap();
        if (columnMap.get(tableName) != null) {
            return columnMap.get(tableName);
        } else {
            StringBuffer sql = new StringBuffer("select t.COLUMN_NAME from ")
                    .append("user_tab_columns t where  t.table_name=? ");
            sql.append(" ORDER BY T.COLUMN_ID ASC");
            List<String> list = getJdbcTemplate().queryForList(sql.toString(),
                    new Object[]{tableName.toUpperCase()}, String.class);
            columnMap.put(tableName, list);
            return list;
        }

    }

    /**
     * 描述 根据表名称获取列的名称列表
     *
     * @param tableName
     * @return
     * @created 2016年2月21日 下午4:37:23
     */
    @Override
    public List<String> findColumnName(String tableName) {
        tableName = tableName.toUpperCase();
        //获取数据库的类型
        String dbType = PlatAppUtil.getDbType();
        if ("MYSQL".equals(dbType)) {
            return this.findColumnNameForMySql(tableName);
        } else if ("ORACLE".equals(dbType)) {
            return this.findColumnNameForOracle(tableName);
        } else if ("SQLSERVER".equals(dbType)) {
            return this.findColumnNameForSqlServer(tableName);
        } else {
            return null;
        }
    }

    /**
     * 描述 获取序列的下一个值,面向ORALCE
     *
     * @param seqName
     * @return
     * @created 2016年2月21日 下午5:22:37
     */
    public String getNextSeqValueForOracle(String seqName) {
        String getNextSeq = "select " + seqName + ".nextval FROM DUAL";
        return String.valueOf(this.getIntBySql(getNextSeq, null));
    }

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
    @Override
    public Map<String, Object> saveOrUpdate(String tableName, Map<String, Object> colValues,
                                            int idGenerator, String seqName) {
        tableName = tableName.toUpperCase();
        boolean exist = this.isExists(tableName, colValues);
        //如果数据存在,那么执行更新操作
        if (exist) {
            return this.updateData(tableName, colValues);
        } else {
            // 定义目标columns
            final List<String> targetCols = new ArrayList<String>();
            // 定义目标值
            final List<Object> targetVal = new ArrayList<Object>();
            //获取列名称
            List<String> columNames = this.findColumnName(tableName);
            //获取主键列表
            List<String> primaryKeyNames = this.findPrimaryKeyNames(tableName);
            //定义生成的主键值MAP
            Map<String, Object> genPrimaryKeyMap = new HashMap<String, Object>();
            final StringBuffer sql = new StringBuffer("INSERT INTO ");
            sql.append(tableName).append("(");
            for (String pkName : primaryKeyNames) {
                if (idGenerator != SysConstants.ID_GENERATOR_AUTOINCREMENT) {
                    targetCols.add(pkName);
                }
                switch (idGenerator) {
                    case SysConstants.ID_GENERATOR_SEQ:
                        //定义单个主键的值
                        String singlePrimaryKeyValue = this.getNextSeqValueForOracle(seqName);
                        targetVal.add(singlePrimaryKeyValue);
                        genPrimaryKeyMap.put(pkName, singlePrimaryKeyValue);
                        break;
                    case SysConstants.ID_GENERATOR_UUID:
                        String uuIdValue = UUIDGenerator.getUUID();
                        targetVal.add(uuIdValue);
                        genPrimaryKeyMap.put(pkName, uuIdValue);
                        break;
                    case SysConstants.ID_GENERATOR_ASSIGNED:
                        targetVal.add(colValues.get(pkName));
                        genPrimaryKeyMap.put(pkName, colValues.get(pkName));
                        break;
                    default:
                        break;
                }
            }
            Iterator insertIter = colValues.entrySet().iterator();
            while (insertIter.hasNext()) {
                Entry<String, Object> entry = (Entry<String, Object>) insertIter.next();
                String fieldName = entry.getKey();
                Object val = entry.getValue();
                if (val != null) {
                    if (columNames.contains(fieldName.toUpperCase()) &&
                            !primaryKeyNames.contains(fieldName.toUpperCase())) {
                        targetCols.add(fieldName.toUpperCase());
                        targetVal.add(val);
                    }
                }
            }
            for (String targetCol : targetCols) {
                sql.append(targetCol).append(",");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(") values (");
            for (String targetCol : targetCols) {
                sql.append("?").append(",");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(")");
            KeyHolder keyHolder = new GeneratedKeyHolder();
            if (idGenerator == SysConstants.ID_GENERATOR_AUTOINCREMENT) {
                getJdbcTemplate().update(new PreparedStatementCreator() {
                                             @Override
                                             public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                                                 PreparedStatement ps = conn.prepareStatement(sql.toString(),
                                                         targetCols.toArray(new String[targetCols.size()]));
                                                 for (int i = 0; i < targetVal.size(); i++) {
                                                     ps.setObject(i + 1, targetVal.get(i));
                                                 }
                                                 return ps;
                                             }
                                         },
                        keyHolder);
            } else {
                this.getJdbcTemplate().update(sql.toString(), targetVal.toArray());
            }
            if (idGenerator == SysConstants.ID_GENERATOR_AUTOINCREMENT) {
                colValues.put(primaryKeyNames.get(0), keyHolder.getKey().toString());
            } else {
                colValues.putAll(genPrimaryKeyMap);
            }
            return colValues;
        }
    }

    /**
     * 描述 删除单表数据库记录信息
     *
     * @param tableName:表名称
     * @param colNames:列名数组
     * @param colValues:列值
     * @created 2016年2月22日 上午9:15:04
     */
    @Override
    public void deleteRecord(String tableName, String[] colNames, Object[] colValues) {
        StringBuffer sql = new StringBuffer("DELETE FROM ");
        sql.append(tableName.toUpperCase());
        sql.append(" WHERE ");
        for (int i = 0; i < colNames.length; i++) {
            if (i > 0) {
                sql.append(" AND ");
            }
            sql.append(colNames[i]).append("=? ");
        }
        this.getJdbcTemplate().update(sql.toString(), colValues);
    }

    /**
     * 描述 根据sql语句和查询参数获取记录总数
     *
     * @param sql
     * @param colValues
     * @return
     * @created 2016年2月22日 上午10:00:18
     */
    private int getTotalCount(String sql, Object[] colValues) {
        String dbType = PlatAppUtil.getDbType();
        StringBuffer newSql = null;
        if ("SQLSERVER".equals(dbType)) {
            List<Map<String, Object>> list = null;
            if (colValues != null && colValues.length > 0) {
                list = this.getJdbcTemplate().queryForList(sql, colValues);
            } else {
                list = this.getJdbcTemplate().queryForList(sql);
            }
            if (list != null && list.size() > 0) {
                return list.size();
            } else {
                return 0;
            }
        } else {
            newSql = new StringBuffer("SELECT COUNT(*) FROM (");
            newSql.append(sql).append(") TEMP_TABLE");
            return this.getIntBySql(newSql.toString(), colValues);
        }
    }

    /**
     * 描述 获取分页后的数据库语句,面向MYSQL数据库
     *
     * @param sql
     * @param pb
     * @return
     * @created 2016年2月22日 上午10:14:30
     */
    private String getPagingBeanSqlForMysql(String sql, PagingBean pb) {
        StringBuffer newSql = new StringBuffer(sql);
        newSql.append(" LIMIT ").append(pb.getStart()).append(",").append(pb.getPageSize());
        int totalPage = (pb.getTotalItems() + pb.getPageSize() - 1) / pb.getPageSize();
        pb.setTotalPage(totalPage);
        return newSql.toString();
    }

    /**
     * 获取分页后的sql语句，面向sqlserver
     *
     * @param sql
     * @param pb
     * @return
     */
    private String getPagingBeanSqlForSqlServer(String sql, PagingBean pb) {
        StringBuffer newSql = new StringBuffer(sql);
        newSql.append(" OFFSET ").append(pb.getStart()).append(" ROW  FETCH NEXT ").append(pb.getPageSize());
        newSql.append(" ROW ONLY ");
        int totalPage = (pb.getTotalItems() + pb.getPageSize() - 1) / pb.getPageSize();
        pb.setTotalPage(totalPage);
        return newSql.toString();
    }

    /**
     * 描述 获取分页SQL语句,面向ORACLE数据库
     *
     * @param sql
     * @param pb
     * @return
     * @created 2016年2月22日 上午10:10:09
     */
    private String getPagingBeanSqlForOracle(String sql, PagingBean pb) {
        int startIndex = pb.getStart() + 1;
        int endIndex = startIndex + pb.getPageSize() - 1;
        String newSql = "select * from (select rOraclePageSQL.*,ROWNUM as currentRow from ("
                + sql
                + ") rOraclePageSQL where rownum <="
                + endIndex
                + ") where currentRow>=" + startIndex;
        int totalPage = (pb.getTotalItems() + pb.getPageSize() - 1) / pb.getPageSize();
        pb.setTotalPage(totalPage);
        return newSql;
    }

    /**
     * 描述 根据sql语句获取记录列表
     *
     * @param sql:sql语句
     * @param colValues:查询的值参数
     * @param pb:分页对象,如果不传,那么不进行分页
     * @return
     * @created 2016年2月22日 上午9:37:35
     */
    @Override
    public List<Map<String, Object>> findBySql(String sql, Object[] colValues, PagingBean pb) {
        if (pb != null) {
            //获取总数量
            int totalCount = this.getTotalCount(sql, colValues);
            pb.setTotalItems(totalCount);
            //获取数据库类型
            String dbType = PlatAppUtil.getDbType();
            String newSql = null;
            if ("MYSQL".equals(dbType)) {
                newSql = this.getPagingBeanSqlForMysql(sql, pb);
            } else if ("ORACLE".equals(dbType)) {
                newSql = this.getPagingBeanSqlForOracle(sql, pb);
            } else if ("SQLSERVER".equals(dbType)) {
                newSql = this.getPagingBeanSqlForSqlServer(sql, pb);
            }
            if (colValues != null && colValues.length > 0) {
                return this.getJdbcTemplate().queryForList(newSql, colValues);
            } else {
                return this.getJdbcTemplate().queryForList(newSql);
            }
        } else {
            if (colValues != null && colValues.length > 0) {
                return this.getJdbcTemplate().queryForList(sql, colValues);
            } else {
                return this.getJdbcTemplate().queryForList(sql);
            }
        }
    }

    /**
     * 描述 获取树形结构表的最大排序值
     *
     * @param tableName
     * @return
     * @created 2016年2月27日 上午11:35:41
     */
    @Override
    public int getMaxTreeSortSn(String tableName, String treeSnName) {
        StringBuffer sql = new StringBuffer("select max(");
        sql.append(treeSnName).append(") from ").append(tableName);
        int result = this.getIntBySql(sql.toString(), null);
        return result;
    }

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
    @Override
    public Map<String, Object> saveOrUpdateTreeData(String tableName,
                                                    Map<String, Object> colValues, int idGenerator, String seqName) {
        String path = "";
        tableName = tableName.toUpperCase();
        String entityName = tableName.substring(tableName.lastIndexOf("_") + 1, tableName.length());
        String parentId = null;
        String PARENT_ID_NAME = entityName + "_PARENTID";
        String TREE_PATH_NAME = entityName + "_PATH";
        String TREE_LEVEL_NAME = entityName + "_LEVEL";
        String TREE_SN_NAME = entityName + "_TREESN";
        if (colValues.get(PARENT_ID_NAME) != null) {
            parentId = colValues.get(PARENT_ID_NAME).toString();
        }
        int level = 0;
        //获取私有主键名称
        String primaryKeyName = this.findPrimaryKeyNames(tableName).get(0);
        if (StringUtils.isNotEmpty(parentId) && !"0".equals(parentId)) {
            StringBuffer sql = new StringBuffer("select * from ").append(tableName).append(" WHERE ")
                    .append(primaryKeyName).append("=? ");
            Map<String, Object> parentData = this.getBySql(sql.toString(), new Object[]{parentId});
            path = (String) parentData.get(TREE_PATH_NAME);
            level = Integer.parseInt(parentData.get(TREE_LEVEL_NAME).toString());
        } else {
            parentId = "0";
            path = "0.";
        }
        colValues.put(TREE_LEVEL_NAME, level + 1);
        colValues.put(PARENT_ID_NAME, parentId);
        int maxSn = this.getMaxTreeSortSn(tableName, TREE_SN_NAME);
        if (maxSn == 0) {
            maxSn = 1;
        }
        // 获取主键值
        String primaryKeyValue = null;
        if (colValues.get(primaryKeyName) != null) {
            primaryKeyValue = colValues.get(primaryKeyName).toString();
        }
        if (StringUtils.isEmpty(primaryKeyValue)
                || idGenerator == SysConstants.ID_GENERATOR_ASSIGNED) {
            colValues.put(TREE_SN_NAME, maxSn + 1);
            colValues.put(TREE_PATH_NAME, path);
        }
        colValues = this.saveOrUpdate(tableName, colValues, idGenerator, seqName);
        if (colValues.get(primaryKeyName) != null) {
            primaryKeyValue = colValues.get(primaryKeyName).toString();
        }
        if (StringUtils.isNotEmpty(primaryKeyValue)) {
            path = path + primaryKeyValue + ".";
            colValues.put(TREE_PATH_NAME, path);
            return this.saveOrUpdate(tableName, colValues, idGenerator, seqName);
        } else {
            return colValues;
        }
    }

    /**
     * 把传递进来的SQL语句进行重新构建 构建成查询的SQL语句
     *
     * @param sqlFilter :sql过滤器
     * @param oldSql    :原本的SQL语句
     * @param params    :查询参数
     * @return
     */
    @Override
    public String getQuerySql(SqlFilter sqlFilter, String oldSql, List<Object> params) {
        StringBuffer sql = new StringBuffer(oldSql);
        Map<String, Object> queryParams = sqlFilter.getQueryParams();
        Map<String, String> orderParams = sqlFilter.getOrderParams();
        Map<String, String> groupParams = sqlFilter.getGroupParams();
        if (sql.indexOf("where") == -1 && sql.indexOf("WHERE") == -1) {
            sql.append(" where 1=1 ");
        }
        Iterator iter = queryParams.entrySet().iterator();
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            String key = entry.getKey().toString();
            String val = entry.getValue().toString();
            SqlFilter.addQueryParam(key, val, sql, params);
        }
        if (groupParams.size() > 0) {
            sql.append(" group by ");
            Iterator orderIter = groupParams.entrySet().iterator();
            while (orderIter.hasNext()) {
                Entry<String, String> entry = (Entry<String, String>) orderIter.next();
                String key = entry.getKey();
                key = key.substring(key.indexOf("_") + 1, key.length());
                sql.append(key).append(",");
            }
            sql = sql.deleteCharAt(sql.length() - 1);
        }
        if (orderParams.size() > 0) {
            sql.append(" order by ");
            Iterator orderIter = orderParams.entrySet().iterator();
            while (orderIter.hasNext()) {
                Entry<String, String> entry = (Entry<String, String>) orderIter.next();
                String key = entry.getKey().toString();
                key = key.substring(key.indexOf("_") + 1, key.length());
                String val = entry.getValue().toString();
                sql.append(key).append(" ").append(val).append(",");
            }
            sql = sql.deleteCharAt(sql.length() - 1);
        }
        return sql.toString();
    }

    /**
     * 获取数据库表信息列表
     *
     * @return
     */
    @Override
    public List<TableInfo> findDbTables() {
        //获取数据库的类型
        String dbType = PlatAppUtil.getDbType();
        List<Map<String, Object>> list = null;
        List<TableInfo> infos = new ArrayList<TableInfo>();
        if ("ORACLE".equals(dbType)) {
            StringBuffer sql = new StringBuffer("SELECT U.TABLE_NAME,U.COMMENTS FROM");
            sql.append(" USER_TAB_COMMENTS U WHERE U.table_type=? ");
            sql.append(" ORDER BY U.table_name ASC");
            list = this.findBySql(sql.toString(), new Object[]{"TABLE"}, null);
            for (Map<String, Object> map : list) {
                TableInfo info = new TableInfo((String) map.get("TABLE_NAME")
                        , (String) map.get("COMMENTS"));
                infos.add(info);
            }
        } else if ("MYSQL".equals(dbType)) {

        } else {
            return null;
        }
        return infos;
    }

    /**
     * 执行sql语句
     *
     * @param sql:sql语句
     * @param params:查询参数
     */
    @Override
    public void executeSql(String sql, Object[] params) {
        if (params != null && params.length > 0) {
            this.getJdbcTemplate().update(sql, params);
        } else {
            this.getJdbcTemplate().update(sql);
        }
    }

    /**
     * 获取孩子数据
     *
     * @param allList
     * @param parentId
     * @param parentMap
     * @param idAndNameArray
     */
    public void getChildren(List<Map<String, Object>> allList, String parentId, Map<String, Object> parentMap,
                            String[] idAndNameArray, Set<String> needCheckIdSet, String parentIdName) {
        List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : allList) {
            // 获取父亲ID
            String pId = map.get(parentIdName).toString();
            if (pId.equals(parentId)) {
                // 获取ID值
                String id = map.get(idAndNameArray[0]).toString();
                // 获取NAME值
                String name = map.get(idAndNameArray[1]).toString();
                map.put("id", id);
                map.put("name", name);
                if (needCheckIdSet.contains(id)) {
                    map.put("checked", true);
                }
                children.add(map);
            }
        }
        if (children.size() > 0) {
            parentMap.put("children", children);
            for (Map<String, Object> child : children) {
                String id = child.get(idAndNameArray[0]).toString();
                this.getChildren(allList, id, child, idAndNameArray, needCheckIdSet, parentIdName);
            }
        }
    }

    /**
     * 获取树形的数据
     *
     * @param params
     * @return
     */
    @Override
    public Object getTreeData(Map<String, Object> params) {
        String isShowTreeTitle = (String) params.get("isShowTreeTitle");
        String treeTitle = (String) params.get("treeTitle");
        String loadRootId = (String) params.get("loadRootId");
        String tableName = (String) params.get("tableName");
        String idAndNameCol = (String) params.get("idAndNameCol");
        String entityName = tableName.substring(tableName.lastIndexOf("_") + 1, tableName.length());
        String parentIdName = entityName + "_PARENTID";
        String[] idAndNameArray = idAndNameCol.split(",");
        String targetCols = (String) params.get("targetCols");
        String needCheckIds = (String) params.get("needCheckIds");
        Set<String> needCheckIdSet = new HashSet<String>();
        if (StringUtils.isNotEmpty(needCheckIds)) {
            needCheckIdSet = new HashSet<String>(Arrays.asList(needCheckIds.split(",")));
        }
        List<Map<String, Object>> allList = findTreeData(params, loadRootId, tableName, idAndNameCol, targetCols);
        Map<String, Object> rootNode = new HashMap<String, Object>();
        if ("true".equals(isShowTreeTitle)) {
            rootNode.put("id", "0");
            rootNode.put("name", treeTitle);
            rootNode.put("open", true);
            if (needCheckIdSet.size() > 0) {
                rootNode.put("checked", true);
            }
        }
        if (!"0".equals(loadRootId)) {
            Map<String, Object> firstLoadNode = this.getRecord(tableName,
                    new String[]{idAndNameArray[0]}, new Object[]{loadRootId});
            firstLoadNode.put("id", loadRootId);
            firstLoadNode.put("name", firstLoadNode.get(idAndNameArray[1]));
            List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
            children.add(firstLoadNode);
            rootNode.put("children", children);
        }

        List<Map<String, Object>> topList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : allList) {
            // 获取ID值
            String id = map.get(idAndNameArray[0]).toString();
            // 获取NAME值
            String name = (String) map.get(idAndNameArray[1]);
            // 获取父亲ID
            String parentId = map.get(parentIdName).toString();
            if (parentId.equals(loadRootId)) {
                map.put("id", id);
                map.put("name", name);
                if (needCheckIds.contains(id)) {
                    map.put("checked", true);
                }
                this.getChildren(allList, id, map, idAndNameArray, needCheckIdSet, parentIdName);
                topList.add(map);
            }
        }
        if (rootNode != null) {
            List<Map<String, Object>> children = (List<Map<String, Object>>) rootNode.get("children");
            if (children != null && children.size() > 0) {
                children.get(0).put("children", topList);
            } else {
                rootNode.put("children", topList);
            }
            return rootNode;
        } else {
            return topList;
        }
    }

    /**
     * 获取树形的JSON数据
     *
     * @param params
     * @return
     */
    @Override
    public String getTreeJson(Map<String, Object> params) {
        Object result = this.getTreeData(params);
        return JSON.toJSONString(result);
    }

    /**
     * 获取树形数据
     *
     * @param params
     * @param loadRootId
     * @param tableName
     * @param idAndNameCol
     * @param targetCols
     */
    public List<Map<String, Object>> findTreeData(Map<String, Object> params, String loadRootId,
                                                  String tableName, String idAndNameCol, String targetCols) {
        String entityName = tableName.substring(tableName.lastIndexOf("_") + 1, tableName.length());
        String treeSnName = entityName + "_TREESN";
        String createTimeName = entityName + "_CREATETIME";
        String pathName = entityName + "_PATH";
        String path = null;
        List<Object> queryParams = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(idAndNameCol).append(",").append(targetCols);
        sql.append(" FROM ").append(tableName);
        if ("0".equals(loadRootId)) {
            path = "0.";
        } else {
            path = "." + loadRootId + ".";
        }
        sql.append(" WHERE ").append(pathName).append(" LIKE ? ");
        queryParams.add("%" + path + "%");
        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, Object> entry = (Entry<String, Object>) iter.next();
            String paramName = entry.getKey();
            Object paramValue = entry.getValue();
            if (paramName.startsWith("Q_")) {
                if (StringUtils.isNotEmpty(paramValue.toString())) {
                    SqlFilter.addQueryParam(paramName, paramValue.toString(), sql, queryParams);
                }
            }
        }
        sql.append("ORDER BY ").append(treeSnName).append(" ASC,");
        sql.append(createTimeName).append(" DESC");
        List<Map<String, Object>> treeDataList = this.findBySql(sql.toString(), queryParams.toArray(), null);
        return treeDataList;
    }

    /**
     * 获取顶级的最后一条数据的ID值
     *
     * @param tableName
     * @return
     */
    private String getTopLevelLastNodeId(String tableName) {
        String primaryKeyName = this.findPrimaryKeyNames(tableName).get(0);
        String entityName = tableName.substring(tableName.lastIndexOf("_") + 1, tableName.length());
        String PARENT_ID_NAME = entityName + "_PARENTID";
        String TREE_SN_NAME = entityName + "_TREESN";
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(primaryKeyName).append(" FROM ").append(tableName);
        sql.append(" where ").append(PARENT_ID_NAME).append("=? ");
        sql.append(" ORDER BY ").append(TREE_SN_NAME).append(" DESC ");
        List<String> idsList = this.getJdbcTemplate().queryForList(sql.toString(),
                new Object[]{"0"}, String.class);
        return idsList.get(0);
    }

    /**
     * 更新树形排序字段
     *
     * @param tableName
     * @param dragTreeNodeId
     * @param dragTreeNodeNewLevel
     * @param targetNodeId
     * @param targetNodeLevel
     */
    @Override
    public void updateTreeSn(String tableName, String dragTreeNodeId, int dragTreeNodeNewLevel, String targetNodeId,
                             int targetNodeLevel, String moveType) {
        if ("0".equals(targetNodeId) && "inner".equals(moveType)) {
            targetNodeId = this.getTopLevelLastNodeId(tableName);
            moveType = "next";
            targetNodeLevel = 1;
        }
        String primaryKeyName = this.findPrimaryKeyNames(tableName).get(0);
        Map<String, Object> dragTreeData = this.getRecord(tableName, new String[]{primaryKeyName},
                new Object[]{dragTreeNodeId});
        Map<String, Object> targetTreeData = this.getRecord(tableName, new String[]{primaryKeyName},
                new Object[]{targetNodeId});
        String entityName = tableName.substring(tableName.lastIndexOf("_") + 1, tableName.length());
        String PARENT_ID_NAME = entityName + "_PARENTID";
        String TREE_PATH_NAME = entityName + "_PATH";
        String TREE_LEVEL_NAME = entityName + "_LEVEL";
        String TREE_SN_NAME = entityName + "_TREESN";
        // 计算最终的level
        //int level = dragTreeNodeNewLevel + 1;
        int level = dragTreeNodeNewLevel;
        // 获取原先的level
        int oldLevel = Integer.parseInt(dragTreeData.get(TREE_LEVEL_NAME).toString());
        // 获取差距的level
        int minusLevel = oldLevel - level;
        // 获取旧的路径
        String oldPath = (String) dragTreeData.get(TREE_PATH_NAME);
        if (dragTreeNodeNewLevel == targetNodeLevel) {
            // 说明将其拖动到目标节点前面
            // 计算最终的排序值
            int treeSn = Integer.parseInt(targetTreeData.get(TREE_SN_NAME).toString());
            if ("next".equals(moveType)) {
                treeSn += 1;
            }
            // 计算最终的父亲ID
            String parentId = targetTreeData.get(PARENT_ID_NAME).toString();
            // 计算最新的path
            String path1 = (String) targetTreeData.get(TREE_PATH_NAME);
            String path2 = path1.substring(0, path1.indexOf(targetNodeId + "."));
            String path = path2 + dragTreeNodeId + ".";
            // 将该节点更新
            dragTreeData.put(TREE_LEVEL_NAME, level);
            dragTreeData.put(PARENT_ID_NAME, parentId);
            dragTreeData.put(TREE_PATH_NAME, path);
            dragTreeData.put(TREE_SN_NAME, treeSn);
            // 将同级的其它节点排序全部加+1
            if ("prev".equals(moveType) || "next".equals(moveType)) {
                StringBuffer updateSnSql = new StringBuffer("UPDATE ");
                updateSnSql.append(tableName).append(" SET ")
                        .append(TREE_SN_NAME).append("=(").append(TREE_SN_NAME)
                        .append("+1) WHERE ").append(TREE_LEVEL_NAME).append("=? ");
                updateSnSql.append("AND ").append(TREE_SN_NAME).append(" >= ? AND ");
                updateSnSql.append(primaryKeyName)
                        .append("!=? ");
                this.getJdbcTemplate().update(updateSnSql.toString(), new Object[]{level, treeSn, dragTreeNodeId});
            }
            // 更新被拖动节点子孙节点的层级信息和PATH信息
            StringBuffer updateChildInfo = new StringBuffer("UPDATE ").append(tableName).append(
                    " SET ").append(TREE_LEVEL_NAME).append("=(").append(TREE_LEVEL_NAME);
            if (minusLevel >= 0) {
                updateChildInfo.append("-");
            } else {
                minusLevel = -minusLevel;
                updateChildInfo.append("+");
            }
            updateChildInfo.append(minusLevel).append("),").append("").append(TREE_PATH_NAME)
                    .append("=REPLACE(").append(TREE_PATH_NAME).append(",'").append(oldPath)
                    .append("','").append(path).append("') WHERE ").append(primaryKeyName).append("!=? AND ")
                    .append(" ").append(PARENT_ID_NAME).append("=? ");
            this.getJdbcTemplate().update(updateChildInfo.toString(), new Object[]{dragTreeNodeId, dragTreeNodeId});
            // 更新被拖动节点的信息
            this.saveOrUpdate(tableName, dragTreeData, SysConstants.ID_GENERATOR_UUID, null);
        } else if (dragTreeNodeNewLevel > targetNodeLevel) {
            // 说明将其拖动到目标节点里面
            String parentId = targetNodeId;
            // 获取该节点下最大排序值的节点
            StringBuffer maxSql = new StringBuffer("select max(").append(TREE_SN_NAME).append(")");
            maxSql.append(" FROM ").append(tableName).append(" WHERE ").append(PARENT_ID_NAME);
            maxSql.append("=? ");
            int treeSn = this.getIntBySql(maxSql.toString(), new Object[]{parentId}) + 1;
            // 计算最新的path
            String path1 = (String) targetTreeData.get(TREE_PATH_NAME);
            String path = path1 + dragTreeNodeId + ".";
            // 更新被拖动节点子孙节点的层级信息和PATH信息
            StringBuffer updateChildInfo = new StringBuffer("UPDATE ").append(tableName).append(
                    " SET ").append(TREE_LEVEL_NAME).append("=(").append(TREE_LEVEL_NAME);
            if (minusLevel >= 0) {
                updateChildInfo.append("-");
            } else {
                minusLevel = -minusLevel;
                updateChildInfo.append("+");
            }
            updateChildInfo.append(minusLevel).append("),").append("").append(TREE_PATH_NAME)
                    .append("=REPLACE(").append(TREE_PATH_NAME).append(",'").append(oldPath)
                    .append("','").append(path).append("') WHERE ").append(primaryKeyName).append("!=? AND ")
                    .append(" ").append(PARENT_ID_NAME).append("=? ");

            this.getJdbcTemplate().update(updateChildInfo.toString(), new Object[]{dragTreeNodeId, dragTreeNodeId});
            // 更新被拖动节点的信息
            // 将该节点更新
            dragTreeData.put(TREE_LEVEL_NAME, level);
            dragTreeData.put(PARENT_ID_NAME, parentId);
            dragTreeData.put(TREE_PATH_NAME, path);
            dragTreeData.put(TREE_SN_NAME, treeSn);
            this.saveOrUpdate(tableName, dragTreeData, SysConstants.ID_GENERATOR_UUID, null);
            //this.saveOrUpdate(dragTreeData, tableName, dragTreeNodeId);
        }
    }

    /**
     * 根据表名称和字段名称删除记录
     *
     * @param tableName
     * @param fieldName
     * @param colValues
     */
    @Override
    public void deleteRecords(String tableName, String fieldName, String[] colValues) {
        StringBuffer sql = new StringBuffer("DELETE FROM ");
        sql.append(tableName).append(" WHERE ");
        sql.append(fieldName).append(" IN ");
        sql.append(PlatStringUtil.getSqlInCondition(colValues));
        this.executeSql(sql.toString(), null);
    }

    /**
     * 根据表名称和字段名称获取注释
     *
     * @param tableName
     * @param colName
     * @return
     */
    @Override
    public String getColComment(String tableName, String colName) {
        String dbType = PlatAppUtil.getDbType();
        if ("ORACLE".equals(dbType)) {
            StringBuffer sql = new StringBuffer("select colcomment.comments from user_tab_columns col");
            sql.append(",user_col_comments colcomment where col.TABLE_NAME=? ")
                    .append(" and col.TABLE_NAME=colcomment.table_name ")
                    .append(" and col.COLUMN_NAME=colcomment.column_name ").append(" and col.COLUMN_NAME=? ");
            String comments = "";
            try {
                comments = this.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{tableName, colName},
                        String.class);
            } catch (Exception e) {
                PlatLogUtil.printStackTrace(e);
            }
            return comments;
        } else if ("MYSQL".equals(dbType)) {
            return null;
        } else {
            return null;
        }
    }

    /**
     * 根据SQL语句获取查询列名称集合
     *
     * @param sql
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<String> findQueryColNamesBySql(String sql) {
        String dbType = PlatAppUtil.getDbType();
        if ("ORACLE".equals(dbType)) {
            final List<String> keys = new ArrayList<String>();
            StringBuffer targetSql = new StringBuffer("select * FROM (").append(sql.toUpperCase()).append(
                    ")  WHERE ROWNUM<=1 ");
            this.getJdbcTemplate().query(targetSql.toString(), new ResultSetExtractor() {
                @Override
                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        keys.add(rsmd.getColumnName(i));
                    }
                    return null;
                }

            });
            return keys;
        } else if ("MYSQL".equals(dbType)) {
            final List<String> keys = new ArrayList<String>();
            StringBuffer targetSql = new StringBuffer("select * FROM (").append(sql.toUpperCase()).append(
                    ") AS TEMP LIMIT 1 ");
            this.getJdbcTemplate().query(targetSql.toString(), new ResultSetExtractor() {
                @Override
                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        keys.add(rsmd.getColumnName(i));
                    }
                    return null;
                }

            });
            return keys;
        } else if ("SQLSERVER".equals(dbType)) {
            final List<String> keys = new ArrayList<String>();
            StringBuffer targetSql = new StringBuffer("select * FROM (").append(sql.toUpperCase()).append(
                    ") AS TEMP ");
            this.getJdbcTemplate().query(targetSql.toString(), new ResultSetExtractor() {
                @Override
                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        keys.add(rsmd.getColumnName(i));
                    }
                    return null;
                }

            });
            return keys;
        } else {
            return null;
        }
    }

    /**
     * 获取oracle的约束类型
     *
     * @param tableName
     * @param columnName
     * @return
     */
    public String getOracleConstraintType(String tableName, String columnName) {
        StringBuffer sql = new StringBuffer("SELECT AU.constraint_type FROM user_constraints AU");
        sql.append(" LEFT JOIN user_cons_columns CU ON cu.constraint_name=au.constraint_name");
        sql.append(" AND AU.table_name=CU.table_name WHERE AU.table_name=? ");
        sql.append(" AND AU.constraint_type IN ('P','R','U') AND CU.column_name=? ");
        String constraintType = this.getUniqueObj(sql.toString(), new Object[]{tableName, columnName}, String.class);
        return constraintType;
    }

    /**
     * 获取oracle的外键列信息字段
     *
     * @param tableName:表名称
     * @param columnName:列名称
     * @return
     */
    private Map<String, Object> getOracleForeignColumnInfo(String tableName, String columnName) {
        StringBuffer sql = new StringBuffer("select t2.table_name,a2.column_name ");
        sql.append("from user_constraints t1, user_constraints t2,");
        sql.append("user_cons_columns a1, user_cons_columns a2 ");
        sql.append("where t1.table_name=? and t1.r_constraint_name = t2.constraint_name and ");
        sql.append("t1.constraint_name = a1.constraint_name and ");
        sql.append("t1.r_constraint_name = a2.constraint_name ");
        sql.append("AND A1.column_name=? ");
        Map<String, Object> map = this.getBySql(sql.toString(), new Object[]{tableName, columnName});
        return map;
    }

    /**
     * 根据表名称和列名称获取外键列信息
     *
     * @param tableName
     * @param columnName
     * @return
     */
    public Map<String, Object> getForeignColumnInfo(String tableName, String columnName) {
        String dbType = PlatAppUtil.getDbType();
        if ("ORACLE".equals(dbType)) {
            return this.getOracleForeignColumnInfo(tableName, columnName);
        }
        return null;
    }

    /**
     * 获取oracle的列信息集合
     *
     * @param tableName
     * @return
     */
    @Override
    public List<Map<String, Object>> findOracleTableColumns(String tableName, String columnName) {
        List<Object> params = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT COL.COLUMN_ID,COL.COLUMN_NAME,COL.DATA_TYPE");
        sql.append(",COL.DATA_LENGTH,COL.NULLABLE,COL.DATA_DEFAULT,CM.comments");
        sql.append(" FROM user_tab_columns col left join user_col_comments cm ");
        sql.append("on col.COLUMN_NAME=cm.column_name AND COL.TABLE_NAME=CM.table_name ");
        sql.append("where col.TABLE_NAME=? ");
        params.add(tableName);
        if (StringUtils.isNotEmpty(columnName)) {
            sql.append(" AND col.column_name=?");
            params.add(columnName);
        }
        sql.append("ORDER BY col.COLUMN_ID ASC");
        List<Map<String, Object>> list = this.findBySql(sql.toString(), params.toArray(), null);
        for (Map<String, Object> map : list) {
            String COLUMN_NAME = (String) map.get("COLUMN_NAME");
            String constraintType = this.getOracleConstraintType(tableName, COLUMN_NAME);
            if (StringUtils.isNotEmpty(constraintType)) {
                map.put("CONSTRAINT_TYPE", constraintType);
            }
        }
        Set<String> columnNames = new HashSet<String>();
        List<Map<String, Object>> columnList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : list) {
            String COLUMN_NAME = (String) map.get("COLUMN_NAME");
            if (!columnNames.contains(COLUMN_NAME)) {
                //获取约束
                String CONSTRAINT_TYPE = (String) map.get("CONSTRAINT_TYPE");
                //如果是外键
                if (StringUtils.isNotEmpty(CONSTRAINT_TYPE) && "R".equals(CONSTRAINT_TYPE)) {
                    Map<String, Object> foreignColumnInfo = this.getForeignColumnInfo(tableName, COLUMN_NAME);
                    map.put("FOREIGNREFTABLENAME", foreignColumnInfo.get("TABLE_NAME"));
                    map.put("FOREIGNREFCOLUMNNAME", foreignColumnInfo.get("COLUMN_NAME"));
                }
                columnList.add(map);
                columnNames.add(COLUMN_NAME);
            }
        }
        return columnList;
    }

    /**
     * 获取mysql的约束类型
     *
     * @param tableName
     * @param columnName
     * @return
     */
    public String getMySqlConstraintType(String tableName, String columnName) {
        StringBuffer sql = new StringBuffer("select t.COLUMN_KEY ");
        sql.append("FROM information_schema.COLUMNS t where t.TABLE_SCHEMA=? ");
        sql.append(" and t.table_name=? AND t.COLUMN_NAME=? ");
        String shemaName = PlatAppUtil.getDbSchema();
        String constraintType = this.getUniqueObj(sql.toString(), new Object[]{shemaName, tableName,
                columnName}, String.class);
        return constraintType;
    }


    /**
     * 根据表名称获取数据库列的集合列表
     *
     * @param tableName
     * @return
     */
    @Override
    public List<TableColumn> findTableColumnByTableName(String tableName) {
        String dbType = PlatAppUtil.getDbType();
        List<TableColumn> columns = new ArrayList<TableColumn>();
        if ("ORACLE".equals(dbType)) {
            List<Map<String, Object>> list = this.findOracleTableColumns(tableName, null);
            for (Map<String, Object> map : list) {
                TableColumn column = getOracleTableColumn(map);
                columns.add(column);
            }
        } else if ("MYSQL".equals(dbType)) {
            List<Map<String, Object>> list = this.findMySqlTableColumns(tableName, null);
            for (Map<String, Object> map : list) {
                TableColumn column = getMySqlTableColumn(map);
                columns.add(column);
            }
        } else if ("SQLSERVER".equals(dbType)) {
            List<Map<String, Object>> list = this.findSqlServerTableColumns(tableName, null);
            for (Map<String, Object> map : list) {
                TableColumn column = getSqlServerTableColumn(map);
                columns.add(column);
            }
        }
        return columns;
    }


    /**
     * @param map
     * @return
     */
    @Override
    public TableColumn getOracleTableColumn(Map<String, Object> map) {
        TableColumn column = new TableColumn();
        column.setColumnId(map.get("COLUMN_ID").toString());
        column.setColumnName((String) map.get("COLUMN_NAME"));
        column.setOldColumnName((String) map.get("COLUMN_NAME"));
        column.setColumnComments((String) map.get("COMMENTS"));
        String dataType = (String) map.get("DATA_TYPE");
        dataType = dataType.toUpperCase();
        if (dataType.contains("VARCHAR")) {
            column.setColumnType(TableColumn.COLUMN_TYPE_STRING);
        } else if (dataType.contains("DATE")) {
            column.setColumnType(TableColumn.COLUMN_TYPE_DATE);
        } else if (dataType.contains("TIMESTAMP")) {
            column.setColumnType(TableColumn.COLUMN_TYPE_TIMESTAMP);
        } else if (dataType.contains("CLOB")) {
            column.setColumnType(TableColumn.COLUMN_TYPE_TEXT);
        } else if (dataType.contains("NUMBER")) {
            //获取小数点位数
            if (map.get("DATA_SCALE") != null && map.get("DATA_PRECISION") == null) {
                int dataScale = Integer.parseInt(map.get("DATA_SCALE").toString());
                if (dataScale == 0) {
                    column.setColumnType(TableColumn.COLUMN_TYPE_INT);
                } else {
                    column.setColumnType(TableColumn.COLUMN_TYPE_NUMBER);
                }
            } else {
                column.setColumnType(TableColumn.COLUMN_TYPE_NUMBER);
            }
            if (map.get("DATA_SCALE") != null) {
                column.setScale(map.get("DATA_SCALE").toString());
            }
            if (map.get("DATA_PRECISION") != null) {
                column.setPrecision(map.get("DATA_PRECISION").toString());
            }
        }
        column.setDataLength(map.get("DATA_LENGTH").toString());
        if (StringUtils.isNotEmpty(column.getPrecision())) {
            column.setDataLength(column.getPrecision());
        }
        String NULLABLE = (String) map.get("NULLABLE");
        if ("N".equals(NULLABLE)) {
            column.setIsNullable("-1");
        } else {
            column.setIsNullable("1");
        }
        if (StringUtils.isNotEmpty((String) map.get("DATA_DEFAULT"))) {
            column.setDefaultValue((String) map.get("DATA_DEFAULT"));
        } else {
            column.setDefaultValue("");
        }

        String CONSTRAINT_TYPE = (String) map.get("CONSTRAINT_TYPE");
        if (StringUtils.isNotEmpty(CONSTRAINT_TYPE) && "P".equals(CONSTRAINT_TYPE)) {
            column.setIsPrimaryKey("1");
        } else {
            column.setIsPrimaryKey("-1");
        }
        if (StringUtils.isNotEmpty(CONSTRAINT_TYPE) && "U".equals(CONSTRAINT_TYPE)) {
            column.setIsUnique("1");
        } else {
            column.setIsUnique("-1");
        }
        if (StringUtils.isNotEmpty(CONSTRAINT_TYPE) && "R".equals(CONSTRAINT_TYPE)) {
            column.setIsForeign("1");
            column.setForeignRefTableName((String) map.get("FOREIGNREFTABLENAME"));
            column.setForeignRefColumnName((String) map.get("FOREIGNREFCOLUMNNAME"));
        } else {
            column.setIsForeign("-1");
            column.setForeignRefTableName("");
            column.setForeignRefColumnName("");
        }
        return column;
    }

    /**
     * 描述 将数据库字段进行重新排序
     *
     * @param columns
     * @param columnKeys
     * @return
     * @created 2016年5月25日 上午11:01:01
     */
    public List<String> findOrderColumn(List<TableColumn> columns, Set<String> columnKeys) {
        List<String> fieldNames = new ArrayList<String>();
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (TableColumn column : columns) {
            if (columnKeys.contains(column.getColumnName())) {
                map.put(Integer.parseInt(column.getColumnId()), column.getColumnName());
            }
        }
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Integer, String> entry = (Entry<Integer, String>) iter.next();
            fieldNames.add(entry.getValue());
        }
        return fieldNames;
    }

    /**
     * 描述 根据表名称获取需要插入的SQL语句
     *
     * @param tableName
     * @return
     * @created 2016年5月25日 上午10:14:31
     */
    public String getPreparedInsertSql(String tableName, Set<String> columnKeys) {
        List<String> orderColumns = this.findOrderColumn(this.findTableColumnByTableName(tableName), columnKeys);
        StringBuffer sql = new StringBuffer("INSERT INTO ");
        sql.append(tableName);
        sql.append("(");
        StringBuffer ques = new StringBuffer("");
        for (int i = 0; i < orderColumns.size(); i++) {
            if (i > 0) {
                sql.append(",");
                ques.append(",");
            }
            sql.append(orderColumns.get(i));
            ques.append("?");
        }
        sql.append(") VALUES(");
        sql.append(ques).append(")");
        return sql.toString();
    }

    /**
     * 描述 批量插入数据
     *
     * @param datas
     * @param tableName
     * @created 2016年5月25日 上午11:23:43
     */
    @Override
    public void saveBatch(final List<Map<String, Object>> datas, String tableName) {
        final List<TableColumn> columns = this.findTableColumnByTableName(tableName);
        String sql = this.getPreparedInsertSql(tableName, datas.get(0).keySet());
        final List<String> orderColumns = this.findOrderColumn(columns, datas.get(0).keySet());
        this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public int getBatchSize() {
                return datas.size();
            }

            @Override
            public void setValues(PreparedStatement ps, int index) throws SQLException {
                Map<String, Object> data = (HashMap<String, Object>) datas.get(index);
                Iterator iter = data.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<String, Object> entry = (Entry<String, Object>) iter.next();
                    ps.setObject(orderColumns.indexOf(entry.getKey()) + 1, entry.getValue());
                }
            }
        });
    }

    /**
     * 获取拷贝表数据的SQL
     *
     * @param tableName
     * @param replaceColumn
     * @return
     */
    @Override
    public String getCopyTableSql(String tableName, Map<String, String> replaceColumn) {
        List<String> columnNames = this.findColumnName(tableName);
        StringBuffer sql = new StringBuffer("INSERT INTO ").append(tableName);
        sql.append("(");
        for (int i = 0; i < columnNames.size(); i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append(columnNames.get(i));
        }
        sql.append(") SELECT ");
        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i);
            if (i > 0) {
                sql.append(",");
            }
            if (StringUtils.isNotEmpty(replaceColumn.get(columnName))) {
                sql.append(replaceColumn.get(columnName));
            } else {
                sql.append(columnName);
            }
        }
        sql.append(" FROM ").append(tableName).append(" ");
        return sql.toString();
    }

    /**
     * 根据SQL语句获取唯一对象
     *
     * @param sql
     * @param classType
     * @return
     */
    @Override
    public <T> T getUniqueObj(String sql, Object[] params, Class<T> classType) {
        StringBuffer countSql = new StringBuffer("SELECT COUNT(*) FROM (");
        countSql.append(sql).append(") ");
        String dbType = PlatAppUtil.getDbType();
        if ("MYSQL".equals(dbType)) {
            countSql.append(" AS COUNT ");
        }
        int resultCount = this.getIntBySql(countSql.toString(), params);
        if (resultCount == 0) {
            return null;
        } else {
            if (params != null && params.length > 0) {
                try {
                    return this.getJdbcTemplate().queryForObject(sql.toString(),
                            params, classType);
                } catch (Exception e) {
                    PlatLogUtil.printStackTrace(e);
                }
                return null;
            } else {
                try {
                    return this.getJdbcTemplate().queryForObject(sql.toString(), classType);
                } catch (Exception e) {
                    PlatLogUtil.printStackTrace(e);
                }
                return null;
            }
        }

    }

    /**
     * 根据SQL获取数量值
     *
     * @param sql
     * @param params
     * @return
     */
    @Override
    public int getIntBySql(String sql, Object[] params) {
        int count = 0;
        try {
            if (params != null && params.length > 0) {
                count = this.getJdbcTemplate().queryForObject(sql, params, Integer.class);
            } else {
                count = this.getJdbcTemplate().queryForObject(sql, Integer.class);
            }
        } catch (Exception e) {
            PlatLogUtil.doNothing(e);
            //e.printStackTrace();
        }
        return count;
    }

    /**
     * 根据表名获取列的名称和注释
     *
     * @param tableName
     * @return
     */
    @Override
    public Map<String, String> getTableColumnComment(String tableName) {
        String dbType = PlatAppUtil.getDbType();
        if ("ORACLE".equals(dbType)) {
            StringBuffer sql = new StringBuffer("select col.COLUMN_NAME,colcomment.COMMENTS from user_tab_columns col");
            sql.append(",user_col_comments colcomment WHERE col.TABLE_NAME=? ");
            sql.append(" and col.TABLE_NAME=colcomment.table_name ");
            sql.append(" and col.COLUMN_NAME=colcomment.column_name ");
            sql.append("order by col.COLUMN_ID asc");
            List<Map<String, Object>> columns = this.findBySql(sql.toString(), new Object[]{tableName}, null);
            Map<String, String> columnInfo = new HashMap<String, String>();
            for (Map<String, Object> column : columns) {
                String COLUMN_NAME = (String) column.get("COLUMN_NAME");
                String COMMENTS = (String) column.get("COMMENTS");
                columnInfo.put(COLUMN_NAME, COMMENTS);
            }
            return columnInfo;
        } else {
            return null;
        }
    }

    /**
     * 获取sqlserver的唯一主键字段
     *
     * @param tableName
     * @return
     */
    private List<String> findUniqueForSqlServer(String tableName) {
        StringBuffer sql = new StringBuffer("SELECT col.name FROM ");
        sql.append("sys.indexes idx JOIN sys.index_columns idxCol");
        sql.append(" ON (idx.object_id = idxCol.object_id ");
        sql.append(" AND idx.index_id = idxCol.index_id ");
        sql.append(" AND idx.is_unique_constraint = 1)");
        sql.append(" JOIN sys.tables tab ON (idx.object_id = tab.object_id)");
        sql.append(" JOIN sys.columns col ON (idx.object_id = col.object_id");
        sql.append(" AND idxCol.column_id = col.column_id) AND tab.name=? ");
        List<String> list = this.getJdbcTemplate().queryForList(sql.toString(),
                new Object[]{tableName}, String.class);
        return list;
    }

    /**
     * 获取sqlServer的列信息集合
     *
     * @param tableName
     * @param columnName
     * @return
     */
    @Override
    public List<Map<String, Object>> findSqlServerTableColumns(String tableName, String columnName) {
        List<Object> params = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer("SELECT distinct ORDINAL_POSITION,T.COLUMN_NAME,cast(C.value as varchar) AS COLUMN_COMMENT");
        sql.append(",T.DATA_TYPE,B.MAX_LENGTH,T.IS_NULLABLE,T.COLUMN_DEFAULT,B.PRECISION AS NUMERIC_PRECISION,");
        sql.append("B.SCALE AS NUMERIC_SCALE FROM sys.columns B ");
        sql.append("LEFT JOIN information_schema.columns T ");
        sql.append("ON B.NAME=T.COLUMN_NAME LEFT JOIN sys.extended_properties C");
        sql.append("  ON C.major_id = B.object_id AND C.minor_id = B.column_id ");
        sql.append(" WHERE T.TABLE_NAME=? ");
        params.add(tableName);
        if (StringUtils.isNotEmpty(columnName)) {
            sql.append(" AND T.COLUMN_NAME=?");
            params.add(columnName);
        }
        sql.append(" order by T.ORDINAL_POSITION ASC");
        List<Map<String, Object>> list = this.findBySql(sql.toString(), params.toArray(), null);
        //获取主键和唯一字段列表
        List<String> pkName = this.findPrimaryKeyNamesForSqlServer(tableName);
        List<String> ukName = this.findUniqueForSqlServer(tableName);
        for (Map<String, Object> map : list) {
            String COLUMN_NAME = (String) map.get("COLUMN_NAME");
            if (pkName.contains(COLUMN_NAME)) {
                map.put("CONSTRAINT_TYPE", "PRI");
            }
            if (ukName.contains(COLUMN_NAME)) {
                map.put("CONSTRAINT_TYPE", "UNI");
            }
        }
        return list;
    }

    /**
     * 获取MySQL的列信息集合
     *
     * @param tableName
     * @return
     */
    @Override
    public List<Map<String, Object>> findMySqlTableColumns(String tableName, String columnName) {
        List<Object> params = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer("select t.ORDINAL_POSITION,t.COLUMN_NAME,t.DATA_TYPE");
        sql.append(",t.CHARACTER_MAXIMUM_LENGTH,t.IS_NULLABLE,t.COLUMN_DEFAULT");
        sql.append(",t.COLUMN_COMMENT,t.NUMERIC_PRECISION,t.NUMERIC_SCALE from ");
        sql.append("information_schema.COLUMNS t where t.TABLE_SCHEMA=? ");
        sql.append(" and t.table_name=? ");
        params.add(PlatAppUtil.getDbSchema());
        params.add(tableName);
        if (StringUtils.isNotEmpty(columnName)) {
            sql.append(" AND T.column_name=?");
            params.add(columnName);
        }
        sql.append(" order by t.ORDINAL_POSITION asc ");
        List<Map<String, Object>> list = this.findBySql(sql.toString(), params.toArray(), null);
        for (Map<String, Object> map : list) {
            String COLUMN_NAME = (String) map.get("COLUMN_NAME");
            String constraintType = this.getMySqlConstraintType(tableName, COLUMN_NAME);
            if (StringUtils.isNotEmpty(constraintType)) {
                map.put("CONSTRAINT_TYPE", constraintType);
            }
        }
        return list;
    }

    /**
     * 获取SqlServer的表格列字段信息
     *
     * @param map
     * @return
     */
    @Override
    public TableColumn getSqlServerTableColumn(Map<String, Object> map) {
        TableColumn column = new TableColumn();
        column.setColumnId(map.get("ORDINAL_POSITION").toString());
        column.setColumnName((String) map.get("COLUMN_NAME"));
        column.setOldColumnName((String) map.get("COLUMN_NAME"));
        column.setColumnComments((String) map.get("COLUMN_COMMENT"));
        String dataType = (String) map.get("DATA_TYPE");
        dataType = dataType.toUpperCase();
        if (dataType.contains("VARCHAR")) {
            column.setColumnType(TableColumn.COLUMN_TYPE_STRING);
        } else if (dataType.contains("DATE")) {
            column.setColumnType(TableColumn.COLUMN_TYPE_DATE);
        } else if (dataType.contains("TIMESTAMP")) {
            column.setColumnType(TableColumn.COLUMN_TYPE_TIMESTAMP);
        } else if (dataType.contains("TEXT")) {
            column.setColumnType(TableColumn.COLUMN_TYPE_TEXT);
        } else if (dataType.contains("INT")) {
            column.setColumnType(TableColumn.COLUMN_TYPE_INT);
        } else if (dataType.contains("DECIMAL")) {
            if (map.get("NUMERIC_PRECISION") != null) {
                column.setPrecision(map.get("NUMERIC_PRECISION").toString());
            }
            if (map.get("NUMERIC_SCALE") != null) {
                column.setScale(map.get("NUMERIC_SCALE").toString());
            }
            column.setColumnType(TableColumn.COLUMN_TYPE_NUMBER);
        }
        if (map.get("MAX_LENGTH") != null) {
            column.setDataLength(map.get("MAX_LENGTH").toString());
        } else {
            column.setDataLength("0");
        }
        if (StringUtils.isNotEmpty(column.getPrecision())) {
            column.setDataLength(column.getPrecision());
        }
        String NULLABLE = (String) map.get("IS_NULLABLE");
        if ("NO".equals(NULLABLE)) {
            column.setIsNullable("-1");
        } else {
            column.setIsNullable("1");
        }
        if (StringUtils.isNotEmpty((String) map.get("COLUMN_DEFAULT"))) {
            column.setDefaultValue((String) map.get("COLUMN_DEFAULT"));
        } else {
            column.setDefaultValue("");
        }

        String CONSTRAINT_TYPE = (String) map.get("CONSTRAINT_TYPE");
        if (StringUtils.isNotEmpty(CONSTRAINT_TYPE) && "PRI".equals(CONSTRAINT_TYPE)) {
            column.setIsPrimaryKey("1");
        } else {
            column.setIsPrimaryKey("-1");
        }
        if (StringUtils.isNotEmpty(CONSTRAINT_TYPE) && "UNI".equals(CONSTRAINT_TYPE)) {
            column.setIsUnique("1");
        } else {
            column.setIsUnique("-1");
        }
        if (StringUtils.isNotEmpty(CONSTRAINT_TYPE) && "R".equals(CONSTRAINT_TYPE)) {
            column.setIsForeign("1");
            column.setForeignRefTableName((String) map.get("FOREIGNREFTABLENAME"));
            column.setForeignRefColumnName((String) map.get("FOREIGNREFCOLUMNNAME"));
        } else {
            column.setIsForeign("-1");
            column.setForeignRefTableName("");
            column.setForeignRefColumnName("");
        }
        if (StringUtils.isEmpty(column.getScale())) {
            column.setScale("");
        }
        return column;
    }

    /**
     * 获取MySQL的表格列字段信息
     *
     * @param map
     * @return
     */
    @Override
    public TableColumn getMySqlTableColumn(Map<String, Object> map) {
        TableColumn column = new TableColumn();
        column.setColumnId(map.get("ORDINAL_POSITION").toString());
        column.setColumnName((String) map.get("COLUMN_NAME"));
        column.setOldColumnName((String) map.get("COLUMN_NAME"));
        column.setColumnComments((String) map.get("COLUMN_COMMENT"));
        String dataType = (String) map.get("DATA_TYPE");
        dataType = dataType.toUpperCase();
        if (dataType.contains("VARCHAR")) {
            column.setColumnType(TableColumn.COLUMN_TYPE_STRING);
        } else if (dataType.contains("DATE")) {
            column.setColumnType(TableColumn.COLUMN_TYPE_DATE);
        } else if (dataType.contains("TIMESTAMP")) {
            column.setColumnType(TableColumn.COLUMN_TYPE_TIMESTAMP);
        } else if (dataType.contains("TEXT")) {
            column.setColumnType(TableColumn.COLUMN_TYPE_TEXT);
        } else if (dataType.contains("INT")) {
            column.setColumnType(TableColumn.COLUMN_TYPE_INT);
        } else if (dataType.contains("DECIMAL")) {
            if (map.get("NUMERIC_PRECISION") != null) {
                column.setPrecision(map.get("NUMERIC_PRECISION").toString());
            }
            if (map.get("NUMERIC_SCALE") != null) {
                column.setScale(map.get("NUMERIC_SCALE").toString());
            }
            column.setColumnType(TableColumn.COLUMN_TYPE_NUMBER);
        }
        if (map.get("CHARACTER_MAXIMUM_LENGTH") != null) {
            column.setDataLength(map.get("CHARACTER_MAXIMUM_LENGTH").toString());
        } else {
            column.setDataLength("0");
        }
        if (StringUtils.isNotEmpty(column.getPrecision())) {
            column.setDataLength(column.getPrecision());
        }
        String NULLABLE = (String) map.get("IS_NULLABLE");
        if ("NO".equals(NULLABLE)) {
            column.setIsNullable("-1");
        } else {
            column.setIsNullable("1");
        }
        if (StringUtils.isNotEmpty((String) map.get("COLUMN_DEFAULT"))) {
            column.setDefaultValue((String) map.get("COLUMN_DEFAULT"));
        } else {
            column.setDefaultValue("");
        }

        String CONSTRAINT_TYPE = (String) map.get("CONSTRAINT_TYPE");
        if (StringUtils.isNotEmpty(CONSTRAINT_TYPE) && "PRI".equals(CONSTRAINT_TYPE)) {
            column.setIsPrimaryKey("1");
        } else {
            column.setIsPrimaryKey("-1");
        }
        if (StringUtils.isNotEmpty(CONSTRAINT_TYPE) && "UNI".equals(CONSTRAINT_TYPE)) {
            column.setIsUnique("1");
        } else {
            column.setIsUnique("-1");
        }
        if (StringUtils.isNotEmpty(CONSTRAINT_TYPE) && "R".equals(CONSTRAINT_TYPE)) {
            column.setIsForeign("1");
            column.setForeignRefTableName((String) map.get("FOREIGNREFTABLENAME"));
            column.setForeignRefColumnName((String) map.get("FOREIGNREFCOLUMNNAME"));
        } else {
            column.setIsForeign("-1");
            column.setForeignRefTableName("");
            column.setForeignRefColumnName("");
        }
        if (StringUtils.isEmpty(column.getScale())) {
            column.setScale("");
        }
        return column;
    }

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
    @Override
    public String getTableFieldValues(String tableName, String targetFieldName,
                                      String[] queryFieldNames, Object[] queryFieldValues, String splitSymbol) {
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(targetFieldName).append(" FROM ").append(tableName);
        if (queryFieldNames != null && queryFieldNames.length > 0) {
            sql.append(" WHERE ");
            for (int i = 0; i < queryFieldNames.length; i++) {
                if (i > 0) {
                    sql.append(" AND ");
                }
                sql.append(queryFieldNames[i]).append("=? ");
            }
            List<String> list = this.getJdbcTemplate().queryForList(sql.toString(),
                    queryFieldValues,
                    String.class);
            StringBuffer selectedRecordIds = new StringBuffer("");
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (i > 0) {
                        selectedRecordIds.append(splitSymbol);
                    }
                    selectedRecordIds.append(list.get(i));
                }
            }
            return selectedRecordIds.toString();
        } else {
            return null;
        }
    }

    /**
     * 获取非键值对的结果列表
     *
     * @param sql
     * @param colValues
     * @param pb
     * @return
     */
    @Override
    public List<List<String>> findListBySql(String sql, Object[] colValues, PagingBean pb) {
        List<Map<String, Object>> list = this.findBySql(sql, colValues, pb);
        if (list != null && list.size() > 0) {
            List<List<String>> dataList = new ArrayList<List<String>>();
            for (Map<String, Object> columnValue : list) {
                List<String> data = new ArrayList<String>();
                Iterator it = columnValue.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, Object> entry = (Entry<String, Object>) it.next();
                    String key = entry.getKey();
                    Object val = entry.getValue();
                    if (val != null) {
                        data.add(val.toString());
                    } else {
                        data.add("");
                    }
                }
                dataList.add(data);
            }
            return dataList;
        } else {
            return null;
        }
    }

}
