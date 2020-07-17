/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.*;
import com.housoo.platform.core.service.SysUserService;
import com.housoo.platform.core.service.WorkdayService;
import com.housoo.platform.workflow.dao.JbpmTaskDao;
import com.housoo.platform.workflow.model.*;
import com.housoo.platform.workflow.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 描述 流程任务业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-16 16:36:01
 */
@Service("jbpmTaskService")
public class JbpmTaskServiceImpl extends BaseServiceImpl implements JbpmTaskService {

    /**
     * 所引入的dao
     */
    @Resource
    private JbpmTaskDao dao;
    /**
     *
     */
    @Resource
    private FlowNodeService flowNodeService;
    /**
     *
     */
    @Resource
    private NodeAssignerService nodeAssignerService;
    /**
     *
     */
    @Resource
    private SysUserService sysUserService;
    /**
     *
     */
    @Resource
    private NodeBindService nodeBindService;
    /**
     *
     */
    @Resource
    private FlowDefService flowDefService;
    /**
     *
     */
    @Resource
    private ExecutionService executionService;
    /**
     *
     */
    @Resource
    private AgentService agentService;
    /**
     *
     */
    @Resource
    private HandupRecordService handupRecordService;
    /**
     *
     */
    @Resource
    private TimeLimitService timeLimitService;
    /**
     *
     */
    @Resource
    private WorkdayService workdayService;
    /**
     *
     */
    @Resource
    private JbpmService jbpmService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据流程实例ID获取任务列表数据
     *
     * @param exeId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByExeId(String exeId) {
        StringBuffer sql = new StringBuffer("SELECT * FROM ");
        sql.append("JBPM6_TASK J WHERE J.TASK_EXEID=? ");
        sql.append(" ORDER BY J.TASK_CREATETIME ASC");
        return dao.findBySql(sql.toString(), new Object[]{exeId}, null);
    }

    /**
     * 根据流程实例ID删除流程任务数据
     *
     * @param exeId
     */
    @Override
    public void deleteByExeId(String exeId) {
        dao.deleteRecord("JBPM6_TASK", new String[]{"TASK_EXEID"}, new Object[]{exeId});
    }

    /**
     * 更新下一个串审任务状态为运行中
     *
     * @param exeId
     * @param nextOrderSn
     * @param orderParentId
     */
    @Override
    public void activeNextTaskStatus(String exeId, int nextOrderSn, String orderParentId, String nodeKey) {
        StringBuffer sql = new StringBuffer("UPDATE JBPM6_TASK ");
        sql.append(" SET TASK_STATUS=? WHERE TASK_EXEID=? ");
        sql.append(" AND TASK_NODEKEY=? AND TASK_ORDERSN=?");
        sql.append(" AND TASK_ORDERPARENTID=? ");
        dao.executeSql(sql.toString(), new Object[]{JbpmTaskService.TASKSTATUS_HANDLING,
                exeId, nodeKey, nextOrderSn, orderParentId});
    }

    /**
     * 判断是否存在正在运行的并行节点数据
     *
     * @param exeId
     * @param defId
     * @param flowVersion
     * @param joinNodeKey
     * @return
     */
    @Override
    public boolean existRunningParallelTask(String exeId, String defId, String flowVersion, String joinNodeKey) {
        List<String> parallelNodeKeys = flowNodeService.getParallelNodeKeySet(defId,
                Integer.parseInt(flowVersion), joinNodeKey);
        int count = dao.getRunningTaskCount(exeId, parallelNodeKeys);
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 更新合并节点前的toKey
     *
     * @param exeId
     * @param defId
     * @param flowVersion
     * @param joinNodeKey
     */
    @Override
    public void updatePreJoinNodeToKey(String exeId, String defId, String flowVersion,
                                       String joinNodeKey, String toKey) {
        List<String> preNodeKeyList = this.flowNodeService.getPreNodeKeyList(defId,
                Integer.parseInt(flowVersion), joinNodeKey);
        String[] nodeKeyArray = preNodeKeyList.toArray(new String[preNodeKeyList.size()]);
        StringBuffer sql = new StringBuffer("UPDATE JBPM6_TASK");
        sql.append(" T SET T.TASK_TONODEKEY=? WHERE T.TASK_EXEID=?");
        sql.append(" AND T.TASK_NODEKEY IN ").append(PlatStringUtil.getSqlInCondition(nodeKeyArray));
        dao.executeSql(sql.toString(), new Object[]{toKey, exeId});
    }

    /**
     * 保存或者更新当前操作流程任务数据
     *
     * @param flowVars
     * @param jbpmFlowInfo
     */
    @Override
    public void saveOrUpdateCurrentTask(Map<String, Object> flowVars, JbpmFlowInfo jbpmFlowInfo) {
        //获取开始节点KEY
        String startNodeKey = jbpmFlowInfo.getJbpmStartNodeKey();
        //获取当前操作环节KEY
        String currentOperingNodeKey = jbpmFlowInfo.getJbpmOperingNodeKey();
        //获取下一环节的连线名称
        String nextNodeLinkText = flowNodeService.getNextNodeLinkText(jbpmFlowInfo.getJbpmDefId(), Integer.parseInt(
                jbpmFlowInfo.getJbpmDefVersion()), jbpmFlowInfo.getJbpmOperingNodeKey());
        String toNodeKey = flowNodeService.getToNodeKey(jbpmFlowInfo);
        if (startNodeKey.equals(currentOperingNodeKey)) {
            saveStartTask(flowVars, jbpmFlowInfo, currentOperingNodeKey,
                    nextNodeLinkText, toNodeKey);
            jbpmFlowInfo.setJbpmAllowAssignTask("true");
        } else {
            updateCurrentTask(flowVars, jbpmFlowInfo, nextNodeLinkText,
                    toNodeKey);
            if (jbpmFlowInfo.getJbpmHandleTaskStatus().
                    equals(String.valueOf(JbpmTaskService.TASKSTATUS_NOAGREE))) {
                jbpmFlowInfo.setJbpmAllowAssignTask("false");
            } else if (jbpmFlowInfo.getJbpmHandleTaskStatus().
                    equals(String.valueOf(JbpmTaskService.TASKSTATUS_HANDLEUP))) {
                handupRecordService.saveHandUpRecord(jbpmFlowInfo);
                jbpmFlowInfo.setJbpmAllowAssignTask("false");
            } else if (jbpmFlowInfo.getJbpmHandleTaskStatus().
                    equals(String.valueOf(JbpmTaskService.TASKSTATUS_RESTART))) {
                //重启任务数据
                Map<String, Object> operingTask = dao.getRecord("JBPM6_TASK",
                        new String[]{"TASK_ID"}, new Object[]{jbpmFlowInfo.getJbpmOperingTaskId()});
                Map<String, Object> newTask = operingTask;
                newTask.remove("TASK_ID");
                newTask.remove("TASK_CREATETIME");
                newTask.remove("TASK_ENDTIME");
                newTask.remove("TASK_REALHANDLERID");
                newTask.remove("TASK_REALHANDLERNAME");
                newTask.remove("TASK_REALHANDLERCOMPANYID");
                newTask.remove("TASK_REALHANDLERCOMPANYPATH");
                newTask.remove("TASK_LASTUPDATELIMIT");
                newTask.put("TASK_STATUS", JbpmTaskService.TASKSTATUS_HANDLING);
                dao.saveOrUpdate("JBPM6_TASK", newTask, SysConstants.ID_GENERATOR_UUID, null);
                jbpmFlowInfo.setJbpmAllowAssignTask("false");
            } else {
                //获取当前节点的任务性质
                String taskNature = nodeBindService.getTaskNature(jbpmFlowInfo.getJbpmDefId(),
                        jbpmFlowInfo.getJbpmDefVersion(), jbpmFlowInfo.getJbpmOperingNodeKey());
                //判断下一环节是否是合并节点
                String joinNodeKey = flowNodeService.getJoinNodeKey(jbpmFlowInfo.getJbpmDefId(),
                        Integer.parseInt(jbpmFlowInfo.getJbpmDefVersion()), jbpmFlowInfo.getJbpmOperingNodeKey());
                if (StringUtils.isNotEmpty(joinNodeKey)) {
                    boolean existRunningTask = this.existRunningParallelTask(jbpmFlowInfo.getJbpmExeId(),
                            jbpmFlowInfo.getJbpmDefId(), jbpmFlowInfo.getJbpmDefVersion(), joinNodeKey);
                    Map<String, Object> currentTask = dao.getRecord("JBPM6_TASK",
                            new String[]{"TASK_ID"}, new Object[]{jbpmFlowInfo.getJbpmOperingTaskId()});
                    if (existRunningTask) {
                        currentTask.put("TASK_TONODEKEY", "");
                        dao.saveOrUpdate("JBPM6_TASK", currentTask, SysConstants.ID_GENERATOR_UUID, null);
                        jbpmFlowInfo.setJbpmAllowAssignTask("false");
                    } else {
                        String TASK_TONODEKEY = (String) currentTask.get("TASK_TONODEKEY");
                        this.updatePreJoinNodeToKey(jbpmFlowInfo.getJbpmExeId(),
                                jbpmFlowInfo.getJbpmDefId(), jbpmFlowInfo.getJbpmDefVersion(),
                                joinNodeKey, TASK_TONODEKEY);
                        jbpmFlowInfo.setJbpmAllowAssignTask("true");
                    }
                } else {
                    if (taskNature.equals(FlowNode.TASKNATURE_SINGLE) || taskNature.equals(FlowNode.TASKNATURE_FREE)) {
                        jbpmFlowInfo.setJbpmAllowAssignTask("true");
                    } else if (taskNature.equals(FlowNode.TASKNATURE_MULTI)) {
                        //判断下一步是否是转办操作
                        if (jbpmFlowInfo.getJbpmHandleTaskStatus().
                                equals(String.valueOf(JbpmTaskService.TASKSTATUS_FORWARD))) {
                            jbpmFlowInfo.setJbpmAllowAssignTask("true");
                        } else {
                            //获取当前操作人任务
                            Map<String, Object> operingTask = this.getRecord("JBPM6_TASK",
                                    new String[]{"TASK_ID"}, new Object[]{jbpmFlowInfo.getJbpmOperingTaskId()});
                            if (operingTask.get("TASK_ORDERSN") != null && operingTask.get("TASK_ORDERPARENTID") != null) {
                                int operingTaskSn = Integer.parseInt(operingTask.get("TASK_ORDERSN").toString());
                                int nextOrderSn = operingTaskSn + 1;
                                String orderParentId = (String) operingTask.get("TASK_ORDERPARENTID");
                                //激活下一个任务
                                this.activeNextTaskStatus(jbpmFlowInfo.getJbpmExeId(),
                                        nextOrderSn, orderParentId, jbpmFlowInfo.getJbpmOperingNodeKey());
                            }
                            boolean isSameRunningTask = this.isExistSameNodeKeyRunningTask(jbpmFlowInfo.getJbpmExeId(),
                                    jbpmFlowInfo.getJbpmOperingNodeKey(), jbpmFlowInfo.getJbpmOperingTaskId());
                            if (isSameRunningTask) {
                                jbpmFlowInfo.setJbpmAllowAssignTask("false");
                            } else {
                                jbpmFlowInfo.setJbpmAllowAssignTask("true");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 保存开始节点任务数据
     *
     * @param flowVars
     * @param jbpmFlowInfo
     * @param currentOperingNodeKey
     * @param nextNodeLinkText
     * @param toNodeKey
     */
    private void saveStartTask(Map<String, Object> flowVars,
                               JbpmFlowInfo jbpmFlowInfo, String currentOperingNodeKey,
                               String nextNodeLinkText, String toNodeKey) {
        int taskCount = dao.getCount(jbpmFlowInfo.getJbpmExeId());
        if (taskCount == 0) {
            //保存开始环节任务数据
            Map<String, Object> startTask = new HashMap<String, Object>();
            startTask.put("TASK_EXEID", jbpmFlowInfo.getJbpmExeId());
            startTask.put("TASK_NODEKEY", currentOperingNodeKey);
            startTask.put("TASK_NODENAME", jbpmFlowInfo.getJbpmOperingNodeName());
            startTask.put("TASK_HANDLERIDS", jbpmFlowInfo.getJbpmCreatorId());
            startTask.put("TASK_HANDLENAMES", jbpmFlowInfo.getJbpmCreatorName());
            //获取当前办理人类型
            String operingHandlerType = jbpmFlowInfo.getJbpmOperingHandlerType();
            if (operingHandlerType.equals(FlowAssignInfo.ASSIGNERNATURE_BACK)) {
                //获取当前登录用户
                Map<String, Object> backLoginUser = null;
                if (StringUtils.isNotEmpty(jbpmFlowInfo.getJbpmCreatorId())) {
                    backLoginUser = dao.getRecord("PLAT_SYSTEM_SYSUSER",
                            new String[]{"SYSUSER_ID"}, new Object[]{jbpmFlowInfo.getJbpmCreatorId()});
                    String SYSUSER_COMPANYID = (String) backLoginUser.get("SYSUSER_COMPANYID");
                    Map<String, Object> company = dao.getRecord("PLAT_SYSTEM_COMPANY",
                            new String[]{"COMPANY_ID"}, new Object[]{SYSUSER_COMPANYID});
                    backLoginUser.put("COMPANY_PATH", company.get("COMPANY_PATH"));
                    backLoginUser.put("COMPANY_NAME", company.get("COMPANY_NAME"));
                } else {
                    backLoginUser = PlatAppUtil.getBackPlatLoginUser();
                }
                String COMPANY_NAME = (String) backLoginUser.get("COMPANY_NAME");
                String COMPANY_ID = (String) backLoginUser.get("SYSUSER_COMPANYID");
                String COMPANY_PATH = (String) backLoginUser.get("COMPANY_PATH");
                startTask.put("TASK_HANDLECOMPANYIDS", COMPANY_ID);
                startTask.put("TASK_HANDLECOMPANYNAMES", COMPANY_NAME);
                startTask.put("TASK_REALHANDLERID", jbpmFlowInfo.getJbpmCreatorId());
                startTask.put("TASK_REALHANDLERNAME", jbpmFlowInfo.getJbpmCreatorName());
                startTask.put("TASK_REALHANDLERCOMPANYID", COMPANY_ID);
                startTask.put("TASK_REALHANDLERCOMPANYPATH", COMPANY_PATH);
            }
            startTask.put("TASK_STATUS", JbpmTaskService.TASKSTATUS_AUDITED);
            startTask.put("TASK_OPINION", jbpmFlowInfo.getJbpmHandleOpinion());
            startTask.put("TASK_MAINTABLENAME", jbpmFlowInfo.getJbpmMainTableName());
            startTask.put("TASK_MAINRECORDID", jbpmFlowInfo.getJbpmMainTableRecordId());
            startTask.put("TASK_HANDLEDESC", "已" + nextNodeLinkText);
            startTask.put("TASK_HANDLERTYPE", operingHandlerType);
            startTask.put("TASK_TONODEKEY", toNodeKey);
            startTask.put("TASK_ENDTIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            startTask.put("TASK_CREATETIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            startTask.put("TASK_LASTUPDATELIMIT", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            Map<String, Object> newStartTask = dao.saveOrUpdate("JBPM6_TASK", startTask, SysConstants.
                    ID_GENERATOR_UUID, null);
            jbpmFlowInfo.setJbpmOperingTaskId((String) newStartTask.get("TASK_ID"));
        } else {
            updateCurrentTask(flowVars, jbpmFlowInfo, nextNodeLinkText,
                    toNodeKey);
        }
    }

    /**
     * 更新当前流程任务信息
     *
     * @param flowVars
     * @param jbpmFlowInfo
     * @param nextNodeLinkText
     * @param toNodeKey
     */
    private void updateCurrentTask(Map<String, Object> flowVars,
                                   JbpmFlowInfo jbpmFlowInfo, String nextNodeLinkText, String toNodeKey) {
        List<Object> params = new ArrayList<Object>();
        //获取提交任务办理状态
        String jbpmHandleTaskStatus = jbpmFlowInfo.getJbpmHandleTaskStatus();
        if (StringUtils.isEmpty(jbpmHandleTaskStatus)) {
            jbpmHandleTaskStatus = String.valueOf(JbpmTaskService.TASKSTATUS_AUDITED);
            jbpmFlowInfo.setJbpmHandleTaskStatus(jbpmHandleTaskStatus);
        }
        if (jbpmHandleTaskStatus.equals(String.valueOf(JbpmTaskService.TASKSTATUS_RESTART))) {
            params.add(String.valueOf(JbpmTaskService.TASKSTATUS_HANDLEUP));
        } else {
            params.add(jbpmHandleTaskStatus);
        }

        String TASK_ENDTIME = PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        if (jbpmHandleTaskStatus.equals(String.valueOf(JbpmTaskService.TASKSTATUS_HANDLEUP))) {
            TASK_ENDTIME = "";
        }
        params.add(TASK_ENDTIME);
        params.add(jbpmFlowInfo.getJbpmHandleOpinion());
        if (jbpmHandleTaskStatus.equals(String.valueOf(JbpmTaskService.TASKSTATUS_AUDITED))) {
            params.add("已" + nextNodeLinkText);
        } else if (jbpmHandleTaskStatus.equals(String.valueOf(JbpmTaskService.TASKSTATUS_BACK))) {
            params.add("已退回");
        } else if (jbpmHandleTaskStatus.equals(String.valueOf(JbpmTaskService.TASKSTATUS_FORWARD))) {
            params.add("已转办");
        } else if (jbpmHandleTaskStatus.equals(String.valueOf(JbpmTaskService.TASKSTATUS_NOAGREE))) {
            params.add("审核不通过");
        } else if (jbpmHandleTaskStatus.equals(String.valueOf(JbpmTaskService.TASKSTATUS_HANDLEUP))) {
            params.add("已挂起");
        } else if (jbpmHandleTaskStatus.equals(String.valueOf(JbpmTaskService.TASKSTATUS_RESTART))) {
            params.add("已重启");
        }
        //获取当前办理人类型
        String operingHandlerType = jbpmFlowInfo.getJbpmOperingHandlerType();
        if (operingHandlerType.equals(FlowAssignInfo.ASSIGNERNATURE_BACK)) {
            //判断是有外部传入用户ID
            String jbpmHandlerId = jbpmFlowInfo.getJbpmHandlerId();
            //获取当前登录用户
            Map<String, Object> backLoginUser = null;
            if (StringUtils.isNotEmpty(jbpmHandlerId)) {
                backLoginUser = dao.getRecord("PLAT_SYSTEM_SYSUSER",
                        new String[]{"SYSUSER_ID"}, new Object[]{jbpmHandlerId});
                String SYSUSER_COMPANYID = (String) backLoginUser.get("SYSUSER_COMPANYID");
                Map<String, Object> companyInfo = dao.getRecord("PLAT_SYSTEM_COMPANY",
                        new String[]{"COMPANY_ID"}, new Object[]{SYSUSER_COMPANYID});
                String COMPANY_PATH = (String) companyInfo.get("COMPANY_PATH");
                backLoginUser.put("COMPANY_PATH", COMPANY_PATH);
            } else {
                backLoginUser = PlatAppUtil.getBackPlatLoginUser();
            }
            params.add((String) backLoginUser.get("SYSUSER_ID"));
            params.add((String) backLoginUser.get("SYSUSER_NAME"));
            params.add((String) backLoginUser.get("SYSUSER_COMPANYID"));
            params.add((String) backLoginUser.get("COMPANY_PATH"));
        }
        params.add(toNodeKey);
        params.add(jbpmFlowInfo.getJbpmOperingTaskId());
        StringBuffer sql = new StringBuffer("UPDATE JBPM6_TASK ");
        sql.append(" SET TASK_STATUS=?,TASK_ENDTIME=?,TASK_OPINION=?");
        sql.append(",TASK_HANDLEDESC=?,TASK_REALHANDLERID=?,TASK_REALHANDLERNAME=?");
        sql.append(",TASK_REALHANDLERCOMPANYID=?,TASK_REALHANDLERCOMPANYPATH=?");
        sql.append(",TASK_TONODEKEY=? WHERE TASK_ID=? ");
        dao.executeSql(sql.toString(), params.toArray());
        if (jbpmHandleTaskStatus.equals(String.valueOf(JbpmTaskService.TASKSTATUS_NOAGREE))) {
            sql = new StringBuffer("UPDATE JBPM6_TASK ");
            sql.append(" SET TASK_STATUS=?,TASK_ENDTIME=?");
            sql.append(" WHERE TASK_EXEID=? AND TASK_STATUS IN(-2,-1,1) ");
            sql.append(" AND TASK_ID!=? ");
            dao.executeSql(sql.toString(), new Object[]{JbpmTaskService.TASKSTATUS_OVER,
                    PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"),
                    jbpmFlowInfo.getJbpmExeId(), jbpmFlowInfo.getJbpmOperingTaskId()
            });
        }
    }

    /**
     * 获取最近一个同节点来源任务ID
     *
     * @param exeId
     * @param nodeKey
     * @return
     */
    @Override
    public String getLatestFromTaskId(String exeId, String nodeKey) {
        StringBuffer sql = new StringBuffer("SELECT J.TASK_FROMTASKID ");
        sql.append(" FROM JBPM6_TASK J WHERE J.TASK_EXEID=? AND J.TASK_NODEKEY=?");
        sql.append(" AND J.TASK_ENDTIME IS NOT NULL AND J.TASK_FROMTASKID IS NOT NULL");
        sql.append(" ORDER BY J.TASK_ENDTIME DESC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{exeId, nodeKey}, null);
        if (list != null && list.size() > 0) {
            return (String) list.get(0).get("TASK_FROMTASKID");
        } else {
            return null;
        }
    }

    /**
     * 派发任务
     *
     * @param nextNodeAssigner
     * @param jbpmFlowInfo
     */
    private void assignerTask(NodeAssigner nextNodeAssigner, JbpmFlowInfo jbpmFlowInfo, FlowAssignInfo assignInfo) {
        StringBuffer jbpmNewAssignTaskIds = new StringBuffer("");
        //获取任务性质
        String nextAuditType = null;
        //获取节点KEY
        String nextNodeKey = assignInfo.getNextNodeKey();
        if (nextNodeAssigner != null) {
            nextAuditType = nextNodeAssigner.getNextAuditType();
        } else {
            nextAuditType = nodeBindService.getTaskNature(jbpmFlowInfo.getJbpmDefId(),
                    jbpmFlowInfo.getJbpmDefVersion(), nextNodeKey);
        }
        //获取环节节点配置信息
        Map<String, Object> nextNodeConfig = this.nodeBindService.getNodeBind(jbpmFlowInfo.getJbpmDefId(),
                jbpmFlowInfo.getJbpmDefVersion(), nextNodeKey, NodeBindService.BINDTYPE_NODECONIG);
        //获取是否是串审
        String isTaskOrder = assignInfo.getNextIsOrderTask();
        String TASK_ORDERPARENTID = UUIDGenerator.getUUID();
        int sendMaxCount = 1;
        if (!nextAuditType.equals(FlowNextStep.AUDITTYPE_SINGLE)) {
            sendMaxCount = assignInfo.getNextAssignerIds().split(",").length;
        }
        for (int i = 0; i < sendMaxCount; i++) {
            //开始分发单人任务
            Map<String, Object> flowTask = new HashMap<String, Object>();
            flowTask.put("TASK_CREATETIME", PlatDateTimeUtil.
                    formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            flowTask.put("TASK_LASTUPDATELIMIT", PlatDateTimeUtil.
                    formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            flowTask.put("TASK_EXEID", jbpmFlowInfo.getJbpmExeId());
            flowTask.put("TASK_NODEKEY", nextNodeKey);
            flowTask.put("TASK_NODENAME", assignInfo.getNextNodeName());
            flowTask.put("TASK_FROMNODEKEY", jbpmFlowInfo.getJbpmOperingNodeKey());
            if (sendMaxCount != 1) {
                flowTask.put("TASK_HANDLERIDS", assignInfo.getNextAssignerIds().split(",")[i]);
            } else {
                flowTask.put("TASK_HANDLERIDS", assignInfo.getNextAssignerIds());
            }
            if (assignInfo.getNextAssignerNature().equals(FlowAssignInfo.ASSIGNERNATURE_BACK)) {
                Map<String, String> userInfo = null;
                if (sendMaxCount != 1) {
                    userInfo = sysUserService.getUserNamesCompanyInfo
                            (assignInfo.getNextAssignerIds().split(",")[i]);
                } else {
                    userInfo = sysUserService.getUserNamesCompanyInfo
                            (assignInfo.getNextAssignerIds());
                }
                flowTask.put("TASK_HANDLENAMES", userInfo.get("SYSUSER_NAMES"));
                flowTask.put("TASK_HANDLECOMPANYIDS", userInfo.get("COMPANY_IDS"));
                flowTask.put("TASK_HANDLECOMPANYNAMES", userInfo.get("COMPANY_NAMES"));
            }
            flowTask.put("TASK_MAINTABLENAME", jbpmFlowInfo.getJbpmMainTableName());
            flowTask.put("TASK_MAINRECORDID", jbpmFlowInfo.getJbpmMainTableRecordId());
            flowTask.put("TASK_HANDLERTYPE", assignInfo.getNextAssignerNature());
            if ("1".equals(isTaskOrder)) {
                flowTask.put("TASK_ORDERSN", i + 1);
                flowTask.put("TASK_ORDERPARENTID", TASK_ORDERPARENTID);
                if (i == 0) {
                    flowTask.put("TASK_STATUS", JbpmTaskService.TASKSTATUS_HANDLING);
                } else {
                    flowTask.put("TASK_STATUS", JbpmTaskService.TASKSTATUS_WAIT);
                }
            } else {
                flowTask.put("TASK_STATUS", JbpmTaskService.TASKSTATUS_HANDLING);
            }
            if (StringUtils.isEmpty(jbpmFlowInfo.getJbpmHandleTaskStatus()) ||
                    jbpmFlowInfo.getJbpmHandleTaskStatus().equals(String.
                            valueOf(JbpmTaskService.TASKSTATUS_AUDITED))) {
                flowTask.put("TASK_FROMTASKID", jbpmFlowInfo.getJbpmOperingTaskId());
            } else if (jbpmFlowInfo.getJbpmHandleTaskStatus().equals(String.
                    valueOf(JbpmTaskService.TASKSTATUS_BACK))) {
                //获取前一个环节的来源节点ID
                String fromTaskId = this.getLatestFromTaskId(jbpmFlowInfo.getJbpmExeId(), nextNodeKey);
                if (StringUtils.isNotEmpty(fromTaskId)) {
                    flowTask.put("TASK_FROMTASKID", fromTaskId);
                }
                //获取当前这个环节绑定的数据
                Map<String, Object> nodeBind = this.nodeBindService.
                        getNodeBind(jbpmFlowInfo.getJbpmDefId(), jbpmFlowInfo.getJbpmDefVersion(),
                                jbpmFlowInfo.getJbpmOperingNodeKey(), NodeBindService.BINDTYPE_NODECONIG);
                //获取退回性质
                String NODEBIND_BACKTYPE = (String) nodeBind.get("NODEBIND_BACKTYPE");
                if (StringUtils.isNotEmpty(NODEBIND_BACKTYPE) && "2".equals(NODEBIND_BACKTYPE)) {
                    //设置原路返回的ID
                    flowTask.put("TASK_BACKTRANID", jbpmFlowInfo.getJbpmOperingTaskId());
                }
            } else if (jbpmFlowInfo.getJbpmHandleTaskStatus().equals(String.
                    valueOf(JbpmTaskService.TASKSTATUS_FORWARD))) {
                Map<String, Object> currentTask = dao.getRecord("JBPM6_TASK",
                        new String[]{"TASK_ID"}, new Object[]{jbpmFlowInfo.getJbpmOperingTaskId()});
                flowTask.put("TASK_FROMTASKID", currentTask.get("TASK_FROMTASKID"));
            }
            //设置时间时限
            setTimeLimit(jbpmFlowInfo, nextNodeKey, flowTask);
            if (nextNodeConfig != null) {
                //获取绑定的子流程ID
                String NODEBIND_SUBDEFID = (String) nextNodeConfig.get("NODEBIND_SUBDEFID");
                if (StringUtils.isNotEmpty(NODEBIND_SUBDEFID)) {
                    flowTask.put("TASK_ISSUBPROCESS", "1");
                    jbpmService.startSubFlow(NODEBIND_SUBDEFID, jbpmFlowInfo, assignInfo, nextNodeAssigner);
                }
            }
            Map<String, Object> jbpmTask = dao.saveOrUpdate("JBPM6_TASK",
                    flowTask, SysConstants.ID_GENERATOR_UUID, null);
            jbpmNewAssignTaskIds.append(jbpmTask.get("TASK_ID")).append(",");
        }
        if (jbpmNewAssignTaskIds.length() > 2) {
            jbpmNewAssignTaskIds.deleteCharAt(jbpmNewAssignTaskIds.length() - 1);
        }
        jbpmFlowInfo.setJbpmNewAssignTaskIds(jbpmNewAssignTaskIds.toString());
    }

    /**
     * @param jbpmFlowInfo
     * @param nextNodeKey
     * @param flowTask
     */
    private void setTimeLimit(JbpmFlowInfo jbpmFlowInfo, String nextNodeKey,
                              Map<String, Object> flowTask) {
        Map<String, Object> timeLimit = timeLimitService.getTaskTimeLimit(jbpmFlowInfo, nextNodeKey);
        flowTask.put("TASK_LIMITTYPE", timeLimit.get("TASK_LIMITTYPE"));
        if (timeLimit.get("TASK_LEFTDAYS") != null) {
            flowTask.put("TASK_LEFTDAYS", timeLimit.get("TASK_LEFTDAYS"));
            int leftDays = Integer.parseInt(timeLimit.get("TASK_LEFTDAYS").toString());
            if (leftDays >= 0) {
                flowTask.put("TASK_TIMEOUT", "-1");
            } else {
                flowTask.put("TASK_TIMEOUT", "1");
            }
        }
    }

    /**
     * 分配流程任务
     *
     * @param flowVars
     * @param jbpmFlowInfo
     */
    @Override
    public void assignerTask(Map<String, Object> flowVars, JbpmFlowInfo jbpmFlowInfo) {
        //获取下一步审核JSON
        String jbpmAssignJson = jbpmFlowInfo.getJbpmAssignJson();
        String jbpmRestartParentExe = jbpmFlowInfo.getJbpmRestartParentExe();
        if (!(StringUtils.isNotEmpty(jbpmRestartParentExe) && "true".equals(jbpmRestartParentExe))) {
            List<FlowAssignInfo> assignList = JSON.parseArray(jbpmAssignJson, FlowAssignInfo.class);
            for (FlowAssignInfo assignInfo : assignList) {
                //获取节点KEY
                String nextNodeKey = assignInfo.getNextNodeKey();
                String nextAssignerIds = assignInfo.getNextAssignerIds();
                if (StringUtils.isEmpty(jbpmFlowInfo.getJbpmHandleTaskStatus()) ||
                        jbpmFlowInfo.getJbpmHandleTaskStatus().equals(String.
                                valueOf(JbpmTaskService.TASKSTATUS_AUDITED))) {
                    nextAssignerIds = agentService.getNextAssignerProxyerIds(
                            nextAssignerIds, jbpmFlowInfo.getJbpmDefId());
                    assignInfo.setNextAssignerIds(nextAssignerIds);
                }
                if (StringUtils.isNotEmpty(nextAssignerIds)) {
                    NodeAssigner nextNodeAssigner = null;
                    if (StringUtils.isEmpty(jbpmFlowInfo.getJbpmHandleTaskStatus()) ||
                            jbpmFlowInfo.getJbpmHandleTaskStatus().equals(String.
                                    valueOf(JbpmTaskService.TASKSTATUS_AUDITED))) {
                        nextNodeAssigner = nodeAssignerService.getNodeAssigner(nextNodeKey, flowVars, jbpmFlowInfo);
                    } else if (jbpmFlowInfo.getJbpmHandleTaskStatus().equals(String.
                            valueOf(JbpmTaskService.TASKSTATUS_BACK)) ||
                            jbpmFlowInfo.getJbpmHandleTaskStatus().equals(String.
                                    valueOf(JbpmTaskService.TASKSTATUS_FORWARD))) {
                        nextNodeAssigner = new NodeAssigner();
                        nextNodeAssigner.setAssignerIds(nextAssignerIds);
                        nextNodeAssigner.setHandlerNature(assignInfo.getNextAssignerNature());
                        if (nextNodeAssigner.getAssignerIds().split(",").length > 1) {
                            nextNodeAssigner.setNextAuditType(FlowNextStep.AUDITTYPE_MULTI);
                        } else {
                            nextNodeAssigner.setNextAuditType(FlowNextStep.AUDITTYPE_SINGLE);
                        }
                    }
                    this.assignerTask(nextNodeAssigner, jbpmFlowInfo, assignInfo);
                }
            }
        }
    }

    /**
     * 获取当前正在办理的流程任务列表数据
     *
     * @param exeId
     * @return
     */
    @Override
    public List<Map<String, Object>> findRunningTaskList(String exeId) {
        String[] runningStatus = new String[]{String.valueOf(JbpmTaskService.TASKSTATUS_HANDLEUP),
                String.valueOf(JbpmTaskService.TASKSTATUS_HANDLING)};
        StringBuffer sql = new StringBuffer("SELECT * FROM JBPM6_TASK ");
        sql.append("T WHERE T.TASK_EXEID=? AND T.TASK_ENDTIME IS NULL AND T.TASK_STATUS IN ");
        sql.append(PlatStringUtil.getSqlInCondition(runningStatus));
        sql.append(" ORDER BY T.TASK_CREATETIME ASC ");
        return dao.findBySql(sql.toString(), new Object[]{exeId}, null);
    }

    /**
     * 获取当前正在办理的流程任务信息
     *
     * @param exeId
     * @return
     */
    @Override
    public Map<String, String> getRunningTaskInfo(String exeId) {
        List<Map<String, Object>> list = this.findRunningTaskList(exeId);
        Map<String, String> taskInfo = new HashMap<String, String>();
        List<String> runningNodeKeys = new ArrayList<String>();
        List<String> runningNodeNames = new ArrayList<String>();
        List<String> runningHandlerIds = new ArrayList<String>();
        List<String> runningHandlerNames = new ArrayList<String>();
        for (Map<String, Object> task : list) {
            String TASK_NODEKEY = (String) task.get("TASK_NODEKEY");
            String TASK_NODENAME = (String) task.get("TASK_NODENAME");
            String[] TASK_HANDLERIDS = ((String) task.get("TASK_HANDLERIDS")).split(",");
            String[] TASK_HANDLENAMES = ((String) task.get("TASK_HANDLENAMES")).split(",");
            if (!runningNodeKeys.contains(TASK_NODEKEY)) {
                runningNodeKeys.add(TASK_NODEKEY);
                runningNodeNames.add(TASK_NODENAME);
            }
            for (int i = 0; i < TASK_HANDLERIDS.length; i++) {
                if (!runningHandlerIds.contains(TASK_HANDLERIDS[i])) {
                    runningHandlerIds.add(TASK_HANDLERIDS[i]);
                    runningHandlerNames.add(TASK_HANDLENAMES[i]);
                }
            }
        }
        if (runningNodeKeys.size() > 0) {
            taskInfo.put("CURRENT_NODEKEYS", PlatStringUtil.getListStringSplit(runningNodeKeys));
            taskInfo.put("CURRENT_NODENAMES", PlatStringUtil.getListStringSplit(runningNodeNames));
            taskInfo.put("CURRENT_HANDLERIDS", PlatStringUtil.getListStringSplit(runningHandlerIds));
            taskInfo.put("CURRENT_HANDLERNAMES", PlatStringUtil.getListStringSplit(runningHandlerNames));
        }
        return taskInfo;
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
        return dao.getTaskMainRecordId(exeId, nodeKey);
    }

    /**
     * 获取已办理的节点集合
     *
     * @param exeId
     * @return
     */
    @Override
    public Set<String> getRunOverTaskNodeKeySet(String exeId) {
        StringBuffer sql = new StringBuffer("SELECT T.TASK_NODEKEY,T.TASK_TONODEKEY,T.TASK_ID ");
        sql.append("FROM JBPM6_TASK T WHERE T.TASK_EXEID=? ");
        sql.append(" AND T.TASK_STATUS IN (2,4,5)");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{exeId}, null);
        Set<String> keySet = new HashSet<String>();
        for (Map<String, Object> map : list) {
            String nodeKey = (String) map.get("TASK_NODEKEY");
            String operingTaskId = (String) map.get("TASK_ID");
            boolean isExists = this.isExistSameNodeKeyRunningTask(exeId, nodeKey, operingTaskId);
            if (!isExists) {
                keySet.add(nodeKey);
                keySet.add((String) map.get("TASK_TONODEKEY"));
            }
        }
        if (keySet.size() > 0) {
            Map<String, Object> execution = dao.getRecord("JBPM6_EXECUTION",
                    new String[]{"EXECUTION_ID"}, new Object[]{exeId});
            String FLOWDEF_ID = (String) execution.get("FLOWDEF_ID");
            int defVersion = Integer.parseInt(execution.get("EXECUTION_VERSION").toString());
            Set<String> newKeySet = new HashSet<String>();
            for (String key : keySet) {
                List<FlowNode> nodeList = this.flowNodeService.getPreNodeList(FLOWDEF_ID, defVersion, key);
                if (nodeList != null && nodeList.size() > 0) {
                    for (FlowNode node : nodeList) {
                        if (node.getNodeType().equals(FlowNode.NODETYPE_JOIN) ||
                                node.getNodeType().equals(FlowNode.NODETYPE_DECISION)) {
                            String preNodeKey = node.getNodeKey();
                            //再获取上一个任务节点KEY
                            List<FlowNode> preNodeList = this.flowNodeService.getPreNodeList(FLOWDEF_ID,
                                    defVersion, preNodeKey);
                            //判断这个节点是有办结的任务数据
                            boolean isExistsTask = dao.isExistsTask(exeId, preNodeList.get(0).getNodeKey(),
                                    JbpmTaskService.TASKSTATUS_AUDITED);
                            if (isExistsTask) {
                                newKeySet.add(node.getNodeKey());
                            }
                        }
                    }
                }
            }
            keySet.addAll(newKeySet);
        }
        return keySet;
    }

    /**
     * 判断是否存在相同节点并且正在办理的任务数据
     *
     * @param exeId
     * @param nodeKey
     * @param operingTaskId
     * @return
     */
    @Override
    public boolean isExistSameNodeKeyRunningTask(String exeId, String nodeKey, String operingTaskId) {
        int count = dao.getSameNodeKeyRunningTaskCount(exeId, nodeKey, operingTaskId);
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据流程实例ID获取最后一条流程实例任务
     *
     * @param exeId
     * @return
     */
    @Override
    public Map<String, Object> getLatestTaskInfo(String exeId) {
        StringBuffer sql = new StringBuffer("SELECT * FROM JBPM6_TASK");
        sql.append(" T WHERE T.TASK_EXEID=? AND T.TASK_MAINRECORDID IS ");
        sql.append("NOT NULL ORDER BY T.TASK_ENDTIME DESC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                new Object[]{exeId}, null);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取相同来源任务的任务列表
     *
     * @param fromTaskId
     * @return
     */
    @Override
    public List<Map<String, Object>> findSameFromTaskId(String fromTaskId) {
        StringBuffer sql = new StringBuffer("SELECT T.TASK_HANDLERTYPE,");
        sql.append("T.TASK_REALHANDLERID,T.TASK_REALHANDLERNAME ");
        sql.append(" FROM JBPM6_TASK T WHERE T.TASK_FROMTASKID=?");
        sql.append(" AND T.TASK_STATUS =? ");
        sql.append(" ORDER BY T.TASK_ENDTIME ASC");
        return dao.findBySql(sql.toString(), new Object[]{fromTaskId, JbpmTaskService.TASKSTATUS_AUDITED}, null);
    }

    /**
     * 获取退回环节列表数据
     *
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public List<FlowNextStep> findReturnStepList(JbpmFlowInfo jbpmFlowInfo) {
        List<FlowNextStep> stepList = new ArrayList<FlowNextStep>();
        Map<String, Object> currentTask = dao.getRecord("JBPM6_TASK",
                new String[]{"TASK_ID"}, new Object[]{jbpmFlowInfo.getJbpmOperingTaskId()});
        List<FlowNode> preNodeList = flowNodeService.getPreNodeList(jbpmFlowInfo.
                        getJbpmDefId(), Integer.parseInt(jbpmFlowInfo.getJbpmDefVersion()),
                jbpmFlowInfo.getJbpmOperingNodeKey());
        FlowNode preNode = preNodeList.get(0);
        if (preNode.getNodeType().equals(FlowNode.NODETYPE_JOIN)) {
            List<FlowNode> intoJoinNodeList = flowNodeService.getPreNodeList(jbpmFlowInfo.
                    getJbpmDefId(), Integer.parseInt(jbpmFlowInfo.getJbpmDefVersion()), preNode.getNodeKey());
            for (FlowNode node : intoJoinNodeList) {
                FlowNextStep nextStep = new FlowNextStep();
                nextStep.setNodeKeys(node.getNodeKey());
                nextStep.setNodeNames(node.getNodeName());
                Map<String, String> taskHandlerInfo = this.getTaskHandlerInfo(jbpmFlowInfo.getJbpmExeId()
                        , node.getNodeKey(), jbpmFlowInfo.getJbpmOperingNodeKey());
                List<NodeAssigner> nodeAssignerList = new ArrayList<NodeAssigner>();
                NodeAssigner nodeAssigner = new NodeAssigner();
                nodeAssigner.setIsOrder("-1");
                nodeAssigner.setHandlerNature(taskHandlerInfo.get("TASK_HANDLERTYPE").toString());
                nodeAssigner.setAssignerIds(taskHandlerInfo.get("TASK_REALHANDLERIDS").toString());
                nodeAssigner.setAssignerNames(taskHandlerInfo.get("TASK_REALHANDLERNAMES").toString());
                nodeAssignerList.add(nodeAssigner);
                nextStep.setNodeAssignerList(nodeAssignerList);
                stepList.add(nextStep);
            }
        } else {
            Map<String, Object> preTask = dao.getRecord("JBPM6_TASK",
                    new String[]{"TASK_ID"}, new Object[]{currentTask.get("TASK_FROMTASKID")});
            String fromNodeKey = (String) preTask.get("TASK_NODEKEY");
            String fromNodeName = (String) preTask.get("TASK_NODENAME");
            String preFromTaskId = (String) preTask.get("TASK_FROMTASKID");
            FlowNextStep nextStep = new FlowNextStep();
            nextStep.setNodeKeys(fromNodeKey);
            nextStep.setNodeNames(fromNodeName);
            List<NodeAssigner> nodeAssignerList = new ArrayList<NodeAssigner>();
            List<Map<String, Object>> fromTaskList = null;
            if (StringUtils.isNotEmpty(preFromTaskId)) {
                fromTaskList = this.findSameFromTaskId(preFromTaskId);
            } else {
                fromTaskList = new ArrayList<Map<String, Object>>();
                fromTaskList.add(preTask);
            }
            NodeAssigner nodeAssigner = new NodeAssigner();
            StringBuffer assignerIds = new StringBuffer("");
            StringBuffer assignerNames = new StringBuffer("");
            for (int i = 0; i < fromTaskList.size(); i++) {
                String realHandleId = (String) fromTaskList.get(i).get("TASK_REALHANDLERID");
                String realHandleName = (String) fromTaskList.get(i).get("TASK_REALHANDLERNAME");
                if (!assignerIds.toString().contains(realHandleId)) {
                    assignerIds.append(realHandleId).append(",");
                    assignerNames.append(realHandleName).append(",");
                    nodeAssigner.setHandlerNature(fromTaskList.get(i).get("TASK_HANDLERTYPE").toString());
                }
            }
            if (assignerIds.length() > 0) {
                assignerIds.deleteCharAt(assignerIds.length() - 1);
                assignerNames.deleteCharAt(assignerNames.length() - 1);
            }
            nodeAssigner.setAssignerIds(assignerIds.toString());
            nodeAssigner.setAssignerNames(assignerNames.toString());
            nodeAssigner.setIsOrder("-1");
            nodeAssignerList.add(nodeAssigner);
            nextStep.setNodeAssignerList(nodeAssignerList);
            stepList.add(nextStep);
        }
        return stepList;
    }

    /**
     * 获取流程办理人相关信息MAP
     *
     * @param exeId
     * @param nodeKey
     * @param toKey
     * @return
     */
    @Override
    public Map<String, String> getTaskHandlerInfo(String exeId, String nodeKey, String toKey) {
        StringBuffer sql = new StringBuffer("SELECT J.TASK_REALHANDLERID,");
        sql.append("J.TASK_REALHANDLERNAME,J.TASK_HANDLERTYPE FROM ");
        sql.append("JBPM6_TASK J WHERE J.TASK_EXEID=? AND J.TASK_NODEKEY=? ");
        sql.append(" AND J.TASK_TONODEKEY=?");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{exeId, nodeKey, toKey}, null);
        Map<String, String> taskHandlerInfo = new HashMap<String, String>();
        List<String> handlerIds = new ArrayList<String>();
        List<String> handlerNames = new ArrayList<String>();
        for (Map<String, Object> map : list) {
            String TASK_REALHANDLERID = (String) map.get("TASK_REALHANDLERID");
            String TASK_REALHANDLERNAME = (String) map.get("TASK_REALHANDLERNAME");
            if (!handlerIds.contains(TASK_REALHANDLERID)) {
                handlerIds.add(TASK_REALHANDLERID);
                handlerNames.add(TASK_REALHANDLERNAME);
            }
        }
        StringBuffer TASK_REALHANDLERIDS = new StringBuffer("");
        StringBuffer TASK_REALHANDLERNAMES = new StringBuffer("");
        for (int i = 0; i < handlerIds.size(); i++) {
            if (i > 0) {
                TASK_REALHANDLERIDS.append(",");
                TASK_REALHANDLERNAMES.append(",");
            }
            TASK_REALHANDLERIDS.append(handlerIds.get(i));
            TASK_REALHANDLERNAMES.append(handlerNames.get(i));
        }
        taskHandlerInfo.put("TASK_REALHANDLERIDS", TASK_REALHANDLERIDS.toString());
        taskHandlerInfo.put("TASK_REALHANDLERNAMES", TASK_REALHANDLERNAMES.toString());
        taskHandlerInfo.put("TASK_HANDLERTYPE", "1");
        return taskHandlerInfo;
    }

    /**
     * 删除流程任务并且级联更新实例数据
     *
     * @param taskIds
     */
    @Override
    public void deleteTaskCacadeExe(String[] taskIds) {
        String exeId = null;
        for (String taskId : taskIds) {
            Map<String, Object> jbpmTask = dao.getRecord("JBPM6_TASK",
                    new String[]{"TASK_ID"}, new Object[]{taskId});
            exeId = (String) jbpmTask.get("TASK_EXEID");
        }
        StringBuffer sql = new StringBuffer("DELETE FROM JBPM6_TASK ");
        sql.append(" WHERE TASK_ID IN").append(PlatStringUtil.getSqlInCondition(taskIds));
        sql.append(" AND TASK_EXEID =? ");
        dao.executeSql(sql.toString(), new Object[]{exeId});
        JbpmFlowInfo jbpmFlowInfo = new JbpmFlowInfo();
        jbpmFlowInfo.setJbpmExeId(exeId);
        executionService.updateFlowExe(new HashMap<String, Object>(), jbpmFlowInfo);

    }

    /**
     * 判断任务是否已经被办理过
     *
     * @param taskId
     * @return
     */
    @Override
    public boolean isTaskHaveHandled(String taskId) {
        return dao.isTaskHaveHandled(taskId);
    }

    public Map<String, String> getHandlerInfo(String checkUserIds) {
        Map<String, String> handlerInfo = new HashMap<String, String>();
        StringBuffer TASK_HANDLERIDS = new StringBuffer(checkUserIds);
        StringBuffer TASK_HANDLENAMES = new StringBuffer("");
        List<String> companyIdArray = new ArrayList<String>();
        List<String> companyNameArray = new ArrayList<String>();
        String[] userIdArray = checkUserIds.split(",");
        for (int i = 0; i < userIdArray.length; i++) {
            Map<String, Object> sysUser = dao.getRecord("PLAT_SYSTEM_SYSUSER", new String[]{"SYSUSER_ID"},
                    new Object[]{userIdArray[i]});
            String userName = (String) sysUser.get("SYSUSER_NAME");
            String SYSUSER_COMPANYID = (String) sysUser.get("SYSUSER_COMPANYID");
            Map<String, Object> company = dao.getRecord("PLAT_SYSTEM_COMPANY",
                    new String[]{"COMPANY_ID"}, new Object[]{SYSUSER_COMPANYID});
            String COMPANY_NAME = (String) company.get("COMPANY_NAME");
            if (i > 0) {
                TASK_HANDLENAMES.append(",");
            }
            TASK_HANDLENAMES.append(userName);
            if (!companyIdArray.contains(SYSUSER_COMPANYID)) {
                companyIdArray.add(SYSUSER_COMPANYID);
                companyNameArray.add(COMPANY_NAME);
            }
        }
        handlerInfo.put("TASK_HANDLERIDS", TASK_HANDLERIDS.toString());
        handlerInfo.put("TASK_HANDLENAMES", TASK_HANDLENAMES.toString());
        handlerInfo.put("TASK_HANDLECOMPANYIDS", PlatStringUtil.getListStringSplit(companyIdArray));
        handlerInfo.put("TASK_HANDLECOMPANYNAMES", PlatStringUtil.getListStringSplit(companyNameArray));
        return handlerInfo;
    }

    /**
     * 转发流程任务数据
     *
     * @param taskId
     * @param checkUserIds
     */
    @Override
    public void forwardTask(String taskId, String checkUserIds) {
        Map<String, Object> jbpmTask = dao.getRecord("JBPM6_TASK",
                new String[]{"TASK_ID"}, new Object[]{taskId});
        String exeId = (String) jbpmTask.get("TASK_EXEID");
        Map<String, String> handlerInfo = this.getHandlerInfo(checkUserIds);
        jbpmTask.put("TASK_HANDLERIDS", handlerInfo.get("TASK_HANDLERIDS"));
        jbpmTask.put("TASK_HANDLENAMES", handlerInfo.get("TASK_HANDLENAMES"));
        jbpmTask.put("TASK_HANDLECOMPANYIDS", handlerInfo.get("TASK_HANDLECOMPANYIDS"));
        jbpmTask.put("TASK_HANDLECOMPANYNAMES", handlerInfo.get("TASK_HANDLECOMPANYNAMES"));
        dao.saveOrUpdate("JBPM6_TASK", jbpmTask, SysConstants.ID_GENERATOR_UUID, null);
        JbpmFlowInfo jbpmFlowInfo = new JbpmFlowInfo();
        jbpmFlowInfo.setJbpmExeId(exeId);
        executionService.updateFlowExe(new HashMap<String, Object>(), jbpmFlowInfo);
    }

    /**
     * 获取正在办理的流程任务列表
     *
     * @param exeId
     * @return
     */
    @Override
    public List<Map<String, Object>> findRuuningTask(String exeId) {
        StringBuffer sql = new StringBuffer("SELECT * FROM ");
        sql.append("JBPM6_TASK J WHERE J.TASK_STATUS=? ");
        sql.append(" ORDER BY J.TASK_CREATETIME ASC ");
        return dao.findBySql(sql.toString(), new Object[]{JbpmTaskService.
                TASKSTATUS_HANDLING}, null);
    }

    /**
     * 环节跳转
     *
     * @param EXECUTION_ID
     * @param jumpList
     */
    @Override
    public void jumpTask(String EXECUTION_ID, List<Map> jumpList) {
        for (Map<String, Object> jump : jumpList) {
            String TASK_ID = (String) jump.get("TASK_ID");
            String JUMP_NODEKEY = (String) jump.get("JUMP_NODEKEY");
            String JUMP_NODENAME = (String) jump.get("JUMP_NODENAME");
            String JUMP_RECEIVERIDS = (String) jump.get("JUMP_RECEIVERIDS");
            Map<String, Object> jbpmTask = dao.getRecord("JBPM6_TASK",
                    new String[]{"TASK_ID"}, new Object[]{TASK_ID});
            Map<String, String> handlerInfo = this.getHandlerInfo(JUMP_RECEIVERIDS);
            jbpmTask.put("TASK_NODEKEY", JUMP_NODEKEY);
            jbpmTask.put("TASK_NODENAME", JUMP_NODENAME);
            jbpmTask.put("TASK_HANDLERIDS", handlerInfo.get("TASK_HANDLERIDS"));
            jbpmTask.put("TASK_HANDLENAMES", handlerInfo.get("TASK_HANDLENAMES"));
            jbpmTask.put("TASK_HANDLECOMPANYIDS", handlerInfo.get("TASK_HANDLECOMPANYIDS"));
            jbpmTask.put("TASK_HANDLECOMPANYNAMES", handlerInfo.get("TASK_HANDLECOMPANYNAMES"));
            dao.saveOrUpdate("JBPM6_TASK", jbpmTask, SysConstants.ID_GENERATOR_UUID, null);
        }
        //获取实例信息
        Map<String, Object> execution = this.executionService.getRecord("JBPM6_EXECUTION",
                new String[]{"EXECUTION_ID"}, new Object[]{EXECUTION_ID});
        JbpmFlowInfo jbpmFlowInfo = new JbpmFlowInfo();
        jbpmFlowInfo.setJbpmDefId(execution.get("FLOWDEF_ID").toString());
        jbpmFlowInfo.setJbpmExeId(EXECUTION_ID);
        executionService.updateFlowExe(new HashMap<String, Object>(), jbpmFlowInfo);
    }

    /**
     * 获取最后一条已办理的任务数据
     *
     * @param executionId
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> getLatestAuditedTask(String executionId, String userId) {
        StringBuffer sql = new StringBuffer("SELECT * FROM JBPM6_TASK T ");
        sql.append("WHERE T.TASK_EXEID=? AND T.TASK_STATUS=? ");
        sql.append(" AND T.TASK_REALHANDLERID=? AND T.TASK_FROMTASKID IS NOT NULL ");
        sql.append("ORDER BY T.TASK_ENDTIME DESC");
        List<Map<String, Object>> getLatest = dao.findBySql(sql.toString(),
                new Object[]{executionId, JbpmTaskService.TASKSTATUS_AUDITED, userId}, null);
        if (getLatest != null && getLatest.size() > 0) {
            return getLatest.get(0);
        } else {
            return null;
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
        return dao.getRevokeMsg(preTaskId);
    }

    /**
     * 撤回流程任务
     *
     * @param taskId
     * @param exeId
     */
    @Override
    public void revokeJbpmTask(String taskId, String exeId) {
        Map<String, Object> task = this.getRecord("JBPM6_TASK",
                new String[]{"TASK_ID"}, new Object[]{taskId});
        StringBuffer sql = new StringBuffer("DELETE FROM JBPM6_TASK");
        sql.append(" WHERE TASK_FROMTASKID=? ");
        dao.executeSql(sql.toString(), new Object[]{taskId});
        task.put("TASK_STATUS", JbpmTaskService.TASKSTATUS_HANDLING);
        task.put("TASK_ENDTIME", null);
        task.put("TASK_OPINION", "");
        task.put("TASK_REALHANDLERID", "");
        task.put("TASK_REALHANDLERNAME", "");
        task.put("TASK_REALHANDLERCOMPANYID", "");
        task.put("TASK_REALHANDLERCOMPANYPATH", "");
        dao.saveOrUpdate("JBPM6_TASK", task, SysConstants.ID_GENERATOR_UUID, null);
        JbpmFlowInfo jbpmFlowInfo = new JbpmFlowInfo();
        jbpmFlowInfo.setJbpmExeId(exeId);
        executionService.updateFlowExe(new HashMap<String, Object>(), jbpmFlowInfo);
    }

    /**
     * 获取已经消耗的天数
     *
     * @param nodeKeys
     * @param exeId
     * @param dayType
     * @return
     */
    @Override
    public int getSpendDayCount(String nodeKeys, String exeId, String dayType) {
        String minCreateTime = dao.getMinCreateTime(exeId, nodeKeys);
        if (StringUtils.isNotEmpty(minCreateTime)) {
            String endDate = PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd");
            String beginDate = minCreateTime.substring(0, 10);
            if (dayType.equals(TimeLimitService.LIMITTYPE_NATURAL)) {
                return PlatDateTimeUtil.getDaysBetween(beginDate, endDate);
            } else {
                return workdayService.getWorkDayCount(beginDate, endDate);
            }
        } else {
            return 0;
        }
    }

    /**
     * 更新剩余天数数据
     */
    @Override
    public void updateLeftDays() {
        //获取所有需要更新的流程任务
        StringBuffer sql = new StringBuffer("SELECT * FROM ");
        sql.append(" JBPM6_TASK J WHERE J.TASK_STATUS=? AND J.TASK_ENDTIME IS NULL");
        sql.append(" AND J.TASK_LEFTDAYS IS NOT NULL ORDER BY J.TASK_CREATETIME ASC");
        List<Map<String, Object>> taskList = dao.findBySql(sql.toString(),
                new Object[]{JbpmTaskService.TASKSTATUS_HANDLING}, null);
        for (Map<String, Object> task : taskList) {
            int TASK_LEFTDAYS = Integer.parseInt(task.get("TASK_LEFTDAYS").toString());
            String TASK_LIMITTYPE = (String) task.get("TASK_LIMITTYPE");
            String TASK_LASTUPDATELIMIT = (String) task.get("TASK_LASTUPDATELIMIT");
            //获取开始日期
            String startDate = TASK_LASTUPDATELIMIT.substring(0, 10);
            //获取当前日期
            String endDate = PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd");
            int spendDays = 0;
            if (TASK_LIMITTYPE.equals(TimeLimitService.LIMITTYPE_NATURAL)) {
                spendDays = PlatDateTimeUtil.getDaysBetween(startDate, endDate);
            } else {
                spendDays = workdayService.getWorkDayCount(startDate, endDate);
            }
            TASK_LEFTDAYS -= spendDays;
            if (TASK_LEFTDAYS < 0) {
                task.put("TASK_TIMEOUT", "1");
            } else {
                task.put("TASK_TIMEOUT", "-1");
            }
            task.put("TASK_LASTUPDATELIMIT", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            task.put("TASK_LEFTDAYS", TASK_LEFTDAYS);
            dao.saveOrUpdate("JBPM6_TASK", task, SysConstants.ID_GENERATOR_UUID, null);
        }
    }

    /**
     * 获取最后被办理的流程任务数据
     *
     * @param exeId
     * @param nodeKey
     * @return
     */
    @Override
    public Map<String, Object> getLatestHandleTask(String exeId, String nodeKey) {
        StringBuffer sql = new StringBuffer("SELECT * FROM JBPM6_TASK J");
        sql.append(" WHERE J.TASK_EXEID=? AND J.TASK_NODEKEY=? AND J.TASK_REALHANDLERID IS NOT NULL");
        sql.append(" ORDER BY J.TASK_ENDTIME DESC ");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{exeId, nodeKey}, null);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取可选退回的节点数据
     *
     * @param flowToken
     * @return
     */
    @Override
    public List<Map<String, Object>> getSelectBackStep(String flowToken) {
        Map<String, Object> flowVars = (Map<String, Object>) PlatAppUtil.getSessionCache(flowToken);
        JbpmFlowInfo jbpmFlowInfo = (JbpmFlowInfo) PlatBeanUtil.mapToBean(new JbpmFlowInfo(), flowVars);
        //获取当前办理的环节KEY
        String operingNodeKey = jbpmFlowInfo.getJbpmOperingNodeKey();
        //获取实例ID
        String exeId = jbpmFlowInfo.getJbpmExeId();
        String defId = jbpmFlowInfo.getJbpmDefId();
        int flowVersion = Integer.parseInt(jbpmFlowInfo.getJbpmDefVersion());
        List<FlowNode> allPreNodeList = this.flowNodeService.getAllPreNodeList(defId,
                flowVersion, operingNodeKey);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (FlowNode flowNode : allPreNodeList) {
            Map<String, Object> handleTask = this.getLatestHandleTask(exeId, flowNode.getNodeKey());
            if (handleTask != null) {
                String nextNodeAssignerIds = (String) handleTask.get("TASK_REALHANDLERID");
                String nextNodeAssignerNames = (String) handleTask.get("TASK_REALHANDLERNAME");
                Map<String, Object> step = new HashMap<String, Object>();
                step.put("VALUE", flowNode.getNodeKey());
                step.put("LABEL", flowNode.getNodeName());
                step.put("isordertask", "-1");
                step.put("handlernature", handleTask.get("TASK_HANDLERTYPE"));
                step.put("assignerIds", nextNodeAssignerIds);
                step.put("assignerNames", nextNodeAssignerNames);
                list.add(step);
            }
        }
        return list;
    }

    /**
     * 获取原路径
     *
     * @param oldTranTaskId
     * @return
     */
    @Override
    public List<FlowNextStep> getOldNextSteps(String oldTranTaskId) {
        //获取原路返回的任务ID
        Map<String, Object> backTainTask = dao.
                getRecord("JBPM6_TASK", new String[]{"TASK_ID"}, new Object[]{oldTranTaskId});
        String TASK_NODEKEY = (String) backTainTask.get("TASK_NODEKEY");
        String TASK_NODENAME = (String) backTainTask.get("TASK_NODENAME");
        String TASK_REALHANDLERID = (String) backTainTask.get("TASK_REALHANDLERID");
        String TASK_REALHANDLERNAME = (String) backTainTask.get("TASK_REALHANDLERNAME");
        String TASK_HANDLERTYPE = (String) backTainTask.get("TASK_HANDLERTYPE");
        List<FlowNextStep> nextStepList = new ArrayList<FlowNextStep>();
        FlowNextStep nextStep = new FlowNextStep();
        nextStep.setIsBranch("-1");
        nextStep.setNodeKeys(TASK_NODEKEY);
        nextStep.setNodeNames(TASK_NODENAME);
        List<NodeAssigner> nodeAssignerList = new ArrayList<NodeAssigner>();
        NodeAssigner assigner = new NodeAssigner();
        assigner.setAssignerIds(TASK_REALHANDLERID);
        assigner.setAssignerNames(TASK_REALHANDLERNAME);
        assigner.setIsOrder("-1");
        assigner.setHandlerNature(TASK_HANDLERTYPE);
        assigner.setNextAuditType("1");
        assigner.setHandlerType("1");
        nodeAssignerList.add(assigner);
        nextStep.setNodeAssignerList(nodeAssignerList);
        nextStepList.add(nextStep);
        return nextStepList;
    }

    /**
     * 获取固定退回节点数据
     *
     * @param backAssignNode
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public FlowNextStep getBackAssignNode(String backAssignNode, JbpmFlowInfo jbpmFlowInfo) {
        //获取实例ID
        String exeId = jbpmFlowInfo.getJbpmExeId();
        StringBuffer sql = new StringBuffer("SELECT * FROM JBPM6_TASK J");
        sql.append(" WHERE J.TASK_EXEID=? AND J.TASK_NODEKEY=? AND J.TASK_STATUS=? ");
        sql.append(" ORDER BY J.TASK_ENDTIME DESC ");
        List<Map<String, Object>> taskList = dao.findBySql(sql.toString(), new Object[]{exeId, backAssignNode,
                JbpmTaskService.TASKSTATUS_AUDITED}, null);
        Map<String, Object> task = taskList.get(0);
        String TASK_NODEKEY = (String) task.get("TASK_NODEKEY");
        String TASK_NODENAME = (String) task.get("TASK_NODENAME");
        String TASK_HANDLERTYPE = (String) task.get("TASK_HANDLERTYPE");
        String TASK_REALHANDLERID = (String) task.get("TASK_REALHANDLERID");
        String TASK_REALHANDLERNAME = (String) task.get("TASK_REALHANDLERNAME");
        FlowNextStep nextStep = new FlowNextStep();
        nextStep.setNodeKeys(TASK_NODEKEY);
        nextStep.setNodeNames(TASK_NODENAME);
        nextStep.setIsBranch("-1");
        List<NodeAssigner> nodeAssignerList = new ArrayList<NodeAssigner>();
        NodeAssigner nodeAssigner = new NodeAssigner();
        nodeAssigner.setIsOrder("-1");
        nodeAssigner.setHandlerNature(TASK_HANDLERTYPE);
        nodeAssigner.setNodeKey(TASK_NODEKEY);
        nodeAssigner.setNodeName(TASK_NODENAME);
        nodeAssigner.setNextAuditType("1");
        nodeAssigner.setAssignerIds(TASK_REALHANDLERID);
        nodeAssigner.setAssignerNames(TASK_REALHANDLERNAME);
        nodeAssignerList.add(nodeAssigner);
        nextStep.setNodeAssignerList(nodeAssignerList);
        return nextStep;
    }

    /**
     * 获取指定节点数据
     *
     * @param goAssignNodeKey
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public FlowNextStep getAppointAssignNode(String goAssignNodeKey, JbpmFlowInfo jbpmFlowInfo) {
        FlowNode node = this.flowNodeService.getFlowNode(jbpmFlowInfo.getJbpmDefId(),
                Integer.parseInt(jbpmFlowInfo.getJbpmDefVersion()), goAssignNodeKey);
        String TASK_NODENAME = "";
        if (node != null) {
            TASK_NODENAME = node.getNodeName();
        }
        FlowNextStep nextStep = new FlowNextStep();
        nextStep.setNodeKeys(goAssignNodeKey);
        nextStep.setNodeNames(TASK_NODENAME);
        nextStep.setIsBranch("-1");
        List<NodeAssigner> nodeAssignerList = new ArrayList<NodeAssigner>();
        NodeAssigner nodeAssigner = new NodeAssigner();
        nodeAssigner.setIsOrder("-1");
        nodeAssigner.setHandlerNature("1");
        nodeAssigner.setNodeKey(goAssignNodeKey);
        nodeAssigner.setNodeName(TASK_NODENAME);
        nodeAssigner.setNextAuditType("1");
        nodeAssigner.setHandlerUrl("appmodel/DesignController.do?goGenUiView&DESIGN_CODE=roleuserselector");
        nodeAssigner.setHandlerHeight("500px");
        nodeAssigner.setHandlerWidth("1000px");

        nodeAssignerList.add(nodeAssigner);
        nextStep.setNodeAssignerList(nodeAssignerList);
        return nextStep;
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
        return dao.getLatestTaskId(exeId, nodeKey);
    }

    /**
     * 获取最后一条任务的节点KEY
     *
     * @param exeId
     * @return
     */
    @Override
    public String getLastTaskNodeKey(String exeId) {
        return dao.getLastTaskNodeKey(exeId);
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
        return dao.getLatestTaskId(exeId, nodeKey, taskStatus);
    }

    /**
     * 派发父亲流程的任务
     *
     * @param parentExeId
     * @param jbpmFlowInfo
     */
    @Override
    public void assignParentTask(String parentExeId, JbpmFlowInfo jbpmFlowInfo) {
        if (StringUtils.isNotEmpty(parentExeId)) {
            Map<String, Object> parentExe = dao.getRecord("JBPM6_EXECUTION",
                    new String[]{"EXECUTION_ID"}, new Object[]{parentExeId});
            //获取父流程的子流程任务
            Map<String, Object> parentExeSubTask = dao.getRecord("JBPM6_TASK",
                    new String[]{"TASK_EXEID", "TASK_STATUS", "TASK_ISSUBPROCESS"},
                    new Object[]{parentExeId, JbpmTaskService.TASKSTATUS_HANDLING, "1"}
            );
            parentExeSubTask.put("TASK_ENDTIME",
                    PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            parentExeSubTask.put("TASK_STATUS", JbpmTaskService.TASKSTATUS_AUDITED);
            //获取当前登录用户
            Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
            parentExeSubTask.put("TASK_REALHANDLERID", backLoginUser.get("SYSUSER_ID"));
            parentExeSubTask.put("TASK_REALHANDLERNAME", backLoginUser.get("SYSUSER_NAME"));
            parentExeSubTask.put("TASK_REALHANDLERCOMPANYID", backLoginUser.get("SYSUSER_COMPANYID"));
            parentExeSubTask.put("TASK_REALHANDLERCOMPANYPATH", backLoginUser.get("COMPANY_PATH"));
            parentExeSubTask.put("TASK_HANDLEDESC", "已办理");
            dao.saveOrUpdate("JBPM6_TASK", parentExeSubTask, SysConstants.ID_GENERATOR_UUID, null);
            //构建新的流程信息对象,并且重新派发父亲流程的任务
            JbpmFlowInfo parentJbpmFlowInfo = new JbpmFlowInfo();
            parentJbpmFlowInfo.setJbpmAssignJson(jbpmFlowInfo.getJbpmAssignJson());
            parentJbpmFlowInfo.setJbpmDefId(parentExe.get("FLOWDEF_ID").toString());
            parentJbpmFlowInfo.setJbpmDefVersion(parentExe.get("EXECUTION_VERSION").toString());
            parentJbpmFlowInfo.setJbpmOperingNodeKey(parentExeSubTask.get("TASK_NODEKEY").toString());
            parentJbpmFlowInfo.setJbpmExeId(parentExeId);
            parentJbpmFlowInfo.setJbpmMainTableName(jbpmFlowInfo.getJbpmMainTableName());
            parentJbpmFlowInfo.setJbpmMainTableRecordId(jbpmFlowInfo.getJbpmMainTableRecordId());
            //派发父亲新任务
            this.assignerTask(new HashMap<String, Object>(), parentJbpmFlowInfo);
            //更新父亲流程的状态
            this.executionService.updateFlowExe(new HashMap<String, Object>(), parentJbpmFlowInfo);
        }
    }

    /**
     * 获取已经超期的流程任务列表
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findTimeOutList() {
        String[] runningStatus = new String[]{String.valueOf(JbpmTaskService.TASKSTATUS_HANDLEUP),
                String.valueOf(JbpmTaskService.TASKSTATUS_HANDLING)};
        StringBuffer sql = new StringBuffer("SELECT * FROM JBPM6_TASK ");
        sql.append("T WHERE T.TASK_ENDTIME IS NULL AND T.TASK_STATUS IN ");
        sql.append(PlatStringUtil.getSqlInCondition(runningStatus));
        sql.append(" AND T.TASK_TIMEOUT=? ");
        sql.append(" ORDER BY T.TASK_CREATETIME ASC ");
        return dao.findBySql(sql.toString(), new Object[]{1}, null);
    }

    /**
     * 描述
     *
     * @return
     * @created 2017年8月17日 下午4:04:57
     */
    @Override
    public int getTodoListNum() {
        Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
        String sysuserId = (String) backLoginUser.get("SYSUSER_ID");
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT COUNT(T.TASK_ID) ALLNUM FROM JBPM6_TASK T  ");
        sql.append(" WHERE T.TASK_STATUS IN (-1,1)  AND T.TASK_ENDTIME IS NULL  ");
        sql.append(" AND T.TASK_HANDLERIDS LIKE ? ");
        int num = 0;
        Map<String, Object> map = dao.getBySql(sql.toString(),
                new Object[]{"%" + sysuserId + "%"});
        if (map != null) {
            num = Integer.parseInt(map.get("ALLNUM").toString());
        }
        return num;
    }
}
