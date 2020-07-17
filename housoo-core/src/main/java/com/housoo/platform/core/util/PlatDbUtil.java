package com.housoo.platform.core.util;

import com.housoo.platform.core.model.PagingBean;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述 使用dbutils来操纵数据库工具类
 *
 * @author
 * @version 1.0
 * @created 2014年10月28日 上午9:14:57
 */
public class PlatDbUtil {

    // 查询条件值匹配的正则表达式
    private static Pattern STRING_PATTERN1 = Pattern.compile("\\[(.*?)]");
    // 查询条件匹配的正则表达式
    private static Pattern STRING_PATTERN2 = Pattern.compile("\\#\\{(.*?)}");

    /**
     * 描述 执行更新数据语句
     *
     * @param sql
     * @param params
     * @return
     * @author
     * @created 2014年10月28日 上午11:01:39
     */
    public static int update(Connection conn, String sql, Object[] params) {
        return update(conn, sql, params, true);
    }

    /**
     * 描述 执行更新数据语句
     *
     * @param sql
     * @param params
     * @return
     * @author
     * @created 2014年10月28日 上午11:01:39
     */
    public static int update(Connection conn, String sql, Object[] params, boolean isClose) {
        try {
            QueryRunner qRunner = new QueryRunner();
            int n = 0;
            if (params != null && params.length > 0) {
                n = qRunner.update(conn, sql, params);
            } else {
                n = qRunner.update(conn, sql);
            }
            return n;
        } catch (SQLException e) {
            try {
                DbUtils.rollback(conn);
            } catch (SQLException e1) {
                PlatLogUtil.printStackTrace(e1);
            }
            PlatLogUtil.printStackTrace(e);
        } finally {
            if (isClose) {
                DbUtils.closeQuietly(conn);
            }
        }
        return 0;
    }

    /**
     * 描述 根据sql获取唯一对象
     *
     * @param sql
     * @param params
     * @return
     * @author
     * @created 2014年10月28日 上午10:41:38
     */
    public static Object getObjectBySql(Connection conn, String sql, Object[] params) {
        return getObjectBySql(conn, sql, params, true);
    }

    /**
     * 描述 根据sql获取唯一对象
     *
     * @param sql
     * @param params
     * @param closeConn true查询后关闭连接
     * @return
     * @author
     * @created 2014年10月28日 上午10:41:38
     */
    public static Object getObjectBySql(Connection conn, String sql, Object[] params, boolean closeConn) {
        ResultSetHandler rsh = new ScalarHandler();
        try {
            QueryRunner qRunner = new QueryRunner();
            Object result = null;
            if (params != null && params.length > 0) {
                result = qRunner.query(conn, sql, params, rsh);
            } else {
                result = qRunner.query(conn, sql, rsh);
            }
            return result;
        } catch (SQLException e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
//            DbUtils.closeQuietly(conn);
        }
        return null;

    }

    /**
     * 描述 根据sql语句获取MAP
     *
     * @param sql
     * @param params
     * @return
     * @author
     * @created 2014年10月28日 上午10:40:01
     */
    public static Map<String, Object> getMapBySql(Connection conn, String sql, Object[] params) {
        return getMapBySql(conn, sql, params, false);
    }

    /**
     * 描述 根据sql语句获取MAP
     *
     * @param sql
     * @param params
     * @return
     * @author Derek Zhang
     * @created 2015年12月31日 下午2:30:13
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapBySql(Connection conn, String sql, Object[] params, boolean isCloseDb) {
        ResultSetHandler rsh = new MapHandler();
        try {
            QueryRunner qRunner = new QueryRunner();
            Map<String, Object> map = null;
            if (params != null && params.length > 0) {
                map = (Map<String, Object>) qRunner.query(conn, sql, params, rsh);
            } else {
                map = (Map<String, Object>) qRunner.query(conn, sql, rsh);
            }
            if (map == null || map.isEmpty()) {
                return null;
            }
            Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, Object> entry = entries.next();
                if (entry.getValue() instanceof Clob) {
                    map.put(entry.getKey(), PlatStringUtil.clobToString((Clob) entry.getValue()));
                } else if (entry.getValue() instanceof Blob) {
                    map.put(entry.getKey(), PlatStringUtil.blobToString((Blob) entry.getValue(), "GBK"));
                }
            }
            return map;
        } catch (SQLException e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            if (isCloseDb) {
                DbUtils.closeQuietly(conn);
            }
        }
        return null;
    }

    /**
     * 描述 根据sql语句获取MAP
     *
     * @param sql
     * @param params
     * @return
     * @author
     * @created 2014年10月28日 上午10:40:01
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapBySqlBlob(Connection conn, String sql, Object[] params) {
        ResultSetHandler rsh = new MapHandler();
        try {
            QueryRunner qRunner = new QueryRunner();
            Map<String, Object> map = null;
            if (params != null && params.length > 0) {
                map = (Map<String, Object>) qRunner.query(conn, sql, params, rsh);
            } else {
                map = (Map<String, Object>) qRunner.query(conn, sql, rsh);
            }
            Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, Object> entry = entries.next();
                if (entry.getValue() instanceof Clob) {
                    map.put(entry.getKey(), (Clob) entry.getValue());
                } else if (entry.getValue() instanceof Blob) {
                    map.put(entry.getKey(), (Blob) entry.getValue());
                }
            }
            return map;
        } catch (SQLException e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
        }
        return null;
    }

    /**
     * 描述 根据SQL语句获取数量值
     *
     * @param sql
     * @return
     * @author
     * @created 2014年10月28日 下午3:46:08
     */
    public static int getCount(Connection conn, String sql, Object[] params) {
        StringBuffer newSql = new StringBuffer("select count(*) from (").append(sql).append(") tmp_count_t ");
        return Integer.parseInt(PlatDbUtil.getObjectBySql(conn, newSql.toString(), params).toString());
    }

    /**
     * 获取分页的SQL语句面向ORACLE
     *
     * @param sql
     * @param pb
     * @return
     */
    public static String getPagingBeanSqlForOracle(String sql, PagingBean pb) {
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
     * 获取分页的SQL
     *
     * @param sql
     * @param pb
     * @return
     */
    public static String getPagingBeanSqlForMysql(String sql, PagingBean pb) {
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
    public static String getPagingBeanSqlForSqlServer(String sql, PagingBean pb) {
        StringBuffer newSql = new StringBuffer(sql);
        newSql.append(" OFFSET ").append(pb.getStart()).append(" ROW  FETCH NEXT ").append(pb.getPageSize());
        newSql.append(" ROW ONLY ");
        int totalPage = (pb.getTotalItems() + pb.getPageSize() - 1) / pb.getPageSize();
        pb.setTotalPage(totalPage);
        return newSql.toString();
    }

    /**
     * 描述
     *
     * @param conn
     * @param sql
     * @param params
     * @param pb
     * @param dbType
     * @return
     * @author
     * @created 2014年10月28日 下午4:08:48
     */
    public static List<Map<String, Object>> findBySql(Connection conn, String sql, Object[] params, PagingBean pb,
                                                      String dbType) {
        int totalCount = PlatDbUtil.getCount(conn, sql, params);
        pb.setTotalItems(totalCount);
        ResultSetHandler rsh = new MapListHandler();
        int pageSize = pb.getPageSize();
        String newSql = null;
        if ("ORACLE".equals(dbType)) {
            newSql = PlatDbUtil.getPagingBeanSqlForOracle(sql, pb);
        } else if ("MYSQL".equals(dbType)) {
            newSql = PlatDbUtil.getPagingBeanSqlForMysql(sql, pb);
        } else if ("SQLSERVER".equals(dbType)) {
            newSql = PlatDbUtil.getPagingBeanSqlForSqlServer(sql, pb);
        }
        try {
            QueryRunner qRunner = new QueryRunner();
            List<Map<String, Object>> list = null;
            if (params != null && params.length > 0) {
                list = (List<Map<String, Object>>) qRunner.query(conn, newSql.toString(), params, rsh);
            } else {
                list = (List<Map<String, Object>>) qRunner.query(conn, newSql.toString(), rsh);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 描述 转换大字段
     *
     * @param list
     * @return
     * @author
     * @created 2015年10月16日 上午10:11:34
     */
    private static List<Map<String, Object>> conventClobAndBlob(List<Map<String, Object>> list) {
        for (Map<String, Object> map : list) {
            Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, Object> entry = entries.next();
                if (entry.getValue() instanceof Clob) {
                    map.put(entry.getKey(), PlatStringUtil.clobToString((Clob) entry.getValue()));
                } else if (entry.getValue() instanceof Blob) {
                    map.put(entry.getKey(), PlatStringUtil.blobToString((Blob) entry.getValue(), "GBK"));
                }
            }
        }
        return list;
    }


    /**
     * 描述 根据SQL语句获取列表数据
     *
     * @param sql
     * @param params
     * @return
     * @author
     * @created 2014年10月28日 上午9:32:46
     */
    public static List<Map<String, Object>> findBySql(Connection conn, String sql, Object[] params) {
        ResultSetHandler rsh = new MapListHandler();
        try {
            QueryRunner qRunner = new QueryRunner();
            List<Map<String, Object>> list = null;
            if (params != null && params.length > 0) {
                list = (List<Map<String, Object>>) qRunner.query(conn, sql, params, rsh);
            } else {
                list = (List<Map<String, Object>>) qRunner.query(conn, sql, rsh);
            }
            return PlatDbUtil.conventClobAndBlob(list);
        } catch (SQLException e) {
            e.printStackTrace();
            //PlatLogUtil.printStackTrace(e);
        }
        return null;
    }


    /**
     * 描述 根据SQL语句获取列表数据
     *
     * @param sql
     * @param params
     * @return
     * @author
     * @created 2014年10月28日 上午9:32:46
     */
    public static List<Map<String, Object>> findBySqlClob(Connection conn, String sql, Object[] params) {
        ResultSetHandler rsh = new MapListHandler();
        try {
            QueryRunner qRunner = new QueryRunner();
            List<Map<String, Object>> list = null;
            if (params != null && params.length > 0) {
                list = (List<Map<String, Object>>) qRunner.query(conn, sql, params, rsh);
            } else {
                list = (List<Map<String, Object>>) qRunner.query(conn, sql, rsh);
            }
            return PlatDbUtil.conventClobAndBlob(list);
        } catch (SQLException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return null;
    }

    /**
     * 描述 获取连接对象
     *
     * @return
     * @throws SQLException
     * @author
     * @created 2014年10月28日 上午9:33:17
     */
    public static Connection getConnect(String dbUrl, String username, String password) throws SQLException {
        String driver = null;
        if (dbUrl.contains("jdbc:mysql:")) {
            driver = "com.mysql.jdbc.Driver";
        } else if (dbUrl.contains("jdbc:sqlserver:")) {
            driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        }
        if (StringUtils.isNotEmpty(driver)) {
            try {
                Class.forName(driver).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        Connection conn = DriverManager.getConnection(dbUrl, username, password);
        return conn;
    }

    /**
     * 获取条件转换后的值
     *
     * @param condition
     * @param queryParams
     * @param conditionParams
     * @return
     */
    public static String getConditionTrans(String condition,
                                           Map<String, Object> queryParams, List<Object> conditionParams) {
        //获取擦操作符
        String operate = null;
        if (StringUtils.containsIgnoreCase(condition, "LIKE")) {
            operate = "LIKE";
        } else if (StringUtils.containsIgnoreCase(condition, ">=")) {
            operate = ">=";
        } else if (StringUtils.containsIgnoreCase(condition, "<=")) {
            operate = "<=";
        } else if (StringUtils.containsIgnoreCase(condition, "=")) {
            operate = "=";
        } else if (StringUtils.containsIgnoreCase(condition, ">")) {
            operate = ">";
        } else if (StringUtils.containsIgnoreCase(condition, "<")) {
            operate = "<";
        }
        //Pattern p = Pattern.compile("\\[(.*?)]");
        Matcher m = STRING_PATTERN1.matcher(condition);
        String conditionName = null;
        String conditionPre = "";
        String conditionAfter = "";
        while (m.find()) {
            conditionName = m.group(1);
            if ("LIKE".equals(operate)) {
                conditionPre = String.valueOf(condition.charAt(m.start() - 1));
                conditionAfter = String.valueOf(condition.charAt(m.end()));
            }
            break;
        }
        if (queryParams.get(conditionName) != null && StringUtils.
                isNotEmpty(queryParams.get(conditionName).toString())) {
            Object queryValue = queryParams.get(conditionName);
            if ("LIKE".equals(operate)) {
                if ("%".equals(conditionPre) && "%".equals(conditionAfter)) {
                    condition = StringUtils.replace(condition, "'%[" + conditionName + "]%'", "?");
                    conditionParams.add("%" + queryValue + "%");
                } else if ("%".equals(conditionPre)) {
                    condition = StringUtils.replace(condition, "'%[" + conditionName + "]'", "?");
                    conditionParams.add("%" + queryValue);
                } else if ("%".equals(conditionAfter)) {
                    condition = StringUtils.replace(condition, "'[" + conditionName + "]%'", "?");
                    conditionParams.add(queryValue + "%");
                }
            } else {
                condition = StringUtils.replace(condition, "[" + conditionName + "]", "?");
                conditionParams.add(queryValue);
            }
            return condition;
        } else {
            return null;
        }
    }

    /**
     * 获取转换后的SQL
     *
     * @param sql
     * @param queryParams
     * @param conditionParams
     * @return
     */
    private static String getConvertSql(String sql, Map<String, Object> queryParams, List<Object> conditionParams) {
        //开始匹配查询条件
        //Pattern p = Pattern.compile("\\#\\{(.*?)}");
        Matcher m = STRING_PATTERN2.matcher(sql);
        while (m.find()) {
            String conditionSql = m.group(1);
            conditionSql = getConditionTrans(conditionSql, queryParams, conditionParams);
            if (StringUtils.isNotEmpty(conditionSql)) {
                sql = m.replaceFirst(conditionSql);
            } else {
                sql = m.replaceFirst("1=1");
            }
            break;
        }
        return sql;
    }

    /**
     * 转换SQL语句
     *
     * @param sql
     * @param queryParams
     * @param conditionParams
     * @return
     */
    public static String transSqlContent(String sql,
                                         Map<String, Object> queryParams, List<Object> conditionParams) {
        if (conditionParams == null) {
            conditionParams = new ArrayList<Object>();
        }
        if (queryParams == null) {
            queryParams = new HashMap<String, Object>();
        }
        int matchCount = PlatStringUtil.getMatchCount(sql, "\\#\\{(.*?)}");
        for (int i = 0; i < matchCount; i++) {
            sql = getConvertSql(sql, queryParams, conditionParams);
        }
        return sql;
    }

    /**
     * 获取磁盘上的SQL语句内容
     *
     * @param sqlPath
     * @param replaceParams
     * @return
     */
    public static String getDiskSqlContent(String sqlPath, Map<String, Object> replaceParams) {
        sqlPath = sqlPath.replace("..", "");
        String prePath = null;
        try {
            prePath = PlatAppUtil.getAppAbsolutePath();
        } catch (Exception e) {

        }
        if (StringUtils.isEmpty(prePath)) {
            prePath = PlatPropUtil.getPropertyValue("config.properties", "genCodeProject");
            prePath += "webapp/";
        }
        String filePath = prePath + "WEB-INF/sql/" + sqlPath + ".sql";
        String sqlContent = PlatFileUtil.readFileString(filePath, "UTF-8");
        if (StringUtils.isNotEmpty(sqlContent)) {
            if (replaceParams != null && replaceParams.size() > 0) {
                sqlContent = PlatStringUtil.getFreeMarkResult(sqlContent, replaceParams);
            }
            return sqlContent;
        } else {
            return null;
        }
    }

    /**
     * 获取系统中的缺省数据库类型
     *
     * @return
     */
    public static String getDefaultDbType() {
        String className = PlatPropUtil.getPropertyValue("config.properties", "jdbc.driverClassName");
        String defaultDbType = "";
        if ("com.mysql.jdbc.Driver".equals(className)) {
            defaultDbType = "MYSQL";
        } else if ("com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(className)) {
            defaultDbType = "SQLSERVER";
        } else if ("oracle.jdbc.driver.OracleDriver".equals(className)) {
            defaultDbType = "ORACLE";
        } else {
            defaultDbType = "UNKNOWDBTYPE";
        }
        return defaultDbType;
    }

}
