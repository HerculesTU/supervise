package com.housoo.platform.core.service;

/**
 * 描述 定时器日志业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-29 16:05:22
 */
public interface JobLogService extends BaseService {
    /**
     * 根据定时器编码保存日志
     *
     * @param scheduleCode
     */
    public void saveJobLog(String scheduleCode);
}
