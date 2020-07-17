package com.housoo.platform.core.dao;

/**
 * 描述 QueryCondition业务相关dao
 *
 * @author housoo
 * @version 1.0
 * @created 2017-03-14 10:55:40
 */
public interface QueryConditionDao extends BaseDao {
    /**
     * 根据表单控件ID获取排序条件的最大值
     *
     * @param formControlId
     * @return
     */
    public int getMaxSn(String formControlId);
}
