package com.housoo.platform.core.dao;

/**
 * 描述 数据源信息业务相关dao
 *
 * @author housoo
 * @version 1.0
 * @created 2018-03-30 15:06:38
 */
public interface DbConnDao extends BaseDao {
    /**
     * 获取数据库类型
     *
     * @param dbConnCode
     * @return
     */
    public String getDbType(String dbConnCode);
}
