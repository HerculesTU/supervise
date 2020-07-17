/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service;

import com.housoo.platform.core.service.BaseService;

import java.util.Map;

/**
 * 描述 流程历史部署业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-04 14:34:24
 */
public interface HistDeployService extends BaseService {
    /**
     * 保存历史部署信息
     *
     * @param oldFlowDef
     */
    public void saveHistDeploy(Map<String, Object> oldFlowDef);
}
