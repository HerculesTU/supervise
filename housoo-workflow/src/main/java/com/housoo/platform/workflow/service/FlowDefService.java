/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service;

import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 描述 流程定义业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-03 16:12:37
 */
public interface FlowDefService extends BaseService {
    /**
     * 保存流程定义信息,并且级联保存其它表配置数据
     *
     * @param flowDef
     * @return
     */
    public Map<String, Object> saveOrUpdateCascade(Map<String, Object> flowDef);

    /**
     * 根据流程类别ID获取流程定义列表
     *
     * @param typeId
     * @return
     */
    public List<Map<String, Object>> findByTypeId(String typeId);

    /**
     * 根据流程定义和版本号获取流程定义对象
     *
     * @param defId
     * @param flowVersion
     * @return
     */
    public Map<String, Object> getFlowDefInfo(String defId, int flowVersion);

    /**
     * 更新流程定义的时限字段
     *
     * @param FLOWDEF_ID
     * @param TOTALLIMIT_DAYS
     * @param TOTALLIMIT_TYPES
     */
    public void updateTimeLimit(String FLOWDEF_ID, String TOTALLIMIT_DAYS, String TOTALLIMIT_TYPES);

    /**
     * 获取监控流程图时定义JSON
     *
     * @param flowDefJson
     * @param jbpmFlowInfo
     * @return
     */
    public String getMonitorFlowDefJson(String flowDefJson, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 获取可选流程定义下拉框数据源
     *
     * @param paramJson
     * @return
     */
    public List<Map<String, Object>> findTypeSelect(String paramJson);

    /**
     * 级联删除流程定义相关数据
     *
     * @param flowDefIds
     */
    public void deleteCascade(String[] flowDefIds);

    /**
     * 克隆流程定义信息
     *
     * @param targetDefId
     * @param newDefCode
     * @param newDefName
     */
    public void cloneFlowDef(String targetDefId, String newDefCode, String newDefName);

    /**
     * 根据filter获取网格项目
     *
     * @param sqlFilter
     * @return
     */
    public List<Map<String, Object>> findGridItemList(SqlFilter sqlFilter);

    /**
     * 获取导出的流程配置信息
     *
     * @param defIds
     * @return
     */
    public Map<String, Object> getExportDefInfo(String defIds);

    /**
     * 根据流程定义JSON导入配置信息
     *
     * @param defConfJson
     */
    public void importDefConfig(String defConfJson);
}
