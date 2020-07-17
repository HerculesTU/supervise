package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.dao.TableColDao;
import com.housoo.platform.core.service.TableColService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 表格列业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-03-30 15:16:45
 */
@Service("tableColService")
public class TableColServiceImpl extends BaseServiceImpl implements TableColService {

    /**
     * 所引入的dao
     */
    @Resource
    private TableColDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据表单控件ID获取下一个排序值
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
     * 更新配置列的排序
     *
     * @param tableColIds
     */
    @Override
    public void updateSn(String[] tableColIds) {
        StringBuffer sql = new StringBuffer("UPDATE PLAT_APPMODEL_TABLECOL");
        sql.append(" SET TABLECOL_ORERSN=? WHERE TABLECOL_ID=?");
        for (int i = 0; i < tableColIds.length; i++) {
            int sn = i + 1;
            dao.executeSql(sql.toString(), new Object[]{sn, tableColIds[i]});
        }
    }

    /**
     * 根据表单控件ID获取列表数据
     *
     * @param formControlId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByFormControlId(String formControlId) {
        StringBuffer sql = new StringBuffer("SELECT T.* ");
        sql.append("FROM PLAT_APPMODEL_TABLECOL T ");
        sql.append(" WHERE T.TABLECOL_FORMCONTROLID=? ");
        sql.append("ORDER BY T.TABLECOL_ORERSN ASC");
        return dao.findBySql(sql.toString(), new Object[]{formControlId}, null);
    }

    /**
     * 克隆配置的表格列信息
     *
     * @param sourceControlId
     * @param newControlId
     */
    @Override
    public void copyTableCols(String sourceControlId, String newControlId) {
        Map<String, String> replaceColumn = new HashMap<String, String>();
        String dbType = PlatAppUtil.getDbType();
        if ("MYSQL".equals(dbType)) {
            replaceColumn.put("TABLECOL_ID", "REPLACE(UUID(),'-','')");
        } else if ("ORACLE".equals(dbType)) {
            replaceColumn.put("TABLECOL_ID", "SYS_GUID()");
        } else if ("SQLSERVER".equals(dbType)) {
            replaceColumn.put("TABLECOL_ID", "REPLACE(newId(),'-','')");
        }
        replaceColumn.put("TABLECOL_FORMCONTROLID", "?");
        String copySql = dao.getCopyTableSql("PLAT_APPMODEL_TABLECOL", replaceColumn);
        StringBuffer sql = new StringBuffer(copySql);
        sql.append(" WHERE TABLECOL_FORMCONTROLID=?");
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
        sql.append("PLAT_APPMODEL_TABLECOL P WHERE P.TABLECOL_FORMCONTROLID");
        sql.append(" IN (SELECT T.FORMCONTROL_ID");
        sql.append(" FROM PLAT_APPMODEL_FORMCONTROL T WHERE T.FORMCONTROL_DESIGN_ID=? )");
        sql.append(" ORDER BY P.TABLECOL_ID DESC ");
        return dao.findBySql(sql.toString(), new Object[]{designId}, null);
    }

}
