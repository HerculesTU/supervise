/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.*;
import com.housoo.platform.core.service.SysUserService;
import com.housoo.platform.workflow.dao.FlowDefDao;
import com.housoo.platform.workflow.model.FlowNode;
import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.workflow.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 描述 流程定义业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-03 16:12:37
 */
@Service("flowDefService")
public class FlowDefServiceImpl extends BaseServiceImpl implements FlowDefService {

    /**
     * 所引入的dao
     */
    @Resource
    private FlowDefDao dao;
    /**
     *
     */
    @Resource
    private ExecutionService executionService;
    /**
     *
     */
    @Resource
    private HistDeployService histDeployService;
    /**
     *
     */
    @Resource
    private ButtonBindService buttonBindService;
    /**
     *
     */
    @Resource
    private EventService eventService;
    /**
     *
     */
    @Resource
    private NodeAssignerService nodeAssignerService;
    /**
     *
     */
    @Resource
    private NodeBindService nodeBindService;
    /**
     *
     */
    @Resource
    private JbpmTaskService jbpmTaskService;
    /**
     *
     */
    @Resource
    private TableBindService tableBindService;
    /**
     *
     */
    @Resource
    private TimeLimitService timeLimitService;
    /**
     *
     */
    @Resource
    private WordBindService wordBindService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 设置节点和连线的JSON数据
     *
     * @param flowDef
     * @return
     */
    private Map<String, Object> setNodeAndLinkJson(Map<String, Object> flowDef) {
        String FLOWDEF_JSON = (String) flowDef.get("FLOWDEF_JSON");
        Map<String, Object> map = JSON.parseObject(FLOWDEF_JSON, Map.class);
        JSONArray nodeDataArray = (JSONArray) map.get("nodeDataArray");
        JSONArray linkDataArray = (JSONArray) map.get("linkDataArray");
        String nodeDataJson = PlatStringUtil.toJsonString(nodeDataArray,
                new String[]{"category", "loc"}, true);
        String linkDataJson = PlatStringUtil.toJsonString(linkDataArray,
                new String[]{"points", "visible", "fromPort", "toPort"}, true);
        flowDef.put("FLOWDEF_NODEJSON", nodeDataJson);
        flowDef.put("FLOWDEF_LINKJSON", linkDataJson);
        return flowDef;
    }

    /**
     * 保存流程定义信息,并且级联保存其它表配置数据
     *
     * @param flowDef
     * @return
     */
    @Override
    public Map<String, Object> saveOrUpdateCascade(Map<String, Object> flowDef) {
        String FLOWDEF_ID = (String) flowDef.get("FLOWDEF_ID");
        flowDef = this.setNodeAndLinkJson(flowDef);
        if (StringUtils.isNotEmpty(FLOWDEF_ID)) {
            Map<String, Object> oldFlowDef = dao.getRecord("JBPM6_FLOWDEF",
                    new String[]{"FLOWDEF_ID"}, new Object[]{FLOWDEF_ID});
            int oldFlowDefVersion = Integer.parseInt(oldFlowDef.get("FLOWDEF_VERSION").toString());
            //判断是否有实例数据
            //boolean isExistExe = executionService.isExistedRunning(FLOWDEF_ID, oldFlowDefVersion);
            if (true) {
                //保存历史部署数据
                histDeployService.saveHistDeploy(oldFlowDef);
                //拷贝按钮绑定信息
                buttonBindService.copyBindButtons(FLOWDEF_ID, oldFlowDefVersion, FLOWDEF_ID, oldFlowDefVersion + 1);
                //拷贝事件信息
                eventService.copyEvents(FLOWDEF_ID, oldFlowDefVersion, FLOWDEF_ID, oldFlowDefVersion + 1);
                //拷贝办理人信息
                nodeAssignerService.copyNodeAssigner(FLOWDEF_ID, oldFlowDefVersion, FLOWDEF_ID, oldFlowDefVersion + 1);
                //拷贝节点绑定信息
                nodeBindService.copyNodeBinds(FLOWDEF_ID, oldFlowDefVersion, FLOWDEF_ID, oldFlowDefVersion + 1);
                flowDef.put("FLOWDEF_VERSION", oldFlowDefVersion + 1);
            }
        } else {
            flowDef.put("FLOWDEF_CREATETIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            flowDef.put("FLOWDEF_VERSION", 1);
        }
        return dao.saveOrUpdate("JBPM6_FLOWDEF", flowDef, SysConstants.ID_GENERATOR_UUID, null);
    }

    /**
     * 根据流程类别ID获取流程定义列表
     *
     * @param typeId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByTypeId(String typeId) {
        StringBuffer sql = new StringBuffer("SELECT T.FLOWDEF_ID");
        sql.append(",T.FLOWDEF_NAME FROM JBPM6_FLOWDEF T ");
        sql.append("WHERE T.FLOWTYPE_ID=? ");
        //获取当前登录用户
        Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
        boolean isAdmin = (boolean) backLoginUser.get(SysUserService.ISADMIN_KEY);
        if (!isAdmin) {
            String grantTypeDefIds = (String) backLoginUser.get(SysUserService.FLOWDEFTYPEIDS_KEY);
            if (StringUtils.isNotEmpty(grantTypeDefIds)) {
                sql.append(" AND T.FLOWDEF_ID IN ");
                sql.append(PlatStringUtil.getSqlInCondition(grantTypeDefIds));
            } else {
                sql.append(" AND T.FLOWDEF_ID='-1' ");
            }
        }
        sql.append(" ORDER BY T.FLOWDEF_CREATETIME DESC");
        return dao.findBySql(sql.toString(), new Object[]{typeId}, null);
    }

    /**
     * 根据流程定义和版本号获取流程定义对象
     *
     * @param defId
     * @param flowVersion
     * @return
     */
    @Override
    public Map<String, Object> getFlowDefInfo(String defId, int flowVersion) {
        Map<String, Object> flowDef = dao.getRecord("JBPM6_FLOWDEF",
                new String[]{"FLOWDEF_ID", "FLOWDEF_VERSION"}, new Object[]{defId, flowVersion});
        if (flowDef == null) {
            flowDef = dao.getRecord("JBPM6_HIST_DEPLOY",
                    new String[]{"FLOWDEF_ID", "FLOWDEF_VERSION"}, new Object[]{defId, flowVersion});
        }
        return flowDef;
    }

    /**
     * 更新流程定义的时限字段
     *
     * @param FLOWDEF_ID
     * @param TOTALLIMIT_DAYS
     * @param TOTALLIMIT_TYPES
     */
    @Override
    public void updateTimeLimit(String FLOWDEF_ID, String TOTALLIMIT_DAYS, String TOTALLIMIT_TYPES) {
        //更新最新流程定义的
        StringBuffer sql = new StringBuffer("UPDATE JBPM6_FLOWDEF");
        sql.append(" SET TOTALLIMIT_DAYS=?,TOTALLIMIT_TYPES=? WHERE FLOWDEF_ID=?");
        if (StringUtils.isEmpty(TOTALLIMIT_DAYS)) {
            TOTALLIMIT_DAYS = "0";
        }
        dao.executeSql(sql.toString(), new Object[]{TOTALLIMIT_DAYS, TOTALLIMIT_TYPES, FLOWDEF_ID});
        //更新历史部署版本的
        sql = new StringBuffer("UPDATE JBPM6_HIST_DEPLOY ");
        sql.append(" SET TOTALLIMIT_DAYS=?,TOTALLIMIT_TYPES=? WHERE FLOWDEF_ID=?");
        dao.executeSql(sql.toString(), new Object[]{TOTALLIMIT_DAYS, TOTALLIMIT_TYPES, FLOWDEF_ID});

    }

    /**
     * 获取监控流程图时定义JSON
     *
     * @param flowDefJson
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public String getMonitorFlowDefJson(String flowDefJson, JbpmFlowInfo jbpmFlowInfo) {
        Map<String, Object> map = JSON.parseObject(flowDefJson, Map.class);
        JSONArray nodeDataArray = (JSONArray) map.get("nodeDataArray");
        Set<String> runningNodeKeySet = new HashSet<String>();
        if (StringUtils.isNotEmpty(jbpmFlowInfo.getJbpmRunningNodeKeys())) {
            runningNodeKeySet = new HashSet<String>(Arrays.asList(jbpmFlowInfo.
                    getJbpmRunningNodeKeys().split(",")));
        }
        Set<String> runOverNodeKeySet = new HashSet<String>();
        if (StringUtils.isNotEmpty(jbpmFlowInfo.getJbpmExeId())) {
            runOverNodeKeySet = jbpmTaskService.getRunOverTaskNodeKeySet(jbpmFlowInfo.getJbpmExeId());
        }
        for (int i = 0; i < nodeDataArray.size(); i++) {
            JSONObject node = (JSONObject) nodeDataArray.get(i);
            String key = node.get("key").toString();
            if (runOverNodeKeySet.contains(key)) {
                node.put("color", FlowNode.NODECOLOR_OVER);
            }
            if (runningNodeKeySet.contains(key)) {
                node.put("color", FlowNode.NODECOLOR_RUNNING);
            }
        }
        map.put("nodeDataArray", nodeDataArray);
        return JSON.toJSONString(map);
    }

    /**
     * 获取可选流程定义下拉框数据源
     *
     * @param paramJson
     * @return
     */
    @Override
    public List<Map<String, Object>> findTypeSelect(String paramJson) {
        StringBuffer sql = new StringBuffer("select T.FLOWDEF_ID AS VALUE,T.FLOWDEF_NAME AS LABEL ");
        sql.append("from JBPM6_FLOWDEF T ORDER BY T.FLOWDEF_CREATETIME ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        return list;
    }

    /**
     * 级联删除流程定义相关数据
     *
     * @param flowDefIds
     */
    @Override
    public void deleteCascade(String[] flowDefIds) {
        StringBuffer sql = new StringBuffer("DELETE FROM JBPM6_BUTTONBIND");
        sql.append(" WHERE BTNBIND_FLOWDEFID IN ").append(PlatStringUtil.
                getSqlInCondition(flowDefIds));
        dao.executeSql(sql.toString(), null);
        sql = new StringBuffer("DELETE FROM JBPM6_EVENT ");
        sql.append(" WHERE EVENT_FLOWDEFID IN ").append(PlatStringUtil.
                getSqlInCondition(flowDefIds));
        dao.executeSql(sql.toString(), null);
        sql = new StringBuffer("DELETE FROM JBPM6_HIST_DEPLOY ");
        sql.append(" WHERE FLOWDEF_ID IN ").append(PlatStringUtil.
                getSqlInCondition(flowDefIds));
        dao.executeSql(sql.toString(), null);
        sql = new StringBuffer("DELETE FROM JBPM6_NODEASSIGNER ");
        sql.append(" WHERE NODEASSIGNER_DEFID IN ").append(PlatStringUtil.
                getSqlInCondition(flowDefIds));
        dao.executeSql(sql.toString(), null);
        sql = new StringBuffer("DELETE FROM JBPM6_NODEBIND ");
        sql.append(" WHERE NODEBIND_FLOWDFEID IN ").append(
                PlatStringUtil.getSqlInCondition(flowDefIds));
        dao.executeSql(sql.toString(), null);
        sql = new StringBuffer("DELETE FROM JBPM6_TABLEBIND ");
        sql.append(" WHERE TABLEBIND_DEFID IN ").append(PlatStringUtil.getSqlInCondition(flowDefIds));
        dao.executeSql(sql.toString(), null);
        sql = new StringBuffer("DELETE FROM JBPM6_TIMELIMIT");
        sql.append(" WHERE TIMELIMIT_FLOWDEFID IN ").append(PlatStringUtil.getSqlInCondition(flowDefIds));
        dao.executeSql(sql.toString(), null);
        sql = new StringBuffer("DELETE FROM JBPM6_WORDBIND ");
        sql.append(" WHERE WORDBIND_FLOWDEFID IN ").append(PlatStringUtil.getSqlInCondition(flowDefIds));
        dao.executeSql(sql.toString(), null);
        //开始删除实例任务数据
        sql = new StringBuffer("DELETE from JBPM6_TASK ");
        sql.append("WHERE TASK_EXEID IN (");
        sql.append("SELECT E.EXECUTION_ID FROM JBPM6_EXECUTION E");
        sql.append(" WHERE E.FLOWDEF_ID IN ").append(PlatStringUtil.getSqlInCondition(flowDefIds));
        sql.append(" )");
        dao.executeSql(sql.toString(), null);
        //开始删除实例数据
        sql = new StringBuffer("DELETE FROM JBPM6_EXECUTION ");
        sql.append(" WHERE FLOWDEF_ID IN ").append(PlatStringUtil.getSqlInCondition(flowDefIds));
        dao.executeSql(sql.toString(), null);
        dao.deleteRecords("JBPM6_FLOWDEF", "FLOWDEF_ID", flowDefIds);
    }

    /**
     * 克隆流程定义信息
     *
     * @param targetDefId
     * @param newDefCode
     * @param newDefName
     */
    @Override
    public void cloneFlowDef(String targetDefId, String newDefCode, String newDefName) {
        //获取源流程定义
        Map<String, Object> targetFlowDef = dao.getRecord("JBPM6_FLOWDEF",
                new String[]{"FLOWDEF_ID"}, new Object[]{targetDefId});
        int targetDefVersion = Integer.parseInt(targetFlowDef.get("FLOWDEF_VERSION").toString());
        //创建新的流程定义
        Map<String, Object> newFlowDef = targetFlowDef;
        newFlowDef.remove("FLOWDEF_ID");
        newFlowDef.remove("FLOWDEF_CREATETIME");
        newFlowDef.put("FLOWDEF_CODE", newDefCode);
        newFlowDef.put("FLOWDEF_NAME", newDefName);
        newFlowDef.put("FLOWDEF_VERSION", 1);
        newFlowDef = dao.saveOrUpdate("JBPM6_FLOWDEF", newFlowDef, SysConstants.ID_GENERATOR_UUID, null);
        String newFlowDefId = (String) newFlowDef.get("FLOWDEF_ID");
        //克隆流程绑定的按钮
        this.buttonBindService.copyBindButtons(targetDefId, targetDefVersion, newFlowDefId, 1);
        //克隆流程绑定的事件
        this.eventService.copyEvents(targetDefId, targetDefVersion, newFlowDefId, 1);
        //克隆环节配置
        nodeAssignerService.copyNodeAssigner(targetDefId, targetDefVersion, newFlowDefId, 1);
        //克隆节点绑定信息
        nodeBindService.copyNodeBinds(targetDefId, targetDefVersion, newFlowDefId, 1);
    }

    /**
     * 根据filter获取网格项目
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map<String, Object>> findGridItemList(SqlFilter sqlFilter) {
        StringBuffer sql = new StringBuffer("select T.FLOWDEF_ID,T.FLOWDEF_NAME");
        sql.append(",T.FLOWDEF_CODE from JBPM6_FLOWDEF T ");
        String selectedDefIds = sqlFilter.getRequest().getParameter("selectedRecordIds");
        String iconfont = sqlFilter.getRequest().getParameter("iconfont");
        String itemconf = sqlFilter.getRequest().getParameter("itemconf");
        Map<String, String> getGridItemConf = PlatUICompUtil.getGridItemConfMap(itemconf);
        if (StringUtils.isNotEmpty(selectedDefIds)) {
            sql.append(" WHERE T.FLOWDEF_ID IN ");
            sql.append(PlatStringUtil.getSqlInCondition(selectedDefIds));
            sql.append(" ORDER BY T.FLOWDEF_CREATETIME DESC");
            List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
            list = PlatUICompUtil.getGridItemList("FLOWDEF_ID", iconfont, getGridItemConf, list);
            return list;
        } else {
            return null;
        }
    }

    /**
     * 获取导出的流程配置信息
     *
     * @param defIds
     * @return
     */
    @Override
    public Map<String, Object> getExportDefInfo(String defIds) {
        Map<String, Object> exportResult = new HashMap<String, Object>();
        List<Map<String, Object>> flowDefList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> exportButtonList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> exportEventList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> exportAssignerList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> exportNodeBindList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> exportTableList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> exportTimeList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> exportWordList = new ArrayList<Map<String, Object>>();
        String[] defIdArray = defIds.split(",");
        for (String defId : defIdArray) {
            //获取流程定义信息
            Map<String, Object> flowDef = dao.getRecord("JBPM6_FLOWDEF",
                    new String[]{"FLOWDEF_ID"}, new Object[]{defId});
            //获取版本号
            Integer FLOWDEF_VERSION = Integer.parseInt(flowDef.get("FLOWDEF_VERSION").toString());
            //获取绑定的按钮列表
            List<Map<String, Object>> buttonList = buttonBindService.findByDefIdAndVersion(defId, FLOWDEF_VERSION);
            exportButtonList.addAll(buttonList);
            //获取绑定的事件列表
            List<Map<String, Object>> eventList = this.eventService.findByDefIdAndVersion(defId, FLOWDEF_VERSION);
            exportEventList.addAll(eventList);
            //获取绑定的配置人列表
            List<Map<String, Object>> assignerList = nodeAssignerService.findByDefIdAndVesion(defId, FLOWDEF_VERSION);
            exportAssignerList.addAll(assignerList);
            //获取绑定的配置列表
            List<Map<String, Object>> nodeBindList = nodeBindService.findByDefIdAndVersion(defId, FLOWDEF_VERSION);
            exportNodeBindList.addAll(nodeBindList);
            //获取绑定的表格列表
            List<Map<String, Object>> tableList = tableBindService.findByDefIdAndVersion(defId, FLOWDEF_VERSION);
            exportTableList.addAll(tableList);
            //获取绑定的时限列表
            List<Map<String, Object>> timeList = this.timeLimitService.findByDefId(defId);
            exportTimeList.addAll(timeList);
            //获取绑定的文书列表
            List<Map<String, Object>> wordList = wordBindService.findByDefId(defId);
            exportWordList.addAll(wordList);
            flowDefList.add(flowDef);
        }
        exportResult.put("JBPM6_FLOWDEF", flowDefList);
        if (exportButtonList.size() > 0) {
            exportResult.put("JBPM6_BUTTONBIND", exportButtonList);
        }
        if (exportEventList.size() > 0) {
            exportResult.put("JBPM6_EVENT", exportEventList);
        }
        if (exportAssignerList.size() > 0) {
            exportResult.put("JBPM6_NODEASSIGNER", exportAssignerList);
        }
        if (exportNodeBindList.size() > 0) {
            exportResult.put("JBPM6_NODEBIND", exportNodeBindList);
        }
        if (exportTableList.size() > 0) {
            exportResult.put("JBPM6_TABLEBIND", exportTableList);
        }
        if (exportTimeList.size() > 0) {
            exportResult.put("JBPM6_TIMELIMIT", exportTimeList);
        }
        if (exportWordList.size() > 0) {
            exportResult.put("JBPM6_WORDBIND", exportWordList);
        }
        return exportResult;
    }

    /**
     * 根据流程定义JSON导入配置信息
     *
     * @param defConfJson
     */
    @Override
    public void importDefConfig(String defConfJson) {
        Map<String, Object> defConfig = JSON.parseObject(defConfJson, Map.class);
        Iterator it = defConfig.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
            String tableName = entry.getKey();
            List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
            for (Map<String, Object> map : list) {
                dao.saveOrUpdate(tableName, map, SysConstants.ID_GENERATOR_ASSIGNED, null);
            }
        }
    }

}
