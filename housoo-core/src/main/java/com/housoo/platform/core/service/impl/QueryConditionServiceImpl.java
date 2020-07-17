package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.core.dao.QueryConditionDao;
import com.housoo.platform.core.service.QueryConditionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 QueryCondition业务相关service实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-03-14 10:55:40
 */
@Service("queryConditionService")
public class QueryConditionServiceImpl extends BaseServiceImpl implements QueryConditionService {

    /**
     * 所引入的dao
     */
    @Resource
    private QueryConditionDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据filter获取配置的查询条件列表
     *
     * @param filter
     * @return
     */
    @Override
    public List<Map<String, Object>> findBySqlFilter(SqlFilter filter) {
        StringBuffer sql = new StringBuffer("SELECT T.QUERYCONDITION_ID,T.QUERYCONDITION_CTRLNAME,");
        sql.append("T.QUERYCONDITION_LABEL,CTRL.DIC_NAME AS CTRLTYPE,STRAGE.DIC_NAME AS QUERYSTRAGE");
        sql.append(" FROM PLAT_APPMODEL_QUERYCONDITION T LEFT JOIN PLAT_SYSTEM_DICTIONARY STRAGE");
        sql.append(" ON T.QUERYCONDITION_QUERYSTRAGE=STRAGE.DIC_VALUE ");
        sql.append("LEFT JOIN PLAT_SYSTEM_DICTIONARY CTRL ON T.QUERYCONDITION_CTRLTYPE=CTRL.DIC_VALUE");
        sql.append(" WHERE CTRL.DIC_DICTYPE_CODE=? AND STRAGE.DIC_DICTYPE_CODE=? ");
        List params = new ArrayList();
        params.add("QUERYCTRLTYPE");
        params.add("QUERY_STRAGE");
        String exeSql = dao.getQuerySql(filter, sql.toString(), params);
        List<Map<String, Object>> list = dao.findBySql(exeSql,
                params.toArray(), null);
        return list;
    }

    /**
     * 根据表单控件ID获取排序条件的下一个值
     *
     * @param formControlId
     * @return
     */
    @Override
    public int getNextSn(String formControlId) {
        return dao.getMaxSn(formControlId) + 1;
    }

    /**
     * 更新排序字段
     *
     * @param conditionIds
     */
    @Override
    public void updateSn(String[] conditionIds) {
        StringBuffer sql = new StringBuffer("UPDATE PLAT_APPMODEL_QUERYCONDITION");
        sql.append(" SET QUERYCONDITION_SN=? WHERE QUERYCONDITION_ID=?");
        for (int i = 0; i < conditionIds.length; i++) {
            int sn = i + 1;
            dao.executeSql(sql.toString(), new Object[]{sn, conditionIds[i]});
        }
    }

    /**
     * 根据表单控件ID获取查询条件列表
     *
     * @param formControlId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByFormControlId(String formControlId) {
        StringBuffer sql = new StringBuffer("SELECT Q.* FROM ");
        sql.append("PLAT_APPMODEL_QUERYCONDITION Q");
        sql.append(" WHERE Q.QUERYCONDITION_FORMCONTROLID=? ORDER BY ");
        sql.append("Q.QUERYCONDITION_SN ASC");
        return dao.findBySql(sql.toString(), new Object[]{formControlId}, null);
    }

    /**
     * 根据表单控件ID级联删除子孙控件配置的查询条件
     *
     * @param targetControlIds
     */
    @Override
    public void deleteCascadeByFormControlId(String targetControlIds) {
        StringBuffer sql = new StringBuffer("DELETE FROM PLAT_APPMODEL_QUERYCONDITION ");
        sql.append(" WHERE QUERYCONDITION_FORMCONTROLID IN ");
        sql.append("(SELECT T.FORMCONTROL_ID FROM PLAT_APPMODEL_FORMCONTROL T");
        sql.append(" WHERE T.FORMCONTROL_ID IN ").append(PlatStringUtil.getSqlInCondition(targetControlIds));
        sql.append(" )");
        dao.executeSql(sql.toString(), null);
    }

    /**
     * 克隆查询条件配置
     *
     * @param sourceControlId
     * @param newControlId
     */
    @Override
    public void copyQuesConditions(String sourceControlId, String newControlId) {
        Map<String, String> replaceColumn = new HashMap<String, String>();
        String dbType = PlatAppUtil.getDbType();
        if ("MYSQL".equals(dbType)) {
            replaceColumn.put("QUERYCONDITION_ID", "REPLACE(UUID(),'-','')");
        } else if ("ORACLE".equals(dbType)) {
            replaceColumn.put("QUERYCONDITION_ID", "SYS_GUID()");
        } else if ("SQLSERVER".equals(dbType)) {
            replaceColumn.put("QUERYCONDITION_ID", "REPLACE(newId(),'-','')");
        }
        replaceColumn.put("QUERYCONDITION_FORMCONTROLID", "?");
        String copySql = dao.getCopyTableSql("PLAT_APPMODEL_QUERYCONDITION", replaceColumn);
        StringBuffer sql = new StringBuffer(copySql);
        sql.append(" WHERE QUERYCONDITION_FORMCONTROLID=?");
        dao.executeSql(sql.toString(), new Object[]{newControlId, sourceControlId});
    }

    /**
     * 根据设计ID获取列表数据
     *
     * @param designId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByDesignId(String designId) {
        StringBuffer sql = new StringBuffer("SELECT * FROM ");
        sql.append("PLAT_APPMODEL_QUERYCONDITION P WHERE P.QUERYCONDITION_FORMCONTROLID");
        sql.append(" IN (SELECT T.FORMCONTROL_ID");
        sql.append(" FROM PLAT_APPMODEL_FORMCONTROL T WHERE T.FORMCONTROL_DESIGN_ID=? )");
        sql.append(" ORDER BY P.QUERYCONDITION_ID DESC ");
        return dao.findBySql(sql.toString(), new Object[]{designId}, null);
    }

}
