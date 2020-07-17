package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.dao.PortalRowDao;
import com.housoo.platform.core.service.PortalRowService;
import com.housoo.platform.core.service.PortalRowConfService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 门户行业务相关service实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-06-08 15:07:52
 */
@Service("portalRowService")
public class PortalRowServiceImpl extends BaseServiceImpl implements PortalRowService {

    /**
     * 所引入的dao
     */
    @Resource
    private PortalRowDao dao;
    /**
     *
     */
    @Resource
    private PortalRowConfService portalRowConfService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据主题ID获取下个排序值
     *
     * @param themeId
     * @return
     */
    @Override
    public int getNextSn(String themeId) {
        int maxSn = dao.getMaxSn(themeId);
        return maxSn + 1;
    }

    /**
     * 获取行配置
     *
     * @param ROW_ID
     * @param sn
     * @return
     */
    private Map<String, Object> getRowConf(String ROW_ID, int sn) {
        Map<String, Object> conf = portalRowConfService.getRecord("PLAT_APPMODEL_PORTALROWCONF",
                new String[]{"CONF_ROWID", "CONF_SN"}, new Object[]{ROW_ID, sn});
        if (conf == null) {
            conf = new HashMap<String, Object>();
            conf.put("CONF_ROWID", ROW_ID);
            conf.put("CONF_TITLE", "未设置标题");
            /*if(sn!=0){
                conf.put("CONF_TITLE", "标题");
            }else{
                conf.put("CONF_TITLE", "标题"+sn);
            }*/
            conf.put("CONF_BORDERCOLOR", PortalRowConfService.BORDERCOLOR_DEFAULT);
        }
        return conf;
    }

    /**
     * 重置组件配置
     *
     * @param LAYOUT_TYPE
     * @param ROW_ID
     */
    public int resetCompConf(String LAYOUT_TYPE, String ROW_ID) {
        if ("1".equals(LAYOUT_TYPE)) {
            for (int i = 1; i <= 4; i++) {
                Map<String, Object> conf = this.getRowConf(ROW_ID, i);
                conf.put("CONF_SN", i);
                conf.put("CONF_COLNUM", 3);
                portalRowConfService.saveOrUpdate("PLAT_APPMODEL_PORTALROWCONF",
                        conf, SysConstants.ID_GENERATOR_UUID, null);
            }
            return 4;
        } else if ("2".equals(LAYOUT_TYPE) || "3".equals(LAYOUT_TYPE) || "5".equals(LAYOUT_TYPE)) {
            for (int i = 1; i <= 2; i++) {
                Map<String, Object> conf = this.getRowConf(ROW_ID, i);
                conf.put("CONF_COLNUM", 3);
                conf.put("CONF_SN", i);
                switch (i) {
                    case 1:
                        if ("2".equals(LAYOUT_TYPE)) {
                            conf.put("CONF_COLNUM", 3);
                        } else if ("3".equals(LAYOUT_TYPE)) {
                            conf.put("CONF_COLNUM", 9);
                        } else if ("5".equals(LAYOUT_TYPE)) {
                            conf.put("CONF_COLNUM", 6);
                        }
                        break;
                    case 2:
                        if ("2".equals(LAYOUT_TYPE)) {
                            conf.put("CONF_COLNUM", 9);
                        } else if ("3".equals(LAYOUT_TYPE)) {
                            conf.put("CONF_COLNUM", 3);
                        } else if ("5".equals(LAYOUT_TYPE)) {
                            conf.put("CONF_COLNUM", 6);
                        }
                        break;
                    default:
                        break;
                }
                portalRowConfService.saveOrUpdate("PLAT_APPMODEL_PORTALROWCONF",
                        conf, SysConstants.ID_GENERATOR_UUID, null);
            }
            return 2;
        } else if ("4".equals(LAYOUT_TYPE)) {
            for (int i = 1; i <= 3; i++) {
                Map<String, Object> conf = this.getRowConf(ROW_ID, i);
                conf.put("CONF_SN", i);
                conf.put("CONF_COLNUM", 4);
                portalRowConfService.saveOrUpdate("PLAT_APPMODEL_PORTALROWCONF",
                        conf, SysConstants.ID_GENERATOR_UUID, null);
            }
            return 3;
        } else if ("6".equals(LAYOUT_TYPE)) {
            Map<String, Object> conf = this.getRowConf(ROW_ID, 1);
            conf.put("CONF_SN", 1);
            conf.put("CONF_COLNUM", 12);
            portalRowConfService.saveOrUpdate("PLAT_APPMODEL_PORTALROWCONF",
                    conf, SysConstants.ID_GENERATOR_UUID, null);
            return 1;
        }
        return 0;
    }

    /**
     * 保存行并且级联保存配置
     *
     * @param portalRow
     * @return
     */
    @Override
    public Map<String, Object> saveCascadeRowConf(Map<String, Object> portalRow) {

        String ROW_ID = (String) portalRow.get("ROW_ID");
        String ROW_THEMEID = (String) portalRow.get("ROW_THEMEID");
        if (StringUtils.isNotEmpty(ROW_ID)) {
            //获取当前组件的配置数量
            String LAYOUT_TYPE = (String) portalRow.get("ROW_LAYOUT");
            //获取当前组件的高度
            String ROW_HEIGHT = (String) portalRow.get("ROW_HEIGHT");
            //获取旧的行数据
            Map<String, Object> rowInfo = this.getRecord("PLAT_APPMODEL_PORTALROW",
                    new String[]{"ROW_ID"}, new Object[]{ROW_ID});
            //获取布局类型
            String OLD_LAYOUTTYPE = (String) rowInfo.get("ROW_LAYOUT");
            rowInfo.put("ROW_LAYOUT", LAYOUT_TYPE);
            rowInfo.put("ROW_HEIGHT", ROW_HEIGHT);
            this.saveOrUpdate("PLAT_APPMODEL_PORTALROW", rowInfo, SysConstants.ID_GENERATOR_UUID, null);
            if (!OLD_LAYOUTTYPE.equals(LAYOUT_TYPE)) {
                //获取旧的组件配置信息数量
                int count = this.portalRowConfService.getCompConfCount(ROW_ID);
                int currentCount = this.resetCompConf(LAYOUT_TYPE, ROW_ID);
                if (count > currentCount) {
                    //删除多余的组件SQL
                    StringBuffer deleteSql = new StringBuffer("DELETE FROM ");
                    deleteSql.append("PLAT_APPMODEL_PORTALROWCONF WHERE CONF_ROWID=? ");
                    deleteSql.append(" AND CONF_SN>? ");
                    dao.executeSql(deleteSql.toString(), new Object[]{ROW_ID, currentCount});
                }
            }
        } else {
            int nextSn = this.getNextSn(ROW_THEMEID);
            portalRow.put("ROW_SN", nextSn);
            portalRow = this.saveOrUpdate("PLAT_APPMODEL_PORTALROW",
                    portalRow, SysConstants.ID_GENERATOR_UUID, null);
            ROW_ID = (String) portalRow.get("ROW_ID");
            portalRowConfService.initRowConfs(ROW_ID, portalRow);
        }
        return portalRow;
    }

    /**
     * 根据主题ID获取数据列表
     *
     * @param themeId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByThemeId(String themeId) {
        StringBuffer sql = new StringBuffer("SELECT * FROM PLAT_APPMODEL_PORTALROW T");
        sql.append(" WHERE T.ROW_THEMEID=? ORDER BY T.ROW_SN ASC ");
        return dao.findBySql(sql.toString(), new Object[]{themeId}, null);
    }

    /**
     * 根据行ID级联删除配置
     *
     * @param rowId
     */
    @Override
    public void deleteCascadeConfg(String rowId) {
        dao.deleteRecords("PLAT_APPMODEL_PORTALROWCONF", "CONF_ROWID", new String[]{rowId});
        dao.deleteRecord("PLAT_APPMODEL_PORTALROW", new String[]{"ROW_ID"}, new Object[]{rowId});
    }

    /**
     * 更新排序值
     *
     * @param rowId
     * @param rowSn
     */
    @Override
    public void updateSn(String rowId, int rowSn) {
        StringBuffer sql = new StringBuffer("UPDATE");
        sql.append(" PLAT_APPMODEL_PORTALROW T SET ");
        sql.append("T.ROW_SN=? WHERE T.ROW_ID=? ");
        dao.executeSql(sql.toString(), new Object[]{rowSn, rowId});
    }

    /**
     * 克隆行数据
     *
     * @param sourceThemeId
     * @param newThemeId
     */
    @Override
    public void copyRows(String sourceThemeId, String newThemeId) {
        //获取源行数据
        StringBuffer sql = new StringBuffer("SELECT * FROM PLAT_APPMODEL_PORTALROW");
        sql.append(" T WHERE T.ROW_THEMEID=? ORDER BY T.ROW_SN ASC");
        List<Map<String, Object>> sourceRowList = dao.findBySql(sql.toString(),
                new Object[]{sourceThemeId}, null);
        StringBuffer sql2 = new StringBuffer("SELECT * FROM PLAT_APPMODEL_PORTALROWCONF");
        sql2.append(" T WHERE T.CONF_ROWID=? ORDER BY T.CONF_SN ASC ");
        for (Map<String, Object> sourceRow : sourceRowList) {
            String SOURCEROW_ID = (String) sourceRow.get("ROW_ID");
            Map<String, Object> newRow = sourceRow;
            newRow.remove("ROW_CREATETIME");
            newRow.remove("ROW_ID");
            newRow.put("ROW_THEMEID", newThemeId);
            newRow = dao.saveOrUpdate("PLAT_APPMODEL_PORTALROW", newRow,
                    SysConstants.ID_GENERATOR_UUID, null);
            String NEWROW_ID = (String) newRow.get("ROW_ID");
            List<Map<String, Object>> sourceConfList = dao.findBySql(sql2.toString(),
                    new Object[]{SOURCEROW_ID}, null);
            for (Map<String, Object> conf : sourceConfList) {
                Map<String, Object> newConf = conf;
                newConf.remove("CONF_ID");
                newConf.put("CONF_ROWID", NEWROW_ID);
                dao.saveOrUpdate("PLAT_APPMODEL_PORTALROWCONF", newConf,
                        SysConstants.ID_GENERATOR_UUID, null);
            }
        }
    }

}
