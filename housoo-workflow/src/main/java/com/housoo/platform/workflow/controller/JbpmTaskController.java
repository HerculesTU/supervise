/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.controller;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.workflow.service.ExecutionService;
import com.housoo.platform.workflow.service.JbpmTaskService;
import com.housoo.platform.workflow.service.NodeBindService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 流程任务业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-16 16:36:01
 */
@Controller
@RequestMapping("/workflow/JbpmTaskController")
public class JbpmTaskController extends BaseController {
    /**
     *
     */
    @Resource
    private JbpmTaskService jbpmTaskService;
    /**
     *
     */
    @Resource
    private ExecutionService executionService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;
    /**
     *
     */
    @Resource
    private NodeBindService nodeBindService;

    /**
     * 删除流程任务数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        jbpmTaskService.deleteTaskCacadeExe(selectColValues.split(","));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到流程任务表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String TASK_ID = request.getParameter("TASK_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> jbpmTask = null;
        if (StringUtils.isNotEmpty(TASK_ID)) {
            jbpmTask = this.jbpmTaskService.getRecord("JBPM6_TASK"
                    , new String[]{"TASK_ID"}, new Object[]{TASK_ID});
        } else {
            jbpmTask = new HashMap<String, Object>();
        }
        request.setAttribute("jbpmTask", jbpmTask);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 跳转到流程任务表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goMonitorList")
    public ModelAndView goMonitorList(HttpServletRequest request) {
        String EXECUTION_ID = request.getParameter("EXECUTION_ID");
        request.setAttribute("EXECUTION_ID", EXECUTION_ID);
        return PlatUICompUtil.goDesignUI("monitortasklist", request);
    }

    /**
     * 转发流程任务
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "forward")
    public void forward(HttpServletRequest request,
                        HttpServletResponse response) {
        String TASK_ID = request.getParameter("TASK_ID");
        String checkUserIds = request.getParameter("checkUserIds");
        jbpmTaskService.forwardTask(TASK_ID, checkUserIds);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到环节跳转界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goJumpList")
    public ModelAndView goJumpList(HttpServletRequest request) {
        String EXECUTION_ID = request.getParameter("EXECUTION_ID");
        List<Map<String, Object>> taskList = jbpmTaskService.findRunningTaskList(EXECUTION_ID);
        Map<String, Object> execution = jbpmTaskService.getRecord("JBPM6_EXECUTION",
                new String[]{"EXECUTION_ID"}, new Object[]{EXECUTION_ID});
        request.setAttribute("FLOWDEF_ID", execution.get("FLOWDEF_ID"));
        request.setAttribute("FLOWDEF_VERSION", execution.get("EXECUTION_VERSION"));
        request.setAttribute("EXECUTION_ID", EXECUTION_ID);
        request.setAttribute("taskList", taskList);
        return new ModelAndView("background/workflow/execution/jumpflow");
    }

    /**
     * 跳转流程任务
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "jump")
    public void jump(HttpServletRequest request,
                     HttpServletResponse response) {
        String EXECUTION_ID = request.getParameter("EXECUTION_ID");
        String jumpAssignJson = request.getParameter("jumpAssignJson");
        List<Map> jumpList = JSON.parseArray(jumpAssignJson, Map.class);
        jbpmTaskService.jumpTask(EXECUTION_ID, jumpList);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 撤回流程任务数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "revoke")
    public void revoke(HttpServletRequest request,
                       HttpServletResponse response) {
        String EXECUTION_ID = request.getParameter("selectColValues");
        String userId = (String) ((Map<String, Object>) PlatAppUtil.getBackPlatLoginUser()).get("SYSUSER_ID");
        Map<String, Object> auditedTask = jbpmTaskService.getLatestAuditedTask(EXECUTION_ID, userId);
        Map<String, Object> result = new HashMap<String, Object>();
        if (auditedTask != null) {
            String TASK_ID = (String) auditedTask.get("TASK_ID");
            String revokeMsg = jbpmTaskService.getRevokeMsg(TASK_ID);
            if (StringUtils.isEmpty(revokeMsg)) {
                jbpmTaskService.revokeJbpmTask(TASK_ID, EXECUTION_ID);
                result.put("success", true);
            } else {
                result.put("success", false);
                result.put("msg", revokeMsg);
            }
        } else {
            result.put("success", false);
            result.put("msg", "未找到符合撤回的任务,撤回失败!");
        }
        this.printObjectJsonString(result, response);
    }

    /**
     * 获取最近的任务ID
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "getLatestTaskId")
    public void getLatestTaskId(HttpServletRequest request,
                                HttpServletResponse response) {
        String jbpmExeId = request.getParameter("jbpmExeId");
        String nodeKey = request.getParameter("nodeKey");
        String taskId = jbpmTaskService.getLatestTaskId(jbpmExeId, nodeKey, 2);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        result.put("taskId", taskId);
        this.printObjectJsonString(result, response);
    }


    /**
     * 获取子流程的任务信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "getSubFlowTaskInfo")
    public void getSubFlowTaskInfo(HttpServletRequest request,
                                   HttpServletResponse response) {
        String parentExeId = request.getParameter("jbpmExeId");
        String nodeKey = request.getParameter("nodeKey");
        //获取父亲流程的信息
        Map<String, Object> parentExe = executionService.getRecord("JBPM6_EXECUTION",
                new String[]{"EXECUTION_ID"}, new Object[]{parentExeId});
        //获取父亲流程的定义ID
        String FLOWDEF_ID = (String) parentExe.get("FLOWDEF_ID");
        //获取父亲流程的版本号
        String EXECUTION_VERSION = parentExe.get("EXECUTION_VERSION").toString();
        //获取绑定的节点
        Map<String, Object> nodeBind = nodeBindService.getNodeBind(FLOWDEF_ID, EXECUTION_VERSION,
                nodeKey, NodeBindService.BINDTYPE_NODECONIG);
        //获取绑定的子流程ID
        String subDefId = (String) nodeBind.get("NODEBIND_SUBDEFID");
        //获取子流程实例信息
        Map<String, Object> subFlowExe = executionService.getRecord("JBPM6_EXECUTION",
                new String[]{"FLOWDEF_ID", "PARENT_EXECUTION_ID"}, new Object[]{subDefId, parentExeId});
        String subFlowExeId = (String) subFlowExe.get("EXECUTION_ID");
        //获取正在运行的节点
        String CURRENT_NODEKEYS = (String) subFlowExe.get("CURRENT_NODEKEYS");
        String subFlowNodeKey = null;
        if (StringUtils.isNotEmpty(CURRENT_NODEKEYS)) {
            subFlowNodeKey = CURRENT_NODEKEYS.split(",")[0];
        } else {
            subFlowNodeKey = jbpmTaskService.getLastTaskNodeKey(subFlowExeId);
        }
        String taskId = jbpmTaskService.getLatestTaskId(subFlowExeId, subFlowNodeKey);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        result.put("taskId", taskId);
        result.put("subFlowExeId", subFlowExeId);
        this.printObjectJsonString(result, response);
    }
}
