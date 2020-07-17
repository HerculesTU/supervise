/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service;

import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.core.service.BaseService;
import com.housoo.platform.workflow.model.FlowNextStep;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述 流程任务业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-16 16:36:01
 */
public interface JbpmTaskService extends BaseService {
    /**
     * 流程状态:挂起
     */
    public static final int TASKSTATUS_HANDLEUP = -1;
    /**
     * 流程状态:正在办理
     */
    public static final int TASKSTATUS_HANDLING = 1;
    /**
     * 流程状态:已办理
     */
    public static final int TASKSTATUS_AUDITED = 2;
    /**
     * 流程状态:退回
     */
    public static final int TASKSTATUS_BACK = 3;
    /**
     * 流程状态:转发
     */
    public static final int TASKSTATUS_FORWARD = 4;
    /**
     * 流程状态:结束流程
     */
    public static final int TASKSTATUS_OVER = 5;
    /**
     * 流程状态:审核不通过
     */
    public static final int TASKSTATUS_NOAGREE = 6;
    /**
     * 流程状态:等待
     */
    public static final int TASKSTATUS_WAIT = -2;
    /**
     * 流程状态:重启
     */
    public static final int TASKSTATUS_RESTART = 7;

    /**
     * 根据流程实例ID获取任务列表数据
     *
     * @param exeId
     * @return
     */
    public List<Map<String, Object>> findByExeId(String exeId);

    /**
     * 根据流程实例ID删除流程任务数据
     *
     * @param exeId
     */
    public void deleteByExeId(String exeId);

    /**
     * 保存或者更新当前操作流程任务数据
     *
     * @param flowVars
     * @param jbpmFlowInfo
     */
    public void saveOrUpdateCurrentTask(Map<String, Object> flowVars, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 分配流程任务
     *
     * @param flowVars
     * @param jbpmFlowInfo
     */
    public void assignerTask(Map<String, Object> flowVars, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 获取当前正在办理的流程任务列表数据
     *
     * @param exeId
     * @return
     */
    public List<Map<String, Object>> findRunningTaskList(String exeId);

    /**
     * 获取当前正在办理的流程任务信息
     *
     * @param exeId
     * @return
     */
    public Map<String, String> getRunningTaskInfo(String exeId);

    /**
     * 获取主表记录ID
     *
     * @param exeId
     * @param nodeKey
     * @return
     */
    public String getTaskMainRecordId(String exeId, String nodeKey);

    /**
     * 获取已办理的节点集合
     *
     * @param exeId
     * @return
     */
    public Set<String> getRunOverTaskNodeKeySet(String exeId);

    /**
     * 判断是否存在相同节点并且正在办理的任务数据
     *
     * @param exeId
     * @param nodeKey
     * @param operingTaskId
     * @return
     */
    public boolean isExistSameNodeKeyRunningTask(String exeId, String nodeKey, String operingTaskId);

    /**
     * 根据流程实例ID获取最后一条流程实例任务
     *
     * @param exeId
     * @return
     */
    public Map<String, Object> getLatestTaskInfo(String exeId);

    /**
     * 更新下一个串审任务状态为运行中
     *
     * @param exeId
     * @param nextOrderSn
     * @param orderParentId
     * @param nodeKey
     */
    public void activeNextTaskStatus(String exeId, int nextOrderSn, String orderParentId, String nodeKey);

    /**
     * 判断是否存在正在运行的并行节点数据
     *
     * @param exeId
     * @param defId
     * @param flowVersion
     * @param joinNodeKey
     * @return
     */
    public boolean existRunningParallelTask(String exeId, String defId, String flowVersion, String joinNodeKey);

    /**
     * 更新合并节点前的toKey
     *
     * @param exeId
     * @param defId
     * @param flowVersion
     * @param joinNodeKey
     * @param toKey
     */
    public void updatePreJoinNodeToKey(String exeId, String defId, String flowVersion,
                                       String joinNodeKey, String toKey);

    /**
     * 获取退回环节列表数据
     *
     * @param jbpmFlowInfo
     * @return
     */
    public List<FlowNextStep> findReturnStepList(JbpmFlowInfo jbpmFlowInfo);

    /**
     * 获取相同来源任务的任务列表
     *
     * @param fromTaskId
     * @return
     */
    public List<Map<String, Object>> findSameFromTaskId(String fromTaskId);

    /**
     * 获取最近一个同节点来源任务ID
     *
     * @param exeId
     * @param nodeKey
     * @return
     */
    public String getLatestFromTaskId(String exeId, String nodeKey);

    /**
     * 获取流程办理人相关信息MAP
     *
     * @param exeId
     * @param nodeKey
     * @param toKey
     * @return
     */
    public Map<String, String> getTaskHandlerInfo(String exeId, String nodeKey, String toKey);

    /**
     * 删除流程任务并且级联更新实例数据
     *
     * @param taskIds
     */
    public void deleteTaskCacadeExe(String[] taskIds);

    /**
     * 判断任务是否已经被办理过
     *
     * @param taskId
     * @return
     */
    public boolean isTaskHaveHandled(String taskId);

    /**
     * 转发流程任务数据
     *
     * @param taskId
     * @param checkUserIds
     */
    public void forwardTask(String taskId, String checkUserIds);

    /**
     * 获取正在办理的流程任务列表
     *
     * @param exeId
     * @return
     */
    public List<Map<String, Object>> findRuuningTask(String exeId);

    /**
     * 环节跳转
     *
     * @param EXECUTION_ID
     * @param jumpList
     */
    public void jumpTask(String EXECUTION_ID, List<Map> jumpList);

    /**
     * 获取最后一条已办理的任务数据
     *
     * @param executionId
     * @param userId
     * @return
     */
    public Map<String, Object> getLatestAuditedTask(String executionId, String userId);

    /**
     * 获取撤回的消息
     *
     * @param preTaskId
     * @return
     */
    public String getRevokeMsg(String preTaskId);

    /**
     * 撤回流程任务
     *
     * @param taskId
     * @param exeId
     */
    public void revokeJbpmTask(String taskId, String exeId);

    /**
     * 获取已经消耗的天数
     *
     * @param nodeKeys
     * @param exeid
     * @param dayType
     * @return
     */
    public int getSpendDayCount(String nodeKeys, String exeid, String dayType);

    /**
     * 更新剩余天数数据
     */
    public void updateLeftDays();

    /**
     * 获取可选退回的节点数据
     *
     * @param flowToken
     * @return
     */
    public List<Map<String, Object>> getSelectBackStep(String flowToken);

    /**
     * 获取最后被办理的流程任务数据
     *
     * @param exeId
     * @param nodeKey
     * @return
     */
    public Map<String, Object> getLatestHandleTask(String exeId, String nodeKey);

    /**
     * 获取原路径
     *
     * @param oldTranTaskId
     * @return
     */
    public List<FlowNextStep> getOldNextSteps(String oldTranTaskId);

    /**
     * 获取固定退回节点数据
     *
     * @param backAssignNode
     * @param jbpmFlowInfo
     * @return
     */
    public FlowNextStep getBackAssignNode(String backAssignNode, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 获取指定节点数据
     *
     * @param goAssignNodeKey
     * @param jbpmFlowInfo
     * @return
     */
    public FlowNextStep getAppointAssignNode(String goAssignNodeKey, JbpmFlowInfo jbpmFlowInfo);

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

    /**
     * 派发父亲流程的任务
     *
     * @param parentExeId
     * @param jbpmFlowInfo
     */
    public void assignParentTask(String parentExeId, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 获取已经超期的流程任务列表
     *
     * @return
     */
    public List<Map<String, Object>> findTimeOutList();

    /**
     * 描述 获取待办总数
     *
     * @return
     * @created 2017年8月17日 下午4:04:31
     */
    public int getTodoListNum();
}
