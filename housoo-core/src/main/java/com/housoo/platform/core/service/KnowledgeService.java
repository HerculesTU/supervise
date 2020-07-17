package com.housoo.platform.core.service;

import java.util.Map;

/**
 * 描述 技术知识业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-06-29 09:57:28
 */
public interface KnowledgeService extends BaseService {
    /**
     * 保存或者更新并且级联索引
     *
     * @param knowledage
     * @return
     */
    public Map<String, Object> saveOrUpdateCascadeIndex(Map<String, Object> knowledage);

    /**
     * 删除并且级联删除索引
     *
     * @param knowledageIds
     */
    public void deleteCascadeIndex(String[] knowledageIds);
}
