package com.housoo.platform.metadata.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.PagingBean;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatDbUtil;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.core.dynadatasource.DataSource;
import com.housoo.platform.metadata.dao.DataResDao;
import com.housoo.platform.metadata.service.DataResService;
import com.housoo.platform.metadata.service.DataSerService;
import com.housoo.platform.metadata.service.QueryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 数据资源信息业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-07 17:35:52
 */
@Service("dataResService")
public class DataResServiceImpl extends BaseServiceImpl implements DataResService {

    /**
     * 所引入的dao
     */
    @Resource
    private DataResDao dao;
    /**
     *
     */
    @Resource
    private QueryService queryService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取数据源列表
     *
     * @param param
     * @return
     */
    @Override
    public List<Map<String, Object>> findDbConnList(String param) {
        StringBuffer sql = new StringBuffer("SELECT T.DBCONN_ID AS VALUE,");
        sql.append("T.DBCONN_NAME AS LABEL FROM PLAT_APPMODEL_DBCONN T");
        sql.append(" ORDER BY T.DBCONN_TIME DESC ");
        List<Map<String, Object>> list = this.findBySql(sql.toString(), null, null);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("VALUE", "-1");
        data.put("LABEL", "本地缺省数据源");
        list.add(0, data);
        return list;
    }

    /**
     * 获取配置的返回结果列表
     * 返回字段结果JSON(FIELD_NAME:字段名称 FIELD_DESC:字段描述)
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map> findReturnFields(SqlFilter sqlFilter) {
        String DATARES_ID = sqlFilter.getRequest().getParameter("DATARES_ID");
        if (StringUtils.isNotEmpty(DATARES_ID)) {
            Map<String, Object> resources = dao.getRecord("PLAT_METADATA_DATARES",
                    new String[]{"DATARES_ID"}, new Object[]{DATARES_ID});
            String DATARES_RETURNJSON = (String) resources.get("DATARES_RETURNJSON");
            if (StringUtils.isNotEmpty(DATARES_RETURNJSON)) {
                return JSON.parseArray(DATARES_RETURNJSON, Map.class);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 保存目录资源中间表
     *
     * @param resId
     * @param catalogIds
     */
    @Override
    public void saveOrUpdateResCatalog(String resId, String catalogIds) {
        this.deleteRecords("PLAT_METADATA_CATARES", "DATARES_ID",
                new String[]{resId});
        String[] catalogIdArray = catalogIds.split(",");
        String sql = "insert into PLAT_METADATA_CATARES(CATALOG_ID,DATARES_ID)";
        sql += " VALUES(?,?) ";
        for (String catalogId : catalogIdArray) {
            dao.executeSql(sql, new Object[]{catalogId, resId});
        }
    }

    /**
     * 根据filter和配置信息获取数据列表
     *
     * @param filter
     * @param fieldInfo
     * @return
     */
    @Override
    public List<Map<String, Object>> findList(SqlFilter filter, Map<String, Object> fieldInfo) {
        String CATALOG_ID = filter.getRequest().getParameter("CATALOG_ID");
        List<Object> params = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer("select  T.DATARES_ID,T.DATARES_CODE,T.DATARES_NAME,T.DATARES_RUTYPE,D.DBCONN_NAME");
        sql.append(" from PLAT_METADATA_DATARES T LEFT JOIN PLAT_APPMODEL_DBCONN D ON T.DATARES_DBID=D.DBCONN_ID ");
        if (StringUtils.isNotEmpty(CATALOG_ID)) {
            sql.append("WHERE T.DATARES_ID IN (SELECT R.DATARES_ID FROM PLAT_METADATA_CATARES R");
            sql.append(" WHERE R.CATALOG_ID=? )");
            params.add(CATALOG_ID);
        }
        String exeSql = dao.getQuerySql(filter, sql.toString(), params);
        exeSql += " ORDER BY T.DATARES_TIME DESC";
        List<Map<String, Object>> list = dao.findBySql(exeSql,
                params.toArray(), filter.getPagingBean());
        return list;
    }

    /**
     * 获取中间表关联的目录信息
     *
     * @param resId
     * @return
     */
    @Override
    public Map<String, String> getCatalogInfo(String resId) {
        List<String> catalogIds = dao.findCataLogIds(resId);
        StringBuffer sql = new StringBuffer("SELECT T.CATALOG_ID,");
        sql.append("T.CATALOG_NAME FROM PLAT_METADATA_CATALOG T");
        sql.append(" WHERE T.CATALOG_ID IN ").append(PlatStringUtil.
                getSqlInCondition(catalogIds.toArray(new String[catalogIds.size()])));
        List<Map<String, Object>> list = this.findBySql(sql.toString(), null, null);
        Map<String, String> info = new HashMap<String, String>();
        StringBuffer DATARES_CATALOGIDS = new StringBuffer("");
        StringBuffer DATARES_CATALOGLABELS = new StringBuffer("");
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> data = list.get(i);
            String catalogId = (String) data.get("CATALOG_ID");
            String catalogName = (String) data.get("CATALOG_NAME");
            if (i > 0) {
                DATARES_CATALOGIDS.append(",");
                DATARES_CATALOGLABELS.append(",");
            }
            DATARES_CATALOGIDS.append(catalogId);
            DATARES_CATALOGLABELS.append(catalogName);
        }
        info.put("DATARES_CATALOGIDS", DATARES_CATALOGIDS.toString());
        info.put("DATARES_CATALOGLABELS", DATARES_CATALOGLABELS.toString());
        return info;
    }

    /**
     * 获取资源列表
     *
     * @param param
     * @return
     */
    @Override
    public List<Map<String, Object>> findSelect(String param) {
        StringBuffer sql = new StringBuffer("SELECT T.DATARES_ID AS VALUE");
        sql.append(",T.DATARES_NAME AS LABEL FROM PLAT_METADATA_DATARES T");
        sql.append(" ORDER BY T.DATARES_TIME DESC ");
        return dao.findBySql(sql.toString(), null, null);
    }

    /**
     * @param queryList
     * @param request
     * @return
     */
    private Map<String, Object> getParamValidResult(List<Map<String, Object>> queryList,
                                                    HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        boolean isPostAllMustParam = true;
        boolean isPostOverLengthParam = false;
        boolean isPostValidParam = true;
        StringBuffer isPostAllMustParamMsg = new StringBuffer("");
        StringBuffer isPostOverLengthParamMsg = new StringBuffer("");
        StringBuffer isPostValidParamMsg = new StringBuffer("");
        for (Map<String, Object> query : queryList) {
            String QUERY_EN = (String) query.get("QUERY_EN");
            String QUERY_CN = (String) query.get("QUERY_CN");
            String QUERY_VARULE = (String) query.get("QUERY_VARULE");
            String ruleName = (String) query.get("DIC_NAME");
            String QUERY_NULLABLE = query.get("QUERY_NULLABLE").toString();
            int QUERY_LENGTH = query.get("QUERY_LENGTH") != null ? Integer.parseInt(
                    query.get("QUERY_LENGTH").toString()) : 0;
            String postParamValue = request.getParameter(QUERY_EN);
            if ("-1".equals(QUERY_NULLABLE) && !StringUtils.isNotEmpty(postParamValue)) {
                isPostAllMustParam = false;
                isPostAllMustParamMsg.append("参数[" + QUERY_CN + "]为必须传递参数! ");
            }
            if (QUERY_LENGTH != 0 & StringUtils.isNotEmpty(postParamValue)
                    && postParamValue.length() > QUERY_LENGTH) {
                isPostOverLengthParam = true;
                isPostOverLengthParamMsg.append("参数[" + QUERY_CN + "]的长度不能超过" + QUERY_LENGTH);
            }
            if (StringUtils.isNotEmpty(QUERY_VARULE) && StringUtils.isNotEmpty(postParamValue)) {
                if (!postParamValue.toString().matches(QUERY_VARULE)) {
                    isPostValidParam = false;
                    isPostValidParamMsg.append("参数[" + QUERY_CN + "]的值必须满足[" + ruleName + "]");
                }
            }
        }
        if (!isPostAllMustParam) {
            result.put("msg", isPostAllMustParamMsg.toString());
            result.put("success", false);
            result.put("invokeResultCode", DataSerService.CODE_LOSEPARAM);
            return result;
        }
        if (isPostOverLengthParam) {
            result.put("msg", isPostOverLengthParamMsg.toString());
            result.put("success", false);
            result.put("invokeResultCode", DataSerService.CODE_UNLEGALPARAM);
            return result;
        }
        if (!isPostValidParam) {
            result.put("msg", isPostValidParamMsg.toString());
            result.put("success", false);
            result.put("invokeResultCode", DataSerService.CODE_UNLEGALPARAM);
            return result;
        }
        return null;
    }

    /**
     * 调用资源
     *
     * @param request
     * @param serviceInfo
     * @param dockInfo
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Override
    public Map<String, Object> invokeRes(HttpServletRequest request, Map<String, Object> serviceInfo
            , Map<String, Object> dockInfo) throws Exception {
        String resId = (String) serviceInfo.get("DATARES_ID");
        return this.exeInvoke(request, resId);
    }

    /**
     * 调用资源
     *
     * @param request
     * @param serviceInfo
     * @param dockInfo
     * @return
     */
    @Override
    public Map<String, Object> invokeRes(HttpServletRequest request, String resId) throws Exception {
        return exeInvoke(request, resId);
    }

    /**
     * @param request
     * @param resId
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private Map<String, Object> exeInvoke(HttpServletRequest request,
                                          String resId) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        Map<String, Object> result = new HashMap<String, Object>();
        //获取配置的参数列表
        List<Map<String, Object>> queryList = queryService.findByResId(resId);
        result = this.getParamValidResult(queryList, request);
        if (result != null) {
            return result;
        } else {
            result = new HashMap<String, Object>();
        }
        //获取请求过来的全部参数
        Map<String, Object> queryParams = new HashMap<String, Object>();
        for (Map<String, Object> query : queryList) {
            String QUERY_CN = (String) query.get("QUERY_CN");
            String QUERY_EN = (String) query.get("QUERY_EN");
            //获取提交的参数
            String postParam = request.getParameter(QUERY_EN);
            if (StringUtils.isNotEmpty(postParam)) {
                queryParams.put(QUERY_CN, postParam);
            }
        }
        //获取资源信息
        Map<String, Object> resInfo = this.getRecord("PLAT_METADATA_DATARES",
                new String[]{"DATARES_ID"}, new Object[]{resId});
        String DATARES_RUTYPE = resInfo.get("DATARES_RUTYPE").toString();
        if ("1".equals(DATARES_RUTYPE)) {
            //获取SQL的内容
            String sqlContent = (String) resInfo.get("DATARES_CONTENT");
            //获取数据源ID
            String dbId = (String) resInfo.get("DATARES_DBID");
            String rows = request.getParameter("rows");
            String page = request.getParameter("page");
            PagingBean pb = null;
            if (StringUtils.isNotEmpty(rows) && StringUtils.isNotEmpty(page)) {
                SqlFilter filter = new SqlFilter(request);
                pb = filter.getPagingBean();
            }
            List<Map<String, Object>> list = this.findSqlDatas(dbId, sqlContent, queryParams, pb);
            result.put("success", true);
            result.put("invokeResultCode", DataSerService.CODE_SUCCESS);
            result.put("datas", list);
        } else if ("2".equals(DATARES_RUTYPE)) {
            //获取接口
            String DATARES_JAVA = (String) resInfo.get("DATARES_JAVA");
            String beanId = DATARES_JAVA.split("[.]")[0];
            String method = DATARES_JAVA.split("[.]")[1];
            Object serviceBean = PlatAppUtil.getBean(beanId);
            if (serviceBean != null) {
                Method invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                        new Class[]{HttpServletRequest.class});
                result = (Map<String, Object>) invokeMethod.invoke(serviceBean,
                        new Object[]{request});
            }
        }
        return result;
    }

    public List<Map<String, Object>> findSqlDatas(String dbId, String sqlContent
            , Map<String, Object> queryParams, PagingBean pb) {
        List<Object> conditionParams = new ArrayList<Object>();
        String newSql = PlatDbUtil.transSqlContent(sqlContent, queryParams, conditionParams);
        if ("-1".equals(dbId)) {
            return this.findBySql(newSql, conditionParams.toArray(), pb);
        } else {
            Map<String, Object> dbInfo = this.getRecord("PLAT_APPMODEL_DBCONN",
                    new String[]{"DBCONN_ID"}, new Object[]{dbId});
            String dbUrl = (String) dbInfo.get("DBCONN_URL");
            String username = (String) dbInfo.get("DBCONN_USERNAME");
            String password = (String) dbInfo.get("DBCONN_PASS");
            String DBCONN_CLASS = (String) dbInfo.get("DBCONN_CLASS");
            Connection conn = null;
            List<Map<String, Object>> list = null;
            String dbType = null;
            if ("com.mysql.jdbc.Driver".equals(DBCONN_CLASS)) {
                dbType = "MYSQL";
            } else if ("com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(DBCONN_CLASS)) {
                dbType = "SQLSERVER";
            } else if ("oracle.jdbc.driver.OracleDriver".equals(DBCONN_CLASS)) {
                dbType = "ORACLE";
            }
            try {
                conn = PlatDbUtil.getConnect(dbUrl, username, password);
                list = PlatDbUtil.findBySql(conn, newSql, conditionParams.toArray(), pb, dbType);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return list;
        }

    }

    /**
     * @param sql
     * @param params
     * @param pb
     * @return
     */
    @Override
    @DataSource("unknown")
    public List<Map<String, Object>> findByDynaSql(String sql, List<Object> params, PagingBean pb) {
        return this.findBySql(sql, params.toArray(), pb);
    }

    /**
     * 级联删除相关信息
     *
     * @param resIds
     */
    @Override
    public void deleteCascade(String resIds) {
        this.deleteRecords("PLAT_METADATA_DATARES", "DATARES_ID",
                resIds.split(","));
        this.deleteRecords("PLAT_METADATA_CATARES", "DATARES_ID",
                resIds.split(","));
        this.deleteRecords("PLAT_METADATA_DATASER", "DATARES_ID",
                resIds.split(","));
    }

}
