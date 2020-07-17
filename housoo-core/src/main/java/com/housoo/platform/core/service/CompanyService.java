package com.housoo.platform.core.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 描述 单位业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-09 09:43:59
 */
public interface CompanyService extends BaseService {
    /**
     * 根据单位ID级联删除相关的数据库表信息
     *
     * @param companyId
     */
    public void deleteCompanyCacasdeAssocial(String companyId);

    /**
     * 新增或者修改单位信息表
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveOrUpdateCompany(HttpServletRequest request);

    /**
     * 删除单位信息
     *
     * @param request
     * @return
     */
    public Map<String, Object> delCompany(HttpServletRequest request);
}
