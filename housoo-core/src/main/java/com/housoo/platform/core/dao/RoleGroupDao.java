package com.housoo.platform.core.dao;

import java.util.Set;

/**
 * 描述 角色组业务相关dao
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-15 15:20:40
 */
public interface RoleGroupDao extends BaseDao {

    /**
     * 根据用户ID
     *
     * @param userId
     * @return
     */
    public Set<String> getUserGrantGroupIds(String userId);
}
