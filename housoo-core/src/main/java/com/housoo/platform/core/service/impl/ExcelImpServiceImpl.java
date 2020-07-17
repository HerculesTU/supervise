package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.model.TableColumn;
import com.housoo.platform.core.dao.ExcelImpDao;
import com.housoo.platform.core.service.DbManagerService;
import com.housoo.platform.core.service.ExcelImpService;
import com.housoo.platform.core.service.FileAttachService;
import com.housoo.platform.core.service.DictionaryService;
import com.housoo.platform.core.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 excel导入配置业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-30 15:20:47
 */
@Service("excelImpService")
public class ExcelImpServiceImpl extends BaseServiceImpl implements ExcelImpService {

    /**
     * 所引入的dao
     */
    @Resource
    private ExcelImpDao dao;
    /**
     *
     */
    @Resource
    private FileAttachService fileAttachService;
    /**
     *
     */
    @Resource
    private DictionaryService dictionaryService;
    /**
     *
     */
    @Resource
    private DbManagerService dbManagerService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 保存配置,级联附件
     *
     * @param excelImp
     * @return
     */
    @Override
    public Map<String, Object> saveCascadeFileAttach(Map<String, Object> excelImp) {
        String TPL_FILES_JSON = (String) excelImp.get("TPL_FILES_JSON");
        String oldImpId = (String) excelImp.get("EXCELIMP_ID");
        if (StringUtils.isEmpty(oldImpId)) {
            List<Map> fileList = JSON.parseArray(TPL_FILES_JSON, Map.class);
            //获取文件存储路径
            String attachFilePath = PlatPropUtil.getPropertyValue("config.properties"
                    , "attachFilePath");
            int EXCELIMP_HEADNUM = Integer.parseInt(excelImp.get("EXCELIMP_HEADNUM").toString());
            String excelTplFilePath = null;
            for (Map file : fileList) {
                String dbfilepath = (String) file.get("dbfilepath");
                excelTplFilePath = attachFilePath + dbfilepath;
                break;
            }
            List<List<Object>> list = PlatOfficeUtil.readExcelRowValues(excelTplFilePath, EXCELIMP_HEADNUM, 0, 0);
            List<Object> headList = list.get(0);
            List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < headList.size(); i++) {
                Map<String, Object> column = new HashMap<String, Object>();
                column.put("COLUMN_NUM", i);
                column.put("ALLOW_BLANK", "false");
                column.put("IS_HIDE", "false");
                column.put("COL_NAME", headList.get(i));
                columns.add(column);
            }
            String EXCELIMP_COLUMNJSON = JSON.toJSONString(columns);
            excelImp.put("EXCELIMP_COLUMNJSON", EXCELIMP_COLUMNJSON);
        }
        excelImp = dao.saveOrUpdate("PLAT_APPMODEL_EXCELIMP",
                excelImp, SysConstants.ID_GENERATOR_UUID, null);
        String EXCELIMP_ID = (String) excelImp.get("EXCELIMP_ID");
        fileAttachService.saveFileAttachs(TPL_FILES_JSON, "PLAT_APPMODEL_EXCELIMP", EXCELIMP_ID, null);
        return excelImp;
    }

    /**
     * 获取列配置列表
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map> findColConfList(SqlFilter sqlFilter) {
        String EXCELIMP_ID = sqlFilter.getRequest().getParameter("EXCELIMP_ID");
        if (StringUtils.isNotEmpty(EXCELIMP_ID)) {
            Map<String, Object> excelImp = dao.getRecord("PLAT_APPMODEL_EXCELIMP",
                    new String[]{"EXCELIMP_ID"}, new Object[]{EXCELIMP_ID});
            String EXCELIMP_COLUMNJSON = (String) excelImp.get("EXCELIMP_COLUMNJSON");
            if (StringUtils.isNotEmpty(EXCELIMP_COLUMNJSON)) {
                return JSON.parseArray(EXCELIMP_COLUMNJSON, Map.class);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 更新列配置JSON
     *
     * @param impId
     * @param columnJson
     */
    @Override
    public void updateColumnJson(String impId, String columnJson) {
        StringBuffer sql = new StringBuffer("UPDATE PLAT_APPMODEL_EXCELIMP ");
        sql.append(" SET EXCELIMP_COLUMNJSON=? WHERE EXCELIMP_ID=?");
        dao.executeSql(sql.toString(), new Object[]{columnJson, impId});
    }

    /**
     * 获取单元数据转换值
     *
     * @param TRANS_INTER
     * @param dataValue
     * @param rowData
     * @return
     */
    public Object getTransValue(String TRANS_INTER, Object dataValue, List<Object> rowData) {
        if (StringUtils.isNotEmpty(TRANS_INTER)) {
            String beanId = TRANS_INTER.split("[.]")[0];
            String method = TRANS_INTER.split("[.]")[1];
            Object serviceBean = PlatAppUtil.getBean(beanId);
            if (serviceBean != null) {
                Method invokeMethod;
                try {
                    invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                            new Class[]{Object.class, List.class});
                    dataValue = (Object) invokeMethod.invoke(serviceBean,
                            new Object[]{dataValue, rowData});
                    return dataValue;
                } catch (Exception e) {
                    PlatLogUtil.printStackTrace(e);
                }
            }
            return null;
        } else {
            return dataValue;
        }
    }

    /**
     * 获取验证接口结果
     *
     * @param VALID_INTER
     * @param dataValue
     * @param rowData
     * @return
     */
    public String getDataValidMsg(String VALID_INTER, Object dataValue, List<Object> rowData) {
        String erromsg = null;
        if (StringUtils.isNotEmpty(VALID_INTER)) {
            String beanId = VALID_INTER.split("[.]")[0];
            String method = VALID_INTER.split("[.]")[1];
            Object serviceBean = PlatAppUtil.getBean(beanId);
            if (serviceBean != null) {
                Method invokeMethod;
                try {
                    invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                            new Class[]{Object.class, List.class});
                    erromsg = (String) invokeMethod.invoke(serviceBean,
                            new Object[]{dataValue, rowData});
                    return erromsg;
                } catch (Exception e) {
                    PlatLogUtil.printStackTrace(e);
                }
            }
            return null;
        } else {
            return erromsg;
        }
    }

    /**
     * 获取验证规则集合
     *
     * @return
     */
    private Map<String, String> getValidRule() {
        //构建验证规则集合
        List<Map<String, Object>> dataRules = dictionaryService.findList("javaValidRule", "ASC");
        Map<String, String> dataRuleMap = new HashMap<String, String>();
        for (Map<String, Object> dataRule : dataRules) {
            dataRuleMap.put(dataRule.get("DIC_VALUE").toString(), dataRule.get("DIC_NAME").toString());
        }
        return dataRuleMap;
    }

    /**
     * 获取表字段长度集合
     *
     * @param tableName
     * @return
     */
    private Map<String, Integer> getTableColumnLength(String tableName) {
        List<TableColumn> columns = dao.
                findTableColumnByTableName(tableName);
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (TableColumn column : columns) {
            map.put(column.getColumnName(), Integer.parseInt(column.getDataLength()));
        }
        return map;
    }

    /**
     * 批量导入单表数据
     *
     * @param targetDatas
     * @param tableName
     */
    @Override
    public void importExcelDatas(List<Map<String, Object>> targetDatas, String tableName) {
        String pkName = dao.findPrimaryKeyNames(tableName).get(0);
        for (Map<String, Object> targetData : targetDatas) {
            targetData.put(pkName, UUIDGenerator.getUUID());
        }
        dao.saveBatch(targetDatas, tableName);
    }

    /**
     * 导入EXCEL数据
     *
     * @param filePath
     * @param impCode
     * @return
     */
    @Override
    public Map<String, Object> impExcelDatas(String filePath, String impCode) {
        Map<String, Object> result = new HashMap<String, Object>();
        //获取配置信息
        Map<String, Object> impConf = dao.getRecord("PLAT_APPMODEL_EXCELIMP",
                new String[]{"EXCELIMP_CODE"}, new Object[]{impCode});
        //获取配置的表名称
        String EXCELIMP_TABLENAME = (String) impConf.get("EXCELIMP_TABLENAME");
        //获取表头所在行
        int EXCELIMP_HEADNUM = Integer.parseInt(impConf.get("EXCELIMP_HEADNUM").toString());
        String EXCELIMP_COLUMNJSON = (String) impConf.get("EXCELIMP_COLUMNJSON");
        List<Map> colList = JSON.parseArray(EXCELIMP_COLUMNJSON, Map.class);
        ////设置隐藏列的值
        List<Map<String, Object>> hiddenConfList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> col : colList) {
            String IS_HIDE = (String) col.get("IS_HIDE");
            if ("true".equals(IS_HIDE)) {
                hiddenConfList.add(col);
            }
        }
        //定义列配置映射
        Map<Integer, Map<String, Object>> columnMap = new HashMap<Integer, Map<String, Object>>();
        for (Map<String, Object> col : colList) {
            int COLUMN_NUM = Integer.parseInt(col.get("COLUMN_NUM").toString());
            columnMap.put(COLUMN_NUM, col);
        }
        //定义验证规则集合
        Map<String, String> ruleMap = this.getValidRule();
        //定义表字段长度集合
        Map<String, Integer> tableLength = this.getTableColumnLength(EXCELIMP_TABLENAME);
        //读取excel的数据
        List<List<Object>> excelDatas = PlatOfficeUtil.readExcelRowValues(filePath, EXCELIMP_HEADNUM + 1, 0, 0);
        //构建最终得到的导入数据列表
        List<Map<String, Object>> targetDatas = new ArrayList<Map<String, Object>>();
        StringBuffer errorMsg = new StringBuffer("");
        //设置显示列的值
        getTargetDatas(EXCELIMP_HEADNUM, columnMap, ruleMap, tableLength,
                excelDatas, targetDatas, errorMsg, hiddenConfList);
        if (errorMsg.length() > 2) {
            result.put("success", false);
            result.put("msg", errorMsg.toString());
        } else {
            String EXCELIMP_INTERFACE = (String) impConf.get("EXCELIMP_INTERFACE");
            String beanId = EXCELIMP_INTERFACE.split("[.]")[0];
            String method = EXCELIMP_INTERFACE.split("[.]")[1];
            Object serviceBean = PlatAppUtil.getBean(beanId);
            if (serviceBean != null) {
                Method invokeMethod;
                try {
                    invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                            new Class[]{List.class, String.class});
                    invokeMethod.invoke(serviceBean,
                            new Object[]{targetDatas, EXCELIMP_TABLENAME});
                } catch (Exception e) {
                    PlatLogUtil.printStackTrace(e);
                }
            }
            result.put("success", true);
        }
        return result;
    }

    /**
     * @param EXCELIMP_HEADNUM
     * @param columnMap
     * @param ruleMap
     * @param tableLength
     * @param excelDatas
     * @param targetDatas
     * @param errorMsg
     */
    private void getTargetDatas(int EXCELIMP_HEADNUM,
                                Map<Integer, Map<String, Object>> columnMap,
                                Map<String, String> ruleMap, Map<String, Integer> tableLength,
                                List<List<Object>> excelDatas,
                                List<Map<String, Object>> targetDatas,
                                StringBuffer errorMsg, List<Map<String, Object>> hiddenConfList) {
        for (int rowNum = 0; rowNum < excelDatas.size(); rowNum++) {
            List<Object> data = excelDatas.get(rowNum);
            Map<String, Object> targetData = new HashMap<String, Object>();
            for (int i = 0; i < data.size(); i++) {
                Map<String, Object> colConfig = columnMap.get(i);
                if (colConfig != null) {
                    //获取字段名称
                    String FIELD_NAME = (String) colConfig.get("FIELD_NAME");
                    //获取列中文名
                    String COL_NAME = (String) colConfig.get("COL_NAME");
                    String TRANS_INTER = (String) colConfig.get("TRANS_INTER");
                    String IS_HIDE = (String) colConfig.get("IS_HIDE");
                    //定义目标的值
                    Object dataValue = null;
                    if ("true".equals(IS_HIDE)) {
                        String HIDE_VALUE = (String) colConfig.get("HIDE_VALUE");
                        dataValue = HIDE_VALUE;
                    }
                    if (dataValue != null) {
                        dataValue = this.getTransValue(TRANS_INTER, dataValue, data);
                    } else {
                        dataValue = this.getTransValue(TRANS_INTER, data.get(i), data);
                    }
                    String singleErrorMsg = getValidErrorMsg(EXCELIMP_HEADNUM, ruleMap,
                            rowNum, data, colConfig, COL_NAME, IS_HIDE,
                            dataValue, tableLength);
                    if (StringUtils.isNotEmpty(singleErrorMsg)) {
                        errorMsg.append(singleErrorMsg);
                    } else {
                        targetData.put(FIELD_NAME, dataValue);
                    }
                }
            }
            for (Map<String, Object> hiddenCol : hiddenConfList) {
                //获取字段名称
                String FIELD_NAME = (String) hiddenCol.get("FIELD_NAME");
                String TRANS_INTER = (String) hiddenCol.get("TRANS_INTER");
                //定义目标的值
                Object dataValue = (String) hiddenCol.get("HIDE_VALUE");
                dataValue = this.getTransValue(TRANS_INTER, dataValue, data);
                targetData.put(FIELD_NAME, dataValue);
            }
            targetDatas.add(targetData);
        }
    }

    /**
     * 获取验证错误信息
     *
     * @param EXCELIMP_HEADNUM
     * @param ruleMap
     * @param rowNum
     * @param data
     * @param colConfig
     * @param COL_NAME
     * @param IS_HIDE
     * @param dataValue
     * @param tableLength
     * @return
     */
    private String getValidErrorMsg(int EXCELIMP_HEADNUM,
                                    Map<String, String> ruleMap, int rowNum,
                                    List<Object> data, Map<String, Object> colConfig, String COL_NAME,
                                    String IS_HIDE, Object dataValue, Map<String, Integer> tableLength) {
        StringBuffer errorMsg = new StringBuffer("");
        if ("false".equals(IS_HIDE)) {
            String ALLOW_BLANK = (String) colConfig.get("ALLOW_BLANK");
            if ("false".equals(ALLOW_BLANK) && dataValue == null) {
                errorMsg.append("第" + (rowNum + EXCELIMP_HEADNUM + 2) + "行,字段[");
                errorMsg.append(COL_NAME).append("],不允许为空!");
            }
            String VALID_INTER = (String) colConfig.get("VALID_INTER");
            String validMsg = this.getDataValidMsg(VALID_INTER, dataValue, data);
            if (StringUtils.isNotEmpty(validMsg)) {
                errorMsg.append("第" + (rowNum + EXCELIMP_HEADNUM + 2) + "行,字段[");
                errorMsg.append(COL_NAME).append("],").append(validMsg);
            }
            String VALID_RULE = (String) colConfig.get("VALID_RULE");
            if (StringUtils.isNotEmpty(VALID_RULE) && dataValue != null) {
                if (!dataValue.toString().matches(VALID_RULE)) {
                    errorMsg.append("第").append((rowNum + EXCELIMP_HEADNUM + 2)).append("行,字段[")
                            .append(COL_NAME).append("],必须满足");
                    errorMsg.append(ruleMap.get(VALID_RULE));
                }
            }
            String FIELD_NAME = (String) colConfig.get("FIELD_NAME");
            Integer maxLength = tableLength.get(FIELD_NAME);
            if (maxLength != null && maxLength > 0 && dataValue != null) {
                try {
                    if (dataValue.toString().getBytes("GBK").length > maxLength) {
                        errorMsg.append("第").append((rowNum + EXCELIMP_HEADNUM + 2)).append("行,字段[")
                                .append(COL_NAME).append("],长度超过");
                        errorMsg.append(maxLength);
                    }
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    PlatLogUtil.printStackTrace(e);
                }
            }
        }
        if (errorMsg.length() > 2) {
            errorMsg.append("<br>");
            return errorMsg.toString();
        } else {
            return null;
        }
    }

}
