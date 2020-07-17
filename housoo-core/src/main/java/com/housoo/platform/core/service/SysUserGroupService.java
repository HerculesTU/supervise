package com.housoo.platform.core.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述 用户组管理业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-10 22:05:56
 */
public interface SysUserGroupService extends BaseService {
    /**
     * 根据单位ID获取下拉树数据
     *
     * @param companyId
     * @return
     */
    public List<Map<String, Object>> findSelectList(String companyId);

    /**
     * @param USERGROUP_ID
     * @param checkUserIds
     */
    public void saveUsers(String USERGROUP_ID, String checkUserIds);

    /**
     * 分配用户接口
     *
     * @param request
     * @return
     */
    public Map<String, Object> grantUsers(HttpServletRequest request);

    /**
     * 删除用户组信息
     *
     * @param request
     * @return
     */
    public Map<String, Object> deleteGroups(HttpServletRequest request);

    /**
     * @param request
     * @return
     */
    public Map<String, Object> saveOrUpdateGroup(HttpServletRequest request);

}
