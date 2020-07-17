package com.housoo.platform.common.service;

import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.BaseService;

import java.util.List;
import java.util.Map;


/**
 * 描述
 *
 * @author 高飞
 * @created 2017年2月1日 上午10:24:31
 */
public interface CommonService extends BaseService {
    /**
     * 判断记录是否存在
     *
     * @param validTableName
     * @param validFieldName
     * @param validFieldValue
     * @return
     */
    public boolean isExistsRecord(String validTableName, String validFieldName,
                                  String validFieldValue, String RECORD_ID);

    /**
     * 获取自动补全的通用数据源
     *
     * @param sqlFilter
     * @return
     */
    public List<Map<String, Object>> findAutoCompleteDatas(SqlFilter sqlFilter);

    /**
     * 获取可编辑表格的测试数据
     *
     * @param sqlFilter
     * @return
     */
    public List<Map<String, Object>> findEditTableDatas(SqlFilter sqlFilter);

    /**
     * 获取所有的表主键集合
     *
     * @return
     */
    public Map<String, List<String>> getAllPkNames();

    /**
     * 获取所有的表字段集合
     *
     * @return
     */
    public Map<String, List<String>> getAllTableColumnNames();

    /**
     * 通用提交请求地址
     *
     * @param params
     * @return
     */
    public List<Map<String, Object>> findCommonUrlList(String params);

    /**
     * 根据节点级联删除数据
     *
     * @param treeNodeId
     * @param tableName
     */
    public void deleteTreeDataCascadeChild(String treeNodeId, String tableName);
}
