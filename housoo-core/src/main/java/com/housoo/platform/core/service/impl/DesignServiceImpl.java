package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.model.TableColumn;
import com.housoo.platform.core.dao.DesignDao;
import com.housoo.platform.core.service.*;
import com.housoo.platform.core.util.*;
import jodd.jerry.Jerry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * 描述
 *
 * @author 高飞
 * @created 2017年2月4日 上午11:22:38
 */
@Service("designService")
public class DesignServiceImpl extends BaseServiceImpl implements DesignService {

    /**
     * 所引入的dao
     */
    @Resource
    private DesignDao dao;
    /**
     *
     */
    @Resource
    private FormControlService formControlService;
    /**
     *
     */
    @Resource
    private ModuleService moduleService;
    /**
     *
     */
    @Resource
    private DbManagerService dbManagerService;
    /**
     *
     */
    @Resource
    private UiCompService uiCompService;
    /**
     *
     */
    @Resource
    private FieldConfigService fieldConfigService;
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
    private TableColService tableColService;

    /**
     *
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据sqlfilter获取到列表数据
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map<String, Object>> findBySqlFilter(SqlFilter sqlFilter) {
        List<Object> params = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer("select T.DESIGN_ID,T.DESIGN_NAME");
        sql.append(",T.DESIGN_CODE,M.MODULE_NAME");
        sql.append(" from PLAT_APPMODEL_DESIGN t LEFT JOIN ");
        sql.append("PLAT_APPMODEL_MODULE M ON T.DESIGN_MODULEID=M.MODULE_ID");
        String exeSql = dao.getQuerySql(sqlFilter, sql.toString(), params);
        List<Map<String, Object>> list = dao.findBySql(exeSql,
                params.toArray(), sqlFilter.getPagingBean());
        return list;
    }

    /**
     * 获取关联的数据库列表
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map> findAssocialTables(SqlFilter sqlFilter) {
        String DESIGN_ID = sqlFilter.getRequest().getParameter("DESIGN_ID");
        if (StringUtils.isNotEmpty(DESIGN_ID)) {
            Map<String, Object> design = dao.getRecord("PLAT_APPMODEL_DESIGN",
                    new String[]{"DESIGN_ID"}, new Object[]{DESIGN_ID});
            String ASSOICAL_JSON = (String) design.get("ASSOICAL_JSON");
            if (StringUtils.isNotEmpty(ASSOICAL_JSON)) {
                List<Map> list = JSON.parseArray(ASSOICAL_JSON, Map.class);
                return list;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 生产java源代码
     *
     * @param assoical
     * @param codeType
     */
    public void genJavaCode(Map assoical, String codeType) {
        String codeTplPath = PlatAppUtil.getCodeProjectPath() + "webapp/webpages/"
                + "background/appmodel/codetpl/javatpl/";
        String PACK_NAME = (String) assoical.get("PACK_NAME");
        String CLASS_NAME = (String) assoical.get("CLASS_NAME");
        String filePath = codeTplPath + "CodeDao.ftl";
        String fileContent = PlatFileUtil.readFileString(filePath);
        String basePackage = "com/housoo/";
        if (fileContent.contains("net.evecom.")) {
            basePackage = "net/evecom/";
        }
        StringBuffer javaCodePath = new StringBuffer(PlatAppUtil.getCodeProjectPath());
        javaCodePath.append("src/").append(basePackage).append("platform/").append(PACK_NAME).append("/");
        if ("1".equals(codeType)) {
            codeTplPath += "CodeDao.ftl";
            javaCodePath.append("dao/").append(CLASS_NAME).append("Dao.java");
        } else if ("2".equals(codeType)) {
            codeTplPath += "CodeDaoImpl.ftl";
            javaCodePath.append("dao/impl/").append(CLASS_NAME).append("DaoImpl.java");
        } else if ("3".equals(codeType)) {
            codeTplPath += "CodeService.ftl";
            javaCodePath.append("service/").append(CLASS_NAME).append("Service.java");
        } else if ("4".equals(codeType)) {
            codeTplPath += "CodeServiceImpl.ftl";
            javaCodePath.append("service/impl/").append(CLASS_NAME).append("ServiceImpl.java");
        } else if ("5".equals(codeType)) {
            codeTplPath += "CodeController.ftl";
            javaCodePath.append("controller/").append(CLASS_NAME).append("Controller.java");
        }
        File javaFile = new File(javaCodePath.toString());
        if (!javaFile.exists()) {
            String tplString = PlatFileUtil.readFileString(codeTplPath);
            String resultString = PlatStringUtil.getFreeMarkResult(tplString, assoical);
            PlatFileUtil.writeDataToDisk(resultString, javaCodePath.toString(), null);
        }
    }

    /**
     * 保存设计信息并且生成JAVA源代码
     *
     * @param design
     * @return
     */
    @Override
    public Map<String, Object> saveAndGenJavaCode(Map<String, Object> design) {
        String DESIGN_ID = (String) design.get("DESIGN_ID");
        if (StringUtils.isEmpty(DESIGN_ID)) {
            design.put("DESIGN_CREATETIME", PlatDateTimeUtil.formatDate(new Date(),
                    "yyyy-MM-dd HH:mm:ss"));
        }
        design = this.saveOrUpdate("PLAT_APPMODEL_DESIGN", design,
                SysConstants.ID_GENERATOR_UUID, null);
        String DESIGN_MODULEID = (String) design.get("DESIGN_MODULEID");
        Map<String, Object> module = moduleService.getRecord("PLAT_APPMODEL_MODULE",
                new String[]{"MODULE_ID"}, new Object[]{DESIGN_MODULEID});
        String moduleName = (String) module.get("MODULE_NAME");
        String ASSOICAL_JSON = (String) design.get("ASSOICAL_JSON");
        if (StringUtils.isNotEmpty(ASSOICAL_JSON)) {
            List<Map> assoicalList = JSON.parseArray(ASSOICAL_JSON, Map.class);
            for (Map assoical : assoicalList) {
                String AUTHOR = PlatPropUtil.getPropertyValue("config.properties", "genCodeAuthor");
                String FILE_CREATETIME = PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
                String GEN_CODE = (String) assoical.get("GEN_CODE");
                if ("1".equals(GEN_CODE)) {
                    assoical.put("AUTHOR", AUTHOR);
                    assoical.put("FILE_CREATETIME", FILE_CREATETIME);
                    String TABLE_NAME = (String) assoical.get("TABLE_NAME");
                    String TABLE_PKNAME = this.findPrimaryKeyNames(TABLE_NAME).get(0);
                    assoical.put("TABLE_PKNAME", TABLE_PKNAME);
                    assoical.put("MODULE_NAME", moduleName);
                    this.genJavaCode(assoical, "1");
                    this.genJavaCode(assoical, "2");
                    this.genJavaCode(assoical, "3");
                    this.genJavaCode(assoical, "4");
                    this.genJavaCode(assoical, "5");
                }
            }
        }
        return design;
    }

    /**
     * 根据设计IDS删除设计信息集合
     *
     * @param designIds
     */
    @Override
    public void deleteCascadeFormControl(String designIds) {
        //删除字段配置信息
        StringBuffer sql = new StringBuffer("DELETE FROM PLAT_APPMODEL_FIELDCONFIG");
        sql.append(" WHERE FIELDCONFIG_FORMCONTROLID IN ( ");
        sql.append("SELECT F.FORMCONTROL_ID FROM PLAT_APPMODEL_FORMCONTROL F");
        sql.append(" WHERE F.FORMCONTROL_DESIGN_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition(designIds)).append(")");
        dao.executeSql(sql.toString(), null);
        //删除查询条件
        sql = new StringBuffer("DELETE FROM PLAT_APPMODEL_QUERYCONDITION ");
        sql.append(" WHERE QUERYCONDITION_FORMCONTROLID IN (");
        sql.append("SELECT F.FORMCONTROL_ID FROM PLAT_APPMODEL_FORMCONTROL F");
        sql.append(" WHERE F.FORMCONTROL_DESIGN_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition(designIds)).append(")");
        dao.executeSql(sql.toString(), null);
        //删除表格按钮
        sql = new StringBuffer("DELETE FROM PLAT_APPMODEL_TABLEBUTTON ");
        sql.append(" WHERE TABLEBUTTON_FORMCONTROLID IN (");
        sql.append("SELECT F.FORMCONTROL_ID FROM PLAT_APPMODEL_FORMCONTROL F");
        sql.append(" WHERE F.FORMCONTROL_DESIGN_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition(designIds)).append(")");
        dao.executeSql(sql.toString(), null);
        //删除表格列
        sql = new StringBuffer("DELETE FROM PLAT_APPMODEL_TABLECOL ");
        sql.append(" WHERE TABLECOL_FORMCONTROLID IN (");
        sql.append("SELECT F.FORMCONTROL_ID FROM PLAT_APPMODEL_FORMCONTROL F");
        sql.append(" WHERE F.FORMCONTROL_DESIGN_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition(designIds)).append(")");
        dao.executeSql(sql.toString(), null);
        //删除表单控件配置信息
        formControlService.deleteByDesignIds(designIds);
        dao.deleteRecords("PLAT_APPMODEL_DESIGN", "DESIGN_ID", designIds.split(","));

    }

    /**
     * 生成子孙控件的代码
     *
     * @param doc
     * @param parentControlId
     * @param designId
     */
    private void genChildCtrlCode(Jerry doc, String parentControlId, String designId, String designMode) {
        List<Map<String, Object>> children = formControlService.
                findGenCodeAndId(designId, parentControlId);
        if (children != null && children.size() > 0) {
            for (Map<String, Object> child : children) {
                String FORMCONTROL_DESIGNCODE = (String) child.get("FORMCONTROL_DESIGNCODE");
                String FORMCONTROL_COMPCODE = (String) child.get("FORMCONTROL_COMPCODE");
                String FORMCONTROL_ID = (String) child.get("FORMCONTROL_ID");
                //判断是否是可编辑表格
                if (StringUtils.isNotEmpty(FORMCONTROL_COMPCODE) && "edittable".equals(FORMCONTROL_COMPCODE)) {
                    //重新生成可编辑表格的行模版
                    uiCompService.genEditTableTrCode(FORMCONTROL_ID, designId);
                }
                String FORMCONTROL_FINCODE = (String) child.get("FORMCONTROL_FINCODE");
                String FORMCONTROL_PARENTCOMPID = (String) child.get("FORMCONTROL_PARENTCOMPID");
                if (StringUtils.isNotEmpty(designMode)) {
                    doc.$("[platcomid='" + FORMCONTROL_PARENTCOMPID + "']").append(FORMCONTROL_DESIGNCODE);
                } else {
                    doc.$("[platcomid='" + FORMCONTROL_PARENTCOMPID + "']").append(FORMCONTROL_FINCODE);
                }
                this.genChildCtrlCode(doc, FORMCONTROL_ID, designId, designMode);
            }
        }
    }

    /**
     * 根据设计ID获取代码
     *
     * @param designId
     * @return
     */
    @Override
    public String getDesignCode(String designId, String platDesignMode) {
        List<Map<String, Object>> topList = this.formControlService.
                findGenCodeAndId(designId, "0");
        if (topList != null && topList.size() > 0) {
            Map<String, Object> top = topList.get(0);
            String FORMCONTROL_DESIGNCODE = (String) top.get("FORMCONTROL_DESIGNCODE");
            String FORMCONTROL_FINCODE = (String) top.get("FORMCONTROL_FINCODE");
            String FORMCONTROL_ID = (String) top.get("FORMCONTROL_ID");
            Jerry doc = null;
            if (StringUtils.isNotEmpty(platDesignMode)) {
                doc = Jerry.jerry(FORMCONTROL_DESIGNCODE);
            } else {
                doc = Jerry.jerry(FORMCONTROL_FINCODE);
            }
            this.genChildCtrlCode(doc, FORMCONTROL_ID, designId, platDesignMode);
            if (StringUtils.isEmpty(platDesignMode)) {
                doc.$("body,div,form").removeAttr("platcompname");
                doc.$("body,div,form").removeAttr("platcomid");
                doc.$("body,div,form").removeAttr("uibtnsrights");
                doc.$("body,div,form").removeAttr("compcontrolid");
                doc.$("body,div,form").removeClass("platdesigncomp");
            }
            return doc.html();
        }
        return null;
    }

    /**
     * 根据设计编码获取所配置的表
     *
     * @param designCode
     * @return
     */
    @Override
    public List<Map> findAssocialTablesByDesignCdoe(String designCode) {
        if (StringUtils.isNotEmpty(designCode)) {
            Map<String, Object> designInfo = this.getRecord("PLAT_APPMODEL_DESIGN",
                    new String[]{"DESIGN_CODE"}, new Object[]{designCode});
            String ASSOICAL_JSON = (String) designInfo.get("ASSOICAL_JSON");
            if (StringUtils.isNotEmpty(ASSOICAL_JSON)) {
                List<Map> assoicalTables = JSON.parseArray(ASSOICAL_JSON, Map.class);
                for (Map<String, Object> table : assoicalTables) {
                    String className = (String) table.get("CLASS_NAME");
                    table.put("VALUE", table.get("TABLE_NAME"));
                    table.put("LABEL", table.get("TABLE_COMMNETS"));
                    table.put("CLASS_NAME", PlatStringUtil.convertFirstLetterToLower(className));
                }
                return assoicalTables;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 根据设计ID获取关联的表信息
     *
     * @param designId
     * @return
     */
    @Override
    public List<Map> findAssoicalTables(String designId) {
        if (StringUtils.isNotEmpty(designId)) {
            Map<String, Object> designInfo = this.getRecord("PLAT_APPMODEL_DESIGN",
                    new String[]{"DESIGN_ID"}, new Object[]{designId});
            String ASSOICAL_JSON = (String) designInfo.get("ASSOICAL_JSON");
            if (StringUtils.isNotEmpty(ASSOICAL_JSON)) {
                List<Map> assoicalTables = JSON.parseArray(ASSOICAL_JSON, Map.class);
                for (Map<String, Object> table : assoicalTables) {
                    table.put("VALUE", table.get("TABLE_NAME"));
                    table.put("LABEL", table.get("TABLE_COMMNETS"));
                    table.put("ALIAS", table.get("TABLE_ALIAS"));
                }
                return assoicalTables;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取可选的表字段信息
     *
     * @param designId
     * @return
     */
    @Override
    public List<Map<String, Object>> findSelectFields(String designId) {
        if (StringUtils.isNotEmpty(designId)) {
            Map<String, Object> designInfo = this.getRecord("PLAT_APPMODEL_DESIGN",
                    new String[]{"DESIGN_ID"}, new Object[]{designId});
            String ASSOICAL_JSON = (String) designInfo.get("ASSOICAL_JSON");
            if (StringUtils.isNotEmpty(ASSOICAL_JSON)) {
                List<Map> assoicalTables = JSON.parseArray(ASSOICAL_JSON, Map.class);
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (Map table : assoicalTables) {
                    String TABLE_NAME = (String) table.get("TABLE_NAME");
                    String TABLE_ALIAS = (String) table.get("TABLE_ALIAS");
                    String TABLE_COMMNETS = (String) table.get("TABLE_COMMNETS");
                    String CLASS_NAME = (String) table.get("CLASS_NAME");
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("VALUE", TABLE_NAME);
                    map.put("LABEL", TABLE_COMMNETS);
                    map.put("TREE_LEVEL", 1);
                    list.add(map);
                    List<TableColumn> columnList = dao.findTableColumnByTableName(TABLE_NAME);
                    for (TableColumn column : columnList) {
                        Map<String, Object> field = new HashMap<String, Object>();
                        field.put("TABLE_NAME", TABLE_NAME);
                        field.put("FIELD_COMMENTS", column.getColumnComments());
                        field.put("LABEL", column.getColumnName() + "(" + column.getColumnComments() + ")");
                        field.put("VALUE", TABLE_ALIAS + "." + column.getColumnName());
                        field.put("COLUMN_NAME", column.getColumnName());
                        field.put("MAX_LENGTH", column.getDataLength());
                        field.put("FIELD_NAME", PlatStringUtil.
                                convertFirstLetterToLower(CLASS_NAME) + "." + column.getColumnName());
                        field.put("ISLEAF", "true");
                        field.put("TREE_LEVEL", 2);
                        list.add(field);
                    }
                }
                list = PlatUICompUtil.getTreeSelectDatas(list);
                return list;
            }
        }
        return null;
    }

    /**
     * 根据设计id删除生成的代码
     *
     * @param designId
     */
    @Override
    public void deleteGenCodeByDesignId(String designId) {
        Map<String, Object> design = this.
                getRecord("PLAT_APPMODEL_DESIGN", new String[]{"DESIGN_ID"}, new Object[]{designId});
        String DESIGN_CODE = (String) design.get("DESIGN_CODE");
        String appPath = PlatAppUtil.getAppAbsolutePath();
        StringBuffer folderPath = new StringBuffer(appPath);
        folderPath.append("webpages/background/genui/").append(DESIGN_CODE);
        PlatFileUtil.deleteFileDir(folderPath.toString());
    }

    /**
     * 更新组件代码
     *
     * @param designIds
     */
    @Override
    public void updateGenUiCode(String designIds) {
        StringBuffer sql = new StringBuffer("SELECT F.*");
        sql.append(" FROM PLAT_APPMODEL_FORMCONTROL F WHERE ");
        sql.append("F.FORMCONTROL_DESIGN_ID IN ").append(PlatStringUtil.getSqlInCondition(designIds));
        sql.append(" ORDER BY F.FORMCONTROL_ID DESC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        for (Map<String, Object> control : list) {
            this.formControlService.saveFormControl(control);
        }
    }

    /**
     * 更新组件模版为最新,并且重新生成组件源码
     *
     * @param designIds
     */
    @Override
    public void updateNewestTpl(String designIds) {
        StringBuffer sql = new StringBuffer("SELECT F.*");
        sql.append(" FROM PLAT_APPMODEL_FORMCONTROL F WHERE ");
        sql.append("F.FORMCONTROL_DESIGN_ID IN ").append(PlatStringUtil.getSqlInCondition(designIds));
        sql.append(" ORDER BY F.FORMCONTROL_ID DESC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        for (Map<String, Object> control : list) {
            StringBuffer tplPath = new StringBuffer(PlatAppUtil.getAppAbsolutePath());
            tplPath.append("webpages/common/compdesign/");
            String FORMCONTROL_COMPCODE = (String) control.get("FORMCONTROL_COMPCODE");
            tplPath.append(FORMCONTROL_COMPCODE).append("/template.jsp");
            String FORMCONTROL_TPLCODE = PlatFileUtil.readFileString(tplPath.toString());
            control.put("FORMCONTROL_TPLCODE", FORMCONTROL_TPLCODE);
            this.formControlService.saveFormControl(control);
        }
    }

    /**
     * 获取生成的JSP代码
     *
     * @param design
     * @param appPath
     * @return
     */
    @Override
    public String saveGenJspCode(Map<String, Object> design, String appPath) {
        String tplPath = appPath + "webpages/background/appmodel/visual/uipreview_tpl.jsp";
        //获取外部资源信息
        String DESIGN_EXTERNALRESES = (String) design.get("DESIGN_EXTERNALRESES");
        if (StringUtils.isNotEmpty(DESIGN_EXTERNALRESES)) {
            String[] reses = DESIGN_EXTERNALRESES.split(",");
            List<String> externalcss = new ArrayList<String>();
            List<String> externaljs = new ArrayList<String>();
            for (String res : reses) {
                if (res.endsWith("css")) {
                    externalcss.add(res.replaceAll("\\\\", "/"));
                } else if (res.endsWith("js")) {
                    externaljs.add(res.replaceAll("\\\\", "/"));
                }
            }
            design.put("externalcss", externalcss);
            design.put("externaljs", externaljs);
        }
        String DESIGN_ID = (String) design.get("DESIGN_ID");
        //更新组件代码
        formControlService.updateCmpCode(DESIGN_ID, null);
        String genHtmlCode = this.getDesignCode(DESIGN_ID, null);
        if (StringUtils.isNotEmpty(genHtmlCode)) {
            design.put("genHtmlCode", genHtmlCode);
        }
        String viewJspTplStr = PlatFileUtil.readFileString(tplPath);
        String genJspCode = PlatStringUtil.getFreeMarkResult(viewJspTplStr, design);
        return genJspCode;
    }

    /**
     * 克隆一个设计UI信息
     *
     * @param sourceDesignId
     * @param newDesignCode
     * @param newDesignName
     * @param newDesignModuleId
     */
    @Override
    public void copyDesignInfo(String sourceDesignId,
                               String newDesignCode, String newDesignName, String newDesignModuleId) {
        Map<String, Object> sourceDesignInfo = this.getRecord("PLAT_APPMODEL_DESIGN",
                new String[]{"DESIGN_ID"}, new Object[]{sourceDesignId});
        Map<String, Object> newDesignInfo = new HashMap<String, Object>();
        newDesignInfo.putAll(sourceDesignInfo);
        newDesignInfo.put("DESIGN_NAME", newDesignName);
        newDesignInfo.put("DESIGN_CODE", newDesignCode);
        newDesignInfo.put("DESIGN_MODULEID", newDesignModuleId);
        newDesignInfo.remove("DESIGN_ID");
        newDesignInfo.remove("DESIGN_CREATETIME");
        newDesignInfo.put("DESIGN_CREATETIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        newDesignInfo = this.saveOrUpdate("PLAT_APPMODEL_DESIGN", newDesignInfo, SysConstants.ID_GENERATOR_UUID, null);
        String newDesignId = (String) newDesignInfo.get("DESIGN_ID");
        formControlService.copyFormControl(sourceDesignId, newDesignId);
        this.updateGenUiCode(newDesignId);
    }

    /**
     * 根据设计ID或者编码生成文件的路径
     *
     * @param designIdOrCode:设计ID或者编码
     * @param fileName:产生的文件名称
     * @param isDesignId:是否设计ID
     * @return
     */
    @Override
    public String getGenPathByDesignIdOrCode(String designIdOrCode, String fileName,
                                             boolean isDesignId, boolean isSubFile) {
        String appPath = PlatAppUtil.getAppAbsolutePath();
        String DESIGN_CODE = null;
        if (isDesignId) {
            Map<String, Object> design = dao.getRecord("PLAT_APPMODEL_DESIGN",
                    new String[]{"DESIGN_ID"}, new Object[]{designIdOrCode});
            DESIGN_CODE = (String) design.get("DESIGN_CODE");
        } else {
            DESIGN_CODE = designIdOrCode;
        }
        if (isSubFile) {
            DESIGN_CODE += "_sub";
        }
        StringBuffer jspPath = new StringBuffer(appPath);
        jspPath.append("webpages/background/genui/").append(DESIGN_CODE).append("/");
        jspPath.append(fileName);
        return jspPath.toString();
    }

    /**
     * 获取可选的下拉设计数据
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> findForSelect(String params) {
        StringBuffer sql = new StringBuffer("select T.DESIGN_CODE AS VALUE,T.DESIGN_NAME AS LABEL ");
        sql.append("from PLAT_APPMODEL_DESIGN T ");
        sql.append(" ORDER BY T.DESIGN_CREATETIME DESC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        for (Map<String, Object> design : list) {
            String VALUE = (String) design.get("VALUE");
            String LABEL = (String) design.get("LABEL");
            LABEL = LABEL + "(" + VALUE + ")";
            design.put("LABEL", LABEL);
        }
        return list;
    }

    /**
     * 获取设计配置信息
     *
     * @param designIds
     * @return
     */
    @Override
    public Map<String, Object> getExportInfo(String designIds) {
        Map<String, Object> exportResult = new HashMap<String, Object>();
        List<Map<String, Object>> exportdesignList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> exportcontrolList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> exportfieldList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> exportqueryList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> exporttableButtonList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> exporttableColList = new ArrayList<Map<String, Object>>();
        String[] designIdArray = designIds.split(",");
        for (String designId : designIdArray) {
            //获取设计数据
            Map<String, Object> design = dao.getRecord("PLAT_APPMODEL_DESIGN",
                    new String[]{"DESIGN_ID"}, new Object[]{designId});
            exportdesignList.add(design);
            List<Map<String, Object>> fromControlList = formControlService.findByDesignId(designId);
            exportcontrolList.addAll(fromControlList);
            //获取字段配置数据
            List<Map<String, Object>> fieldList = fieldConfigService.findByDesignId(designId);
            exportfieldList.addAll(fieldList);
            //获取查询字段配置
            List<Map<String, Object>> queryList = queryConditionService.findByDesignId(designId);
            exportqueryList.addAll(queryList);
            //获取表格按钮配置
            List<Map<String, Object>> buttonList = tableButtonService.findByDesignId(designId);
            exporttableButtonList.addAll(buttonList);
            //获取表格列配置
            List<Map<String, Object>> tableColList = tableColService.findByDesignId(designId);
            exporttableColList.addAll(tableColList);
        }
        exportResult.put("PLAT_APPMODEL_DESIGN", exportdesignList);
        if (exportcontrolList.size() > 0) {
            exportResult.put("PLAT_APPMODEL_FORMCONTROL", exportcontrolList);
        }
        if (exportfieldList.size() > 0) {
            exportResult.put("PLAT_APPMODEL_FIELDCONFIG", exportfieldList);
        }
        if (exportqueryList.size() > 0) {
            exportResult.put("PLAT_APPMODEL_QUERYCONDITION", exportqueryList);
        }
        if (exporttableButtonList.size() > 0) {
            exportResult.put("PLAT_APPMODEL_TABLEBUTTON", exporttableButtonList);
        }
        if (exporttableColList.size() > 0) {
            exportResult.put("PLAT_APPMODEL_TABLECOL", exporttableColList);
        }
        return exportResult;
    }


    /**
     * 先删除设计ID
     *
     * @param confJson
     */
    private void deleteByDesignConfJson(String confJson) {
        Map<String, Object> defConfig = JSON.parseObject(confJson, Map.class);
        Iterator it = defConfig.entrySet().iterator();
        List<String> designIdArray = new ArrayList<String>();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
            String tableName = entry.getKey();
            List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
            for (Map<String, Object> map : list) {
                if ("PLAT_APPMODEL_DESIGN".equals(tableName)) {
                    String DESIGN_ID = (String) map.get("DESIGN_ID");
                    designIdArray.add(DESIGN_ID);
                }
            }
        }
        String selectColValues = PlatStringUtil.getListStringSplit(designIdArray);
        this.deleteCascadeFormControl(selectColValues);
    }

    /**
     * 根据JSON导入配置信息
     *
     * @param confJson
     */
    @Override
    public void importConfig(String confJson) {
        //先删除设计
        this.deleteByDesignConfJson(confJson);
        Map<String, Object> defConfig = JSON.parseObject(confJson, Map.class);
        Iterator it = defConfig.entrySet().iterator();
        List<String> designIdArray = new ArrayList<String>();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
            String tableName = entry.getKey();
            List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
            for (Map<String, Object> map : list) {
                if ("PLAT_APPMODEL_DESIGN".equals(tableName)) {
                    String DESIGN_ID = (String) map.get("DESIGN_ID");
                    designIdArray.add(DESIGN_ID);
                }
                dao.saveOrUpdate(tableName, map, SysConstants.ID_GENERATOR_ASSIGNED, null);
            }
        }
        for (String designId : designIdArray) {
            this.deleteGenCodeByDesignId(designId);
        }
    }

    /**
     * 修正父亲ID和组件ID不一致的情况
     *
     * @param designId
     */
    @Override
    public void updateParentCompIdNotSame(String designId) {
        String dbType = PlatAppUtil.getDbType();
        StringBuffer sql = new StringBuffer("UPDATE PLAT_APPMODEL_FORMCONTROL ");
        sql.append("SET FORMCONTROL_PARENTID=FORMCONTROL_PARENTCOMPID");
        sql.append(" WHERE FORMCONTROL_DESIGN_ID=? ");
        sql.append("and FORMCONTROL_PARENTID!=FORMCONTROL_PARENTCOMPID AND ");
        if ("ORACLE".equals(dbType)) {
            sql.append(" FORMCONTROL_PARENTCOMPID NOT LIKE '%\\_%' ESCAPE '\\'");
        } else if ("MYSQL".equals(dbType)) {
            sql.append(" FORMCONTROL_PARENTCOMPID NOT LIKE '%\\_%' ");
        } else if ("SQLSERVER".equals(dbType)) {
            sql.append(" FORMCONTROL_PARENTCOMPID NOT LIKE '%\\_%' ESCAPE '\\' ");
        }
        dao.executeSql(sql.toString(), new Object[]{designId});
    }

}