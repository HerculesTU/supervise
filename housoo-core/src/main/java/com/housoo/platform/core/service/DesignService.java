package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author 高飞
 * @created 2017年2月4日 上午11:22:24
 */
public interface DesignService extends BaseService {
    /**
     * 根据sqlfilter获取到列表数据
     *
     * @param sqlFilter
     * @return
     */
    public List<Map<String, Object>> findBySqlFilter(SqlFilter sqlFilter);

    /**
     * 获取关联的数据库列表
     *
     * @param sqlFilter
     * @return
     */
    public List<Map> findAssocialTables(SqlFilter sqlFilter);

    /**
     * 保存设计信息并且生成JAVA源代码
     *
     * @param design
     * @return
     */
    public Map<String, Object> saveAndGenJavaCode(Map<String, Object> design);

    /**
     * 根据设计IDS删除设计信息集合
     *
     * @param designIds
     */
    public void deleteCascadeFormControl(String designIds);

    /**
     * 根据设计ID获取代码
     *
     * @param designId
     * @return
     */
    public String getDesignCode(String designId, String platDesignMode);

    /**
     * 根据设计ID获取关联的表信息
     *
     * @param designId
     * @return
     */
    public List<Map> findAssoicalTables(String designId);

    /**
     * 获取可选的表字段信息
     *
     * @param designId
     * @return
     */
    public List<Map<String, Object>> findSelectFields(String designId);

    /**
     * 根据设计id删除生成的代码
     *
     * @param designId
     */
    public void deleteGenCodeByDesignId(String designId);

    /**
     * 更新组件模版为最新,并且重新生成组件源码
     *
     * @param designIds
     */
    public void updateNewestTpl(String designIds);

    /**
     * 更新组件代码
     *
     * @param designIds
     */
    public void updateGenUiCode(String designIds);

    /**
     * 获取生成的JSP代码
     *
     * @param design
     * @param appPath
     * @return
     */
    public String saveGenJspCode(Map<String, Object> design, String appPath);

    /**
     * 克隆一个设计UI信息
     *
     * @param sourceDesignId
     * @param newDesignCode
     * @param newDesignName
     * @param newDesignModuleId
     */
    public void copyDesignInfo(String sourceDesignId,
                               String newDesignCode, String newDesignName, String newDesignModuleId);

    /**
     * 根据设计ID或者编码生成文件的路径
     *
     * @param designIdOrCode:设计ID或者编码
     * @param fileName:产生的文件名称
     * @param isDesignId:是否设计ID
     * @return
     */
    public String getGenPathByDesignIdOrCode(String designIdOrCode, String fileName
            , boolean isDesignId, boolean isSubFile);

    /**
     * 获取可选的下拉设计数据
     *
     * @param params
     * @return
     */
    public List<Map<String, Object>> findForSelect(String params);

    /**
     * 根据设计编码获取所配置的表
     *
     * @param designCode
     * @return
     */
    public List<Map> findAssocialTablesByDesignCdoe(String designCode);

    /**
     * 获取设计配置信息
     *
     * @param designIds
     * @return
     */
    public Map<String, Object> getExportInfo(String designIds);

    /**
     * 根据JSON导入配置信息
     *
     * @param confJson
     */
    public void importConfig(String confJson);

    /**
     * 修正父亲ID和组件ID不一致的情况
     *
     * @param designId
     */
    public void updateParentCompIdNotSame(String designId);
}
