/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.dao.impl;

import com.housoo.platform.core.dao.impl.BaseDaoImpl;
import com.housoo.platform.workflow.dao.AgentDao;
import org.springframework.stereotype.Repository;

/**
 * 描述工作委托业务相关dao实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-27 16:49:15
 */
@Repository
public class AgentDaoImpl extends BaseDaoImpl implements AgentDao {

    /**
     * 获取配置数量
     *
     * @param userId
     * @return
     */
    @Override
    public int getCount(String userId) {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
        sql.append("JBPM6_AGENT J WHERE J.AGENT_USERID=? ");
        int count = this.getIntBySql(sql.toString(), new Object[]{userId});
        return count;
    }
}
