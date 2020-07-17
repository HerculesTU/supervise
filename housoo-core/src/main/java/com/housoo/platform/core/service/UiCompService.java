package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import java.util.List;
import java.util.Map;

/**
 * 描述 Uicomp业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-02-21 10:11:07
 */
public interface UiCompService extends BaseService {
    /**
     * 根据filter获取数据列表
     *
     * @param filter
     * @return
     */
    public List<Map<String, Object>> findBySqlFilter(SqlFilter filter);

    /**
     * 根据组件类别编码获取数据列表
     *
     * @param compTypeCode
     * @return
     */
    public List<Map<String, Object>> findByCompTypeCode(String compTypeCode);

    /**
     * 生产东西南北中布局代码
     *
     * @param paramsConfig
     * @return
     */
    public Map<String, String> genDirectorLayout(Map<String, Object> paramsConfig);

    /**
     * 生成bootstrapTAB控件代码
     *
     * @param paramsConfig
     * @return
     */
    public Map<String, String> genBootTabControlCode(Map<String, Object> paramsConfig);

    /**
     * 生成树形面板的代码
     *
     * @param paramsConfig
     * @return
     */
    public Map<String, String> genTreePanelControlCode(Map<String, Object> paramsConfig);

    /**
     * 生成JqGrid表格插件代码
     *
     * @param paramsConfig
     * @return
     */
    public Map<String, String> genJqgridCode(Map<String, Object> paramsConfig);

    /**
     * 生成JqTree表格插件代码
     *
     * @param paramsConfig
     * @return
     */
    public Map<String, String> genJqTreeControlCode(Map<String, Object> paramsConfig);

    /**
     * 生成通用类控件代码
     *
     * @param paramsConfig
     * @return
     */
    public Map<String, String> genFormControlCode(Map<String, Object> paramsConfig);

    /**
     * 生成网格类控件代码
     *
     * @param paramsConfig
     * @return
     */
    public Map<String, String> genGridItemControlCode(Map<String, Object> paramsConfig);

    /**
     * 生成输入框控件代码
     *
     * @param paramsConfig
     * @return
     */
    public Map<String, String> genInputCompCode(Map<String, Object> paramsConfig);

    /**
     * 生成按钮工具栏代码
     *
     * @param paramsConfig
     * @return
     */
    public Map<String, String> genButtonToolbarCompCode(Map<String, Object> paramsConfig);

    /**
     * 生成textarea的控件代码
     *
     * @param paramsConfig
     * @return
     */
    public Map<String, String> genTextareaCompCode(Map<String, Object> paramsConfig);

    /**
     * 生成可编辑表格的控件代码
     *
     * @param paramsConfig
     * @return
     */
    public Map<String, String> genEditTableCode(Map<String, Object> paramsConfig);

    /**
     * 生成可编辑表格行模版的代码
     *
     * @param formControlId
     */
    public void genEditTableTrCode(String formControlId, String designId);

    /**
     * 生成可操作列表组控件代码
     *
     * @param paramsConfig
     * @return
     */
    public Map<String, String> genOperListgroupControlCode(Map<String, Object> paramsConfig);

    /**
     * 生成向导控件代码
     *
     * @param paramsConfig
     * @return
     */
    public Map<String, String> genWizardCode(Map<String, Object> paramsConfig);

    /**
     * 生成Echart图形报表相关代码
     *
     * @param paramsConfig
     * @return
     */
    public Map<String, String> genEchartCode(Map<String, Object> paramsConfig);

}
