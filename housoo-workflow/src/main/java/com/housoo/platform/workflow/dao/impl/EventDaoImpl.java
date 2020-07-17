/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.dao.impl;

import com.housoo.platform.core.dao.impl.BaseDaoImpl;
import com.housoo.platform.workflow.dao.EventDao;
import com.housoo.platform.workflow.service.NodeBindService;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述流程事件业务相关dao实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-08 09:43:22
 */
@Repository
public class EventDaoImpl extends BaseDaoImpl implements EventDao {

    /**
     * 获取配置的最大排序值
     *
     * @param EVENT_FLOWDEFID
     * @param EVENT_FLOWVERSION
     * @return
     */
    @Override
    public int getMaxSn(String EVENT_FLOWDEFID, String EVENT_FLOWVERSION) {
        StringBuffer sql = new StringBuffer("SELECT MAX(T.EVENT_SN) ");
        sql.append("FROM JBPM6_EVENT T WHERE T.EVENT_FLOWDEFID=? ");
        sql.append(" AND T.EVENT_FLOWVERSION=? ");
        int maxSn = this.getIntBySql(sql.toString(), new Object[]{EVENT_FLOWDEFID,
                Integer.parseInt(EVENT_FLOWVERSION)});
        return maxSn;
    }

    /**
     * 获取节点所配置的接口数据JSON列表
     *
     * @param defId
     * @param defVersion
     * @param nodeKey
     * @return
     */
    @Override
    public List<String> findEventInterJson(String defId, String defVersion, String nodeKey) {
        StringBuffer sql = new StringBuffer("SELECT E.EVENT_INTERJSON FROM");
        sql.append(" JBPM6_EVENT E WHERE E.EVENT_ID IN (");
        sql.append("select T.NODEBIND_RECORDID from JBPM6_NODEBIND");
        sql.append(" t where t.nodebind_type=? and t.nodebind_flowdfeid=? ");
        sql.append(" and t.nodebind_flowversion=? AND T.NODEBIND_NODEKEY=? )");
        sql.append(" ORDER BY E.EVENT_SN ASC");
        List<String> list = this.getJdbcTemplate().queryForList(sql.toString(),
                new Object[]{NodeBindService.BINDTYPE_EVENT, defId, defVersion, nodeKey}, String.class);
        return list;
    }
}
