package com.housoo.platform.core.dao;

/**
 * 描述 门户行业务相关dao
 *
 * @author housoo
 * @version 1.0
 * @created 2017-06-08 15:07:52
 */
public interface PortalRowDao extends BaseDao {
    /**
     * 根据主题ID获取最大排序值
     *
     * @param themeId
     * @return
     */
    public int getMaxSn(String themeId);
}
