package com.housoo.platform.common.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.model.TableColumn;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.service.DbManagerService;
import com.housoo.platform.common.dao.CommonDao;
import com.housoo.platform.common.service.CommonService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author 高飞
 * @created 2017年2月1日 上午10:24:51
 */
@Service("commonService")
public class CommonServiceImpl extends BaseServiceImpl implements CommonService {

    /**
     * 所引入的dao
     */
    @Resource
    private CommonDao dao;
    /**
     *
     */
    @Resource
    private DbManagerService dbManagerService;

    /**
     *
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 判断记录是否存在
     *
     * @param validTableName
     * @param validFieldName
     * @param validFieldValue
     * @return
     */
    @Override
    public boolean isExistsRecord(String validTableName, String validFieldName,
                                  String validFieldValue, String RECORD_ID) {
        int count = dao.getRecordCount(validTableName, validFieldName, validFieldValue, RECORD_ID);
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取自动补全的通用数据源
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map<String, Object>> findAutoCompleteDatas(SqlFilter sqlFilter) {
        String tableName = sqlFilter.getRequest().getParameter("tableName");
        String idAndNameCol = sqlFilter.getRequest().getParameter("idAndNameCol");
        String dataJavaInter = sqlFilter.getRequest().getParameter("dataJavaInter");
        if (StringUtils.isNotEmpty(dataJavaInter)) {
            String beanId = dataJavaInter.split("[.]")[0];
            String method = dataJavaInter.split("[.]")[1];
            Object serviceBean = PlatAppUtil.getBean(beanId);
            if (serviceBean != null) {
                Method invokeMethod;
                List<Map<String, Object>> list = null;
                try {
                    invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                            new Class[]{SqlFilter.class});
                    list = (List<Map<String, Object>>) invokeMethod.invoke(serviceBean,
                            new Object[]{sqlFilter});
                } catch (Exception e) {
                    PlatLogUtil.printStackTrace(e);
                }
                if (list != null) {
                    return list;
                } else {
                    return new ArrayList<Map<String, Object>>();
                }
            } else {
                return new ArrayList<Map<String, Object>>();
            }
        } else {
            if (StringUtils.isNotEmpty(idAndNameCol)) {
                String targetCol = idAndNameCol.split(",")[1];
                StringBuffer sql = new StringBuffer("SELECT ");
                sql.append(targetCol).append(" AS label,");
                sql.append(targetCol).append(" AS value FROM ");
                sql.append(tableName);
                List<Object> params = new ArrayList<Object>();
                String exeSql = dao.getQuerySql(sqlFilter, sql.toString(), params);
                List<Map<String, Object>> list = dao.findBySql(exeSql,
                        params.toArray(), null);
                return list;
            } else {
                return new ArrayList<Map<String, Object>>();
            }
        }


    }

    /**
     * 获取可编辑表格的测试数据
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map<String, Object>> findEditTableDatas(SqlFilter sqlFilter) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        return list;
    }

    /**
     * 获取所有的表主键集合
     *
     * @return
     */
    @Override
    public Map<String, List<String>> getAllPkNames() {
        String dbType = PlatAppUtil.getDbType();
        Map<String, List<String>> pkHashMap = new HashMap<String, List<String>>();
        if ("ORACLE".equals(dbType)) {
            StringBuffer sql = new StringBuffer(
                    "select cu.column_name,au.table_name from user_cons_columns cu")
                    .append(",user_constraints au where cu.constraint_name = au.constraint_name ")
                    .append("and au.constraint_type = 'P' ");
            //.append(" AND (au.TABLE_NAME LIKE '%PLAT_%' OR au.TABLE_NAME LIKE '%JBPM6_%') ");
            List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
            for (Map<String, Object> map : list) {
                String column_name = (String) map.get("COLUMN_NAME");
                String tableName = (String) map.get("TABLE_NAME");
                tableName = tableName.toUpperCase();
                List<String> keyList = pkHashMap.get(tableName);
                if (keyList == null) {
                    keyList = new ArrayList<String>();
                }
                keyList.add(column_name.toUpperCase());
                pkHashMap.put(tableName, keyList);
            }
        } else if ("MYSQL".equals(dbType)) {
            String dbSchema = PlatAppUtil.getDbSchema();
            StringBuffer sql = new StringBuffer(
                    "select upper(T.COLUMN_NAME) AS COLUMN_NAME,T.TABLE_NAME from information_schema.columns ")
                    .append("T where T.table_schema =?   ")
                    .append("AND T.COLUMN_KEY='PRI' ORDER BY T.ORDINAL_POSITION ASC ");
            List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{dbSchema}, null);
            for (Map<String, Object> map : list) {
                String column_name = (String) map.get("COLUMN_NAME");
                String tableName = (String) map.get("TABLE_NAME");
                tableName = tableName.toUpperCase();
                List<String> keyList = pkHashMap.get(tableName);
                if (keyList == null) {
                    keyList = new ArrayList<String>();
                }
                keyList.add(column_name.toUpperCase());
                pkHashMap.put(tableName, keyList);
            }
        } else if ("SQLSERVER".equals(dbType)) {
            List<Map<String, Object>> list = dbManagerService.findAllTables(null);
            for (Map<String, Object> data : list) {
                String tableName = (String) data.get("VALUE");
                List<String> pkList = this.findPrimaryKeyNames(tableName);
                pkHashMap.put(tableName, pkList);
            }
        }
        return pkHashMap;
    }

    /**
     * 获取所有的表字段集合
     *
     * @return
     */
    @Override
    public Map<String, List<String>> getAllTableColumnNames() {
        String dbType = PlatAppUtil.getDbType();
        Map<String, List<String>> pkHashMap = new HashMap<String, List<String>>();
        if ("ORACLE".equals(dbType)) {
            StringBuffer sql = new StringBuffer("select t.COLUMN_NAME")
                    .append(",t.TABLE_NAME from user_tab_columns t ");
            sql.append(" ORDER BY T.COLUMN_ID ASC");
            List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
            for (Map<String, Object> map : list) {
                String column_name = (String) map.get("COLUMN_NAME");
                String tableName = (String) map.get("TABLE_NAME");
                tableName = tableName.toUpperCase();
                List<String> keyList = pkHashMap.get(tableName);
                if (keyList == null) {
                    keyList = new ArrayList<String>();
                }
                keyList.add(column_name.toUpperCase());
                pkHashMap.put(tableName, keyList);
            }
        } else if ("MYSQL".equals(dbType)) {
            StringBuffer sql = new StringBuffer(
                    "select upper(T.COLUMN_NAME) AS COLUMN_NAME,T.TABLE_NAME from information_schema.columns ")
                    .append("T where T.table_schema =?   ")
                    .append("AND T.COLUMN_KEY='PRI' ORDER BY T.ORDINAL_POSITION ASC ");
            List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                    new Object[]{PlatAppUtil.getDbSchema()}, null);
            for (Map<String, Object> map : list) {
                String column_name = (String) map.get("COLUMN_NAME");
                String tableName = (String) map.get("TABLE_NAME");
                tableName = tableName.toUpperCase();
                List<String> keyList = pkHashMap.get(tableName);
                if (keyList == null) {
                    keyList = new ArrayList<String>();
                }
                keyList.add(column_name.toUpperCase());
                pkHashMap.put(tableName, keyList);
            }
        } else if ("SQLSERVER".equals(dbType)) {
            List<Map<String, Object>> list = dbManagerService.findAllTables(null);
            for (Map<String, Object> data : list) {
                String tableName = (String) data.get("VALUE");
                List<TableColumn> colList = this.findTableColumnByTableName(tableName);
                List<String> keyList = pkHashMap.get(tableName);
                if (keyList == null) {
                    keyList = new ArrayList<String>();
                }
                for (TableColumn column : colList) {
                    keyList.add(column.getColumnName());
                }
                pkHashMap.put(tableName, keyList);
            }
        }
        return pkHashMap;
    }

    /**
     * 通用提交请求地址
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> findCommonUrlList(String params) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> url1 = new HashMap<String, Object>();
        url1.put("VALUE", "common/baseController.do?saveOrUpdateSingle&tableName=你的表名&busDesc=业务说明");
        url1.put("LABEL", "通用单表存储提交地址");
        Map<String, Object> url2 = new HashMap<String, Object>();
        url2.put("VALUE", "common/baseController.do?saveOrUpdateTree&tableName=你的表名&busDesc=业务说明");
        url2.put("LABEL", "通用树形单表存储提交地址");
        Map<String, Object> url3 = new HashMap<String, Object>();
        url3.put("VALUE", "metadata/DataResController.do?invokeRes&PLAT_RESCODE=你的资源编码");
        url3.put("LABEL", "提交到数据中心地址");
        list.add(url1);
        list.add(url2);
        list.add(url3);
        return list;
    }

    /**
     * 根据节点级联删除数据
     *
     * @param treeNodeId
     * @param tableName
     */
    @Override
    public void deleteTreeDataCascadeChild(String treeNodeId, String tableName) {
        String pkName = this.findPrimaryKeyNames(tableName).get(0);
        String pathName = pkName.substring(0, pkName.lastIndexOf("_")) + "_PATH";
        StringBuffer sql = new StringBuffer("DELETE FROM " + tableName);
        sql.append(" WHERE ").append(pathName).append(" LIKE ? ");
        dao.executeSql(sql.toString(), new Object[]{"%." + treeNodeId + ".%"});
    }

}
