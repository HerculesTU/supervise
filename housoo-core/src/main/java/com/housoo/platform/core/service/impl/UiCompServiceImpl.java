package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.*;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatFileUtil;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.core.dao.UiCompDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 UiComp业务相关service实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-02-21 10:11:07
 */
@Service("uiCompService")
public class UiCompServiceImpl extends BaseServiceImpl implements UiCompService {

    /**
     * 所引入的dao
     */
    @Resource
    private UiCompDao dao;
    /**
     *
     */
    @Resource
    private QueryConditionService queryConditionService;
    /**
     *
     */
    @Resource
    private TableButtonService tableButtonService;
    /**
     *
     */
    @Resource
    private FieldConfigService fieldConfigService;
    /**
     *
     */
    @Resource
    private TableColService tableColService;
    /**
     *
     */
    @Resource
    private DesignService designService;

    /**
     *
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据filter获取数据列表
     *
     * @param filter
     * @return
     */
    @Override
    public List<Map<String, Object>> findBySqlFilter(SqlFilter filter) {
        StringBuffer sql = new StringBuffer("SELECT D.DIC_NAME,P.COMP_ID,");
        sql.append("P.COMP_NAME,P.COMP_CODE FROM ");
        sql.append("PLAT_APPMODEL_UICOMP P LEFT JOIN PLAT_SYSTEM_DICTIONARY D");
        sql.append(" ON P.COMP_TYPECODE=D.DIC_VALUE ");
        sql.append(" WHERE D.DIC_DICTYPE_CODE=? ");
        List<Object> params = new ArrayList<Object>();
        params.add("CONTROL_TYPE");
        String exeSql = dao.getQuerySql(filter, sql.toString(), params);
        List<Map<String, Object>> list = dao.findBySql(exeSql,
                params.toArray(), filter.getPagingBean());
        return list;
    }

    /**
     * 根据组件类别编码获取数据列表
     *
     * @param compTypeCode
     * @return
     */
    @Override
    public List<Map<String, Object>> findByCompTypeCode(String compTypeCode) {
        StringBuffer sql = new StringBuffer("SELECT T.COMP_CODE AS VALUE,");
        sql.append("T.COMP_NAME AS LABEL FROM PLAT_APPMODEL_UICOMP T");
        List params = new ArrayList();
        if (StringUtils.isNotEmpty(compTypeCode)) {
            sql.append(" WHERE T.COMP_TYPECODE=? ");
            params.add(compTypeCode);
        }
        sql.append(" ORDER BY T.COMP_CREATETIME DESC ");
        return dao.findBySql(sql.toString(), params.toArray(), null);
    }

    /**
     * 生产东西南北中布局代码
     *
     * @param paramsConfig
     * @return
     */
    @Override
    public Map<String, String> genDirectorLayout(Map<String, Object> paramsConfig) {
        String FORMCONTROL_TPLCODE = (String) paramsConfig.get("FORMCONTROL_TPLCODE");
        String LAYOUT_JSON = (String) paramsConfig.get("LAYOUT_JSON");
        List<Map> layouts = JSON.parseArray(LAYOUT_JSON, Map.class);
        StringBuffer layoutsize = new StringBuffer("");
        for (Map layout : layouts) {
            String LAYOUT_TYPE = (String) layout.get("LAYOUT_TYPE");
            String LAYOUT_SIZE = (String) layout.get("LAYOUT_SIZE");
            if (StringUtils.isNotEmpty(LAYOUT_SIZE)) {
                layoutsize.append("&quot;").append(LAYOUT_TYPE).append("__size&quot;:");
                layoutsize.append(LAYOUT_SIZE).append(",");
            }
            if ("center".equals(LAYOUT_TYPE)) {
                layout.put("platcompname", "中央布局");
            } else if ("west".equals(LAYOUT_TYPE)) {
                layout.put("platcompname", "西部布局");
            } else if ("east".equals(LAYOUT_TYPE)) {
                layout.put("platcompname", "东部布局");
            } else if ("north".equals(LAYOUT_TYPE)) {
                layout.put("platcompname", "北部布局");
            } else if ("south".equals(LAYOUT_TYPE)) {
                layout.put("platcompname", "南部布局");
            }
        }
        if (layoutsize.length() > 1) {
            layoutsize = layoutsize.deleteCharAt(layoutsize.length() - 1);
            paramsConfig.put("layoutsize", layoutsize.toString());
        }
        paramsConfig.put("layoutlist", layouts);
        String resultCode = PlatStringUtil.getFreeMarkResult(FORMCONTROL_TPLCODE, paramsConfig);
        Map<String, String> result = new HashMap<String, String>();
        result.put("genCode", resultCode);
        return result;
    }

    /**
     * 生成bootstrapTAB控件代码
     *
     * @param paramsConfig
     * @return
     */
    @Override
    public Map<String, String> genBootTabControlCode(Map<String, Object> paramsConfig) {
        String FORMCONTROL_TPLCODE = (String) paramsConfig.get("FORMCONTROL_TPLCODE");
        String tabConfigJson = (String) paramsConfig.get("tabConfigJson");
        List<Map> tabList = JSON.parseArray(tabConfigJson, Map.class);
        paramsConfig.put("tabList", tabList);
        String resultCode = PlatStringUtil.getFreeMarkResult(FORMCONTROL_TPLCODE, paramsConfig);
        Map<String, String> result = new HashMap<String, String>();
        result.put("genCode", resultCode);
        return result;
    }

    /**
     * 生成树形面板的代码
     *
     * @param paramsConfig
     * @return
     */
    @Override
    public Map<String, String> genTreePanelControlCode(Map<String, Object> paramsConfig) {
        String FORMCONTROL_TPLCODE = (String) paramsConfig.get("FORMCONTROL_TPLCODE");
        String TABLEBUTTON_IDS = (String) paramsConfig.get("TABLEBUTTON_IDS");
        String FORMCONTROL_ID = (String) paramsConfig.get("FORMCONTROL_ID");
        String CHECKEDCAS = (String) paramsConfig.get("CHECKEDCAS");
        String UNCHECKEDCAS = (String) paramsConfig.get("UNCHECKEDCAS");
        if (StringUtils.isNotEmpty(CHECKEDCAS)) {
            paramsConfig.put("CHECKEDCAS", CHECKEDCAS.replace(",", ""));
        }
        if (StringUtils.isNotEmpty(UNCHECKEDCAS)) {
            paramsConfig.put("UNCHECKEDCAS", UNCHECKEDCAS.replace(",", ""));
        }
        if (StringUtils.isNotEmpty(TABLEBUTTON_IDS)) {
            tableButtonService.updateSn(TABLEBUTTON_IDS.split(","));
        }
        List<Map<String, Object>> operbtns = tableButtonService.findByFormControlId(FORMCONTROL_ID);
        StringBuffer rightmenus = new StringBuffer("");
        for (Map<String, Object> operbtn : operbtns) {
            StringBuffer menu = new StringBuffer("[");
            menu.append((String) operbtn.get("TABLEBUTTON_NAME")).append(",");
            menu.append((String) operbtn.get("TABLEBUTTON_ICON")).append(",");
            menu.append((String) operbtn.get("TABLEBUTTON_FN"));
            String TABLEBUTTON_RESKEY = (String) operbtn.get("TABLEBUTTON_RESKEY");
            if (StringUtils.isNotEmpty(TABLEBUTTON_RESKEY)) {
                menu.append(",").append(TABLEBUTTON_RESKEY);
            }
            menu.append("]");
            rightmenus.append(menu);
        }
        if (rightmenus.length() > 1) {
            paramsConfig.put("rightmenus", rightmenus.toString());
        }
        paramsConfig.put("operbtns", operbtns);
        if (StringUtils.isNotEmpty((String) paramsConfig.get("PLAT_DESIGNMODE"))) {
            paramsConfig.put("PLAT_DESIGNMODE", "true");
        }
        List<Map<String, Object>> querys = queryConditionService.findByFormControlId(FORMCONTROL_ID);
        paramsConfig.put("querys", querys);
        String resultCode = PlatStringUtil.getFreeMarkResult(FORMCONTROL_TPLCODE, paramsConfig);
        Map<String, String> result = new HashMap<String, String>();
        result.put("genCode", resultCode);
        return result;
    }

    /**
     * 生成JqTree表格插件代码
     *
     * @param paramsConfig
     * @return
     */
    @Override
    public Map<String, String> genJqTreeControlCode(Map<String, Object> paramsConfig) {
        String FORMCONTROL_TPLCODE = (String) paramsConfig.get("FORMCONTROL_TPLCODE");
        String QUERYCONDITIONIDS = (String) paramsConfig.get("QUERYCONDITIONIDS");
        String TABLEBUTTON_IDS = (String) paramsConfig.get("TABLEBUTTON_IDS");
        String FORMCONTROL_ID = (String) paramsConfig.get("FORMCONTROL_ID");
        String TREE_TABLENAME = (String) paramsConfig.get("TREE_TABLENAME");
        String primaryKeyName = this.findPrimaryKeyNames(TREE_TABLENAME).get(0);
        paramsConfig.put("primaryKeyName", primaryKeyName);
        if (StringUtils.isNotEmpty(QUERYCONDITIONIDS)) {
            this.queryConditionService.updateSn(QUERYCONDITIONIDS.split(","));
        }
        if (StringUtils.isNotEmpty(TABLEBUTTON_IDS)) {
            tableButtonService.updateSn(TABLEBUTTON_IDS.split(","));
        }
        List<Map<String, Object>> querys = queryConditionService.findByFormControlId(FORMCONTROL_ID);
        paramsConfig.put("querys", querys);
        List<Map<String, Object>> operbtns = tableButtonService.findByFormControlId(FORMCONTROL_ID);
        paramsConfig.put("operbtns", operbtns);

        String columnJson = fieldConfigService.getFieldValue(FORMCONTROL_ID, "COLUMN_JSON");
        List<Map> columns = JSON.parseArray(columnJson, Map.class);
        String ExpandColumn = null;
        List<Map> showColumns = new ArrayList<Map>();
        for (Map column : columns) {
            String FIELD_ISHIDE = (String) column.get("FIELD_ISHIDE");
            if ("-1".equals(FIELD_ISHIDE)) {
                showColumns.add(column);
            }
        }
        ExpandColumn = showColumns.get(1).get("FIELD_NAME").toString();
        paramsConfig.put("ExpandColumn", ExpandColumn);
        paramsConfig.put("columns", columns);
        if (StringUtils.isNotEmpty((String) paramsConfig.get("PLAT_DESIGNMODE"))) {
            paramsConfig.put("PLAT_DESIGNMODE", "true");
        }
        String resultCode = PlatStringUtil.getFreeMarkResult(FORMCONTROL_TPLCODE, paramsConfig);
        Map<String, String> result = new HashMap<String, String>();
        result.put("genCode", resultCode);
        return result;
    }

    /**
     * 生成JqGrid表格插件代码
     *
     * @param paramsConfig
     * @return
     */
    @Override
    public Map<String, String> genJqgridCode(Map<String, Object> paramsConfig) {
        String FORMCONTROL_TPLCODE = (String) paramsConfig.get("FORMCONTROL_TPLCODE");
        String QUERYCONDITIONIDS = (String) paramsConfig.get("QUERYCONDITIONIDS");
        String TABLEBUTTON_IDS = (String) paramsConfig.get("TABLEBUTTON_IDS");
        String FORMCONTROL_ID = (String) paramsConfig.get("FORMCONTROL_ID");
        if (StringUtils.isNotEmpty(QUERYCONDITIONIDS)) {
            this.queryConditionService.updateSn(QUERYCONDITIONIDS.split(","));
        }
        if (StringUtils.isNotEmpty(TABLEBUTTON_IDS)) {
            tableButtonService.updateSn(TABLEBUTTON_IDS.split(","));
        }
        List<Map<String, Object>> querys = queryConditionService.findByFormControlId(FORMCONTROL_ID);
        //定义是否存在可视的查询条件
        String haveVisiableQuery = "-1";
        for (Map<String, Object> query : querys) {
            String QUERYCONDITION_CTRLTYPE = (String) query.get("QUERYCONDITION_CTRLTYPE");
            if (!"1".equals(QUERYCONDITION_CTRLTYPE)) {
                haveVisiableQuery = "1";
            }
        }
        paramsConfig.put("haveVisiableQuery", haveVisiableQuery);
        paramsConfig.put("querys", querys);
        List<Map<String, Object>> operbtns = tableButtonService.findByFormControlId(FORMCONTROL_ID);
        paramsConfig.put("operbtns", operbtns);
        String columnJson = fieldConfigService.getFieldValue(FORMCONTROL_ID, "COLUMN_JSON");
        List<Map> columns = JSON.parseArray(columnJson, Map.class);
        paramsConfig.put("columns", columns);
        if (StringUtils.isNotEmpty((String) paramsConfig.get("PLAT_DESIGNMODE"))) {
            paramsConfig.put("PLAT_DESIGNMODE", "true");
        }
        String resultCode = PlatStringUtil.getFreeMarkResult(FORMCONTROL_TPLCODE, paramsConfig);
        Map<String, String> result = new HashMap<String, String>();
        result.put("genCode", resultCode);
        return result;
    }

    /**
     * 生成可编辑表格的控件代码
     *
     * @param paramsConfig
     * @return
     */
    @Override
    public Map<String, String> genEditTableCode(Map<String, Object> paramsConfig) {
        String FORMCONTROL_TPLCODE = (String) paramsConfig.get("FORMCONTROL_TPLCODE");
        String QUERYCONDITIONIDS = (String) paramsConfig.get("QUERYCONDITIONIDS");
        String TABLEBUTTON_IDS = (String) paramsConfig.get("TABLEBUTTON_IDS");
        String FORMCONTROL_ID = (String) paramsConfig.get("FORMCONTROL_ID");
        String CONTROL_ID = (String) paramsConfig.get("CONTROL_ID");
        String DESIGN_ID = (String) paramsConfig.get("FORMCONTROL_DESIGN_ID");
        Map<String, Object> design = this.designService.getRecord("PLAT_APPMODEL_DESIGN",
                new String[]{"DESIGN_ID"}, new Object[]{DESIGN_ID});
        //获取所配置的列IDS
        String TABLECOLIDS = (String) paramsConfig.get("TABLECOLIDS");
        if (StringUtils.isNotEmpty(QUERYCONDITIONIDS)) {
            this.queryConditionService.updateSn(QUERYCONDITIONIDS.split(","));
        }
        if (StringUtils.isNotEmpty(TABLEBUTTON_IDS)) {
            tableButtonService.updateSn(TABLEBUTTON_IDS.split(","));
        }
        if (StringUtils.isNotEmpty(TABLECOLIDS)) {
            tableColService.updateSn(TABLECOLIDS.split(","));
        }
        //-----------------开始查询所配置的查询条件-----------------------
        List<Map<String, Object>> querys = queryConditionService.findByFormControlId(FORMCONTROL_ID);
        //定义是否存在可视的查询条件
        String haveVisiableQuery = "-1";
        for (Map<String, Object> query : querys) {
            String QUERYCONDITION_CTRLTYPE = (String) query.get("QUERYCONDITION_CTRLTYPE");
            if (!"1".equals(QUERYCONDITION_CTRLTYPE)) {
                haveVisiableQuery = "1";
            }
        }
        paramsConfig.put("haveVisiableQuery", haveVisiableQuery);
        paramsConfig.put("querys", querys);
        //------------------结束查询所配置的查询条件-------------------------
        //-----------------开始查询所配置的操作按钮--------------------------
        List<Map<String, Object>> operbtns = tableButtonService.findByFormControlId(FORMCONTROL_ID);
        paramsConfig.put("operbtns", operbtns);
        //-----------------结束查询所配置的操作按钮
        paramsConfig = this.setEditTableTrTableCol(FORMCONTROL_ID, paramsConfig);
        paramsConfig.put("DESIGN_CODE", design.get("DESIGN_CODE").toString());
        //定义行模版路径
        //----------------开始生成配置的行模版文件-------------------
        String appPath = PlatAppUtil.getAppAbsolutePath();
        StringBuffer editTrPath = new StringBuffer(appPath);
        editTrPath.append("webpages/common/compdesign/edittable/edittabletr_tpl.jsp");
        String LOADTR_JS = fieldConfigService.getFieldValue(FORMCONTROL_ID, "LOADTR_JS");
        paramsConfig.put("LOADTR_JS", LOADTR_JS);
        String editTrTplString = PlatFileUtil.readFileString(editTrPath.toString());
        String editTrResult = PlatStringUtil.getFreeMarkResult(editTrTplString, paramsConfig);
        String editTrGenPath = designService.getGenPathByDesignIdOrCode(DESIGN_ID, CONTROL_ID + ".jsp", true, true);
        PlatFileUtil.writeDataToDisk(editTrResult, editTrGenPath, null);
        //----------------结束生成配置的行模版文件-------------------
        //-----------------结束查询所配置的列-------------------------------
        if (StringUtils.isNotEmpty((String) paramsConfig.get("PLAT_DESIGNMODE"))) {
            paramsConfig.put("PLAT_DESIGNMODE", "true");
        }
        String resultCode = PlatStringUtil.getFreeMarkResult(FORMCONTROL_TPLCODE, paramsConfig);
        Map<String, String> result = new HashMap<String, String>();
        result.put("genCode", resultCode);
        return result;
    }

    /**
     * 生成可编辑表格行模版的代码
     *
     * @param formControlId
     */
    @Override
    public void genEditTableTrCode(String formControlId, String designId) {
        Map<String, Object> paramsConfig = new HashMap<String, Object>();
        paramsConfig = setEditTableTrTableCol(formControlId, paramsConfig);
        String appPath = PlatAppUtil.getAppAbsolutePath();
        String CONTROL_ID = this.fieldConfigService.getFieldValue(formControlId, "CONTROL_ID");
        String LOADTR_JS = this.fieldConfigService.getFieldValue(formControlId, "LOADTR_JS");
        paramsConfig.put("LOADTR_JS", LOADTR_JS);
        StringBuffer editTrPath = new StringBuffer(appPath);
        editTrPath.append("webpages/common/compdesign/edittable/edittabletr_tpl.jsp");
        String editTrTplString = PlatFileUtil.readFileString(editTrPath.toString());
        String editTrResult = PlatStringUtil.getFreeMarkResult(editTrTplString, paramsConfig);
        String editTrGenPath = designService.getGenPathByDesignIdOrCode(designId, CONTROL_ID + ".jsp", true, true);
        PlatFileUtil.writeDataToDisk(editTrResult, editTrGenPath, null);
    }

    /**
     * @param formControlId
     * @param paramsConfig
     * @return
     */
    private Map<String, Object> setEditTableTrTableCol(String formControlId,
                                                       Map<String, Object> paramsConfig) {
        List<Map<String, Object>> tableCols = tableColService.findByFormControlId(formControlId);
        List<Map<String, Object>> showCols = new ArrayList<Map<String, Object>>();
        StringBuffer COL_STYLE = new StringBuffer("");
        for (Map<String, Object> tableCol : tableCols) {
            String TABLECOL_COMPTYPE = (String) tableCol.get("TABLECOL_COMPTYPE");
            if (!"hidden".equals(TABLECOL_COMPTYPE)) {
                showCols.add(tableCol);
            } else {
                String TABLECOL_FIELDNAME = (String) tableCol.get("TABLECOL_FIELDNAME");
                String fieldDataName = "${DATA." + TABLECOL_FIELDNAME + "}";
                tableCol.put("FIELD_DATANAME", fieldDataName);
            }
        }
        for (int i = 0; i < showCols.size(); i++) {
            Map<String, Object> tableCol = showCols.get(i);
            String TABLECOL_PERCENT = tableCol.get("TABLECOL_PERCENT").toString();
            String TABLECOL_NAME = (String) tableCol.get("TABLECOL_NAME");
            String TABLECOL_FIELDNAME = (String) tableCol.get("TABLECOL_FIELDNAME");
            String TABLECOL_COMPTYPE = (String) tableCol.get("TABLECOL_COMPTYPE");
            if ("input".equals(TABLECOL_COMPTYPE)) {
                String TABLECOL_ALLOWBLANK = (String) tableCol.get("TABLECOL_ALLOWBLANK");
                if ("false".equals(TABLECOL_ALLOWBLANK)) {
                    String validRules = "required;";
                    String TABLECOL_VALIDRULES = (String) tableCol.get("TABLECOL_VALIDRULES");
                    if (StringUtils.isNotEmpty(TABLECOL_VALIDRULES)) {
                        validRules += TABLECOL_VALIDRULES;
                    }
                    tableCol.put("TABLECOL_VALIDRULES", validRules);
                }
            }
            String colStyle = "[";
            if (i > 0) {
                COL_STYLE.append(",");
            }
            colStyle += TABLECOL_PERCENT + "%,";
            colStyle += TABLECOL_NAME + "]";
            COL_STYLE.append(colStyle);
            String fieldDataName = "${DATA." + TABLECOL_FIELDNAME + "}";
            tableCol.put("FIELD_DATANAME", fieldDataName);
        }
        paramsConfig.put("COL_STYLE", COL_STYLE);
        paramsConfig.put("tableCols", tableCols);
        return paramsConfig;
    }

    /**
     * 生成通用类控件代码
     *
     * @param paramsConfig
     * @return
     */
    @Override
    public Map<String, String> genFormControlCode(Map<String, Object> paramsConfig) {
        String FORMCONTROL_TPLCODE = (String) paramsConfig.get("FORMCONTROL_TPLCODE");
        String resultCode = PlatStringUtil.getFreeMarkResult(FORMCONTROL_TPLCODE, paramsConfig);
        Map<String, String> result = new HashMap<String, String>();
        result.put("genCode", resultCode);
        return result;
    }

    /**
     * 生成网格类控件代码
     *
     * @param paramsConfig
     * @return
     */
    @Override
    public Map<String, String> genGridItemControlCode(Map<String, Object> paramsConfig) {
        String FORMCONTROL_TPLCODE = (String) paramsConfig.get("FORMCONTROL_TPLCODE");
        String COLUMN_JSON = (String) paramsConfig.get("COLUMN_JSON");
        List<Map> columnList = JSON.parseArray(COLUMN_JSON, Map.class);
        StringBuffer ITEMCONF = new StringBuffer("");
        for (Map column : columnList) {
            ITEMCONF.append("[");
            ITEMCONF.append(column.get("FIELD_NAME")).append(",");
            ITEMCONF.append(column.get("IS_HIDE")).append(",");
            ITEMCONF.append(column.get("LABELNAME")).append("]");
        }
        paramsConfig.put("ITEMCONF", ITEMCONF.toString());
        String resultCode = PlatStringUtil.getFreeMarkResult(FORMCONTROL_TPLCODE, paramsConfig);
        Map<String, String> result = new HashMap<String, String>();
        result.put("genCode", resultCode);
        return result;
    }

    /**
     * 生成按钮工具栏代码
     *
     * @param paramsConfig
     * @return
     */
    @Override
    public Map<String, String> genButtonToolbarCompCode(Map<String, Object> paramsConfig) {
        String FORMCONTROL_TPLCODE = (String) paramsConfig.get("FORMCONTROL_TPLCODE");
        String TABLEBUTTON_IDS = (String) paramsConfig.get("TABLEBUTTON_IDS");
        String FORMCONTROL_ID = (String) paramsConfig.get("FORMCONTROL_ID");
        if (StringUtils.isNotEmpty(TABLEBUTTON_IDS)) {
            tableButtonService.updateSn(TABLEBUTTON_IDS.split(","));
        }
        List<Map<String, Object>> operbtns = tableButtonService.findByFormControlId(FORMCONTROL_ID);
        paramsConfig.put("operbtns", operbtns);
        String resultCode = PlatStringUtil.getFreeMarkResult(FORMCONTROL_TPLCODE, paramsConfig);
        Map<String, String> result = new HashMap<String, String>();
        result.put("genCode", resultCode);
        return result;
    }

    /**
     * 生成textarea的控件代码
     *
     * @param paramsConfig
     * @return
     */
    @Override
    public Map<String, String> genTextareaCompCode(Map<String, Object> paramsConfig) {
        String FORMCONTROL_TPLCODE = (String) paramsConfig.get("FORMCONTROL_TPLCODE");
        String TABLEBUTTON_IDS = (String) paramsConfig.get("TABLEBUTTON_IDS");
        String FORMCONTROL_ID = (String) paramsConfig.get("FORMCONTROL_ID");
        if (StringUtils.isNotEmpty(TABLEBUTTON_IDS)) {
            tableButtonService.updateSn(TABLEBUTTON_IDS.split(","));
        }
        List<Map<String, Object>> operbtns = tableButtonService.findByFormControlId(FORMCONTROL_ID);
        paramsConfig.put("operbtns", operbtns);
        StringBuffer BUTTONCONFIGS = new StringBuffer("");
        for (Map<String, Object> operbtn : operbtns) {
            StringBuffer btnStr = new StringBuffer("[");
            btnStr.append(operbtn.get("TABLEBUTTON_NAME")).append(",");
            btnStr.append(operbtn.get("TABLEBUTTON_FN")).append("]");
            BUTTONCONFIGS.append(btnStr);
        }
        if (BUTTONCONFIGS.length() > 0) {
            paramsConfig.put("BUTTONCONFIGS", BUTTONCONFIGS.toString());
        }
        String resultCode = PlatStringUtil.getFreeMarkResult(FORMCONTROL_TPLCODE, paramsConfig);
        Map<String, String> result = new HashMap<String, String>();
        result.put("genCode", resultCode);
        return result;
    }

    /**
     * 生成输入框控件代码
     *
     * @param paramsConfig
     * @return
     */
    @Override
    public Map<String, String> genInputCompCode(Map<String, Object> paramsConfig) {
        String FORMCONTROL_TPLCODE = (String) paramsConfig.get("FORMCONTROL_TPLCODE");
        String ALLOW_BLANK = (String) paramsConfig.get("ALLOW_BLANK");
        String DATA_RULE = (String) paramsConfig.get("DATA_RULE");
        String AJAX_ABLE = (String) paramsConfig.get("AJAX_ABLE");
        String AJAX_URL = (String) paramsConfig.get("AJAX_URL");
        StringBuffer validRules = new StringBuffer("");
        if ("false".equals(ALLOW_BLANK)) {
            validRules.append("required;");
        }
        if (StringUtils.isNotEmpty(DATA_RULE)) {
            String[] rules = DATA_RULE.split(",");
            for (String rule : rules) {
                validRules.append(rule);
            }
        }
        if ("1".equals(AJAX_ABLE) && StringUtils.isNotEmpty(AJAX_URL)) {
            validRules.append(AJAX_URL);
        }
        if (validRules.length() > 1) {
            paramsConfig.put("DATA_RULE", validRules.toString());
        }
        String resultCode = PlatStringUtil.getFreeMarkResult(FORMCONTROL_TPLCODE, paramsConfig);
        Map<String, String> result = new HashMap<String, String>();
        result.put("genCode", resultCode);
        return result;
    }

    /**
     * 生成可操作列表组控件代码
     *
     * @param paramsConfig
     * @return
     */
    @Override
    public Map<String, String> genOperListgroupControlCode(Map<String, Object> paramsConfig) {
        String FORMCONTROL_TPLCODE = (String) paramsConfig.get("FORMCONTROL_TPLCODE");
        String EVENT_JSON = (String) paramsConfig.get("EVENT_JSON");
        List<Map> eventList = JSON.parseArray(EVENT_JSON, Map.class);
        for (Map event : eventList) {
            String CLICK_FNTYPE = (String) event.get("CLICK_FNTYPE");
            if ("1".equals(CLICK_FNTYPE)) {
                paramsConfig.put("TREE_CLICKFNNAME", event.get("CLICK_FNNAME"));
                paramsConfig.put("TREE_CLICKFN", event.get("CLICK_FN"));
            } else if ("2".equals(CLICK_FNTYPE)) {
                paramsConfig.put("ADDCLICKFN", event.get("CLICK_FNNAME"));
                paramsConfig.put("ADDCLICKFNCONTENT", event.get("CLICK_FN"));
            } else if ("3".equals(CLICK_FNTYPE)) {
                paramsConfig.put("DELCLICKFN", event.get("CLICK_FNNAME"));
                paramsConfig.put("DELCLICKFNCONTENT", event.get("CLICK_FN"));
            } else if ("4".equals(CLICK_FNTYPE)) {
                paramsConfig.put("EDITCLICKFN", event.get("CLICK_FNNAME"));
                paramsConfig.put("EDITCLICKFNCONTENT", event.get("CLICK_FN"));
            }
        }
        String resultCode = PlatStringUtil.getFreeMarkResult(FORMCONTROL_TPLCODE, paramsConfig);
        Map<String, String> result = new HashMap<String, String>();
        result.put("genCode", resultCode);
        return result;
    }

    /**
     * 生成向导控件代码
     *
     * @param paramsConfig
     * @return
     */
    @Override
    public Map<String, String> genWizardCode(Map<String, Object> paramsConfig) {
        String FORMCONTROL_TPLCODE = (String) paramsConfig.get("FORMCONTROL_TPLCODE");
        String wizardstepJson = (String) paramsConfig.get("wizardstepJson");
        List<Map> stepList = JSON.parseArray(wizardstepJson, Map.class);
        paramsConfig.put("stepList", stepList);
        String resultCode = PlatStringUtil.getFreeMarkResult(FORMCONTROL_TPLCODE, paramsConfig);
        Map<String, String> result = new HashMap<String, String>();
        result.put("genCode", resultCode);
        return result;
    }

    /**
     * 生成Echart图形报表相关代码
     *
     * @param paramsConfig
     * @return
     */
    @Override
    public Map<String, String> genEchartCode(Map<String, Object> paramsConfig) {
        String FORMCONTROL_TPLCODE = (String) paramsConfig.get("FORMCONTROL_TPLCODE");
        String QUERYCONDITIONIDS = (String) paramsConfig.get("QUERYCONDITIONIDS");
        String FORMCONTROL_ID = (String) paramsConfig.get("FORMCONTROL_ID");
        if (StringUtils.isNotEmpty(QUERYCONDITIONIDS)) {
            this.queryConditionService.updateSn(QUERYCONDITIONIDS.split(","));
        }
        List<Map<String, Object>> querys = queryConditionService.findByFormControlId(FORMCONTROL_ID);
        //定义是否存在可视的查询条件
        String haveVisiableQuery = "-1";
        for (Map<String, Object> query : querys) {
            String QUERYCONDITION_CTRLTYPE = (String) query.get("QUERYCONDITION_CTRLTYPE");
            if (!"1".equals(QUERYCONDITION_CTRLTYPE)) {
                haveVisiableQuery = "1";
            }
        }
        paramsConfig.put("haveVisiableQuery", haveVisiableQuery);
        paramsConfig.put("querys", querys);
        if (StringUtils.isNotEmpty((String) paramsConfig.get("PLAT_DESIGNMODE"))) {
            paramsConfig.put("PLAT_DESIGNMODE", "true");
        }
        String resultCode = PlatStringUtil.getFreeMarkResult(FORMCONTROL_TPLCODE, paramsConfig);
        Map<String, String> result = new HashMap<String, String>();
        result.put("genCode", resultCode);
        return result;
    }

}
