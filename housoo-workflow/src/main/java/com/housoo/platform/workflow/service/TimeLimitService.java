/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service;

import com.housoo.platform.core.service.BaseService;
import com.housoo.platform.core.model.JbpmFlowInfo;

import java.util.List;
import java.util.Map;

/**
 * 描述 环节时限业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-10 10:06:52
 */
public interface TimeLimitService extends BaseService {
    /**
     * 时限类型:工作日
     */
    public static final String LIMITTYPE_WORKDAY = "1";
    /**
     * 时限类型:自然日
     */
    public static final String LIMITTYPE_NATURAL = "2";

    /**
     * 获取任务时限数据
     *
     * @param jbpmFlowInfo
     * @param nextNodeKey
     * @return 返回字段TASK_LEFTDAYS剩余天数 TASK_LIMITTYPE天数类型
     */
    public Map<String, Object> getTaskTimeLimit(JbpmFlowInfo jbpmFlowInfo, String nextNodeKey);

    /**
     * 获取实例时限数据
     *
     * @param jbpmFlowInfo
     * @return 返回字段EXECUTION_LEFTDAYS剩余天数 LIMITTYPE天数类型
     */
    public Map<String, Object> getExeTimeLimit(JbpmFlowInfo jbpmFlowInfo);

    /**
     * 根据流程定义
     *
     * @param defId
     * @param nodeKey
     * @return
     */
    public Map<String, Object> getTimeLimit(String defId, String nodeKey);

    /**
     * 根据流程定义获取列表数据
     *
     * @param defId
     * @return
     */
    public List<Map<String, Object>> findByDefId(String defId);
}
