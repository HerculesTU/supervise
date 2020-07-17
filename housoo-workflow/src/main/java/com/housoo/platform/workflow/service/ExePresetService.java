/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service;

import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.BaseService;
import com.housoo.platform.workflow.model.FlowNextHandler;

import java.util.List;
import java.util.Map;

/**
 * 描述 实例预设办理人业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-12-26 09:04:38
 */
public interface ExePresetService extends BaseService {
    /**
     * 获取预设人员列表
     *
     * @param sqlFilter
     * @return
     */
    public List<Map> findPresetList(SqlFilter sqlFilter);

    /**
     * 获取流程预设办理人列表
     *
     * @param exeId
     * @param nodeKey
     * @return
     */
    public List<FlowNextHandler> findPresetHandler(String exeId, String nodeKey);
}
