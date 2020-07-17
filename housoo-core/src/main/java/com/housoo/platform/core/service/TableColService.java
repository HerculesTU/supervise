package com.housoo.platform.core.service;

import java.util.List;
import java.util.Map;

/**
 * 描述 表格列业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-03-30 15:16:45
 */
public interface TableColService extends BaseService {

    /**
     * 根据表单控件ID获取下一个排序值
     *
     * @param formControlId
     * @return
     */
    public int getNextSn(String formControlId);

    /**
     * 更新配置列的排序
     *
     * @param tableColIds
     */
    public void updateSn(String[] tableColIds);

    /**
     * 根据表单控件ID获取列表数据
     *
     * @param formControlId
     * @return
     */
    public List<Map<String, Object>> findByFormControlId(String formControlId);

    /**
     * 克隆配置的表格列信息
     *
     * @param sourceControlId
     * @param newControlId
     */
    public void copyTableCols(String sourceControlId, String newControlId);

    /**
     * 根据设计ID获取列表数据
     *
     * @param designId
     * @return
     */
    public List<Map<String, Object>> findByDesignId(String designId);
}
