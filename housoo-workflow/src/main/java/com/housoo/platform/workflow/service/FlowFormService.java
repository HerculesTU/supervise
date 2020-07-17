/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service;

import com.housoo.platform.core.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 描述 流程表单业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-05 10:54:04
 */
public interface FlowFormService extends BaseService {
    /**
     * 获取可选设计表单列表
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> findForSelect(String param);

    /**
     * 根据表单ID获取可见的控件配置信息
     *
     * @param formId
     * @return
     */
    public List<Map<String, Object>> findAuthAbleControl(String formId);

    /**
     * 保存流程表单并且级联保存表单字段
     *
     * @param flowForm
     * @return
     */
    public Map<String, Object> saveFormCascadeFields(Map<String, Object> flowForm);

    /**
     * 获取当前环节绑定的表单信息
     *
     * @param nodeBindList
     * @return
     */
    public Map<String, Object> getCurrentNodeBindForm(List<Map<String, Object>> nodeBindList);
}
