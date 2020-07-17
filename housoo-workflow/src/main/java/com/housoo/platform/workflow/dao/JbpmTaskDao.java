/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.dao;

import com.housoo.platform.core.dao.BaseDao;

import java.util.List;

/**
 * 描述 流程任务业务相关dao
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-16 16:36:01
 */
public interface JbpmTaskDao extends BaseDao {
    /**
     * 根据实例ID获取任务的数量
     *
     * @param exeId
     * @return
     */
    public int getCount(String exeId);

    /**
     * 获取主表记录ID
     *
     * @param exeId
     * @param nodeKey
     * @return
     */
    public String getTaskMainRecordId(String exeId, String nodeKey);

    /**
     * 获取相同节点并且正在运行的任务数量
     *
     * @param exeId
     * @param nodeKey
     * @param operingTaskId
     * @return
     */
    public int getSameNodeKeyRunningTaskCount(String exeId, String nodeKey, String operingTaskId);

    /**
     * 获取正在运行状态任务数量数据
     *
     * @param exeId
     * @param flowVersion
     * @param nodeKeys
     * @return
     */
    public int getRunningTaskCount(String exeId, List<String> nodeKeys);

    /**
     * 判断任务是否已经被办理过
     *
     * @param taskId
     * @return
     */
    public boolean isTaskHaveHandled(String taskId);

    /**
     * 获取撤回的消息
     *
     * @param preTaskId
     * @return
     */
    public String getRevokeMsg(String preTaskId);

    /**
     * 获取创建的最小时间
     *
     * @param exeId
     * @param nodeKeys
     * @return
     */
    public String getMinCreateTime(String exeId, String nodeKeys);

    /**
     * 判断任务是否存在
     *
     * @param exeId
     * @param nodeKey
     * @param taskStatus
     * @return
     */
    public boolean isExistsTask(String exeId, String nodeKey, int taskStatus);

    /**
     * 获取最后的任务ID
     *
     * @param exeId
     * @param nodeKey
     * @return
     */
    public String getLatestTaskId(String exeId, String nodeKey);

    /**
     * 获取最后一条任务的节点KEY
     *
     * @param exeId
     * @return
     */
    public String getLastTaskNodeKey(String exeId);

    /**
     * 获取最后的任务ID
     *
     * @param exeId
     * @param nodeKey
     * @param taskStatus
     * @return
     */
    public String getLatestTaskId(String exeId, String nodeKey, int taskStatus);
}
