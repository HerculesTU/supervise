package com.housoo.platform.core.dao.impl;

import com.housoo.platform.core.dao.DbConnDao;
import org.springframework.stereotype.Repository;

/**
 * 描述数据源信息业务相关dao实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2018-03-30 15:06:38
 */
@Repository
public class DbConnDaoImpl extends BaseDaoImpl implements DbConnDao {

    /**
     * 获取数据库类型
     *
     * @param dbConnCode
     * @return
     */
    @Override
    public String getDbType(String dbConnCode) {
        StringBuffer sql = new StringBuffer("SELECT T.DBCONN_CLASS");
        sql.append(" FROM PLAT_APPMODEL_DBCONN T WHERE T.DBCONN_CODE=?");
        String driverName = this.getJdbcTemplate().queryForObject(sql.toString(),
                new Object[]{dbConnCode}, String.class);
        if ("com.mysql.jdbc.Driver".equals(driverName)) {
            return "MYSQL";
        } else if ("com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(driverName)) {
            return "SQLSERVER";
        } else {
            return "ORACLE";
        }
    }
}
