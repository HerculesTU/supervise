package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.core.dao.PortalRowConfDao;
import com.housoo.platform.core.service.PortalRowConfService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 行组件配置业务相关service实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-06-08 15:47:00
 */
@Service("portalRowConfService")
public class PortalRowConfServiceImpl extends BaseServiceImpl implements PortalRowConfService {

    /**
     * 所引入的dao
     */
    @Resource
    private PortalRowConfDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 初始化行配置信息
     *
     * @param rowId
     * @param portalRow
     */
    @Override
    public void initRowConfs(String rowId, Map<String, Object> portalRow) {
        //获取布局类型
        String LAYOUT_TYPE = (String) portalRow.get("ROW_LAYOUT");
        if ("1".equals(LAYOUT_TYPE)) {
            for (int i = 1; i <= 4; i++) {
                Map<String, Object> conf = new HashMap<String, Object>();
                conf.put("CONF_ROWID", rowId);
                conf.put("CONF_TITLE", "标题" + i);
                conf.put("CONF_SN", i);
                conf.put("CONF_BORDERCOLOR", PortalRowConfService.BORDERCOLOR_DEFAULT);
                conf.put("CONF_COLNUM", 3);
                this.saveOrUpdate("PLAT_APPMODEL_PORTALROWCONF", conf,
                        SysConstants.ID_GENERATOR_UUID, null);
            }
        } else if ("2".equals(LAYOUT_TYPE) || "3".equals(LAYOUT_TYPE) || "5".equals(LAYOUT_TYPE)) {
            for (int i = 1; i <= 2; i++) {
                Map<String, Object> conf = new HashMap<String, Object>();
                conf.put("CONF_ROWID", rowId);
                conf.put("CONF_TITLE", "标题" + i);
                conf.put("CONF_SN", i);
                conf.put("CONF_BORDERCOLOR", PortalRowConfService.BORDERCOLOR_DEFAULT);
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
                this.saveOrUpdate("PLAT_APPMODEL_PORTALROWCONF", conf,
                        SysConstants.ID_GENERATOR_UUID, null);
            }
        } else if ("4".equals(LAYOUT_TYPE)) {
            for (int i = 1; i <= 3; i++) {
                Map<String, Object> conf = new HashMap<String, Object>();
                conf.put("CONF_ROWID", rowId);
                conf.put("CONF_TITLE", "标题" + i);
                conf.put("CONF_SN", i);
                conf.put("CONF_BORDERCOLOR", PortalRowConfService.BORDERCOLOR_DEFAULT);
                conf.put("CONF_COLNUM", 4);
                this.saveOrUpdate("PLAT_APPMODEL_PORTALROWCONF", conf,
                        SysConstants.ID_GENERATOR_UUID, null);
            }
        } else if ("6".equals(LAYOUT_TYPE)) {
            Map<String, Object> conf = new HashMap<String, Object>();
            conf.put("CONF_ROWID", rowId);
            conf.put("CONF_TITLE", "标题");
            conf.put("CONF_SN", 1);
            conf.put("CONF_BORDERCOLOR", PortalRowConfService.BORDERCOLOR_DEFAULT);
            conf.put("CONF_COLNUM", 12);
            this.saveOrUpdate("PLAT_APPMODEL_PORTALROWCONF", conf,
                    SysConstants.ID_GENERATOR_UUID, null);
        }
    }

    /**
     * 根据行ID获取数据列表
     *
     * @param rowId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByRowId(String rowId) {
        StringBuffer sql = new StringBuffer("select C.*,R.RES_NAME,R.RES_MENUICON ");
        sql.append(",R.RES_CODE,R.RES_MENUURL from PLAT_APPMODEL_PORTALROWCONF C");
        sql.append(" LEFT JOIN PLAT_APPMODEL_PORTALCOMP T ON C.CONF_COMPID=T.COMP_ID");
        sql.append(" LEFT JOIN PLAT_SYSTEM_RES R ON R.RES_ID=T.COMP_MORERESID");
        sql.append(" WHERE C.CONF_ROWID=? ORDER BY C.CONF_SN ASC ");
        return dao.findBySql(sql.toString(), new Object[]{rowId}, null);
    }

    /**
     * 获取组件配置数量
     *
     * @param rowId
     * @return
     */
    @Override
    public int getCompConfCount(String rowId) {
        return dao.getCompConfCount(rowId);
    }

    /**
     * 获取列数量
     *
     * @param LAYOUT_TYPE
     * @param confSn
     * @return
     */
    private int getColNum(String LAYOUT_TYPE, int confSn) {
        if ("1".equals(LAYOUT_TYPE)) {
            return 3;
        } else if ("2".equals(LAYOUT_TYPE) || "3".equals(LAYOUT_TYPE) || "5".equals(LAYOUT_TYPE)) {
            switch (confSn) {
                case 1:
                    if ("2".equals(LAYOUT_TYPE)) {
                        return 3;
                    } else if ("3".equals(LAYOUT_TYPE)) {
                        return 9;
                    } else if ("5".equals(LAYOUT_TYPE)) {
                        return 6;
                    }
                    break;
                case 2:
                    if ("2".equals(LAYOUT_TYPE)) {
                        return 9;
                    } else if ("3".equals(LAYOUT_TYPE)) {
                        return 3;
                    } else if ("5".equals(LAYOUT_TYPE)) {
                        return 6;
                    }
                    break;
                default:
                    return 3;
            }
        } else if ("4".equals(LAYOUT_TYPE)) {
            return 4;
        } else if ("6".equals(LAYOUT_TYPE)) {
            return 12;
        }
        return 3;
    }

    /**
     * 更新组件的排序
     *
     * @param confId
     * @param sn
     */
    @Override
    public void updateCompConfSn(String confId, int sn, String rowId) {
        Map<String, Object> row = dao.getRecord("PLAT_APPMODEL_PORTALROW", new String[]{"ROW_ID"},
                new Object[]{rowId});
        String ROW_LAYOUT = (String) row.get("ROW_LAYOUT");
        int CONF_COLNUM = this.getColNum(ROW_LAYOUT, sn);
        StringBuffer sql = new StringBuffer("UPDATE PLAT_APPMODEL_PORTALROWCONF");
        sql.append(" SET CONF_SN=?,CONF_ROWID=?,CONF_COLNUM=? WHERE CONF_ID=? ");
        dao.executeSql(sql.toString(), new Object[]{sn, rowId, CONF_COLNUM, confId});
    }

    /**
     * 判断是否存在该配置
     *
     * @param THEME_ID    主题ID
     * @param CONF_ID     现有配置ID
     * @param CONF_COMPID 组件ID
     * @return
     */
    @Override
    public boolean isExists(String THEME_ID, String CONF_ID, String CONF_COMPID) {
        return dao.isExists(THEME_ID, CONF_ID, CONF_COMPID);
    }

    /**
     * 更新组件的URL
     *
     * @param compId
     * @param compUrl
     */
    @Override
    public void updateCompUrl(String compId, String compUrl) {
        StringBuffer sql = new StringBuffer("UPDATE PLAT_APPMODEL_PORTALROWCONF");
        sql.append(" SET CONF_COMPURL=? WHERE CONF_COMPID=?");
        dao.executeSql(sql.toString(), new Object[]{compUrl, compId});
    }

    /**
     * 更新组件为空
     *
     * @param compIds
     */
    @Override
    public void updateCompToNull(String compIds) {
        StringBuffer sql = new StringBuffer("UPDATE PLAT_APPMODEL_PORTALROWCONF");
        sql.append(" SET CONF_COMPID=null,CONF_COMPTYPECODE=null,CONF_COMPURL=null");
        sql.append(" WHERE CONF_COMPID IN ").append(PlatStringUtil.getValueArray(compIds));
        dao.executeSql(sql.toString(), null);
    }

}
