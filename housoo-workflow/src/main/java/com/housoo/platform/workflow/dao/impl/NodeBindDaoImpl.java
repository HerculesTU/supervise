/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.dao.impl;

import com.housoo.platform.core.dao.impl.BaseDaoImpl;
import com.housoo.platform.workflow.dao.NodeBindDao;
import com.housoo.platform.workflow.service.NodeBindService;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述流程绑定业务相关dao实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-06 13:24:42
 */
@Repository
public class NodeBindDaoImpl extends BaseDaoImpl implements NodeBindDao {

    /**
     * 获取绑定的节点信息KEY列表
     *
     * @param defId
     * @param flowVersion
     * @param busRecordId
     * @param bindType
     * @return
     */
    @Override
    public List<String> findBindNodeKeyList(String defId,
                                            int flowVersion, String busRecordId, int bindType) {
        StringBuffer sql = new StringBuffer("SELECT D.NODEBIND_NODEKEY ");
        sql.append(" FROM JBPM6_NODEBIND D WHERE D.NODEBIND_FLOWDFEID=? ");
        sql.append(" AND D.NODEBIND_FLOWVERSION=? ");
        sql.append("AND D.NODEBIND_RECORDID=? AND D.NODEBIND_TYPE=? ");
        return this.getJdbcTemplate().queryForList(sql.toString(),
                new Object[]{defId, flowVersion, busRecordId, bindType}, String.class);
    }

    /**
     * 获取环节任务性质
     *
     * @param defId
     * @param defVersion
     * @param nodeKey
     * @return
     */
    @Override
    public String getTaskNature(String defId, String defVersion, String nodeKey) {
        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append(" T.NODEBIND_TASKNATURE FROM JBPM6_NODEBIND T");
        sql.append(" WHERE T.NODEBIND_FLOWDFEID=? AND T.NODEBIND_FLOWVERSION=?");
        sql.append(" AND T.NODEBIND_NODEKEY=? AND T.NODEBIND_TYPE=? AND T.NODEBIND_TASKNATURE IS NOT NULL ");
        String taskNature = null;
        taskNature = this.getUniqueObj(sql.toString(), new Object[]{defId, defVersion, nodeKey,
                NodeBindService.BINDTYPE_NODECONIG}, String.class);
        return taskNature;
    }
}
