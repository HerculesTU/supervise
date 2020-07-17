/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.dao;

import com.housoo.platform.core.dao.BaseDao;

/**
 * 描述 工作委托业务相关dao
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-27 16:49:15
 */
public interface AgentDao extends BaseDao {
    /**
     * 获取配置数量
     *
     * @param userId
     * @return
     */
    public int getCount(String userId);
}
