package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import java.util.List;
import java.util.Map;

/**
 * 描述 表格按钮业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-03-17 10:25:49
 */
public interface TableButtonService extends BaseService {
    /**
     * 根据sqlFilter获取数据记录列表
     *
     * @param sqlFilter
     * @return
     */
    public List<Map<String, Object>> findBySqlFilter(SqlFilter sqlFilter);

    /**
     * 获取下一个排序值
     *
     * @param formControlId
     * @return
     */
    public int getNextSn(String formControlId);

    /**
     * 更新排序值
     *
     * @param buttonIds
     */
    public void updateSn(String[] buttonIds);

    /**
     * 根据表单控件ID获取按钮列表
     *
     * @param formControlId
     * @return
     */
    public List<Map<String, Object>> findByFormControlId(String formControlId);

    /**
     * 根据表单控件ID级联删除子孙控件配置的查询条件
     *
     * @param targetCtrolIds
     */
    public void deleteCascadeByFormControlId(String targetCtrolIds);

    /**
     * 克隆配置的按钮信息
     *
     * @param sourceControlId
     * @param newControlId
     */
    public void copyTableButtons(String sourceControlId, String newControlId);

    /**
     * 根据设计ID获取列表数据
     *
     * @param designId
     * @return
     */
    public List<Map<String, Object>> findByDesignId(String designId);
}
