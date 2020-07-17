package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述 系统用户业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-10 17:03:31
 */
public interface SysUserService extends BaseService {
    /**
     * 用户状态:正常
     */
    public static final int SYSUSER_STATUS_NORMAL = 1;
    /**
     * 用户状态:删除
     */
    public static final int SYSUSER_STATUS_DEL = -1;
    /**
     * 用户状态:禁用
     */
    public static final int SYSUSER_STATUS_DISABLED = 0;
    /**
     * 用户状态:锁定
     */
    public static final int SYSUSER_STATUS_LOCKED = 2;
    /**
     * 被授权的流程定义和类别IDS
     */
    public static final String FLOWDEFTYPEIDS_KEY = "FLOWDEFTYPEIDS";
    /**
     * 被授权的角色组ID字符串用逗号拼接
     */
    public static final String GROUPIDS_KEY = "GROUPIDS";
    /**
     * 被授权的角色组ID集合
     */
    public static final String GROUPIDSET_KEY = "GROUPIDSET";
    /**
     * 是否是超管
     */
    public static final String ISADMIN_KEY = "IS_ADMIN";
    /**
     * 被授权的角色编码集合
     */
    public static final String ROLECODESET_KEY = "ROLECODESET";
    /**
     * 被授权的角色编码字符串
     */
    public static final String ROLECODES_KEY = "ROLECODES";
    /**
     * 被授权的编码集合
     */
    public static final String RESCODESET_KEY = "RESCODESET";
    /**
     * 被授权的编码
     */
    public static final String RESCODES_KEY = "RESCODES";
    /**
     *
     */
    public static final String GRANTURLS_KEY = "GRANTURLS";
    /**
     * 被授权的资源URL
     */
    public static final String GRANTURLSTR_KEY = "GRANTURLSTR";
    /**
     *
     */
    public static final String GRANTRESLIST_KEY = "GRANTRESLIST";
    /**
     * 缺省明文密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 根据用户账号和密码获取用户信息数据
     *
     * @param userAccount
     * @param pass
     * @return
     */
    public Map<String, Object> checkAccountAndPass(String userAccount, String pass);

    /**
     * 根据用户账号和token获取用户信息
     *
     * @param userAccount
     * @param token
     * @return
     */
    public Map<String, Object> getByAccountAndToken(String userAccount, String token);

    /**
     * 新增或者修改系统用户信息
     *
     * @param sysUser
     * @return
     */
    public Map<String, Object> saveOrUpdateUser(Map<String, Object> sysUser, int idGenerator);

    /**
     * 第三方系统新增或者修改用户
     *
     * @param sysUser
     * @return
     */
    public Map<String, Object> saveOrUpdateByThird(Map<String, Object> sysUser);

    /**
     * 根据部门ID获取用户列表数据
     *
     * @param departId
     * @return
     */
    public List<Map<String, Object>> findByDepartId(String departId);

    /**
     * 根据部门ID和角色IDS获取用户列表数据
     *
     * @param departId
     * @param roleIds
     * @return
     */
    public List<Map<String, Object>> findByDepartIdAndRoleIds(String departId, String roleIds);

    /**
     * 根据角色ID获取用户列表
     *
     * @param roleId
     * @return
     */
    public List<Map<String, Object>> findByRoleId(String roleId);

    /**
     * 根据角色组ID获取用户列表,如果没有传入,则获取所有
     *
     * @param groupId
     * @return
     */
    public List<Map<String, Object>> findByRoleGroupId(String groupId);

    /**
     * 根据filter获取网格项目
     *
     * @param sqlFilter
     * @return
     */
    public List<Map<String, Object>> findGridItemList(SqlFilter sqlFilter);

    /**
     * 根据角色ID获取用户IDS
     *
     * @param roleId
     * @return
     */
    public List<String> findUserIds(String roleId);

    /**
     * 保存角色用户中间表
     *
     * @param userId
     * @param roleIds
     */
    public void saveRoles(String userId, List<String> roleIds);

    /**
     * 根据用户ID数组删除用户数据,伪删除
     *
     * @param userIds
     */
    public void deleteUserCascadeRole(String[] userIds);

    /**
     * 获取获得用户信息的前缀SQL
     *
     * @return
     */
    public String getUserInfoPreSql();

    /**
     * 获取多个用户的姓名单位相关信息
     *
     * @param userIds
     * @return
     */
    public Map<String, String> getUserNamesCompanyInfo(String userIds);

    /**
     * 获取被授权的资源ID集合
     *
     * @param userId
     * @return
     */
    public List<String> findGrantRightIds(String userId);

    /**
     * 获取在线用户列表
     *
     * @param sqlFilter
     * @param fieldConfig
     * @return
     */
    public List<Map<String, Object>> findOnlineUsers(SqlFilter sqlFilter, Map<String, Object> fieldConfig);

    /**
     * 判断是否存在用户
     *
     * @param userId
     * @return
     */
    public boolean isExistsUser(String userId);

    /**
     * 获取测试的资源用户列表
     *
     * @param sqlFilter
     * @return
     */
    public List<Map<String, Object>> findTestResUsers(SqlFilter sqlFilter);

    /**
     * 获取系统用户详细信息
     *
     * @param request
     * @param postParams
     * @return
     */
    public Map<String, Object> getSysUserInfo(HttpServletRequest request,
                                              Map<String, Object> postParams);

    /**
     * 新增或者修改用户信息
     *
     * @param request
     * @param postParams
     * @return
     */
    public Map<String, Object> saveOrUpdateUser(HttpServletRequest request,
                                                Map<String, Object> postParams);

    /**
     * 删除用户信息
     *
     * @param request
     * @param postParams
     * @return
     */
    public Map<String, Object> deleteUser(HttpServletRequest request,
                                          Map<String, Object> postParams);

    /**
     * 重置用户的密码
     *
     * @param userIds
     */
    public void resetPassword(String userIds);

    /**
     * 更新用户拥有的权限JSON字段
     *
     * @param userId
     */
    public void updateUserRightJson(String userId);

    /**
     * 更新权限JSON字段为空
     */
    public void updateRightJsonToNull();

    /**
     * 更新权限JSON字段为空
     *
     * @param roleId
     */
    public void updateRightJsonToNull(String roleId, String tableName);

    /**
     * 根据单位ID和角色ID获取用户IDS
     *
     * @param companyId
     * @param roleIds
     * @return
     */
    public List<String> findUserIds(String companyId, String roleIds);

    /**
     * 获取下个用户的排序值
     *
     * @return
     */
    public int getNextUserSn(String companyId);

    /**
     * 保存排序
     *
     * @param userIds
     */
    public void updateSn(String[] userIds);

    /**
     * 根据用户组ID获取用户ID列表
     *
     * @param USERGROUP_ID
     * @return
     */
    public List<String> findGroupUserIds(String USERGROUP_ID);

    /**
     * 新增或者修改用户信息
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveOrUpdateUser(HttpServletRequest request);

    /**
     * 删除用户信息
     *
     * @param request
     * @return
     */
    public Map<String, Object> deleteUsers(HttpServletRequest request);

    /**
     * 根据部门ID获取获取ID列表
     *
     * @param DEPART_ID
     * @return
     */
    public List<String> findDepartUserIds(String DEPART_ID);

    /**
     * 获取无部门的用户
     *
     * @param DEPART_COMPANYID
     * @param ROLE_IDS
     * @return
     */
    public List<Map<String, Object>> findNoDepartIdByCompanyIdAndRoleIds(
            String DEPART_COMPANYID, String ROLE_IDS);

    /**
     * 判断用户是否首次登录
     * cjr 20190418
     *
     * @param sysUser
     * @return
     */
    public boolean checkFirstLogin(Map<String, Object> sysUser);

    /**
     * 根据用户账号和密码锁定用户
     *
     * @param userAccount
     * @param pwd
     */
    void lockUser(String userAccount, String pwd);

    /**
     * 根据用户ID解锁用户
     *
     * @param userId
     */
    void unlockUser(String userId);
}
