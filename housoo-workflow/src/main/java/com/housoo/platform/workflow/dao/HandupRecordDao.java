/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.dao;

import com.housoo.platform.core.dao.BaseDao;

/**
 * 描述 挂起任务业务相关dao
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-06-01 09:29:36
 */
public interface HandupRecordDao extends BaseDao {
    /**
     * 获取挂起的数量
     *
     * @param exeId
     * @param sysUserId
     * @param nodeKey
     * @return
     */
    public int getCount(String exeId, String sysUserId, String nodeKey);

    /**
     * 获取最小开始挂起日期
     *
     * @param exeId
     * @return
     */
    public String getMinBeginDate(String exeId);

    /**
     * 获取最小开始挂起日期
     *
     * @param exeId
     * @param nodeKeys
     * @return
     */
    public String getMinBeginDate(String exeId, String nodeKeys);

    /**
     * 获取最大更新日期
     *
     * @param exeId
     * @return
     */
    public String getMaxUpdateDate(String exeId);

    /**
     * 获取最大更新日期
     *
     * @param exeId
     * @param nodeKeys
     * @return
     */
    public String getMaxUpdateDate(String exeId, String nodeKeys);
}
