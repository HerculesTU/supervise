package com.housoo.platform.metadata.service;

import com.housoo.platform.core.service.BaseService;

import java.util.List;

/**
 * 描述 数据目录业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-07 10:53:23
 */
public interface DataCatalogService extends BaseService {
    /**
     * 根据目录ID级联删除子孙目录
     *
     * @param catalogId
     */
    public void deleteCascadeChild(String catalogId);

    /**
     * 根据资源ID获取目录id集合
     *
     * @param resId
     * @return
     */
    public List<String> findCatalogIds(String resId);
}
