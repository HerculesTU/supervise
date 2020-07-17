package com.housoo.platform.core.service;

import java.util.List;
import java.util.Map;

/**
 * 描述 系统日志备份业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-10 16:24:38
 */
public interface SysLogBkService extends BaseService {
    /**
     * 复制日志
     *
     * @param paramMap
     */
    public void copyLog(Map<String, Object> paramMap);

    /**
     * 获取日志数据
     *
     * @param paramMap
     * @return
     */
    public List<Map<String, Object>> findExportData(Map<String, Object> paramMap);

    /**
     * @param sysLogBk
     */
    public void backLogData(Map<String, Object> sysLogBk) throws Exception;

    /**
     * @param excelFilePath
     * @param type
     * @return
     */
    public Map<String, Object> impExcelDatas(String excelFilePath, String type);
}
