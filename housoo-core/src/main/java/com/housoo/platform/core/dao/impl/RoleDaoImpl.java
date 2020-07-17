package com.housoo.platform.core.dao.impl;

import com.housoo.platform.core.util.PlatCollectionUtil;
import com.housoo.platform.core.dao.RoleDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述角色业务相关dao实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-15 15:20:40
 */
@Repository
public class RoleDaoImpl extends BaseDaoImpl implements RoleDao {

    /**
     * 根据用户ID获取关联的角色IDS
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> findRoleIds(String userId) {
        StringBuffer sql = new StringBuffer("SELECT T.ROLE_ID ");
        sql.append("FROM PLAT_SYSTEM_SYSUSERROLE T");
        sql.append(" WHERE T.SYSUSER_ID=? ORDER BY T.ROLE_ID ASC");
        List<String> list = this.getJdbcTemplate().queryForList(sql.toString(),
                new Object[]{userId}, String.class);
        return list;
    }

    /**
     * 根据用户ID获取被授权的角色编码列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> findGrantRoleCodes(String userId) {
        StringBuffer sql = new StringBuffer("SELECT R.ROLE_CODE FROM PLAT_SYSTEM_ROLE R");
        sql.append(" WHERE R.ROLE_ID IN (SELECT UR.ROLE_ID FROM ");
        sql.append("PLAT_SYSTEM_SYSUSERROLE UR WHERE UR.SYSUSER_ID=? )");
        List<String> codes = this.getJdbcTemplate().queryForList(sql.toString(),
                new Object[]{userId}, String.class);
        return codes;
    }

    /**
     *
     */
    @Override
    public void updateSn(String[] roleIds) {
        int[] oldSns = new int[roleIds.length];
        StringBuffer sql = new StringBuffer("select ROLE_SN FROM PLAT_SYSTEM_ROLE ").append(" WHERE ROLE_ID=? ");
        for (int i = 0; i < roleIds.length; i++) {
            int dicSn = this.getIntBySql(sql.toString(), new Object[]{roleIds[i]});
            oldSns[i] = dicSn;
        }
        int[] newSns = PlatCollectionUtil.sortByDesc(oldSns);
        StringBuffer updateSql = new StringBuffer("update PLAT_SYSTEM_ROLE ")
                .append(" SET ROLE_SN=? WHERE ROLE_ID=? ");
        for (int i = 0; i < roleIds.length; i++) {
            getJdbcTemplate().update(updateSql.toString(), new Object[]{newSns[i], roleIds[i]});
        }

    }
}
