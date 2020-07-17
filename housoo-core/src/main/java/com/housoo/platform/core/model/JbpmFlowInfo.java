package com.housoo.platform.core.model;

import java.io.Serializable;

/**
 * 描述 流程流转过程的对象信息
 *
 * @author 高飞
 * @created 2017年5月13日 上午7:49:38
 */
public class JbpmFlowInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 流程定义ID
     */
    private String jbpmDefId;
    /**
     * 流程定义版本号
     */
    private String jbpmDefVersion;
    /**
     * 流程定义编码
     */
    private String jbpmDefCode;
    /**
     * 当前实例正在运行的节点KEYS
     */
    private String jbpmRunningNodeKeys;
    /**
     * 当前办理人正在办理的节点KEY
     */
    private String jbpmOperingNodeKey;
    /**
     * 流程开始节点的KEY
     */
    private String jbpmStartNodeKey;
    /**
     * 是否是查询状态(true,false)
     */
    private String jbpmIsQuery;
    /**
     * 当前操作节点涉及主表名称
     */
    private String jbpmMainTableName;
    /**
     * 当前操作节点实体类名称
     */
    private String jbpmMainClassName;
    /**
     * 提交是否是暂存操作(true,false)
     */
    private String jbpmIsTempSave;
    /**
     * 执行事件的类型
     * 存储:0,前置:1,后置:2,决策:3
     */
    private String jbpmEventType;
    /**
     * 停止往下执行
     */
    private String jbpmStopExe;
    /**
     * 当前操作节点涉及主表ID
     */
    private String jbpmMainTableRecordId;
    /**
     * 当前操作节点涉及主表主键名称
     */
    private String jbpmMainTablePkName;
    /**
     * 当前流程实例ID
     */
    private String jbpmExeId;
    /**
     * 流程标题
     */
    private String jbpmFlowSubject;
    /**
     * 流程定义名称
     */
    private String jbpmDefName;
    /**
     * 流程发起人ID
     */
    private String jbpmCreatorId;
    /**
     * 流程发起人姓名
     */
    private String jbpmCreatorName;
    /**
     * 流程申请人姓名
     */
    private String jbpmApplyerName;
    /**
     * 流程申请人移动电话
     */
    private String jbpmApplyerMobile;
    /**
     * 当前办理人操作的任务ID
     */
    private String jbpmOperingTaskId;
    /**
     * 流程定义JSON
     */
    private String jbpmDefJson;
    /**
     * 当前操作环节的设计表单编码
     */
    private String jbpmOperingDesignCode;
    /**
     * 当前操作表单跳转前置接口
     */
    private String jbpmOperingFormBeforeInter;
    /**
     * 流程定义总时限天数
     */
    private String jbpmDefAllLimitDays;
    /**
     * 流程定义总时限类型
     */
    private String jbpmDefAllLimitType;
    /**
     * 当前办理人正在办理的节点名称
     */
    private String jbpmOperingNodeName;
    /**
     * 下一环节是否是并行环节(true,false)
     */
    private String jbpmIsParallel;
    /**
     * 提交的办理意见
     */
    private String jbpmHandleOpinion;
    /**
     * 下一环节办里人JSON
     */
    private String jbpmAssignJson;
    /**
     * 当前办理人的类型(1:后台用户 2:网站用户)
     */
    private String jbpmOperingHandlerType;
    /**
     * 是否允许派发任务true false
     */
    private String jbpmAllowAssignTask;
    /**
     * 提交流程的办结状态值(2:已办结(正常结束)
     * 3:已办结(审核通过) 4:已办结(审核不通过)
     * 5:已办结(强制终止)
     */
    private String jbpmToEndStatus;
    /**
     * 办结时的最终审核意见
     */
    private String jbpmEndOpinion;
    /**
     * 提交流程的处理任务状态值
     * 任务状态(-2:等待 -1:挂起 1:正在办理 2:已办理 3:退回 4:转发 5:结束流程 6:审核不通过 7:重启任务 )
     */
    private String jbpmHandleTaskStatus;
    /**
     * 流程当前操作来源节点KEY
     */
    private String jbpmOperingFromNodeKey;
    /**
     * 新派发的流程任务IDS
     */
    private String jbpmNewAssignTaskIds;
    /**
     * 事项的ID SERITEM_ID
     */
    private String jbpmSerItemId;
    /**
     * 指定挂起天数
     */
    private String jbpmHandleUpLimitDay;
    /**
     * 父流程的定义ID
     */
    private String jbpmParentDefId;
    /**
     * 父亲流程的定义版本
     */
    private String jbpmParentDefVersion;
    /**
     * 父亲流程当前正在办理的KEY
     */
    private String jbpmParentOperingNodeKey;
    /**
     * 重启父流程true false
     */
    private String jbpmRestartParentExe;
    /**
     * 外部传入的流程办理人ID
     */
    private String jbpmHandlerId;
    /**
     * 自定义字段的权限JSON
     */
    private String jbpmFieldAuthJson;

    /**
     * @return the jbpmHandlerId
     */
    public String getJbpmHandlerId() {
        return jbpmHandlerId;
    }

    /**
     * @param jbpmHandlerId the jbpmHandlerId to set
     */
    public void setJbpmHandlerId(String jbpmHandlerId) {
        this.jbpmHandlerId = jbpmHandlerId;
    }

    /**
     * @return the jbpmRestartParentExe
     */
    public String getJbpmRestartParentExe() {
        return jbpmRestartParentExe;
    }

    /**
     * @param jbpmRestartParentExe the jbpmRestartParentExe to set
     */
    public void setJbpmRestartParentExe(String jbpmRestartParentExe) {
        this.jbpmRestartParentExe = jbpmRestartParentExe;
    }

    /**
     * @return the jbpmHandleUpLimitDay
     */
    public String getJbpmHandleUpLimitDay() {
        return jbpmHandleUpLimitDay;
    }

    /**
     * @param jbpmHandleUpLimitDay the jbpmHandleUpLimitDay to set
     */
    public void setJbpmHandleUpLimitDay(String jbpmHandleUpLimitDay) {
        this.jbpmHandleUpLimitDay = jbpmHandleUpLimitDay;
    }

    /**
     * @return the jbpmSerItemId
     */
    public String getJbpmSerItemId() {
        return jbpmSerItemId;
    }

    /**
     * @param jbpmSerItemId the jbpmSerItemId to set
     */
    public void setJbpmSerItemId(String jbpmSerItemId) {
        this.jbpmSerItemId = jbpmSerItemId;
    }

    /**
     * @return the jbpmNewAssignTaskIds
     */
    public String getJbpmNewAssignTaskIds() {
        return jbpmNewAssignTaskIds;
    }

    /**
     * @param jbpmNewAssignTaskIds the jbpmNewAssignTaskIds to set
     */
    public void setJbpmNewAssignTaskIds(String jbpmNewAssignTaskIds) {
        this.jbpmNewAssignTaskIds = jbpmNewAssignTaskIds;
    }

    /**
     * @return the jbpmOperingFromNodeKey
     */
    public String getJbpmOperingFromNodeKey() {
        return jbpmOperingFromNodeKey;
    }

    /**
     * @param jbpmOperingFromNodeKey the jbpmOperingFromNodeKey to set
     */
    public void setJbpmOperingFromNodeKey(String jbpmOperingFromNodeKey) {
        this.jbpmOperingFromNodeKey = jbpmOperingFromNodeKey;
    }

    /**
     * @return the jbpmHandleTaskStatus
     */
    public String getJbpmHandleTaskStatus() {
        return jbpmHandleTaskStatus;
    }

    /**
     * @param jbpmHandleTaskStatus the jbpmHandleTaskStatus to set
     */
    public void setJbpmHandleTaskStatus(String jbpmHandleTaskStatus) {
        this.jbpmHandleTaskStatus = jbpmHandleTaskStatus;
    }

    /**
     * @return the jbpmEndOpinion
     */
    public String getJbpmEndOpinion() {
        return jbpmEndOpinion;
    }

    /**
     * @param jbpmEndOpinion the jbpmEndOpinion to set
     */
    public void setJbpmEndOpinion(String jbpmEndOpinion) {
        this.jbpmEndOpinion = jbpmEndOpinion;
    }

    /**
     * @return the jbpmToEndStatus
     */
    public String getJbpmToEndStatus() {
        return jbpmToEndStatus;
    }

    /**
     * @param jbpmToEndStatus the jbpmToEndStatus to set
     */
    public void setJbpmToEndStatus(String jbpmToEndStatus) {
        this.jbpmToEndStatus = jbpmToEndStatus;
    }

    /**
     * @return the jbpmAllowAssignTask
     */
    public String getJbpmAllowAssignTask() {
        return jbpmAllowAssignTask;
    }

    /**
     * @param jbpmAllowAssignTask the jbpmAllowAssignTask to set
     */
    public void setJbpmAllowAssignTask(String jbpmAllowAssignTask) {
        this.jbpmAllowAssignTask = jbpmAllowAssignTask;
    }

    /**
     * @return the jbpmOperingHandlerType
     */
    public String getJbpmOperingHandlerType() {
        return jbpmOperingHandlerType;
    }

    /**
     * @param jbpmOperingHandlerType the jbpmOperingHandlerType to set
     */
    public void setJbpmOperingHandlerType(String jbpmOperingHandlerType) {
        this.jbpmOperingHandlerType = jbpmOperingHandlerType;
    }

    /**
     * @return the jbpmOperingNodeName
     */
    public String getJbpmOperingNodeName() {
        return jbpmOperingNodeName;
    }

    /**
     * @param jbpmOperingNodeName the jbpmOperingNodeName to set
     */
    public void setJbpmOperingNodeName(String jbpmOperingNodeName) {
        this.jbpmOperingNodeName = jbpmOperingNodeName;
    }

    /**
     * @return the jbpmOperingFormBeforeInter
     */
    public String getJbpmOperingFormBeforeInter() {
        return jbpmOperingFormBeforeInter;
    }

    /**
     * @param jbpmOperingFormBeforeInter the jbpmOperingFormBeforeInter to set
     */
    public void setJbpmOperingFormBeforeInter(String jbpmOperingFormBeforeInter) {
        this.jbpmOperingFormBeforeInter = jbpmOperingFormBeforeInter;
    }

    /**
     * @return the jbpmOperingDesignCode
     */
    public String getJbpmOperingDesignCode() {
        return jbpmOperingDesignCode;
    }

    /**
     * @param jbpmOperingDesignCode the jbpmOperingDesignCode to set
     */
    public void setJbpmOperingDesignCode(String jbpmOperingDesignCode) {
        this.jbpmOperingDesignCode = jbpmOperingDesignCode;
    }

    /**
     * @return the jbpmDefJson
     */
    public String getJbpmDefJson() {
        return jbpmDefJson;
    }

    /**
     * @param jbpmDefJson the jbpmDefJson to set
     */
    public void setJbpmDefJson(String jbpmDefJson) {
        this.jbpmDefJson = jbpmDefJson;
    }

    /**
     * @return the jbpmOperingTaskId
     */
    public String getJbpmOperingTaskId() {
        return jbpmOperingTaskId;
    }

    /**
     * @param jbpmOperingTaskId the jbpmOperingTaskId to set
     */
    public void setJbpmOperingTaskId(String jbpmOperingTaskId) {
        this.jbpmOperingTaskId = jbpmOperingTaskId;
    }

    /**
     * @return the jbpmDefName
     */
    public String getJbpmDefName() {
        return jbpmDefName;
    }

    /**
     * @param jbpmDefName the jbpmDefName to set
     */
    public void setJbpmDefName(String jbpmDefName) {
        this.jbpmDefName = jbpmDefName;
    }

    /**
     * @return the jbpmFlowSubject
     */
    public String getJbpmFlowSubject() {
        return jbpmFlowSubject;
    }

    /**
     * @param jbpmFlowSubject the jbpmFlowSubject to set
     */
    public void setJbpmFlowSubject(String jbpmFlowSubject) {
        this.jbpmFlowSubject = jbpmFlowSubject;
    }

    /**
     * @return the jbpmExeId
     */
    public String getJbpmExeId() {
        return jbpmExeId;
    }

    /**
     * @param jbpmExeId the jbpmExeId to set
     */
    public void setJbpmExeId(String jbpmExeId) {
        this.jbpmExeId = jbpmExeId;
    }

    /**
     * @return the jbpmMainTablePkName
     */
    public String getJbpmMainTablePkName() {
        return jbpmMainTablePkName;
    }

    /**
     * @param jbpmMainTablePkName the jbpmMainTablePkName to set
     */
    public void setJbpmMainTablePkName(String jbpmMainTablePkName) {
        this.jbpmMainTablePkName = jbpmMainTablePkName;
    }

    /**
     * @return the jbpmMainTableRecordId
     */
    public String getJbpmMainTableRecordId() {
        return jbpmMainTableRecordId;
    }

    /**
     * @param jbpmMainTableRecordId the jbpmMainTableRecordId to set
     */
    public void setJbpmMainTableRecordId(String jbpmMainTableRecordId) {
        this.jbpmMainTableRecordId = jbpmMainTableRecordId;
    }

    /**
     * @return the jbpmIsTempSave
     */
    public String getJbpmIsTempSave() {
        return jbpmIsTempSave;
    }

    /**
     * @param jbpmIsTempSave the jbpmIsTempSave to set
     */
    public void setJbpmIsTempSave(String jbpmIsTempSave) {
        this.jbpmIsTempSave = jbpmIsTempSave;
    }

    /**
     * @return the jbpmMainClassName
     */
    public String getJbpmMainClassName() {
        return jbpmMainClassName;
    }

    /**
     * @param jbpmMainClassName the jbpmMainClassName to set
     */
    public void setJbpmMainClassName(String jbpmMainClassName) {
        this.jbpmMainClassName = jbpmMainClassName;
    }

    /**
     * @return the jbpmMainTableName
     */
    public String getJbpmMainTableName() {
        return jbpmMainTableName;
    }

    /**
     * @param jbpmMainTableName the jbpmMainTableName to set
     */
    public void setJbpmMainTableName(String jbpmMainTableName) {
        this.jbpmMainTableName = jbpmMainTableName;
    }

    /**
     * @return the jbpmDefId
     */
    public String getJbpmDefId() {
        return jbpmDefId;
    }

    /**
     * @param jbpmDefId the jbpmDefId to set
     */
    public void setJbpmDefId(String jbpmDefId) {
        this.jbpmDefId = jbpmDefId;
    }

    /**
     * @return the jbpmDefVersion
     */
    public String getJbpmDefVersion() {
        return jbpmDefVersion;
    }

    /**
     * @param jbpmDefVersion the jbpmDefVersion to set
     */
    public void setJbpmDefVersion(String jbpmDefVersion) {
        this.jbpmDefVersion = jbpmDefVersion;
    }

    /**
     * @return the jbpmDefCode
     */
    public String getJbpmDefCode() {
        return jbpmDefCode;
    }

    /**
     * @param jbpmDefCode the jbpmDefCode to set
     */
    public void setJbpmDefCode(String jbpmDefCode) {
        this.jbpmDefCode = jbpmDefCode;
    }

    /**
     * @return the jbpmRunningNodeKeys
     */
    public String getJbpmRunningNodeKeys() {
        return jbpmRunningNodeKeys;
    }

    /**
     * @param jbpmRunningNodeKeys the jbpmRunningNodeKeys to set
     */
    public void setJbpmRunningNodeKeys(String jbpmRunningNodeKeys) {
        this.jbpmRunningNodeKeys = jbpmRunningNodeKeys;
    }

    /**
     * @return the jbpmOperingNodeKey
     */
    public String getJbpmOperingNodeKey() {
        return jbpmOperingNodeKey;
    }

    /**
     * @param jbpmOperingNodeKey the jbpmOperingNodeKey to set
     */
    public void setJbpmOperingNodeKey(String jbpmOperingNodeKey) {
        this.jbpmOperingNodeKey = jbpmOperingNodeKey;
    }

    /**
     * @return the jbpmStartNodeKey
     */
    public String getJbpmStartNodeKey() {
        return jbpmStartNodeKey;
    }

    /**
     * @param jbpmStartNodeKey the jbpmStartNodeKey to set
     */
    public void setJbpmStartNodeKey(String jbpmStartNodeKey) {
        this.jbpmStartNodeKey = jbpmStartNodeKey;
    }

    /**
     * @return the jbpmIsQuery
     */
    public String getJbpmIsQuery() {
        return jbpmIsQuery;
    }

    /**
     * @param jbpmIsQuery the jbpmIsQuery to set
     */
    public void setJbpmIsQuery(String jbpmIsQuery) {
        this.jbpmIsQuery = jbpmIsQuery;
    }

    /**
     * @return the jbpmEventType
     */
    public String getJbpmEventType() {
        return jbpmEventType;
    }

    /**
     * @param jbpmEventType the jbpmEventType to set
     */
    public void setJbpmEventType(String jbpmEventType) {
        this.jbpmEventType = jbpmEventType;
    }

    /**
     * @return the jbpmStopExe
     */
    public String getJbpmStopExe() {
        return jbpmStopExe;
    }

    /**
     * @param jbpmStopExe the jbpmStopExe to set
     */
    public void setJbpmStopExe(String jbpmStopExe) {
        this.jbpmStopExe = jbpmStopExe;
    }

    /**
     * @return the jbpmCreatorId
     */
    public String getJbpmCreatorId() {
        return jbpmCreatorId;
    }

    /**
     * @param jbpmCreatorId the jbpmCreatorId to set
     */
    public void setJbpmCreatorId(String jbpmCreatorId) {
        this.jbpmCreatorId = jbpmCreatorId;
    }

    /**
     * @return the jbpmCreatorName
     */
    public String getJbpmCreatorName() {
        return jbpmCreatorName;
    }

    /**
     * @param jbpmCreatorName the jbpmCreatorName to set
     */
    public void setJbpmCreatorName(String jbpmCreatorName) {
        this.jbpmCreatorName = jbpmCreatorName;
    }

    /**
     * @return the jbpmApplyerName
     */
    public String getJbpmApplyerName() {
        return jbpmApplyerName;
    }

    /**
     * @param jbpmApplyerName the jbpmApplyerName to set
     */
    public void setJbpmApplyerName(String jbpmApplyerName) {
        this.jbpmApplyerName = jbpmApplyerName;
    }

    /**
     * @return the jbpmApplyerMobile
     */
    public String getJbpmApplyerMobile() {
        return jbpmApplyerMobile;
    }

    /**
     * @param jbpmApplyerMobile the jbpmApplyerMobile to set
     */
    public void setJbpmApplyerMobile(String jbpmApplyerMobile) {
        this.jbpmApplyerMobile = jbpmApplyerMobile;
    }

    /**
     * @return the jbpmDefAllLimitDays
     */
    public String getJbpmDefAllLimitDays() {
        return jbpmDefAllLimitDays;
    }

    /**
     * @param jbpmDefAllLimitDays the jbpmDefAllLimitDays to set
     */
    public void setJbpmDefAllLimitDays(String jbpmDefAllLimitDays) {
        this.jbpmDefAllLimitDays = jbpmDefAllLimitDays;
    }

    /**
     * @return the jbpmDefAllLimitType
     */
    public String getJbpmDefAllLimitType() {
        return jbpmDefAllLimitType;
    }

    /**
     * @param jbpmDefAllLimitType the jbpmDefAllLimitType to set
     */
    public void setJbpmDefAllLimitType(String jbpmDefAllLimitType) {
        this.jbpmDefAllLimitType = jbpmDefAllLimitType;
    }

    /**
     * @return the jbpmIsParallel
     */
    public String getJbpmIsParallel() {
        return jbpmIsParallel;
    }

    /**
     * @param jbpmIsParallel the jbpmIsParallel to set
     */
    public void setJbpmIsParallel(String jbpmIsParallel) {
        this.jbpmIsParallel = jbpmIsParallel;
    }

    /**
     * @return the jbpmHandleOpinion
     */
    public String getJbpmHandleOpinion() {
        return jbpmHandleOpinion;
    }

    /**
     * @param jbpmHandleOpinion the jbpmHandleOpinion to set
     */
    public void setJbpmHandleOpinion(String jbpmHandleOpinion) {
        this.jbpmHandleOpinion = jbpmHandleOpinion;
    }

    /**
     * @return the jbpmAssignJson
     */
    public String getJbpmAssignJson() {
        return jbpmAssignJson;
    }

    /**
     * @param jbpmAssignJson the jbpmAssignJson to set
     */
    public void setJbpmAssignJson(String jbpmAssignJson) {
        this.jbpmAssignJson = jbpmAssignJson;
    }

    /**
     * @return the jbpmParentDefId
     */
    public String getJbpmParentDefId() {
        return jbpmParentDefId;
    }

    /**
     * @param jbpmParentDefId the jbpmParentDefId to set
     */
    public void setJbpmParentDefId(String jbpmParentDefId) {
        this.jbpmParentDefId = jbpmParentDefId;
    }

    /**
     * @return the jbpmParentDefVersion
     */
    public String getJbpmParentDefVersion() {
        return jbpmParentDefVersion;
    }

    /**
     * @param jbpmParentDefVersion the jbpmParentDefVersion to set
     */
    public void setJbpmParentDefVersion(String jbpmParentDefVersion) {
        this.jbpmParentDefVersion = jbpmParentDefVersion;
    }

    /**
     * @return the jbpmParentOperingNodeKey
     */
    public String getJbpmParentOperingNodeKey() {
        return jbpmParentOperingNodeKey;
    }

    /**
     * @param jbpmParentOperingNodeKey the jbpmParentOperingNodeKey to set
     */
    public void setJbpmParentOperingNodeKey(String jbpmParentOperingNodeKey) {
        this.jbpmParentOperingNodeKey = jbpmParentOperingNodeKey;
    }

    /**
     * @return the jbpmFieldAuthJson
     */
    public String getJbpmFieldAuthJson() {
        return jbpmFieldAuthJson;
    }

    /**
     * @param jbpmFieldAuthJson the jbpmFieldAuthJson to set
     */
    public void setJbpmFieldAuthJson(String jbpmFieldAuthJson) {
        this.jbpmFieldAuthJson = jbpmFieldAuthJson;
    }
}
