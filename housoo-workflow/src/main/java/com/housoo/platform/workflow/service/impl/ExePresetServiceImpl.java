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
import com.housoo.platform.workflow.dao.ExePresetDao;
import com.housoo.platform.workflow.model.FlowNextHandler;
import com.housoo.platform.workflow.model.FlowNode;
import com.housoo.platform.workflow.service.ExePresetService;
import com.housoo.platform.workflow.service.ExecutionService;
import com.housoo.platform.workflow.service.FlowNodeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 实例预设办理人业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-12-26 09:04:38
 */
@Service("exePresetService")
public class ExePresetServiceImpl extends BaseServiceImpl implements ExePresetService {

    /**
     * 所引入的dao
     */
    @Resource
    private ExePresetDao dao;
    /**
     *
     */
    @Resource
    private FlowNodeService flowNodeService;
    /**
     *
     */
    @Resource
    private ExecutionService executionService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取预设人员列表
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map> findPresetList(SqlFilter sqlFilter) {
        String EXEID = sqlFilter.getRequest().getParameter("EXEID");
        if (StringUtils.isNotEmpty(EXEID)) {
            Map<String, Object> flowExe = executionService.getRecord("JBPM6_EXECUTION",
                    new String[]{"EXECUTION_ID"}, new Object[]{EXEID});
            Map<String, Object> exePreSet = dao.getRecord("JBPM6_EXEPRESET",
                    new String[]{"EXEPRESET_EXEID"}, new Object[]{EXEID});
            if (exePreSet != null) {
                String EXEPRESET_HANDLEJSON = (String) exePreSet.get("EXEPRESET_HANDLEJSON");
                List<Map> list = JSON.parseArray(EXEPRESET_HANDLEJSON, Map.class);
                /*for(Map data:list){
                    String targetKey = null;
                    Object targetValue = null;
                    Iterator it = data.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String,Object> entry = (Map.Entry<String,Object>) it.next();
                        String key = entry.getKey();
                        if(key.contains("HANDER_IDS_")){
                            targetKey = "HANDER_IDS_LABELS";
                            targetValue = entry.getValue();
                        }
                    }
                    if(StringUtils.isNotEmpty(targetKey)){
                        data.put(targetKey, targetValue);
                    }
                }*/
                return list;
            } else {
                String defId = (String) flowExe.get("FLOWDEF_ID");
                int defVersion = Integer.parseInt(flowExe.get("EXECUTION_VERSION").toString());
                List<Map> nodeList = flowNodeService.findDefNodeList(defId,
                        defVersion, FlowNode.NODETYPE_TASK);
                List<Map> list = new ArrayList<Map>();
                for (Map node : nodeList) {
                    Map data = new HashMap();
                    data.put("NODE_KEY", node.get("key"));
                    data.put("NODE_NAME", node.get("text"));
                    data.put("HANDER_IDS", "");
                    list.add(data);
                }
                return list;
            }

        } else {
            return null;
        }
    }

    /**
     * 获取流程预设办理人列表
     *
     * @param exeId
     * @param nodeKey
     * @return
     */
    @Override
    public List<FlowNextHandler> findPresetHandler(String exeId, String nodeKey) {
        if (StringUtils.isNotEmpty(exeId)) {
            Map<String, Object> preset = this.getRecord("JBPM6_EXEPRESET",
                    new String[]{"EXEPRESET_EXEID"}, new Object[]{exeId});
            if (preset != null) {
                String EXEPRESET_HANDLEJSON = (String) preset.get("EXEPRESET_HANDLEJSON");
                List<Map> list = JSON.parseArray(EXEPRESET_HANDLEJSON, Map.class);
                String targetUserIds = null;
                for (Map data : list) {
                    String NODE_KEY = (String) data.get("NODE_KEY");
                    if (NODE_KEY.equals(nodeKey)) {
                        targetUserIds = (String) data.get("HANDER_IDS");
                        break;
                    }
                }
                if (StringUtils.isNotEmpty(targetUserIds)) {
                    String[] userIds = targetUserIds.split(",");
                    List<FlowNextHandler> handlerList = new ArrayList<FlowNextHandler>();
                    for (String userId : userIds) {
                        Map<String, Object> sysUser = dao.getRecord("PLAT_SYSTEM_SYSUSER",
                                new String[]{"SYSUSER_ID"}, new Object[]{userId});
                        FlowNextHandler handler = new FlowNextHandler();
                        handler.setAssignerId(userId);
                        handler.setAssignerName(sysUser.get("SYSUSER_NAME").toString());
                        handler.setTaskOrder(-1);
                        handlerList.add(handler);
                    }
                    return handlerList;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
