package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import java.util.List;
import java.util.Map;

/**
 * 描述 工作日业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-05-03 09:32:53
 */
public interface WorkdayService extends BaseService {

    /**
     * 获取日期数据
     *
     * @param filter
     * @return
     */
    public List<Map<String, Object>> findDataBySqlFilter(SqlFilter filter);

    /**
     * @param formatDate
     */
    public String testAdd(String formatDate);

    /**
     *
     */
    public void testDel();

    /**
     * 根据开始日期和结束日期获取工作日数量
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public int getWorkDayCount(String beginDate, String endDate);

}
