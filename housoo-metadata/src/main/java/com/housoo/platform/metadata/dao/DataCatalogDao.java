package com.housoo.platform.metadata.dao;

import com.housoo.platform.core.dao.BaseDao;

import java.util.List;

/**
 * 描述 数据目录业务相关dao
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-07 10:53:23
 */
public interface DataCatalogDao extends BaseDao {
    /**
     * 根据资源ID获取目录id集合
     *
     * @param resId
     * @return
     */
    public List<String> findCatalogIds(String resId);
}
