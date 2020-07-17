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
 * 描述 流程列表绑定业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-12 09:53:34
 */
public interface TableBindService extends BaseService {
    /**
     * 根据流程定义和表格类型获取绑定的设计界面代码
     *
     * @param defId
     * @param tableType
     * @return
     */
    public String getTableBindDesignCode(String defId, String tableType);

    /**
     * 根据流程定义和版本获取列表数据
     *
     * @param defId
     * @param defVersion
     * @return
     */
    public List<Map<String, Object>> findByDefIdAndVersion(String defId, int defVersion);
}
