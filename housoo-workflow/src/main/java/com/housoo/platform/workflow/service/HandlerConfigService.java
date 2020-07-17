/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service;

import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.core.service.BaseService;
import com.housoo.platform.workflow.model.FlowNextHandler;
import com.housoo.platform.workflow.model.NodeAssigner;

import java.util.List;
import java.util.Map;

/**
 * 描述 办理人业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-10 15:50:11
 */
public interface HandlerConfigService extends BaseService {

    /**
     * 获取指定岗位审核人
     *
     * @param flowVars
     * @param nodeAssigner
     * @param jbpmFlowInfo
     * @return
     */
    public List<FlowNextHandler> getAssignPositionHandlers(Map<String, Object> flowVars,
                                                           NodeAssigner nodeAssigner, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 获取指定角色审核人
     *
     * @param flowVars
     * @param nodeAssigner
     * @param jbpmFlowInfo
     * @return
     */
    public List<FlowNextHandler> getAssignRoleHandlers(Map<String, Object> flowVars,
                                                       NodeAssigner nodeAssigner, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 获取发起人作为审核人
     *
     * @param flowVars
     * @param nodeAssigner
     * @param jbpmFlowInfo
     * @return
     */
    public List<FlowNextHandler> getStartHandlers(Map<String, Object> flowVars,
                                                  NodeAssigner nodeAssigner, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 获取下拉框数据源
     *
     * @param parmaJson
     * @return
     */
    public List<Map<String, Object>> findForSelect(String parmaJson);

    /**
     * 获取指定具体用户数据列表
     *
     * @param flowVars
     * @param nodeAssigner
     * @param jbpmFlowInfo
     * @return
     */
    public List<FlowNextHandler> getAssignHandlers(Map<String, Object> flowVars,
                                                   NodeAssigner nodeAssigner, JbpmFlowInfo jbpmFlowInfo);
}
