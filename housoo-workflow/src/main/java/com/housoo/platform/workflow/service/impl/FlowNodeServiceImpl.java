/*
 * Copyright (c) 2005, 2017, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.workflow.dao.FlowNodeDao;
import com.housoo.platform.workflow.model.FlowAssignInfo;
import com.housoo.platform.workflow.model.FlowNode;
import com.housoo.platform.workflow.service.FlowDefService;
import com.housoo.platform.workflow.service.FlowNodeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 描述
 *
 * @author 胡裕
 * @created 2017年5月6日 下午3:19:55
 */
@Service("flowNodeService")
public class FlowNodeServiceImpl extends BaseServiceImpl implements
        FlowNodeService {

    /**
     * 所引入的dao
     */
    @Resource
    private FlowNodeDao dao;
    /**
     *
     */
    @Resource
    private FlowDefService flowDefService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }


    /**
     * 根据流程定义ID和版本号获取配置节点列表
     *
     * @param defId
     * @param flowVersion
     * @return
     */
    @Override
    public List<Map> findDefNodeList(String defId, int flowVersion) {
        Map<String, Object> flowDef = dao.getRecord("JBPM6_FLOWDEF",
                new String[]{"FLOWDEF_ID"}, new Object[]{defId});
        int newestDefVersion = Integer.parseInt(flowDef.get("FLOWDEF_VERSION").toString());
        String FLOWDEF_NODEJSON = (String) flowDef.get("FLOWDEF_NODEJSON");
        if (flowVersion != newestDefVersion) {
            Map<String, Object> hist = dao.getRecord("JBPM6_HIST_DEPLOY",
                    new String[]{"FLOWDEF_ID", "FLOWDEF_VERSION"}, new Object[]{defId, flowVersion});
            FLOWDEF_NODEJSON = (String) hist.get("FLOWDEF_NODEJSON");
        }
        if (StringUtils.isNotEmpty(FLOWDEF_NODEJSON)) {
            return JSON.parseArray(FLOWDEF_NODEJSON, Map.class);
        } else {
            return null;
        }
    }

    /**
     * 根据节点列表和节点key获取节点类型和名称
     *
     * @param nodeList
     * @return
     */
    @Override
    public String[] getNodeTypeAndName(List<Map> nodeList, String nodeKey) {
        String[] typeAndName = new String[2];
        for (Map<String, Object> node : nodeList) {
            String key = node.get("key").toString();
            String nodeType = node.get("nodeType").toString();
            String text = node.get("text").toString();
            if (key.equals(nodeKey)) {
                typeAndName[0] = nodeType;
                typeAndName[1] = text;
                break;
            }
        }
        return typeAndName;
    }

    /**
     * 获取节点对象
     *
     * @param nodeList
     * @param nodeKey
     * @return
     */
    @Override
    public FlowNode getFlowNode(List<Map> nodeList, String nodeKey) {
        FlowNode flowNode = null;
        for (Map<String, Object> node : nodeList) {
            String key = node.get("key").toString();
            String nodeType = node.get("nodeType").toString();
            String text = node.get("text").toString();
            if (key.equals(nodeKey)) {
                flowNode = new FlowNode();
                flowNode.setNodeName(text);
                flowNode.setNodeKey(key);
                flowNode.setNodeType(nodeType);
                break;
            }
        }
        return flowNode;
    }

    /**
     * 获取下一环节列表
     *
     * @param nextNodeList
     * @param nodeList
     * @param linkList
     * @param nextNodeTypeSet
     * @param fromNodeKey
     * @return
     */
    private List<Map> fintNextNode(List<Map> nextNodeList, List<Map> nodeList,
                                   List<Map> linkList, Set<String> nextNodeTypeSet, String fromNodeKey, Set<String> nextNodeKeySet) {
        if (nextNodeList == null) {
            nextNodeList = new ArrayList<Map>();
            nextNodeKeySet = new HashSet<String>();
        }
        for (Map<String, Object> node : nodeList) {
            String key = node.get("key").toString();
            if (key.equals(fromNodeKey)) {
                for (Map<String, Object> link : linkList) {
                    String toNodeKey = link.get("to").toString();
                    String linkFromNodeKey = link.get("from").toString();
                    String[] toNodeTypeAndName = this.getNodeTypeAndName(nodeList, toNodeKey);
                    String toNodeType = toNodeTypeAndName[0];
                    String toNodeName = toNodeTypeAndName[1];
                    if (nextNodeTypeSet.contains(toNodeType) && linkFromNodeKey.equals(fromNodeKey)) {
                        if (!nextNodeKeySet.contains(toNodeKey)) {
                            Map nextNode = new HashMap();
                            nextNode.put("NODE_KEY", toNodeKey);
                            nextNode.put("NODE_NAME", toNodeName);
                            nextNode.put("NODE_TYPE", toNodeType);
                            nextNodeList.add(nextNode);
                            nextNodeKeySet.add(toNodeKey);
                        }
                    } else if (linkFromNodeKey.equals(fromNodeKey)) {
                        fintNextNode(nextNodeList, nodeList, linkList,
                                nextNodeTypeSet, toNodeKey, nextNodeKeySet);
                    }
                }
            }
        }
        return nextNodeList;
    }

    /**
     * 获取下一环节列表
     *
     * @param defId:流程定义ID
     * @param flowVersion:流程版本号
     * @param fromNodeKey:来源节点KEY
     * @param nextNodeTypeSet:下一节点类型
     * @return
     */
    @Override
    @SuppressWarnings("rawtypes")
    public List<Map> findNextNodeList(String defId, int flowVersion,
                                      String fromNodeKey, Set<String> nextNodeTypeSet) {
        Map<String, Object> flowDef = flowDefService.getFlowDefInfo(defId, flowVersion);
        String FLOWDEF_NODEJSON = (String) flowDef.get("FLOWDEF_NODEJSON");
        String FLOWDEF_LINKJSON = (String) flowDef.get("FLOWDEF_LINKJSON");
        List<Map> nodeList = JSON.parseArray(FLOWDEF_NODEJSON, Map.class);
        List<Map> linkList = JSON.parseArray(FLOWDEF_LINKJSON, Map.class);
        List<Map> nextNodeList = this.fintNextNode(null, nodeList, linkList,
                nextNodeTypeSet, fromNodeKey, null);
        return nextNodeList;
    }

    /**
     * 获取下一环节列表数据
     *
     * @param defId:流程定义ID
     * @param flowVersion:流程版本号
     * @param fromNodeKey:来源节点KEY
     * @return
     */
    @Override
    public List<FlowNode> findNextNodeList(String defId, int flowVersion, String fromNodeKey) {
        Map<String, Object> flowDef = flowDefService.getFlowDefInfo(defId, flowVersion);
        String FLOWDEF_NODEJSON = (String) flowDef.get("FLOWDEF_NODEJSON");
        String FLOWDEF_LINKJSON = (String) flowDef.get("FLOWDEF_LINKJSON");
        List<Map> nodeList = JSON.parseArray(FLOWDEF_NODEJSON, Map.class);
        List<Map> linkList = JSON.parseArray(FLOWDEF_LINKJSON, Map.class);
        List<FlowNode> nextNodeList = new ArrayList<FlowNode>();
        for (Map<String, Object> node : nodeList) {
            String key = node.get("key").toString();
            if (key.equals(fromNodeKey)) {
                for (Map<String, Object> link : linkList) {
                    String toNodeKey = link.get("to").toString();
                    String linkFromNodeKey = link.get("from").toString();
                    String linkText = "";
                    if (link.get("text") != null) {
                        linkText = link.get("text").toString();
                    }
                    String[] toNodeTypeAndName = this.getNodeTypeAndName(nodeList, toNodeKey);
                    String toNodeType = toNodeTypeAndName[0];
                    String toNodeName = toNodeTypeAndName[1];
                    if (linkFromNodeKey.equals(fromNodeKey)) {
                        FlowNode nextNode = new FlowNode();
                        nextNode.setNodeKey(toNodeKey);
                        nextNode.setNodeName(toNodeName);
                        nextNode.setNodeType(toNodeType);
                        nextNode.setNodeLinkText(linkText);
                        nextNodeList.add(nextNode);
                    }
                }
            }
        }
        return nextNodeList;
    }

    /**
     * 获取下一环节的连线文本
     *
     * @param defId
     * @param flowVersion
     * @param fromNodeKey
     * @return
     */
    @Override
    public String getNextNodeLinkText(String defId, int flowVersion, String fromNodeKey) {
        List<FlowNode> nodeList = this.findNextNodeList(defId, flowVersion, fromNodeKey);
        String nodeLinkText = "";
        for (FlowNode node : nodeList) {
            if (StringUtils.isNotEmpty(node.getNodeLinkText())) {
                nodeLinkText = node.getNodeLinkText();
                break;
            }
        }
        if (StringUtils.isEmpty(nodeLinkText)) {
            nodeLinkText = "办理";
        }
        return nodeLinkText;
    }

    /**
     * 获取下一环节可选列表
     *
     * @param paramJson
     * @return
     */
    @Override
    public List<Map> findNextNodeForSelect(String paramJson) {
        if (StringUtils.isNotEmpty(paramJson)) {
            Map flowDefParam = JSON.parseObject(paramJson, Map.class);
            String FLOWDEF_ID = flowDefParam.get("FLOWDEF_ID").toString();
            if (StringUtils.isNotEmpty(FLOWDEF_ID)) {
                String FLOWDEF_VERSION = flowDefParam.get("FLOWDEF_VERSION").toString();
                String NODE_KEY = flowDefParam.get("NODE_KEY").toString();
                int flowVersion = Integer.parseInt(FLOWDEF_VERSION);
                Set<String> nextNodeType = new HashSet<String>();
                nextNodeType.add(FlowNode.NODETYPE_TASK);
                nextNodeType.add(FlowNode.NODETYPE_SUBPROCESS);
                nextNodeType.add(FlowNode.NODETYPE_START);
                List<Map> nextNodeList = this.findNextNodeList(FLOWDEF_ID,
                        flowVersion, NODE_KEY, nextNodeType);
                for (Map nextNode : nextNodeList) {
                    nextNode.put("VALUE", nextNode.get("NODE_KEY"));
                    nextNode.put("LABEL", nextNode.get("NODE_NAME"));
                }
                return nextNodeList;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 根据流程定义对象获取开始节点
     *
     * @param flowDef
     * @return
     */
    @Override
    public FlowNode getStartFlowNode(Map<String, Object> flowDef) {
        String FLOWDEF_NODEJSON = (String) flowDef.get("FLOWDEF_NODEJSON");
        List<Map> nodeList = JSON.parseArray(FLOWDEF_NODEJSON, Map.class);
        FlowNode flowNode = new FlowNode();
        for (Map node : nodeList) {
            String nodeType = (String) node.get("nodeType");
            String nodeKey = node.get("key").toString();
            String nodeName = node.get("text").toString();
            if (nodeType.equals(FlowNode.NODETYPE_START)) {
                flowNode.setNodeKey(nodeKey);
                flowNode.setNodeName(nodeName);
                flowNode.setNodeType(nodeType);
                break;
            }
        }
        return flowNode;
    }

    /**
     * 获取去往目标节点KEY
     *
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public String getToNodeKey(JbpmFlowInfo jbpmFlowInfo) {
        //获取下一环节办理人信息数据
        String assignJson = jbpmFlowInfo.getJbpmAssignJson();
        if (StringUtils.isNotEmpty(assignJson)) {
            List<FlowAssignInfo> assignList = JSON.parseArray(assignJson, FlowAssignInfo.class);
            if (assignList.size() == 1) {
                return assignList.get(0).getNextNodeKey();
            } else if (assignList.size() > 1) {
                //获取并行环节的节点KEY
                List<FlowNode> nodeList = this.findNextNodeList(jbpmFlowInfo.getJbpmDefId(),
                        Integer.parseInt(jbpmFlowInfo.getJbpmDefVersion()), jbpmFlowInfo.getJbpmOperingNodeKey());
                return nodeList.get(0).getNodeKey();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取合并节点的KEY
     *
     * @param defId
     * @param flowVersion
     * @param fromNodeKey
     * @return
     */
    @Override
    public String getJoinNodeKey(String defId, int flowVersion, String fromNodeKey) {
        List<FlowNode> nextNodeList = this.findNextNodeList(defId, flowVersion, fromNodeKey);
        if (nextNodeList != null && nextNodeList.size() == 1) {
            String nextNodeType = nextNodeList.get(0).getNodeType();
            if (nextNodeType.equals(FlowNode.NODETYPE_JOIN)) {
                return nextNodeList.get(0).getNodeKey();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * @param startKey
     * @param nodeList
     * @param linkList
     * @param keySet
     * @return
     */
    private List<String> getParallelNodeKeySet(String startKey, List<Map> nodeList,
                                               List<Map> linkList, List<String> keySet) {
        if (keySet == null) {
            keySet = new ArrayList<String>();
        }
        for (Map<String, Object> link : linkList) {
            String to = link.get("to").toString();
            String from = link.get("from").toString();
            if (to.equals(startKey)) {
                for (Map<String, Object> node : nodeList) {
                    String nodeType = node.get("nodeType").toString();
                    String nodeKey = node.get("key").toString();
                    if (nodeKey.equals(from) && "task".equals(nodeType)) {
                        if (!keySet.contains(nodeKey)) {
                            keySet.add(nodeKey);
                        }
                        this.getParallelNodeKeySet(nodeKey, nodeList, linkList, keySet);
                    }
                }
            }
        }
        return keySet;
    }

    /**
     * 获取并行节点KEY的集合
     *
     * @param defId
     * @param flowVersion
     * @param joinNodeKey
     * @return
     */
    @Override
    public List<String> getParallelNodeKeySet(String defId, int flowVersion, String joinNodeKey) {
        Map<String, Object> flowDef = flowDefService.getFlowDefInfo(defId, flowVersion);
        String FLOWDEF_NODEJSON = (String) flowDef.get("FLOWDEF_NODEJSON");
        String FLOWDEF_LINKJSON = (String) flowDef.get("FLOWDEF_LINKJSON");
        List<Map> nodeList = JSON.parseArray(FLOWDEF_NODEJSON, Map.class);
        List<Map> linkList = JSON.parseArray(FLOWDEF_LINKJSON, Map.class);
        List<String> keySet = this.getParallelNodeKeySet(joinNodeKey, nodeList, linkList, null);
        return keySet;
    }

    /**
     * 获取来源节点KEY列表
     *
     * @param defId
     * @param flowVersion
     * @param nodeKey
     * @return
     */
    @Override
    public List<String> getPreNodeKeyList(String defId, int flowVersion, String nodeKey) {
        Map<String, Object> flowDef = flowDefService.getFlowDefInfo(defId, flowVersion);
        String FLOWDEF_LINKJSON = (String) flowDef.get("FLOWDEF_LINKJSON");
        List<Map> linkList = JSON.parseArray(FLOWDEF_LINKJSON, Map.class);
        List<String> keyList = new ArrayList<String>();
        for (Map<String, Object> link : linkList) {
            String to = link.get("to").toString();
            String from = link.get("from").toString();
            if (to.equals(nodeKey)) {
                if (!keyList.contains(from)) {
                    keyList.add(from);
                }
            }
        }
        return keyList;
    }

    /**
     * 获取来源节点列表
     *
     * @param defId
     * @param flowVersion
     * @param nodeKey
     * @return
     */
    @Override
    public List<FlowNode> getPreNodeList(String defId, int flowVersion, String nodeKey) {
        Map<String, Object> flowDef = flowDefService.getFlowDefInfo(defId, flowVersion);
        String FLOWDEF_LINKJSON = (String) flowDef.get("FLOWDEF_LINKJSON");
        String FLOWDEF_NODEJSON = (String) flowDef.get("FLOWDEF_NODEJSON");
        List<Map> linkList = JSON.parseArray(FLOWDEF_LINKJSON, Map.class);
        List<Map> nodeList = JSON.parseArray(FLOWDEF_NODEJSON, Map.class);
        List<FlowNode> flowNodeList = new ArrayList<FlowNode>();
        Set<String> keySet = new HashSet<String>();
        for (Map<String, Object> link : linkList) {
            String to = link.get("to").toString();
            String from = link.get("from").toString();
            if (to.equals(nodeKey) && !keySet.contains(from)) {
                FlowNode flowNode = this.getFlowNode(nodeList, from);
                flowNodeList.add(flowNode);
            }
        }
        return flowNodeList;
    }

    /**
     * 获取节点对象
     *
     * @param defId
     * @param flowVersoin
     * @param nodeKey
     * @return
     */
    @Override
    public FlowNode getFlowNode(String defId, int flowVersoin, String nodeKey) {
        Map<String, Object> flowDef = flowDefService.getFlowDefInfo(defId, flowVersoin);
        String FLOWDEF_NODEJSON = (String) flowDef.get("FLOWDEF_NODEJSON");
        List<Map> nodeList = JSON.parseArray(FLOWDEF_NODEJSON, Map.class);
        FlowNode flowNode = this.getFlowNode(nodeList, nodeKey);
        return flowNode;
    }

    /**
     * @param flowNodeList
     * @param keySet
     */
    private void getPreChildNodes(List<FlowNode> flowNodeList, Set<String> keySet,
                                  List<Map> linkList, List<Map> nodeList, String nodeKey) {
        for (Map<String, Object> link : linkList) {
            String to = link.get("to").toString();
            String from = link.get("from").toString();
            if (to.equals(nodeKey) && !keySet.contains(from)) {
                FlowNode flowNode = this.getFlowNode(nodeList, from);
                keySet.add(flowNode.getNodeKey());
                if (flowNode.getNodeType().equals(FlowNode.NODETYPE_TASK)
                        || flowNode.getNodeType().equals(FlowNode.NODETYPE_START)) {
                    flowNodeList.add(flowNode);
                }
                this.getPreChildNodes(flowNodeList, keySet, linkList, nodeList, flowNode.getNodeKey());
            }
        }
    }

    /**
     * 获取先前所有节点列表
     *
     * @param defId
     * @param flowVersion
     * @param nodeKey
     * @return
     */
    @Override
    public List<FlowNode> getAllPreNodeList(String defId, int flowVersion, String nodeKey) {
        Map<String, Object> flowDef = flowDefService.getFlowDefInfo(defId, flowVersion);
        String FLOWDEF_LINKJSON = (String) flowDef.get("FLOWDEF_LINKJSON");
        String FLOWDEF_NODEJSON = (String) flowDef.get("FLOWDEF_NODEJSON");
        List<Map> linkList = JSON.parseArray(FLOWDEF_LINKJSON, Map.class);
        List<Map> nodeList = JSON.parseArray(FLOWDEF_NODEJSON, Map.class);
        List<FlowNode> flowNodeList = new ArrayList<FlowNode>();
        Set<String> keySet = new HashSet<String>();
        this.getPreChildNodes(flowNodeList, keySet, linkList, nodeList, nodeKey);
        return flowNodeList;
    }

    /**
     * 根据流程定义ID和版本号还有类型获取节点类别
     *
     * @param defId
     * @param flowVersion
     * @param nodeType
     * @return
     */
    @Override
    public List<Map> findDefNodeList(String defId, int flowVersion, String nodeType) {
        List<Map> defNodeList = this.findDefNodeList(defId, flowVersion);
        List<Map> nodeList = new ArrayList<Map>();
        for (Map defNode : defNodeList) {
            String oldNodeType = (String) defNode.get("nodeType");
            if (oldNodeType.equals(nodeType)) {
                nodeList.add(defNode);
            }
        }
        return nodeList;
    }

}
