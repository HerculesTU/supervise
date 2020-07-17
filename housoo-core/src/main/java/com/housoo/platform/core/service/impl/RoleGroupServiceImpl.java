package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.core.dao.RoleGroupDao;
import com.housoo.platform.core.service.RoleGroupService;
import com.housoo.platform.core.service.RoleService;
import com.housoo.platform.core.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 描述 角色组业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-15 15:20:40
 */
@Service("roleGroupService")
public class RoleGroupServiceImpl extends BaseServiceImpl implements RoleGroupService {

    /**
     * 所引入的dao
     */
    @Resource
    private RoleGroupDao dao;
    /**
     *
     */
    @Resource
    private RoleService roleService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取下拉框数据源
     *
     * @param queryParams
     * @return
     */
    @Override
    public List<Map<String, Object>> findForSelect(String queryParams) {
        StringBuffer sql = new StringBuffer("select T.GROUP_ID AS VALUE,T.GROUP_NAME AS LABEL ");
        sql.append("from PLAT_SYSTEM_ROLEGROUP T  ");
        //获取当前登录用户
        Map<String, Object> sysUser = PlatAppUtil.getBackPlatLoginUser();
        boolean isAdmin = (boolean) sysUser.get(SysUserService.ISADMIN_KEY);
        if (!isAdmin) {
            Set<String> grantGroupIds = (Set<String>) sysUser.get(SysUserService.GROUPIDSET_KEY);
            sql.append(" WHERE T.GROUP_ID IN ").append(PlatStringUtil.getSqlInCondition(grantGroupIds));
        }
        sql.append(" ORDER BY T.GROUP_CREATETIME ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        return list;
    }

    /**
     * 获取角色组列表数据源
     *
     * @param queryParamJson
     * @return
     */
    @Override
    public List<Map<String, Object>> findGroupList(String queryParamJson) {
        StringBuffer sql = new StringBuffer("select T.GROUP_ID AS VALUE,T.GROUP_NAME AS LABEL ");
        sql.append("from PLAT_SYSTEM_ROLEGROUP T ");
        //获取当前登录用户
        Map<String, Object> sysUser = PlatAppUtil.getBackPlatLoginUser();
        boolean isAdmin = (boolean) sysUser.get(SysUserService.ISADMIN_KEY);
        if (!isAdmin) {
            Set<String> grantGroupIds = (Set<String>) sysUser.get(SysUserService.GROUPIDSET_KEY);
            sql.append("WHERE T.GROUP_ID IN ").append(PlatStringUtil.getSqlInCondition(grantGroupIds));
        }
        sql.append(" ORDER BY T.GROUP_CREATETIME ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        if (list == null) {
            list = new ArrayList<Map<String, Object>>();
        }
        Map<String, Object> allType = new HashMap<String, Object>();
        allType.put("LABEL", "全部角色组");
        allType.put("VALUE", "0");
        list.add(0, allType);
        return list;
    }

    /**
     * 获取角色组信息列表
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> findGroupList() {
        StringBuffer sql = new StringBuffer("select T.GROUP_ID AS VALUE,T.GROUP_NAME AS LABEL ");
        sql.append("from PLAT_SYSTEM_ROLEGROUP T ORDER BY T.GROUP_CREATETIME ASC ");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        return list;
    }

    /**
     * 获取角色组树形JSON
     *
     * @param params
     * @return
     */
    @Override
    public String getGroupTreeJson(Map<String, Object> params) {
        String needCheckIds = (String) params.get("needCheckIds");
        Set<String> needCheckIdSet = new HashSet<String>();
        if (StringUtils.isNotEmpty(needCheckIds)) {
            needCheckIdSet = new HashSet<String>(Arrays.asList(needCheckIds.split(",")));
        }
        Map<String, Object> rootNode = new HashMap<String, Object>();
        rootNode.put("id", "0");
        rootNode.put("name", "角色组信息树");
        rootNode.put("nocheck", true);
        rootNode.put("open", true);
        List<Map<String, Object>> groupList = this.findGroupList();
        for (Map<String, Object> group : groupList) {
            String groupId = (String) group.get("VALUE");
            String groupName = (String) group.get("LABEL");
            group.put("id", groupId);
            group.put("name", groupName);
            if (needCheckIdSet.contains(groupId)) {
                group.put("checked", true);
            }
        }
        rootNode.put("children", groupList);
        return JSON.toJSONString(rootNode);
    }

    /**
     * 获取角色组和角色的树形JSON
     *
     * @param params
     * @return
     */
    @Override
    public String getGroupAndRoleJson(Map<String, Object> params) {
        String needCheckIds = (String) params.get("needCheckIds");
        Set<String> needCheckIdSet = new HashSet<String>();
        if (StringUtils.isNotEmpty(needCheckIds)) {
            needCheckIdSet = new HashSet<String>(Arrays.asList(needCheckIds.split(",")));
        }
        Map<String, Object> rootNode = new HashMap<String, Object>();
        rootNode.put("id", "0");
        rootNode.put("name", "角色信息树");
        rootNode.put("nocheck", true);
        List<Map<String, Object>> groupList = this.findGroupList();
        for (Map<String, Object> group : groupList) {
            String groupId = (String) group.get("VALUE");
            String groupName = (String) group.get("LABEL");
            group.put("id", groupId);
            group.put("name", groupName);
            group.put("nocheck", true);
            List<Map<String, Object>> roleList = roleService.findByGroupId(groupId);
            if (roleList != null && roleList.size() > 0) {
                for (Map<String, Object> role : roleList) {
                    String roleId = (String) role.get("ROLE_ID");
                    if (needCheckIdSet.contains(roleId)) {
                        role.put("checked", true);
                    }
                    role.put("id", role.get("ROLE_ID"));
                    role.put("name", role.get("ROLE_NAME"));
                }
                group.put("children", roleList);
            }
        }
        rootNode.put("children", groupList);
        return JSON.toJSONString(rootNode);
    }

    /**
     * 获取自动补全的角色组角色数据
     *
     * @param filter
     * @return
     */
    @Override
    public List<Map<String, Object>> findAutoGroupRole(SqlFilter filter) {
        StringBuffer sql = new StringBuffer("SELECT R.GROUP_NAME AS value,R.GROUP_NAME AS label");
        sql.append(" FROM PLAT_SYSTEM_ROLEGROUP R ");
        sql.append(" ORDER BY R.GROUP_CREATETIME DESC");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> groupList = dao.findBySql(sql.toString(), null, null);
        list.addAll(groupList);
        //获取所有角色数据
        sql = new StringBuffer("SELECT R.ROLE_NAME AS value,R.ROLE_NAME AS label");
        sql.append(" FROM PLAT_SYSTEM_ROLE R ORDER BY R.ROLE_CREATETIME DESC ");
        List<Map<String, Object>> roleList = dao.findBySql(sql.toString(), null, null);
        list.addAll(roleList);
        return list;
    }

    /**
     * 获取自动补全的角色组数据
     *
     * @param filter
     * @return
     */
    @Override
    public List<Map<String, Object>> findAutoGroup(SqlFilter filter) {
        StringBuffer sql = new StringBuffer("SELECT R.GROUP_NAME AS value,R.GROUP_NAME AS label");
        sql.append(" FROM PLAT_SYSTEM_ROLEGROUP R ");
        sql.append(" ORDER BY R.GROUP_CREATETIME DESC");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> groupList = dao.findBySql(sql.toString(), null, null);
        list.addAll(groupList);
        return list;
    }

    /**
     * 根据角色组删除ID并且级联更新角色
     * @param groupId
     */
    @Override
    public void deleteGroupCascadeRole(String groupId) {
        StringBuffer sql = new StringBuffer("UPDATE PLAT_SYSTEM_ROLE ");
        sql.append(" SET ROLE_GROUPID=null where ROLE_GROUPID=?");
        dao.executeSql(sql.toString(), new Object[]{groupId});
        //删除角色组
        dao.deleteRecords("PLAT_SYSTEM_ROLEGROUP", "GROUP_ID", new String[]{groupId});
    }

    /**
     * 根据用户ID获取被授权的角色组IDS
     *
     * @param userId
     * @return
     */
    @Override
    public Set<String> getUserGrantGroupIds(String userId) {
        return dao.getUserGrantGroupIds(userId);
    }

}
