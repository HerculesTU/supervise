/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service;

import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.core.service.BaseService;

import java.util.Map;

/**
 * 描述 字段修改业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-11-02 09:40:47
 */
public interface FieldModifyService extends BaseService {

    /**
     * 保存字段修改日志
     *
     * @param postParams
     * @param jbpmFlowInfo
     */
    public void saveFieldModify(Map<String, Object> postParams,
                                JbpmFlowInfo jbpmFlowInfo);
}
