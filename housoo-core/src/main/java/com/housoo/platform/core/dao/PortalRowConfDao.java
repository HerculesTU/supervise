package com.housoo.platform.core.dao;

/**
 * 描述 行组件配置业务相关dao
 *
 * @author housoo
 * @version 1.0
 * @created 2017-06-08 15:47:00
 */
public interface PortalRowConfDao extends BaseDao {
    /**
     * 获取组件配置数量
     *
     * @param rowId
     * @return
     */
    public int getCompConfCount(String rowId);

    /**
     * 判断是否存在该配置
     *
     * @param THEME_ID    主题ID
     * @param CONF_ID     现有配置ID
     * @param CONF_COMPID 组件ID
     * @return
     */
    public boolean isExists(String THEME_ID, String CONF_ID, String CONF_COMPID);
}
