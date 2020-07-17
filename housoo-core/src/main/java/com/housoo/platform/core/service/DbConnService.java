package com.housoo.platform.core.service;

import java.util.Map;

/**
 * 描述 数据源信息业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2018-03-30 15:06:38
 */
public interface DbConnService extends BaseService {
    /**
     * 判断是否是有效的连接
     *
     * @param dbConn
     * @return
     */
    public boolean isValidDb(Map<String, Object> dbConn);

    /**
     * 获取数据库类型
     *
     * @param dbConnCode
     * @return
     */
    public String getDbType(String dbConnCode);

}
