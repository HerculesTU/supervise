package com.housoo.platform.core.service;

import java.util.List;
import java.util.Map;

/**
 * 描述 行组件配置业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-06-08 15:47:00
 */
public interface PortalRowConfService extends BaseService {
    /**
     * 默认颜色灰色
     */
    public static final String BORDERCOLOR_DEFAULT = "eui-bdcolor4";

    /**
     * 初始化行配置信息
     *
     * @param rowId
     * @param portalRow
     */
    public void initRowConfs(String rowId, Map<String, Object> portalRow);

    /**
     * 根据行ID获取数据列表
     *
     * @param rowId
     * @return
     */
    public List<Map<String, Object>> findByRowId(String rowId);

    /**
     * 获取组件配置数量
     *
     * @param rowId
     * @return
     */
    public int getCompConfCount(String rowId);

    /**
     * 更新组件的排序
     *
     * @param confId
     * @param sn
     */
    public void updateCompConfSn(String confId, int sn, String rowId);

    /**
     * 判断是否存在该配置
     *
     * @param THEME_ID    主题ID
     * @param CONF_ID     现有配置ID
     * @param CONF_COMPID 组件ID
     * @return
     */
    public boolean isExists(String THEME_ID, String CONF_ID, String CONF_COMPID);

    /**
     * 更新组件的URL
     *
     * @param compId
     * @param compUrl
     */
    public void updateCompUrl(String compId, String compUrl);

    /**
     * 更新组件为空
     *
     * @param compIds
     */
    public void updateCompToNull(String compIds);
}
