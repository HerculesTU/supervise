/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.dao;

import com.housoo.platform.core.dao.BaseDao;

import java.util.List;

/**
 * 描述 流程事件业务相关dao
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-08 09:43:22
 */
public interface EventDao extends BaseDao {
    /**
     * 获取配置的最大排序值
     *
     * @param EVENT_FLOWDEFID
     * @param EVENT_FLOWVERSION
     * @return
     */
    public int getMaxSn(String EVENT_FLOWDEFID, String EVENT_FLOWVERSION);

    /**
     * 获取节点所配置的接口数据JSON列表
     *
     * @param defId
     * @param defVersion
     * @param nodeKey
     * @return
     */
    public List<String> findEventInterJson(String defId, String defVersion, String nodeKey);
}
