package com.housoo.platform.metadata.dao;

import com.housoo.platform.core.dao.BaseDao;

import java.util.List;

/**
 * 描述 数据资源信息业务相关dao
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-07 17:35:52
 */
public interface DataResDao extends BaseDao {
    /**
     * 根据资源ID获取目录ID列表
     *
     * @param resId
     * @return
     */
    public List<String> findCataLogIds(String resId);
}
