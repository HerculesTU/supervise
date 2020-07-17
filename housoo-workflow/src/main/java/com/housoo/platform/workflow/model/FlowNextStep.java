/*
 * Copyright (c) 2005, 2017, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述 下一步环节信息
 *
 * @author 胡裕
 * @created 2017年5月17日 上午9:43:54
 */
public class FlowNextStep implements Serializable {
    /**
     * 环节审批类型:单人任务
     */
    public static final String AUDITTYPE_SINGLE = "1";
    /**
     * 环节审批类型:多人任务
     */
    public static final String AUDITTYPE_MULTI = "2";
    /**
     * 环节审批类型:自由任务
     */
    public static final String AUDITTYPE_FREE = "3";
    /**
     * 下一环节KEYS
     */
    private String nodeKeys;
    /**
     * 下一环节NAMES
     */
    private String nodeNames;
    /**
     * 是否分支(1:是 -1:否)
     */
    private String isBranch;
    /**
     * 分支节点选中的KEY
     */
    private String branchSelectKey;
    /**
     * 节点审核人列表
     */
    private List<NodeAssigner> nodeAssignerList = new ArrayList<NodeAssigner>();


    /**
     * @return the nodeAssignerList
     */
    public List<NodeAssigner> getNodeAssignerList() {
        return nodeAssignerList;
    }

    /**
     * @param nodeAssignerList the nodeAssignerList to set
     */
    public void setNodeAssignerList(List<NodeAssigner> nodeAssignerList) {
        this.nodeAssignerList = nodeAssignerList;
    }

    /**
     * @return the nodeKeys
     */
    public String getNodeKeys() {
        return nodeKeys;
    }

    /**
     * @param nodeKeys the nodeKeys to set
     */
    public void setNodeKeys(String nodeKeys) {
        this.nodeKeys = nodeKeys;
    }

    /**
     * @return the nodeNames
     */
    public String getNodeNames() {
        return nodeNames;
    }

    /**
     * @param nodeNames the nodeNames to set
     */
    public void setNodeNames(String nodeNames) {
        this.nodeNames = nodeNames;
    }

    /**
     * @return the isBranch
     */
    public String getIsBranch() {
        return isBranch;
    }

    /**
     * @param isBranch the isBranch to set
     */
    public void setIsBranch(String isBranch) {
        this.isBranch = isBranch;
    }

    /**
     * @return the branchSelectKey
     */
    public String getBranchSelectKey() {
        return branchSelectKey;
    }

    /**
     * @param branchSelectKey the branchSelectKey to set
     */
    public void setBranchSelectKey(String branchSelectKey) {
        this.branchSelectKey = branchSelectKey;
    }
}
