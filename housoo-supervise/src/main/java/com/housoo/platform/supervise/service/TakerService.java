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
 * 描述 承办人业务相关service
 *
 * @author cjr
 * @version 1.0
 * @created 2020-04-14 15:30:11
 */
public interface TakerService extends BaseService {
    /**
     * 获取承办人承办任务列表
     *
     * @param request
     * @return
     */
    List<Map<String, Object>> findTakerSuperviseList(HttpServletRequest request);

    /**
     * 获取承办人首页数值型统计数据
     *
     * @param request
     * @return
     */
    Map<String, Object> getTakerIndexNumericData(HttpServletRequest request);

    /**
     * 20200511 新增
     * 获取承办人首页饼图统计数据
     *
     * @param request
     * @return
     */
    List<Map<String, Object>> getTakerIndexPieData(HttpServletRequest request);

    /**
     * 获取承办人首页柱图统计数据
     *
     * @param request
     * @return
     */
    List<Map<String, Object>> findTakerIndexChartData(HttpServletRequest request);

    /**
     * 获取承办人首页督办列表数据
     *
     * @param request
     * @return
     */
    List<Map<String, Object>> findTakerIndexTableData(HttpServletRequest request);

    /**
     * 获取承办人首页逾期统计数据
     *
     * @param request
     * @return
     */
    Map<String, Object> findTakerIndexOutDateData(HttpServletRequest request);

    /**
     * 承办人提交反馈
     *
     * @param request
     * @return
     */
    Map<String, Object> takerHandleFeedback(HttpServletRequest request);

    /**
     * 获取承办人待办结的任务列表
     *
     * @param sqlFilter
     * @return
     */
    List<Map<String, Object>> findTakerSupEndList(SqlFilter sqlFilter);

    /**
     * 根据督办事项ID初始化督办事项的督办流程
     *
     * @param request
     * @return
     */
    List<Map<String, Object>> initSuperviseProgress(HttpServletRequest request);

    /**
     * 更新督办信息
     *
     * @param params
     */
    void updateSupervise(Map<String, Object> params);

    /**
     * 获取承办人指定节点的最近一次反馈记录，指定排序方式
     *
     * @param recordId
     * @param nodeId
     * @param orderType
     * @return
     */
    Map<String, Object> getFeedbackInfo(String recordId, String nodeId, String orderType);

    /**
     * 保存反馈文件上传记录
     *
     * @param params
     */
    void saveFileAttach(Map<String, Object> params);

    /**
     * 获取督办事项指定节点的所有批复意见
     *
     * @param recordId 督办ID
     * @param nodeId   节点ID
     * @param agree    通过/未通过
     * @return
     */
    List<Map<String, Object>> findReplyList(String recordId, String nodeId, String agree);

    /**
     * 督办流程节点联动右侧反馈内容
     * cjr
     *
     * @param request
     */
    Map<String, Object> loadTakerFeedbackInfo(HttpServletRequest request);

    /**
     * 判断当前督办事项是否存在逾期
     *
     * @param supervise
     * @return
     */
    boolean isOutDate(Map<String, Object> supervise);

    /**
     * 判断当前督办事项指定节点是否存在逾期
     *
     * @param supervise
     * @param nodeId
     * @return
     */
    boolean isOutDate(Map<String, Object> supervise, String nodeId);

    /**
     * 承办人督办确认
     * cjr 20200710
     *
     * @param request
     * @return
     */
    Map<String, Object> takerConfirmSupervise(HttpServletRequest request);
}
