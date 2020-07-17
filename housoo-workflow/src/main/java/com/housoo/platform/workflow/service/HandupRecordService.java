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
 * 描述 挂起任务业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-06-01 09:29:36
 */
public interface HandupRecordService extends BaseService {
    /**
     * 根据实例ID删除数据
     *
     * @param exeId
     */
    public void deleteByExeId(String exeId);

    /**
     * 保存挂起记录信息
     *
     * @param jbpmFlowInfo
     */
    public void saveHandUpRecord(JbpmFlowInfo jbpmFlowInfo);

    /**
     * 重启流程任务
     *
     * @param recordId
     * @return
     */
    public Map<String, Object> restartJbpmTask(String recordId);

    /**
     * 重启流程任务
     *
     * @param taskIds
     * @return
     */
    public void restartJbpmTasks(String taskIds);

    /**
     * 判断是否存在记录
     *
     * @param exeId
     * @param sysUserId
     * @param nodeKey
     * @return
     */
    public boolean isExists(String exeId, String sysUserId, String nodeKey);

    /**
     * 更新天数
     */
    public void updateDays();

    /**
     * 获取挂起天数的数量
     *
     * @param exeId
     * @param limitType
     * @return
     */
    public int getHandUpCount(String exeId, String limitType);

    /**
     * 获取挂起的天数数量
     *
     * @param exeId
     * @param limitType
     * @param nodeKeys
     * @return
     */
    public int getHandUpDays(String exeId, String limitType, String nodeKeys);
}
