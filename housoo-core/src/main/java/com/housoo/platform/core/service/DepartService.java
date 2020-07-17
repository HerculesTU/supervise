package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述 部门业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-09 09:43:59
 */
public interface DepartService extends BaseService {
    /**
     * 根据单位ID和部门编码判断是否存在部门信息
     *
     * @param companyId
     * @param departCode
     * @return
     */
    public boolean isExistsDepart(String companyId, String departCode);

    /**
     * 根据单位ID获取下拉树数据
     *
     * @param companyId
     * @return
     */
    public List<Map<String, Object>> findSelectTree(String companyId);

    /**
     * 获取部门和用户的JSON字符串
     *
     * @param params
     * @return
     */
    public String getDepartAndUserJson(Map<String, Object> params);

    /**
     * 获取自动补全的部门和用户的数据源
     *
     * @param sqlFilter
     * @return
     */
    public List<Map<String, Object>> findAutoDepartUser(SqlFilter sqlFilter);

    /**
     * 删除部门并且级联删除用户
     *
     * @param departId
     */
    public void deleteDepartCascadeUser(String departId);

    /**
     * 新增或者修改部门信息
     *
     * @param request
     * @param postParams
     * @return
     */
    public Map<String, Object> saveOrUpdateDepart(HttpServletRequest request,
                                                  Map<String, Object> postParams);

    /**
     * 删除部门
     *
     * @param request
     * @param postParams
     * @return
     */
    public Map<String, Object> delDepart(HttpServletRequest request,
                                         Map<String, Object> postParams);

    /**
     * 根据filter获取网格项目
     *
     * @param sqlFilter
     * @return
     */
    public List<Map<String, Object>> findGridItemList(SqlFilter sqlFilter);

    /**
     * 新增或者修改部门接口
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveOrUpdateDep(HttpServletRequest request);

    /**
     * 删除部门接口
     *
     * @param request
     * @return
     */
    public Map<String, Object> deleteDepartCascadeUser(HttpServletRequest request);

    /**
     * 保存选择的用户的部门ID
     *
     * @param dEPART_ID
     * @param checkUserIds
     */
    public void saveUsers(String dEPART_ID, String checkUserIds);

    /**
     * @param params
     * @return
     */
    public String getDepartAndUserAndAllJson(Map<String, Object> params);

    /**
     * 分配用户接口
     *
     * @param request
     * @return
     */
    public Map<String, Object> grantUsers(HttpServletRequest request);

    /**
     * 保存部门主任信息
     *
     * @param departId
     * @param directorId
     * @param directorName
     */
    public void saveDepartDirector(String departId, String directorId, String directorName);

    /**
     * 根据DepartId获取Director
     *
     * @param departId
     * @return
     */
    List<String> findDirectorListByDepartId(String departId);

    /**
     * 部门主任
     * 根据filter获取用户网格项目
     *
     * @param sqlFilter
     * @return
     */
    public List<Map<String, Object>> findDeptDirectorList(SqlFilter sqlFilter);

    /**
     * 保存部门承办人信息
     *
     * @param departId
     * @param takerId
     * @param takerName
     */
    void saveDepartTaker(String departId, String takerId, String takerName);

    /**
     * 根据DepartId获取Taker
     *
     * @param departId
     * @return
     */
    List<String> findTakerListByDepartId(String departId);

    /**
     * 部门承办人
     * 根据filter获取用户网格项目
     *
     * @param sqlFilter
     * @return
     */
    public List<Map<String, Object>> findDeptTakerList(SqlFilter sqlFilter);

    /**
     * 根据用户ID获取分管的部门
     *
     * @param userId
     * @return
     */
    List<String> findDepartListByUserId(String userId);


}
