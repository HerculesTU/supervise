package com.housoo.platform.core.service;

import java.util.List;
import java.util.Map;

/**
 * 描述 FieldConfig业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-03-02 10:54:42
 */
public interface FieldConfigService extends BaseService {
    /**
     * 根据表单控件ID删除配置数据
     *
     * @param formControlId
     */
    public void deleteByFormControlId(String formControlId);

    /**
     * 根据表单控件ID获取字段配置数据
     *
     * @param formControlId
     * @return
     */
    public Map<String, Object> getFieldMapInfo(String formControlId);

    /**
     * 根据表单控件ID获取字段信息列表
     *
     * @param formControlId
     * @return
     */
    public List<Map<String, Object>> findByFormControlId(String formControlId);

    /**
     * 保存字段配置
     *
     * @param fieldConfig
     * @param confType    配置类型(base:基本配置 attach:附加配置)
     */
    public void saveFieldConfigAfterDel(Map<String, Object> fieldConfig, String confType);

    /**
     * 根据表单控件ID和字段名称获取字段值
     *
     * @param formControlId
     * @param fieldName
     * @return
     */
    public String getFieldValue(String formControlId, String fieldName);

    /**
     * 克隆控件配置信息数据
     *
     * @param sourceControlId
     * @param newControlId
     */
    public void copyFieldConfigs(String sourceControlId, String newControlId);

    /**
     * 根据设计ID获取字段配置列表
     *
     * @param designId
     * @return
     */
    public List<Map<String, Object>> findByDesignId(String designId);

}
