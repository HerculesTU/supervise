package com.housoo.platform.core.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述 安全审核记录业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-13 16:40:58
 */
public interface SecAuditService extends BaseService {
    /**
     * 业务操作类型:新增
     */
    public final static int BUSTYPE_ADD = 1;
    /**
     * 业务操作类型:编辑
     */
    public final static int BUSTYPE_EDIT = 2;
    /**
     * 业务操作类型:删除
     */
    public final static int BUSTYPE_DEL = 3;
    /**
     * 业务操作类型:授权
     */
    public final static int BUSTYPE_GRANT = 4;
    /**
     * 业务操作类型:分配用户
     */
    public final static int BUSTYPE_GRANTUSER = 5;
    /**
     * 业务对象:部门
     */
    public final static int BUSOBJ_DEP = 1;
    /**
     * 业务对象:人员
     */
    public final static int BUSOBJ_USER = 2;
    /**
     * 业务对象:角色
     */
    public final static int BUSOBJ_ROLE = 3;
    /**
     * 业务对象:用户组
     */
    public final static int BUSOBJ_USERGROUP = 4;
    /**
     * 业务对象:单位
     */
    public final static int BUSOBJ_COMPANY = 5;
    /**
     * 业务对象:系统日志
     */
    public final static int BUSOBJ_SYSLOG = 6;
    /**
     * 业务对象:岗位
     */
    public final static int BUSOBJ_POS = 7;
    /**
     * 业务对象:用户分管领导
     */
    public final static int BUSOBJ_USERDEPART = 8;

    /**
     * 创建部门的安全审核记录
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveDeleteDep(HttpServletRequest request);

    /**
     * 创建部门或修改
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveOrUpdateDep(HttpServletRequest request);

    /**
     * 创建部门的分配用户审核操作
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveGrantUserToDep(HttpServletRequest request);

    /**
     * 获取状态信息列表
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> findStatusList(String param);

    /**
     * 根据审批状态获取数量
     *
     * @param auditStatus
     * @return
     */
    public int getCountByStatus(int auditStatus);

    /**
     * @param request
     * @return
     */
    public Map<String, Object> saveDeleteUser(HttpServletRequest request);

    /**
     * 新增或者修改用户
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveOrUpdateUser(HttpServletRequest request);

    /**
     * 授权对象权限
     *
     * @param request
     * @return
     */
    public Map<String, Object> grantsRightRole(HttpServletRequest request);

    /**
     * 创建单位的安全审核记录
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveDelCompany(HttpServletRequest request);

    /**
     * 创建或者修改单位
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveOrUpdateCompany(HttpServletRequest request);

    /**
     * 创建删除日志的安全审核记录
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveDeleteLog(HttpServletRequest request);

    /**
     * 创建角色的分配用户审核操作
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveGrantUserToRole(HttpServletRequest request);

    /**
     * 创建用户组的分配用户审核操作
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveGrantUserToGroup(HttpServletRequest request);

    /**
     * 创建岗位的分配用户审核操作
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveGrantUserToPos(HttpServletRequest request);

    /**
     * 获取审核操作明细
     *
     * @param secId
     * @return
     */
    public Map<String, Object> getSecAuditInfo(String secId);

    /**
     * @param request
     * @return
     */
    public Map<String, Object> saveDeleteRole(HttpServletRequest request);

    /**
     * 创建或者修改角色
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveOrUpdateRole(HttpServletRequest request);

    /**
     * @param request
     * @return
     */
    public Map<String, Object> saveDeleteGroup(HttpServletRequest request);

    /**
     * 创建或者修改用户组
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveOrUpdateGroup(HttpServletRequest request);

    /**
     * @param request
     * @return
     */
    public Map<String, Object> saveDeletePos(HttpServletRequest request);

    /**
     * 创建或者修改用户组
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveOrUpdatePos(HttpServletRequest request);

    /**
     * 创建或者修改用户分管部门
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveOrUpdateUserDepart(HttpServletRequest request);
}
