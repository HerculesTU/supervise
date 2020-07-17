package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import java.util.List;
import java.util.Map;

/**
 * 描述 excel导入配置业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-30 15:20:47
 */
public interface ExcelImpService extends BaseService {
    /**
     * 保存配置,级联附件
     *
     * @param excelImp
     * @return
     */
    public Map<String, Object> saveCascadeFileAttach(Map<String, Object> excelImp);

    /**
     * 获取列配置列表
     *
     * @param sqlFilter
     * @return
     */
    public List<Map> findColConfList(SqlFilter sqlFilter);

    /**
     * 更新列配置JSON
     *
     * @param impId
     * @param columnJson
     */
    public void updateColumnJson(String impId, String columnJson);

    /**
     * 导入EXCEL数据
     *
     * @param filePath
     * @param impCode
     * @return
     */
    public Map<String, Object> impExcelDatas(String filePath, String impCode);

    /**
     * 批量导入单表数据
     *
     * @param targetDatas
     * @param tableName
     */
    public void importExcelDatas(List<Map<String, Object>> targetDatas, String tableName);
}
