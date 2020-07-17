package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.util.*;
import com.housoo.platform.core.dao.SysLogBkDao;
import com.housoo.platform.core.service.SysLogBkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 系统日志备份业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-10 16:24:38
 */
@Service("sysLogBkService")
public class SysLogBkServiceImpl extends BaseServiceImpl implements SysLogBkService {
    /**
     *
     */
    @Resource
    private SysLogBkDao dao;

    /**
     *
     */
    @Override
    protected BaseDao getDao() {
        return this.dao;
    }

    /**
     *
     */
    @Override
    public void copyLog(Map sysLogBk) {
        String type = (String) sysLogBk.get("TYPE");
        Map timeLimit = getTimeLimit(sysLogBk);
        String beginTime = (String) timeLimit.get("beginTime");
        String endTime = (String) timeLimit.get("endTime");
        StringBuffer delSql = new StringBuffer("");
        delSql.append(" DELETE FROM PLAT_SYSTEM_SYSLOG_BK WHERE LOG_ID IN ( ");
        delSql.append(" SELECT LOG_ID  FROM PLAT_SYSTEM_SYSLOG WHERE OPER_TIME>=? AND OPER_TIME<=? ");
        delSql.append(" AND LOG_TYPE=? ) ");
        this.dao.executeSql(delSql.toString(), new Object[]{beginTime, endTime, type});
        StringBuffer insertSql = new StringBuffer("");
        insertSql.append(" INSERT INTO PLAT_SYSTEM_SYSLOG_BK ");
        insertSql.append(" SELECT *  FROM PLAT_SYSTEM_SYSLOG WHERE OPER_TIME>=? AND OPER_TIME<=? AND LOG_TYPE=? ");
        this.dao.executeSql(insertSql.toString(), new Object[]{beginTime, endTime, type});
    }

    /**
     * @param sysLogBk
     * @return
     */
    private Map<String, Object> getTimeLimit(Map sysLogBk) {
        Map timeLimit = new HashMap();
        String TIMETYPE = (String) sysLogBk.get("COPY_TIMETYPE");
        String YEAR = (String) sysLogBk.get("YEAR");
        String MONTH = (String) sysLogBk.get("MONTH");
        String QUARTER = (String) sysLogBk.get("QUARTER");
        String beginTime = "";
        String endTime = "";
        if ("1".equals(TIMETYPE)) {
            beginTime = YEAR + "-" + PlatStringUtil.getFormatNumber(2, MONTH) + "-01";
            endTime = YEAR + "-" + PlatStringUtil.getFormatNumber(2, MONTH) + "-31";
        } else if ("2".equals(TIMETYPE)) {
            beginTime = YEAR + "-" + PlatStringUtil.getFormatNumber(2, new StringBuilder(String.valueOf((Integer.parseInt(QUARTER) - 1) * 3 + 1)).toString()) + "-01";
            endTime = YEAR + "-" + PlatStringUtil.getFormatNumber(2, new StringBuilder(String.valueOf(Integer.parseInt(QUARTER) * 3)).toString()) + "-31";
        } else if ("3".equals(TIMETYPE)) {
            beginTime = YEAR + "-01-01";
            endTime = YEAR + "-12-31";
        }
        timeLimit.put("beginTime", beginTime + " 00:00:00");
        timeLimit.put("endTime", endTime + " 23:59:59");
        return timeLimit;
    }

    /**
     *
     */
    @Override
    public List findExportData(Map sysLogBk) {
        String type = (String) sysLogBk.get("TYPE");
        Map timeLimit = getTimeLimit(sysLogBk);
        String beginTime = (String) timeLimit.get("beginTime");
        String endTime = (String) timeLimit.get("endTime");
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT T.LOG_ID,T.BROWSER,T.OPER_TIME,T.OPER_USERNAME,T.OPER_USERACCOUNT,T.IP_ADDRESS,T.OPER_MODULENAME,D.DIC_NAME AS OPER_TYPE,T.LOG_CONTENT  FROM PLAT_SYSTEM_SYSLOG T  LEFT JOIN PLAT_SYSTEM_DICTIONARY D ON T.OPER_TYPE=D.DIC_VALUE WHERE D.DIC_DICTYPE_CODE='OPER_TYPE' AND T.OPER_TIME>=? AND T.OPER_TIME<=? AND T.LOG_TYPE=? ");

        return this.dao.findBySql(sql.toString(), new Object[]{beginTime, endTime, type}, null);
    }

    /**
     *
     */
    @Override
    public void backLogData(Map sysLogBk) throws Exception {
        String type = (String) sysLogBk.get("TYPE");
        String typename = "";
        if ("1".equals(type)) {
            typename = "普通用户";
        } else if ("2".equals(type)) {
            typename = "系统管理员";
        } else if ("3".equals(type)) {
            typename = "安全保密管理员";
        } else if ("4".equals(type)) {
            typename = "安全审计员";
        }
        String beginTime = (String) sysLogBk.get("BEGIN_TIME") + " 00:00:00";
        String endTime = (String) sysLogBk.get("END_TIME") + " 23:59:59";
        StringBuffer delSql = new StringBuffer("");
        delSql.append(" DELETE FROM PLAT_SYSTEM_SYSLOG_BK WHERE LOG_ID IN ( ");
        delSql.append(" SELECT LOG_ID  FROM PLAT_SYSTEM_SYSLOG WHERE OPER_TIME>=? AND OPER_TIME<=? ");
        delSql.append(" AND LOG_TYPE=? ) ");
        this.dao.executeSql(delSql.toString(), new Object[]{beginTime, endTime, type});
        StringBuffer insertSql = new StringBuffer("");
        insertSql.append(" INSERT INTO PLAT_SYSTEM_SYSLOG_BK ");
        insertSql.append(" SELECT *  FROM PLAT_SYSTEM_SYSLOG WHERE OPER_TIME>=? AND OPER_TIME<=? AND LOG_TYPE=? ");
        this.dao.executeSql(insertSql.toString(), new Object[]{beginTime, endTime, type});
        StringBuffer updateSql = new StringBuffer("");
        updateSql.append(" UPDATE PLAT_SYSTEM_SYSLOG SET IS_DEL='1' WHERE  ");
        updateSql.append(" OPER_TIME>=? AND OPER_TIME<=? ");
        updateSql.append(" AND LOG_TYPE=? ");
        this.dao.executeSql(updateSql.toString(), new Object[]{beginTime, endTime, type});
        StringBuffer selectSql = new StringBuffer("");
        selectSql.append(" SELECT LOG_ID  FROM PLAT_SYSTEM_SYSLOG WHERE  ");
        selectSql.append(" OPER_TIME>=? AND OPER_TIME<=? ");
        selectSql.append(" AND LOG_TYPE=?  ");
        List<Map<String, Object>> list = dao.findBySql(selectSql.toString(),
                new Object[]{beginTime, endTime, type}, null);
        String filePath = this.creatFile(list);
        Map<String, Object> backMap = new HashMap<String, Object>();
        String curTime = PlatDateTimeUtil.formatDate(new Date(),
                "yyyy-MM-dd HH:mm:ss");
        backMap.put("BKINFO_TIME", curTime);
        String username = (String) PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_NAME");
        backMap.put("BKINFO_USERNAME", PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ACCOUNT"));
        backMap.put("BKINFO_FULLNAME", username);
        backMap.put("BKINFO_FILEPATH", filePath);
        backMap.put("BKINFO_CONTENT", "用户" + username + "在" + curTime + "备份"
                + sysLogBk.get("BEGIN_TIME").toString() + "至" + sysLogBk.get("END_TIME") + "的" + typename + "的操作日志");
        dao.saveOrUpdate("PLAT_SYSTEM_SYSLOG_BKINFO", backMap,
                SysConstants.ID_GENERATOR_UUID, null);

    }

    /**
     * @param list
     * @return
     */
    public String creatFile(List<Map<String, Object>> list) throws Exception {
        String data = JSON.toJSONString(list);
        String uploadRootFolder = "sysLogBk";
        String attachFilePath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        File file = new File(attachFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String currentDate = PlatDateTimeUtil.formatDate(new Date(), "YYYY-MM-dd");
        StringBuffer uploadFileFolderPath = new StringBuffer(attachFilePath);
        uploadFileFolderPath.append(uploadRootFolder).append("/").append(currentDate).append("/");
        StringBuffer dbFilePath = new StringBuffer(uploadRootFolder).append("/").append(currentDate).append("/");
        String fileName = UUIDGenerator.getUUID() + ".json";
        String enfileName = UUIDGenerator.getUUID() + ".housoobk";
        String realPath = uploadFileFolderPath.toString() + fileName;
        String enrealPath = uploadFileFolderPath.toString() + enfileName;
        PlatFileUtil.writeDataToDisk(data,
                uploadFileFolderPath.append(fileName).toString()
                , "UTF-8");
        PlatFileUtil.encrypt(realPath,
                enrealPath, PlatFileUtil.ENCRYPT_KEY);
        return dbFilePath.append(enfileName).toString();
    }

    /**
     *
     */
    @Override
    public Map<String, Object> impExcelDatas(String excelFilePath, String type) {
        Map<String, Object> result = new HashMap<String, Object>();
        String key = PlatFileUtil.readFileLastByte(excelFilePath, PlatFileUtil.ENCRYPT_KEY.length());
        if (PlatFileUtil.ENCRYPT_KEY.equals(key)) {
            try {
                String trgUrl = excelFilePath.substring(0, excelFilePath.lastIndexOf("/"))
                        + UUIDGenerator.getUUID() + ".json";
                PlatFileUtil.decrypt(excelFilePath, trgUrl, PlatFileUtil.ENCRYPT_KEY.length());
                String json = PlatFileUtil.readFileString(trgUrl, "UTF-8");
                List<Map> list = JSON.parseArray(json, Map.class);
                if (list != null && list.size() > 0) {
                    StringBuffer inLogIds = new StringBuffer("");
                    for (int i = 0; i < list.size(); i++) {
                        inLogIds.append((String) list.get(i).get("LOG_ID") + ",");
                    }
                    String LOG_ID = (String) list.get(0).get("LOG_ID");
                    Map<String, Object> sysLogBk = this.dao.getRecord("PLAT_SYSTEM_SYSLOG_BK",
                            new String[]{"LOG_ID"}, new Object[]{LOG_ID});
                    if (sysLogBk != null) {
                        String LOG_TYPE = (String) sysLogBk.get("LOG_TYPE");
                        if (type.equals(LOG_TYPE)) {
                            StringBuffer delSql = new StringBuffer("");
                            delSql.append(" DELETE FROM PLAT_SYSTEM_SYSLOG WHERE LOG_ID IN ")
                                    .append(PlatStringUtil.getValueArray(inLogIds.toString()));
                            dao.executeSql(delSql.toString(), null);
                            StringBuffer insertSql = new StringBuffer("");
                            insertSql.append(" INSERT INTO PLAT_SYSTEM_SYSLOG ");
                            insertSql.append(" SELECT *  FROM PLAT_SYSTEM_SYSLOG_BK WHERE LOG_ID IN ")
                                    .append(PlatStringUtil.getValueArray(inLogIds.toString()));
                            this.dao.executeSql(insertSql.toString(), null);
                            result.put("success", true);
                            result.put("msg", "导入成功！");
                        } else {
                            result.put("success", false);
                            result.put("msg", "导入失败,导入的备份文件和当前该导入的列表类型不一致!");
                        }
                    }
                }
            } catch (Exception e) {
                result.put("success", false);
                result.put("msg", "导入失败!");
                PlatLogUtil.printStackTrace(e);
            }

        } else {
            result.put("success", false);
            result.put("msg", "导入失败，该文件不为系统产生的加密文件!");
        }
        return result;
    }
}