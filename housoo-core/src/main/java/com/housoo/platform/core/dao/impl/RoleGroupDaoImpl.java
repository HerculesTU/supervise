package com.housoo.platform.core.dao.impl;

import com.housoo.platform.core.util.PlatDbUtil;
import com.housoo.platform.core.dao.RoleGroupDao;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 描述角色组业务相关dao实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-15 15:20:40
 */
@Repository
public class RoleGroupDaoImpl extends BaseDaoImpl implements RoleGroupDao {

    /**
     * 根据用户ID
     *
     * @param userId
     * @return
     */
    @Override
    public Set<String> getUserGrantGroupIds(String userId) {
        String sql = PlatDbUtil.getDiskSqlContent("system/rolegroup/001", null);
        List<String> resCodeList = this.getJdbcTemplate()
                .queryForList(sql.toString(), new Object[]{"PLAT_SYSTEM_ROLEGROUP",
                        userId, userId, "PLAT_SYSTEM_SYSUSER",
                        userId, "PLAT_SYSTEM_USERGROUP"}, String.class);
        if (resCodeList != null && resCodeList.size() > 0) {
            return new HashSet<String>(resCodeList);
        } else {
            return null;
        }
    }
}
