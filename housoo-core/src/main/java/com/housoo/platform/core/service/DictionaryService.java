package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author 高飞
 * @created 2017年2月1日 下午6:02:18
 */
public interface DictionaryService extends BaseService {
    /**
     * 根据sqlfilter获取到列表数据
     *
     * @param sqlFilter
     * @return
     */
    public List<Map<String, Object>> findByList(SqlFilter sqlFilter, Map<String, Object> fieldInfo);

    /**
     * 根据类别编码获取最大排序
     *
     * @param typeCode
     * @return
     */
    public int getMaxSn(String typeCode);

    /**
     * 判断一个字典值是否存在
     *
     * @param dicId
     * @param typeCode
     * @param dicValue
     * @return
     */
    public boolean isExistsDic(String dicId, String typeCode, String dicValue);

    /**
     * 描述 更新排序字段
     *
     * @param dicIds
     * @author housoo
     * @created 2014年10月3日 下午12:54:23
     */
    public void updateSn(String[] dicIds);

    /**
     * 描述 根据查询参数JSON获取数据列表
     *
     * @param queryParamsJson
     * @return
     * @created 2016年3月27日 上午11:16:25
     */
    public List<Map<String, Object>> findList(String queryParamsJson);

    /**
     * 获取UI类别列表数据
     *
     * @param queryParamJson
     * @return
     */
    public List<Map<String, Object>> findUiTypeList(String queryParamJson);

    /**
     * 获取资源库列表数据
     *
     * @param queryParamJson
     * @return
     */
    public List<Map<String, Object>> findResLibaryList(String queryParamJson);

    /**
     * 初始化全国街道数据
     */
    public void initCountryStree();

    /**
     * 获取字典数据根据类别编码和排序方式
     *
     * @param typeCode
     * @param orderType
     * @return
     */
    public List<Map<String, Object>> findList(String typeCode, String orderType);

    /**
     * 获取客户端验证规则
     *
     * @return
     */
    public Map<String, String[]> getJsValidRules();

    /**
     * 根据类别和名称获取值
     *
     * @return
     */
    public Map<String, Object> getDicValue(String dicTypeCode, String dicName);

    /**
     * 获取字典的附加值
     *
     * @param dicValue
     * @param dicTypeCode
     * @param attachKey
     * @return
     */
    public String getAttachValue(String dicValue, String dicTypeCode, String attachKey);

    /**
     * 获取字典的值
     *
     * @param typeCode
     * @param dicName
     * @return
     */
    public String getDictionaryValue(String typeCode, String dicName);

    /**
     * 获取行政区划列表数据
     *
     * @param params
     * @return
     */
    public List<Map<String, Object>> findAdminDivision(String params);
}
