package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.util.UUIDGenerator;
import com.housoo.platform.core.dao.RoleRightDao;
import com.housoo.platform.core.service.RoleRightService;
import com.housoo.platform.core.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 描述 角色权限中间表业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-23 16:57:24
 */
@Service("roleRightService")
public class RoleRightServiceImpl extends BaseServiceImpl implements RoleRightService {

    /**
     * 所引入的dao
     */
    @Resource
    private RoleRightDao dao;
    /**
     *
     */
    @Resource
    private SysUserService sysUserService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 保存角色权限中间表数据
     *
     * @param roleId
     * @param tableName
     * @param recorIds
     */
    @Override
    public void saveRights(String roleId, List<String> resIds,
                           List<String> groupIds, List<String> typeDefIds, String tableName) {
        //先清除数据
        StringBuffer sql = new StringBuffer("DELETE FROM ");
        sql.append("PLAT_SYSTEM_ROLERIGHT WHERE ROLE_ID=? AND ROLE_TABLE=? ");
        dao.executeSql(sql.toString(), new Object[]{roleId, tableName});
        List<Map<String, Object>> rightList = new ArrayList<Map<String, Object>>();
        for (String resId : resIds) {
            Map<String, Object> roleRight = new HashMap<String, Object>();
            roleRight.put("RIGHT_ID", UUIDGenerator.getUUID());
            roleRight.put("ROLE_ID", roleId);
            roleRight.put("RE_RECORDID", resId);
            roleRight.put("RE_TABLENAME", "PLAT_SYSTEM_RES");
            roleRight.put("ROLE_TABLE", tableName);
            rightList.add(roleRight);
        }
        for (String groupId : groupIds) {
            Map<String, Object> roleRight = new HashMap<String, Object>();
            roleRight.put("RIGHT_ID", UUIDGenerator.getUUID());
            roleRight.put("ROLE_ID", roleId);
            roleRight.put("RE_RECORDID", groupId);
            roleRight.put("RE_TABLENAME", "PLAT_SYSTEM_ROLEGROUP");
            roleRight.put("ROLE_TABLE", tableName);
            rightList.add(roleRight);
        }
        for (String typeDefId : typeDefIds) {
            Map<String, Object> roleRight = new HashMap<String, Object>();
            roleRight.put("RIGHT_ID", UUIDGenerator.getUUID());
            roleRight.put("ROLE_ID", roleId);
            roleRight.put("RE_RECORDID", typeDefId);
            roleRight.put("RE_TABLENAME", "JBPM6_FLOWDEF");
            roleRight.put("ROLE_TABLE", tableName);
            rightList.add(roleRight);
        }
        if (rightList.size() > 0) {
            dao.saveBatch(rightList, "PLAT_SYSTEM_ROLERIGHT");
        }

        sysUserService.updateRightJsonToNull(roleId, tableName);
    }

    /**
     * 根据角色ID获取
     *
     * @param roleId
     * @return
     */
    @Override
    public List<String> getRightRecordIds(String roleId, String tableName) {
        return dao.getRightRecordIds(roleId, tableName);
    }

    /**
     * 根据用户ID和表名称获取用户被授权的资源IDS集合
     *
     * @param userId
     * @param tableName
     * @return
     */
    @Override
    public Set<String> getUserGrantRightIds(String userId, String tableName) {
        return dao.getUserGrantRightIds(userId, tableName);
    }

}
