package com.housoo.platform.core.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述 系统日志业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-17 16:24:38
 */
public interface SysLogService extends BaseService {
    /**
     * 登录操作
     */
    public static final int OPER_TYPE_LOGIN = 1;
    /**
     * 登出操作
     */
    public static final int OPER_TYPE_LOGOFF = 2;
    /**
     * 新增操作
     */
    public static final int OPER_TYPE_ADD = 3;
    /**
     * 修改操作
     */
    public static final int OPER_TYPE_EDIT = 4;
    /**
     * 删除操作
     */
    public static final int OPER_TYPE_DEL = 5;
    /**
     * 上传操作
     */
    public static final int OPER_TYPE_UPLOAD = 6;
    /**
     * 其它操作
     */
    public static final int OPER_TYPE_OTHER = 7;
    /**
     * 授权操作
     */
    public static final int OPER_TYPE_GRANTRIGHTS = 8;

    /**
     * 保存后台系统日志信息
     *
     * @param moduleName
     * @param operType
     * @param logContent
     */
    public void saveBackLog(String moduleName, int operType, String logContent, HttpServletRequest request);

    /**
     * 操作类型转换接口
     *
     * @param dataValue
     * @param rowDatas
     * @return
     */
    public Object conventOperType(Object dataValue, List<Object> rowDatas);

    /**
     * 验证接口
     *
     * @param dataValue
     * @param rowDatas
     * @return
     */
    public String validDataValue(Object dataValue, List<Object> rowDatas);

    /**
     * 获取变更的字段列表
     *
     * @param formfieldModifyArray
     * @param busType
     * @return
     */
    public List<Map<String, Object>> getFieldList(List<Map> formfieldModifyArray, int busType);

    /**
     * 保存后台日志明细版
     *
     * @param moduleName
     * @param operType
     * @param logContent
     * @param request
     * @param formfieldModifyArray 修改的字段列表
     * @param delColNames          被删除的列名称
     * @param delColValues         被删除的列值
     */
    public void saveBackLog(String moduleName, int operType, String logContent,
                            HttpServletRequest request, List<Map> formfieldModifyArray,
                            String delColNames, List<List<String>> delColValues);

    /**
     * 删除系统日志
     *
     * @param request
     * @return
     */
    public Map<String, Object> deleteLog(HttpServletRequest request);

}
