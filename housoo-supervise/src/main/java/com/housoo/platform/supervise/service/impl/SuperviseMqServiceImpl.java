/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.AppMqService;
import com.housoo.platform.core.service.GlobalConfigService;
import com.housoo.platform.core.service.SysUserService;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.supervise.dao.SuperviseDao;
import com.housoo.platform.supervise.service.SuperviseMqService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * 描述 督办消息推送业务相关service实现类
 *
 * @author cjr
 * @version 1.0
 * @created 2020-04-14 15:30:11
 */
@Service("superviseMqService")
public class SuperviseMqServiceImpl extends BaseServiceImpl implements SuperviseMqService {

    /**
     * 所引入的dao
     */
    @Resource
    private SuperviseDao dao;

    /**
     * AppMqService
     */
    //@Resource
    //private AppMqService appMqService;
    /**
     * SysUserService
     */
    @Resource
    private SysUserService sysUserService;
    /**
     * GlobalConfigService
     */
    @Resource
    private GlobalConfigService globalConfigService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 承办人提交反馈推送消息到消息队列
     * 消息中包含的数据信息：1.系统来源，2.用户信息（待办接收方），3.待办业务数据信息，4.待办任务描述信息
     *
     * @return
     */
    @Override
    public Map<Object, Object> pushTakerHandleInfo(Map<String, Object> params) {
        //获取全局配置中的系统名称
        Map<String, Object> supervise = dao.getRecord("tb_supervise", new String[]{"RECORD_ID"}, new Object[]{params.get("SUPERVISE_ID")});
        //根据节点ID和督办ID获取tb_supervise_node_info信息
        Map<String, Object> node = dao.getRecord("tb_supervise_node_info", new String[]{"SUPERVISE_ID", "NODE_ID"}, new Object[]{params.get("SUPERVISE_ID"), params.get("NODE_ID")});
        if (node != null) {
            //发送复杂的订阅消息---------推送承办人已办结当期待办任务
            Map<Object, Object> message1 = new HashMap<>();
            message1.put("systemName", globalConfigService.getFirstConfigMap().get("CONFIG_PROJECTNAME"));
            message1.put("recordId", node.get("RECORD_ID"));//当前处理的待办记录ID
            message1.put("userId", PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID"));
            message1.put("superviseType", supervise.get("SUPERVISE_TYPE"));
            message1.put("nodeId", params.get("NODE_ID"));
            message1.put("nodeName", dao.getRecord("tb_supervise_approve", new String[]{"NODE_ID"}, new Object[]{params.get("NODE_ID")}).get("NODE_NAME"));
            message1.put("superviseId", params.get("SUPERVISE_ID"));
            message1.put("superviseNo", supervise.get("SUPERVISE_NO"));
            message1.put("createTime", node.get("CREATE_TIME"));
            message1.put("status", "2");
            Map<Object, Object> data1 = new HashMap<>();
            data1.put("data", message1);
            //appMqService.sendComplexQueueMessage("004", data1);

            //发送复杂的订阅消息---------推送立项人待办消息
            Map<Object, Object> message2 = new HashMap<>();
            message2.put("systemName", globalConfigService.getFirstConfigMap().get("CONFIG_PROJECTNAME"));
            message2.put("userId", supervise.get("USER_ID"));
            message2.put("recordId", params.get("RECORD_ID"));
            message2.put("superviseType", supervise.get("SUPERVISE_TYPE"));
            message2.put("nodeId", params.get("NODE_ID"));
            message2.put("nodeName", dao.getRecord("tb_supervise_approve", new String[]{"NODE_ID"}, new Object[]{params.get("NODE_ID")}).get("NODE_NAME"));
            message2.put("superviseId", params.get("SUPERVISE_ID"));
            message2.put("superviseNo", supervise.get("SUPERVISE_NO"));
            message2.put("msgDesc", "您有一条督办编号为" + supervise.get("SUPERVISE_NO") + "的督办任务待" + message2.get("nodeName") + "确认。");
            message2.put("status", "1");
            message2.put("createTime", params.get("CREATE_TIME"));
            Map<Object, Object> data2 = new HashMap<>();
            data2.put("data", message2);
            //appMqService.sendComplexQueueMessage("004", data2);

            data2.put("success", true);
            return data2;
        }
        return null;
    }

    /**
     * 立项人 创建 督办任务推送消息到消息队列
     *
     * @param supervise
     * @return
     */
    @Override
    public Map<Object, Object> pushSponsorCreateInfo(Map<String, Object> supervise) {
        //根据节点ID和督办ID获取tb_supervise_node_info tb_supervise_task 信息
        Map<String, Object> node = dao.getRecord("tb_supervise_node_info", new String[]{"SUPERVISE_ID", "NODE_ID"}, new Object[]{supervise.get("RECORD_ID"), supervise.get("CURRENT_NODE")});
        Map<String, Object> task = dao.getRecord("tb_supervise_task", new String[]{"SUPERVISE_ID"}, new Object[]{supervise.get("RECORD_ID")});
        if (node != null) {
            //发送复杂的订阅消息---------推送承办人有新的待办任务
            Map<Object, Object> message1 = new HashMap<>();
            message1.put("systemName", globalConfigService.getFirstConfigMap().get("CONFIG_PROJECTNAME"));
            message1.put("userId", task.get("USER_ID"));
            message1.put("recordId", node.get("RECORD_ID"));//当前处理的待办记录ID
            message1.put("superviseType", supervise.get("SUPERVISE_TYPE"));
            message1.put("nodeId", supervise.get("CURRENT_NODE"));
            message1.put("nodeName", dao.getRecord("tb_supervise_approve", new String[]{"NODE_ID"}, new Object[]{supervise.get("CURRENT_NODE")}).get("NODE_NAME"));
            message1.put("superviseId", supervise.get("RECORD_ID"));
            message1.put("superviseNo", supervise.get("SUPERVISE_NO"));
            message1.put("msgDesc", "您有一条督办编号为" + supervise.get("SUPERVISE_NO") + "的督办任务待" + message1.get("nodeName") + "办理。");
            message1.put("createTime", node.get("CREATE_TIME"));
            message1.put("status", "1");
            Map<Object, Object> data = new HashMap<>();
            data.put("data", message1);
            //appMqService.sendComplexQueueMessage("004", data);
            data.put("success", true);
            return data;
        }
        return null;
    }

    /**
     * 立项人 批复 督办任务推送消息到消息队列
     * zxl
     *
     * @return
     */
    @Override
    public Map<Object, Object> pushSponsorHandleInfo(Map<String, Object> params) {
        //获取全局配置中的系统名称
        Map<String, Object> supervise = dao.getRecord("tb_supervise", new String[]{"RECORD_ID"}, new Object[]{params.get("SUPERVISE_ID")});
        //根据节点ID和督办ID获取tb_supervise_node_info  tb_supervise_task 信息
        Map<String, Object> node = dao.getRecord("tb_supervise_node_info", new String[]{"SUPERVISE_ID", "NODE_ID"}, new Object[]{params.get("SUPERVISE_ID"), params.get("NODE_ID")});
        Map<String, Object> nodeCurrent = dao.getRecord("tb_supervise_node_info", new String[]{"SUPERVISE_ID", "NODE_ID"}, new Object[]{params.get("SUPERVISE_ID"), supervise.get("CURRENT_NODE")});
        Map<String, Object> task = dao.getRecord("tb_supervise_task", new String[]{"SUPERVISE_ID"}, new Object[]{supervise.get("RECORD_ID")});
        if (node != null) {
            //发送复杂的订阅消息---------推送 立项人 已办结当前待办任务
            Map<Object, Object> message1 = new HashMap<>();
            message1.put("systemName", globalConfigService.getFirstConfigMap().get("CONFIG_PROJECTNAME"));
            message1.put("recordId", node.get("RECORD_ID"));//当前处理的待办记录ID
            message1.put("userId", PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID"));
            message1.put("superviseType", supervise.get("SUPERVISE_TYPE"));
            message1.put("nodeId", params.get("NODE_ID"));
            message1.put("nodeName", dao.getRecord("tb_supervise_approve", new String[]{"NODE_ID"}, new Object[]{params.get("NODE_ID")}).get("NODE_NAME"));
            message1.put("superviseId", params.get("SUPERVISE_ID"));
            message1.put("superviseNo", supervise.get("SUPERVISE_NO"));
            message1.put("createTime", node.get("CREATE_TIME"));
            message1.put("status", "2");
            Map<Object, Object> data1 = new HashMap<>();
            data1.put("data", message1);
            //appMqService.sendComplexQueueMessage("004", data1);

            //发送复杂的订阅消息---------推送 承办人 有新的待办消息
            if (!"9".equals(supervise.get("STATUS").toString())) {
                Map<Object, Object> message2 = new HashMap<>();
                message2.put("systemName", globalConfigService.getFirstConfigMap().get("CONFIG_PROJECTNAME"));
                message2.put("userId", task.get("USER_ID"));
                message2.put("recordId", params.get("RECORD_ID"));
                message2.put("superviseType", supervise.get("SUPERVISE_TYPE"));
                message2.put("nodeId", supervise.get("CURRENT_NODE"));
                message2.put("nodeName", dao.getRecord("tb_supervise_approve", new String[]{"NODE_ID"}, new Object[]{supervise.get("CURRENT_NODE")}).get("NODE_NAME"));
                message2.put("superviseId", params.get("SUPERVISE_ID"));
                message2.put("superviseNo", supervise.get("SUPERVISE_NO"));
                message2.put("msgDesc", "您有一条督办编号为" + supervise.get("SUPERVISE_NO") + "的督办任务待" + message2.get("nodeName") + "办理。");
                message2.put("status", "1");
                message2.put("createTime", nodeCurrent.get("CREATE_TIME"));
                Map<Object, Object> data2 = new HashMap<>();
                data2.put("data", message2);
                //appMqService.sendComplexQueueMessage("004", data2);
                data2.put("success", true);
                return data2;
            } else {
                data1.put("success", true);
                return data1;
            }
        }
        return null;
    }


}
