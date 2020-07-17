package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.dao.GenCmpTplDao;
import com.housoo.platform.core.service.GenCmpTplService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述 GenCmpTpl业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-03-15 17:15:24
 */
@Service("genCmpTplService")
public class GenCmpTplServiceImpl extends BaseServiceImpl implements GenCmpTplService {

    /**
     * 所引入的dao
     */
    @Resource
    private GenCmpTplDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据sqlFilter
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map<String, Object>> findBySqlFilter(SqlFilter sqlFilter) {
        StringBuffer sql = new StringBuffer("SELECT T.GENCMPTPL_ID");
        sql.append(",T.GENCMPTPL_DESC,D.DIC_NAME AS GENCMPTPL_TYPE");
        sql.append(" FROM PLAT_APPMODEL_GENCMPTPL T ");
        sql.append("LEFT JOIN PLAT_SYSTEM_DICTIONARY D");
        sql.append(" ON T.GENCMPTPL_TYPE=D.DIC_VALUE ");
        sql.append(" WHERE D.DIC_DICTYPE_CODE=? ");
        List params = new ArrayList();
        params.add("GEN_TPL_TYPE");
        String exeSql = dao.getQuerySql(sqlFilter, sql.toString(), params);
        List<Map<String, Object>> list = dao.findBySql(exeSql,
                params.toArray(), sqlFilter.getPagingBean());
        return list;
    }

    /**
     * 根据模版类型获取可选JAVA接口列表
     *
     * @param tplType
     * @return
     */
    @Override
    public List<Map<String, Object>> findSelectJavaInters(String tplType) {
        StringBuffer sql = new StringBuffer("SELECT T.GENCMPTPL_JAVAINTER AS VALUE");
        sql.append(",T.GENCMPTPL_DESC,T.GENCMPTPL_CODE FROM PLAT_APPMODEL_GENCMPTPL T");
        sql.append(" WHERE T.GENCMPTPL_TYPE=? ORDER BY T.GENCMPTPL_CREATETIME ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{tplType}, null);
        for (Map<String, Object> map : list) {
            String GENCMPTPL_DESC = (String) map.get("GENCMPTPL_DESC");
            map.put("LABEL", GENCMPTPL_DESC + "(" + map.get("VALUE") + ")");
        }
        return list;
    }

    /**
     * 根据模版类型获取button接口列表
     *
     * @param tplType
     * @return
     */
    @Override
    public List<Map<String, Object>> findSelectButtons(String tplType) {
        StringBuffer sql = new StringBuffer("SELECT T.GENCMPTPL_ID AS VALUE,T.GENCMPTPL_DESC AS LABEL");
        sql.append(",T.GENCMPTPL_BTNICON,T.GENCMPTPL_BTNCOLOR,T.GENCMPTPL_BTNNAME");
        sql.append(" FROM PLAT_APPMODEL_GENCMPTPL T");
        sql.append(" WHERE T.GENCMPTPL_TYPE=? ORDER BY T.GENCMPTPL_CREATETIME ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{tplType}, null);
        /*for(Map<String,Object> map:list){
            String GENCMPTPL_CODE = (String) map.get("GENCMPTPL_CODE");
            map.put("GENCMPTPL_CODE", StringEscapeUtils.escapeHtml(GENCMPTPL_CODE));
        }*/
        return list;
    }

}
