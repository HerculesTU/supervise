/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.dao;

import com.housoo.platform.core.dao.BaseDao;

import java.util.List;
import java.util.Map;

/**
 * 描述 承办人业务相关dao
 *
 * @author cjr
 * @version 1.0
 * @created 2020-04-14 15:30:11
 */
public interface TakerDao extends BaseDao {
    /**
     * 获取承办人首页年度数值型统计数据
     *
     * @param params
     * @return
     */
    Map<String, Object> getTakerIndexNumericDataByYear(Map<String, Object> params);

    /**
     * 获取承办人首页季度数值型统计数据
     *
     * @param params
     * @return
     */
    Map<String, Object> getTakerIndexNumericDataByQuarter(Map<String, Object> params);

    /**
     * 获取承办人首页月份数值型统计数据
     *
     * @param params
     * @return
     */
    Map<String, Object> getTakerIndexNumericDataByMonth(Map<String, Object> params);

    /**
     * 获取承办人首页近七天数值型统计数据
     *
     * @param params
     * @return
     */
    Map<String, Object> getTakerIndexNumericDataByDays(Map<String, Object> params);

    /**
     * 获取承办人首页时间段数值型统计数据
     *
     * @param params
     * @return
     */
    Map<String, Object> getTakerIndexNumericDataByRangeTime(Map<String, Object> params);

    /**
     * 根据督办事项Id获取承办部门任务信息
     *
     * @param superviseId
     * @return
     */
    List<Map<String, Object>> findTaskListBySuperviseId(String superviseId);

    /**
     * 根据督办事项Id获取承办部门督办事项节点信息
     *
     * @param superviseId
     * @return
     */
    List<Map<String, Object>> findTaskNodeListBySuperviseId(String superviseId);

    /**
     * 20200511 新增
     * 获取承办人首页年度饼图统计数据
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> getTakerIndexPieDataByYear(Map<String, Object> params);

    /**
     * 20200511 新增
     * 获取承办人首页季度饼图统计数据
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> getTakerIndexPieDataByQuarter(Map<String, Object> params);

    /**
     * 20200511 新增
     * 获取承办人首页月份饼图统计数据
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> getTakerIndexPieDataByMonth(Map<String, Object> params);

    /**
     * 20200511 新增
     * 获取承办人首页近七天饼图统计数据
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> getTakerIndexPieDataByDays(Map<String, Object> params);

    /**
     * 20200511 新增
     * 获取承办人首页时间段饼图统计数据
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> getTakerIndexPieDataByRangeTime(Map<String, Object> params);

}
