/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.workflow.dao.NodeBindDao;
import com.housoo.platform.workflow.service.FlowNodeService;
import com.housoo.platform.workflow.service.FormFieldService;
import com.housoo.platform.workflow.service.NodeBindService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 描述 流程绑定业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-06 13:24:42
 */
@Service("nodeBindService")
public class NodeBindServiceImpl extends BaseServiceImpl implements NodeBindService {

    /**
     * 所引入的dao
     */
    @Resource
    private NodeBindDao dao;
    /**
     *
     */
    @Resource
    private FlowNodeService flowNodeService;
    /**
     *
     */
    @Resource
    private FormFieldService formFieldService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 判断是否绑定过表单
     * @param recordId
     * @param defId
     * @param flowVersion
     * @return
     */
    @Override
    public boolean haveBindSame(String recordId, String defId,
                                int flowVersion, int bindType) {
        Map<String, Object> nodeBind = dao.getRecord("JBPM6_NODEBIND",
                new String[]{"NODEBIND_TYPE", "NODEBIND_RECORDID", "NODEBIND_FLOWDFEID", "NODEBIND_FLOWVERSION"},
                new Object[]{bindType, recordId, defId, flowVersion});
        if (nodeBind != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否多次绑定节点
     *
     * @param nodeKey
     * @param defId
     * @param flowVersion
     * @param bindType
     * @return
     */
    @Override
    public boolean haveBindMultNode(String nodeKey, String defId, int flowVersion, int bindType) {
        Map<String, Object> nodeBind = dao.getRecord("JBPM6_NODEBIND",
                new String[]{"NODEBIND_TYPE", "NODEBIND_NODEKEY", "NODEBIND_FLOWDFEID", "NODEBIND_FLOWVERSION"},
                new Object[]{bindType, nodeKey, defId, flowVersion});
        if (nodeBind != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 保存表单和节点间绑定关系
     *
     * @param FLOWDEF_ID
     * @param FLOWDEF_VERSION
     * @param FORM_ID
     * @param nodeKeys
     */
    @Override
    public void saveFormNodesBind(String FLOWDEF_ID, String FLOWDEF_VERSION
            , String FORM_ID, String nodeKeys, String OLDFORM_ID) {
        int flowVersion = Integer.parseInt(FLOWDEF_VERSION);
        //清除冲突数据
        dao.deleteRecord("JBPM6_NODEBIND", new String[]{"NODEBIND_RECORDID",
                        "NODEBIND_FLOWDFEID", "NODEBIND_FLOWVERSION", "NODEBIND_TYPE"},
                new Object[]{FORM_ID, FLOWDEF_ID, flowVersion, NodeBindService.BINDTYPE_FORM});
        for (String nodeKey : nodeKeys.split(",")) {
            Map<String, Object> bindInfo = new HashMap<String, Object>();
            bindInfo.put("NODEBIND_TYPE", NodeBindService.BINDTYPE_FORM);
            bindInfo.put("NODEBIND_RECORDID", FORM_ID);
            bindInfo.put("NODEBIND_FLOWDFEID", FLOWDEF_ID);
            bindInfo.put("NODEBIND_FLOWVERSION", flowVersion);
            bindInfo.put("NODEBIND_NODEKEY", nodeKey);
            bindInfo.put("NODEBIND_CREATETIME", PlatDateTimeUtil.
                    formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            dao.saveOrUpdate("JBPM6_NODEBIND", bindInfo, SysConstants.ID_GENERATOR_UUID, null);
           /* //获取是否配置过数据
            Map<String,Object> oldBindInfo = this.getRecord("JBPM6_NODEBIND",
                    new String[]{"NODEBIND_TYPE","NODEBIND_RECORDID","NODEBIND_FLOWDFEID",
                    "NODEBIND_FLOWVERSION","NODEBIND_NODEKEY"
            }, new Object[]{NodeBindService.BINDTYPE_FORM,FORM_ID,FLOWDEF_ID,flowVersion,nodeKey});
            if(oldBindInfo==null){
                Map<String,Object> bindInfo =new HashMap<String,Object>();
                bindInfo.put("NODEBIND_TYPE", NodeBindService.BINDTYPE_FORM);
                bindInfo.put("NODEBIND_RECORDID", FORM_ID);
                bindInfo.put("NODEBIND_FLOWDFEID", FLOWDEF_ID);
                bindInfo.put("NODEBIND_FLOWVERSION", flowVersion);
                bindInfo.put("NODEBIND_NODEKEY", nodeKey);
                bindInfo.put("NODEBIND_CREATETIME",PlatDateTimeUtil.
                        formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                dao.saveOrUpdate("JBPM6_NODEBIND",bindInfo,SysConstants.ID_GENERATOR_UUID,null);
            }*/
        }
    }

    /**
     * 获取绑定列表信息
     *
     * @param flowDef 流程定义信息
     * @param nodeKey 节点key
     * @return
     */
    @Override
    public List<Map<String, Object>> findBindNodeList(Map<String, Object> flowDef, String nodeKey) {
        String FLOWDEF_ID = (String) flowDef.get("FLOWDEF_ID");
        String FLOWDEF_VERSION = flowDef.get("FLOWDEF_VERSION").toString();
        StringBuffer sql = new StringBuffer("SELECT B.* FROM JBPM6_NODEBIND B");
        sql.append(" WHERE B.NODEBIND_FLOWDFEID=? AND B.NODEBIND_FLOWVERSION=? ");
        sql.append(" AND B.NODEBIND_NODEKEY=? ORDER BY B.NODEBIND_CREATETIME ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{FLOWDEF_ID,
                FLOWDEF_VERSION, nodeKey}, null);
        return list;
    }

    /**
     * 获取绑定的节点信息
     *
     * @param defId
     * @param flowVersion
     * @param busRecordId
     * @param bindType
     * @return
     */
    @Override
    public List<Map<String, Object>> findBindNodeList(String defId,
                                                      int flowVersion, String busRecordId, int bindType) {
        List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
        List<String> keyList = dao.findBindNodeKeyList(defId, flowVersion, busRecordId, bindType);
        List<Map> flowNodeList = flowNodeService.findDefNodeList(defId, flowVersion);
        for (String key : keyList) {
            Map<String, Object> node = new HashMap<String, Object>();
            for (Map<String, Object> flowNode : flowNodeList) {
                String nodeKey = flowNode.get("key").toString();
                String text = flowNode.get("text").toString();
                if (nodeKey.equals(key)) {
                    node.put("NODE_KEY", nodeKey);
                    node.put("NODE_NAME", text);
                    nodeList.add(node);
                    break;
                }

            }
        }
        return nodeList;
    }


    /**
     * 获取列表数据
     *
     * @param sqlFilter
     * @param fieldInfo
     * @return
     */
    @Override
    public List<Map<String, Object>> findBindFormSqlFilter(SqlFilter sqlFilter, Map<String, Object> fieldInfo) {
        String FLOWDEF_ID = sqlFilter.getRequest().getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = sqlFilter.getRequest().getParameter("FLOWDEF_VERSION");
        if (StringUtils.isNotEmpty(FLOWDEF_VERSION)) {
            int flowVersion = Integer.parseInt(FLOWDEF_VERSION);
            StringBuffer sql = new StringBuffer("SELECT F.FLOWFORM_ID,F.FLOWFORM_NAME");
            sql.append(" FROM JBPM6_FLOWFORM F WHERE F.FLOWFORM_ID ");
            sql.append("IN (select D.NODEBIND_RECORDID from JBPM6_NODEBIND D ");
            sql.append("WHERE D.NODEBIND_FLOWDFEID=? AND D.NODEBIND_FLOWVERSION=?");
            sql.append(" AND D.NODEBIND_TYPE=?) ORDER BY F.FLOWFORM_CREATETIME DESC");
            List<Map<String, Object>> formList = dao.findBySql(sql.toString(),
                    new Object[]{FLOWDEF_ID, FLOWDEF_VERSION, NodeBindService.BINDTYPE_FORM}, null);
            for (Map<String, Object> form : formList) {
                String formId = (String) form.get("FLOWFORM_ID");
                List<Map<String, Object>> bindNodes = this.findBindNodeList(FLOWDEF_ID,
                        flowVersion, formId, NodeBindService.BINDTYPE_FORM);
                StringBuffer NODE_NAMES = new StringBuffer("");
                for (int i = 0; i < bindNodes.size(); i++) {
                    if (i > 0) {
                        NODE_NAMES.append(",");
                    }
                    NODE_NAMES.append(bindNodes.get(i).get("NODE_NAME"));
                }
                form.put("NODE_NAMES", NODE_NAMES.toString());
            }
            return formList;
        } else {
            return null;
        }
    }

    /**
     * 更新表单字段权限JSON数据
     *
     * @param formId
     * @param defId
     * @param flowVersion
     */
    @Override
    public void updateFieldAuthJson(String formId, String defId,
                                    int flowVersion, String key) {
        List<Map<String, Object>> formFields = formFieldService.findByFormId(formId);
        Map<String, Object> nodeBind = dao.getRecord("JBPM6_NODEBIND",
                new String[]{"NODEBIND_TYPE", "NODEBIND_RECORDID", "NODEBIND_FLOWDFEID",
                        "NODEBIND_FLOWVERSION", "NODEBIND_NODEKEY"
                }, new Object[]{NodeBindService.BINDTYPE_FORM, formId, defId, flowVersion, key});
        String NODEBIND_FIELDAUTHJSON = (String) nodeBind.get("NODEBIND_FIELDAUTHJSON");
        Map<String, String[]> oldFieldMap = null;
        if (StringUtils.isNotEmpty(NODEBIND_FIELDAUTHJSON)) {
            List<Map> fieldAuthList = JSON.parseArray(NODEBIND_FIELDAUTHJSON, Map.class);
            oldFieldMap = new HashMap<String, String[]>();
            for (Map fieldAuth : fieldAuthList) {
                String FORMFIELD_NAME = (String) fieldAuth.get("FORMFIELD_NAME");
                String FORMFIELD_ALLOWBLANK = (String) fieldAuth.get("FORMFIELD_ALLOWBLANK");
                String FORMFIELD_AUTH = (String) fieldAuth.get("FORMFIELD_AUTH");
                oldFieldMap.put(FORMFIELD_NAME, new String[]{FORMFIELD_ALLOWBLANK, FORMFIELD_AUTH});
            }
        }
        List<Map<String, Object>> fieldAuthList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> formField : formFields) {
            formField.remove("FORMFIELD_ID");
            formField.remove("FORMFIELD_FORMID");
            formField.remove("FORMFIELD_CREATETIME");
            String FORMFIELD_NAME = (String) formField.get("FORMFIELD_NAME");
            if (oldFieldMap != null && oldFieldMap.get(FORMFIELD_NAME) != null) {
                formField.put("FORMFIELD_ALLOWBLANK", oldFieldMap.get(FORMFIELD_NAME)[0]);
                formField.put("FORMFIELD_AUTH", oldFieldMap.get(FORMFIELD_NAME)[1]);
            } else {
                formField.put("FORMFIELD_AUTH", "write");
            }
            fieldAuthList.add(formField);
        }
        NODEBIND_FIELDAUTHJSON = JSON.toJSONString(fieldAuthList);
        nodeBind.put("NODEBIND_FIELDAUTHJSON", NODEBIND_FIELDAUTHJSON);
        dao.saveOrUpdate("JBPM6_NODEBIND", nodeBind, SysConstants.ID_GENERATOR_UUID, null);
    }

    /**
     * 获取节点配置的字段权限列表
     *
     * @param sqlFilter
     * @param fieldConfig
     * @return
     */
    @Override
    public List<Map> findFieldAuthList(SqlFilter sqlFilter
            , Map<String, Object> fieldConfig) {
        String FLOWDEF_ID = sqlFilter.getRequest().getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = sqlFilter.getRequest().getParameter("FLOWDEF_VERSION");
        String NODE_KEY = sqlFilter.getRequest().getParameter("NODE_KEY");
        if (StringUtils.isNotEmpty(FLOWDEF_ID)) {
            Map<String, Object> nodeBind = dao.getRecord("JBPM6_NODEBIND",
                    new String[]{"NODEBIND_FLOWDFEID", "NODEBIND_FLOWVERSION",
                            "NODEBIND_NODEKEY", "NODEBIND_TYPE"},
                    new Object[]{FLOWDEF_ID, FLOWDEF_VERSION, NODE_KEY, NodeBindService.BINDTYPE_FORM});
            if (nodeBind != null) {
                String NODEBIND_FIELDAUTHJSON = (String) nodeBind.get("NODEBIND_FIELDAUTHJSON");
                return JSON.parseArray(NODEBIND_FIELDAUTHJSON, Map.class);
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    /**
     * 更新表单字段权限
     *
     * @param FLOWDEF_ID
     * @param FLOWDEF_VERSION
     * @param NODE_KEY
     * @param fieldNames
     * @param authValue
     */
    @Override
    public void updateFieldAuth(String FLOWDEF_ID, String FLOWDEF_VERSION,
                                String NODE_KEY, String fieldNames, String authValue) {
        Map<String, Object> nodeBind = dao.getRecord("JBPM6_NODEBIND",
                new String[]{"NODEBIND_TYPE", "NODEBIND_FLOWDFEID", "NODEBIND_FLOWVERSION", "NODEBIND_NODEKEY"},
                new Object[]{NodeBindService.BINDTYPE_FORM, FLOWDEF_ID, FLOWDEF_VERSION, NODE_KEY});
        String NODEBIND_FIELDAUTHJSON = (String) nodeBind.get("NODEBIND_FIELDAUTHJSON");
        List<Map> fieldList = JSON.parseArray(NODEBIND_FIELDAUTHJSON, Map.class);
        Set<String> fieldNameSet = new HashSet<String>(Arrays.asList(fieldNames.split(",")));
        for (Map<String, Object> field : fieldList) {
            String FORMFIELD_NAME = (String) field.get("FORMFIELD_NAME");
            if (fieldNameSet.contains(FORMFIELD_NAME)) {
                field.put("FORMFIELD_AUTH", authValue);
            }
        }
        NODEBIND_FIELDAUTHJSON = JSON.toJSONString(fieldList);
        nodeBind.put("NODEBIND_FIELDAUTHJSON", NODEBIND_FIELDAUTHJSON);
        dao.saveOrUpdate("JBPM6_NODEBIND", nodeBind, SysConstants.ID_GENERATOR_UUID, null);
    }

    /**
     * 更新表单字段允许非空属性
     *
     * @param FLOWDEF_ID
     * @param FLOWDEF_VERSION
     * @param NODE_KEY
     * @param fieldNames
     * @param allowBlank
     */
    @Override
    public void updateFieldMustOrNot(String FLOWDEF_ID, String FLOWDEF_VERSION,
                                     String NODE_KEY, String fieldNames, String allowBlank) {
        Map<String, Object> nodeBind = dao.getRecord("JBPM6_NODEBIND",
                new String[]{"NODEBIND_TYPE", "NODEBIND_FLOWDFEID", "NODEBIND_FLOWVERSION", "NODEBIND_NODEKEY"},
                new Object[]{NodeBindService.BINDTYPE_FORM, FLOWDEF_ID, FLOWDEF_VERSION, NODE_KEY});
        String NODEBIND_FIELDAUTHJSON = (String) nodeBind.get("NODEBIND_FIELDAUTHJSON");
        List<Map> fieldList = JSON.parseArray(NODEBIND_FIELDAUTHJSON, Map.class);
        Set<String> fieldNameSet = new HashSet<String>(Arrays.asList(fieldNames.split(",")));
        for (Map<String, Object> field : fieldList) {
            String FORMFIELD_NAME = (String) field.get("FORMFIELD_NAME");
            if (fieldNameSet.contains(FORMFIELD_NAME)) {
                field.put("FORMFIELD_ALLOWBLANK", allowBlank);
            }
        }
        NODEBIND_FIELDAUTHJSON = JSON.toJSONString(fieldList);
        nodeBind.put("NODEBIND_FIELDAUTHJSON", NODEBIND_FIELDAUTHJSON);
        dao.saveOrUpdate("JBPM6_NODEBIND", nodeBind, SysConstants.ID_GENERATOR_UUID, null);
    }

    /**
     * 更新节点配置信息
     *
     * @param defId
     * @param defVersion
     * @param nodeKey
     * @param taskNature
     * @param handleUpType
     * @param handleUpDays
     */
    @Override
    public void updateNodeBindConfig(String defId, String defVersion, String nodeKey,
                                     String taskNature, String handleUpType, String handleUpDays, String backType) {
        //清除已经配置的信息
        StringBuffer sql = new StringBuffer("DELETE FROM JBPM6_NODEBIND ");
        sql.append(" WHERE NODEBIND_FLOWDFEID=?");
        sql.append(" AND NODEBIND_FLOWVERSION=? AND NODEBIND_NODEKEY=?");
        sql.append(" AND NODEBIND_TYPE=? AND NODEBIND_TASKNATURE IS NOT NULL");
        dao.executeSql(sql.toString(), new Object[]{defId, defVersion, nodeKey, NodeBindService.BINDTYPE_NODECONIG});
        //保存新配置
        Map<String, Object> nodeBind = new HashMap<String, Object>();
        nodeBind.put("NODEBIND_TYPE", NodeBindService.BINDTYPE_NODECONIG);
        nodeBind.put("NODEBIND_FLOWDFEID", defId);
        nodeBind.put("NODEBIND_FLOWVERSION", defVersion);
        nodeBind.put("NODEBIND_NODEKEY", nodeKey);
        nodeBind.put("NODEBIND_TASKNATURE", taskNature);
        nodeBind.put("NODEBIND_HANDUPTYPE", handleUpType);
        if (StringUtils.isNotEmpty(handleUpDays)) {
            nodeBind.put("NODEBIND_HANDUPDAYS", handleUpDays);
        }
        nodeBind.put("NODEBIND_BACKTYPE", backType);
        dao.saveOrUpdate("JBPM6_NODEBIND", nodeBind, SysConstants.ID_GENERATOR_UUID, null);
    }

    /**
     * 更新节点配置信息
     *
     * @param defId
     * @param defVersion
     * @param nodeKey
     * @param nodeBind
     */
    @Override
    public void updateNodeBindConfig(String defId, String defVersion, String nodeKey, Map<String, Object> nodeBind) {
        //清除已经配置的信息
        StringBuffer sql = new StringBuffer("DELETE FROM JBPM6_NODEBIND");
        sql.append(" WHERE NODEBIND_FLOWDFEID=?");
        sql.append(" AND NODEBIND_FLOWVERSION=? AND NODEBIND_NODEKEY=?");
        sql.append(" AND NODEBIND_TYPE=? AND NODEBIND_TASKNATURE IS NOT NULL");
        dao.executeSql(sql.toString(), new Object[]{defId, defVersion, nodeKey, NodeBindService.BINDTYPE_NODECONIG});
        Map<String, Object> newNodeBind = new HashMap<String, Object>();
        newNodeBind.putAll(nodeBind);
        newNodeBind.put("NODEBIND_TYPE", NodeBindService.BINDTYPE_NODECONIG);
        newNodeBind.put("NODEBIND_FLOWDFEID", defId);
        newNodeBind.put("NODEBIND_FLOWVERSION", defVersion);
        newNodeBind.put("NODEBIND_NODEKEY", nodeKey);
        if (nodeBind.get("NODEBIND_HANDUPDAYS") == null || StringUtils.isEmpty(nodeBind.
                get("NODEBIND_HANDUPDAYS").toString())) {
            newNodeBind.put("NODEBIND_HANDUPDAYS", 0);
        }
        newNodeBind.put("NODEBIND_CREATETIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        dao.saveOrUpdate("JBPM6_NODEBIND", newNodeBind, SysConstants.ID_GENERATOR_UUID, null);
    }

    /**
     * 获取环节任务性质
     *
     * @param defId
     * @param defVersion
     * @param nodeKey
     * @return
     */
    @Override
    public String getTaskNature(String defId, String defVersion, String nodeKey) {
        return dao.getTaskNature(defId, defVersion, nodeKey);
    }

    /**
     * 获取节点绑定信息
     *
     * @param defId
     * @param defVersion
     * @param nodeKey
     * @param bindType
     * @return
     */
    @Override
    public Map<String, Object> getNodeBind(String defId, String defVersion, String nodeKey, int bindType) {
        Map<String, Object> nodeBind = dao.getRecord("JBPM6_NODEBIND",
                new String[]{"NODEBIND_TYPE", "NODEBIND_FLOWDFEID", "NODEBIND_FLOWVERSION", "NODEBIND_NODEKEY"},
                new Object[]{bindType, defId, defVersion, nodeKey});
        return nodeBind;
    }

    /**
     * 克隆节点绑定数据
     *
     * @param oldFlowDefId
     * @param oldFlowDefVersion
     */
    @Override
    public void copyNodeBinds(String oldFlowDefId, int oldFlowDefVersion,
                              String newFlowDefId, int newFlowDefVersion) {
        //进行表单和任务性质的克隆
        Map<String, String> replaceColumn = new HashMap<String, String>();
        String dbType = PlatAppUtil.getDbType();
        if ("MYSQL".equals(dbType)) {
            replaceColumn.put("NODEBIND_ID", "REPLACE(UUID(),'-','')");
        } else if ("ORACLE".equals(dbType)) {
            replaceColumn.put("NODEBIND_ID", "SYS_GUID()");
        } else if ("SQLSERVER".equals(dbType)) {
            replaceColumn.put("NODEBIND_ID", "REPLACE(newId(),'-','')");
        }
        replaceColumn.put("NODEBIND_FLOWDFEID", "?");
        replaceColumn.put("NODEBIND_FLOWVERSION", "?");
        String copySql = dao.getCopyTableSql("JBPM6_NODEBIND", replaceColumn);
        StringBuffer sql = new StringBuffer(copySql);
        sql.append(" WHERE NODEBIND_FLOWDFEID=? AND NODEBIND_FLOWVERSION=? AND NODEBIND_TYPE IN (1,4)");
        dao.executeSql(sql.toString(), new Object[]{newFlowDefId,
                newFlowDefVersion, oldFlowDefId, oldFlowDefVersion});
    }

    /**
     * 根据流程定义和版本号获取列表数据
     *
     * @param defId
     * @param flowVersion
     * @return
     */
    @Override
    public List<Map<String, Object>> findByDefIdAndVersion(String defId, int flowVersion) {
        StringBuffer sql = new StringBuffer("SELECT * FROM ");
        sql.append("JBPM6_NODEBIND J WHERE J.NODEBIND_FLOWDFEID=? AND ");
        sql.append("J.NODEBIND_FLOWVERSION=? ORDER BY J.NODEBIND_CREATETIME ASC ");
        return dao.findBySql(sql.toString(), new Object[]{defId, flowVersion}, null);
    }

    /**
     * 获取自定义的控件权限列表
     *
     * @param sqlFilter
     * @param fieldConfig
     * @return
     */
    @Override
    public List<Map> findCtrlAuthList(SqlFilter sqlFilter, Map<String, Object> fieldConfig) {
        String FLOWDEF_ID = sqlFilter.getRequest().getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = sqlFilter.getRequest().getParameter("FLOWDEF_VERSION");
        String NODE_KEY = sqlFilter.getRequest().getParameter("NODE_KEY");
        if (StringUtils.isNotEmpty(FLOWDEF_ID)) {
            Map<String, Object> nodeBind = dao.getRecord("JBPM6_NODEBIND",
                    new String[]{"NODEBIND_FLOWDFEID", "NODEBIND_FLOWVERSION",
                            "NODEBIND_NODEKEY", "NODEBIND_TYPE"},
                    new Object[]{FLOWDEF_ID, FLOWDEF_VERSION, NODE_KEY, NodeBindService.BINDTYPE_DEFCTRL});
            if (nodeBind != null) {
                String NODEBIND_FIELDAUTHJSON = (String) nodeBind.get("NODEBIND_FIELDAUTHJSON");
                return JSON.parseArray(NODEBIND_FIELDAUTHJSON, Map.class);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 转换英文和中文名称
     *
     * @param sourceEnName
     * @return
     */
    private String[] convertCtrlEnCnName(String sourceEnName) {
        String[] result = new String[2];
        if (StringUtils.isNotEmpty(sourceEnName)) {
            result[0] = sourceEnName;
            String cnName = sourceEnName.replaceAll("upload", "上传");
            cnName = cnName.replaceAll("del", "删除");
            result[1] = cnName;
        } else {
            result[0] = "none";
            result[1] = "无权限";
        }
        return result;
    }

    /**
     * 新增或者修改自定义控件权限
     *
     * @param defCtrl
     */
    @Override
    public void saveOrUpdateDefCtrl(Map<String, Object> defCtrl) {
        String FLOWDEF_ID = (String) defCtrl.get("FLOWDEF_ID");
        String FLOWDEF_VERSION = (String) defCtrl.get("FLOWDEF_VERSION");
        String NODE_KEY = (String) defCtrl.get("NODE_KEY");
        String CTRL_ENNAME = (String) defCtrl.get("CTRL_ENNAME");
        String CTRL_CNNAME = (String) defCtrl.get("CTRL_CNNAME");
        String CTRL_RIGHTSEN = (String) defCtrl.get("CTRL_RIGHTSEN");
        Map<String, Object> nodeBind = dao.getRecord("JBPM6_NODEBIND",
                new String[]{"NODEBIND_FLOWDFEID", "NODEBIND_FLOWVERSION",
                        "NODEBIND_NODEKEY", "NODEBIND_TYPE"},
                new Object[]{FLOWDEF_ID, FLOWDEF_VERSION, NODE_KEY, NodeBindService.BINDTYPE_DEFCTRL});
        if (nodeBind == null) {
            nodeBind = new HashMap<String, Object>();
            nodeBind.put("NODEBIND_TYPE", NodeBindService.BINDTYPE_DEFCTRL);
            nodeBind.put("NODEBIND_FLOWDFEID", FLOWDEF_ID);
            nodeBind.put("NODEBIND_FLOWVERSION", FLOWDEF_VERSION);
            nodeBind.put("NODEBIND_NODEKEY", NODE_KEY);
            nodeBind.put("NODEBIND_CREATETIME", PlatDateTimeUtil.formatDate(new Date(),
                    "yyyy-MM-dd HH:mm:ss"));
            nodeBind = dao.saveOrUpdate("JBPM6_NODEBIND", nodeBind,
                    SysConstants.ID_GENERATOR_UUID, null);
        }
        String NODEBIND_FIELDAUTHJSON = (String) nodeBind.get("NODEBIND_FIELDAUTHJSON");
        List<Map> authList = null;
        if (StringUtils.isNotEmpty(NODEBIND_FIELDAUTHJSON)) {
            authList = JSON.parseArray(NODEBIND_FIELDAUTHJSON, Map.class);
        } else {
            authList = new ArrayList<Map>();
        }
        boolean isUpdate = false;
        for (Map auth : authList) {
            String oldEnName = (String) auth.get("CTRL_ENNAME");
            if (oldEnName.equals(CTRL_ENNAME)) {
                auth.put("CTRL_ENNAME", CTRL_ENNAME);
                auth.put("CTRL_CNNAME", CTRL_CNNAME);
                String[] enCnName = this.convertCtrlEnCnName(CTRL_RIGHTSEN);
                auth.put("CTRL_RIGHTSEN", enCnName[0]);
                auth.put("CTRL_RIGHTSCN", enCnName[1]);
                isUpdate = true;
                break;
            }
        }
        if (!isUpdate) {
            Map auth = new HashMap();
            auth.put("CTRL_ENNAME", CTRL_ENNAME);
            auth.put("CTRL_CNNAME", CTRL_CNNAME);
            String[] enCnName = this.convertCtrlEnCnName(CTRL_RIGHTSEN);
            auth.put("CTRL_RIGHTSEN", enCnName[0]);
            auth.put("CTRL_RIGHTSCN", enCnName[1]);
            authList.add(auth);
        }
        NODEBIND_FIELDAUTHJSON = JSON.toJSONString(authList);
        nodeBind.put("NODEBIND_FIELDAUTHJSON", NODEBIND_FIELDAUTHJSON);
        nodeBind = dao.saveOrUpdate("JBPM6_NODEBIND", nodeBind,
                SysConstants.ID_GENERATOR_UUID, null);
    }

    /**
     * 获取绑定节点绑定的表单ID
     */
    @Override
    public String getNodeBindFormId(String defId, int flowVersion, String nodeKey) {
        StringBuffer sql = new StringBuffer("SELECT T.NODEBIND_RECORDID");
        sql.append(" FROM JBPM6_NODEBIND T WHERE T.NODEBIND_TYPE=?");
        sql.append(" AND T.NODEBIND_FLOWDFEID=? AND T.NODEBIND_FLOWVERSION=?");
        sql.append(" AND T.NODEBIND_NODEKEY=? ");
        String formId = dao.getUniqueObj(sql.toString(), new Object[]{
                        NodeBindService.BINDTYPE_FORM, defId, flowVersion, nodeKey},
                String.class);
        return formId;
    }
}
