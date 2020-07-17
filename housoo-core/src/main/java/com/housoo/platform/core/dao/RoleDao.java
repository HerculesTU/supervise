package com.housoo.platform.core.dao;

import java.util.List;

/**
 * 描述 角色业务相关dao
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-15 15:20:40
 */
public interface RoleDao extends BaseDao {
    /**
     * 根据用户ID获取关联的角色IDS
     *
     * @param userId
     * @return
     */
    public List<String> findRoleIds(String userId);

    /**
     * 根据用户ID获取被授权的角色编码列表
     *
     * @param userId
     * @return
     */
    public List<String> findGrantRoleCodes(String userId);

    /**
     * @param roleIds
     */
    public void updateSn(String[] roleIds);
}
