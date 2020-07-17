package com.housoo.platform.chatonline.service;

import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 描述 用户分组业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-07-13 10:59:23
 */
public interface UserGroupService extends BaseService {

    /**
     * 描述 保存群
     *
     * @return
     * @created 2017年7月16日 下午2:26:03
     */
    public Map<String, Object> saveOrUpdateGroup(Map<String, Object> userGroup);

    /**
     * 描述
     *
     * @param id
     * @return
     * @created 2017年7月16日 下午3:41:03
     */
    public Map<String, Object> getCreateMap(String id);

    /**
     * 描述
     *
     * @param id
     * @return
     * @created 2017年7月16日 下午3:50:02
     */
    public List<Map<String, Object>> findGroupMemberListMap(String id);

    /**
     * 描述
     *
     * @param groupId
     * @return
     * @created 2017年7月28日 上午11:41:40
     */
    public String getTreeJsonByGroupId(String groupId);

    /**
     * 描述
     *
     * @param sqlFilter
     * @return
     * @created 2017年7月28日 下午3:03:27
     */
    public List<Map<String, Object>> findAutoComplete(SqlFilter sqlFilter);

    /**
     * 描述
     *
     * @param userGroup
     * @return
     * @created 2017年7月28日 下午3:41:31
     */
    public Map<String, Object> assignmentGroup(Map<String, Object> userGroup);

}
