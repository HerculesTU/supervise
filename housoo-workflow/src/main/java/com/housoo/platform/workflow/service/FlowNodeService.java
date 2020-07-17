/*
 * Copyright (c) 2005, 2017, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service;

import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.core.service.BaseService;
import com.housoo.platform.workflow.model.FlowNode;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述
 *
 * @author 胡裕
 * @created 2017年5月6日 下午3:19:43
 */
public interface FlowNodeService extends BaseService {
    /**
     * 根据流程定义ID和版本号获取配置节点列表
     *
     * @param defId
     * @param flowVersion
     * @return
     */
    public List<Map> findDefNodeList(String defId, int flowVersion);

    /**
     * 根据节点列表和节点key获取节点类型和名称
     *
     * @param nodeList
     * @param nodeKey
     * @return
     */
    public String[] getNodeTypeAndName(List<Map> nodeList, String nodeKey);

    /**
     * 获取下一环节列表
     *
     * @param defId:流程定义ID
     * @param flowVersion:流程版本号
     * @param fromNodeKey:来源节点KEY
     * @param nextNodeTypeSet:下一节点类型集合
     * @return
     */
    public List<Map> findNextNodeList(String defId, int flowVersion,
                                      String fromNodeKey, Set<String> nextNodeTypeSet);

    /**
     * 获取下一环节可选列表
     *
     * @param paramJson
     * @return
     */
    public List<Map> findNextNodeForSelect(String paramJson);

    /**
     * 根据流程定义对象获取开始节点
     *
     * @param flowDef
     * @return
     */
    public FlowNode getStartFlowNode(Map<String, Object> flowDef);

    /**
     * 获取下一环节列表数据
     *
     * @param defId:流程定义ID
     * @param flowVersion:流程版本号
     * @param fromNodeKey:来源节点KEY
     * @return
     */
    public List<FlowNode> findNextNodeList(String defId, int flowVersion, String fromNodeKey);

    /**
     * 获取下一环节的连线文本
     *
     * @param defId
     * @param flowVersion
     * @param fromNodeKey
     * @return
     */
    public String getNextNodeLinkText(String defId, int flowVersion, String fromNodeKey);

    /**
     * 获取去往目标节点KEY
     *
     * @param jbpmFlowInfo
     * @return
     */
    public String getToNodeKey(JbpmFlowInfo jbpmFlowInfo);

    /**
     * 获取合并节点的KEY
     *
     * @param defId
     * @param flowVersion
     * @param fromNodeKey
     * @return
     */
    public String getJoinNodeKey(String defId, int flowVersion, String fromNodeKey);

    /**
     * 获取并行节点KEY的集合
     *
     * @param defId
     * @param flowVersion
     * @param joinNodeKey
     * @return
     */
    public List<String> getParallelNodeKeySet(String defId, int flowVersion, String joinNodeKey);

    /**
     * 获取来源节点KEY列表
     *
     * @param defId
     * @param flowVersion
     * @param nodeKey
     * @return
     */
    public List<String> getPreNodeKeyList(String defId, int flowVersion, String nodeKey);

    /**
     * 获取来源节点列表
     *
     * @param defId
     * @param flowVersion
     * @param nodeKey
     * @return
     */
    public List<FlowNode> getPreNodeList(String defId, int flowVersion, String nodeKey);

    /**
     * 获取节点对象
     *
     * @param nodeList
     * @param nodeKey
     * @return
     */
    public FlowNode getFlowNode(List<Map> nodeList, String nodeKey);

    /**
     * 获取先前所有节点列表
     *
     * @param defId
     * @param flowVersion
     * @param nodeKey
     * @return
     */
    public List<FlowNode> getAllPreNodeList(String defId, int flowVersion, String nodeKey);

    /**
     * 获取节点对象
     *
     * @param defId
     * @param flowVersoin
     * @param nodeKey
     * @return
     */
    public FlowNode getFlowNode(String defId, int flowVersoin, String nodeKey);

    /**
     * 根据流程定义ID和版本号还有类型获取节点类别
     *
     * @param defId
     * @param flowVersion
     * @param nodeType
     * @return
     */
    public List<Map> findDefNodeList(String defId, int flowVersion, String nodeType);
}
