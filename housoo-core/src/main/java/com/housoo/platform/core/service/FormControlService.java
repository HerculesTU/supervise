package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import java.util.List;
import java.util.Map;

/**
 * 描述 FormControl业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-02-21 17:26:03
 */
public interface FormControlService extends BaseService {
    /**
     * 根据设计IDS删除表单控件
     *
     * @param designIds
     */
    public void deleteByDesignIds(String designIds);

    /**
     * 获取外部资源树形数据
     *
     * @return
     */
    public Map<String, Object> getExternalResTreeData(String needCheckIds);

    /**
     * 保存控件信息数据
     *
     * @param formControl
     */
    public void saveFormControl(Map<String, Object> formControl);

    /**
     * 根据表单控件ID级联删除字段配置信息
     *
     * @param formControlId
     */
    public void deleteCascadeFieldConfig(String formControlId, String childctrolIds);

    /**
     * 根据设计ID和父亲ID获取生成的代码和ID
     *
     * @param designId
     * @param parentId
     * @return
     */
    public List<Map<String, Object>> findGenCodeAndId(String designId, String parentId);

    /**
     * 获取关联的数据库列表
     *
     * @param sqlFilter
     * @return
     */
    public List<Map> findGridSqlConfigSubTables(SqlFilter sqlFilter);

    /**
     * 根据表单控件ID获取模版代码
     *
     * @param formControlId
     * @return
     */
    public String getTplCode(String formControlId);

    /**
     * 根据设计ID生成控件代码
     *
     * @param designId
     */
    public void updateCmpCode(String designId, String platDesignMode);

    /**
     * 根据设计ID获取控件列表
     *
     * @param designId
     * @return
     */
    public List<Map<String, Object>> findByDesignId(String designId);

    /**
     * 克隆表单控件信息
     *
     * @param sourceDesignId
     * @param newDesignId
     */
    public void copyFormControl(String sourceDesignId, String newDesignId);

    /**
     * 根据设计id和父亲ID获取配置的控件列表
     *
     * @param designId
     * @param parentId
     * @return
     */
    public List<Map<String, Object>> findList(String designId, String parentId);

    /**
     * 更新控件排序
     *
     * @param designId
     * @param dragTreeNodeId
     * @param dragTreeNodeNewLevel
     * @param targetNodeId
     * @param targetNodeLevel
     * @param moveType
     */
    public void updateFormControlSn(String designId, String dragTreeNodeId,
                                    int dragTreeNodeNewLevel, String targetNodeId, int targetNodeLevel,
                                    String targetPlatComId, String moveType);

}
