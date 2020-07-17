package com.housoo.platform.metadata.service;

import com.housoo.platform.core.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 描述 输入参数业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-08 11:11:34
 */
public interface QueryService extends BaseService {
    /**
     * 根据资源ID获取参数列表
     *
     * @param resId
     * @return
     */
    public List<Map<String, Object>> findByResId(String resId);

    /**
     * 判断参数是否存在
     *
     * @param resId
     * @param cn
     * @param en
     * @return
     */
    public boolean isExistsQuery(String resId, String cn, String en);
}
