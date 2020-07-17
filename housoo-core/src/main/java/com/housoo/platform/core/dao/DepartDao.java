package com.housoo.platform.core.dao;

import java.util.List;

/**
 * 描述 部门业务相关dao
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-09 09:43:59
 */
public interface DepartDao extends BaseDao {
    /**
     * 根据单位ID和部门编码判断是否存在部门信息
     *
     * @param companyId
     * @param departCode
     * @return
     */
    public boolean isExistsDepart(String companyId, String departCode);

    /**
     * 根据DepartId获取Director
     *
     * @param depart_Id
     * @return
     */
    public List<String> findDirectorListByDepartId(String depart_Id);

    /**
     * 根据DepartId获取Taker
     *
     * @param departId
     * @return
     */
    List<String> findTakerListByDepartId(String departId);

    /**
     * 根据用户ID获取分管的部门
     *
     * @param userId
     * @return
     */
    List<String> findDepartListByUserId(String userId);
}
