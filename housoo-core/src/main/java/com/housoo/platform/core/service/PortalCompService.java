package com.housoo.platform.core.service;

import java.util.List;
import java.util.Map;

/**
 * 描述 门户组件业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-06-12 14:39:45
 */
public interface PortalCompService extends BaseService {
    /**
     * 根据类别编码获取列表数据
     *
     * @param typeCode
     * @return
     */
    public List<Map<String, Object>> findByCompTypeCode(String typeCode);
}
