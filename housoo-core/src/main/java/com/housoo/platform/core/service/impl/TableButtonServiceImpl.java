package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.core.dao.TableButtonDao;
import com.housoo.platform.core.service.TableButtonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 表格按钮业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-03-17 10:25:49
 */
@Service("tableButtonService")
public class TableButtonServiceImpl extends BaseServiceImpl implements TableButtonService {

    /**
     * 所引入的dao
     */
    @Resource
    private TableButtonDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据sqlFilter获取数据记录列表
     *
     * @param filter
     * @return
     */
    @Override
    public List<Map<String, Object>> findBySqlFilter(SqlFilter filter) {
        StringBuffer sql = new StringBuffer("SELECT T.TABLEBUTTON_ID,T.TABLEBUTTON_NAME");
        sql.append(",T.TABLEBUTTON_ICON,T.TABLEBUTTON_FN,T.TABLEBUTTON_RESKEY ");
        sql.append("FROM PLAT_APPMODEL_TABLEBUTTON T");
        List params = new ArrayList();
        String exeSql = dao.getQuerySql(filter, sql.toString(), params);
        List<Map<String, Object>> list = dao.findBySql(exeSql,
                params.toArray(), null);
        return list;
    }

    /**
     * 获取下一个排序值
     *
     * @param formControlId
     * @return
     */
    @Override
    public int getNextSn(String formControlId) {
        int maxSn = dao.getMaxSn(formControlId);
        return maxSn + 1;
    }

    /**
     * 更新排序值
     *
     * @param buttonIds
     */
    @Override
    public void updateSn(String[] buttonIds) {
        StringBuffer sql = new StringBuffer("UPDATE PLAT_APPMODEL_TABLEBUTTON");
        sql.append(" SET TABLEBUTTON_SN=? WHERE TABLEBUTTON_ID=?");
        for (int i = 0; i < buttonIds.length; i++) {
            int sn = i + 1;
            dao.executeSql(sql.toString(), new Object[]{sn, buttonIds[i]});
        }
    }

    /**
     * 根据表单控件ID获取按钮列表
     *
     * @param formControlId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByFormControlId(String formControlId) {
        StringBuffer sql = new StringBuffer("SELECT T.* FROM PLAT_APPMODEL_TABLEBUTTON");
        sql.append(" T WHERE T.TABLEBUTTON_FORMCONTROLID=? ORDER BY ");
        sql.append("T.TABLEBUTTON_SN ASC");
        return dao.findBySql(sql.toString(), new Object[]{formControlId}, null);
    }

    /**
     * 根据表单控件ID级联删除子孙控件配置的操作按钮
     *
     * @param targetCtrolIds
     */
    @Override
    public void deleteCascadeByFormControlId(String targetCtrolIds) {
        StringBuffer sql = new StringBuffer("DELETE FROM PLAT_APPMODEL_TABLEBUTTON ");
        sql.append(" WHERE TABLEBUTTON_FORMCONTROLID IN ");
        sql.append("( SELECT T.FORMCONTROL_ID FROM PLAT_APPMODEL_FORMCONTROL T");
        sql.append(" WHERE T.FORMCONTROL_ID IN ").append(PlatStringUtil.getSqlInCondition(targetCtrolIds));
        sql.append(" )");
        dao.executeSql(sql.toString(), null);
    }

    /**
     * 克隆配置的按钮信息
     *
     * @param sourceControlId
     * @param newControlId
     */
    @Override
    public void copyTableButtons(String sourceControlId, String newControlId) {
        Map<String, String> replaceColumn = new HashMap<String, String>();
        String dbType = PlatAppUtil.getDbType();
        if ("MYSQL".equals(dbType)) {
            replaceColumn.put("TABLEBUTTON_ID", "REPLACE(UUID(),'-','')");
        } else if ("ORACLE".equals(dbType)) {
            replaceColumn.put("TABLEBUTTON_ID", "SYS_GUID()");
        } else if ("SQLSERVER".equals(dbType)) {
            replaceColumn.put("TABLEBUTTON_ID", "REPLACE(newId(),'-','')");
        }
        replaceColumn.put("TABLEBUTTON_FORMCONTROLID", "?");
        String copySql = dao.getCopyTableSql("PLAT_APPMODEL_TABLEBUTTON", replaceColumn);
        StringBuffer sql = new StringBuffer(copySql);
        sql.append(" WHERE TABLEBUTTON_FORMCONTROLID=?");
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
        sql.append("PLAT_APPMODEL_TABLEBUTTON P WHERE P.TABLEBUTTON_FORMCONTROLID");
        sql.append(" IN (SELECT T.FORMCONTROL_ID");
        sql.append(" FROM PLAT_APPMODEL_FORMCONTROL T WHERE T.FORMCONTROL_DESIGN_ID=? )");
        sql.append(" ORDER BY P.TABLEBUTTON_ID DESC ");
        return dao.findBySql(sql.toString(), new Object[]{designId}, null);
    }

}
