package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.dao.FieldConfigDao;
import com.housoo.platform.core.service.FieldConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 描述 FieldConfig业务相关service实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-03-02 10:54:42
 */
@Service("fieldConfigService")
public class FieldConfigServiceImpl extends BaseServiceImpl implements FieldConfigService {

    /**
     * 所引入的dao
     */
    @Resource
    private FieldConfigDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据表单控件ID删除配置数据
     *
     * @param formControlId
     */
    @Override
    public void deleteByFormControlId(String formControlId) {
        StringBuffer sql = new StringBuffer("DELETE FROM ");
        sql.append("PLAT_APPMODEL_FIELDCONFIG  WHERE FIELDCONFIG_FORMCONTROLID=?");
        dao.executeSql(sql.toString(), new Object[]{formControlId});
    }

    /**
     * 根据表单控件ID获取字段信息列表
     *
     * @param formControlId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByFormControlId(String formControlId) {
        StringBuffer sql = new StringBuffer("SELECT * FROM PLAT_APPMODEL_FIELDCONFIG F");
        sql.append(" WHERE F.FIELDCONFIG_FORMCONTROLID=? ");
        return dao.findBySql(sql.toString(), new Object[]{formControlId}, null);
    }

    /**
     * 根据表单控件ID获取字段配置数据
     *
     * @param formControlId
     * @return
     */
    @Override
    public Map<String, Object> getFieldMapInfo(String formControlId) {
        List<Map<String, Object>> list = this.findByFormControlId(formControlId);
        Map<String, Object> fieldInfo = new HashMap<String, Object>();
        for (Map<String, Object> map : list) {
            fieldInfo.put(map.get("FIELDCONFIG_FIELDNAME").toString(),
                    map.get("FIELDCONFIG_FIELDVALUE"));
        }
        return fieldInfo;
    }

    /**
     * 保存字段配置
     *
     * @param fieldConfigInfo
     * @param confType        配置类型(base:基本配置 attach:附加配置)
     * @return
     */
    @Override
    public void saveFieldConfigAfterDel(Map<String, Object> fieldConfigInfo, String confType) {
        String FORMCONTROL_ID = (String) fieldConfigInfo.get("FORMCONTROL_ID");
        this.deleteRecord("PLAT_APPMODEL_FIELDCONFIG", new String[]{"FIELDCONFIG_FORMCONTROLID"
                , "FIELDCONFIG_CONFTYPE"}, new Object[]{FORMCONTROL_ID, confType});
        Set<String> excludeFieldNames = new HashSet<String>();
        excludeFieldNames.add("VIEW_TYPE");
        excludeFieldNames.add("FORMCONTROL_ID");
        excludeFieldNames.add("FORMCONTROL_COMPCODE");
        Iterator it = fieldConfigInfo.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            if (fieldValue != null && StringUtils.isNotEmpty(fieldValue.toString())) {
                if (!excludeFieldNames.contains(fieldName)) {
                    Map<String, Object> fieldConfig = new HashMap<String, Object>();
                    fieldConfig.put("FIELDCONFIG_FORMCONTROLID", FORMCONTROL_ID);
                    fieldConfig.put("FIELDCONFIG_FIELDNAME", fieldName);
                    fieldConfig.put("FIELDCONFIG_FIELDVALUE", fieldValue);
                    fieldConfig.put("FIELDCONFIG_CONFTYPE", confType);
                    this.saveOrUpdate("PLAT_APPMODEL_FIELDCONFIG", fieldConfig,
                            SysConstants.ID_GENERATOR_UUID, null);
                }
            }
        }
    }

    /**
     * 根据表单控件ID和字段名称获取字段值
     *
     * @param formControlId
     * @param fieldName
     * @return
     */
    @Override
    public String getFieldValue(String formControlId, String fieldName) {
        Map<String, Object> fieldConfig = this.
                getRecord("PLAT_APPMODEL_FIELDCONFIG", new String[]{"FIELDCONFIG_FORMCONTROLID"
                        , "FIELDCONFIG_FIELDNAME"}, new Object[]{formControlId, fieldName});
        if (fieldConfig != null) {
            return (String) fieldConfig.get("FIELDCONFIG_FIELDVALUE");
        } else {
            return null;
        }
    }

    /**
     * 克隆控件配置信息数据
     *
     * @param sourceControlId
     * @param newControlId
     */
    @Override
    public void copyFieldConfigs(String sourceControlId, String newControlId) {
        Map<String, String> replaceColumn = new HashMap<String, String>();
        //获取数据库类型
        String dbType = PlatAppUtil.getDbType();
        if ("MYSQL".equals(dbType)) {
            replaceColumn.put("FIELDCONFIG_ID", "REPLACE(UUID(),'-','')");
        } else if ("ORACLE".equals(dbType)) {
            replaceColumn.put("FIELDCONFIG_ID", "SYS_GUID()");
        } else if ("SQLSERVER".equals(dbType)) {
            replaceColumn.put("FIELDCONFIG_ID", "REPLACE(newId(),'-','')");
        }
        replaceColumn.put("FIELDCONFIG_FORMCONTROLID", "?");
        String copySql = dao.getCopyTableSql("PLAT_APPMODEL_FIELDCONFIG", replaceColumn);
        StringBuffer sql = new StringBuffer(copySql);
        sql.append(" WHERE FIELDCONFIG_FORMCONTROLID=?");
        dao.executeSql(sql.toString(), new Object[]{newControlId, sourceControlId});
    }

    /**
     * 根据设计ID获取字段配置列表
     *
     * @param designId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByDesignId(String designId) {
        StringBuffer sql = new StringBuffer("SELECT * FROM PLAT_APPMODEL_FIELDCONFIG P");
        sql.append(" WHERE P.FIELDCONFIG_FORMCONTROLID IN (SELECT T.FORMCONTROL_ID");
        sql.append(" FROM PLAT_APPMODEL_FORMCONTROL T WHERE T.FORMCONTROL_DESIGN_ID=? )");
        sql.append(" ORDER BY P.FIELDCONFIG_ID DESC ");
        return dao.findBySql(sql.toString(), new Object[]{designId}, null);
    }
}
