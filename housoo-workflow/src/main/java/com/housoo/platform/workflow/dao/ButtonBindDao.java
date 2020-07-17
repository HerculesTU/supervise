package com.housoo.platform.workflow.dao;

import com.housoo.platform.core.dao.BaseDao;

/**
 * 描述 按钮绑定业务相关dao
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-05-07 09:49:51
 */
public interface ButtonBindDao extends BaseDao {
    /**
     * 获取配置的最大排序值
     *
     * @param BTNBIND_FLOWDEFID
     * @param BTNBIND_FLOWVERSION
     * @return
     */
    public int getMaxSn(String BTNBIND_FLOWDEFID, String BTNBIND_FLOWVERSION);
}
