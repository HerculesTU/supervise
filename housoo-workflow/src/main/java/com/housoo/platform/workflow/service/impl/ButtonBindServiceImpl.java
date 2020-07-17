/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.workflow.dao.ButtonBindDao;
import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.workflow.service.ButtonBindService;
import com.housoo.platform.workflow.service.NodeBindService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 按钮绑定业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-07 09:49:51
 */
@Service("buttonBindService")
public class ButtonBindServiceImpl extends BaseServiceImpl implements ButtonBindService {

    /**
     * 所引入的dao
     */
    @Resource
    private ButtonBindDao dao;
    /**
     *
     */
    @Resource
    private NodeBindService nodeBindService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取绑定的按钮列表
     *
     * @param sqlFilter
     * @param fieldInfo
     * @return
     */
    @Override
    public List<Map<String, Object>> findBySqlFilter(SqlFilter sqlFilter, Map<String, Object> fieldInfo) {
        String FLOWDEF_ID = sqlFilter.getRequest().getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = sqlFilter.getRequest().getParameter("FLOWDEF_VERSION");
        if (StringUtils.isNotEmpty(FLOWDEF_VERSION)) {
            int flowVersion = Integer.parseInt(FLOWDEF_VERSION);
            StringBuffer sql = new StringBuffer("SELECT T.BTNBIND_ID,T.BTNBIND_NAME,");
            sql.append("T.BTNBIND_ICON,T.BTNBIND_COLOR,T.BTNBIND_CLICKFN,T.BTNBIND_SN,T.BTNBIND_QUERYABLE");
            sql.append(" FROM JBPM6_BUTTONBIND T WHERE T.BTNBIND_FLOWDEFID=?");
            sql.append(" AND T.BTNBIND_FLOWVERSION=? ORDER BY T.BTNBIND_SN ASC ");
            List<Map<String, Object>> buttonList = dao.findBySql(sql.toString(),
                    new Object[]{FLOWDEF_ID, flowVersion}, null);
            for (Map<String, Object> button : buttonList) {
                String BTNBIND_ID = (String) button.get("BTNBIND_ID");
                List<Map<String, Object>> nodeList = nodeBindService.findBindNodeList(FLOWDEF_ID,
                        flowVersion, BTNBIND_ID, NodeBindService.BINDTYPE_BUTTON);
                StringBuffer BTNBIND_NODENAMES = new StringBuffer("");
                for (int i = 0; i < nodeList.size(); i++) {
                    if (i > 0) {
                        BTNBIND_NODENAMES.append(",");
                    }
                    BTNBIND_NODENAMES.append(nodeList.get(i).get("NODE_NAME"));
                }
                button.put("BTNBIND_NODENAMES", BTNBIND_NODENAMES.toString());
            }
            return buttonList;
        } else {
            return null;
        }

    }


    /**
     * 获取下一个排序值
     *
     * @param BTNBIND_FLOWDEFID
     * @param BTNBIND_FLOWVERSION
     * @return
     */
    @Override
    public int getNextSn(String BTNBIND_FLOWDEFID, String BTNBIND_FLOWVERSION) {
        int maxSn = dao.getMaxSn(BTNBIND_FLOWDEFID, BTNBIND_FLOWVERSION);
        return maxSn + 1;
    }

    /**
     * 新增或者编辑按钮绑定信息
     *
     * @param buttonBind
     * @return
     */
    @Override
    public Map<String, Object> saveCascadeBind(Map<String, Object> buttonBind) {
        String BTNBIND_NODEKEYS = (String) buttonBind.get("BTNBIND_NODEKEYS");
        String BTNBIND_ID = (String) buttonBind.get("BTNBIND_ID");
        String BTNBIND_FLOWDEFID = (String) buttonBind.get("BTNBIND_FLOWDEFID");
        String BTNBIND_FLOWVERSION = (String) buttonBind.get("BTNBIND_FLOWVERSION");
        if (StringUtils.isEmpty(BTNBIND_ID)) {
            int nextSn = this.getNextSn(BTNBIND_FLOWDEFID, BTNBIND_FLOWVERSION);
            buttonBind.put("BTNBIND_SN", nextSn);
        }
        buttonBind = dao.saveOrUpdate("JBPM6_BUTTONBIND", buttonBind,
                SysConstants.ID_GENERATOR_UUID, null);
        BTNBIND_ID = (String) buttonBind.get("BTNBIND_ID");
        //清除旧绑定数据
        StringBuffer sql = new StringBuffer("DELETE FROM JBPM6_NODEBIND ");
        sql.append(" WHERE NODEBIND_TYPE=? AND NODEBIND_RECORDID=? ");
        dao.executeSql(sql.toString(), new Object[]{NodeBindService.BINDTYPE_BUTTON, BTNBIND_ID});
        for (String nodeKey : BTNBIND_NODEKEYS.split(",")) {
            Map<String, Object> nodeBind = new HashMap<String, Object>();
            nodeBind.put("NODEBIND_TYPE", NodeBindService.BINDTYPE_BUTTON);
            nodeBind.put("NODEBIND_RECORDID", BTNBIND_ID);
            nodeBind.put("NODEBIND_FLOWDFEID", BTNBIND_FLOWDEFID);
            nodeBind.put("NODEBIND_FLOWVERSION", BTNBIND_FLOWVERSION);
            nodeBind.put("NODEBIND_NODEKEY", nodeKey);
            nodeBind.put("NODEBIND_CREATETIME", "NODEBIND_CREATETIME");
            dao.saveOrUpdate("JBPM6_NODEBIND", nodeBind, SysConstants.ID_GENERATOR_UUID, null);
        }
        return buttonBind;
    }

    /**
     * 删除按钮绑定记录
     *
     * @param buttonBindIds
     */
    @Override
    public void deleteCacadeBind(String buttonBindIds) {
        //删除环节绑定记录
        StringBuffer sql = new StringBuffer("DELETE FROM JBPM6_NODEBIND ");
        sql.append(" WHERE NODEBIND_TYPE=? AND NODEBIND_RECORDID IN ");
        sql.append(PlatStringUtil.getSqlInCondition(buttonBindIds));
        dao.executeSql(sql.toString(), new Object[]{NodeBindService.BINDTYPE_BUTTON});
        dao.deleteRecords("JBPM6_BUTTONBIND", "BTNBIND_ID", buttonBindIds.split(","));
    }

    /**
     * 改变按钮权限
     *
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public boolean changeBindButton(JbpmFlowInfo jbpmFlowInfo) {
        return false;
    }

    /**
     * 更新排序
     *
     * @param BTNBIND_IDS
     */
    @Override
    public void updateSn(String BTNBIND_IDS) {
        String[] bindIds = BTNBIND_IDS.split(",");
        StringBuffer sql = new StringBuffer("UPDATE JBPM6_BUTTONBIND");
        sql.append(" SET BTNBIND_SN=? WHERE BTNBIND_ID=?");
        for (int i = 0; i < bindIds.length; i++) {
            int sn = i + 1;
            dao.executeSql(sql.toString(), new Object[]{sn, bindIds[i]});
        }
    }

    /**
     * 获取环节所绑定的按钮列表
     *
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public List<Map<String, Object>> findButtonBind(JbpmFlowInfo jbpmFlowInfo) {
        if (jbpmFlowInfo != null) {
            String isQuery = jbpmFlowInfo.getJbpmIsQuery();
            List<Object> params = new ArrayList<Object>();
            StringBuffer sql = new StringBuffer("SELECT T.* FROM JBPM6_BUTTONBIND T");
            if ("true".equals(isQuery)) {
                sql.append(" WHERE T.BTNBIND_QUERYABLE=? ");
                params.add("1");
                sql.append(" AND T.BTNBIND_ID IN (SELECT ");
                sql.append("N.NODEBIND_RECORDID FROM JBPM6_NODEBIND N");
                sql.append(" WHERE N.NODEBIND_TYPE=? AND N.NODEBIND_FLOWDFEID=?");
                sql.append(" AND N.NODEBIND_FLOWVERSION=? )");
                params.add(NodeBindService.BINDTYPE_BUTTON);
                params.add(jbpmFlowInfo.getJbpmDefId());
                params.add(jbpmFlowInfo.getJbpmDefVersion());
            } else if ("false".equals(isQuery)) {
                sql.append(" WHERE T.BTNBIND_ID IN (SELECT ");
                sql.append("N.NODEBIND_RECORDID FROM JBPM6_NODEBIND N");
                sql.append(" WHERE N.NODEBIND_TYPE=? AND N.NODEBIND_FLOWDFEID=?");
                sql.append(" AND N.NODEBIND_FLOWVERSION=? AND N.NODEBIND_NODEKEY=? )");
                //sql.append(" AND T.BTNBIND_QUERYABLE=? ");
                params.add(NodeBindService.BINDTYPE_BUTTON);
                params.add(jbpmFlowInfo.getJbpmDefId());
                params.add(jbpmFlowInfo.getJbpmDefVersion());
                params.add(jbpmFlowInfo.getJbpmOperingNodeKey());
                //params.add("-1");
            }
            sql.append(" ORDER BY T.BTNBIND_SN ASC ");
            List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                    params.toArray(), null);
            List<Map<String, Object>> buttonList = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> button : list) {
                String BTNBIND_INTER = (String) button.get("BTNBIND_INTER");
                if (StringUtils.isNotEmpty(BTNBIND_INTER)) {
                    String beanId = BTNBIND_INTER.split("[.]")[0];
                    String method = BTNBIND_INTER.split("[.]")[1];
                    Object serviceBean = PlatAppUtil.getBean(beanId);
                    boolean result = false;
                    if (serviceBean != null) {
                        Method invokeMethod;
                        try {
                            invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                                    new Class[]{JbpmFlowInfo.class});
                            result = (boolean) invokeMethod.invoke(serviceBean,
                                    new Object[]{jbpmFlowInfo});
                            if (result) {
                                buttonList.add(button);
                            }
                        } catch (Exception e) {
                            PlatLogUtil.printStackTrace(e);
                        }
                    }
                } else {
                    buttonList.add(button);
                }
            }
            return buttonList;
        } else {
            return new ArrayList<Map<String, Object>>();
        }
    }

    /**
     * 拷贝旧的按钮绑定信息
     *
     * @param oldFlowDefId
     * @param oldFlowDefVersion
     */
    @Override
    public void copyBindButtons(String oldFlowDefId, int oldFlowDefVersion,
                                String newFlowDefId, int newFlowDefVersion) {
        StringBuffer sql = new StringBuffer("SELECT * FROM JBPM6_BUTTONBIND B");
        sql.append(" WHERE B.BTNBIND_FLOWDEFID=? AND B.BTNBIND_FLOWVERSION=?");
        List<Map<String, Object>> bindButtons = dao.findBySql(sql.toString(),
                new Object[]{oldFlowDefId, oldFlowDefVersion}, null);
        Map<String, String> oldNewIdMap = new HashMap<String, String>();
        for (Map<String, Object> bindButton : bindButtons) {
            String oldBindId = (String) bindButton.get("BTNBIND_ID");
            bindButton.remove("BTNBIND_ID");
            bindButton.put("BTNBIND_FLOWDEFID", newFlowDefId);
            bindButton.put("BTNBIND_FLOWVERSION", newFlowDefVersion);
            bindButton = dao.saveOrUpdate("JBPM6_BUTTONBIND", bindButton, SysConstants.ID_GENERATOR_UUID, null);
            oldNewIdMap.put(oldBindId, bindButton.get("BTNBIND_ID").toString());
        }
        sql = new StringBuffer("SELECT * FROM JBPM6_NODEBIND N");
        sql.append(" WHERE N.NODEBIND_FLOWDFEID=? AND N.NODEBIND_FLOWVERSION=?");
        sql.append(" AND N.NODEBIND_TYPE=?");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                new Object[]{oldFlowDefId, oldFlowDefVersion, NodeBindService.BINDTYPE_BUTTON}, null);
        for (Map<String, Object> nodeBind : list) {
            String oldRecordId = (String) nodeBind.get("NODEBIND_RECORDID");
            String newRecordId = oldNewIdMap.get(oldRecordId);
            nodeBind.remove("NODEBIND_ID");
            nodeBind.put("NODEBIND_RECORDID", newRecordId);
            nodeBind.put("NODEBIND_FLOWDFEID", newFlowDefId);
            nodeBind.put("NODEBIND_FLOWVERSION", newFlowDefVersion);
            dao.saveOrUpdate("JBPM6_NODEBIND", nodeBind, SysConstants.ID_GENERATOR_UUID, null);
        }
    }

    /**
     * 根据流程定义ID和流程版本号获取列表
     *
     * @param defId
     * @param flowVersion
     * @return
     */
    @Override
    public List<Map<String, Object>> findByDefIdAndVersion(String defId, int flowVersion) {
        StringBuffer sql = new StringBuffer("SELECT * FROM JBPM6_BUTTONBIND");
        sql.append(" B WHERE B.BTNBIND_FLOWDEFID=? AND B.BTNBIND_FLOWVERSION=? ");
        sql.append("ORDER BY B.BTNBIND_ID ASC");
        return dao.findBySql(sql.toString(), new Object[]{defId, flowVersion}, null);
    }


}
