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
import com.housoo.platform.core.service.WorkdayService;
import com.housoo.platform.workflow.dao.HandupRecordDao;
import com.housoo.platform.workflow.model.FlowAssignInfo;
import com.housoo.platform.workflow.model.FlowNode;
import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.workflow.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 挂起任务业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-06-01 09:29:36
 */
@Service("handupRecordService")
public class HandupRecordServiceImpl extends BaseServiceImpl implements HandupRecordService {

    /**
     * 所引入的dao
     */
    @Resource
    private HandupRecordDao dao;
    /**
     *
     */
    @Resource
    private ExecutionService executionService;
    /**
     *
     */
    @Resource
    private FlowDefService flowDefService;
    /**
     *
     */
    @Resource
    private JbpmTaskService jbpmTaskService;
    /**
     *
     */
    @Resource
    private JbpmService jbpmService;
    /**
     *
     */
    @Resource
    private FlowNodeService flowNodeService;
    /**
     *
     */
    @Resource
    private WorkdayService workdayService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据实例ID删除数据
     *
     * @param exeId
     */
    @Override
    public void deleteByExeId(String exeId) {
        dao.deleteRecords("JBPM6_HANDUPRECORD", "RECORD_EXEID",
                new String[]{exeId});
    }

    /**
     * 保存挂起记录信息
     *
     * @param jbpmFlowInfo
     */
    @Override
    public void saveHandUpRecord(JbpmFlowInfo jbpmFlowInfo) {
        Map<String, Object> sysUser = PlatAppUtil.getBackPlatLoginUser();
        Map<String, Object> nodeBind = dao.getRecord("JBPM6_NODEBIND",
                new String[]{"NODEBIND_FLOWDFEID", "NODEBIND_FLOWVERSION", "NODEBIND_TYPE", "NODEBIND_NODEKEY"},
                new Object[]{jbpmFlowInfo.getJbpmDefId(),
                        jbpmFlowInfo.getJbpmDefVersion(), NodeBindService.BINDTYPE_NODECONIG,
                        jbpmFlowInfo.getJbpmOperingNodeKey()});
        String SYSUSER_ID = (String) sysUser.get("SYSUSER_ID");
        Map<String, Object> handUpRecord = new HashMap<String, Object>();
        handUpRecord.put("RECORD_EXEID", jbpmFlowInfo.getJbpmExeId());
        handUpRecord.put("RECORD_CREATORID", SYSUSER_ID);
        handUpRecord.put("RECORD_CREATORNAME", sysUser.get("SYSUSER_NAME"));
        handUpRecord.put("RECORD_CREATORCOMPANYPATH", sysUser.get("COMPANY_PATH"));
        handUpRecord.put("RECORD_CREATORCOMPANYID", sysUser.get("SYSUSER_COMPANYID"));
        handUpRecord.put("RECORD_BEGINDATE", PlatDateTimeUtil.
                formatDate(new Date(), "yyyy-MM-dd"));
        handUpRecord.put("RECORD_REASON", jbpmFlowInfo.getJbpmHandleOpinion());
        if (nodeBind.get("NODEBIND_HANDUPDAYS") != null) {
            handUpRecord.put("RECORD_LEFTDAYS", nodeBind.get("NODEBIND_HANDUPDAYS"));
            handUpRecord.put("RECORD_TOTALDAYS", nodeBind.get("NODEBIND_HANDUPDAYS"));
        }
        String jbpmHandleUpLimitDay = jbpmFlowInfo.getJbpmHandleUpLimitDay();
        if (StringUtils.isNotEmpty(jbpmHandleUpLimitDay)) {
            handUpRecord.put("RECORD_LEFTDAYS", jbpmHandleUpLimitDay);
            handUpRecord.put("RECORD_TOTALDAYS", jbpmHandleUpLimitDay);
        }
        handUpRecord.put("RECORD_HAVEDAYS", 0);
        handUpRecord.put("RECORD_DAYTYPE", nodeBind.get("NODEBIND_HANDUPTYPE"));
        handUpRecord.put("RECORD_TASKID", jbpmFlowInfo.getJbpmOperingTaskId());
        handUpRecord.put("RECORD_NODEKEY", jbpmFlowInfo.getJbpmOperingNodeKey());
        dao.saveOrUpdate("JBPM6_HANDUPRECORD", handUpRecord, SysConstants.ID_GENERATOR_UUID, null);
    }

    /**
     * 重启流程任务
     *
     * @param recordId
     * @return
     */
    @Override
    public Map<String, Object> restartJbpmTask(String recordId) {
        Map<String, Object> handleRecord = dao.getRecord("JBPM6_HANDUPRECORD",
                new String[]{"RECORD_ID"}, new Object[]{recordId});
        String RECORD_EXEID = (String) handleRecord.get("RECORD_EXEID");
        String RECORD_TASKID = (String) handleRecord.get("RECORD_TASKID");
        Map<String, Object> jbpmTask = jbpmTaskService.getRecord("JBPM6_TASK",
                new String[]{"TASK_ID"}, new Object[]{RECORD_TASKID});
        Map<String, Object> execution = executionService.getRecord("JBPM6_EXECUTION",
                new String[]{"EXECUTION_ID"}, new Object[]{RECORD_EXEID});
        Map<String, Object> flowDef = flowDefService.getRecord("JBPM6_FLOWDEF",
                new String[]{"FLOWDEF_ID"}, new Object[]{execution.get("FLOWDEF_ID").toString()});
        //获取开始节点
        FlowNode startNode = flowNodeService.getStartFlowNode(flowDef);
        Map<String, Object> params = new HashMap<String, Object>();
        JbpmFlowInfo jbpmFlowInfo = new JbpmFlowInfo();
        jbpmFlowInfo.setJbpmDefId(execution.get("FLOWDEF_ID").toString());
        jbpmFlowInfo.setJbpmDefVersion(execution.get("EXECUTION_VERSION").toString());
        jbpmFlowInfo.setJbpmDefCode(flowDef.get("FLOWDEF_CODE").toString());
        jbpmFlowInfo.setJbpmOperingNodeKey(jbpmTask.get("TASK_NODEKEY").toString());
        jbpmFlowInfo.setJbpmOperingTaskId(RECORD_TASKID);
        jbpmFlowInfo.setJbpmIsTempSave("false");
        jbpmFlowInfo.setJbpmIsQuery("true");
        jbpmFlowInfo.setJbpmExeId(RECORD_EXEID);
        jbpmFlowInfo.setJbpmHandleTaskStatus("7");
        jbpmFlowInfo.setJbpmStartNodeKey(startNode.getNodeKey());
        jbpmFlowInfo.setJbpmOperingHandlerType(FlowAssignInfo.ASSIGNERNATURE_BACK);
        Map<String, Object> result = jbpmService.doFlowJob(params, jbpmFlowInfo);
        handleRecord.put("RECORD_ENDDATE", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd"));
        handleRecord.put("RECORD_LASTUPDATEDAYS", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        dao.saveOrUpdate("JBPM6_HANDUPRECORD", handleRecord, SysConstants.ID_GENERATOR_UUID, null);
        return result;
    }

    /**
     * 重启流程任务
     *
     * @param taskIds
     * @return
     */
    @Override
    public void restartJbpmTasks(String taskIds) {
        String[] taskArray = taskIds.split(",");
        StringBuffer sql = new StringBuffer("SELECT * FROM ");
        sql.append("JBPM6_HANDUPRECORD T WHERE T.RECORD_TASKID=? AND T.RECORD_ENDDATE IS NULL");
        sql.append(" ORDER BY T.RECORD_CREATETIME ASC ");
        for (String taskId : taskArray) {
            List<Map<String, Object>> recordList = dao.findBySql(sql.toString(), new Object[]{taskId}, null);
            Map<String, Object> record = recordList.get(0);
            String recordId = (String) record.get("RECORD_ID");
            this.restartJbpmTask(recordId);
        }
    }

    /**
     * 判断是否存在记录
     *
     * @param exeId
     * @param sysUserId
     * @param nodeKey
     * @return
     */
    @Override
    public boolean isExists(String exeId, String sysUserId, String nodeKey) {
        int count = dao.getCount(exeId, sysUserId, nodeKey);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 更新天数
     */
    @Override
    public void updateDays() {
        StringBuffer sql = new StringBuffer("SELECT * FROM  ");
        sql.append("JBPM6_HANDUPRECORD H WHERE H.RECORD_ENDDATE IS NULL");
        sql.append(" ORDER BY H.RECORD_CREATETIME ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        for (Map<String, Object> handle : list) {
            Integer RECORD_LEFTDAYS = null;
            Integer RECORD_TOTALDAYS = null;
            if (handle.get("RECORD_LEFTDAYS") != null) {
                RECORD_LEFTDAYS = Integer.parseInt(handle.get("RECORD_LEFTDAYS").toString());
                RECORD_TOTALDAYS = Integer.parseInt(handle.get("RECORD_TOTALDAYS").toString());
            }
            int RECORD_HAVEDAYS = Integer.parseInt(handle.get("RECORD_HAVEDAYS").toString());
            String RECORD_LASTUPDATEDAYS = (String) handle.get("RECORD_LASTUPDATEDAYS");
            String RECORD_DAYTYPE = (String) handle.get("RECORD_DAYTYPE");
            //获取开始日期
            String startDate = RECORD_LASTUPDATEDAYS.substring(0, 10);
            //获取当前日期
            String endDate = PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd");
            int spendDays = 0;
            if (RECORD_DAYTYPE.equals(TimeLimitService.LIMITTYPE_NATURAL)) {
                spendDays = PlatDateTimeUtil.getDaysBetween(startDate, endDate);
            } else {
                spendDays = workdayService.getWorkDayCount(startDate, endDate);
            }
            RECORD_HAVEDAYS += spendDays;
            if (RECORD_LEFTDAYS != null) {
                RECORD_LEFTDAYS = RECORD_TOTALDAYS - RECORD_HAVEDAYS;
                handle.put("RECORD_LEFTDAYS", RECORD_LEFTDAYS);
                handle.put("RECORD_TOTALDAYS", RECORD_TOTALDAYS);
            }
            handle.put("RECORD_LASTUPDATEDAYS", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            handle.put("RECORD_HAVEDAYS", RECORD_HAVEDAYS);
            dao.saveOrUpdate("JBPM6_HANDUPRECORD", handle, SysConstants.ID_GENERATOR_UUID, null);
        }
    }

    /**
     * 获取挂起天数的数量
     *
     * @param exeId
     * @param limitType
     * @return
     */
    @Override
    public int getHandUpCount(String exeId, String limitType) {
        String minBeginDate = dao.getMinBeginDate(exeId);
        if (StringUtils.isNotEmpty(minBeginDate)) {
            String maxEndDate = dao.getMaxUpdateDate(exeId);
            int handleDays = 0;
            if (limitType.equals(TimeLimitService.LIMITTYPE_NATURAL)) {
                handleDays = PlatDateTimeUtil.getDaysBetween(minBeginDate, maxEndDate);
            } else {
                handleDays = workdayService.getWorkDayCount(minBeginDate, maxEndDate);
            }
            return handleDays;
        } else {
            return 0;
        }
    }

    /**
     * 获取挂起的天数数量
     *
     * @param exeId
     * @param limitType
     * @param nodeKeys
     * @return
     */
    @Override
    public int getHandUpDays(String exeId, String limitType, String nodeKeys) {
        String minBeginDate = dao.getMinBeginDate(exeId, nodeKeys);
        if (StringUtils.isNotEmpty(minBeginDate)) {
            String maxEndDate = dao.getMaxUpdateDate(exeId, nodeKeys);
            int handleDays = 0;
            if (limitType.equals(TimeLimitService.LIMITTYPE_NATURAL)) {
                handleDays = PlatDateTimeUtil.getDaysBetween(minBeginDate, maxEndDate);
            } else {
                handleDays = workdayService.getWorkDayCount(minBeginDate, maxEndDate);
            }
            return handleDays;
        } else {
            return 0;
        }
    }

}
