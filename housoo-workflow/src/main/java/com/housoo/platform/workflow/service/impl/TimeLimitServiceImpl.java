/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.service.WorkdayService;
import com.housoo.platform.workflow.dao.TimeLimitDao;
import com.housoo.platform.workflow.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 描述 环节时限业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-10 10:06:52
 */
@Service("timeLimitService")
public class TimeLimitServiceImpl extends BaseServiceImpl implements TimeLimitService {
    /**
     * 所引入的dao
     */
    @Resource
    private TimeLimitDao dao;
    /**
     *
     */
    @Resource
    private FlowDefService flowDefService;
    /**
     *
     */
    @Resource
    private ExecutionService executionService;
    /**
     *
     */
    @Resource
    private WorkdayService workdayService;
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

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取任务时限数据
     *
     * @param jbpmFlowInfo
     * @return 返回字段TASK_LEFTDAYS剩余天数 TASK_LIMITTYPE天数类型
     */
    @Override
    public Map<String, Object> getTaskTimeLimit(JbpmFlowInfo jbpmFlowInfo, String nextNodeKey) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> timeLimit = this.getTimeLimit(jbpmFlowInfo.getJbpmDefId(), nextNodeKey);
        if (timeLimit != null) {
            //获取时限类型
            String TASK_LIMITTYPE = timeLimit.get("TIMELIMIT_TYPE").toString();
            //获取总时限天数
            int TIMELIMIT_DAYS = Integer.parseInt(timeLimit.get("TIMELIMIT_DAYS").toString());
            result.put("TASK_LIMITTYPE", TASK_LIMITTYPE);
            //获取开始节点名称和中间节点名称
            String startNodeKey = (String) timeLimit.get("START_NODEKEY");
            String middleNodeKey = (String) timeLimit.get("MIDDLE_NODEKEYS");
            StringBuffer preNodeKeys = new StringBuffer(startNodeKey);
            preNodeKeys.append(",").append(middleNodeKey);
            int spendCount = jbpmTaskService.getSpendDayCount(preNodeKeys.toString(),
                    jbpmFlowInfo.getJbpmExeId(), TASK_LIMITTYPE);
            int handupCount = handupRecordService.getHandUpDays(jbpmFlowInfo.getJbpmExeId(),
                    TASK_LIMITTYPE, preNodeKeys.toString());
            int leftDays = TIMELIMIT_DAYS - spendCount + handupCount;
            result.put("TASK_LEFTDAYS", leftDays);
        } else {
            //获取流程定义
            Map<String, Object> flowDef = flowDefService.getRecord("JBPM6_FLOWDEF",
                    new String[]{"FLOWDEF_ID"}, new Object[]{jbpmFlowInfo.getJbpmDefId()});
            String TOTALLIMIT_TYPES = (String) flowDef.get("TOTALLIMIT_TYPES");
            result.put("TASK_LIMITTYPE", TOTALLIMIT_TYPES);
            if (flowDef.get("TOTALLIMIT_DAYS") != null) {
                //获取所配置的总时限
                int TOTALLIMIT_DAYS = Integer.parseInt(flowDef.get("TOTALLIMIT_DAYS").toString());
                //获取实例创建时间
                Map<String, Object> execution = executionService.getRecord("JBPM6_EXECUTION",
                        new String[]{"EXECUTION_ID"}, new Object[]{jbpmFlowInfo.getJbpmExeId()});
                String EXECUTION_CREATETIME = (String) execution.get("EXECUTION_CREATETIME");
                Date createTime = PlatDateTimeUtil.formatStr(EXECUTION_CREATETIME, "yyyy-MM-dd HH:mm:ss");
                String beginDate = PlatDateTimeUtil.formatDate(createTime, "yyyy-MM-dd");
                String currentTime = PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd");
                if (TOTALLIMIT_TYPES.equals(TimeLimitService.LIMITTYPE_NATURAL)) {
                    int spendDays = PlatDateTimeUtil.getDaysBetween(beginDate, currentTime);
                    int taskLeftDays = TOTALLIMIT_DAYS - spendDays;
                    result.put("TASK_LEFTDAYS", taskLeftDays);
                } else {
                    int spendDays = workdayService.getWorkDayCount(beginDate, currentTime);
                    int taskLeftDays = TOTALLIMIT_DAYS - spendDays;
                    result.put("TASK_LEFTDAYS", taskLeftDays);
                }
            }
        }
        return result;
    }

    /**
     * 获取实例时限数据
     *
     * @param jbpmFlowInfo
     * @return 返回字段EXECUTION_LEFTDAYS剩余天数 LIMITTYPE天数类型
     */
    @Override
    public Map<String, Object> getExeTimeLimit(JbpmFlowInfo jbpmFlowInfo) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> execution = null;
        if (StringUtils.isNotEmpty(jbpmFlowInfo.getJbpmExeId())) {
            execution = executionService.getRecord("JBPM6_EXECUTION",
                    new String[]{"EXECUTION_ID"}, new Object[]{jbpmFlowInfo.getJbpmExeId()});
        }
        if (!StringUtils.isNotEmpty(jbpmFlowInfo.getJbpmDefId())) {
            String defId = (String) execution.get("FLOWDEF_ID");
            jbpmFlowInfo.setJbpmDefId(defId);
        }
        //获取流程定义
        Map<String, Object> flowDef = flowDefService.getRecord("JBPM6_FLOWDEF",
                new String[]{"FLOWDEF_ID"}, new Object[]{jbpmFlowInfo.getJbpmDefId()});
        String TOTALLIMIT_TYPES = (String) flowDef.get("TOTALLIMIT_TYPES");
        result.put("LIMITTYPE", TOTALLIMIT_TYPES);
        if (flowDef.get("TOTALLIMIT_DAYS") != null) {
            //获取所配置的总时限
            int TOTALLIMIT_DAYS = Integer.parseInt(flowDef.get("TOTALLIMIT_DAYS").toString());
            //获取实例创建时间
            String EXECUTION_CREATETIME = (String) execution.get("EXECUTION_CREATETIME");
            Date createTime = PlatDateTimeUtil.formatStr(EXECUTION_CREATETIME, "yyyy-MM-dd HH:mm:ss");
            String beginDate = PlatDateTimeUtil.formatDate(createTime, "yyyy-MM-dd");
            String currentTime = PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd");
            if (TOTALLIMIT_TYPES.equals(TimeLimitService.LIMITTYPE_NATURAL)) {
                int spendDays = PlatDateTimeUtil.getDaysBetween(beginDate, currentTime);
                int taskLeftDays = TOTALLIMIT_DAYS - spendDays;
                result.put("EXECUTION_LEFTDAYS", taskLeftDays);
            } else {
                int spendDays = workdayService.getWorkDayCount(beginDate, currentTime);
                int taskLeftDays = TOTALLIMIT_DAYS - spendDays;
                result.put("EXECUTION_LEFTDAYS", taskLeftDays);
            }
        }
        return result;
    }

    /**
     * 根据流程定义
     *
     * @param defId
     * @param nodeKey
     * @return
     */
    @Override
    public Map<String, Object> getTimeLimit(String defId, String nodeKey) {
        Map<String, Object> timeLimit = dao.getRecord("JBPM6_TIMELIMIT",
                new String[]{"TIMELIMIT_FLOWDEFID", "START_NODEKEY"}, new Object[]{defId, nodeKey});
        if (timeLimit != null) {
            return timeLimit;
        } else {
            StringBuffer sql = new StringBuffer("SELECT * FROM JBPM6_TIMELIMIT");
            sql.append(" T WHERE T.MIDDLE_NODEKEYS LIKE ? AND ");
            sql.append("T.TIMELIMIT_FLOWDEFID=? ORDER BY T.TIMELIMIT_CREATETIME ASC");
            List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                    new Object[]{"%" + nodeKey + "%", defId}, null);
            Map<String, Object> targetLimit = null;
            for (Map<String, Object> map : list) {
                String MIDDLE_NODEKEYS = (String) map.get("MIDDLE_NODEKEYS");
                Set<String> keySet = new HashSet<String>(Arrays.asList(MIDDLE_NODEKEYS.split(",")));
                if (keySet.contains(nodeKey)) {
                    targetLimit = map;
                    break;
                }
            }
            return targetLimit;
        }
    }

    /**
     * 根据流程定义获取列表数据
     *
     * @param defId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByDefId(String defId) {
        StringBuffer sql = new StringBuffer("SELECT * FROM ");
        sql.append("JBPM6_TIMELIMIT J WHERE J.TIMELIMIT_FLOWDEFID=? ");
        sql.append(" ORDER BY J.TIMELIMIT_ID DESC ");
        return dao.findBySql(sql.toString(), new Object[]{defId}, null);
    }

}
