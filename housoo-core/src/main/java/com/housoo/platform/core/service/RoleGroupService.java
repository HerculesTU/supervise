package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述 角色组业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-15 15:20:40
 */
public interface RoleGroupService extends BaseService {
    /**
     * 获取下拉框数据源
     *
     * @param queryParams
     * @return
     */
    public List<Map<String, Object>> findForSelect(String queryParams);

    /**
     * 获取角色组列表数据源
     *
     * @param queryParamJson
     * @return
     */
    public List<Map<String, Object>> findGroupList(String queryParamJson);

    /**
     * 获取角色组和角色的树形JSON
     *
     * @param params
     * @return
     */
    public String getGroupAndRoleJson(Map<String, Object> params);

    /**
     * 获取角色组树形JSON
     *
     * @param params
     * @return
     */
    public String getGroupTreeJson(Map<String, Object> params);

    /**
     * 获取自动补全的角色组角色数据
     *
     * @param filter
     * @return
     */
    public List<Map<String, Object>> findAutoGroupRole(SqlFilter filter);

    /**
     * 获取自动补全的角色组数据
     *
     * @param filter
     * @return
     */
    public List<Map<String, Object>> findAutoGroup(SqlFilter filter);

    /**
     * 获取角色组信息列表
     *
     * @return
     */
    public List<Map<String, Object>> findGroupList();

    /**
     * 根据角色组删除ID并且级联更新角色
     * @param groupId
     */
    public void deleteGroupCascadeRole(String groupId);

    /**
     * 根据用户ID获取被授权的角色组IDS
     *
     * @param userId
     * @return
     */
    public Set<String> getUserGrantGroupIds(String userId);

}
