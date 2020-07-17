/*
 * Copyright (c) 2005, 2017, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.core.service.SysUserService;
import com.housoo.platform.core.service.WorkdayService;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.*;
import com.housoo.platform.workflow.dao.JbpmDao;
import com.housoo.platform.workflow.model.*;
import com.housoo.platform.workflow.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 描述
 *
 * @author 胡裕
 * @created 2017年5月14日 下午4:57:05
 */
@Service("jbpmService")
public class JbpmServiceImpl extends BaseServiceImpl implements JbpmService {
    /**
     * 所引入的dao
     */
    @Resource
    private JbpmDao dao;
    /**
     *
     */
    @Resource
    private EventService eventService;
    /**
     *
     */
    @Resource
    private ExecutionService executionService;
    /**
     *
     */
    @Resource
    private FlowDefService flowDefService;
    /**
     *
     */
    @Resource
    private FlowNodeService flowNodeService;
    /**
     *
     */
    @Resource
    private NodeBindService nodeBindService;
    /**
     *
     */
    @Resource
    private FlowFormService flowFormService;
    /**
     *
     */
    @Resource
    private FormFieldService formFieldService;
    /**
     *
     */
    @Resource
    private NodeAssignerService nodeAssignerService;
    /**
     *
     */
    @Resource
    private JbpmTaskService jbpmTaskService;
    /**
     *
     */
    @Resource
    private WorkdayService workdayService;
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
    private SysUserService sysUserService;

    /**
     *
     */
    @Resource
    private FieldModifyService fieldModifyService;
    /**
     *
     */
    @Resource
    private ExePresetService exePresetService;

    /**
     *
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 调用节点事件
     *
     * @param postParams
     * @param jbpmFlowInfo
     */
    public void invokeNodeEvent(Map<String, Object> postParams, JbpmFlowInfo jbpmFlowInfo, String eventType) {
        jbpmFlowInfo.setJbpmEventType(eventType);
        List<String> eventCodeList = eventService.findEventCodes(jbpmFlowInfo.getJbpmDefId(),
                jbpmFlowInfo.getJbpmDefVersion(),
                jbpmFlowInfo.getJbpmOperingNodeKey(), eventType);
        for (String eventCode : eventCodeList) {
            String beanId = eventCode.split("[.]")[0];
            String method = eventCode.split("[.]")[1];
            Object serviceBean = PlatAppUtil.getBean(beanId);
            if (serviceBean != null) {
                Method invokeMethod;
                try {
                    invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                            new Class[]{Map.class, JbpmFlowInfo.class});
                    invokeMethod.invoke(serviceBean,
                            new Object[]{postParams, jbpmFlowInfo});
                } catch (Exception e) {
                    PlatLogUtil.printStackTrace(e);
                }
            }
        }
    }


    /**
     * 处理流程工作
     *
     * @param postParams
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public Map<String, Object> doFlowJob(Map<String, Object> postParams, JbpmFlowInfo jbpmFlowInfo) {
        if (StringUtils.isEmpty(jbpmFlowInfo.getJbpmHandleTaskStatus())
                || jbpmFlowInfo.getJbpmHandleTaskStatus().equals(String.valueOf(JbpmTaskService.TASKSTATUS_AUDITED)) ||
                jbpmFlowInfo.getJbpmHandleTaskStatus().equals(String.valueOf(JbpmTaskService.TASKSTATUS_HANDLEUP))
                ) {
            //=================1.开始执行前置事件=====================
            this.invokeNodeEvent(postParams, jbpmFlowInfo, EventService.EVENTTYPE_PRE);
            //=================1.结束执行前置事件=====================
            //保存修改字段日志
            fieldModifyService.saveFieldModify(postParams, jbpmFlowInfo);
            //=================2.开始执行存储事件=====================
            this.invokeNodeEvent(postParams, jbpmFlowInfo, EventService.EVENTTYPE_SAVE);
            //=================2.结束执行存储事件=====================
        }

        //=================3.开始执行流程实例存储事件=====================
        String jbpmStopExe = jbpmFlowInfo.getJbpmStopExe();
        if (!(StringUtils.isNotEmpty(jbpmStopExe) && "true".equals(jbpmStopExe))) {
            executionService.saveFlowExe(postParams, jbpmFlowInfo);
        }
        //=================3.结束执行流程实例存储事件=====================
        //=================4.开始执行当前流程任务的更新API=====================
        jbpmStopExe = jbpmFlowInfo.getJbpmStopExe();
        if (!(StringUtils.isNotEmpty(jbpmStopExe) && "true".equals(jbpmStopExe))) {
            jbpmTaskService.saveOrUpdateCurrentTask(postParams, jbpmFlowInfo);
        }
        //=================4.结束执行当前流程任务的更新API=====================
        //=================5.开始执行分配流程任务API=====================
        jbpmStopExe = jbpmFlowInfo.getJbpmStopExe();
        if (!(StringUtils.isNotEmpty(jbpmStopExe) && "true".equals(jbpmStopExe))) {
            String jbpmAllowAssignTask = jbpmFlowInfo.getJbpmAllowAssignTask();
            if ("true".equals(jbpmAllowAssignTask)) {
                jbpmTaskService.assignerTask(postParams, jbpmFlowInfo);
            }
        }
        //=================5.结束执行分配流程任务API=====================
        //=================6.开始执行更新流程实例信息=====================
        jbpmStopExe = jbpmFlowInfo.getJbpmStopExe();
        if (!(StringUtils.isNotEmpty(jbpmStopExe) && "true".equals(jbpmStopExe))) {
            executionService.updateFlowExe(postParams, jbpmFlowInfo);
        }
        //=================6.结束执行更新流程实例信息=====================
        jbpmStopExe = jbpmFlowInfo.getJbpmStopExe();
        if (StringUtils.isEmpty(jbpmFlowInfo.getJbpmHandleTaskStatus())
                || jbpmFlowInfo.getJbpmHandleTaskStatus().
                equals(String.valueOf(JbpmTaskService.TASKSTATUS_AUDITED))) {
            //=================7.开始执行后置事件===========================
            this.invokeNodeEvent(postParams, jbpmFlowInfo, EventService.EVENTTYPE_AFTER);
            //=================7.结束执行后置事件===========================
        }
        jbpmFlowInfo.setJbpmStopExe(null);
        jbpmFlowInfo.setJbpmHandleOpinion(null);
        jbpmFlowInfo.setJbpmAssignJson(null);
        String jbpmFlowInfoJson = JSON.toJSONString(jbpmFlowInfo);
        postParams.put("JbpmFlowInfoJson", jbpmFlowInfoJson);
        return postParams;
    }

    /**
     * 通用保存单表业务数据接口
     *
     * @param postParams
     * @param jbpmFlowInfo
     */
    @Override
    public void genSaveBusData(Map<String, Object> postParams, JbpmFlowInfo jbpmFlowInfo) {
        //获取主表名称
        String mainTableName = jbpmFlowInfo.getJbpmMainTableName();
        Map<String, Object> mainRecord = dao.saveOrUpdate(mainTableName, postParams,
                SysConstants.ID_GENERATOR_UUID, null);
        String recordId = (String) mainRecord.get(jbpmFlowInfo.getJbpmMainTablePkName());
        jbpmFlowInfo.setJbpmMainTableRecordId(recordId);
    }

    /**
     * 获取流程对象
     *
     * @param jbpmExeId
     * @param flowDefId
     * @param jbpmIsQuery
     * @param jbpmOperingTaskId
     * @return
     */
    @Override
    public JbpmFlowInfo getJbpmFlowInfo(String jbpmExeId, String flowDefId, String jbpmIsQuery, String jbpmOperingTaskId) {
        Map<String, Object> flowDef = null;
        Map<String, Object> execution = null;
        //===========开始构建流程对象信息============================
        JbpmFlowInfo jbpmFlowInfo = new JbpmFlowInfo();
        //定义正在允许的节点KEYS
        String runningNodeKeys = null;
        if (StringUtils.isNotEmpty(jbpmExeId)) {
            //获取流程实例信息
            execution = this.executionService.getRecord("JBPM6_EXECUTION"
                    , new String[]{"EXECUTION_ID"}, new Object[]{jbpmExeId});
            String FLOWDEF_ID = (String) execution.get("FLOWDEF_ID");
            String EXECUTION_VERSION = execution.get("EXECUTION_VERSION").toString();
            flowDef = flowDefService.getFlowDefInfo(FLOWDEF_ID, Integer.parseInt(EXECUTION_VERSION));
            runningNodeKeys = (String) execution.get("CURRENT_NODEKEYS");
            String SERITEM_ID = (String) execution.get("SERITEM_ID");
            jbpmFlowInfo.setJbpmSerItemId(SERITEM_ID);
        } else {
            flowDef = flowDefService.getRecord("JBPM6_FLOWDEF", new String[]{"FLOWDEF_ID"}, new Object[]{flowDefId});
        }
        if (StringUtils.isNotEmpty(jbpmOperingTaskId)) {
            jbpmFlowInfo.setJbpmOperingTaskId(jbpmOperingTaskId);
        }
        //获取开始节点
        FlowNode startNode = flowNodeService.getStartFlowNode(flowDef);
        //获取当前环节绑定数据列表
        List<Map<String, Object>> nodeBindList = null;
        if (StringUtils.isNotEmpty(jbpmOperingTaskId)) {
            Map<String, Object> jbpmTask = jbpmTaskService.getRecord("JBPM6_TASK",
                    new String[]{"TASK_ID"}, new Object[]{jbpmOperingTaskId});
            String operingNodeKey = (String) jbpmTask.get("TASK_NODEKEY");
            nodeBindList = nodeBindService.findBindNodeList(flowDef, operingNodeKey);
        } else if (StringUtils.isNotEmpty(runningNodeKeys)) {
            if (runningNodeKeys.split(",").length == 1) {
                //获取绑定节点配置
                String defId = (String) flowDef.get("FLOWDEF_ID");
                String defVersion = (String) flowDef.get("FLOWDEF_VERSION").toString();
                Map<String, Object> nodeBind = nodeBindService.getNodeBind(defId, defVersion,
                        runningNodeKeys, NodeBindService.BINDTYPE_NODECONIG);
                String NODEBIND_SUBDEFID = (String) nodeBind.get("NODEBIND_SUBDEFID");
                if (StringUtils.isNotEmpty(NODEBIND_SUBDEFID)) {
                    Map<String, Object> subFlowDef = flowDefService.getRecord("JBPM6_FLOWDEF",
                            new String[]{"FLOWDEF_ID"}, new Object[]{NODEBIND_SUBDEFID});
                    FlowNode subFlowStartNode = flowNodeService.getStartFlowNode(subFlowDef);
                    nodeBindList = nodeBindService.findBindNodeList(subFlowDef, subFlowStartNode.getNodeKey());
                } else {
                    nodeBindList = nodeBindService.findBindNodeList(flowDef, runningNodeKeys.split(",")[0]);
                }
            } else {
                nodeBindList = nodeBindService.findBindNodeList(flowDef, runningNodeKeys.split(",")[0]);
            }
        } else {
            nodeBindList = nodeBindService.findBindNodeList(flowDef, startNode.getNodeKey());
        }
        //获取当前环节绑定的表单
        Map<String, Object> nodeBindForm = flowFormService.getCurrentNodeBindForm(nodeBindList);
        jbpmFlowInfo.setJbpmDefId(flowDef.get("FLOWDEF_ID").toString());
        jbpmFlowInfo.setJbpmDefVersion(flowDef.get("FLOWDEF_VERSION").toString());
        jbpmFlowInfo.setJbpmDefCode(flowDef.get("FLOWDEF_CODE").toString());
        setJbpmBaseFlowInfo(jbpmExeId, jbpmIsQuery, jbpmOperingTaskId,
                execution, jbpmFlowInfo, runningNodeKeys, startNode);
        if (StringUtils.isNotEmpty(jbpmIsQuery)) {
            jbpmFlowInfo.setJbpmIsQuery(jbpmIsQuery);
        } else {
            jbpmFlowInfo.setJbpmIsQuery("false");
        }
        jbpmFlowInfo.setJbpmMainTableName(nodeBindForm.get("FLOWFORM_MAINTABLENAME").toString());
        String mainTablePKName = flowDefService.findPrimaryKeyNames(jbpmFlowInfo.getJbpmMainTableName()).get(0);
        jbpmFlowInfo.setJbpmMainTablePkName(mainTablePKName);
        jbpmFlowInfo.setJbpmMainClassName(nodeBindForm.get("FLOWFORM_MAINCLASS").toString());
        jbpmFlowInfo.setJbpmDefName(flowDef.get("FLOWDEF_NAME").toString());
        if (execution != null) {
            jbpmFlowInfo.setJbpmCreatorId(execution.get("CREATOR_ID").toString());
            jbpmFlowInfo.setJbpmCreatorName(execution.get("CREATOR_NAME").toString());
        } else {
            //获取当前登录用户
            Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
            jbpmFlowInfo.setJbpmCreatorId(backLoginUser.get("SYSUSER_ID").toString());
            jbpmFlowInfo.setJbpmCreatorName(backLoginUser.get("SYSUSER_NAME").toString());
        }
        jbpmFlowInfo.setJbpmOperingHandlerType(FlowAssignInfo.ASSIGNERNATURE_BACK);
        //获取定义的JSON
        String defJson = (String) flowDef.get("FLOWDEF_JSON");
        jbpmFlowInfo.setJbpmDefJson(defJson);
        String FLOWFORM_DESIGNCODE = (String) nodeBindForm.get("FLOWFORM_DESIGNCODE");
        jbpmFlowInfo.setJbpmOperingDesignCode(FLOWFORM_DESIGNCODE);
        jbpmFlowInfo.setJbpmOperingFormBeforeInter((String) nodeBindForm.get("FLOWFORM_BEFOREINTER"));
        //获取设置的流程总时限配置
        if (flowDef.get("TOTALLIMIT_DAYS") != null) {
            String TOTALLIMIT_DAYS = flowDef.get("TOTALLIMIT_DAYS").toString();
            String TOTALLIMIT_TYPES = flowDef.get("TOTALLIMIT_TYPES").toString();
            jbpmFlowInfo.setJbpmDefAllLimitDays(TOTALLIMIT_DAYS);
            jbpmFlowInfo.setJbpmDefAllLimitType(TOTALLIMIT_TYPES);
        }
        //获取自定义控件节点配置
        Map<String, Object> defCtrlBind = nodeBindService.getNodeBind(jbpmFlowInfo.getJbpmDefId(),
                jbpmFlowInfo.getJbpmDefVersion(),
                jbpmFlowInfo.getJbpmOperingNodeKey(), NodeBindService.BINDTYPE_DEFCTRL);
        if (defCtrlBind != null) {
            String NODEBIND_FIELDAUTHJSON = (String) defCtrlBind.get("NODEBIND_FIELDAUTHJSON");
            jbpmFlowInfo.setJbpmFieldAuthJson(NODEBIND_FIELDAUTHJSON);
        }
        return jbpmFlowInfo;
    }

    /**
     * @param jbpmExeId
     * @param jbpmIsQuery
     * @param jbpmOperingTaskId
     * @param execution
     * @param jbpmFlowInfo
     * @param runningNodeKeys
     * @param startNode
     */
    private void setJbpmBaseFlowInfo(String jbpmExeId, String jbpmIsQuery,
                                     String jbpmOperingTaskId, Map<String, Object> execution,
                                     JbpmFlowInfo jbpmFlowInfo, String runningNodeKeys,
                                     FlowNode startNode) {
        if (execution != null) {
            //获取流程实例状态
            String exeStatus = (String) execution.get("STATUS");
            if (exeStatus.equals(ExecutionService.STATUS_DRAFT)) {
                jbpmFlowInfo.setJbpmRunningNodeKeys(startNode.getNodeKey());
                jbpmFlowInfo.setJbpmOperingNodeKey(startNode.getNodeKey());
                jbpmFlowInfo.setJbpmStartNodeKey(startNode.getNodeKey());
                jbpmFlowInfo.setJbpmOperingNodeName(startNode.getNodeName());
                jbpmFlowInfo.setJbpmMainTableRecordId(execution.get("TMPSAVE_RECORDID").toString());
            } else if (StringUtils.isNotEmpty(runningNodeKeys) && "true".equals(jbpmIsQuery)) {
                jbpmFlowInfo.setJbpmRunningNodeKeys(runningNodeKeys);
                jbpmFlowInfo.setJbpmStartNodeKey(startNode.getNodeKey());
                String mainTableRecordId = this.jbpmTaskService.getTaskMainRecordId(jbpmExeId,
                        runningNodeKeys.split(",")[0]);
                jbpmFlowInfo.setJbpmMainTableRecordId(mainTableRecordId);
                if (StringUtils.isNotEmpty(jbpmOperingTaskId)) {
                    Map<String, Object> jbpmTask = jbpmTaskService.getRecord("JBPM6_TASK",
                            new String[]{"TASK_ID"}, new Object[]{jbpmOperingTaskId});
                    String operingNodeKey = (String) jbpmTask.get("TASK_NODEKEY");
                    String operingNodeName = (String) jbpmTask.get("TASK_NODENAME");
                    jbpmFlowInfo.setJbpmOperingNodeKey(operingNodeKey);
                    jbpmFlowInfo.setJbpmOperingNodeName(operingNodeName);
                }
            } else if (StringUtils.isNotEmpty(jbpmOperingTaskId) && "false".equals(jbpmIsQuery)) {
                Map<String, Object> jbpmTask = jbpmTaskService.getRecord("JBPM6_TASK",
                        new String[]{"TASK_ID"}, new Object[]{jbpmOperingTaskId});
                String operingNodeKey = (String) jbpmTask.get("TASK_NODEKEY");
                String operingNodeName = (String) jbpmTask.get("TASK_NODENAME");
                String TASK_MAINRECORDID = (String) jbpmTask.get("TASK_MAINRECORDID");
                jbpmFlowInfo.setJbpmRunningNodeKeys(runningNodeKeys);
                jbpmFlowInfo.setJbpmOperingNodeKey(operingNodeKey);
                jbpmFlowInfo.setJbpmOperingNodeName(operingNodeName);
                jbpmFlowInfo.setJbpmStartNodeKey(startNode.getNodeKey());
                jbpmFlowInfo.setJbpmMainTableRecordId(TASK_MAINRECORDID);
                jbpmFlowInfo.setJbpmOperingTaskId(jbpmOperingTaskId);
                jbpmFlowInfo.setJbpmOperingFromNodeKey((String) jbpmTask.get("TASK_FROMNODEKEY"));
            } else if (!exeStatus.equals(ExecutionService.STATUS_DRAFT)
                    && !exeStatus.equals(ExecutionService.STATUS_RUNNING)) {
                jbpmFlowInfo.setJbpmStartNodeKey(startNode.getNodeKey());
                Map<String, Object> lastestTask = jbpmTaskService.getLatestTaskInfo(jbpmExeId);
                jbpmFlowInfo.setJbpmMainTableRecordId((String) lastestTask.get("TASK_MAINRECORDID"));
            }
            jbpmFlowInfo.setJbpmExeId(jbpmExeId);
        } else {
            jbpmFlowInfo.setJbpmRunningNodeKeys(startNode.getNodeKey());
            jbpmFlowInfo.setJbpmOperingNodeKey(startNode.getNodeKey());
            jbpmFlowInfo.setJbpmOperingNodeName(startNode.getNodeName());
            jbpmFlowInfo.setJbpmStartNodeKey(startNode.getNodeKey());
        }
    }

    /**
     * 通用设置表单业务数据
     *
     * @param request
     * @param jbpmFlowInfo
     */
    @Override
    public void genSetBusData(HttpServletRequest request, JbpmFlowInfo jbpmFlowInfo) {
        //获取主表ID
        String mainRecordId = jbpmFlowInfo.getJbpmMainTableRecordId();
        if (StringUtils.isNotEmpty(mainRecordId)) {
            String tableName = jbpmFlowInfo.getJbpmMainTableName();
            String pkName = jbpmFlowInfo.getJbpmMainTablePkName();
            Map<String, Object> mainRecord = dao.getRecord(tableName,
                    new String[]{pkName}, new Object[]{mainRecordId});
            request.setAttribute(jbpmFlowInfo.getJbpmMainClassName(), mainRecord);
        }
    }

    /**
     * 跳转到流程设计UI界面
     * @param request
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public ModelAndView getFlowDesignUI(HttpServletRequest request, JbpmFlowInfo jbpmFlowInfo) {
        //设置表单字段权限
        if ("false".equals(jbpmFlowInfo.getJbpmIsQuery())) {
            formFieldService.setFlowFormFieldAuth(request, jbpmFlowInfo);
        }
        String defJson = jbpmFlowInfo.getJbpmDefJson();
        defJson = flowDefService.getMonitorFlowDefJson(defJson, jbpmFlowInfo);
        jbpmFlowInfo.setJbpmDefJson(null);
        request.setAttribute("jbpmFlowInfo", jbpmFlowInfo);
        request.setAttribute("FLOWDEF_JSON", StringEscapeUtils.escapeHtml3(defJson));
        String FLOWFORM_DESIGNCODE = jbpmFlowInfo.getJbpmOperingDesignCode();
        String beforeInterCode = jbpmFlowInfo.getJbpmOperingFormBeforeInter();
        if (StringUtils.isNotEmpty(beforeInterCode)) {
            String beanId = beforeInterCode.split("[.]")[0];
            String method = beforeInterCode.split("[.]")[1];
            Object serviceBean = PlatAppUtil.getBean(beanId);
            if (serviceBean != null) {
                Method invokeMethod;
                try {
                    invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                            new Class[]{HttpServletRequest.class, JbpmFlowInfo.class});
                    invokeMethod.invoke(serviceBean,
                            new Object[]{request, jbpmFlowInfo});
                } catch (Exception e) {
                    PlatLogUtil.printStackTrace(e);
                }
            }
        }
        return PlatUICompUtil.goDesignUI(FLOWFORM_DESIGNCODE, request);
    }

    /**
     * 获取下一步审核人列表
     *
     * @param flowVars
     * @param jbpmFlowInfo
     * @param handleType
     * @param nodeAssigner
     * @return
     */
    private List<FlowNextHandler> getNextHandler(Map<String, Object> flowVars,
                                                 JbpmFlowInfo jbpmFlowInfo, String handleType, NodeAssigner nodeAssigner) {
        List<FlowNextHandler> nextHandlers = null;
        //获取默认接口
        String defaultInter = nodeAssigner.getDefaultInter();
        if (StringUtils.isNotEmpty(defaultInter)) {
            //获取动态数据源
            String beanId = defaultInter.split("[.]")[0];
            String method = defaultInter.split("[.]")[1];
            Object serviceBean = PlatAppUtil.getBean(beanId);
            if (serviceBean != null) {
                Method invokeMethod;
                try {
                    invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                            new Class[]{Map.class, NodeAssigner.class, JbpmFlowInfo.class});
                    nextHandlers = (List<FlowNextHandler>) invokeMethod.invoke(serviceBean,
                            new Object[]{flowVars, nodeAssigner, jbpmFlowInfo});
                } catch (Exception e) {
                    PlatLogUtil.printStackTrace(e);
                }
            }
        }
        return nextHandlers;
    }

    /**
     * 获取下一个环节节点列表数据
     *
     * @param nextHandleNodeList
     * @param operingNodeKey
     * @param jbpmFlowInfo
     * @return
     */
    private List<FlowNode> getNextHandleNodeList(List<FlowNode> nextHandleNodeList,
                                                 String operingNodeKey, JbpmFlowInfo jbpmFlowInfo, Map<String, Object> flowVars) {
        if (nextHandleNodeList == null) {
            nextHandleNodeList = new ArrayList<FlowNode>();
        }
        //获取下一环节列表数据
        List<FlowNode> nextNodeList = this.flowNodeService.findNextNodeList(jbpmFlowInfo.getJbpmDefId(),
                Integer.parseInt(jbpmFlowInfo.getJbpmDefVersion()), operingNodeKey);
        if (nextNodeList.size() == 1) {
            if (nextNodeList.get(0).getNodeType().equals(FlowNode.NODETYPE_END)) {
                //获取当前实例
                Map<String, Object> curExe = this.executionService.getRecord("JBPM6_EXECUTION",
                        new String[]{"EXECUTION_ID"}, new Object[]{jbpmFlowInfo.getJbpmExeId()});
                String PARENT_EXECUTION_ID = (String) curExe.get("PARENT_EXECUTION_ID");
                if (StringUtils.isNotEmpty(PARENT_EXECUTION_ID)) {
                    //获取父亲流程实例
                    Map<String, Object> parentExe = this.executionService.getRecord("JBPM6_EXECUTION",
                            new String[]{"EXECUTION_ID"}, new Object[]{PARENT_EXECUTION_ID});
                    String parentDefId = (String) parentExe.get("FLOWDEF_ID");
                    int parentDefVersion = Integer.parseInt(parentExe.get("EXECUTION_VERSION").toString());
                    //获取子流程任务
                    Map<String, Object> parentSubTask = this.jbpmTaskService.getRecord("JBPM6_TASK",
                            new String[]{"TASK_EXEID", "TASK_STATUS", "TASK_ISSUBPROCESS"},
                            new Object[]{PARENT_EXECUTION_ID, JbpmTaskService.TASKSTATUS_HANDLING, "1"});
                    String TASK_NODEKEY = (String) parentSubTask.get("TASK_NODEKEY");
                    nextNodeList = this.flowNodeService.findNextNodeList(parentDefId,
                            parentDefVersion, TASK_NODEKEY);
                    jbpmFlowInfo.setJbpmParentDefId(parentDefId);
                    jbpmFlowInfo.setJbpmParentDefVersion(String.valueOf(parentDefVersion));
                    jbpmFlowInfo.setJbpmParentOperingNodeKey(TASK_NODEKEY);
                }
            }
        }
        jbpmFlowInfo.setJbpmIsParallel("false");
        for (FlowNode nextNode : nextNodeList) {
            String nodeType = nextNode.getNodeType();
            if (nodeType.equals(FlowNode.NODETYPE_TASK) || nodeType.equals(FlowNode.NODETYPE_END) ||
                    nodeType.equals(FlowNode.NODETYPE_SUBPROCESS) || nodeType.equals(FlowNode.NODETYPE_START)) {
                nextHandleNodeList.add(nextNode);
            } else if (nodeType.equals(FlowNode.NODETYPE_DECISION)) {
                nextNodeList = this.flowNodeService.findNextNodeList(jbpmFlowInfo.getJbpmDefId(),
                        Integer.parseInt(jbpmFlowInfo.getJbpmDefVersion()), nextNode.getNodeKey());
                //获取决策事件代码
                List<String> eventList = eventService.findEventCodes(jbpmFlowInfo.getJbpmDefId(),
                        jbpmFlowInfo.getJbpmDefVersion(), nextNode.getNodeKey(), EventService.EVENTTYPE_DESIDE);
                String eventCode = eventList.get(0);
                Set<String> resultNodeKey = null;
                String beanId = eventCode.split("[.]")[0];
                String method = eventCode.split("[.]")[1];
                Object serviceBean = PlatAppUtil.getBean(beanId);
                if (serviceBean != null) {
                    Method invokeMethod;
                    try {
                        invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                                new Class[]{Map.class, JbpmFlowInfo.class});
                        resultNodeKey = (Set<String>) invokeMethod.invoke(serviceBean,
                                new Object[]{flowVars, jbpmFlowInfo});
                    } catch (Exception e) {
                        PlatLogUtil.printStackTrace(e);
                    }
                }
                for (FlowNode resultNode : nextNodeList) {
                    if (resultNodeKey.contains(resultNode.getNodeKey())) {
                        nextHandleNodeList.add(resultNode);
                    }
                }

            } else if (nodeType.equals(FlowNode.NODETYPE_PARALLEL)) {
                jbpmFlowInfo.setJbpmIsParallel("true");
                nextNodeList = this.flowNodeService.findNextNodeList(jbpmFlowInfo.getJbpmDefId(),
                        Integer.parseInt(jbpmFlowInfo.getJbpmDefVersion()), nextNode.getNodeKey());
                for (FlowNode resultNode : nextNodeList) {
                    nextHandleNodeList.add(resultNode);
                }
            } else if (nodeType.equals(FlowNode.NODETYPE_JOIN)) {
                nextNodeList = this.flowNodeService.findNextNodeList(jbpmFlowInfo.getJbpmDefId(),
                        Integer.parseInt(jbpmFlowInfo.getJbpmDefVersion()), nextNode.getNodeKey());
                for (FlowNode resultNode : nextNodeList) {
                    nextHandleNodeList.add(resultNode);
                }
            }
        }
        return nextHandleNodeList;
    }

    /**
     * 获取并行步骤列表
     *
     * @param nextHandleNodeList
     * @param flowVars
     * @param jbpmFlowInfo
     * @return
     */
    public List<FlowNextStep> findNextSteps(List<FlowNode> nextHandleNodeList,
                                            Map<String, Object> flowVars, JbpmFlowInfo jbpmFlowInfo) {
        List<FlowNextStep> nextStepList = new ArrayList<FlowNextStep>();
        for (FlowNode nextHandleNode : nextHandleNodeList) {
            List<NodeAssigner> nodeAssignerList = new ArrayList<NodeAssigner>();
            NodeAssigner nodeAssigner = nodeAssignerService.getNodeAssigner(nextHandleNode.getNodeKey()
                    , flowVars, jbpmFlowInfo);
            nodeAssignerList.add(nodeAssigner);
            String handleType = nodeAssigner.getHandlerType();
            List<FlowNextHandler> nextHandlers = this.getNextHandler(flowVars, jbpmFlowInfo,
                    handleType, nodeAssigner);
            if (nextHandlers != null && nextHandlers.size() > 0) {
                StringBuffer assignerIds = new StringBuffer("");
                StringBuffer assignerNames = new StringBuffer("");
                for (int i = 0; i < nextHandlers.size(); i++) {
                    if (i > 0) {
                        assignerIds.append(",");
                        assignerNames.append(",");
                    }
                    assignerIds.append(nextHandlers.get(i).getAssignerId());
                    assignerNames.append(nextHandlers.get(i).getAssignerName());
                }
                nodeAssigner.setAssignerIds(assignerIds.toString());
                nodeAssigner.setAssignerNames(assignerNames.toString());
            }
            nodeAssigner.setNextHandlers(nextHandlers);
            FlowNextStep nextStep = new FlowNextStep();
            StringBuffer nodeKeys = new StringBuffer("");
            StringBuffer nodeNames = new StringBuffer("");
            for (int i = 0; i < nodeAssignerList.size(); i++) {
                if (i > 0) {
                    nodeKeys.append(",");
                    nodeNames.append(",");
                }
                nodeKeys.append(nodeAssignerList.get(i).getNodeKey());
                nodeNames.append(nodeAssignerList.get(i).getNodeName());
            }
            nextStep.setNodeKeys(nodeKeys.toString());
            nextStep.setNodeNames(nodeNames.toString());
            nextStep.setIsBranch("-1");
            nextStep.setNodeAssignerList(nodeAssignerList);
            nextStepList.add(nextStep);
        }
        return nextStepList;
    }

    /**
     * 获取下一步骤信息
     *
     * @param nextHandleNodeList
     * @param flowVars
     * @param jbpmFlowInfo
     * @return
     */
    private FlowNextStep getFlowNextStep(List<FlowNode> nextHandleNodeList,
                                         Map<String, Object> flowVars, JbpmFlowInfo jbpmFlowInfo, String isBranch) {
        List<NodeAssigner> nodeAssignerList = new ArrayList<NodeAssigner>();
        for (FlowNode nextHandleNode : nextHandleNodeList) {
            if (!nextHandleNode.getNodeType().equals(FlowNode.NODETYPE_END)) {
                NodeAssigner nodeAssigner = null;
                if (StringUtils.isNotEmpty(jbpmFlowInfo.getJbpmParentDefId())) {
                    JbpmFlowInfo parentFlowInfo = new JbpmFlowInfo();
                    parentFlowInfo.setJbpmDefId(jbpmFlowInfo.getJbpmParentDefId());
                    parentFlowInfo.setJbpmDefVersion(jbpmFlowInfo.getJbpmParentDefVersion());
                    parentFlowInfo.setJbpmOperingNodeKey(jbpmFlowInfo.getJbpmParentOperingNodeKey());
                    nodeAssigner = nodeAssignerService.getNodeAssigner(nextHandleNode.getNodeKey()
                            , flowVars, parentFlowInfo);
                } else {
                    nodeAssigner = nodeAssignerService.getNodeAssigner(nextHandleNode.getNodeKey()
                            , flowVars, jbpmFlowInfo);
                }
                nodeAssignerList.add(nodeAssigner);
                List<FlowNextHandler> nextHandlers = exePresetService.findPresetHandler(
                        jbpmFlowInfo.getJbpmExeId(), nodeAssigner.getNodeKey());
                if (nextHandlers != null && nextHandlers.size() > 0) {
                    nodeAssigner.setHandlerType("1");
                    nodeAssigner.setHandlerNature("1");
                } else {
                    String handleType = nodeAssigner.getHandlerType();
                    nextHandlers = this.getNextHandler(flowVars, jbpmFlowInfo,
                            handleType, nodeAssigner);
                }
                if (nextHandlers != null && nextHandlers.size() > 0) {
                    StringBuffer assignerIds = new StringBuffer("");
                    StringBuffer assignerNames = new StringBuffer("");
                    for (int i = 0; i < nextHandlers.size(); i++) {
                        if (i > 0) {
                            assignerIds.append(",");
                            assignerNames.append(",");
                        }
                        assignerIds.append(nextHandlers.get(i).getAssignerId());
                        assignerNames.append(nextHandlers.get(i).getAssignerName());
                    }
                    nodeAssigner.setAssignerIds(assignerIds.toString());
                    nodeAssigner.setAssignerNames(assignerNames.toString());
                }
                nodeAssigner.setNextHandlers(nextHandlers);
            } else {
                NodeAssigner assigner = new NodeAssigner();
                assigner.setNodeKey(nextHandleNode.getNodeKey());
                assigner.setNodeName(nextHandleNode.getNodeName());
                nodeAssignerList.add(assigner);
            }
        }
        FlowNextStep nextStep = new FlowNextStep();
        StringBuffer nodeKeys = new StringBuffer("");
        StringBuffer nodeNames = new StringBuffer("");
        for (int i = 0; i < nodeAssignerList.size(); i++) {
            if (i > 0) {
                nodeKeys.append(",");
                nodeNames.append(",");
            }
            nodeKeys.append(nodeAssignerList.get(i).getNodeKey());
            nodeNames.append(nodeAssignerList.get(i).getNodeName());
        }
        nextStep.setNodeKeys(nodeKeys.toString());
        nextStep.setNodeNames(nodeNames.toString());
        nextStep.setIsBranch(isBranch);
        nextStep.setNodeAssignerList(nodeAssignerList);
        return nextStep;
    }

    /**
     * 获取下一环节列表数据
     *
     * @param flowVars
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public List<FlowNextStep> findNextStepList(Map<String, Object> flowVars, JbpmFlowInfo jbpmFlowInfo) {
        List<FlowNode> nextHandleNodeList = this.getNextHandleNodeList(null, jbpmFlowInfo.
                getJbpmOperingNodeKey(), jbpmFlowInfo, flowVars);
        List<FlowNextStep> nextStepList = new ArrayList<FlowNextStep>();
        //获取下一环节的数量值
        if (nextHandleNodeList.size() == 1) {
            FlowNextStep nextStep = this.getFlowNextStep(nextHandleNodeList, flowVars, jbpmFlowInfo, "-1");
            nextStepList.add(nextStep);
        } else if (nextHandleNodeList.size() > 1) {
            //获取是否是并行流程
            String isParallel = jbpmFlowInfo.getJbpmIsParallel();
            if ("false".equals(isParallel)) {
                //说明是分支选择流程
                FlowNextStep nextStep = this.getFlowNextStep(nextHandleNodeList, flowVars, jbpmFlowInfo, "1");
                nextStepList.add(nextStep);
            } else {
                nextStepList = this.findNextSteps(nextHandleNodeList, flowVars, jbpmFlowInfo);
            }
        }
        return nextStepList;
    }

    /**
     * 获取下一环节分支节点数据
     *
     * @param stepToken
     * @return
     */
    @Override
    public List<Map<String, Object>> getNextStepBranch(String stepToken) {
        FlowNextStep nextStep = (FlowNextStep) PlatAppUtil.getSessionCache(stepToken);
        List<NodeAssigner> list = nextStep.getNodeAssignerList();
        List<Map<String, Object>> steplist = new ArrayList<Map<String, Object>>();
        for (NodeAssigner nodeAssigner : list) {
            Map<String, Object> step = PlatBeanUtil.beanToMap(nodeAssigner);
            step.put("VALUE", nodeAssigner.getNodeKey());
            step.put("LABEL", nodeAssigner.getNodeName());
            step.remove("nextHandlers");
            step.put("isordertask", nodeAssigner.getIsOrder());
            step.put("handlernature", nodeAssigner.getHandlerNature());
            steplist.add(step);
        }
        PlatAppUtil.removeSessionCache(stepToken);
        return steplist;
    }

    /**
     * 启动流程
     * 必须设置参数
     * jbpmDefCode:流程定义编码
     * jbpmOperingHandlerType:发起人类型(1后台用户 2网站用户)
     * jbpmCreatorId:发起人ID
     * 其它的参数为业务字段参数
     *
     * @param flowVars
     * @return
     */
    @Override
    public Map<String, Object> startFlow(Map<String, Object> flowVars) {
        JbpmFlowInfo jbpmFlowInfo = (JbpmFlowInfo) PlatBeanUtil.mapToBean(new JbpmFlowInfo(), flowVars);
        //获取流程定义信息
        Map<String, Object> flowDef = dao.getRecord("JBPM6_FLOWDEF",
                new String[]{"FLOWDEF_CODE"}, new Object[]{jbpmFlowInfo.getJbpmDefCode()});
        //获取发起人类型
        String jbpmOperingHandlerType = jbpmFlowInfo.getJbpmOperingHandlerType();
        if ("1".equals(jbpmOperingHandlerType)) {
            //获取发起人数据
            Map<String, Object> backUser = dao.getRecord("PLAT_SYSTEM_SYSUSER",
                    new String[]{"SYSUSER_ID"}, new Object[]{jbpmFlowInfo.getJbpmCreatorId()});
            jbpmFlowInfo.setJbpmCreatorName((String) backUser.get("SYSUSER_NAME"));
        } else {

        }
        jbpmFlowInfo.setJbpmDefId((String) flowDef.get("FLOWDEF_ID"));
        jbpmFlowInfo.setJbpmDefName((String) flowDef.get("FLOWDEF_NAME"));
        jbpmFlowInfo.setJbpmDefVersion(flowDef.get("FLOWDEF_VERSION").toString());
        jbpmFlowInfo.setJbpmExeId("-1");
        jbpmFlowInfo.setJbpmIsQuery("false");
        jbpmFlowInfo.setJbpmIsTempSave("false");
        //获取开始节点
        FlowNode startNode = flowNodeService.getStartFlowNode(flowDef);
        //获取当前环节绑定数据列表
        List<Map<String, Object>> nodeBindList = nodeBindService.findBindNodeList(flowDef, startNode.getNodeKey());
        //获取当前环节绑定的表单
        Map<String, Object> nodeBindForm = flowFormService.getCurrentNodeBindForm(nodeBindList);
        jbpmFlowInfo.setJbpmRunningNodeKeys(startNode.getNodeKey());
        jbpmFlowInfo.setJbpmOperingNodeKey(startNode.getNodeKey());
        jbpmFlowInfo.setJbpmOperingNodeName(startNode.getNodeName());
        jbpmFlowInfo.setJbpmStartNodeKey(startNode.getNodeKey());
        jbpmFlowInfo.setJbpmMainTableName(nodeBindForm.get("FLOWFORM_MAINTABLENAME").toString());
        String mainTablePKName = flowDefService.findPrimaryKeyNames(jbpmFlowInfo.getJbpmMainTableName()).get(0);
        jbpmFlowInfo.setJbpmMainTablePkName(mainTablePKName);
        jbpmFlowInfo.setJbpmMainClassName(nodeBindForm.get("FLOWFORM_MAINCLASS").toString());
        Map<String, Object> result = new HashMap<String, Object>();
        boolean notFoundHandler = false;
        List<FlowAssignInfo> assignInfoList = this.getAssignList(notFoundHandler, flowVars, jbpmFlowInfo);
        if (notFoundHandler || assignInfoList.size() == 0) {
            result.put("OPER_SUCCESS", false);
            result.put("OPER_MSG", "未找到办理人员!");
            return result;
        } else {
            String jbpmAssignJson = JSON.toJSONString(assignInfoList);
            jbpmFlowInfo.setJbpmAssignJson(jbpmAssignJson);
            if (StringUtils.isNotEmpty(jbpmFlowInfo.getJbpmMainTableRecordId())) {
                flowVars.put(mainTablePKName, jbpmFlowInfo.getJbpmMainTableRecordId());
            }
            return this.doFlowJob(flowVars, jbpmFlowInfo);
        }
    }

    /**
     * 获取下一步办理人列表
     *
     * @param notFoundHandler
     * @param flowVars
     * @param jbpmFlowInfo
     * @return
     */
    public List<FlowAssignInfo> getAssignList(boolean notFoundHandler,
                                              Map<String, Object> flowVars, JbpmFlowInfo jbpmFlowInfo) {
        List<FlowNextStep> nextStepList = null;
        if (StringUtils.isNotEmpty(jbpmFlowInfo.getJbpmHandleTaskStatus())) {
            int taskStatus = Integer.parseInt(jbpmFlowInfo.getJbpmHandleTaskStatus());
            if (taskStatus == JbpmTaskService.TASKSTATUS_BACK) {
                nextStepList = jbpmTaskService.findReturnStepList(jbpmFlowInfo);
            }
        } else {
            nextStepList = this.findNextStepList(flowVars, jbpmFlowInfo);
        }
        List<FlowAssignInfo> assignInfoList = new ArrayList<FlowAssignInfo>();
        for (FlowNextStep nextStep : nextStepList) {
            FlowAssignInfo assignInfo = new FlowAssignInfo();
            assignInfo.setNextNodeKey(nextStep.getNodeKeys());
            assignInfo.setNextNodeName(nextStep.getNodeNames());
            if (nextStep.getNodeAssignerList() != null && nextStep.getNodeAssignerList().size() > 0) {
                assignInfo.setNextAssignerIds(nextStep.getNodeAssignerList().get(0).getAssignerIds());
                assignInfo.setNextIsOrderTask(nextStep.getNodeAssignerList().get(0).getIsOrder());
                assignInfo.setNextAssignerNature(nextStep.getNodeAssignerList().get(0).getHandlerNature());
                assignInfoList.add(assignInfo);
            } else {
                notFoundHandler = true;
                break;
            }
        }
        return assignInfoList;
    }

    /**
     * 更新剩余天数数据
     */
    @Override
    public void updateTimeLimit() {
        //更新任务的剩余天数
        this.jbpmTaskService.updateLeftDays();
        //更新挂起记录的相关天数
        handupRecordService.updateDays();
        //更新流程实例的相关天数
        this.executionService.updateLeftDays();
    }

    /**
     * 保存子流程
     *
     * @param subFlowDef
     * @param parentExeId
     * @param subExeId
     * @return
     */
    private Map<String, Object> saveSubExe(Map<String, Object> subFlowDef, String parentExeId, String subExeId) {
        String FLOWDEF_NAME = (String) subFlowDef.get("FLOWDEF_NAME");
        String FLOWDEF_VERSION = subFlowDef.get("FLOWDEF_VERSION").toString();
        String subFlowDefId = (String) subFlowDef.get("FLOWDEF_ID");
        Map<String, Object> sysUser = PlatAppUtil.getBackPlatLoginUser();
        String SYSUSER_ID = (String) sysUser.get("SYSUSER_ID");
        String SYSUSER_NAME = (String) sysUser.get("SYSUSER_NAME");
        Map<String, Object> subExecution = new HashMap<String, Object>();
        subExecution.put("FLOWDEF_ID", subFlowDefId);
        subExecution.put("EXECUTION_SUBJECT", FLOWDEF_NAME + "【发起人:" + SYSUSER_NAME + "】");
        subExecution.put("EXECUTION_VERSION", FLOWDEF_VERSION);
        subExecution.put("STATUS", ExecutionService.STATUS_RUNNING);
        subExecution.put("CREATOR_ID", SYSUSER_ID);
        subExecution.put("CREATOR_NAME", SYSUSER_NAME);
        subExecution.put("PARENT_EXECUTION_ID", parentExeId);
        subExecution.put("EXECUTION_CREATETIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        if (subFlowDef.get("TOTALLIMIT_DAYS") != null) {
            String TOTALLIMIT_DAYS = subFlowDef.get("TOTALLIMIT_DAYS").toString();
            String TOTALLIMIT_TYPES = subFlowDef.get("TOTALLIMIT_TYPES").toString();
            subExecution.put("EXECUTION_LEFTDAYS", TOTALLIMIT_DAYS);
            subExecution.put("LIMITTYPE", TOTALLIMIT_TYPES);
        }
        subExecution.put("EXECUTION_ID", subExeId);
        subExecution = dao.saveOrUpdate("JBPM6_EXECUTION", subExecution, SysConstants.ID_GENERATOR_ASSIGNED, null);
        return subExecution;
    }

    /**
     * 启动子流程
     *
     * @param subFlowDefId
     * @param jbpmFlowInfo
     * @param assignInfo
     * @param nextNodeAssigner
     */
    @Override
    public void startSubFlow(String subFlowDefId, JbpmFlowInfo jbpmFlowInfo, FlowAssignInfo assignInfo,
                             NodeAssigner nextNodeAssigner) {

        Map<String, Object> subFlowDef = this.flowDefService.getRecord("JBPM6_FLOWDEF",
                new String[]{"FLOWDEF_ID"}, new Object[]{subFlowDefId});
        String FLOWDEF_VERSION = subFlowDef.get("FLOWDEF_VERSION").toString();
        String subExeId = executionService.getSubProcessExeId(jbpmFlowInfo.getJbpmExeId());
        //保存子流程实例数据
        this.saveSubExe(subFlowDef, jbpmFlowInfo.getJbpmExeId(), subExeId);
        //保存子流程的开始节点任务
        saveSubFlowTask(subFlowDefId, jbpmFlowInfo, assignInfo,
                nextNodeAssigner, subFlowDef, FLOWDEF_VERSION, subExeId);
        //更新子流程的实例信息
        JbpmFlowInfo subJbpmFlowInfo = new JbpmFlowInfo();
        subJbpmFlowInfo.setJbpmExeId(subExeId);
        subJbpmFlowInfo.setJbpmDefId(subFlowDefId);
        this.executionService.updateFlowExe(new HashMap<String, Object>(), subJbpmFlowInfo);
    }

    /**
     * 保存子流程任务
     *
     * @param subFlowDefId
     * @param jbpmFlowInfo
     * @param assignInfo
     * @param nextNodeAssigner
     * @param subFlowDef
     * @param FLOWDEF_VERSION
     * @param subExeId
     */
    private void saveSubFlowTask(String subFlowDefId,
                                 JbpmFlowInfo jbpmFlowInfo, FlowAssignInfo assignInfo,
                                 NodeAssigner nextNodeAssigner, Map<String, Object> subFlowDef,
                                 String FLOWDEF_VERSION, String subExeId) {
        //获取子流程的开始节点
        FlowNode startNode = this.flowNodeService.getStartFlowNode(subFlowDef);
        Map<String, Object> startNodeBind = this.nodeBindService.getNodeBind(subFlowDefId,
                FLOWDEF_VERSION, startNode.getNodeKey(), NodeBindService.BINDTYPE_NODECONIG);
        //获取任务性质
        String nextAuditType = startNodeBind.get("NODEBIND_TASKNATURE").toString();
        //获取节点KEY
        String nextNodeKey = startNode.getNodeKey();
        if (nextNodeAssigner != null) {
            nextAuditType = nextNodeAssigner.getNextAuditType();
        } else {
            nextAuditType = nodeBindService.getTaskNature(jbpmFlowInfo.getJbpmDefId(),
                    jbpmFlowInfo.getJbpmDefVersion(), nextNodeKey);
        }
        //获取是否是串审
        String isTaskOrder = assignInfo.getNextIsOrderTask();
        String TASK_ORDERPARENTID = UUIDGenerator.getUUID();
        int sendMaxCount = 1;
        if (!nextAuditType.equals(FlowNextStep.AUDITTYPE_SINGLE)) {
            sendMaxCount = assignInfo.getNextAssignerIds().split(",").length;
        }
        //创建子流程的第一个任务
        for (int i = 0; i < sendMaxCount; i++) {
            //开始分发单人任务
            Map<String, Object> flowTask = new HashMap<String, Object>();
            flowTask.put("TASK_CREATETIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            flowTask.put("TASK_EXEID", subExeId);
            flowTask.put("TASK_NODEKEY", startNode.getNodeKey());
            flowTask.put("TASK_NODENAME", startNode.getNodeName());
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
            }
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
            dao.saveOrUpdate("JBPM6_TASK",
                    flowTask, SysConstants.ID_GENERATOR_UUID, null);
        }
    }

    /**
     * 根据实例ID和节点KEY获取任务数据
     */
    @Override
    public List<Map<String, Object>> findTaskList(String jbpmExeId,
                                                  String nodeKey) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT T.TASK_OPINION FROM JBPM6_TASK T WHERE ");
        sql.append(" T.TASK_EXEID=? AND T.TASK_NODEKEY=?  AND T.TASK_OPINION IS NOT NULL ");
        sql.append(" ORDER BY T.TASK_CREATETIME DESC ");
        return dao.findBySql(sql.toString(), new Object[]{jbpmExeId, nodeKey}, null);
    }

    /**
     * 接口式往下提交流程
     *
     * @param flowVars
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public Map<String, Object> doNextStep(Map<String, Object> flowVars, JbpmFlowInfo jbpmFlowInfo) {
        //获取流程实例ID
        String jbpmExeId = jbpmFlowInfo.getJbpmExeId();
        Map<String, Object> flowExe = executionService.getRecord("JBPM6_EXECUTION",
                new String[]{"EXECUTION_ID"}, new Object[]{jbpmExeId});
        String flowDefId = (String) flowExe.get("FLOWDEF_ID");
        JbpmFlowInfo flowInfo = this.getJbpmFlowInfo(jbpmExeId,
                flowDefId, "false", jbpmFlowInfo.getJbpmOperingTaskId());
        PlatBeanUtil.mergeObject(flowInfo, jbpmFlowInfo);
        Map<String, Object> result = null;
        if (StringUtils.isEmpty(jbpmFlowInfo.getJbpmAssignJson())) {
            boolean notFoundHandler = false;
            result = new HashMap<String, Object>();
            List<FlowAssignInfo> assignInfoList = this.getAssignList(notFoundHandler, flowVars, jbpmFlowInfo);
            if (notFoundHandler || assignInfoList.size() == 0) {
                result.put("OPER_SUCCESS", false);
                result.put("OPER_MSG", "未找到办理人员!");
                return result;
            } else {
                String jbpmAssignJson = JSON.toJSONString(assignInfoList);
                jbpmFlowInfo.setJbpmAssignJson(jbpmAssignJson);
            }
        }
        if (StringUtils.isNotEmpty(jbpmFlowInfo.getJbpmAssignJson())) {
            if (StringUtils.isNotEmpty(jbpmFlowInfo.getJbpmMainTableRecordId())) {
                flowVars.put(jbpmFlowInfo.getJbpmMainTablePkName(), jbpmFlowInfo.getJbpmMainTableRecordId());
            }
            result = this.executionService.exeFlowResult(flowVars, jbpmFlowInfo);
        } else {
            result = new HashMap<String, Object>();
            result.put("OPER_SUCCESS", false);
            result.put("OPER_MSG", "未找到办理人员!");
        }
        return result;
    }

    /**
     * 批量流程动作
     *
     * @param postParams
     */
    @Override
    public void doBatchFlow(Map<String, Object> postParams) {
        String jbpmDefCode = (String) postParams.get("jbpmDefCode");
        String flowHandleJson = (String) postParams.get("flowHandleJson");
        Map<String, Object> backUser = PlatAppUtil.getBackPlatLoginUser();
        List<Map> list = JSON.parseArray(flowHandleJson, Map.class);
        for (Map flowHandle : list) {
            String flowExeId = (String) flowHandle.get("flowExeId");
            String flowTaskId = (String) flowHandle.get("flowTaskId");
            String flowMainRecordId = (String) flowHandle.get("flowMainRecordId");
            String flowHandleOpinion = (String) flowHandle.get("flowHandleOpinion");
            Map<String, Object> flowVars = new HashMap<String, Object>();
            if (StringUtils.isNotEmpty(flowExeId) && StringUtils.isNotEmpty(flowTaskId)) {
                JbpmFlowInfo jbpmFlowInfo = new JbpmFlowInfo();
                jbpmFlowInfo.setJbpmExeId(flowExeId);
                jbpmFlowInfo.setJbpmOperingTaskId(flowTaskId);
                jbpmFlowInfo.setJbpmHandleOpinion(flowHandleOpinion);
                jbpmFlowInfo.setJbpmIsTempSave("false");
                jbpmFlowInfo.setJbpmHandlerId((String) backUser.get("SYSUSER_ID"));
                this.doNextStep(flowVars, jbpmFlowInfo);
            } else {
                flowVars.put("jbpmDefCode", jbpmDefCode);
                flowVars.put("jbpmOperingHandlerType", "1");
                flowVars.put("jbpmCreatorId", backUser.get("SYSUSER_ID"));
                flowVars.put("jbpmMainTableRecordId", flowMainRecordId);
                flowVars.put("jbpmHandleOpinion", flowHandleOpinion);
                this.startFlow(flowVars);
            }
        }
    }

}
