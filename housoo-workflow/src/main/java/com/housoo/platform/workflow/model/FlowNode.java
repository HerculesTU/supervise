/*
 * Copyright (c) 2005, 2017, STOOGES Technology Co.,Ltd. All rights reserved.
 * STOOGES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.model;

import java.io.Serializable;

/**
 * 描述 流程节点对象
 *
 * @author 胡裕
 * @created 2017年5月12日 下午4:36:06
 */
public class FlowNode implements Serializable {
    /**
     * 任务性质:单人
     */
    public static final String TASKNATURE_SINGLE = "1";
    /**
     * 任务性质:多人
     */
    public static final String TASKNATURE_MULTI = "2";
    /**
     * 任务性质:自由
     */
    public static final String TASKNATURE_FREE = "3";
    /**
     * 节点类型:开始
     */
    public static final String NODETYPE_START = "start";
    /**
     * 节点类型:任务
     */
    public static final String NODETYPE_TASK = "task";
    /**
     * 节点类型:判断
     */
    public static final String NODETYPE_DECISION = "decision";
    /**
     * 节点类型:结束
     */
    public static final String NODETYPE_END = "end";
    /**
     * 节点类型:并行
     */
    public static final String NODETYPE_PARALLEL = "parallel";
    /**
     * 节点类型:合并
     */
    public static final String NODETYPE_JOIN = "join";
    /**
     * 节点类型:子流程
     */
    public static final String NODETYPE_SUBPROCESS = "subprocess";

    /**
     * 正在运行节点颜色
     */
    public static final String NODECOLOR_RUNNING = "red";
    /**
     * 已经运行完成节点颜色
     */
    public static final String NODECOLOR_OVER = "#0997F7";
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 节点KEY
     */
    private String nodeKey;
    /**
     * 节点类型
     */
    private String nodeType;
    /**
     * 节点连线文本
     */
    private String nodeLinkText;


    /**
     * @return the nodeLinkText
     */
    public String getNodeLinkText() {
        return nodeLinkText;
    }

    /**
     * @param nodeLinkText the nodeLinkText to set
     */
    public void setNodeLinkText(String nodeLinkText) {
        this.nodeLinkText = nodeLinkText;
    }

    /**
     * @return the nodeName
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * @param nodeName the nodeName to set
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * @return the nodeKey
     */
    public String getNodeKey() {
        return nodeKey;
    }

    /**
     * @param nodeKey the nodeKey to set
     */
    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    /**
     * @return the nodeType
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * @param nodeType the nodeType to set
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }
}
