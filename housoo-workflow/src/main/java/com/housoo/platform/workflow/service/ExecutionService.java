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
 * 描述 流程实例业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-04 14:34:24
 */
public interface ExecutionService extends BaseService {
    /**
     * 状态:草稿
     */
    public static final String STATUS_DRAFT = "0";
    /**
     * 状态:办理中
     */
    public static final String STATUS_RUNNING = "1";
    /**
     * 状态:已办结(正常结束)
     */
    public static final String STATUS_NORMALOVER = "2";
    /**
     * 状态:已办结(审核通过)
     */
    public static final String STATUS_AGREEOVER = "3";
    /**
     * 状态:已办结(审核不通过)
     */
    public static final String STATUS_NOAGREEOVER = "4";
    /**
     * 状态:已办结(强制终止)
     */
    public static final String STATUS_TERMINAL = "5";

    /**
     * 根据流程定义ID和流程版本号判断是否存在记录
     *
     * @param defId
     * @param flowVersion
     * @return
     */
    public boolean isExistedRunning(String defId, int flowVersion);

    /**
     * 保存流程实例信息
     *
     * @param postParams
     * @param jbpmFlowInfo
     */
    public void saveFlowExe(Map<String, Object> postParams, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 删除流程实例数据并且级联删除子表
     *
     * @param exeIds
     */
    public void delCascadeSubTables(String[] exeIds);

    /**
     * 更新流程实例信息
     *
     * @param postParams
     * @param jbpmFlowInfo
     */
    public void updateFlowExe(Map<String, Object> postParams, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 强制终结流程实例数据
     *
     * @param exeIds
     */
    public void endExecution(String[] exeIds);

    /**
     * 更新剩余天数
     */
    public void updateLeftDays();

    /**
     * 获取子流程的实例ID
     *
     * @param parentExeId
     * @return
     */
    public String getSubProcessExeId(String parentExeId);

    /**
     * 获取实例ID
     *
     * @return
     */
    public String getNextExeId();

    /**
     * 获取往下执行流程的结果
     *
     * @param flowVars
     * @param jbpmFlowInfo
     * @return
     */
    public Map<String, Object> exeFlowResult(Map<String, Object> flowVars, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 更新流程实例版本为最新版本
     *
     * @param exeIds
     */
    public void updateExeVersionToNewest(String[] exeIds);

    /**
     * 根据流程实例ID删除流程实例
     *
     * @param exeId
     */
    public void deleteExeById(String exeId);

    /**
     * 根据流程定义获取实例数量
     *
     * @param defIds
     * @return
     */
    public int getCountByDefIds(String defIds);
}
