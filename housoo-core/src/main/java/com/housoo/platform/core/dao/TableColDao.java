package com.housoo.platform.core.dao;

/**
 * 描述 表格列业务相关dao
 *
 * @author housoo
 * @version 1.0
 * @created 2017-03-30 15:16:45
 */
public interface TableColDao extends BaseDao {
    /**
     * 根据表单控件ID获取最大排序值
     *
     * @param formControlId
     * @return
     */
    public int getMaxSn(String formControlId);
}
