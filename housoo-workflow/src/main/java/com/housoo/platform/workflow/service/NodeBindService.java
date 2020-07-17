/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service;

import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 描述 流程绑定业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-06 13:24:42
 */
public interface NodeBindService extends BaseService {
    /**
     * 绑定类型:表单
     */
    public static final int BINDTYPE_FORM = 1;
    /**
     * 绑定类型:按钮
     */
    public static final int BINDTYPE_BUTTON = 2;
    /**
     * 绑定类型:事件
     */
    public static final int BINDTYPE_EVENT = 3;
    /**
     * 绑定类型:节点配置
     */
    public static final int BINDTYPE_NODECONIG = 4;
    /**
     * 绑定类型:自定义控件
     */
    public static final int BINDTYPE_DEFCTRL = 5;

    /**
     * 判断是否绑定过
     *
     * @param recordId
     * @param defId
     * @param flowVersion
     * @param bindType
     * @return
     */
    public boolean haveBindSame(String recordId, String defId,
                                int flowVersion, int bindType);

    /**
     * 判断是否多次绑定节点
     *
     * @param nodeKey
     * @param defId
     * @param flowVersion
     * @param bindType
     * @return
     */
    public boolean haveBindMultNode(String nodeKey, String defId, int flowVersion, int bindType);

    /**
     * 保存表单和节点间绑定关系
     *
     * @param FLOWDEF_ID
     * @param FLOWDEF_VERSION
     * @param FORM_ID
     * @param nodeKeys
     * @param OLDFORM_ID
     */
    public void saveFormNodesBind(String FLOWDEF_ID, String FLOWDEF_VERSION,
                                  String FORM_ID, String nodeKeys, String OLDFORM_ID);

    /**
     * 获取列表数据
     *
     * @param sqlFilter
     * @param fieldInfo
     * @return
     */
    public List<Map<String, Object>> findBindFormSqlFilter(SqlFilter sqlFilter, Map<String, Object> fieldInfo);

    /**
     * 获取绑定的节点信息
     *
     * @param defId
     * @param flowVersion
     * @param busRecordId
     * @param bindType
     * @return
     */
    public List<Map<String, Object>> findBindNodeList(String defId,
                                                      int flowVersion, String busRecordId, int bindType);

    /**
     * 获取绑定列表信息
     *
     * @param flowDef 流程定义信息
     * @param nodeKey 节点key
     * @return
     */
    public List<Map<String, Object>> findBindNodeList(Map<String, Object> flowDef, String nodeKey);

    /**
     * 更新表单字段权限JSON数据
     *
     * @param formId
     * @param defId
     * @param flowVersion
     * @param key
     */
    public void updateFieldAuthJson(String formId, String defId,
                                    int flowVersion, String key);

    /**
     * 获取节点配置的字段权限列表
     *
     * @param sqlFilter
     * @param fieldConfig
     * @return
     */
    public List<Map> findFieldAuthList(SqlFilter sqlFilter, Map<String, Object> fieldConfig);

    /**
     * 更新表单字段权限
     *
     * @param FLOWDEF_ID
     * @param FLOWDEF_VERSION
     * @param NODE_KEY
     * @param fieldNames
     * @param authValue
     */
    public void updateFieldAuth(String FLOWDEF_ID, String FLOWDEF_VERSION,
                                String NODE_KEY, String fieldNames, String authValue);

    /**
     * 更新表单字段允许非空属性
     *
     * @param FLOWDEF_ID
     * @param FLOWDEF_VERSION
     * @param NODE_KEY
     * @param fieldNames
     * @param allowBlank
     */
    public void updateFieldMustOrNot(String FLOWDEF_ID, String FLOWDEF_VERSION,
                                     String NODE_KEY, String fieldNames, String allowBlank);

    /**
     * 更新节点配置信息
     *
     * @param defId
     * @param defVersion
     * @param nodeKey
     * @param taskNature
     * @param handleUpType
     * @param handleUpDays
     * @param backType
     */
    public void updateNodeBindConfig(String defId, String defVersion, String nodeKey,
                                     String taskNature, String handleUpType, String handleUpDays, String backType);

    /**
     * 更新节点配置信息
     *
     * @param defId
     * @param defVersion
     * @param nodeKey
     * @param nodeBind
     */
    public void updateNodeBindConfig(String defId, String defVersion, String nodeKey, Map<String, Object> nodeBind);

    /**
     * 获取环节任务性质
     *
     * @param defId
     * @param defVersion
     * @param nodeKey
     * @return
     */
    public String getTaskNature(String defId, String defVersion, String nodeKey);

    /**
     * 获取节点绑定信息
     *
     * @param defId
     * @param defVersion
     * @param nodeKey
     * @param bindType
     * @return
     */
    public Map<String, Object> getNodeBind(String defId, String defVersion, String nodeKey, int bindType);

    /**
     * 克隆节点绑定数据
     *
     * @param oldFlowDefId
     * @param oldFlowDefVersion
     * @param newFlowDefId
     * @param newFlowDefVersion
     */
    public void copyNodeBinds(String oldFlowDefId, int oldFlowDefVersion,
                              String newFlowDefId, int newFlowDefVersion);

    /**
     * 根据流程定义和版本号获取列表数据
     *
     * @param defId
     * @param flowVersion
     * @return
     */
    public List<Map<String, Object>> findByDefIdAndVersion(String defId, int flowVersion);

    /**
     * 获取自定义的控件权限列表
     *
     * @param sqlFilter
     * @param fieldConfig
     * @return
     */
    public List<Map> findCtrlAuthList(SqlFilter sqlFilter, Map<String, Object> fieldConfig);

    /**
     * 新增或者修改自定义控件权限
     *
     * @param defCtr
     */
    public void saveOrUpdateDefCtrl(Map<String, Object> defCtr);

    /**
     * 获取绑定节点绑定的表单ID
     *
     * @param defId
     * @param flowVersion
     * @param nodeKey
     * @return
     */
    public String getNodeBindFormId(String defId, int flowVersion, String nodeKey);

}
