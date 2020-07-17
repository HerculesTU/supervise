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
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.workflow.dao.EventDao;
import com.housoo.platform.workflow.service.EventService;
import com.housoo.platform.workflow.service.NodeBindService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 描述 流程事件业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-08 09:43:22
 */
@Service("eventService")
public class EventServiceImpl extends BaseServiceImpl implements EventService {

    /**
     * 所引入的dao
     */
    @Resource
    private EventDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据过滤器获取事件配置的接口列表
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map> findByEventInter(SqlFilter sqlFilter) {
        String INTER_EVENTID = sqlFilter.getRequest().getParameter("INTER_EVENTID");
        if (StringUtils.isNotEmpty(INTER_EVENTID)) {
            Map<String, Object> event = dao.getRecord("JBPM6_EVENT"
                    , new String[]{"EVENT_ID"}, new Object[]{INTER_EVENTID});
            String EVENT_INTERJSON = (String) event.get("EVENT_INTERJSON");
            if (StringUtils.isNotEmpty(EVENT_INTERJSON)) {
                return JSON.parseArray(EVENT_INTERJSON, Map.class);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取配置的下一排序值
     *
     * @param EVENT_FLOWDEFID
     * @param EVENT_FLOWVERSION
     * @return
     */
    @Override
    public int getNextSn(String EVENT_FLOWDEFID, String EVENT_FLOWVERSION) {
        return dao.getMaxSn(EVENT_FLOWDEFID, EVENT_FLOWVERSION) + 1;
    }

    /**
     * 保存事件并且级联保存环节配置
     *
     * @param event
     * @return
     */
    @Override
    public Map<String, Object> saveEventCascade(Map<String, Object> event) {
        String EVENT_NODEKEYS = (String) event.get("EVENT_NODEKEYS");
        String EVENT_ID = (String) event.get("EVENT_ID");
        String EVENT_FLOWDEFID = (String) event.get("EVENT_FLOWDEFID");
        String EVENT_FLOWVERSION = (String) event.get("EVENT_FLOWVERSION");
        if (StringUtils.isEmpty(EVENT_ID)) {
            int nextSn = this.getNextSn(EVENT_FLOWDEFID, EVENT_FLOWVERSION);
            event.put("EVENT_SN", nextSn);
        }
        event = dao.saveOrUpdate("JBPM6_EVENT", event,
                SysConstants.ID_GENERATOR_UUID, null);
        EVENT_ID = (String) event.get("EVENT_ID");
        //清除旧绑定数据
        StringBuffer sql = new StringBuffer("DELETE FROM JBPM6_NODEBIND ");
        sql.append(" WHERE NODEBIND_TYPE=? AND NODEBIND_RECORDID=? ");
        dao.executeSql(sql.toString(), new Object[]{NodeBindService.BINDTYPE_EVENT, EVENT_ID});
        for (String nodeKey : EVENT_NODEKEYS.split(",")) {
            Map<String, Object> nodeBind = new HashMap<String, Object>();
            nodeBind.put("NODEBIND_TYPE", NodeBindService.BINDTYPE_EVENT);
            nodeBind.put("NODEBIND_RECORDID", EVENT_ID);
            nodeBind.put("NODEBIND_FLOWDFEID", EVENT_FLOWDEFID);
            nodeBind.put("NODEBIND_FLOWVERSION", EVENT_FLOWVERSION);
            nodeBind.put("NODEBIND_NODEKEY", nodeKey);
            nodeBind.put("NODEBIND_CREATETIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            dao.saveOrUpdate("JBPM6_NODEBIND", nodeBind, SysConstants.ID_GENERATOR_UUID, null);
        }
        return event;
    }

    /**
     * 获取绑定事件的代码
     *
     * @param defId
     * @param defVersion
     * @param nodeKey
     * @param eventType
     * @return
     */
    @Override
    public List<String> findEventCodes(String defId, String defVersion, String nodeKey, String eventType) {
        List<String> eventJsonList = dao.findEventInterJson(defId, defVersion, nodeKey);
        List<String> list = new ArrayList<String>();
        for (String eventJson : eventJsonList) {
            List<Map> eventList = JSON.parseArray(eventJson, Map.class);
            for (Map<String, Object> event : eventList) {
                String INTERFACECODE = (String) event.get("INTERFACECODE");
                String INTERTYPE = (String) event.get("INTERTYPE");
                if (eventType.equals(INTERTYPE)) {
                    if (!list.contains(INTERFACECODE)) {
                        list.add(INTERFACECODE);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 克隆事件数据
     *
     * @param oldFlowDefId
     * @param oldFlowDefVersion
     */
    @Override
    public void copyEvents(String oldFlowDefId, int oldFlowDefVersion,
                           String newFlowDefId, int newFlowDefVersion) {
        StringBuffer sql = new StringBuffer("SELECT * FROM JBPM6_EVENT E");
        sql.append(" WHERE E.EVENT_FLOWDEFID=? AND E.EVENT_FLOWVERSION=?");
        List<Map<String, Object>> bindEvents = dao.findBySql(sql.toString(),
                new Object[]{oldFlowDefId, oldFlowDefVersion}, null);
        Map<String, String> oldNewIdMap = new HashMap<String, String>();
        for (Map<String, Object> bindEvent : bindEvents) {
            String oldEventId = (String) bindEvent.get("EVENT_ID");
            bindEvent.remove("EVENT_ID");
            bindEvent.put("EVENT_FLOWDEFID", newFlowDefId);
            bindEvent.put("EVENT_FLOWVERSION", newFlowDefVersion);
            bindEvent = dao.saveOrUpdate("JBPM6_EVENT", bindEvent, SysConstants.ID_GENERATOR_UUID, null);
            oldNewIdMap.put(oldEventId, bindEvent.get("EVENT_ID").toString());
        }
        sql = new StringBuffer("SELECT * FROM JBPM6_NODEBIND N");
        sql.append(" WHERE N.NODEBIND_FLOWDFEID=? AND N.NODEBIND_FLOWVERSION=?");
        sql.append(" AND N.NODEBIND_TYPE=?");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                new Object[]{oldFlowDefId, oldFlowDefVersion, NodeBindService.BINDTYPE_EVENT}, null);
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
     * 获取列表
     *
     * @param defId
     * @param flowVersion
     * @return
     */
    @Override
    public List<Map<String, Object>> findByDefIdAndVersion(String defId, int flowVersion) {
        StringBuffer sql = new StringBuffer("SELECT * FROM JBPM6_EVENT E");
        sql.append(" WHERE E.EVENT_FLOWDEFID=? AND E.EVENT_FLOWVERSION=? ");
        sql.append("ORDER BY E.EVENT_ID DESC");
        return dao.findBySql(sql.toString(), new Object[]{defId, flowVersion}, null);
    }
}
