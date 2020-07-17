package com.housoo.platform.core.service;

import java.util.Map;

/**
 * 描述 全局配置业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-09 13:52:03
 */
public interface GlobalConfigService extends BaseService {

    /**
     * 获取第一个配置
     *
     * @return Map
     */
    public Map<String, Object> getFirstConfigMap();

}
