package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author 高飞
 * @created 2017年3月12日 下午2:08:57
 */
public interface CommonUIService extends BaseService {
    /**
     * 获取配置的布局
     *
     * @param sqlFilter
     * @return
     */
    public List<Map> findDirectLayouts(SqlFilter sqlFilter);

    /**
     * 获取配置的列信息
     *
     * @param sqlFilter
     * @return
     */
    public List<Map> findDatatableColumns(SqlFilter sqlFilter);

    /**
     * 获取配置的排序列信息
     *
     * @param sqlFilter
     * @return
     */
    public List<Map> findDatatableOrders(SqlFilter sqlFilter);

    /**
     * 保存数据表格基本配置信息
     *
     * @param sqlFilter
     * @param fieldInfo
     */
    public void saveDataTableBaseConfig(SqlFilter sqlFilter, Map<String, Object> fieldInfo);

    /**
     * 保存 jq树形表格基本配置信息
     *
     * @param sqlFilter
     * @param fieldInfo
     */
    public void saveJqTreeTableBaseConfig(SqlFilter sqlFilter, Map<String, Object> fieldInfo);

    /**
     * 根据表单控件ID和通用TPLID获取按钮的模版代码
     *
     * @param FORMCONTROL_ID
     * @param GENCMPTPL_ID
     * @return
     */
    public String getJqgridBtnTplCode(String FORMCONTROL_ID, String GENCMPTPL_ID);

    /**
     * 获取表格的数据列表
     *
     * @param filter
     * @param IS_PAGE
     * @param formControlId
     * @return
     */
    public List<Map<String, Object>> findTableDatas(SqlFilter filter, String IS_PAGE, String formControlId);

    /**
     * 通用的获取树形下拉框数据源列表
     *
     * @param paramsConfig 参数配置
     *                     参数例子:TABLE_NAME指的是表名,TREE_IDANDNAMECOL指的是作为下拉树形的ID和NAME的，
     *                     TREE_QUERYFIELDS指的是额外需要查询的列表,FILTERS指的是作为过滤的查询条件
     *                     [TABLE_NAME:PLAT_APPMODEL_MODULE],[TREE_IDANDNAMECOL:MODULE_ID,MODULE_NAME],
     *                     [TREE_QUERYFIELDS:MODULE_CODE,MODULE_PARENTID],[FILTERS:MODULE_NAME_LIKE|测试,MODULE_TREESN_EQ|3]
     * @return
     */
    public List<Map<String, Object>> findGenTreeSelectorDatas(String paramsConfig);

    /**
     * 通用的获取树形下拉框数据源列表
     *
     * @param paramsConfig 参数配置
     *                     参数例子:TABLE_NAME指的是表名,TREE_IDANDNAMECOL指的是作为下拉树形的ID和NAME的，
     *                     TREE_QUERYFIELDS指的是额外需要查询的列表,FILTERS指的是作为过滤的查询条件
     *                     [TABLE_NAME:PLAT_APPMODEL_MODULE],[TREE_IDANDNAMECOL:MODULE_ID,MODULE_NAME],
     *                     [TREE_QUERYFIELDS:MODULE_CODE,MODULE_PARENTID],[FILTERS:MODULE_NAME_LIKE|测试,MODULE_TREESN_EQ|3]
     * @return
     * @author zxl
     * @date 2020-04-14
     */
    List<Map<String, Object>> findCompanyAndDeptSelectTree(String paramsConfig);

    /**
     * 根据sqlFilter获取页签配置信息列表
     *
     * @param sqlFilter
     * @return
     */
    public List<Map> findTabConfigList(SqlFilter sqlFilter);

    /**
     * 获取配置的事件信息
     *
     * @param sqlFilter
     * @return
     */
    public List<Map> findListGroupEvents(SqlFilter sqlFilter);

    /**
     * 根据sqlFilter获取步骤配置列表
     *
     * @param sqlFilter
     * @return
     */
    public List<Map> findWizardConfigList(SqlFilter sqlFilter);

}
