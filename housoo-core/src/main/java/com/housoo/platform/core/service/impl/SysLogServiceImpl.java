package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.BrowserUtils;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.dao.SysLogDao;
import com.housoo.platform.core.service.SecAuditService;
import com.housoo.platform.core.service.SysLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 描述 系统日志业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-17 16:24:38
 */
@Service("sysLogService")
public class SysLogServiceImpl extends BaseServiceImpl implements SysLogService {

    /**
     * 所引入的dao
     */
    @Resource
    private SysLogDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 保存后台系统日志信息
     *
     * @param moduleName
     * @param operType
     * @param logContent
     */
    @Override
    public void saveBackLog(String moduleName, int operType, String logContent, HttpServletRequest request) {
        Map<String, Object> sysLog = getBaseLogInfo(moduleName, operType,
                logContent, request);
        sysLog.put("DETAILABLE", -1);
        dao.saveOrUpdate("PLAT_SYSTEM_SYSLOG", sysLog, SysConstants.ID_GENERATOR_UUID, null);
    }


    /**
     * @param moduleName
     * @param operType
     * @param logContent
     * @param request
     * @return
     */
    private Map<String, Object> getBaseLogInfo(String moduleName, int operType,
                                               String logContent, HttpServletRequest request) {
        Map<String, Object> sysLog = new HashMap<String, Object>();
        String browser = BrowserUtils.checkBrowse(request);
        String DOSYSLOG_USERID = request.getParameter("DOSYSLOG_USERID");
        Map<String, Object> curUser = null;
        if (StringUtils.isNotEmpty(DOSYSLOG_USERID)) {
            curUser = this.getRecord("PLAT_SYSTEM_SYSUSER", new String[]{"SYSUSER_ID"},
                    new Object[]{DOSYSLOG_USERID});
        } else {
            curUser = PlatAppUtil.getBackPlatLoginUser();
        }
        sysLog.put("BROWSER", browser);
        sysLog.put("OPER_TYPE", operType);
        if (curUser != null) {
            String LOG_TYPE = "1";
            String userAccount = (String) curUser.get("SYSUSER_ACCOUNT");
            if ("admin".equals(userAccount) || "sysadmin".equals(userAccount)) {
                LOG_TYPE = "2";
            } else if ("aqbmadmin".equals(userAccount)) {
                LOG_TYPE = "3";
            } else if ("aqsjy".equals(userAccount)) {
                LOG_TYPE = "4";
            }
            sysLog.put("OPER_USERNAME", curUser.get("SYSUSER_NAME"));
            sysLog.put("OPER_USERACCOUNT", curUser.get("SYSUSER_ACCOUNT"));
            sysLog.put("LOG_TYPE", LOG_TYPE);
        }
        sysLog.put("IP_ADDRESS", BrowserUtils.getIpAddr(request));
        sysLog.put("LOG_CONTENT", logContent);
        sysLog.put("OPER_MODULENAME", moduleName);
        sysLog.put("OPER_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return sysLog;
    }

    /**
     * 操作类型转换接口
     *
     * @param dataValue
     * @param rowDatas
     * @return
     */
    @Override
    public Object conventOperType(Object dataValue, List<Object> rowDatas) {
        String value = dataValue.toString();
        if ("登录操作".equals(value)) {
            return "1";
        } else if ("登出操作".equals(value)) {
            return "2";
        } else {
            return "3";
        }
    }

    /**
     * 验证接口
     *
     * @param dataValue
     * @param rowDatas
     * @return
     */
    @Override
    public String validDataValue(Object dataValue, List<Object> rowDatas) {
        return "必须是有效身份证!";
    }

    /**
     * 获取变更的字段列表
     *
     * @param formfieldModifyArray
     * @param busType
     * @return
     */
    @Override
    public List<Map<String, Object>> getFieldList(List<Map> formfieldModifyArray, int busType) {
        List<Map<String, Object>> fieldList = new ArrayList<Map<String, Object>>();
        for (Map fields : formfieldModifyArray) {
            String FIELD_CN = (String) fields.get("FIELD_CN");
            String FIELD_EN = (String) fields.get("FIELD_EN");
            String FIELD_NEWVALUE = (String) fields.get("FIELD_NEWVALUE");
            String FIELD_OLDVALUE = (String) fields.get("FIELD_OLDVALUE");
            if (busType == SecAuditService.BUSTYPE_ADD) {
                FIELD_OLDVALUE = FIELD_NEWVALUE;
            }
            if (StringUtils.isNotEmpty(FIELD_CN) && StringUtils.
                    isNotEmpty(FIELD_EN) && StringUtils.isNotEmpty(FIELD_NEWVALUE)) {
                FIELD_NEWVALUE = FIELD_NEWVALUE.trim();
                if (StringUtils.isNotEmpty(FIELD_OLDVALUE)) {
                    FIELD_OLDVALUE = FIELD_OLDVALUE.trim();
                }
                Map<String, Object> secField = new HashMap<String, Object>();
                secField.put("SECFIELD_VAL", FIELD_OLDVALUE);
                secField.put("SECFIELD_UPVAL", FIELD_NEWVALUE);
                if (FIELD_NEWVALUE.equals(FIELD_OLDVALUE)) {
                    secField.put("SECFIELD_ISUPADE", "-1");
                } else {
                    secField.put("SECFIELD_ISUPADE", "1");
                }
                secField.put("SECFIELD_TYPE", "1");
                secField.put("SECFIELD_CN", FIELD_CN);
                fieldList.add(secField);
            }
        }
        return fieldList;
    }

    /**
     * 保存后台系统日志信息
     *
     * @param moduleName
     * @param operType
     * @param logContent
     */
    public void saveBackLog(String moduleName, int operType, String logContent,
                            HttpServletRequest request, Map<String, Object> logDetailMap) {

    }

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
    @Override
    public void saveBackLog(String moduleName, int operType, String logContent,
                            HttpServletRequest request, List<Map> formfieldModifyArray,
                            String delColNames, List<List<String>> delColValues) {
        Map<String, Object> logDetailMap = new HashMap<String, Object>();
        int busType = -1;
        switch (operType) {
            case SysLogService.OPER_TYPE_ADD:
                busType = 1;
                break;
            case SysLogService.OPER_TYPE_EDIT:
                busType = 2;
                break;
            default:
                break;
        }
        if (formfieldModifyArray != null && formfieldModifyArray.size() > 0) {
            List<Map<String, Object>> fieldList = this.getFieldList(
                    formfieldModifyArray, busType);
            logDetailMap.put("FIELD_JSONLIST", fieldList);
        }
        if (StringUtils.isNotEmpty(delColNames) && delColValues.size() > 0) {
            logDetailMap.put("DEL_COLNAMES", delColNames);
            logDetailMap.put("DEL_COLJSON", delColValues);
        }
        Map<String, Object> sysLog = getBaseLogInfo(moduleName, operType,
                logContent, request);
        sysLog.put("DETAILABLE", 1);
        sysLog.put("LOG_DETAIL", JSON.toJSONString(logDetailMap));
        dao.saveOrUpdate("PLAT_SYSTEM_SYSLOG", sysLog, SysConstants.ID_GENERATOR_UUID, null);
    }

    /**
     * 删除系统日志
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> deleteLog(HttpServletRequest request) {
        String selectColValues = request.getParameter("selectColValues");
        this.deleteRecords("PLAT_SYSTEM_SYSLOG", "LOG_ID", selectColValues.split(","));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }


}
