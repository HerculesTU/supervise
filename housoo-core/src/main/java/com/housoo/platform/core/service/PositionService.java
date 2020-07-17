package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述 岗位业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-12 15:52:01
 */
public interface PositionService extends BaseService {

    /**
     * 获取获得用户信息的前缀SQL
     *
     * @return
     */
    public String getUserInfoPreSql();

    /**
     * 保存用户岗位中间表
     *
     * @param positionId
     * @param userIds
     */
    public void saveUsers(String positionId, String[] userIds);

    /**
     * 获取已经选择的服务记录
     *
     * @param filter
     * @return
     */
    public List<Map<String, Object>> findSelected(SqlFilter filter);

    /**
     * 获取用户所在岗位信息
     *
     * @param userId
     * @return
     */
    public Map<String, String> getUserPositionInfo(String userId);

    /**
     * 分配用户接口
     *
     * @param request
     * @return
     */
    public Map<String, Object> grantUsers(HttpServletRequest request);

    /**
     * 删除数据
     *
     * @param request
     * @return
     */
    public Map<String, Object> deletePos(HttpServletRequest request);

    /**
     * 保存数据
     *
     * @param request
     * @return
     */
    public Map<String, Object> saveOrUpdatePos(HttpServletRequest request);
}
