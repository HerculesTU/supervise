package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.model.TableColumn;
import com.housoo.platform.core.model.TableInfo;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatPropUtil;
import com.housoo.platform.core.dao.DbManagerDao;
import com.housoo.platform.core.service.DbManagerService;
import com.housoo.platform.core.dynadatasource.DataSource;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 描述
 *
 * @author housoo
 * @created 2017年1月25日 上午9:30:33
 */
@Service("dbManagerService")
public class DbManagerServiceImpl extends BaseServiceImpl implements
        DbManagerService {

    /**
     * 所引入的dao
     */
    @Resource
    private DbManagerDao dao;

    /**
     *
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据sqlfilter获取到数据列表
     *
     * @param sqlFilter
     * @return
     */
    private List<Map<String, Object>> findOracleTables(SqlFilter sqlFilter) {
        List<Object> params = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer("SELECT U.TABLE_NAME,U.COMMENTS FROM USER_TAB_COMMENTS U");
        String exeSql = dao.getQuerySql(sqlFilter, sql.toString(), params);
        List<Map<String, Object>> list = dao.findBySql(exeSql,
                params.toArray(), sqlFilter.getPagingBean());
        return list;
    }

    /**
     * @param sqlFilter
     * @return
     */
    @Override
    public List<TableInfo> findBySqlFilter(SqlFilter sqlFilter) {
        String dbType = PlatAppUtil.getDbType();
        List<TableInfo> tableInfos = new ArrayList<TableInfo>();
        if ("ORACLE".equals(dbType)) {
            List<Map<String, Object>> list = this.findOracleTables(sqlFilter);
            for (Map<String, Object> map : list) {
                TableInfo info = new TableInfo((String) map.get("TABLE_NAME")
                        , (String) map.get("COMMENTS"));
                tableInfos.add(info);
            }
        }
        return tableInfos;
    }

    /**
     * 获取mysql的数据库表列表
     *
     * @param sqlFilter
     * @return
     */
    private List<Map<String, Object>> findMySqlTables(SqlFilter sqlFilter) {
        List<Object> params = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer("select U.TABLE_NAME,U.TABLE_COMMENT AS COMMENTS");
        sql.append(" FROM information_schema.TABLES U where U.TABLE_SCHEMA=? ");
        String schemaName = PlatPropUtil.getPropertyValue("config.properties", "dbschema");
        params.add(schemaName);
        String exeSql = dao.getQuerySql(sqlFilter, sql.toString(), params);
        List<Map<String, Object>> list = dao.findBySql(exeSql,
                params.toArray(), sqlFilter.getPagingBean());
        return list;
    }

    /**
     * @param sqlFilter
     * @return
     */
    private List<Map<String, Object>> findSqlServerTables(SqlFilter sqlFilter) {
        List<Object> params = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer("SELECT * FROM (");
        sql.append(" select U.NAME as TABLE_NAME,cast(sys.extended_properties.value as varchar) AS COMMENTS");
        sql.append(" from sysobjects U LEFT JOIN sys.extended_properties ");
        sql.append(" on U.id=sys.extended_properties.major_id ");
        sql.append(" WHERE U.TYPE='U' and sys.extended_properties.minor_id='0' ");
        sql.append(" ) U WHERE 1=1 ");
        String tableComments = sqlFilter.getRequest().getParameter("Q_U.COMMENTS_LIKE");
        if (StringUtils.isNotEmpty(tableComments)) {
            sqlFilter.getQueryParams().remove("Q_U.COMMENTS_LIKE");
            sql.append(" AND cast(U.COMMENTS as varchar) LIKE ? ");
            params.add("%" + tableComments + "%");
        }
        String exeSql = dao.getQuerySql(sqlFilter, sql.toString(), params);
        List<Map<String, Object>> list = dao.findBySql(exeSql,
                params.toArray(), sqlFilter.getPagingBean());
        return list;
    }

    /**
     * 根据filter和配置信息获取数据列表
     *
     * @param filter
     * @param fieldInfo
     * @return
     */
    @Override
    public List<Map<String, Object>> findList(SqlFilter filter, Map<String, Object> fieldInfo) {
        String dbType = PlatAppUtil.getDbType();
        HttpServletRequest request = filter.getRequest();
        String columnOrderName = request.getParameter("sidx");
        if (StringUtils.isEmpty(columnOrderName)) {
            filter.addFilter("O_U.TABLE_NAME", "ASC", SqlFilter.FILTER_TYPE_ORDER);
        }
        if ("ORACLE".equals(dbType)) {
            return this.findOracleTables(filter);
        } else if ("MYSQL".equals(dbType)) {
            String tableComments = filter.getRequest().getParameter("Q_U.COMMENTS_LIKE");
            if (StringUtils.isNotEmpty(tableComments)) {
                filter.getQueryParams().remove("Q_U.COMMENTS_LIKE");
                filter.addFilter("Q_U.TABLE_COMMENT_LIKE", tableComments, SqlFilter.FILTER_TYPE_QUERY);
            }
            return this.findMySqlTables(filter);
        } else if ("SQLSERVER".equals(dbType)) {
            return this.findSqlServerTables(filter);
        } else {
            return null;
        }
    }

    /**
     * 获取oracle的表信息
     *
     * @param tableName
     * @return
     */
    private Map<String, Object> getOracleTableInfo(String tableName) {
        StringBuffer sql = new StringBuffer("SELECT U.TABLE_NAME,U.COMMENTS FROM USER_TAB_COMMENTS U");
        sql.append(" WHERE U.TABLE_NAME=? ");
        Map<String, Object> map = dao.getBySql(sql.toString(), new Object[]{tableName.toUpperCase()});
        return map;
    }

    /**
     * 获取mysql的表信息
     *
     * @param tableName
     * @return
     */
    private Map<String, Object> getMySqlTableInfo(String tableName) {
        StringBuffer sql = new StringBuffer("SELECT T.TABLE_NAME,T.TABLE_COMMENT AS COMMENTS ");
        sql.append(" FROM information_schema.TABLES T WHERE T.TABLE_SCHEMA=? ");
        sql.append(" AND T.TABLE_NAME=? ");
        String dbSchema = PlatAppUtil.getDbSchema();
        Map<String, Object> map = dao.getBySql(sql.toString(), new Object[]{dbSchema, tableName.toUpperCase()});
        return map;
    }

    /**
     * 获取sqlServer的表信息
     *
     * @param tableName
     * @return
     */
    private Map<String, Object> getSqlServerTableInfo(String tableName) {
        StringBuffer sql = new StringBuffer("select U.NAME as TABLE_NAME, cast(sys.extended_properties.value ");
        sql.append("as varchar) AS COMMENTS from sysobjects U LEFT JOIN sys.extended_properties ");
        sql.append(" on U.id=sys.extended_properties.major_id");
        sql.append(" WHERE U.TYPE='U' and sys.extended_properties.minor_id='0' ");
        sql.append(" AND U.NAME=? ");
        Map<String, Object> map = dao.getBySql(sql.toString(), new Object[]{tableName.toUpperCase()});
        return map;
    }

    /**
     * 根据表名称获取表信息
     *
     * @param tableName
     * @return
     */
    @Override
    public TableInfo getTableInfo(String tableName) {
        String dbType = PlatAppUtil.getDbType();
        TableInfo info = null;
        Map<String, Object> map = null;
        if ("ORACLE".equals(dbType)) {
            map = this.getOracleTableInfo(tableName);
        } else if ("MYSQL".equals(dbType)) {
            map = this.getMySqlTableInfo(tableName);
        } else if ("SQLSERVER".equals(dbType)) {
            map = this.getSqlServerTableInfo(tableName);
        }
        info = new TableInfo((String) map.get("TABLE_NAME")
                , (String) map.get("COMMENTS"));
        return info;
    }

    /**
     * 根据表名称和列名称获取列信息
     *
     * @param tableName
     * @param columnName
     * @return
     */
    @Override
    public TableColumn getTableColumn(String tableName, String columnName) {
        String dbType = PlatAppUtil.getDbType();
        TableColumn column = null;
        if ("ORACLE".equals(dbType)) {
            List<Map<String, Object>> list = dao.findOracleTableColumns(tableName, columnName);
            Map<String, Object> map = list.get(0);
            column = dao.getOracleTableColumn(map);
        } else if ("MYSQL".equals(dbType)) {
            List<Map<String, Object>> list = dao.findMySqlTableColumns(tableName, columnName);
            Map<String, Object> map = list.get(0);
            column = dao.getMySqlTableColumn(map);
        } else if ("SQLSERVER".equals(dbType)) {
            List<Map<String, Object>> list = dao.findSqlServerTableColumns(tableName, columnName);
            Map<String, Object> map = list.get(0);
            column = dao.getSqlServerTableColumn(map);
        }
        return column;
    }


    /**
     * 根据sqlfilter获取数据库的字段列表
     *
     * @param filter
     * @return
     */
    @Override
    public List<Map> findTableColumnByFilter(SqlFilter filter) {
        String TABLE_NAME = filter.getRequest().getParameter("TABLE_NAME");
        List<Map> list = new ArrayList<Map>();
        if (StringUtils.isNotEmpty(TABLE_NAME)) {
            List<TableColumn> columns = dao.findTableColumnByTableName(TABLE_NAME);
            for (TableColumn column : columns) {
                Map<String, Object> map = new HashMap<String, Object>();
                Field[] declaredFields = column.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    try {
                        map.put(field.getName(), field.get(column));
                    } catch (IllegalArgumentException e) {
                        PlatLogUtil.printStackTrace(e);
                    } catch (IllegalAccessException e) {
                        PlatLogUtil.printStackTrace(e);
                    }
                }
                column.setOldColumnName(column.getColumnName());
                map.put("oldColumnName", column.getColumnName());
                map.put("isNewColumn", "-1");
                list.add(map);
            }
        }
        return list;
    }


    /**
     * 判断一张表是否存在数据库中
     *
     * @param tableName:表名称
     * @param isIncludeThisTable:判断是否包含该传入参数表
     * @return
     */
    @Override
    public boolean isExistsTable(String tableName, boolean isIncludeThisTable) {
        return dao.isExistsTable(tableName.toUpperCase(), isIncludeThisTable);
    }

    /**
     * 判断是否存在该列
     *
     * @param columnName:列名称
     * @param isIncludeThisColumnName:是否包含该列
     * @return
     */
    @Override
    public boolean isExistsColumn(String columnName, boolean isIncludeThisColumnName) {
        List<TableColumn> tableColumns = (List<TableColumn>)
                PlatAppUtil.getSessionCache("tableColumns");
        if (tableColumns != null && tableColumns.size() > 0) {
            Set<String> columnNames = new HashSet<String>();
            for (TableColumn column : tableColumns) {
                if (!isIncludeThisColumnName) {
                    if (!column.getColumnName().equals(columnName)) {
                        columnNames.add(column.getColumnName());
                    }
                } else {
                    columnNames.add(column.getColumnName());
                }
            }
            if (columnNames.contains(columnName)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 获取新增或者修改的列
     *
     * @param columns
     * @param type    1:新增的列 2:修改的列
     * @return
     */
    private List<TableColumn> getNewOrUpdateColumns(List<TableColumn> columns, int type, String tableName) {
        List<TableColumn> targetColumns = new ArrayList<TableColumn>();
        for (TableColumn column : columns) {
            switch (type) {
                case 1:
                    if ("1".equals(column.getIsNewColumn())) {
                        targetColumns.add(column);
                    }
                    break;
                case 2:
                    if (!"1".equals(column.getIsNewColumn())) {
                        TableColumn oldTableColumn = this.getTableColumn(tableName,
                                column.getOldColumnName());
                        if (!oldTableColumn.equals(column)) {
                            targetColumns.add(column);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return targetColumns;
    }

    /**
     * 创建oracle新的数据库列
     *
     * @param newColumns
     * @param tableName
     */
    private void addNewOracleTableColumn(List<TableColumn> newColumns, String tableName) {
        for (int i = 0; i < newColumns.size(); i++) {
            TableColumn column = newColumns.get(i);
            int columnType = column.getColumnType();
            String isNullable = column.getIsNullable();
            StringBuffer sql = new StringBuffer("alter table ");
            sql.append(tableName).append(" ADD ");
            sql.append(column.getColumnName()).append(" ");
            appendOracleColumnType(column, columnType, sql);
            if (StringUtils.isNotEmpty(column.getDefaultValue())) {
                sql.append("default ").append(column.getDefaultValue()).append(" ");
            }
            if ("-1".equals(isNullable)) {
                sql.append("NOT NULL");
            }
            dao.executeSql(sql.toString(), null);
            sql = new StringBuffer("");
            sql.append("comment on column ").append(tableName).append(".");
            sql.append(column.getColumnName()).append(" is '");
            sql.append(column.getColumnComments()).append("'");
            dao.executeSql(sql.toString(), null);
        }
    }

    /**
     * 拼接oracle的数据库列
     *
     * @param column
     * @param columnType
     * @param sql
     */
    private void appendOracleColumnType(TableColumn column, int columnType,
                                        StringBuffer sql) {
        switch (columnType) {
            case TableColumn.COLUMN_TYPE_STRING:
                sql.append("VARCHAR2(").append(column.getDataLength()).append(") ");
                break;
            case TableColumn.COLUMN_TYPE_NUMBER:
                sql.append("NUMBER ");
                break;
            case TableColumn.COLUMN_TYPE_DATE:
                sql.append("DATE ");
                break;
            case TableColumn.COLUMN_TYPE_TIMESTAMP:
                sql.append("TIMESTAMP ");
                break;
            case TableColumn.COLUMN_TYPE_TEXT:
                sql.append("CLOB ");
                break;
            default:
                break;
        }
    }

    /**
     * 修改oracle的数据库列
     *
     * @param modifyColumns
     * @param tableName
     */
    private void updateOracleTableColumn(List<TableColumn> modifyColumns, String tableName) {
        StringBuffer sql = null;
        for (int i = 0; i < modifyColumns.size(); i++) {
            TableColumn column = modifyColumns.get(i);
            TableColumn oldColumn = null;
            if (StringUtils.isNotEmpty(column.getOldColumnName())) {
                oldColumn = this.getTableColumn(tableName, column.getOldColumnName());
            } else {
                oldColumn = this.getTableColumn(tableName, column.getColumnName());
            }
            int columnType = column.getColumnType();
            String isNullable = column.getIsNullable();
            if (StringUtils.isNotEmpty(column.getOldColumnName()) &&
                    !column.getColumnName().equals(column.getOldColumnName())) {
                sql = new StringBuffer("alter table ");
                sql.append(tableName).append(" rename column ");
                sql.append(column.getOldColumnName()).append(" TO ");
                sql.append(column.getColumnName());
                dao.executeSql(sql.toString(), null);
            }
            sql = new StringBuffer("alter table ");
            sql.append(tableName).append(" modify ");
            sql.append(column.getColumnName()).append(" ");
            appendOracleColumnType(column, columnType, sql);
            if (StringUtils.isNotEmpty(column.getDefaultValue())) {
                sql.append("default ").append(column.getDefaultValue()).append(" ");
            }
            if (!oldColumn.getIsNullable().equals(column.getIsNullable())) {
                if ("-1".equals(isNullable)) {
                    sql.append("NOT NULL");
                } else {
                    sql.append("NULL");
                }
            }
            dao.executeSql(sql.toString(), null);
            sql = new StringBuffer("");
            sql.append("comment on column ").append(tableName).append(".");
            sql.append(column.getColumnName()).append(" is '");
            sql.append(column.getColumnComments()).append("'");
            dao.executeSql(sql.toString(), null);
        }
    }

    /**
     * 删除oracle的列
     *
     * @param tableName
     */
    private void dropOracleTableColumn(String tableName, List<TableColumn> tableColumns) {
        //获取旧的列
        List<TableColumn> oldColumns = dao.findTableColumnByTableName(tableName);
        Set<String> curColNameSet = new HashSet<String>();
        for (TableColumn curCol : tableColumns) {
            if (StringUtils.isNotEmpty(curCol.getOldColumnName())) {
                curColNameSet.add(curCol.getOldColumnName());
            } else {
                curColNameSet.add(curCol.getColumnName());
            }
        }
        for (TableColumn oldCol : oldColumns) {
            if (!curColNameSet.contains(oldCol.getColumnName())) {
                StringBuffer sql = new StringBuffer("alter table ");
                sql.append(tableName).append(" drop column ");
                sql.append(oldCol.getColumnName()).append(" cascade constraint ");
                dao.executeSql(sql.toString(), null);
            }
        }
    }

    /**
     * 删除oracle表的所有约束
     *
     * @param tableName
     */
    private void deleteOracleConstraint(String tableName) {
        List<Map<String, Object>> list = dao.findOracleConstraints(tableName);
        for (Map<String, Object> map : list) {
            String CONSTRAINT_NAME = (String) map.get("CONSTRAINT_NAME");
            StringBuffer sql = new StringBuffer("ALTER table ");
            sql.append(tableName).append(" DROP CONSTRAINT ");
            sql.append(CONSTRAINT_NAME);
            dao.executeSql(sql.toString(), null);
            ;
        }
    }

    /**
     * 新增或者修改oracle的表结构
     *
     * @param isEdit
     * @param tableName
     * @param tableComments
     * @param tableColumns
     */
    private void saveOrUpdateOracleTable(String isEdit, String tableName, String tableComments
            , List<TableColumn> tableColumns, String oldTableName) {
        //获取私有主键列表
        List<TableColumn> pkColumns = this.getConstraintColumns(tableColumns, 1);
        //获取唯一约束列表
        List<TableColumn> unColumns = this.getConstraintColumns(tableColumns, 2);
        //获取外键列表
        List<TableColumn> foreignColumns = this.getConstraintColumns(tableColumns, 3);
        if ("true".equals(isEdit)) {
            StringBuffer sql = null;
            if (!oldTableName.equals(tableName)) {
                sql = new StringBuffer("ALTER TABLE ");
                sql.append(oldTableName).append(" RENAME TO ");
                sql.append(tableName);
                dao.executeSql(sql.toString(), null);
            }
            sql = new StringBuffer("");
            sql.append("comment on table ").append(tableName).append(" is ");
            sql.append("'").append(tableComments).append("'");
            dao.executeSql(sql.toString(), null);
            //删除oracle的列
            this.dropOracleTableColumn(tableName, tableColumns);
            //获取修改的列
            List<TableColumn> modifyColumns = this.getNewOrUpdateColumns(tableColumns, 2, tableName);
            //修改数据库的列
            this.updateOracleTableColumn(modifyColumns, tableName);
            //获取新增的列
            List<TableColumn> newColumns = this.getNewOrUpdateColumns(tableColumns, 1, tableName);
            //创建新的列
            this.addNewOracleTableColumn(newColumns, tableName);
            //去除约束
            this.deleteOracleConstraint(tableName);
            addConstraint(tableName, pkColumns, unColumns, foreignColumns);
        } else {
            StringBuffer sql = new StringBuffer("CREATE TABLE ");
            sql.append(tableName).append("(");
            for (int i = 0; i < tableColumns.size(); i++) {
                TableColumn column = tableColumns.get(i);
                int columnType = column.getColumnType();
                String isNullable = column.getIsNullable();
                if (i > 0) {
                    sql.append(",");
                }
                sql.append(column.getColumnName()).append(" ");
                appendOracleColumnType(column, columnType, sql);
                if (StringUtils.isNotEmpty(column.getDefaultValue())) {
                    sql.append("default ").append(column.getDefaultValue()).append(" ");
                }
                if ("-1".equals(isNullable)) {
                    sql.append("NOT NULL");
                }
            }
            sql.append(")");
            dao.executeSql(sql.toString(), null);
            sql = new StringBuffer("");
            sql.append("comment on table ").append(tableName).append(" is ");
            sql.append("'").append(tableComments).append("'");
            dao.executeSql(sql.toString(), null);
            for (TableColumn tableColumn : tableColumns) {
                sql = new StringBuffer("");
                sql.append("comment on column ").append(tableName).append(".");
                sql.append(tableColumn.getColumnName()).append(" is '");
                sql.append(tableColumn.getColumnComments()).append("'");
                dao.executeSql(sql.toString(), null);
            }
            addConstraint(tableName, pkColumns, unColumns, foreignColumns);
        }
    }

    /**
     * 添加oracle的各种约束
     *
     * @param tableName
     * @param pkColumns
     * @param unColumns
     * @param foreignColumns
     */
    private void addConstraint(String tableName, List<TableColumn> pkColumns,
                               List<TableColumn> unColumns, List<TableColumn> foreignColumns) {
        StringBuffer sql;
        //--------------开始追加主键的约束-------------------
        sql = new StringBuffer("ALTER TABLE ");
        sql.append(tableName).append(" add constraint");
        sql.append(" ").append(tableName).append("_PK primary key (");
        for (int i = 0; i < pkColumns.size(); i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append(pkColumns.get(i).getColumnName());
        }
        sql.append(")");
        dao.executeSql(sql.toString(), null);
        //--------------结束追加主键的约束-------------------
        //--------------开始追加唯一约束--------------------
        for (int i = 0; i < unColumns.size(); i++) {
            TableColumn column = unColumns.get(i);
            sql = new StringBuffer("ALTER TABLE ");
            sql.append(tableName).append(" add constraint");
            sql.append(" ").append(tableName).append("_UK");
            if (i > 0) {
                sql.append(i);
            }
            sql.append(" unique (").append(column.getColumnName()).append(")");
            dao.executeSql(sql.toString(), null);
        }
        //--------------结束追加唯一约束--------------------
        //--------------开始追加外键约束-------------------
        for (int i = 0; i < foreignColumns.size(); i++) {
            TableColumn column = foreignColumns.get(i);
            sql = new StringBuffer("ALTER TABLE ");
            sql.append(tableName).append(" add constraint");
            sql.append(" ").append(tableName).append("_FK");
            if (i > 0) {
                sql.append(i);
            }
            sql.append(" foreign key(").append(column.getColumnName()).append(")");
            sql.append(" references ").append(column.getForeignRefTableName());
            sql.append("(").append(column.getForeignRefColumnName()).append(")");
            dao.executeSql(sql.toString(), null);
        }
        //--------------结束追加外键约束-------------------
    }

    /**
     * 获取约束类型的列
     *
     * @param tableColumns:源列数据
     * @param constraintType    1:主键类型 2:唯一性类型 3:外键类型
     * @return
     */
    private List<TableColumn> getConstraintColumns(List<TableColumn> tableColumns, int constraintType) {
        List<TableColumn> targetColumns = new ArrayList<TableColumn>();
        switch (constraintType) {
            case 1:
                for (TableColumn column : tableColumns) {
                    if ("1".equals(column.getIsPrimaryKey())) {
                        targetColumns.add(column);
                    }
                }
                break;
            case 2:
                for (TableColumn column : tableColumns) {
                    if ("1".equals(column.getIsUnique())) {
                        targetColumns.add(column);
                    }
                }
                break;
            case 3:
                /*for(TableColumn column:tableColumns){
                    if(column.getIsForeign().equals("1")){
                        targetColumns.add(column);
                    }
                }*/
                break;
            default:
                break;
        }
        return targetColumns;
    }

    /**
     * 获取MYSQL添加约束语句
     *
     * @param tableName
     * @param pkColumns
     * @param unColumns
     * @param foreignColumns
     * @return
     */
    public List<String> getMysqlAddConstraintSql(String tableName, List<TableColumn> pkColumns,
                                                 List<TableColumn> unColumns, List<TableColumn> foreignColumns) {
        List<String> sqlList = new ArrayList<String>();
        StringBuffer sql;
        //--------------开始追加主键的约束-------------------
        sql = new StringBuffer("ALTER TABLE ");
        sql.append(tableName).append(" add PRIMARY");
        sql.append(" ").append("KEY (");
        for (int i = 0; i < pkColumns.size(); i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append(pkColumns.get(i).getColumnName());
        }
        sql.append(")");
        sqlList.add(sql.toString());
        //--------------结束追加主键的约束-------------------
        //--------------开始追加唯一约束--------------------
        for (int i = 0; i < unColumns.size(); i++) {
            TableColumn column = unColumns.get(i);
            sql = new StringBuffer("ALTER TABLE ");
            sql.append(tableName).append(" add UNIQUE");
            sql.append(" ").append(tableName);
            if (i == 0) {
                sql.append("_UK");
            } else {
                sql.append("_UK" + i);
            }
            if (i > 0) {
                sql.append(i);
            }
            sql.append(" (").append(column.getColumnName()).append(")");
            sqlList.add(sql.toString());
        }
        return sqlList;
    }

    /**
     * 添加mysql的各种约束
     *
     * @param tableName
     * @param pkColumns
     * @param unColumns
     * @param foreignColumns
     */
    private void addMySqlConstraint(String tableName, List<TableColumn> pkColumns,
                                    List<TableColumn> unColumns, List<TableColumn> foreignColumns) {
        StringBuffer sql;
        //--------------开始追加主键的约束-------------------
        sql = new StringBuffer("ALTER TABLE ");
        sql.append(tableName).append(" add PRIMARY");
        sql.append(" ").append("KEY (");
        for (int i = 0; i < pkColumns.size(); i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append(pkColumns.get(i).getColumnName());
        }
        sql.append(")");
        dao.executeSql(sql.toString(), null);
        //--------------结束追加主键的约束-------------------
        //--------------开始追加唯一约束--------------------
        for (int i = 0; i < unColumns.size(); i++) {
            TableColumn column = unColumns.get(i);
            sql = new StringBuffer("ALTER TABLE ");
            sql.append(tableName).append(" add UNIQUE");
            sql.append(" ").append(tableName);
            if (i == 0) {
                sql.append("_UK");
            } else {
                sql.append("_UK" + i);
            }
            if (i > 0) {
                sql.append(i);
            }
            sql.append(" (").append(column.getColumnName()).append(")");
            dao.executeSql(sql.toString(), null);
        }
        //--------------结束追加唯一约束--------------------
    }

    /**
     * 获取SqlServer 语句列表
     *
     * @param tableName
     * @param pkColumns
     * @param unColumns
     * @param foreignColumns
     * @return
     */
    private List<String> getSqlServerConstraintSql(String tableName, List<TableColumn> pkColumns,
                                                   List<TableColumn> unColumns, List<TableColumn> foreignColumns) {
        List<String> sqlList = new ArrayList<String>();
        StringBuffer sql = new StringBuffer("ALTER TABLE ");
        sql.append(tableName).append(" add PRIMARY");
        sql.append(" ").append("KEY (");
        for (int i = 0; i < pkColumns.size(); i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append(pkColumns.get(i).getColumnName());
        }
        sql.append(")");
        sqlList.add(sql.toString());
        for (int i = 0; i < unColumns.size(); i++) {
            TableColumn column = unColumns.get(i);
            sql = new StringBuffer("ALTER TABLE ");
            sql.append(tableName).append(" add CONSTRAINT ");
            sql.append(" ").append(tableName);
            if (i == 0) {
                sql.append("_UK");
            } else {
                sql.append("_UK" + i);
            }
            if (i > 0) {
                sql.append(i);
            }
            sql.append(" UNIQUE ");
            sql.append(" (").append(column.getColumnName()).append(")");
            sqlList.add(sql.toString());
        }
        return sqlList;
    }

    /**
     * 添加sqlServer的各种约束
     *
     * @param tableName
     * @param pkColumns
     * @param unColumns
     * @param foreignColumns
     */
    private void addSqlServerConstraint(String tableName, List<TableColumn> pkColumns,
                                        List<TableColumn> unColumns, List<TableColumn> foreignColumns) {
        List<String> sqlList = this.getSqlServerConstraintSql(tableName, pkColumns, unColumns, foreignColumns);
        for (String sql : sqlList) {
            dao.executeSql(sql, null);
        }
    }

    /**
     * 删除oracle的列
     *
     * @param tableName
     */
    private void dropTableColumn(String tableName, List<TableColumn> tableColumns) {
        //获取旧的列
        List<TableColumn> oldColumns = dao.findTableColumnByTableName(tableName);
        Set<String> curColNameSet = new HashSet<String>();
        for (TableColumn curCol : tableColumns) {
            if (StringUtils.isNotEmpty(curCol.getOldColumnName())) {
                curColNameSet.add(curCol.getOldColumnName());
            } else {
                curColNameSet.add(curCol.getColumnName());
            }
        }
        String dbType = PlatAppUtil.getDbType();
        for (TableColumn oldCol : oldColumns) {
            if (!curColNameSet.contains(oldCol.getColumnName())) {
                StringBuffer sql = new StringBuffer("alter table ");
                sql.append(tableName).append(" drop column ");
                sql.append(oldCol.getColumnName());
                if ("ORACLE".equals(dbType)) {
                    sql.append(" cascade constraint ");
                }
                dao.executeSql(sql.toString(), null);
            }
        }
    }

    /**
     * 拼接sqlServer的数据库列
     *
     * @param column
     * @param columnType
     * @param sql
     */
    private void appendSqlServerColumnType(TableColumn column, int columnType,
                                           StringBuffer sql) {
        switch (columnType) {
            case TableColumn.COLUMN_TYPE_STRING:
                sql.append("VARCHAR(").append(column.getDataLength()).append(") ");
                break;
            case TableColumn.COLUMN_TYPE_NUMBER:
                if (StringUtils.isNotEmpty(column.getDataLength())) {
                    sql.append("decimal (").append(column.getDataLength());
                    if (StringUtils.isNotEmpty(column.getScale())) {
                        sql.append(",").append(column.getScale()).append(") ");
                    } else {
                        sql.append(") ");
                    }
                } else {
                    sql.append("decimal ");
                }
                break;
            case TableColumn.COLUMN_TYPE_DATE:
                sql.append("DATE ");
                break;
            case TableColumn.COLUMN_TYPE_TIMESTAMP:
                sql.append("datetime ");
                break;
            case TableColumn.COLUMN_TYPE_TEXT:
                sql.append("TEXT ");
                break;
            case TableColumn.COLUMN_TYPE_INT:
                sql.append("INT ");
                break;
            default:
                break;
        }
    }

    /**
     * 拼接mysql的数据库列
     *
     * @param column
     * @param columnType
     * @param sql
     */
    private void appendMySqlColumnType(TableColumn column, int columnType,
                                       StringBuffer sql) {
        switch (columnType) {
            case TableColumn.COLUMN_TYPE_STRING:
                sql.append("VARCHAR(").append(column.getDataLength()).append(") ");
                break;
            case TableColumn.COLUMN_TYPE_NUMBER:
                if (StringUtils.isNotEmpty(column.getDataLength())) {
                    sql.append("decimal (").append(column.getDataLength());
                    if (StringUtils.isNotEmpty(column.getScale())) {
                        sql.append(",").append(column.getScale()).append(") ");
                    } else {
                        sql.append(") ");
                    }
                } else {
                    sql.append("decimal ");
                }
                break;
            case TableColumn.COLUMN_TYPE_DATE:
                sql.append("DATE ");
                break;
            case TableColumn.COLUMN_TYPE_TIMESTAMP:
                sql.append("TIMESTAMP ");
                break;
            case TableColumn.COLUMN_TYPE_TEXT:
                sql.append("TEXT ");
                break;
            case TableColumn.COLUMN_TYPE_INT:
                sql.append("INT ");
                break;
            default:
                break;
        }
    }

    /**
     * 修改oracle的数据库列
     *
     * @param modifyColumns
     * @param tableName
     */
    private void updateTableColumn(List<TableColumn> modifyColumns, String tableName) {
        StringBuffer sql = null;
        String dbType = PlatAppUtil.getDbType();
        for (int i = 0; i < modifyColumns.size(); i++) {
            TableColumn column = modifyColumns.get(i);
            TableColumn oldColumn = null;
            if (StringUtils.isNotEmpty(column.getOldColumnName())) {
                oldColumn = this.getTableColumn(tableName, column.getOldColumnName());
            } else {
                oldColumn = this.getTableColumn(tableName, column.getColumnName());
            }
            int columnType = column.getColumnType();
            String isNullable = column.getIsNullable();
            if (StringUtils.isNotEmpty(column.getOldColumnName()) &&
                    !column.getColumnName().equals(column.getOldColumnName())) {
                sql = new StringBuffer("alter table ");
                sql.append(tableName).append(" rename column ");
                sql.append(column.getOldColumnName()).append(" TO ");
                sql.append(column.getColumnName());
                dao.executeSql(sql.toString(), null);
            }
            sql = new StringBuffer("alter table ");
            sql.append(tableName).append(" modify ");
            sql.append(column.getColumnName()).append(" ");
            if ("ORACLE".equals(dbType)) {
                appendOracleColumnType(column, columnType, sql);
            } else if ("MYSQL".equals(dbType)) {
                this.appendMySqlColumnType(column, columnType, sql);
            } else if ("SQLSERVER".equals(dbType)) {
                this.appendSqlServerColumnType(oldColumn, columnType, sql);
            }
            if (StringUtils.isNotEmpty(column.getDefaultValue())) {
                sql.append("default ").append(column.getDefaultValue()).append(" ");
            }
            if (!oldColumn.getIsNullable().equals(column.getIsNullable())) {
                if ("-1".equals(isNullable)) {
                    sql.append("NOT NULL");
                } else {
                    sql.append("NULL");
                }
            }
            if ("ORACLE".equals(dbType)) {
                dao.executeSql(sql.toString(), null);
                sql = new StringBuffer("");
                sql.append("comment on column ").append(tableName).append(".");
                sql.append(column.getColumnName()).append(" is '");
                sql.append(column.getColumnComments()).append("'");
                dao.executeSql(sql.toString(), null);
            } else if ("MYSQL".equals(dbType)) {
                sql.append(" comment '").append(column.getColumnComments()).append("' ");
                dao.executeSql(sql.toString(), null);

            }

        }
    }

    /**
     * 创建新的数据库列
     *
     * @param newColumns
     * @param tableName
     */
    private void addNewTableColumn(List<TableColumn> newColumns, String tableName) {
        String dbType = PlatAppUtil.getDbType();
        for (int i = 0; i < newColumns.size(); i++) {
            TableColumn column = newColumns.get(i);
            int columnType = column.getColumnType();
            String isNullable = column.getIsNullable();
            StringBuffer sql = new StringBuffer("alter table ");
            sql.append(tableName).append(" ADD ");
            sql.append(column.getColumnName()).append(" ");
            if ("ORACLE".equals(dbType)) {
                appendOracleColumnType(column, columnType, sql);
            } else if ("MYSQL".equals(dbType)) {
                this.appendMySqlColumnType(column, columnType, sql);
            } else if ("SQLSERVER".equals(dbType)) {
                this.appendSqlServerColumnType(column, columnType, sql);
            }
            if (StringUtils.isNotEmpty(column.getDefaultValue())) {
                sql.append("default ").append(column.getDefaultValue()).append(" ");
            }
            if ("-1".equals(isNullable)) {
                sql.append("NOT NULL");
            }
            if ("ORACLE".equals(dbType)) {
                dao.executeSql(sql.toString(), null);
                sql = new StringBuffer("");
                sql.append("comment on column ").append(tableName).append(".");
                sql.append(column.getColumnName()).append(" is '");
                sql.append(column.getColumnComments()).append("'");
            } else if ("MYSQL".equals(dbType)) {
               /* sql = new StringBuffer("");
                sql.append("alter table ").append(tableName).append("");
                sql.append(" modify column ");*/
                sql.append(" comment '").append(column.getColumnComments()).append("' ");
            }

            dao.executeSql(sql.toString(), null);
        }
    }

    /**
     * 新增或者修改sqlServer的表结构
     *
     * @param isEdit
     * @param tableName
     * @param tableComments
     * @param tableColumns
     * @param oldTableName
     */
    private void saveOrUpdateSqlServerTable(String isEdit, String tableName, String tableComments
            , List<TableColumn> tableColumns, String oldTableName) {
        //获取私有主键列表
        List<TableColumn> pkColumns = this.getConstraintColumns(tableColumns, 1);
        //获取唯一约束列表
        List<TableColumn> unColumns = this.getConstraintColumns(tableColumns, 2);
        //获取外键列表
        List<TableColumn> foreignColumns = this.getConstraintColumns(tableColumns, 3);
        if ("true".equals(isEdit)) {
            StringBuffer sql = null;
            if (!oldTableName.equals(tableName)) {
                sql = new StringBuffer("EXEC sp_rename '");
                sql.append(oldTableName).append("','");
                sql.append(tableName).append("'");
                dao.executeSql(sql.toString(), null);
            }
            sql = new StringBuffer("EXEC sys.sp_updateextendedproperty @name=N'MS_Description',");
            sql.append("@value=N'").append(tableComments).append("',@level0type=N'SCHEMA',@level0name=N'dbo',");
            sql.append("@level1type=N'TABLE',@level1name=N'").append(tableName).append("'");
            dao.executeSql(sql.toString(), null);
            //删除oracle的列
            this.dropTableColumn(tableName, tableColumns);
            //获取修改的列
            List<TableColumn> modifyColumns = this.getNewOrUpdateColumns(tableColumns, 2, tableName);
            //修改数据库的列
            this.updateTableColumn(modifyColumns, tableName);
            //获取新增的列
            List<TableColumn> newColumns = this.getNewOrUpdateColumns(tableColumns, 1, tableName);
            //创建新的列
            this.addNewTableColumn(newColumns, tableName);
            //给表字段加上注释
            for (int i = 0; i < newColumns.size(); i++) {
                TableColumn column = newColumns.get(i);
                String columnComments = column.getColumnComments();
                sql = new StringBuffer("EXECUTE sp_addextendedproperty 'MS_Description','");
                sql.append(columnComments).append("','user','dbo','table','");
                sql.append(tableName).append("','column','").append(column.getColumnName()).append("'");
                dao.executeSql(sql.toString(), null);
            }
           /* //去除主键约束
            sql = new StringBuffer("");
            sql.append("ALTER TABLE ").append(tableName).append(" drop primary key ");
            try{
                dao.executeSql(sql.toString(), null);
            }catch(Exception e){
                e.printStackTrace();
            }
            //去除唯一约束
            for(int i=0;i<unColumns.size();i++){
                sql = new StringBuffer("");
                sql.append("ALTER TABLE ").append(tableName);
                sql.append(" DROP INDEX ").append(tableName);
                if(i==0){
                    sql.append("_UK");
                }else{
                    sql.append("_UK").append(i);
                }
                try{
                    dao.executeSql(sql.toString(), null);
                }catch(Exception e){
                    PlatLogUtil.printStackTrace(e);
                }
            }
            addSqlServerConstraint(tableName, pkColumns, unColumns, foreignColumns);*/
        } else {
            String sql = this.getSqlServerCreateTableSql(tableName, tableComments, tableColumns);
            dao.executeSql(sql, null);
            //开始给表加上注释
            sql = this.getSqlServerAddTableCommentSql(tableName, tableComments);
            dao.executeSql(sql, null);
            List<String> addCommentList = this.getSqlServerAddColumnComment(tableColumns, tableName);
            for (String addCommnentSql : addCommentList) {
                dao.executeSql(addCommnentSql, null);
            }
            //给表加上各种约束
            addSqlServerConstraint(tableName, pkColumns, unColumns, foreignColumns);
        }
    }

    /**
     * 获取sqlServer的注释列表
     *
     * @param tableColumns
     * @param tableName
     * @return
     */
    private List<String> getSqlServerAddColumnComment(List<TableColumn> tableColumns, String tableName) {
        List<String> sqlList = new ArrayList<String>();
        for (int i = 0; i < tableColumns.size(); i++) {
            TableColumn column = tableColumns.get(i);
            String columnComments = column.getColumnComments();
            StringBuffer sql = new StringBuffer("EXECUTE sp_addextendedproperty 'MS_Description','");
            sql.append(columnComments).append("','user','dbo','table','");
            sql.append(tableName).append("','column','").append(column.getColumnName()).append("'");
            sqlList.add(sql.toString());
        }
        return sqlList;
    }

    /**
     * 获取SqlServer的创建表语句
     *
     * @param tableName
     * @param tableComments
     * @param tableColumns
     * @return
     */
    public String getSqlServerCreateTableSql(String tableName, String tableComments
            , List<TableColumn> tableColumns) {
        StringBuffer sql = new StringBuffer("CREATE TABLE ");
        sql.append(tableName).append("(");
        for (int i = 0; i < tableColumns.size(); i++) {
            TableColumn column = tableColumns.get(i);
            int columnType = column.getColumnType();
            String isNullable = column.getIsNullable();
            //String columnComments = column.getColumnComments();
            if (i > 0) {
                sql.append(",");
            }
            sql.append(column.getColumnName()).append(" ");
            appendSqlServerColumnType(column, columnType, sql);
            if (StringUtils.isNotEmpty(column.getDefaultValue())) {
                if (column.getColumnType() == TableColumn.COLUMN_TYPE_TIMESTAMP) {
                    sql.append("default ").append("getdate()").append(" ");
                } else {
                    sql.append("default ").append(column.getDefaultValue()).append(" ");
                }
            }
            if ("-1".equals(isNullable)) {
                sql.append("NOT NULL");
            }
        }
        sql.append(")");
        return sql.toString();
    }

    /**
     * 获取MYSQL的表创建语句
     *
     * @param tableName
     * @param tableComments
     * @param tableColumns
     * @return
     */
    public String getMysqlCreateTableSql(String tableName, String tableComments
            , List<TableColumn> tableColumns) {
        StringBuffer sql = new StringBuffer("CREATE TABLE ");
        sql.append(tableName).append("(");
        for (int i = 0; i < tableColumns.size(); i++) {
            TableColumn column = tableColumns.get(i);
            int columnType = column.getColumnType();
            String isNullable = column.getIsNullable();
            String columnComments = column.getColumnComments();
            if (i > 0) {
                sql.append(",");
            }
            sql.append(column.getColumnName()).append(" ");
            appendMySqlColumnType(column, columnType, sql);
            if (StringUtils.isNotEmpty(column.getDefaultValue()) && !column.getDefaultValue().contains("null")) {
                if (column.getColumnType() == TableColumn.COLUMN_TYPE_TIMESTAMP) {
                    sql.append("default ").append("CURRENT_TIMESTAMP").append(" ");
                } else {
                    sql.append("default ").append(column.getDefaultValue()).append(" ");
                }
            }
            if ("-1".equals(isNullable)) {
                sql.append("NOT NULL");
            }
            if (StringUtils.isNotEmpty(columnComments)) {
                sql.append(" COMMENT '").append(columnComments).append("' ");
            }
        }
        sql.append(")");
        return sql.toString();
    }

    /**
     * 获取MYSQL创建注释语句
     *
     * @param tableName
     * @param tableComments
     * @return
     */
    public String getMySqlAddTableCommentSql(String tableName, String tableComments) {
        StringBuffer sql = new StringBuffer("");
        sql.append("ALTER TABLE ").append(tableName).append(" COMMENT= ");
        sql.append("'").append(tableComments).append("'");
        return sql.toString();
    }

    /**
     * 获取SqlServer创建注释语句
     *
     * @param tableName
     * @param tableComments
     * @return
     */
    public String getSqlServerAddTableCommentSql(String tableName, String tableComments) {
        StringBuffer sql = new StringBuffer("EXEC sys.sp_addextendedproperty @name=N'MS_Description',");
        sql.append("@value=N'").append(tableComments).append("',@level0type=N'SCHEMA',@level0name=N'dbo',");
        sql.append("@level1type=N'TABLE',@level1name=N'").append(tableName).append("'");
        return sql.toString();
    }

    /**
     * 获取MYSQL数据库所有创建表相关语句
     *
     * @param tableName
     * @param tableComments
     * @param tableColumns
     * @return
     */
    @Override
    public List<String> getMysqlCreateTableAllSql(String tableName, String tableComments
            , List<TableColumn> tableColumns) {
        List<String> sqlList = new ArrayList<String>();
        String dropSql = "DROP TABLE IF EXISTS " + tableName;
        sqlList.add(dropSql);
        //获取私有主键列表
        List<TableColumn> pkColumns = this.getConstraintColumns(tableColumns, 1);
        //获取唯一约束列表
        List<TableColumn> unColumns = this.getConstraintColumns(tableColumns, 2);
        //获取外键列表
        List<TableColumn> foreignColumns = this.getConstraintColumns(tableColumns, 3);
        String createTableSql = this.getMysqlCreateTableSql(tableName, tableComments, tableColumns);
        sqlList.add(createTableSql);
        String addTableCommentSql = this.getMySqlAddTableCommentSql(tableName, tableComments);
        sqlList.add(addTableCommentSql);
        List<String> sql2 = this.getMysqlAddConstraintSql(tableName, pkColumns, unColumns, foreignColumns);
        sqlList.addAll(sql2);
        return sqlList;
    }

    /**
     * 新增或者修改MYSQL表结构
     *
     * @param isEdit
     * @param tableName
     * @param tableComments
     * @param tableColumns
     * @param oldTableName
     */
    private void saveOrUpdateMySqlTable(String isEdit, String tableName, String tableComments
            , List<TableColumn> tableColumns, String oldTableName) {
        //获取私有主键列表
        List<TableColumn> pkColumns = this.getConstraintColumns(tableColumns, 1);
        //获取唯一约束列表
        List<TableColumn> unColumns = this.getConstraintColumns(tableColumns, 2);
        //获取外键列表
        List<TableColumn> foreignColumns = this.getConstraintColumns(tableColumns, 3);
        if ("true".equals(isEdit)) {
            StringBuffer sql = null;
            if (!oldTableName.equals(tableName)) {
                sql = new StringBuffer("ALTER TABLE ");
                sql.append(oldTableName).append(" RENAME TO ");
                sql.append(tableName);
                dao.executeSql(sql.toString(), null);
            }
            sql = new StringBuffer("");
            sql.append("ALTER TABLE ").append(tableName).append(" COMMENT= ");
            sql.append("'").append(tableComments).append("'");
            dao.executeSql(sql.toString(), null);
            //删除oracle的列
            this.dropTableColumn(tableName, tableColumns);
            //获取修改的列
            List<TableColumn> modifyColumns = this.getNewOrUpdateColumns(tableColumns, 2, tableName);
            //修改数据库的列
            this.updateTableColumn(modifyColumns, tableName);
            //获取新增的列
            List<TableColumn> newColumns = this.getNewOrUpdateColumns(tableColumns, 1, tableName);
            //创建新的列
            this.addNewTableColumn(newColumns, tableName);
            //去除主键约束
            sql = new StringBuffer("");
            sql.append("ALTER TABLE ").append(tableName).append(" drop primary key ");
            try {
                dao.executeSql(sql.toString(), null);
            } catch (Exception e) {
                PlatLogUtil.printStackTrace(e);
            }
            //去除唯一约束
            for (int i = 0; i < unColumns.size(); i++) {
                sql = new StringBuffer("");
                sql.append("ALTER TABLE ").append(tableName);
                sql.append(" DROP INDEX ").append(tableName);
                if (i == 0) {
                    sql.append("_UK");
                } else {
                    sql.append("_UK").append(i);
                }
                try {
                    dao.executeSql(sql.toString(), null);
                } catch (Exception e) {
                    PlatLogUtil.printStackTrace(e);
                }
            }
            addMySqlConstraint(tableName, pkColumns, unColumns, foreignColumns);
        } else {
            String createTableSql = this.getMysqlCreateTableSql(tableName, tableComments, tableColumns);
            dao.executeSql(createTableSql, null);
            String addTableCommentSql = this.getMySqlAddTableCommentSql(tableName, tableComments);
            dao.executeSql(addTableCommentSql, null);
            addMySqlConstraint(tableName, pkColumns, unColumns, foreignColumns);
        }
    }

    /**
     * 新增或者修改表结构
     *
     * @param isEdit
     * @param tableName
     * @param tableComments
     * @param tableColumns
     */
    @Override
    public void saveOrUpdateTable(String isEdit, String tableName, String tableComments
            , List<TableColumn> tableColumns, String oldTableName) {
        tableName = tableName.toUpperCase();
        String dbType = PlatAppUtil.getDbType();
        if ("ORACLE".equals(dbType)) {
            this.saveOrUpdateOracleTable(isEdit, tableName, tableComments, tableColumns, oldTableName);
        } else if ("MYSQL".equals(dbType)) {
            this.saveOrUpdateMySqlTable(isEdit, tableName, tableComments, tableColumns, oldTableName);
        } else if ("SQLSERVER".equals(dbType)) {
            this.saveOrUpdateSqlServerTable(isEdit, tableName, tableComments, tableColumns, oldTableName);
        }
    }

    /**
     * 删除数据库表
     *
     * @param tableNames
     */
    @Override
    public void deleteTable(String tableNames) {
        String[] tableNameArray = tableNames.split(",");
        String dbType = PlatAppUtil.getDbType();
        for (String tableName : tableNameArray) {
            StringBuffer sql = new StringBuffer("DROP TABLE ");
            sql.append(tableName).append(" ");
            if ("ORACLE".equals(dbType)) {
                sql.append("CASCADE constraints");
            }
            dao.executeSql(sql.toString(), null);
        }
    }

    /**
     * 获取oracle所有的表
     *
     * @return
     */
    private List<Map<String, Object>> findAllOracleTables() {
        StringBuffer sql = new StringBuffer("SELECT T.TABLE_NAME||'('||T.comments||')' AS LABEL");
        sql.append(",T.TABLE_NAME AS VALUE,T.comments FROM USER_TAB_COMMENTS T");
        sql.append(" ORDER BY T.table_name ASC");
        return dao.findBySql(sql.toString(), null, null);
    }

    /**
     * 获取mysql所有的表
     *
     * @return
     */
    private List<Map<String, Object>> findAllMySqlTables() {
        StringBuffer sql = new StringBuffer("SELECT concat(T.TABLE_NAME,'(',T.TABLE_COMMENT,')')");
        sql.append(" AS LABEL,T.TABLE_NAME AS VALUE,T.TABLE_COMMENT AS COMMENTS FROM ");
        sql.append("information_schema.TABLES T ");
        sql.append(" where T.TABLE_SCHEMA=? ORDER BY T.TABLE_NAME ASC");
        String schemaName = PlatAppUtil.getDbSchema();
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), new String[]{schemaName}, null);
        for (Map<String, Object> table : list) {
            table.put("LABEL", table.get("LABEL").toString().toUpperCase());
            table.put("VALUE", table.get("VALUE").toString().toUpperCase());
        }
        return list;
    }

    /**
     * 获取sqlServer的所有表
     *
     * @return
     */
    private List<Map<String, Object>> findAllSqlServerTables() {
        StringBuffer sql = new StringBuffer("select U.NAME as VALUE,");
        sql.append("U.NAME+'('+ cast(sys.extended_properties.value as varchar)+')' AS LABEL");
        sql.append(",cast(sys.extended_properties.value as varchar) AS ");
        sql.append("COMMENTS from sysobjects U LEFT JOIN sys.extended_properties");
        sql.append(" on U.id=sys.extended_properties.major_id ");
        sql.append(" WHERE U.TYPE='U' and sys.extended_properties.minor_id='0' ORDER BY U.NAME ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        for (Map<String, Object> table : list) {
            table.put("LABEL", table.get("LABEL").toString().toUpperCase());
            table.put("VALUE", table.get("VALUE").toString().toUpperCase());
        }
        return list;
    }

    /**
     * 获取所有的表
     *
     * @param queryParam
     * @return
     */
    @Override
    public List<Map<String, Object>> findAllTables(String queryParam) {
        String dbType = PlatAppUtil.getDbType();
        if ("ORACLE".equals(dbType)) {
            return this.findAllOracleTables();
        } else if ("MYSQL".equals(dbType)) {
            return this.findAllMySqlTables();
        } else if ("SQLSERVER".equals(dbType)) {
            return this.findAllSqlServerTables();
        }
        return null;
    }

    /**
     * 根据表名称获取字段列表
     *
     * @param tableName
     * @return
     */
    @Override
    public List<Map<String, Object>> findTableColumns(String tableName) {
        if (StringUtils.isNotEmpty(tableName)) {
            List<TableColumn> columnList = dao.findTableColumnByTableName(tableName);
            List<Map<String, Object>> columnFields = new ArrayList<Map<String, Object>>();
            for (TableColumn column : columnList) {
                Map<String, Object> info = new HashMap<String, Object>();
                String value = column.getColumnName();
                String comments = value + "(" + column.getColumnComments() + ")";
                info.put("VALUE", value);
                info.put("LABEL", comments);
                columnFields.add(info);
            }
            return columnFields;
        } else {
            return null;
        }
    }

    /**
     * 根据SQL语句获取查询了多少张表
     *
     * @param sql
     * @return
     */
    @Override
    public List<String> getSelectTableNames(String sql) {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = null;
        try {
            select = (Select) parserManager.parse(new StringReader(sql));
        } catch (JSQLParserException e) {
            PlatLogUtil.printStackTrace(e);
        }
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> result = tablesNamesFinder.getTableList(select);
        return result;
    }

    /**
     * 根据sql语句获取字段列表
     *
     * @param sql
     * @return
     */
    @Override
    public List<Map<String, Object>> findFieldColumnsBySql(String sql) {
        List<String> tableNames = this.getSelectTableNames(sql);
        List<String> colNames = dao.findQueryColNamesBySql(sql);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (String colName : colNames) {
            Map<String, Object> colInfo = new HashMap<String, Object>();
            colInfo.put("FIELD_NAME", colName);
            String FIELD_COMMENT = dao.getColumnComments(tableNames, colName);
            if (StringUtils.isEmpty(FIELD_COMMENT)) {
                FIELD_COMMENT = colName;
            }
            colInfo.put("FIELD_COMMENT", FIELD_COMMENT);
            list.add(colInfo);
        }
        return list;
    }

    /**
     * 判断一个视图是否存在数据库中
     *
     * @param viewName
     * @param isIncludeThisView
     * @return
     */
    @Override
    public boolean isExistsView(String viewName, boolean isIncludeThisView) {
        return dao.isExistsView(viewName, isIncludeThisView);
    }

    /**
     * 根据接口获取字段列表
     *
     * @param fieldInfo
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map<String, Object>> findFieldColumnsByInterface(Map<String, Object> fieldInfo,
                                                                 SqlFilter sqlFilter) {
        List<Map<String, Object>> columnList = new ArrayList<Map<String, Object>>();
        String JAVA_INTERFACE = (String) fieldInfo.get("JAVA_INTERCODE");
        String ASSOCIAL_TABLENAMES = (String) fieldInfo.get("ASSOCIAL_TABLENAMES");
        List<String> tableNames = null;
        if (StringUtils.isNotEmpty(ASSOCIAL_TABLENAMES)) {
            tableNames = Arrays.asList(ASSOCIAL_TABLENAMES.split(","));
        }
        String beanId = JAVA_INTERFACE.split("[.]")[0];
        String method = JAVA_INTERFACE.split("[.]")[1];
        Object serviceBean = PlatAppUtil.getBean(beanId);
        if (serviceBean != null) {
            Method invokeMethod;
            try {
                invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                        new Class[]{SqlFilter.class, Map.class});
                List<Map<String, Object>> list = (List<Map<String, Object>>) invokeMethod.invoke(serviceBean,
                        new Object[]{sqlFilter, fieldInfo});
                if (list != null && list.size() > 0) {
                    Map<String, Object> map = list.get(0);
                    for (String columnName : map.keySet()) {
                        if (!"CURRENTROW".equals(columnName)) {
                            if (tableNames != null) {
                                String comment = dao.getColumnComments(tableNames, columnName);
                                Map<String, Object> colInfo = new HashMap<String, Object>();
                                colInfo.put("FIELD_NAME", columnName);
                                if (StringUtils.isNotEmpty(comment)) {
                                    colInfo.put("FIELD_COMMENT", comment);
                                } else {
                                    colInfo.put("FIELD_COMMENT", columnName);
                                }
                                columnList.add(colInfo);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
        return columnList;
    }

    /**
     * 根据视图名称获取视图信息
     *
     * @param viewName
     * @return
     */
    @Override
    public Map<String, Object> getViewInfo(String viewName) {
        StringBuffer sql = new StringBuffer("SELECT * FROM USER_VIEWS T");
        sql.append(" WHERE T.VIEW_NAME=? ");
        Map<String, Object> userView = dao.getBySql(sql.toString(), new Object[]{viewName});
        return userView;
    }

    /**
     * 获取sqlServer数据库所有创建表语句
     *
     * @param tableName
     * @param tableComments
     * @param tableColumns
     * @return
     */
    @Override
    public List<String> getSqlServerCreateTableAllSql(String tableName, String tableComments
            , List<TableColumn> tableColumns) {
        List<String> sqlList = new ArrayList<String>();
        StringBuffer dropSql = new StringBuffer("IF EXISTS(Select 1 From Sysobjects Where Name='");
        dropSql.append(tableName).append("') DROP table ").append(tableName);
        sqlList.add(dropSql.toString());
        //获取私有主键列表
        List<TableColumn> pkColumns = this.getConstraintColumns(tableColumns, 1);
        //获取唯一约束列表
        List<TableColumn> unColumns = this.getConstraintColumns(tableColumns, 2);
        //获取外键列表
        List<TableColumn> foreignColumns = this.getConstraintColumns(tableColumns, 3);
        String createTableSql = this.getSqlServerCreateTableSql(tableName, tableComments, tableColumns);
        sqlList.add(createTableSql);
        String addTableCommentSql = this.getSqlServerAddTableCommentSql(tableName, tableComments);
        sqlList.add(addTableCommentSql);
        List<String> sql1 = this.getSqlServerAddColumnComment(tableColumns, tableName);
        sqlList.addAll(sql1);
        List<String> sql2 = this.getSqlServerConstraintSql(tableName, pkColumns, unColumns, foreignColumns);
        sqlList.addAll(sql2);
        return sqlList;
    }

    /**
     * 创建MYSQL表
     *
     * @param sqlList
     */
    @Override
    @DataSource("localmysql")
    public void createMySqlTable(List<String> sqlList) {
        for (String sql : sqlList) {
            dao.executeSql(sql, null);
        }
    }

    /**
     * 保存MYSQL的数据
     *
     * @param dataList
     */
    @Override
    @DataSource("localmysql")
    public void saveMySqlTableDatas(List<Map<String, Object>> dataList, String tableName) {
        String sql = "delete from " + tableName;
        dao.executeSql(sql, null);
        if (dataList != null && dataList.size() > 0) {
            dao.saveBatch(dataList, tableName);
        }
    }

    /**
     * 创建SQLSERVER表
     *
     * @param sqlList
     */
    @Override
    @DataSource("localmssql")
    public void createSqlServerTable(List<String> sqlList) {
        for (String sql : sqlList) {
            dao.executeSql(sql, null);
        }
    }

    /**
     * 保存SqlServer的数据
     *
     * @param dataList
     */
    @Override
    @DataSource("localmssql")
    public void saveSqlServerTableDatas(List<Map<String, Object>> dataList, String tableName) {
        String sql = "delete from " + tableName;
        dao.executeSql(sql, null);
        if (dataList != null && dataList.size() > 0) {
            dao.saveBatch(dataList, tableName);
        }
    }

}
