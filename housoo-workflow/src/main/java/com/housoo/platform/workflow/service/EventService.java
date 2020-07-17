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
 * 描述 流程事件业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-08 09:43:22
 */
public interface EventService extends BaseService {
    /**
     * 事件类型:存储
     */
    public static String EVENTTYPE_SAVE = "0";
    /**
     * 事件类型:前置
     */
    public static String EVENTTYPE_PRE = "1";
    /**
     * 事件类型:后置
     */
    public static String EVENTTYPE_AFTER = "2";
    /**
     * 事件类型:决策
     */
    public static String EVENTTYPE_DESIDE = "3";

    /**
     * 根据过滤器获取事件配置的接口列表
     *
     * @param sqlFilter
     * @return
     */
    public List<Map> findByEventInter(SqlFilter sqlFilter);

    /**
     * 保存事件并且级联保存环节配置
     *
     * @param event
     * @return
     */
    public Map<String, Object> saveEventCascade(Map<String, Object> event);

    /**
     * 获取配置的下一排序值
     *
     * @param EVENT_FLOWDEFID
     * @param EVENT_FLOWVERSION
     * @return
     */
    public int getNextSn(String EVENT_FLOWDEFID, String EVENT_FLOWVERSION);

    /**
     * 获取绑定事件的代码
     *
     * @param defId
     * @param defVersion
     * @param nodeKey
     * @param eventType
     * @return
     */
    public List<String> findEventCodes(String defId, String defVersion, String nodeKey, String eventType);

    /**
     * 克隆事件数据
     *
     * @param oldFlowDefId
     * @param oldFlowDefVersion
     * @param newFlowDefId
     * @param newFlowDefVersion
     */
    public void copyEvents(String oldFlowDefId, int oldFlowDefVersion,
                           String newFlowDefId, int newFlowDefVersion);

    /**
     * 获取列表
     *
     * @param defId
     * @param flowVersion
     * @return
     */
    public List<Map<String, Object>> findByDefIdAndVersion(String defId, int flowVersion);
}
