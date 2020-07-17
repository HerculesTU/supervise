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
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.core.service.WorkdayService;
import com.housoo.platform.workflow.dao.ExecutionDao;
import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.workflow.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 描述 流程实例业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-04 14:34:24
 */
@Service("executionService")
public class ExecutionServiceImpl extends BaseServiceImpl implements ExecutionService {

    /**
     * 所引入的dao
     */
    @Resource
    private ExecutionDao dao;
    /**
     *
     */
    @Resource
    private JbpmTaskService jbpmTaskService;
    /**
     *
     */
    @Resource
    private HandupRecordService handupRecordService;
    /**
     *
     */
    @Resource
    private TimeLimitService timeLimitService;
    /**
     *
     */
    @Resource
    private WorkdayService workdayService;
    /**
     *
     */
    @Resource
    private JbpmService jbpmService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据流程定义ID和流程版本号判断是否存在记录
     *
     * @param defId
     * @param flowVersion
     * @return
     */
    @Override
    public boolean isExistedRunning(String defId, int flowVersion) {
        return dao.isExistedRunning(defId, flowVersion);
    }

    /**
     * 获取下一个流程实例号
     *
     * @return
     */
    @Override
    public String getNextExeId() {
        StringBuffer exeId = new StringBuffer("FJFDA");
        exeId.append(PlatDateTimeUtil.formatDate(new Date(), "yyMMddHHmmss"));
        int randomNumber = PlatStringUtil.getRandomIntNumber(1, 1000);
        String number = PlatStringUtil.getFormatNumber(4, String.valueOf(randomNumber));
        exeId.append(number);
        return exeId.toString();
    }

    /**
     * 保存流程实例信息
     *
     * @param postParams
     * @param jbpmFlowInfo
     */
    @Override
    public void saveFlowExe(Map<String, Object> postParams, JbpmFlowInfo jbpmFlowInfo) {
        String jbpmExeId = jbpmFlowInfo.getJbpmExeId();
        Map<String, Object> execution = null;
        if (StringUtils.isNotEmpty(jbpmExeId) && !"-1".equals(jbpmExeId)) {
            execution = dao.getRecord("JBPM6_EXECUTION",
                    new String[]{"EXECUTION_ID"}, new Object[]{jbpmExeId});
        } else {
            execution = new HashMap<String, Object>();
            jbpmExeId = this.getNextExeId();
            execution.put("EXECUTION_ID", jbpmExeId);
            execution.put("FLOWDEF_ID", jbpmFlowInfo.getJbpmDefId());
            String jbpmIsTempSave = jbpmFlowInfo.getJbpmIsTempSave();
            if ("true".equals(jbpmIsTempSave)) {
                execution.put("STATUS", ExecutionService.STATUS_DRAFT);
                jbpmFlowInfo.setJbpmStopExe("true");
            } else {
                execution.put("STATUS", ExecutionService.STATUS_RUNNING);
            }
            execution.put("EXECUTION_LEFTDAYS", jbpmFlowInfo.getJbpmDefAllLimitDays());
            execution.put("LIMITTYPE", jbpmFlowInfo.getJbpmDefAllLimitType());
            execution.put("TMPSAVE_MAINTABLENAME", jbpmFlowInfo.getJbpmMainTableName());
            execution.put("TMPSAVE_RECORDID", jbpmFlowInfo.getJbpmMainTableRecordId());
            execution.put("CREATOR_ID", jbpmFlowInfo.getJbpmCreatorId());
            execution.put("CREATOR_NAME", jbpmFlowInfo.getJbpmCreatorName());
            execution.put("SERITEM_ID", jbpmFlowInfo.getJbpmSerItemId());
            execution.put("EXECUTION_CREATETIME", PlatDateTimeUtil.
                    formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            execution.put("LASTUPDATELEFTDAYS", PlatDateTimeUtil.
                    formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }
        if (StringUtils.isNotEmpty(jbpmFlowInfo.getJbpmFlowSubject())) {
            execution.put("EXECUTION_SUBJECT", jbpmFlowInfo.getJbpmFlowSubject());
        } else {
            String EXECUTION_SUBJECT = (String) execution.get("EXECUTION_SUBJECT");
            if (StringUtils.isEmpty(EXECUTION_SUBJECT)) {
                StringBuffer subject = new StringBuffer(jbpmFlowInfo.getJbpmDefName());
                subject.append("【发起人:").append(jbpmFlowInfo.getJbpmCreatorName()).append("】");
                execution.put("EXECUTION_SUBJECT", subject.toString());
            }
        }
        execution.put("EXECUTION_VERSION", jbpmFlowInfo.getJbpmDefVersion());
        execution.put("APPLYER_NAME", jbpmFlowInfo.getJbpmApplyerName());
        execution.put("APPLYER_MOBILE", jbpmFlowInfo.getJbpmApplyerMobile());
        dao.saveOrUpdate("JBPM6_EXECUTION", execution, SysConstants.ID_GENERATOR_ASSIGNED, null);
        if ("true".equals(jbpmFlowInfo.getJbpmIsTempSave())) {
            jbpmFlowInfo.setJbpmStopExe("true");
        }
        jbpmFlowInfo.setJbpmExeId(jbpmExeId);
    }

    /**
     * 删除流程实例数据并且级联删除子表
     *
     * @param exeIds
     */
    @Override
    public void delCascadeSubTables(String[] exeIds) {
        for (String exeId : exeIds) {
            //获取子流程实例IDS
            List<String> subExeIdArray = dao.findSubExeIdArray(exeId);
            if (subExeIdArray != null && subExeIdArray.size() > 0) {
                this.delCascadeSubTables(subExeIdArray.toArray(new String[subExeIdArray.size()]));
            }
            Map<String, Object> exeInfo = dao.getRecord("JBPM6_EXECUTION",
                    new String[]{"EXECUTION_ID"}, new Object[]{exeId});
            //获取状态
            String exeStatus = (String) exeInfo.get("STATUS");
            if (exeStatus.equals(ExecutionService.STATUS_DRAFT)) {
                String TMPSAVE_MAINTABLENAME = (String) exeInfo.get("TMPSAVE_MAINTABLENAME");
                String TMPSAVE_RECORDID = (String) exeInfo.get("TMPSAVE_RECORDID");
                String PKNAME = dao.findPrimaryKeyNames(TMPSAVE_MAINTABLENAME).get(0);
                StringBuffer sql = new StringBuffer("DELETE FROM ");
                sql.append(TMPSAVE_MAINTABLENAME).append(" WHERE ");
                sql.append(PKNAME).append("=? ");
                dao.executeSql(sql.toString(), new Object[]{TMPSAVE_RECORDID});
            } else {
                List<Map<String, Object>> list = jbpmTaskService.findByExeId(exeId);
                for (Map<String, Object> task : list) {
                    String TASK_MAINTABLENAME = (String) task.get("TASK_MAINTABLENAME");
                    String TASK_MAINRECORDID = (String) task.get("TASK_MAINRECORDID");
                    if (StringUtils.isNotEmpty(TASK_MAINTABLENAME)) {
                        String tablePkName = dao.findPrimaryKeyNames(TASK_MAINTABLENAME).get(0);
                        dao.deleteRecords(TASK_MAINTABLENAME, tablePkName, new String[]{TASK_MAINRECORDID});
                    }
                }
                handupRecordService.deleteByExeId(exeId);
                jbpmTaskService.deleteByExeId(exeId);
            }
        }
        dao.deleteRecords("JBPM6_EXECUTION", "EXECUTION_ID", exeIds);
    }

    /**
     * 根据流程实例ID删除流程实例
     *
     * @param exeId
     */
    @Override
    public void deleteExeById(String exeId) {
        //获取子流程实例IDS
        List<String> subExeIdArray = dao.findSubExeIdArray(exeId);
        if (subExeIdArray != null && subExeIdArray.size() > 0) {
            for (String subExeId : subExeIdArray) {
                this.deleteExeById(subExeId);
            }
        }
        handupRecordService.deleteByExeId(exeId);
        jbpmTaskService.deleteByExeId(exeId);
        dao.deleteRecords("JBPM6_EXECUTION", "EXECUTION_ID", new String[]{exeId});
    }

    /**
     * 更新流程实例信息
     *
     * @param postParams
     * @param jbpmFlowInfo
     */
    @Override
    public void updateFlowExe(Map<String, Object> postParams, JbpmFlowInfo jbpmFlowInfo) {
        //获取流程实例信息
        String exeId = jbpmFlowInfo.getJbpmExeId();
        //获取当前流程实例信息
        Map<String, Object> curExe = dao.getRecord("JBPM6_EXECUTION",
                new String[]{"EXECUTION_ID"}, new Object[]{jbpmFlowInfo.getJbpmExeId()});
        String PARENT_EXECUTION_ID = (String) curExe.get("PARENT_EXECUTION_ID");
        //获取正在运行的任务信息
        Map<String, String> runningTaskInfo = jbpmTaskService.getRunningTaskInfo(exeId);
        StringBuffer sql = null;
        if (runningTaskInfo.size() > 0) {
            //获取实例的办结时限要求数据
            List<Object> params = new ArrayList<Object>();
            String CURRENT_NODEKEYS = runningTaskInfo.get("CURRENT_NODEKEYS");
            String CURRENT_NODENAMES = runningTaskInfo.get("CURRENT_NODENAMES");
            String CURRENT_HANDLERIDS = runningTaskInfo.get("CURRENT_HANDLERIDS");
            String CURRENT_HANDLERNAMES = runningTaskInfo.get("CURRENT_HANDLERNAMES");
            params.add(CURRENT_NODEKEYS);
            params.add(CURRENT_NODENAMES);
            params.add(CURRENT_HANDLERIDS);
            params.add(CURRENT_HANDLERNAMES);
            params.add(ExecutionService.STATUS_RUNNING);
            Map<String, Object> exeTimeLimit = timeLimitService.getExeTimeLimit(jbpmFlowInfo);
            sql = new StringBuffer("UPDATE JBPM6_EXECUTION ");
            sql.append(" SET CURRENT_NODEKEYS=?,CURRENT_NODENAMES=?,CURRENT_HANDLERIDS=?");
            sql.append(",CURRENT_HANDLERNAMES=?,STATUS=? ");
            if (exeTimeLimit.get("EXECUTION_LEFTDAYS") != null) {
                sql.append(",EXECUTION_LEFTDAYS=? ");
                params.add(exeTimeLimit.get("EXECUTION_LEFTDAYS"));
            }
            sql.append(" WHERE EXECUTION_ID=?");
            params.add(jbpmFlowInfo.getJbpmExeId());
            dao.executeSql(sql.toString(), params.toArray());
            if (StringUtils.isNotEmpty(PARENT_EXECUTION_ID)) {
                //更新父流程的当前办理人
                sql = new StringBuffer("UPDATE JBPM6_EXECUTION ");
                sql.append(" SET CURRENT_HANDLERIDS=?,CURRENT_HANDLERNAMES=?");
                sql.append(" WHERE EXECUTION_ID=? ");
                dao.executeSql(sql.toString(), new Object[]{CURRENT_HANDLERIDS,
                        CURRENT_HANDLERNAMES, PARENT_EXECUTION_ID});
            }
        } else {
            //获取提交过来的办结状态
            String jbpmToEndStatus = jbpmFlowInfo.getJbpmToEndStatus();
            if (StringUtils.isEmpty(jbpmToEndStatus)) {
                jbpmToEndStatus = ExecutionService.STATUS_NORMALOVER;
            }
            String jbpmEndOpinion = jbpmFlowInfo.getJbpmEndOpinion();
            if (StringUtils.isEmpty(jbpmEndOpinion)) {
                jbpmEndOpinion = "";
            }
            sql = new StringBuffer("UPDATE JBPM6_EXECUTION ");
            sql.append(" SET STATUS=?,FINAL_OPINION=?,EXECUTION_ENDTIME=?");
            sql.append(",CURRENT_NODEKEYS=?,CURRENT_NODENAMES=?,CURRENT_HANDLERIDS=?");
            sql.append(",CURRENT_HANDLERNAMES=? WHERE EXECUTION_ID=?");
            String endTime = PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
            dao.executeSql(sql.toString(), new Object[]{jbpmToEndStatus, jbpmEndOpinion, endTime, "", ""
                    , "", "", jbpmFlowInfo.getJbpmExeId()});
            //派发父流程任务
            this.jbpmTaskService.assignParentTask(PARENT_EXECUTION_ID, jbpmFlowInfo);
        }
    }

    /**
     * 强制终结流程实例数据
     *
     * @param exeIds
     */
    @Override
    public void endExecution(String[] exeIds) {
        StringBuffer sql = new StringBuffer("UPDATE JBPM6_TASK");
        sql.append(" SET TASK_ENDTIME=?,TASK_HANDLEDESC=?,");
        sql.append("TASK_REALHANDLERID=?,TASK_REALHANDLERNAME=?,TASK_STATUS=? ");
        sql.append("WHERE TASK_EXEID=? AND TASK_STATUS IN (-2,-1,1)");
        Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
        String TASK_REALHANDLERID = (String) backLoginUser.get("SYSUSER_ID");
        String TASK_REALHANDLERNAME = (String) backLoginUser.get("SYSUSER_NAME");
        StringBuffer handleDesc = new StringBuffer("强制终结了流程");
        for (String exeId : exeIds) {
            Map<String, Object> execution = dao.getRecord("JBPM6_EXECUTION"
                    , new String[]{"EXECUTION_ID"}, new Object[]{exeId});
            String STATUS = (String) execution.get("STATUS");
            if (STATUS.equals(ExecutionService.STATUS_RUNNING)) {
                String endTime = PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
                dao.executeSql(sql.toString(), new Object[]{endTime, handleDesc.toString(),
                        TASK_REALHANDLERID, TASK_REALHANDLERNAME, JbpmTaskService.TASKSTATUS_OVER,
                        exeId});
                execution.put("STATUS", ExecutionService.STATUS_TERMINAL);
                execution.put("EXECUTION_ENDTIME", endTime);
                execution.put("CURRENT_NODEKEYS", "");
                execution.put("CURRENT_NODENAMES", "");
                execution.put("CURRENT_HANDLERIDS", "");
                execution.put("CURRENT_HANDLERNAMES", "");
                dao.saveOrUpdate("JBPM6_EXECUTION", execution, SysConstants.ID_GENERATOR_UUID, null);
            }
        }
    }

    /**
     * 更新剩余天数
     */
    @Override
    public void updateLeftDays() {
        StringBuffer sql = new StringBuffer("SELECT * FROM ");
        sql.append("JBPM6_EXECUTION T WHERE T.STATUS=? AND T.EXECUTION_LEFTDAYS IS NOT NULL");
        sql.append(" ORDER BY T.EXECUTION_CREATETIME ASC ");
        List<Map<String, Object>> exeList = dao.findBySql(sql.toString(),
                new Object[]{ExecutionService.STATUS_RUNNING}, null);
        for (Map<String, Object> exe : exeList) {
            String exeId = (String) exe.get("EXECUTION_ID");
            int EXECUTION_LEFTDAYS = Integer.parseInt(exe.get("EXECUTION_LEFTDAYS").toString());
            String LIMITTYPE = (String) exe.get("LIMITTYPE");
            String LASTUPDATELEFTDAYS = (String) exe.get("LASTUPDATELEFTDAYS");
            //获取开始日期
            String startDate = LASTUPDATELEFTDAYS.substring(0, 10);
            //获取当前日期
            String endDate = PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd");
            int spendDays = 0;
            if (LIMITTYPE.equals(TimeLimitService.LIMITTYPE_NATURAL)) {
                spendDays = PlatDateTimeUtil.getDaysBetween(startDate, endDate);
            } else {
                spendDays = workdayService.getWorkDayCount(startDate, endDate);
            }
            //获取挂起的天数
            int handUpDays = handupRecordService.getHandUpCount(exeId, LIMITTYPE);
            EXECUTION_LEFTDAYS = EXECUTION_LEFTDAYS - spendDays + handUpDays;
            exe.put("EXECUTION_LEFTDAYS", EXECUTION_LEFTDAYS);
            exe.put("LASTUPDATELEFTDAYS", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            dao.saveOrUpdate("JBPM6_EXECUTION", exe, SysConstants.ID_GENERATOR_UUID, null);
        }
    }

    /**
     * 获取子流程的实例ID
     *
     * @param parentExeId
     * @return
     */
    @Override
    public String getSubProcessExeId(String parentExeId) {
        int subCount = dao.getSubProcessCount(parentExeId);
        return parentExeId + "_" + subCount;
    }

    /**
     * 获取往下执行流程的结果
     *
     * @param flowVars
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public Map<String, Object> exeFlowResult(Map<String, Object> flowVars, JbpmFlowInfo jbpmFlowInfo) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            //获取当前操作人任务ID
            String operingTaskId = jbpmFlowInfo.getJbpmOperingTaskId();
            if (StringUtils.isNotEmpty(operingTaskId)) {
                boolean isHaveHandled = this.jbpmTaskService.isTaskHaveHandled(operingTaskId);
                if (isHaveHandled) {
                    result.put("OPER_SUCCESS", true);
                    result.put("OPER_MSG", "当前流程任务已经被办理,不允许重复办理!");
                    return result;
                }
            }
            result = jbpmService.doFlowJob(flowVars, jbpmFlowInfo);
            if (!(result.get("OPER_SUCCESS") != null && result.get("OPER_MSG") != null)) {
                result.put("OPER_SUCCESS", true);
                result.put("OPER_MSG", "操作成功!");
            }
        } catch (Exception e) {
            result.put("OPER_SUCCESS", false);
            String OPER_MSG = (String) result.get("OPER_MSG");
            if (!StringUtils.isNotEmpty(OPER_MSG)) {
                result.put("OPER_MSG", "提交失败,请联系系统管理员!");
            }
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 更新流程实例版本为最新版本
     *
     * @param exeIds
     */
    @Override
    public void updateExeVersionToNewest(String[] exeIds) {
        for (String exeId : exeIds) {
            Map<String, Object> jbpmExe = this.getRecord("JBPM6_EXECUTION",
                    new String[]{"EXECUTION_ID"}, new Object[]{exeId});
            String FLOWDEF_ID = (String) jbpmExe.get("FLOWDEF_ID");
            Map<String, Object> jbpmFlowDef = this.getRecord("JBPM6_FLOWDEF",
                    new String[]{"FLOWDEF_ID"}, new Object[]{FLOWDEF_ID});
            jbpmExe.put("EXECUTION_VERSION", jbpmFlowDef.get("FLOWDEF_VERSION"));
            dao.saveOrUpdate("JBPM6_EXECUTION", jbpmExe, SysConstants.ID_GENERATOR_UUID, null);
        }
    }

    /**
     * 根据流程定义获取实例数量
     *
     * @param defIds
     * @return
     */
    @Override
    public int getCountByDefIds(String defIds) {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
        sql.append("JBPM6_EXECUTION E WHERE E.FLOWDEF_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition(defIds));
        int count = dao.getIntBySql(sql.toString(), null);
        return count;
    }
}
