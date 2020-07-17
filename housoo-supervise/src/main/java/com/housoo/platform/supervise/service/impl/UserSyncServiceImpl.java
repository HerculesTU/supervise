package com.housoo.platform.supervise.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.SysUserDao;
import com.housoo.platform.core.service.SysUserService;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.supervise.service.UserSyncService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户同步实现类
 */
public class UserSyncServiceImpl extends BaseServiceImpl implements UserSyncService {

    @Resource
    private SysUserDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    @Resource
    private SysUserService sysUserService;

    /**
     * 同步用户
     *
     * @param messageContent 用户JSON字符串
     */
    @Override
    public void syncUser(String messageContent) {
        JSONObject userJson = JSONObject.parseObject(messageContent);
        Map<String, Object> userMap = new HashMap<>();
        // 同步标记 add:新增 update:修改 del:删除
        String syncFlag = userJson.getString("status");
        // 消息中文说明
        String msg = userJson.getString("msg");
        // 新增
        if ("useradd".equals(syncFlag)) {
            userMap.put("SYSUSER_ID", userJson.getString("id"));
            userMap.put("SYSUSER_ACCOUNT", userJson.getString("username"));
            userMap.put("SYSUSER_NAME", userJson.getString("fullname"));
            userMap.put("SYSUSER_PASSWORD", userJson.getString("password"));
            userMap.put("SYSUSER_MOBILE", userJson.getString("mobile"));
            userMap.put("SYSUSER_STATUS", userJson.getString("enabled"));
            userMap.put("SYSUSER_CREATETIME", userJson.getString("creationDate"));
            userMap.put("SYSUSER_DEPARTID", userJson.getString("dept_id"));
            userMap.put("SYSUSER_SN", userJson.getString("userOrder"));
            userMap.put("SYSUSER_EMAIL", userJson.getString("email"));
            userMap = sysUserService.saveOrUpdate("PLAT_SYSTEM_SYSUSER", userMap, SysConstants.ID_GENERATOR_UUID, null);
        }
        // 修改
        if ("useredit".equals(syncFlag)) {
            String userId = userJson.getString("id");
            userMap = sysUserService.getRecord("PLAT_SYSTEM_SYSUSER", new String[]{"DEPART_ID"}, new Object[]{userId});
            userMap.put("SYSUSER_ID", userId);
            userMap.put("SYSUSER_ACCOUNT", userJson.getString("username"));
            userMap.put("SYSUSER_NAME", userJson.getString("fullname"));
            userMap.put("SYSUSER_PASSWORD", userJson.getString("password"));
            userMap.put("SYSUSER_MOBILE", userJson.getString("mobile"));
            userMap.put("SYSUSER_STATUS", userJson.getString("enabled"));
            userMap.put("SYSUSER_DEPARTID", userJson.getString("dept_id"));
            userMap.put("SYSUSER_SN", userJson.getString("userOrder"));
            userMap.put("SYSUSER_EMAIL", userJson.getString("email"));
            userMap = sysUserService.saveOrUpdate("PLAT_SYSTEM_SYSUSER", userMap, SysConstants.ID_GENERATOR_UUID, null);
        }
        // 删除
        if ("userdelete".equals(syncFlag)) {
            String userIds = userJson.getString("ids");
            String[] userIdArr = userIds.split(",");
            for (int i = 0; i < userIdArr.length; i++) {
                sysUserService.deleteRecord("PLAT_SYSTEM_SYSUSER", new String[]{"SYSUSER_ID"}, new Object[]{userIdArr[i]});
            }
        }
    }
}
