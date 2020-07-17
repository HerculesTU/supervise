/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.service;

import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述 督查督办业务相关service
 *
 * @author zxl
 * @version 1.0
 * @created 2020-04-14 15:30:11
 */
public interface SuperviseService extends BaseService {
    /**
     * 验证督查督办信息
     *
     * @param departId 部门ID
     * @return
     */
    boolean verifySuperviseInfo(String departId);

    /**
     * 存储督查督办信息
     *
     * @param supervise
     */
    Map<String, Object> saveSuperviseInfo(Map<String, Object> supervise);

    /**
     * 督查督办信息存储为草稿信息
     *
     * @param superviseMap
     * @return
     */
    Map<String, Object> saveDraftInfo(Map<String, Object> superviseMap);

    /**
     * 根据督办事项ID初始化督办事项的督办流程
     *
     * @param request
     * @return
     */
    List<Map<String, Object>> initSuperviseProgress(HttpServletRequest request);

    /**
     * 获取督办任务逾期情况
     *
     * @param userId  用户Id
     * @param clazzId 督办类型Id
     * @param status  正在督办 2  待发督办 3  完后督办 4 全部督办 1
     * @return
     */
    List<List<Map<String, Object>>> getSuperviseInfo(String userId, String clazzId, String status);

    /**
     * 根据主键ID 和 nodeId获取当前 节点是否逾期
     *
     * @param superviseId 主键ID
     * @param nodeId      节点ID
     * @return
     */
    Map<String, Object> getOutTimeInfoBySuperviseIdAndNodeId(String superviseId, String nodeId);

}
