package com.housoo.platform.supervise.dao;

import com.housoo.platform.core.dao.BaseDao;

import java.util.Map;

/**
 * 描述 督查督办业务 立项人 相关Dao
 *
 * @author zxl
 * @version 1.0
 * @created 2020-04-22 15:30:11
 */
public interface SponsorDao extends BaseDao {

    /**
     * 获取立项人首页年度数值型统计数据
     *
     * @param params
     * @return
     */
    Map<String, Object> getSponsorIndexNumericDataByYear(Map<String, Object> params);

    /**
     * 获取立项人首页季度数值型统计数据
     *
     * @param params
     * @return
     */
    Map<String, Object> getSponsorIndexNumericDataByQuarter(Map<String, Object> params);

    /**
     * 获取立项人首页月份数值型统计数据
     *
     * @param params
     * @return
     */
    Map<String, Object> getSponsorIndexNumericDataByMonth(Map<String, Object> params);

    /**
     * 获取立项人首页近七天数值型统计数据
     *
     * @param params
     * @return
     */
    Map<String, Object> getSponsorIndexNumericDataByDays(Map<String, Object> params);

    /**
     * 获取立项人首页时间段数值型统计数据
     *
     * @param params
     * @return
     */
    Map<String, Object> getSponsorIndexNumericDataByRangeTime(Map<String, Object> params);
}
