package com.housoo.platform.workflow.model;

import java.io.Serializable;

/**
 * 描述 流转流转对象
 *
 * @author 胡裕
 * @created 2017年5月19日 下午5:00:51
 */
public class FlowAssignInfo implements Serializable {
    /**
     * 办理人类型:后台用户
     */
    public static final String ASSIGNERNATURE_BACK = "1";
    /**
     * 办理人类型:网站用户
     */
    public static final String ASSIGNERNATURE_WEBSIZE = "2";
    /**
     * 下一环节KEY
     */
    private String nextNodeKey;
    /**
     * 下一环节名称
     */
    private String nextNodeName;
    /**
     * 下一环节办理人IDS
     */
    private String nextAssignerIds;
    /**
     * 下一环节是否是串审环节(1:是 -1:否)
     */
    private String nextIsOrderTask;
    /**
     * 办理人性质(1:后台用户 2:网站用户)
     */
    private String nextAssignerNature;

    /**
     * @return the nextNodeKey
     */
    public String getNextNodeKey() {
        return nextNodeKey;
    }

    /**
     * @param nextNodeKey the nextNodeKey to set
     */
    public void setNextNodeKey(String nextNodeKey) {
        this.nextNodeKey = nextNodeKey;
    }

    /**
     * @return the nextNodeName
     */
    public String getNextNodeName() {
        return nextNodeName;
    }

    /**
     * @param nextNodeName the nextNodeName to set
     */
    public void setNextNodeName(String nextNodeName) {
        this.nextNodeName = nextNodeName;
    }

    /**
     * @return the nextAssignerIds
     */
    public String getNextAssignerIds() {
        return nextAssignerIds;
    }

    /**
     * @param nextAssignerIds the nextAssignerIds to set
     */
    public void setNextAssignerIds(String nextAssignerIds) {
        this.nextAssignerIds = nextAssignerIds;
    }

    /**
     * @return the nextIsOrderTask
     */
    public String getNextIsOrderTask() {
        return nextIsOrderTask;
    }

    /**
     * @param nextIsOrderTask the nextIsOrderTask to set
     */
    public void setNextIsOrderTask(String nextIsOrderTask) {
        this.nextIsOrderTask = nextIsOrderTask;
    }

    /**
     * @return the nextAssignerNature
     */
    public String getNextAssignerNature() {
        return nextAssignerNature;
    }

    /**
     * @param nextAssignerNature the nextAssignerNature to set
     */
    public void setNextAssignerNature(String nextAssignerNature) {
        this.nextAssignerNature = nextAssignerNature;
    }
}
