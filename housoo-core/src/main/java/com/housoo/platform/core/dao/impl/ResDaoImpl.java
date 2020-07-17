package com.housoo.platform.core.dao.impl;

import com.housoo.platform.core.util.PlatDbUtil;
import com.housoo.platform.core.dao.ResDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 描述系统资源业务相关dao实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-05 17:12:34
 */
@Repository
public class ResDaoImpl extends BaseDaoImpl implements ResDao {

    /**
     * 根据用户ID获取用户被授权的资源KEY集合
     *
     * @param userId
     * @return
     */
    @Override
    public Set<String> getUserGrantResCodes(String userId) {
        String sql = PlatDbUtil.getDiskSqlContent("system/res/001", null);
        List<String> resCodeList = this.getJdbcTemplate()
                .queryForList(sql.toString(),
                        new Object[]{"PLAT_SYSTEM_RES", userId, userId, "PLAT_SYSTEM_SYSUSER",
                                userId, "PLAT_SYSTEM_USERGROUP"}, String.class);
        if (resCodeList != null && resCodeList.size() > 0) {
            return new HashSet<String>(resCodeList);
        } else {
            return null;
        }
    }

    /**
     * 根据资源编码获取被分配的用户ID列表
     *
     * @param resCode
     * @return
     */
    @Override
    public List<String> findGrantedUserIds(String resCode) {
        List<String> userIds = new ArrayList<String>();
        String sql = PlatDbUtil.getDiskSqlContent("system/res/004", null);
        List<String> ids1 = this.getJdbcTemplate().queryForList(sql, new Object[]{resCode}, String.class);
        if (ids1 != null && ids1.size() > 0) {
            userIds.addAll(ids1);
        }
        sql = PlatDbUtil.getDiskSqlContent("system/res/005", null);
        List<String> ids2 = this.getJdbcTemplate().queryForList(sql, new Object[]{resCode}, String.class);
        if (ids2 != null && ids2.size() > 0) {
            userIds.addAll(ids2);
        }
        sql = PlatDbUtil.getDiskSqlContent("system/res/006", null);
        List<String> ids3 = this.getJdbcTemplate().queryForList(sql, new Object[]{resCode}, String.class);
        if (ids3 != null && ids3.size() > 0) {
            userIds.addAll(ids3);
        }
        return userIds;
    }
}
