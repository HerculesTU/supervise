package com.housoo.platform.core.service;

import java.util.List;
import java.util.Map;

/**
 * 描述 定时任务业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-27 16:37:05
 */
public interface ScheduleService extends BaseService {
    /**
     * 根据编码判断是否存在记录
     *
     * @param scheduleCode
     * @return
     */
    public boolean isExistsSchedule(String scheduleCode);

    /**
     * 更新定时器的状态
     *
     * @param scheduleIds
     * @param status
     */
    public void updateStatus(String scheduleIds, int status);

    /**
     * 启动定时调度池
     */
    public void startAllSchedule();

    /**
     * 根据状态获取列表数据
     *
     * @param status
     * @return
     */
    public List<Map<String, Object>> findByByStatus(int status);

    /**
     * 级联删除定时调度
     *
     * @param scheduleIds
     */
    public void deleteCascadeJob(String scheduleIds);

    /**
     * @param scheduleInfo
     * @return
     */
    public Map<String, Object> saveOrUpdateCascadeJob(Map<String, Object> scheduleInfo);
}
