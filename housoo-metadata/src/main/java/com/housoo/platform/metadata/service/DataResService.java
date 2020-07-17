package com.housoo.platform.metadata.service;

import com.housoo.platform.core.model.PagingBean;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述 数据资源信息业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-07 17:35:52
 */
public interface DataResService extends BaseService {
    /**
     * 获取数据源列表
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> findDbConnList(String param);

    /**
     * 获取配置的返回结果列表
     * 返回字段结果JSON(FIELD_NAME:字段名称 FIELD_DESC:字段描述)
     *
     * @param sqlFilter
     * @return
     */
    public List<Map> findReturnFields(SqlFilter sqlFilter);

    /**
     * 保存目录资源中间表
     *
     * @param resId
     * @param catalogIds
     */
    public void saveOrUpdateResCatalog(String resId, String catalogIds);

    /**
     * 根据filter和配置信息获取数据列表
     *
     * @param filter
     * @param fieldInfo
     * @return
     */
    public List<Map<String, Object>> findList(SqlFilter filter, Map<String, Object> fieldInfo);

    /**
     * 获取中间表关联的目录信息
     *
     * @param resId
     * @return
     */
    public Map<String, String> getCatalogInfo(String resId);

    /**
     * 获取资源列表
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> findSelect(String param);

    /**
     * 调用资源
     *
     * @param request
     * @param serviceInfo
     * @param dockInfo
     * @return
     * @throws Exception
     */
    public Map<String, Object> invokeRes(HttpServletRequest request, Map<String, Object> serviceInfo
            , Map<String, Object> dockInfo) throws Exception;

    /**
     * 根据动态SQL查找
     *
     * @param sql
     * @param params
     * @param pb
     * @return
     */
    public List<Map<String, Object>> findByDynaSql(String sql, List<Object> params, PagingBean pb);

    /**
     * 级联删除相关信息
     *
     * @param resIds
     */
    public void deleteCascade(String resIds);

    /**
     * 调用资源
     *
     * @param request
     * @param resId
     * @return
     * @throws Exception
     */
    public Map<String, Object> invokeRes(HttpServletRequest request, String resId) throws Exception;
}
