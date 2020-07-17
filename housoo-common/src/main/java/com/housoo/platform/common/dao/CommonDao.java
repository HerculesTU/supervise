package com.housoo.platform.common.dao;

import com.housoo.platform.core.dao.BaseDao;

/**
 * 描述
 *
 * @author 高飞
 * @created 2017年2月1日 上午10:22:50
 */
public interface CommonDao extends BaseDao {
    /**
     * 获取记录的数量
     *
     * @param validTableName
     * @param validFieldName
     * @param validFieldValue
     * @return
     */
    public int getRecordCount(String validTableName,
                              String validFieldName, String validFieldValue, String RECORD_ID);
}
