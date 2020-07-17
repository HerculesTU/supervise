package com.housoo.platform.workflow.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述 节点经办人配置
 *
 * @author 胡裕
 * @created 2017年5月17日 下午2:29:53
 */
public class NodeAssigner implements Serializable {
    /**
     * 接口类型
     */
    public static final String HANDLETYPE_INTERFACEL = "1";
    /**
     * 弹出框类型
     */
    public static final String HANDLETYPE_WIN = "2";
    /**
     * 办理人过滤规则:和当前办理人同属一个单位
     */
    public static final String FILTERRULE_SAMECOMPANY = "1";
    /**
     * 是否支持串审(1：是  -1：否)
     */
    private String isOrder;
    /**
     * 过滤规则
     */
    private String filterRule;
    /**
     * 缺省值接口
     */
    private String defaultInter;
    /**
     * 变量值
     */
    private String varValues;
    /**
     * 办理人控件类型(1:接口 2:弹出框选择)
     */
    private String handlerType;
    /**
     * 弹出框URL地址
     */
    private String handlerUrl;
    /**
     * 弹出框宽度
     */
    private String handlerWidth;
    /**
     * 弹出框高度
     */
    private String handlerHeight;
    /**
     * 办理人性质 (1:后台用户 2:网站用户)
     */
    private String handlerNature;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 节点KEY
     */
    private String nodeKey;
    /**
     * 下一环节审批类型(1:单人任务,2:多人任务,3:自由任务)
     */
    private String nextAuditType;
    /**
     * 办理人IDS
     */
    private String assignerIds;
    /**
     * 办理人NAMES
     */
    private String assignerNames;
    /**
     * 环节办理人列表
     */
    private List<FlowNextHandler> nextHandlers = new ArrayList<FlowNextHandler>();

    /**
     * @return the handlerNature
     */
    public String getHandlerNature() {
        return handlerNature;
    }

    /**
     * @param handlerNature the handlerNature to set
     */
    public void setHandlerNature(String handlerNature) {
        this.handlerNature = handlerNature;
    }

    /**
     * @return the isOrder
     */
    public String getIsOrder() {
        return isOrder;
    }

    /**
     * @param isOrder the isOrder to set
     */
    public void setIsOrder(String isOrder) {
        this.isOrder = isOrder;
    }

    /**
     * @return the filterRule
     */
    public String getFilterRule() {
        return filterRule;
    }

    /**
     * @param filterRule the filterRule to set
     */
    public void setFilterRule(String filterRule) {
        this.filterRule = filterRule;
    }

    /**
     * @return the defaultInter
     */
    public String getDefaultInter() {
        return defaultInter;
    }

    /**
     * @param defaultInter the defaultInter to set
     */
    public void setDefaultInter(String defaultInter) {
        this.defaultInter = defaultInter;
    }

    /**
     * @return the varValues
     */
    public String getVarValues() {
        return varValues;
    }

    /**
     * @param varValues the varValues to set
     */
    public void setVarValues(String varValues) {
        this.varValues = varValues;
    }

    /**
     * @return the handlerType
     */
    public String getHandlerType() {
        return handlerType;
    }

    /**
     * @param handlerType the handlerType to set
     */
    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    /**
     * @return the handlerUrl
     */
    public String getHandlerUrl() {
        return handlerUrl;
    }

    /**
     * @param handlerUrl the handlerUrl to set
     */
    public void setHandlerUrl(String handlerUrl) {
        this.handlerUrl = handlerUrl;
    }

    /**
     * @return the handlerWidth
     */
    public String getHandlerWidth() {
        return handlerWidth;
    }

    /**
     * @param handlerWidth the handlerWidth to set
     */
    public void setHandlerWidth(String handlerWidth) {
        this.handlerWidth = handlerWidth;
    }

    /**
     * @return the handlerHeight
     */
    public String getHandlerHeight() {
        return handlerHeight;
    }

    /**
     * @param handlerHeight the handlerHeight to set
     */
    public void setHandlerHeight(String handlerHeight) {
        this.handlerHeight = handlerHeight;
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
     * @return the nextHandlers
     */
    public List<FlowNextHandler> getNextHandlers() {
        return nextHandlers;
    }

    /**
     * @param nextHandlers the nextHandlers to set
     */
    public void setNextHandlers(List<FlowNextHandler> nextHandlers) {
        this.nextHandlers = nextHandlers;
    }

    /**
     * @return the assignerIds
     */
    public String getAssignerIds() {
        return assignerIds;
    }

    /**
     * @param assignerIds the assignerIds to set
     */
    public void setAssignerIds(String assignerIds) {
        this.assignerIds = assignerIds;
    }

    /**
     * @return the assignerNames
     */
    public String getAssignerNames() {
        return assignerNames;
    }

    /**
     * @param assignerNames the assignerNames to set
     */
    public void setAssignerNames(String assignerNames) {
        this.assignerNames = assignerNames;
    }

    /**
     * @return the nextAuditType
     */
    public String getNextAuditType() {
        return nextAuditType;
    }

    /**
     * @param nextAuditType the nextAuditType to set
     */
    public void setNextAuditType(String nextAuditType) {
        this.nextAuditType = nextAuditType;
    }

}
