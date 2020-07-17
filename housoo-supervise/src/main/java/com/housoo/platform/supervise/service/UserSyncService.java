package com.housoo.platform.supervise.service;

import com.housoo.platform.core.service.BaseService;

/**
 * 用户同步Service
 */
public interface UserSyncService extends BaseService {

    /**
     * 同步用户
     *
     * @param messageContent 用户JSON字符串
     */
    void syncUser(String messageContent);
}
