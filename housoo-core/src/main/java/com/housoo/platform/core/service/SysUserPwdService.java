package com.housoo.platform.core.service;

import java.util.Map;

/**
 * 描述 系统用户密码业务相关service
 *
 * @author gf
 * @version 1.0
 * @created 2019-11-22
 */
public interface SysUserPwdService extends BaseService {

    /**
     * 根据用户ID获取用户密码信息
     *
     * @param userId
     * @return
     */
    Map<String, Object> getUserPwdInfo(String userId);

}
