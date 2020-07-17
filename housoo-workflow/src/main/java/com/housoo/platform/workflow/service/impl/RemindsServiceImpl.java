/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.workflow.dao.RemindsDao;
import com.housoo.platform.workflow.service.ExecutionService;
import com.housoo.platform.workflow.service.JbpmTaskService;
import com.housoo.platform.workflow.service.RemindsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 催办信息业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-11-16 08:50:10
 */
@Service("remindsService")
public class RemindsServiceImpl extends BaseServiceImpl implements RemindsService {

    /**
     * 所引入的dao
     */
    @Resource
    private RemindsDao dao;
    /**
     *
     */
    @Resource
    private JbpmTaskService jbpmTaskService;


    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 发送催办意见
     *
     * @param REMINDS_EXEIDS
     * @param REMINDS_CONTENT
     * @return
     */
    @Override
    public boolean sendReminds(String REMINDS_EXEIDS, String REMINDS_CONTENT) {
        String[] exeIds = REMINDS_EXEIDS.split(",");
        Map<String, Object> sysUser = PlatAppUtil.getBackPlatLoginUser();
        String SYSUSER_ID = (String) sysUser.get("SYSUSER_ID");
        for (String exeId : exeIds) {
            Map<String, Object> flowExe = dao.getRecord("JBPM6_EXECUTION",
                    new String[]{"EXECUTION_ID", "STATUS"},
                    new Object[]{exeId, ExecutionService.STATUS_RUNNING});
            if (flowExe != null) {
                List<Map<String, Object>> list = jbpmTaskService.
                        findRunningTaskList(exeId);
                //获取标题名称
                String flowTitle = (String) flowExe.get("EXECUTION_SUBJECT");
                StringBuffer remindTitle = new StringBuffer("【催办】");
                remindTitle.append(flowTitle);
                Map<String, Object> reminds = new HashMap<String, Object>();
                reminds.put("REMINDS_TYPE", 2);
                reminds.put("REMINDS_USERID", SYSUSER_ID);
                reminds.put("REMINDS_EXEID", exeId);
                reminds.put("REMINDS_CREATETIME", PlatDateTimeUtil.
                        formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                reminds.put("REMINDS_TITLE", remindTitle.toString());
                reminds.put("REMINDS_CONTENT", REMINDS_CONTENT);
                reminds = dao.saveOrUpdate("JBPM6_REMINDS", reminds,
                        SysConstants.ID_GENERATOR_UUID, null);
                String REMINDS_ID = (String) reminds.get("REMINDS_ID");
                for (Map<String, Object> task : list) {
                    String TASK_HANDLERIDS = (String) task.get("TASK_HANDLERIDS");
                    String taskId = (String) task.get("TASK_ID");
                    String[] handlerIds = TASK_HANDLERIDS.split(",");
                    for (String handlerId : handlerIds) {
                        Map<String, Object> receive = new HashMap<String, Object>();
                        receive.put("REMINDS_ID", REMINDS_ID);
                        receive.put("REMINDRECEIVE_USERID", handlerId);
                        receive.put("REMINDRECEIVE_TASKID", taskId);
                        receive.put("REMINDRECEIVE_READ", -1);
                        dao.saveOrUpdate("JBPM6_REMINDRECEIVE", receive,
                                SysConstants.ID_GENERATOR_UUID, null);
                    }
                }

            }
        }
        return true;
    }

    /**
     * 自动发送催办
     */
    @Override
    public void sendAutoReminds() {
        List<Map<String, Object>> taskList = jbpmTaskService.findTimeOutList();
        for (Map<String, Object> task : taskList) {
            String exeId = (String) task.get("TASK_EXEID");
            Map<String, Object> flowExe = dao.getRecord("JBPM6_EXECUTION",
                    new String[]{"EXECUTION_ID", "STATUS"},
                    new Object[]{exeId, ExecutionService.STATUS_RUNNING});
            if (flowExe != null) {
                //获取标题名称
                String flowTitle = (String) flowExe.get("EXECUTION_SUBJECT");
                StringBuffer remindTitle = new StringBuffer("【催办】");
                remindTitle.append(flowTitle);
                Map<String, Object> reminds = new HashMap<String, Object>();
                reminds.put("REMINDS_TYPE", 1);
                reminds.put("REMINDS_EXEID", exeId);
                reminds.put("REMINDS_CREATETIME", PlatDateTimeUtil.
                        formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                reminds.put("REMINDS_TITLE", remindTitle.toString());
                reminds = dao.saveOrUpdate("JBPM6_REMINDS", reminds,
                        SysConstants.ID_GENERATOR_UUID, null);
                String REMINDS_ID = (String) reminds.get("REMINDS_ID");
                String TASK_HANDLERIDS = (String) task.get("TASK_HANDLERIDS");
                String taskId = (String) task.get("TASK_ID");
                String[] handlerIds = TASK_HANDLERIDS.split(",");
                for (String handlerId : handlerIds) {
                    Map<String, Object> receive = new HashMap<String, Object>();
                    receive.put("REMINDS_ID", REMINDS_ID);
                    receive.put("REMINDRECEIVE_USERID", handlerId);
                    receive.put("REMINDRECEIVE_TASKID", taskId);
                    receive.put("REMINDRECEIVE_READ", -1);
                    dao.saveOrUpdate("JBPM6_REMINDRECEIVE", receive,
                            SysConstants.ID_GENERATOR_UUID, null);
                }

            }
        }
    }

}
