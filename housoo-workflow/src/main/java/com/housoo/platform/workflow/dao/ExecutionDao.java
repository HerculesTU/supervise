/*
 * Copyright (c) 2005, 2014, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.dao;

import com.housoo.platform.core.dao.BaseDao;

import java.util.List;

/**
 * 描述 流程实例业务相关dao
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-04 14:34:24
 */
public interface ExecutionDao extends BaseDao {

    /**
     * 根据流程定义ID和流程版本号判断是否存在记录
     *
     * @param defId
     * @param flowVersion
     * @return
     */
    public boolean isExistedRunning(String defId, int flowVersion);

    /**
     * 获取子流程的数量值
     *
     * @param exeId
     * @return
     */
    public int getSubProcessCount(String exeId);

    /**
     * 获取子流程实例ID数组
     *
     * @param parentId
     * @return
     */
    public List<String> findSubExeIdArray(String parentId);
}
