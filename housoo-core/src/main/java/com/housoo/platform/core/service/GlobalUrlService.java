package com.housoo.platform.core.service;

import java.util.List;
import java.util.Set;

/**
 * 描述 全局URL业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-23 09:22:54
 */
public interface GlobalUrlService extends BaseService {
    /**
     * 根据过滤类型获取全局URL列表
     *
     * @param filterType
     * @return
     */
    public List<String> findByFilterType(String filterType);

    /**
     * 获取全部的可匿名访问的URL
     *
     * @return
     */
    public Set<String> getAllAnonUrl();
}
