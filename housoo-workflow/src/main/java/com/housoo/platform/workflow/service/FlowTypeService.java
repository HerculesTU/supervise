/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service;

import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 描述 流程类别业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-03 16:12:37
 */
public interface FlowTypeService extends BaseService {
    /**
     * 获取流程类别组列表数据源
     *
     * @param queryParamJson
     * @return
     */
    public List<Map<String, Object>> findGroupList(String queryParamJson);

    /**
     * 删除流程类别级联更新流程定义数据
     *
     * @param typeId
     */
    public void deleteCascadeFlowDef(String typeId);

    /**
     * 根据查询参数获取数据列表
     *
     * @param queryvalue
     * @return
     */
    public List<Map<String, Object>> findForSelect(String queryvalue);

    /**
     * 获取流程类别和流程定义树形JSON
     *
     * @param params
     * @return
     */
    public String getTypeAndDefJson(Map<String, Object> params);

    /**
     * 获取自动补全的流程类别和定义数据
     *
     * @param filter
     * @return
     */
    public List<Map<String, Object>> findAutoTypeDef(SqlFilter filter);

    /**
     * 获取可选流程类别下拉框数据源
     *
     * @param paramJson
     * @return
     */
    public List<Map<String, Object>> findTypeSelect(String paramJson);

    /**
     * 根据当前登录用户ID获取授权的流程
     *
     * @param filter
     * @param fieldInfo
     * @return
     */
    List<Map<String,Object>> getGrantFlowByUserId(SqlFilter filter, Map<String, Object> fieldInfo);
}
