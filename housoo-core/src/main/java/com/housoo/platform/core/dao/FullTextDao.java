package com.housoo.platform.core.dao;

/**
 * 描述 全文检索业务相关dao
 *
 * @author housoo
 * @version 1.0
 * @created 2017-07-01 10:15:45
 */
public interface FullTextDao extends BaseDao {
    /**
     * 根据表名和记录ID获取ID
     *
     * @param busTableName
     * @param recordId
     * @return
     */
    public String getId(String busTableName, String recordId);
}
