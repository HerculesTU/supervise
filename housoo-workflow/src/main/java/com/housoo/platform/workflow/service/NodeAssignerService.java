/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service;

import com.housoo.platform.core.service.BaseService;
import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.workflow.model.NodeAssigner;

import java.util.List;
import java.util.Map;

/**
 * 描述 下一环节办理人业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-10 17:05:56
 */
public interface NodeAssignerService extends BaseService {
    /**
     * 获取节点配置的办理人信息
     *
     * @param nextNodeKey
     * @param postParams
     * @param jbpmFlowInfo
     * @return
     */
    public NodeAssigner getNodeAssigner(String nextNodeKey, Map<String, Object> postParams, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 克隆节点办理人配置表
     *
     * @param oldFlowDefId
     * @param oldFlowDefVersion
     * @param newFlowDefId
     * @param newFlowDefVersion
     */
    public void copyNodeAssigner(String oldFlowDefId, int oldFlowDefVersion,
                                 String newFlowDefId, int newFlowDefVersion);

    /**
     * 获取定义ID和版本号列表
     *
     * @param defId
     * @param flowVersion
     * @return
     */
    public List<Map<String, Object>> findByDefIdAndVesion(String defId, int flowVersion);


}
