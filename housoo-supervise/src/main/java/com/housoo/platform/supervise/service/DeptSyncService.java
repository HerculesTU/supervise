package com.housoo.platform.supervise.service;

import com.housoo.platform.core.service.BaseService;

/**
 * 用户同步Service
 */
public interface DeptSyncService extends BaseService {
    /**
     * 同步部门
     *
     * @param messageContent 部门JSON字符串
     */
    void syncDept(String messageContent);
}
