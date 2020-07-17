package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.dao.FormControlDao;
import com.housoo.platform.core.service.*;
import com.housoo.platform.core.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 描述 FormControl业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-02-21 17:26:03
 */
@Service("formControlService")
public class FormControlServiceImpl extends BaseServiceImpl implements FormControlService {
    /**
     * log4J声明
     */
    private static Log logger = LogFactory.getLog(FormControlServiceImpl.class);
    /**
     * 所引入的dao
     */
    @Resource
    private FormControlDao dao;
    /**
     *
     */
    @Resource
    private FieldConfigService fieldConfigService;
    /**
     *
     */
    @Resource
    private UiCompService uiCompService;
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
    private DesignService designService;
    /**
     *
     */
    @Resource
    private ModuleService moduleService;
    /**
     *
     */
    @Resource
    private TableColService tableColService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据设计IDS删除表单控件
     *
     * @param designIds
     */
    @Override
    public void deleteByDesignIds(String designIds) {
        StringBuffer sql = new StringBuffer("DELETE FROM PLAT_APPMODEL_FORMCONTROL");
        sql.append(" WHERE FORMCONTROL_DESIGN_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition(designIds));
        dao.executeSql(sql.toString(), null);
    }

    /**
     * @param root
     * @param parentFile
     * @param fileExts
     * @return
     */
    public Map<String, Object> readExternalFiles(Map<String, Object> root,
                                                 File parentFile, Set<String> fileExts, Set<String> needCheckIdSet) {
        if (root == null) {
            root = new HashMap<String, Object>();
            root.put("id", parentFile.getAbsolutePath());
            root.put("name", parentFile.getName());
            root.put("open", true);
            root.put("nocheck", true);
        }
        File[] childFiles = parentFile.listFiles();
        List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
        for (File file : childFiles) {
            Map<String, Object> child = new HashMap<String, Object>();
            String id = file.getAbsolutePath();
            child.put("id", id);
            child.put("name", file.getName());
            if (!file.isHidden() && !"WEB-INF".equals(file.getName())) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    String format = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if (fileExts.contains(format)) {
                        String resId = id.substring(id.indexOf("webapp\\") + 8, id.length());
                        child.put("id", resId);
                        if (needCheckIdSet.contains(resId)) {
                            child.put("checked", true);
                        }
                        children.add(child);
                    }
                } else {
                    child.put("nocheck", "true");
                    children.add(child);
                }
                if (file.isDirectory()) {
                    readExternalFiles(child, file, fileExts, needCheckIdSet);
                }
            }

        }
        root.put("children", children);
        return root;
    }

    /**
     * 获取外部资源树形数据
     *
     * @return
     */
    @Override
    public Map<String, Object> getExternalResTreeData(String needCheckIds) {
        String prjPath = PlatPropUtil.getPropertyValue("config.properties", "genCodeProject");
        StringBuffer folderPath = new StringBuffer(prjPath);
        folderPath.append("webapp");
        File parentFile = new File(folderPath.toString());
        Set<String> fileExts = new HashSet<String>();
        fileExts.add("css");
        fileExts.add("js");
        Set<String> needCheckIdSet = new HashSet<String>();
        if (StringUtils.isNotEmpty(needCheckIds)) {
            needCheckIdSet = new HashSet<String>(Arrays.asList(needCheckIds.split(",")));
        }
        Map<String, Object> root = this.readExternalFiles(null, parentFile, fileExts, needCheckIdSet);
        return root;
    }

    /**
     * 获取控件生成的代码和返回的控件名称
     *
     * @param controlConfigData
     * @param formControl
     * @return
     */
    private Map<String, String> getControlCodeAndName(Map<String, Object> controlConfigData
            , Map<String, Object> formControl) {
        Map<String, Object> genCodeParams = new HashMap<String, Object>();
        genCodeParams.putAll(formControl);
        genCodeParams.putAll(controlConfigData);
        String FORMCONTROL_COMPCODE = (String) genCodeParams.get("FORMCONTROL_COMPCODE");
        Map<String, Object> uiCompInfo = uiCompService.getRecord("PLAT_APPMODEL_UICOMP",
                new String[]{"COMP_CODE"}, new Object[]{FORMCONTROL_COMPCODE});
        String COMP_JAVAINTERFACE = (String) uiCompInfo.get("COMP_JAVAINTERFACE");
        String beanId = COMP_JAVAINTERFACE.split("[.]")[0];
        String method = COMP_JAVAINTERFACE.split("[.]")[1];
        Object serviceBean = PlatAppUtil.getBean(beanId);
        Map<String, String> getCodeAndName = null;
        if (serviceBean != null) {
            Method invokeMethod;
            try {
                invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                        new Class[]{Map.class});
                getCodeAndName = (Map<String, String>) invokeMethod.invoke(serviceBean,
                        new Object[]{genCodeParams});
            } catch (Exception e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
        return getCodeAndName;
    }


    /**
     * 保存控件信息数据
     *
     * @param formControl
     */
    @Override
    public void saveFormControl(Map<String, Object> formControl) {
        String FORMCONTROL_ID = (String) formControl.get("FORMCONTROL_ID");
        Map<String, Object> controlConfigData = fieldConfigService.getFieldMapInfo(FORMCONTROL_ID);
        formControl.put("PLAT_DESIGNMODE", "true");
        Map<String, String> designCode = this.getControlCodeAndName(controlConfigData, formControl);
        formControl.put("FORMCONTROL_DESIGNCODE", designCode.get("genCode"));
        if (StringUtils.isNotEmpty(designCode.get("compName"))) {
            formControl.put("FORMCONTROL_NAME", designCode.get("compName"));
        }
        formControl.remove("PLAT_DESIGNMODE");
        Map<String, String> genCode = this.getControlCodeAndName(controlConfigData, formControl);
        formControl.put("FORMCONTROL_FINCODE", genCode.get("genCode"));
        formControl = this.saveOrUpdateTreeData("PLAT_APPMODEL_FORMCONTROL", formControl
                , SysConstants.ID_GENERATOR_UUID, null);
        String DESIGN_ID = (String) formControl.get("FORMCONTROL_DESIGN_ID");
        this.designService.deleteGenCodeByDesignId(DESIGN_ID);
    }

    /**
     * 根据表单控件ID级联删除字段配置信息
     *
     * @param formControlId
     */
    @Override
    public void deleteCascadeFieldConfig(String formControlId, String childctrolIds) {
        Map<String, Object> formControl = dao.getRecord("PLAT_APPMODEL_FORMCONTROL",
                new String[]{"FORMCONTROL_ID"}, new Object[]{formControlId});
        String DESIGN_ID = (String) formControl.get("FORMCONTROL_DESIGN_ID");
        this.designService.deleteGenCodeByDesignId(DESIGN_ID);
        String targetCtrolIds = formControlId;
        if (StringUtils.isNotEmpty(childctrolIds)) {
            targetCtrolIds = targetCtrolIds + "," + childctrolIds;
        }
        StringBuffer sql = new StringBuffer("DELETE FROM PLAT_APPMODEL_FIELDCONFIG ");
        sql.append(" WHERE FIELDCONFIG_FORMCONTROLID IN ");
        sql.append("(SELECT T.FORMCONTROL_ID FROM PLAT_APPMODEL_FORMCONTROL T");
        sql.append(" WHERE T.FORMCONTROL_ID IN ").append(PlatStringUtil.getSqlInCondition(targetCtrolIds));
        sql.append(" )");
        dao.executeSql(sql.toString(), null);
        //级联删除查询条件
        this.queryConditionService.deleteCascadeByFormControlId(formControlId);
        //级联删除操作按钮
        this.tableButtonService.deleteCascadeByFormControlId(formControlId);
        sql = new StringBuffer("DELETE FROM PLAT_APPMODEL_FORMCONTROL ");
        sql.append(" WHERE FORMCONTROL_ID IN ").append(PlatStringUtil.getSqlInCondition(targetCtrolIds));
        dao.executeSql(sql.toString(), null);
    }

    /**
     * 根据设计ID和父亲ID获取生成的代码和ID
     *
     * @param designId
     * @param parentId
     * @return
     */
    @Override
    public List<Map<String, Object>> findGenCodeAndId(String designId, String parentId) {
        StringBuffer sql = new StringBuffer("SELECT T.FORMCONTROL_ID,T.FORMCONTROL_DESIGNCODE");
        sql.append(",T.FORMCONTROL_PARENTCOMPID,T.FORMCONTROL_FINCODE,");
        sql.append("T.FORMCONTROL_COMPCODE ");
        sql.append(" FROM PLAT_APPMODEL_FORMCONTROL T WHERE T.FORMCONTROL_PARENTID=?");
        sql.append(" AND T.FORMCONTROL_DESIGN_ID=? ORDER BY T.FORMCONTROL_TREESN ASC");
        return dao.findBySql(sql.toString(), new Object[]{parentId, designId}, null);
    }

    /**
     * 获取关联的数据库列表
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map> findGridSqlConfigSubTables(SqlFilter sqlFilter) {
        return null;
    }

    /**
     * 根据表单控件ID获取模版代码
     *
     * @param formControlId
     * @return
     */
    @Override
    public String getTplCode(String formControlId) {
        return dao.getTplCode(formControlId);
    }

    /**
     * 根据设计ID获取控件列表
     *
     * @param designId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByDesignId(String designId) {
        StringBuffer sql = new StringBuffer("SELECT * FROM PLAT_APPMODEL_FORMCONTROL T");
        sql.append(" WHERE T.FORMCONTROL_DESIGN_ID=? ");
        sql.append("ORDER BY T.FORMCONTROL_CREATETIME ASC");
        return dao.findBySql(sql.toString(), new Object[]{designId}, null);
    }

    /**
     * 根据设计ID删除空的模版代码控件
     *
     * @param designId
     */
    public void deleteEmptyTplCodeControls(String designId) {
        StringBuffer sql = new StringBuffer("DELETE FROM PLAT_APPMODEL_FORMCONTROL");
        sql.append(" WHERE FORMCONTROL_DESIGN_ID=? AND FORMCONTROL_TPLCODE IS NULL");
        dao.executeSql(sql.toString(), new Object[]{designId});
    }

    /**
     * 根据设计ID生成控件代码
     *
     * @param designId
     */
    @Override
    public void updateCmpCode(String designId, String platDesignMode) {
        this.deleteEmptyTplCodeControls(designId);
    }

    /**
     * 根据设计id和父亲ID获取配置的控件列表
     *
     * @param designId
     * @param parentId
     * @return
     */
    @Override
    public List<Map<String, Object>> findList(String designId, String parentId) {
        StringBuffer sql = new StringBuffer("SELECT * FROM PLAT_APPMODEL_FORMCONTROL T");
        sql.append(" WHERE T.FORMCONTROL_DESIGN_ID=? AND T.FORMCONTROL_PARENTID=?");
        sql.append(" ORDER BY T.FORMCONTROL_TREESN ASC");
        return dao.findBySql(sql.toString(), new Object[]{designId, parentId}, null);
    }

    /**
     * 克隆孩子表单控件
     *
     * @param sourceDesignId
     * @param sourceParentId
     * @param newDesignId
     * @param newParentId
     */
    public void copyChildrenFormControl(String sourceDesignId, String sourceParentId,
                                        String newDesignId, String newParentId) {
        List<Map<String, Object>> children = this.findList(sourceDesignId, sourceParentId);
        if (children != null && children.size() > 0) {
            for (Map<String, Object> child : children) {
                String sourceControlId = (String) child.get("FORMCONTROL_ID");
                Map<String, Object> newControl = child;
                newControl.remove("FORMCONTROL_ID");
                newControl.remove("FORMCONTROL_CREATETIME");
                newControl.remove("FORMCONTROL_PATH");
                newControl.put("FORMCONTROL_DESIGN_ID", newDesignId);
                newControl.put("FORMCONTROL_PARENTID", newParentId);
                //获取源父亲组件ID
                String sourceParentCompId = (String) child.get("FORMCONTROL_PARENTCOMPID");
                if (sourceParentCompId.contains("_")) {
                    String index = sourceParentCompId.substring(sourceParentCompId.lastIndexOf("_") + 1,
                            sourceParentCompId.length());
                    newControl.put("FORMCONTROL_PARENTCOMPID", newParentId + "_" + index);
                } else {
                    newControl.put("FORMCONTROL_PARENTCOMPID", newParentId);
                }
                newControl = this.saveOrUpdateTreeData("PLAT_APPMODEL_FORMCONTROL", newControl,
                        SysConstants.ID_GENERATOR_UUID, null);
                String newControlId = (String) newControl.get("FORMCONTROL_ID");
                //克隆字段配置信息
                this.fieldConfigService.copyFieldConfigs(sourceControlId, newControlId);
                //克隆查询条件信息
                this.queryConditionService.copyQuesConditions(sourceControlId, newControlId);
                //克隆按钮配置信息
                this.tableButtonService.copyTableButtons(sourceControlId, newControlId);
                //克隆配置的列数据信息
                this.tableColService.copyTableCols(sourceControlId, newControlId);
                this.copyChildrenFormControl(sourceDesignId, sourceControlId, newDesignId, newControlId);
            }
        }
    }

    /**
     * 克隆表单控件信息
     *
     * @param sourceDesignId
     * @param newDesignId
     */
    @Override
    public void copyFormControl(String sourceDesignId, String newDesignId) {
        List<Map<String, Object>> topList = this.findList(sourceDesignId, "0");
        for (Map<String, Object> top : topList) {
            String sourceControlId = (String) top.get("FORMCONTROL_ID");
            Map<String, Object> newTop = top;
            newTop.remove("FORMCONTROL_ID");
            newTop.remove("FORMCONTROL_CREATETIME");
            newTop.remove("FORMCONTROL_PATH");
            newTop.put("FORMCONTROL_DESIGN_ID", newDesignId);
            newTop = this.saveOrUpdateTreeData("PLAT_APPMODEL_FORMCONTROL", newTop,
                    SysConstants.ID_GENERATOR_UUID, null);
            String newControlId = (String) newTop.get("FORMCONTROL_ID");
            //克隆字段配置信息
            this.fieldConfigService.copyFieldConfigs(sourceControlId, newControlId);
            //克隆查询条件信息
            this.queryConditionService.copyQuesConditions(sourceControlId, newControlId);
            //克隆按钮配置信息
            this.tableButtonService.copyTableButtons(sourceControlId, newControlId);
            //克隆列配置信息
            this.tableColService.copyTableCols(sourceControlId, newControlId);
            this.copyChildrenFormControl(sourceDesignId, sourceControlId, newDesignId, newControlId);
        }
    }

    /**
     * 更新控件排序
     *
     * @param designId
     * @param dragTreeNodeId
     * @param dragTreeNodeNewLevel
     * @param targetNodeId
     * @param targetNodeLevel
     * @param moveType
     */
    @Override
    public void updateFormControlSn(String designId, String dragTreeNodeId,
                                    int dragTreeNodeNewLevel, String targetNodeId, int targetNodeLevel,
                                    String targetPlatComId, String moveType) {
        //更新排序值
        this.updateTreeSn("PLAT_APPMODEL_FORMCONTROL", dragTreeNodeId,
                dragTreeNodeNewLevel, targetNodeId, targetNodeLevel, moveType);
        if ("inner".equals(moveType)) {
            //更新父亲控件ID
            this.updateControlParentCompId(targetPlatComId, dragTreeNodeId);
        }
        //清除已经生成的代码
        this.designService.deleteGenCodeByDesignId(designId);
    }

    /**
     * 更新父亲控件的组件ID
     *
     * @param parentCompId
     */
    private void updateControlParentCompId(String parentCompId, String controlId) {
        //FORMCONTROL_PARENTID
        String parentId = null;
        if (parentCompId.contains("_")) {
            parentId = parentCompId.substring(0, parentCompId.indexOf("_"));
        } else {
            parentId = parentCompId;
        }
        StringBuffer sql = new StringBuffer("UPDATE PLAT_APPMODEL_FORMCONTROL ");
        sql.append(" SET FORMCONTROL_PARENTCOMPID=? ");
        sql.append(",FORMCONTROL_PARENTID=? ");
        sql.append(" WHERE FORMCONTROL_ID=? ");
        dao.executeSql(sql.toString(), new Object[]{parentCompId, parentId, controlId});
    }

}
