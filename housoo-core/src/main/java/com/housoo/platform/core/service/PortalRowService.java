package com.housoo.platform.core.service;

import java.util.List;
import java.util.Map;

/**
 * 描述 门户行业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-06-08 15:07:52
 */
public interface PortalRowService extends BaseService {
    /**
     * 根据主题ID获取下个排序值
     *
     * @param themeId
     * @return
     */
    public int getNextSn(String themeId);

    /**
     * 保存行并且级联保存配置
     *
     * @param variables
     * @return
     */
    public Map<String, Object> saveCascadeRowConf(Map<String, Object> variables);

    /**
     * 根据主题ID获取数据列表
     *
     * @param themeId
     * @return
     */
    public List<Map<String, Object>> findByThemeId(String themeId);

    /**
     * 根据行ID级联删除配置
     *
     * @param rowId
     */
    public void deleteCascadeConfg(String rowId);

    /**
     * 更新排序值
     *
     * @param rowId
     * @param rowSn
     */
    public void updateSn(String rowId, int rowSn);

    /**
     * 克隆行数据
     *
     * @param sourceThemeId
     * @param newThemeId
     */
    public void copyRows(String sourceThemeId, String newThemeId);

}
