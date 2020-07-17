package com.housoo.platform.core.service;

import java.util.List;
import java.util.Map;

/**
 * 描述 门户主题业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-06-08 10:20:49
 */
public interface PortalThemeService extends BaseService {
    /**
     * 更新主题的名称和行JSON
     *
     * @param themeId
     * @param themeName
     * @param rowJson
     */
    public void updateThemeNameAndRowOrder(String themeId, String themeName, String rowJson, String isAdminDesign);

    /**
     * 根据创建人获取主题列表数据
     *
     * @param creatorId
     * @return
     */
    public List<Map<String, Object>> createAndfindByCreatorId(String creatorId);

    /**
     * 删除主题数据
     *
     * @param themeIds
     */
    public void deleteCascade(String themeIds);

    /**
     * @param themeId
     * @param themeName
     * @param rowJson
     */
    public void cloneForNewTheme(String themeId, String themeName, String rowJson);
}
