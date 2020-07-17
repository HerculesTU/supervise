package com.housoo.platform.core.service;


import com.housoo.platform.core.model.SqlFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述 保存用户分管部门业务相关service
 *
 * @author wh
 * @version 1.0
 * @created 2019-05-06 15:29:55
 */
public interface UserDepartService extends BaseService {

    /**
     * 根据filter获取网格项目
     *
     * @param sqlFilter
     * @return
     */
    public List<Map<String, Object>> findGridItemList(SqlFilter sqlFilter);

    /**
     * 保存用户分管部门信息
     * @param userId
     * @param departId
     */
    public void saveDepart(String userId, String departId);

    /**
     * 根据部门Id获取分管领导信息
     *
     * @param departId
     * @return
     */
    List<Map<String, Object>> getDepartLeader(String departId);

    /**
     * 保存用户分管部门信息（安全审核回调接口）
     *
     * @param request
     * @return
     */
    Map<String, Object> saveUserDepart(HttpServletRequest request);

}
