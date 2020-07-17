/*
 * Copyright (c) 2005, 2017, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.model;

import java.io.Serializable;

/**
 * 描述 流程下一环节办理人实体对象
 *
 * @author 胡裕
 * @created 2017年5月10日 下午4:37:56
 */
public class FlowNextHandler implements Serializable {

    /**
     * 下一环节办理人标识
     */
    private String assignerId;
    /**
     * 下一环节办理人姓名
     */
    private String assignerName;
    /**
     * 下一环节办理人编码
     */
    private String assignerCode;
    /**
     * 下一环节办理人手机号
     */
    private String assignerMobile;
    /**
     * 下一环节办理人邮件地址
     */
    private String assignerEmail;
    /**
     * 任务排序值(-1代表不进行排序)
     */
    private int taskOrder;

    /**
     * @return the taskOrder
     */
    public int getTaskOrder() {
        return taskOrder;
    }

    /**
     * @param taskOrder the taskOrder to set
     */
    public void setTaskOrder(int taskOrder) {
        this.taskOrder = taskOrder;
    }

    /**
     * @return the assignerId
     */
    public String getAssignerId() {
        return assignerId;
    }

    /**
     * @param assignerId the assignerId to set
     */
    public void setAssignerId(String assignerId) {
        this.assignerId = assignerId;
    }

    /**
     * @return the assignerName
     */
    public String getAssignerName() {
        return assignerName;
    }

    /**
     * @param assignerName the assignerName to set
     */
    public void setAssignerName(String assignerName) {
        this.assignerName = assignerName;
    }

    /**
     * @return the assignerCode
     */
    public String getAssignerCode() {
        return assignerCode;
    }

    /**
     * @param assignerCode the assignerCode to set
     */
    public void setAssignerCode(String assignerCode) {
        this.assignerCode = assignerCode;
    }

    /**
     * @return the assignerMobile
     */
    public String getAssignerMobile() {
        return assignerMobile;
    }

    /**
     * @param assignerMobile the assignerMobile to set
     */
    public void setAssignerMobile(String assignerMobile) {
        this.assignerMobile = assignerMobile;
    }

    /**
     * @return the assignerEmail
     */
    public String getAssignerEmail() {
        return assignerEmail;
    }

    /**
     * @param assignerEmail the assignerEmail to set
     */
    public void setAssignerEmail(String assignerEmail) {
        this.assignerEmail = assignerEmail;
    }
}
