package com.housoo.platform.core.dao;

/**
 * 描述 定时任务业务相关dao
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-27 16:37:05
 */
public interface ScheduleDao extends BaseDao {
    /**
     * 根据编码获取数量
     *
     * @param scheduleCode
     * @return
     */
    public int getCountByScheduleCode(String scheduleCode);

}
