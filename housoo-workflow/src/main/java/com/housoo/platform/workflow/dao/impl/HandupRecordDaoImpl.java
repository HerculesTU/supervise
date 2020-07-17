/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.dao.impl;

import com.housoo.platform.core.dao.impl.BaseDaoImpl;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.workflow.dao.HandupRecordDao;
import org.springframework.stereotype.Repository;

/**
 * 描述挂起任务业务相关dao实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-06-01 09:29:36
 */
@Repository
public class HandupRecordDaoImpl extends BaseDaoImpl implements HandupRecordDao {

    /**
     * 获取挂起的数量
     *
     * @param exeId
     * @param taskId
     * @return
     */
    @Override
    public int getCount(String exeId, String sysUserId, String nodeKey) {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
        sql.append("JBPM6_HANDUPRECORD H WHERE H.RECORD_EXEID=?");
        sql.append(" AND H.RECORD_CREATORID=? AND H.RECORD_NODEKEY=? ");
        int count = this.getIntBySql(sql.toString(), new Object[]{exeId, sysUserId, nodeKey});
        return count;
    }

    /**
     * 获取最小开始挂起日期
     *
     * @param exeId
     * @param limitType
     * @return
     */
    @Override
    public String getMinBeginDate(String exeId) {
        StringBuffer sql = new StringBuffer("SELECT MIN(T.RECORD_BEGINDATE) ");
        sql.append("FROM JBPM6_HANDUPRECORD T WHERE T.RECORD_EXEID=? ");
        String beginDate = this.getJdbcTemplate().queryForObject(sql.toString(),
                new Object[]{exeId}, String.class);
        return beginDate;
    }

    /**
     * 获取最小开始挂起日期
     *
     * @param exeId
     * @return
     */
    @Override
    public String getMinBeginDate(String exeId, String nodeKeys) {
        StringBuffer sql = new StringBuffer("SELECT MIN(T.RECORD_BEGINDATE) ");
        sql.append("FROM JBPM6_HANDUPRECORD T WHERE T.RECORD_EXEID=? ");
        sql.append(" AND T.RECORD_NODEKEY IN ").append(PlatStringUtil.getSqlInCondition(nodeKeys));
        String beginDate = this.getJdbcTemplate().queryForObject(sql.toString(),
                new Object[]{exeId}, String.class);
        return beginDate;
    }

    /**
     * 获取最大更新日期
     *
     * @param exeId
     * @return
     */
    @Override
    public String getMaxUpdateDate(String exeId) {
        StringBuffer sql = new StringBuffer("SELECT MAX(T.RECORD_LASTUPDATEDAYS) ");
        sql.append("FROM JBPM6_HANDUPRECORD T WHERE T.RECORD_EXEID=? ");
        String time = this.getJdbcTemplate().queryForObject(sql.toString(),
                new Object[]{exeId}, String.class);
        return time.substring(0, 10);
    }

    /**
     * 获取最大更新日期
     *
     * @param exeId
     * @return
     */
    @Override
    public String getMaxUpdateDate(String exeId, String nodeKeys) {
        StringBuffer sql = new StringBuffer("SELECT MAX(T.RECORD_LASTUPDATEDAYS) ");
        sql.append("FROM JBPM6_HANDUPRECORD T WHERE T.RECORD_EXEID=? ");
        sql.append(" AND T.RECORD_NODEKEY IN ").append(PlatStringUtil.getSqlInCondition(nodeKeys));
        String time = this.getJdbcTemplate().queryForObject(sql.toString(),
                new Object[]{exeId}, String.class);
        return time.substring(0, 10);
    }
}
