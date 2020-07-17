/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service;

import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.core.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述 表单字段业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-08 15:00:57
 */
public interface FormFieldService extends BaseService {
    /**
     * 批量保存表单的字段信息
     *
     * @param formId
     */
    public void saveBatch(String formId);

    /**
     * 更新表单ID获取字段列表
     *
     * @param formId
     * @return
     */
    public List<Map<String, Object>> findByFormId(String formId);

    /**
     * 设置流程表单字段权限
     *
     * @param request
     * @param jbpmFlowInfo
     */
    public void setFlowFormFieldAuth(HttpServletRequest request, JbpmFlowInfo jbpmFlowInfo);
}
