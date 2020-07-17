/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.dao.impl;

import com.housoo.platform.core.dao.impl.BaseDaoImpl;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.workflow.dao.JbpmTaskDao;
import com.housoo.platform.workflow.service.JbpmTaskService;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述流程任务业务相关dao实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-16 16:36:01
 */
@Repository
public class JbpmTaskDaoImpl extends BaseDaoImpl implements JbpmTaskDao {

    /**
     * 根据实例ID获取任务的数量
     *
     * @param exeId
     * @return
     */
    @Override
    public int getCount(String exeId) {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
        sql.append("JBPM6_TASK J WHERE J.TASK_EXEID=? ");
        int count = this.getIntBySql(sql.toString(), new Object[]{exeId});
        return count;
    }

    /**
     * 获取主表记录ID
     *
     * @param exeId
     * @param nodeKey
     * @return
     */
    @Override
    public String getTaskMainRecordId(String exeId, String nodeKey) {
        StringBuffer sql = new StringBuffer("SELECT T.TASK_MAINRECORDID ");
        sql.append("FROM JBPM6_TASK T WHERE T.TASK_EXEID=? AND T.TASK_NODEKEY=?");
        sql.append(" ORDER BY T.TASK_CREATETIME DESC ");
        List<String> recordIds = this.getJdbcTemplate().queryForList(sql.toString()
                , new Object[]{exeId, nodeKey}, String.class);
        if (recordIds != null && recordIds.size() > 0) {
            return recordIds.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取相同节点并且正在运行的任务数量
     *
     * @param exeId
     * @param nodeKey
     * @param operingTaskId
     * @return
     */
    @Override
    public int getSameNodeKeyRunningTaskCount(String exeId, String nodeKey, String operingTaskId) {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) ");
        sql.append(" FROM JBPM6_TASK T WHERE T.TASK_EXEID=?");
        sql.append(" AND T.TASK_NODEKEY=? AND T.TASK_STATUS IN (-1,1,-2) ");
        sql.append("AND T.TASK_ID!=? ");
        int count = this.getIntBySql(sql.toString(), new Object[]{exeId, nodeKey, operingTaskId});
        return count;
    }

    /**
     * 获取正在运行状态任务数量数据
     *
     * @param exeId
     * @param nodeKeys
     * @return
     */
    @Override
    public int getRunningTaskCount(String exeId, List<String> nodeKeys) {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
        sql.append("JBPM6_TASK T WHERE T.TASK_EXEID=? AND T.TASK_STATUS ");
        sql.append(" IN (-1,1) AND T.TASK_NODEKEY IN ");
        sql.append(PlatStringUtil.getSqlInCondition(nodeKeys.toArray(new String[nodeKeys.size()])));
        int count = this.getIntBySql(sql.toString(), new Object[]{exeId});
        return count;
    }

    /**
     * 判断任务是否已经被办理过
     *
     * @param taskId
     * @return
     */
    @Override
    public boolean isTaskHaveHandled(String taskId) {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
        sql.append(" JBPM6_TASK T WHERE T.TASK_ID=? ");
        sql.append(" AND T.TASK_STATUS IN (2,3,4,5) ");
        int count = this.getIntBySql(sql.toString(), new Object[]{taskId});
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取撤回的消息
     *
     * @param preTaskId
     * @return
     */
    @Override
    public String getRevokeMsg(String preTaskId) {
        StringBuffer sql = new StringBuffer("SELECT J.TASK_STATUS FROM ");
        sql.append("JBPM6_TASK J WHERE J.TASK_FROMTASKID=? ");
        List<Integer> statusList = this.getJdbcTemplate().queryForList(sql.toString(),
                new Object[]{preTaskId}, Integer.class);
        if (statusList != null && statusList.size() > 0) {
            boolean allowRevoke = true;
            for (int status : statusList) {
                if (status == JbpmTaskService.TASKSTATUS_AUDITED) {
                    allowRevoke = false;
                }
            }
            if (allowRevoke) {
                return null;
            } else {
                return "下一环节已经被办理,无法进行撤回操作!";
            }
        } else {
            return "未找到下一环节办理任务信息,无法撤回!";
        }
    }

    /**
     * 获取创建的最小时间
     *
     * @param exeId
     * @param nodeKeys
     * @return
     */
    @Override
    public String getMinCreateTime(String exeId, String nodeKeys) {
        StringBuffer sql = new StringBuffer("SELECT min(T.TASK_CREATETIME)");
        sql.append(" FROM JBPM6_TASK T WHERE T.TASK_EXEID=? AND ");
        sql.append(" T.TASK_NODEKEY IN ");
        sql.append(PlatStringUtil.getSqlInCondition(nodeKeys));
        String minCreateTime = this.getJdbcTemplate().queryForObject(sql.toString(),
                new Object[]{exeId}, String.class);
        return minCreateTime;
    }

    /**
     * 判断任务是否存在
     *
     * @param exeId
     * @param nodeKey
     * @param taskStatus
     * @return
     */
    @Override
    public boolean isExistsTask(String exeId, String nodeKey, int taskStatus) {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
        sql.append(" JBPM6_TASK T WHERE T.TASK_EXEID=? ");
        sql.append(" AND T.TASK_STATUS =? AND T.TASK_NODEKEY=? ");
        int count = this.getIntBySql(sql.toString(), new Object[]{exeId, taskStatus, nodeKey});
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取最后的任务ID
     *
     * @param exeId
     * @param nodeKey
     * @return
     */
    @Override
    public String getLatestTaskId(String exeId, String nodeKey) {
        StringBuffer sql = new StringBuffer("SELECT T.TASK_ID");
        sql.append(" FROM JBPM6_TASK T WHERE T.TASK_EXEID=? ");
        sql.append(" AND T.TASK_NODEKEY=? ");
        sql.append(" ORDER BY T.TASK_ENDTIME DESC ");
        List<String> list = this.getJdbcTemplate().queryForList(sql.toString(),
                new Object[]{exeId, nodeKey}, String.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取最后的任务ID
     *
     * @param exeId
     * @param nodeKey
     * @param taskStatus
     * @return
     */
    @Override
    public String getLatestTaskId(String exeId, String nodeKey, int taskStatus) {
        StringBuffer sql = new StringBuffer("SELECT T.TASK_ID");
        sql.append(" FROM JBPM6_TASK T WHERE T.TASK_EXEID=? ");
        sql.append(" AND T.TASK_NODEKEY=? ");
        sql.append(" AND T.TASK_STATUS=? ORDER BY T.TASK_ENDTIME DESC ");
        List<String> list = this.getJdbcTemplate().queryForList(sql.toString(),
                new Object[]{exeId, nodeKey, taskStatus}, String.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取最后一条任务的节点KEY
     *
     * @param exeId
     * @return
     */
    @Override
    public String getLastTaskNodeKey(String exeId) {
        StringBuffer sql = new StringBuffer("SELECT T.TASK_NODEKEY FROM ");
        sql.append("JBPM6_TASK T WHERE T.TASK_EXEID=? ORDER BY ");
        sql.append("T.TASK_ENDTIME DESC");
        List<String> list = this.getJdbcTemplate().queryForList(sql.toString(),
                new String[]{exeId}, String.class);
        return list.get(0);
    }
}
