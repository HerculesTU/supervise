package com.housoo.platform.core.dao.impl;

import com.housoo.platform.core.dao.DepartDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 描述部门业务相关dao实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-09 09:43:59
 */
@Repository
public class DepartDaoImpl extends BaseDaoImpl implements DepartDao {

    /**
     * 根据单位ID和部门编码判断是否存在部门信息
     *
     * @param companyId
     * @param departCode
     * @return
     */
    @Override
    public boolean isExistsDepart(String companyId, String departCode) {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
        sql.append("PLAT_SYSTEM_DEPART D WHERE D.DEPART_COMPANYID=? ");
        sql.append(" AND D.DEPART_CODE=?");
        int count = this.getIntBySql(sql.toString(), new Object[]{companyId, departCode});
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据DepartId获取Director
     *
     * @param departId
     * @return
     */
    @Override
    public List<String> findDirectorListByDepartId(String departId) {
        List<String> result = new ArrayList<>();
        Map<String, Object> depart = this.getRecord("plat_system_depart", new String[]{"DEPART_ID"}, new Object[]{departId});
        if (depart != null && depart.get("PRINCIPAL_USER1") != null) {
            String PRINCIPAL_USER1 = (String) depart.get("PRINCIPAL_USER1");
            result = Arrays.asList(PRINCIPAL_USER1.split(","));
        }
        return result;
    }

    /**
     * 根据DepartId获取Taker
     *
     * @param departId
     * @return
     */
    @Override
    public List<String> findTakerListByDepartId(String departId) {
        List<String> result = new ArrayList<>();
        Map<String, Object> depart = this.getRecord("plat_system_depart", new String[]{"DEPART_ID"}, new Object[]{departId});
        if (depart != null && depart.get("UNDERTAKE_USER") != null) {
            String UNDERTAKE_USER = (String) depart.get("UNDERTAKE_USER");
            result = Arrays.asList(UNDERTAKE_USER.split(","));
        }
        return result;
    }

    /**
     * 根据用户ID获取分管的部门
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> findDepartListByUserId(String userId) {
        StringBuffer sql = new StringBuffer("select T.DEPART_ID");
        sql.append(" FROM tb_user_depart T WHERE T.SYSUSER_ID=? ");
        return this.getJdbcTemplate().queryForList(sql.toString(), new Object[]{userId},
                String.class);
    }

}
