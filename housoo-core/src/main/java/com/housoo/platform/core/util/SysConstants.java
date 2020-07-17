package com.housoo.platform.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述 定义全局常用常量值
 *
 * @author housoo
 * @created 2017年1月3日 下午2:52:31
 */
public class SysConstants {
    /**
     * 主键生成策略:使用UUID
     */
    public static final int ID_GENERATOR_UUID = 1;
    /**
     * 主键生成策略:自增长
     */
    public static final int ID_GENERATOR_AUTOINCREMENT = 2;
    /**
     * 主键生成策略:分配模式
     */
    public static final int ID_GENERATOR_ASSIGNED = 3;
    /**
     * 主键生成策略:序列
     */
    public static final int ID_GENERATOR_SEQ = 4;
    /**
     * 后台登录用户sessionKey
     */
    public static final String BACK_PLAT_USER_SESSION_KEY = "__BACK_PLAT_USER";
    /**
     * 后台登录用户状态KEY
     */
    public static final String BACK_PLAT_USER_STATUS_KEY = "__BACK_USER_STATUS";
    /**
     * 后台登录用户JSON字符串
     */
    public static final String BACKPLAT_USERSESSIONJSON = "__BACK_PLAT_USER_JSON";
    /**
     * 改变表单控件权限和必填属性的MAPKEY
     */
    public static final String CHANGE_COMP_AUTH_MAP_KEY = "__CHANGE_COMP_AUTH_MAP";
    /**
     * 改变自定义控件的权限的MAPKEY
     */
    public static final String CHANGE_DEF_CTRL_AUTH_MAP_KEY = "__CHANGE_DEF_CTRL_AUTH_MAP";
    /**
     * 会话强制退出KEY
     */
    public static final String SESSION_FORCE_LOGOUT_KEY = "session.force.logout";
    /**
     * SSO Cookie名称
     */
    public static final String SSO_COOKIE_NAME = "PLAT_SSO";
    /**
     * 在线用户MAP
     */
    public static final String ONLINE_USERS_KEY = "__ONLINE_USERS";
    /**
     * SSO票据和名称
     */
    public static final Map<String, String> SSO_TICKET_AND_NAME = new HashMap<String, String>();

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
     * 被授权的URL
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

}
