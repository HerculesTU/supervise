package com.housoo.platform.core.service;

import java.util.List;
import java.util.Map;

/**
 * 描述 部署节点列表业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2018-04-19 16:15:13
 */
public interface DeployNodeService extends BaseService {
    /**
     * 获取部署节点列表
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> findList(String param);
}
