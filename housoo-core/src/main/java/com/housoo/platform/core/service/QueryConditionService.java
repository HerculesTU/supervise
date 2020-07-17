package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import java.util.List;
import java.util.Map;

/**
 * 描述 QueryCondition业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-03-14 10:55:40
 */
public interface QueryConditionService extends BaseService {
    /**
     * 根据filter获取配置的查询条件列表
     *
     * @param filter
     * @return
     */
    public List<Map<String, Object>> findBySqlFilter(SqlFilter filter);

    /**
     * 根据表单控件ID获取排序条件的下一个值
     *
     * @param formControlId
     * @return
     */
    public int getNextSn(String formControlId);

    /**
     * 更新排序字段
     *
     * @param conditionIds
     */
    public void updateSn(String[] conditionIds);

    /**
     * 根据表单控件ID获取查询条件列表
     *
     * @param formControlId
     * @return
     */
    public List<Map<String, Object>> findByFormControlId(String formControlId);

    /**
     * 根据表单控件ID级联删除子孙控件配置的查询条件
     *
     * @param targetControlIds
     */
    public void deleteCascadeByFormControlId(String targetControlIds);

    /**
     * 克隆查询条件配置
     *
     * @param sourceControlId
     * @param newControlId
     */
    public void copyQuesConditions(String sourceControlId, String newControlId);

    /**
     * 根据设计ID获取列表数据
     *
     * @param designId
     * @return
     */
    public List<Map<String, Object>> findByDesignId(String designId);
}
