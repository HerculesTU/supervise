package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import java.util.List;
import java.util.Map;

/**
 * 描述 GenCmpTpl业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-03-15 17:15:24
 */
public interface GenCmpTplService extends BaseService {
    /**
     * 根据sqlFilter
     *
     * @param sqlFilter
     * @return
     */
    public List<Map<String, Object>> findBySqlFilter(SqlFilter sqlFilter);

    /**
     * 根据模版类型获取可选JAVA接口列表
     *
     * @param tplType
     * @return
     */
    public List<Map<String, Object>> findSelectJavaInters(String tplType);

    /**
     * 根据模版类型获取button接口列表
     *
     * @param tplType
     * @return
     */
    public List<Map<String, Object>> findSelectButtons(String tplType);
}
