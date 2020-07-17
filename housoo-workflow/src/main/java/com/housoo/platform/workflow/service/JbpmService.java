/*
 * Copyright (c) 2005, 2017, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service;

import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.core.service.BaseService;
import com.housoo.platform.workflow.model.FlowAssignInfo;
import com.housoo.platform.workflow.model.FlowNextStep;
import com.housoo.platform.workflow.model.NodeAssigner;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author 胡裕
 * @created 2017年5月14日 下午4:56:47
 */
public interface JbpmService extends BaseService {
    /**
     * 处理流程工作
     *
     * @param postParams
     * @param jbpmFlowInfo
     * @return
     */
    public Map<String, Object> doFlowJob(Map<String, Object> postParams, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 通用保存单表业务数据接口
     *
     * @param postParams
     * @param jbpmFlowInfo
     */
    public void genSaveBusData(Map<String, Object> postParams, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 获取流程对象
     *
     * @param jbpmExeId
     * @param flowDefId
     * @param jbpmIsQuery
     * @param jbpmOperingTaskId
     * @return
     */
    public JbpmFlowInfo getJbpmFlowInfo(String jbpmExeId, String flowDefId, String jbpmIsQuery, String jbpmOperingTaskId);

    /**
     * 通用设置表单业务数据
     *
     * @param request
     * @param jbpmFlowInfo
     */
    public void genSetBusData(HttpServletRequest request, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 跳转到流程设计UI界面
     * @param request
     * @param jbpmFlowInfo
     * @return
     */
    public ModelAndView getFlowDesignUI(HttpServletRequest request, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 获取下一环节列表数据
     *
     * @param flowVars
     * @param jbpmFlowInfo
     * @return
     */
    public List<FlowNextStep> findNextStepList(Map<String, Object> flowVars, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 获取下一环节分支节点数据
     *
     * @param stepToken
     * @return
     */
    public List<Map<String, Object>> getNextStepBranch(String stepToken);

    /**
     * 接口式启动流程
     * 必须设置参数
     * jbpmDefCode:流程定义编码
     * jbpmOperingHandlerType:发起人类型(1后台用户 2网站用户)
     * 其它的参数为业务字段参数
     *
     * @param flowVars
     * @return
     */
    public Map<String, Object> startFlow(Map<String, Object> flowVars);

    /**
     * 更新剩余天数数据
     */
    public void updateTimeLimit();

    /**
     * 根据实例ID和节点KEY获取任务数据
     *
     * @param jbpmExeId
     * @param nodeKey
     * @return
     */
    public List<Map<String, Object>> findTaskList(String jbpmExeId,
                                                  String nodeKey);

    /**
     * 启动子流程
     *
     * @param subFlowDefId
     * @param jbpmFlowInfo
     * @param assignInfo
     * @param nextNodeAssigner
     */
    public void startSubFlow(String subFlowDefId, JbpmFlowInfo jbpmFlowInfo, FlowAssignInfo assignInfo,
                             NodeAssigner nextNodeAssigner);

    /**
     * 接口式往下提交流程
     *
     * @param flowVars
     * @param jbpmFlowInfo
     * @return
     */
    public Map<String, Object> doNextStep(Map<String, Object> flowVars, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 批量流程动作
     *
     * @param flowVars
     */
    public void doBatchFlow(Map<String, Object> flowVars);
}
