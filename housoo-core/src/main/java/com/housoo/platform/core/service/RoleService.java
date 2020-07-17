package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述 角色业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-15 15:20:40
 */
public interface RoleService extends BaseService {
    /**
     * 保存角色用户信息
     *
     * @param roleId
     * @param userIds
     */
    public void saveUsers(String roleId, List<String> userIds);

    /**
     * 根据角色组ID获取角色列表
     *
     * @param groupId:如果为空，那么获取所有的角色
     * @return
     */
    public List<Map<String, Object>> findByGroupId(String groupId);

    /**
     * 获取角色和用户的树形JSON
     *
     * @param params
     * @return
     */
    public String getRoleAndUserJson(Map<String, Object> params);

    /**
     * 获取自动补全的角色用户数据
     *
     * @param filter
     * @return
     */
    public List<Map<String, Object>> findAutoRoleUser(SqlFilter filter);

    /**
     * 根据filter获取网格项目
     *
     * @param sqlFilter
     * @return
     */
    public List<Map<String, Object>> findGridItemList(SqlFilter sqlFilter);

    /**
     * 根据用户ID获取关联的角色IDS
     *
     * @param userId
     * @return
     */
    public List<String> findRoleIds(String userId);

    /**
     * 根据用户ID获取管理的角色列表
     *
     * @param userId
     * @return
     */
    public List<Map<String, Object>> findByUserId(String userId);

    /**
     * 删除角色信息并且级联删除相关中间表
     *
     * @param roleIds
     */
    public void deleteCascadeUserAndRes(String roleIds);

    /**
     * 根据用户ID获取被授权的角色编码集合
     *
     * @param userId
     * @return
     */
    public Set<String> getGrantRoleCodeSet(String userId);

    /**
     * 新增或者修改角色信息
     *
     * @param request
     * @param postParams
     * @return
     */
    public Map<String, Object> saveOrUpdateRole(HttpServletRequest request,
                                                Map<String, Object> postParams);

    /**
     * 删除角色信息
     *
     * @param request
     * @param postParams
     * @return
     */
    public Map<String, Object> deleteRole(HttpServletRequest request,
                                          Map<String, Object> postParams);

    /**
     * 分配用户
     *
     * @param request
     * @param postParams
     * @return
     */
    public Map<String, Object> grantUsers(HttpServletRequest request,
                                          Map<String, Object> postParams);

    /**
     * 获取下个角色的排序值
     *
     * @return
     */
    public int getNextRoleSn();

    /**
     * @param roleIds
     */
    public void updateSn(String[] roleIds);

    /**
     * 新增或修改角色信息
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveOrUpdateRole(HttpServletRequest request);

    /**
     * 删除角色接口
     *
     * @param request
     * @return
     */
    public Map<String, Object> delRole(HttpServletRequest request);

    /**
     * 分配对象权限接口
     *
     * @param request
     * @return
     */
    public Map<String, Object> grantRights(HttpServletRequest request);

    /**
     * 分配用户接口
     *
     * @param request
     * @return
     */
    public Map<String, Object> grantUsers(HttpServletRequest request);
}
