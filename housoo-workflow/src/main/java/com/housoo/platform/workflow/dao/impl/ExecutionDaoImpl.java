/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.dao.impl;

import com.housoo.platform.core.dao.impl.BaseDaoImpl;
import com.housoo.platform.workflow.dao.ExecutionDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述流程实例业务相关dao实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-04 14:34:24
 */
@Repository
public class ExecutionDaoImpl extends BaseDaoImpl implements ExecutionDao {

    /**
     * 根据流程定义ID和流程版本号判断是否存在记录
     *
     * @param defId
     * @param flowVersion
     * @return
     */
    @Override
    public boolean isExistedRunning(String defId, int flowVersion) {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
        sql.append("JBPM6_EXECUTION E WHERE E.FLOWDEF_ID=? AND E.EXECUTION_VERSION=?");
        int count = this.getIntBySql(sql.toString(), new Object[]{defId, flowVersion});
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取子流程的数量值
     *
     * @param exeId
     * @return
     */
    @Override
    public int getSubProcessCount(String exeId) {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
        sql.append("JBPM6_EXECUTION E WHERE E.PARENT_EXECUTION_ID=? ");
        return this.getIntBySql(sql.toString(), new Object[]{exeId});
    }

    /**
     * 获取子流程实例ID数组
     *
     * @param parentId
     * @return
     */
    @Override
    public List<String> findSubExeIdArray(String parentId) {
        StringBuffer sql = new StringBuffer("SELECT T.EXECUTION_ID FROM ");
        sql.append("JBPM6_EXECUTION T WHERE T.PARENT_EXECUTION_ID=? ");
        List<String> exeIds = this.getJdbcTemplate().queryForList(sql.toString(),
                new Object[]{parentId}, String.class);
        return exeIds;
    }
}
