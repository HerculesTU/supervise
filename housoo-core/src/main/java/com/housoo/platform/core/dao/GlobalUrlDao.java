package com.housoo.platform.core.dao;

import java.util.List;

/**
 * 描述 全局URL业务相关dao
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-23 09:22:54
 */
public interface GlobalUrlDao extends BaseDao {
    /**
     * 根据过滤类型获取全局URL列表
     *
     * @param filterType
     * @return
     */
    public List<String> findByFilterType(String filterType);
}
