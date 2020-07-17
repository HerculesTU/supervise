package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述 系统资源业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-05 17:12:34
 */
public interface ResService extends BaseService {
    /**
     * 根据filter获取资源所配置的URL
     *
     * @param filter
     * @return
     */
    public List<Map> findResUrlByFilter(SqlFilter filter);

    /**
     * 根据资源ID删除数据,并且级联删除相关联的数据信息
     *
     * @param resId
     */
    public void deleteAndCascadeAssoical(String resId);

    /**
     * 获取下拉资源树数据源
     *
     * @param params
     * @return
     */
    public List<Map<String, Object>> findTreeSelectRes(String params);

    /**
     * 根据用户ID获取用户被授权的资源KEY集合
     *
     * @param userId
     * @return
     */
    public Set<String> getUserGrantResCodeSet(String userId);

    /**
     * 根据用户ID获取用户被授权的资源编码
     *
     * @param userId
     * @return
     */
    public String getUserGrantResCodes(String userId);

    /**
     * 根据用户ID获取被授权的资源列表
     *
     * @param userId
     * @return
     */
    public List<Map<String, Object>> findGrantResList(String userId, String subsyscode);

    /**
     * 根据用户ID获取
     *
     * @param userId
     * @return
     */
    public Set<String> getUserGrantResUrlSet(String userId, String grantCodes);

    /**
     * 获取全部的资源地址集合
     *
     * @return
     */
    public Set<String> getAllResUrl();

    /**
     * 获取可点击菜单类型的资源
     *
     * @param paramsJson
     * @return
     */
    public List<Map<String, Object>> findUrlList(String paramsJson);

    /**
     * 新增更新资源信息
     *
     * @param resInfo
     * @return
     */
    public Map<String, Object> saveUpdateResInfo(Map<String, Object> resInfo);

    /**
     * 根据资源编码获取被分配的用户ID列表
     *
     * @param resCode
     * @return
     */
    public List<String> findGrantedUserIds(String resCode);

    /**
     * 根据父级资源ID和资源状态获取子级资源
     *
     * @param parentResId
     * @param resStatus
     * @return
     */
    public List<Map<String, Object>> getChildRes(String parentResId, String resStatus);
}
