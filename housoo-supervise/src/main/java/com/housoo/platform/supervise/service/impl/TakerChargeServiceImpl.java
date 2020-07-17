/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.DepartService;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.*;
import com.housoo.platform.supervise.dao.TakerChargeDao;
import com.housoo.platform.supervise.dao.TakerDao;
import com.housoo.platform.supervise.service.SuperviseApproveService;
import com.housoo.platform.supervise.service.SuperviseClazzService;
import com.housoo.platform.supervise.service.TakerChargeService;
import com.housoo.platform.supervise.service.TakerService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * 描述 承办人业务相关service实现类
 *
 * @author cjr
 * @version 1.0
 * @created 2020-04-14 15:30:11
 */
@Service("takerChargeService")
public class TakerChargeServiceImpl extends BaseServiceImpl implements TakerChargeService {

    /**
     * 搜索类型 年度 季度 月份 近七天
     */
    private static final String SEARCHTYPE_YEAR = "year";
    private static final String SEARCHTYPE_QUARTER = "quarter";
    private static final String SEARCHTYPE_MONTH = "month";
    private static final String SEARCHTYPE_DAYS = "days";

    /**
     * 督办节点 ID
     */
    private static final String DRAFT = "0";
    private static final String NODE_ID_1 = "1";
    private static final String NODE_ID_2 = "2";
    private static final String NODE_ID_3 = "3";
    private static final String NODE_ID_4 = "4";
    private static final String NODE_ID_5 = "5";

    /**
     * 督办节点 名称
     */
    private static final String NODE_NAME_1 = "任务发起";
    private static final String NODE_NAME_2 = "督办确认";
    private static final String NODE_NAME_3 = "办理反馈";
    private static final String NODE_NAME_4 = "落实反馈";
    private static final String NODE_NAME_5 = "办结反馈";

    /**
     * 全部 正在 待办 完成督办
     */

    private static final String STATUS_ALL = "1";
    private static final String STATUS_PROCESS = "2";
    private static final String STATUS_DRAFT = "3";
    private static final String STATUS_DONE = "4";
    /**
     * 需要审批
     */
    private static final String NEED_APPROVE_FLAG = "1";

    /**
     * 排序方式
     */
    private static final String ORDER_BY_ASC = "ASC";
    private static final String ORDER_BY_DESC = "DESC";

    /**
     * 所引入的dao
     */
    @Resource
    private TakerChargeDao dao;

    /**
     * 部门管理Service
     */
    @Resource
    private DepartService departService;
    /**
     * SuperviseService
     */
    @Resource
    private TakerChargeService takerChargeService;
    /**
     * SuperviseClazzService
     */
    @Resource
    private SuperviseClazzService superviseClazzService;
    /**
     * SuperviseApproveService
     */
    @Resource
    private SuperviseApproveService superviseApproveService;


    /**
     * 获取dao
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取承办人承办任务列表
     *
     * @param request
     * @return
     */
    @Override
    public List<Map<String, Object>> findTakerSuperviseList(HttpServletRequest request) {
        String departId = (String) PlatAppUtil.getBackPlatLoginUser().get("DEPART_ID");
        StringBuffer sql = new StringBuffer("SELECT s.RECORD_ID,s.SUPERVISE_NO,s.SUPERVISE_CLAZZ_ID,s.SUPERVISE_SOURCE,s.SUPERVISE_TYPE,s.SUPERVISE_CLAZZ,s.SUPERVISE_ITEM_ID,SUPERVISE_ITEM,s.TITLE,s.KEYWORDS,d.DEPART_NAME,s.CREATE_TIME,s.CURRENT_NODE,s.HANDLE_LIMIT,s.STATUS ");
        sql.append("FROM tb_supervise s ");
        sql.append("LEFT JOIN tb_supervise_task t on s.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
        sql.append("LEFT JOIN plat_system_depart d on s.DEPART_ID = d.DEPART_ID ");
        sql.append("where t.DEPART_ID = ? AND YEAR (s.CREATE_TIME) = YEAR (NOW()) ");
        sql.append("AND s.SUPERVISE_CLAZZ_ID = ? and s.STATUS IN ");
        sql.append(PlatStringUtil.getSqlInCondition(request.getParameter("status")));
        sql.append("ORDER BY s.CREATE_TIME DESC ");
        List<Map<String, Object>> list = superviseClazzService.findBySql(sql.toString(), new Object[]{departId, request.getParameter("clazzId")}, null);
        //获取每个事项的节点信息
        if (list != null) {
            for (Map<String, Object> supervise : list) {
                String id = (String) supervise.get("RECORD_ID");
                StringBuffer sql1 = new StringBuffer("select a.NODE_ID,a.NODE_NAME,temp.RECORD_ID,temp.NODE_CONTENT,temp.CREATE_TIME,t.DEPART_ID,d.DEPART_NAME ");
                sql1.append("from tb_supervise_approve a ");
                sql1.append("LEFT JOIN (SELECT RECORD_ID,NODE_CONTENT,CREATE_TIME,NODE_ID,SUPERVISE_ID,SUPERVISE_NO from tb_supervise_node_info where SUPERVISE_ID = ? ) temp ");
                sql1.append("on a.NODE_ID = temp.NODE_ID ");
                sql1.append("LEFT JOIN tb_supervise_task t on t.SUPERVISE_ID = temp.SUPERVISE_ID and T.SUPERVISE_NO = temp.SUPERVISE_NO ");
                sql1.append("LEFT JOIN plat_system_depart d on t.DEPART_ID = d.DEPART_ID ");
                sql1.append("where a.DEL_FLAG = '1' ");
                sql1.append("ORDER BY a.NODE_ID ASC ");
                List<Map<String, Object>> nodes = superviseClazzService.findBySql(sql1.toString(), new Object[]{id}, null);
                //判断节点状态
                for (Map<String, Object> node : nodes) {
                    String nodeId = (String) node.get("NODE_ID");
                    if (NODE_ID_1.equals(nodeId)) {
                        node.put("DEPART_NAME", supervise.get("DEPART_NAME"));
                        node.put("timeStatus", "完成");
                        node.put("feedbackStatus", '1');//已反馈
                    }
                    if (NODE_ID_2.equals(nodeId)) {
                        boolean flag = this.isOutDate(supervise, nodeId);
                        if (flag) {
                            if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) < 2) {
                                node.put("timeStatus", "未办理");
                            } else if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) == 2) {
                                node.put("timeStatus", "逾期");
                            } else if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) > 2) {
                                node.put("timeStatus", "完成");
                            }
                        } else {
                            if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) > 2) {
                                node.put("timeStatus", "完成");
                            } else if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) == 2) {
                                node.put("timeStatus", "办理中");
                            } else {
                                node.put("timeStatus", "未办理");
                            }
                        }
                        String recordIds = dao.getTableFieldValues("tb_supervise_confirm", "RECORD_ID", new String[]{"SUPERVISE_ID", "NODE_ID"},
                                new Object[]{supervise.get("RECORD_ID"), nodeId}, ",");
                        if (StringUtils.isNotEmpty(recordIds)) {
                            node.put("feedbackStatus", '1');//已确认
                        } else {
                            node.put("feedbackStatus", '0');//未确认
                        }
                    }
                    if (NODE_ID_3.equals(nodeId)) {
                        boolean flag = this.isOutDate(supervise, nodeId);
                        if (flag) {
                            if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) < 3) {
                                node.put("timeStatus", "未办理");
                            } else if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) == 3) {
                                node.put("timeStatus", "逾期");
                            } else if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) > 3) {
                                node.put("timeStatus", "完成");
                            }
                        } else {
                            if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) > 3) {
                                node.put("timeStatus", "完成");
                            } else if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) == 3) {
                                node.put("timeStatus", "办理中");
                            } else {
                                node.put("timeStatus", "未办理");
                            }
                        }
                        //判断节点有无反馈
                        String recordIds = dao.getTableFieldValues("tb_supervise_feedback", "RECORD_ID", new String[]{"SUPERVISE_ID", "NODE_ID"},
                                new Object[]{supervise.get("RECORD_ID"), nodeId}, ",");
                        if (StringUtils.isNotEmpty(recordIds)) {
                            node.put("feedbackStatus", '1');//已反馈
                        } else {
                            node.put("feedbackStatus", '0');//未反馈
                        }
                    }
                    if (NODE_ID_4.equals(nodeId)) {

                        boolean flag = this.isOutDate(supervise, nodeId);
                        if (flag) {
                            if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) < 4) {
                                node.put("timeStatus", "未办理");
                            } else if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) == 4) {
                                node.put("timeStatus", "逾期");
                            } else if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) > 4) {
                                node.put("timeStatus", "完成");
                            }
                        } else {
                            if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) > 4) {
                                node.put("timeStatus", "完成");
                            } else if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) == 4) {
                                node.put("timeStatus", "办理中");
                            } else {
                                node.put("timeStatus", "未办理");
                            }
                        }
                        //判断节点有无反馈
                        String recordIds = dao.getTableFieldValues("tb_supervise_feedback", "RECORD_ID", new String[]{"SUPERVISE_ID", "NODE_ID"},
                                new Object[]{supervise.get("RECORD_ID"), nodeId}, ",");
                        if (StringUtils.isNotEmpty(recordIds)) {
                            node.put("feedbackStatus", '1');//已反馈
                        } else {
                            node.put("feedbackStatus", '0');//未反馈
                        }
                    }
                    if (NODE_ID_5.equals(nodeId)) {

                        boolean flag = this.isOutDate(supervise, nodeId);
                        if (flag) {
                            if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) < 5) {
                                node.put("timeStatus", "未办理");
                            } else if (Integer.parseInt(String.valueOf(supervise.get("STATUS"))) == 9) {
                                node.put("timeStatus", "完成");
                            } else if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) == 5) {
                                node.put("timeStatus", "逾期");
                            }
                        } else {
                            if (Integer.parseInt(String.valueOf(supervise.get("STATUS"))) == 9) {
                                node.put("timeStatus", "完成");
                            } else if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) == 5) {
                                node.put("timeStatus", "办理中");
                            } else {
                                node.put("timeStatus", "未办理");
                            }
                        }


                        /*long intervalTime = PlatDateTimeUtil.getIntervalTime((String) supervise.get("CREATE_TIME"),
                                PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                        //判断落实反馈第一次的反馈时间
                        Map<String, Object> feedback = this.takerService.getFeedbackInfo(id, NODE_ID_4, "ASC");
                        if (feedback != null) {
                            intervalTime = PlatDateTimeUtil.getIntervalTime((String) supervise.get("CREATE_TIME"), (String) feedback.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                        }
                        if (intervalTime <= (Integer.parseInt(String.valueOf(supervise.get("HANDLE_LIMIT"))) * 24)) {
                            //办理中 完成 两种状态
                            if (Integer.parseInt(String.valueOf(supervise.get("STATUS"))) == 9) {
                                node.put("timeStatus", "完成");
                            } else if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) == 5) {
                                node.put("timeStatus", "办理中");
                            } else {
                                node.put("timeStatus", "未办理");
                            }
                        } else {
                            if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) < 5) {
                                node.put("timeStatus", "未办理");
                            } else if (Integer.parseInt(String.valueOf(supervise.get("CURRENT_NODE"))) == 5) {
                                node.put("timeStatus", "逾期");
                            } else if (Integer.parseInt(String.valueOf(supervise.get("STATUS"))) == 9) {
                                node.put("timeStatus", "完成");
                            }
                        }*/
                        //判断节点有无反馈
                        String recordIds = dao.getTableFieldValues("tb_supervise_feedback", "RECORD_ID", new String[]{"SUPERVISE_ID", "NODE_ID"},
                                new Object[]{supervise.get("RECORD_ID"), nodeId}, ",");
                        if (StringUtils.isNotEmpty(recordIds)) {
                            node.put("feedbackStatus", '1');//已反馈
                        } else {
                            node.put("feedbackStatus", '0');//未反馈
                        }
                    }
                }
                supervise.put("nodes", nodes);
            }
        }
        return list;
    }

    /**
     * 根据督办事项ID初始化督办事项的督办流程
     *
     * @param request
     * @return
     */
    @Override
    public List<Map<String, Object>> initSuperviseProgress(HttpServletRequest request) {
        List<Map<String, Object>> result = new ArrayList<>();
        String superviseId = request.getParameter("SUPERVISE_ID");
        Map<String, Object> supervise = dao.getRecord("tb_supervise", new String[]{"RECORD_ID"}, new Object[]{superviseId});
        //获取督办部门
        String dbDepartName = (String) dao.getRecord("plat_system_depart", new String[]{"DEPART_ID"}, new Object[]{supervise.get("DEPART_ID")}).get("DEPART_NAME");
        //获取立项部门、获取承办部门、获取审核节点
        List<Map<String, Object>> taskDeparts = dao.findTaskListBySuperviseId(superviseId);
        //获取审核节点
        List<Map<String, Object>> taskNodes = dao.findTaskNodeListBySuperviseId(superviseId);
        for (Map<String, Object> node : taskNodes) {
            if (NODE_ID_1.equals(node.get("NODE_ID"))) {
                Map<String, Object> progress = new HashMap<>();
                progress.put("NODE_ID", node.get("NODE_ID"));
                progress.put("SHORT_NAME", node.get("SHORT_NAME"));
                progress.put("VALUE", dbDepartName + " 发起");
                progress.put("date", node.get("CREATE_TIME"));
                progress.put("status", "1");//办结
                result.add(progress);
                if (NEED_APPROVE_FLAG.equals(node.get("NEED_APPROVE_FLAG"))) {
                    progress = new HashMap<>();
                    progress.put("temp", dbDepartName + " 审核");
                    result.add(progress);
                }
            }
            if (NODE_ID_2.equals(node.get("NODE_ID"))) {
                for (Map<String, Object> taskDepart : taskDeparts) {
                    String departName = (String) taskDepart.get("DEPART_NAME");
                    Map<String, Object> progress = new HashMap<>();
                    progress.put("NODE_ID", node.get("NODE_ID"));
                    progress.put("SHORT_NAME", node.get("SHORT_NAME"));
                    progress.put("VALUE", departName + "承办人 接收");
                    progress.put("date", node.get("CREATE_TIME"));
                    progress.put("status", "1");//办结
                    result.add(progress);
                }
            }
            if (NODE_ID_3.equals(node.get("NODE_ID"))) {
                for (Map<String, Object> taskDepart : taskDeparts) {
                    String departName = (String) taskDepart.get("DEPART_NAME");
                    Map<String, Object> progress = new HashMap<>();
                    progress.put("NODE_ID", node.get("NODE_ID"));
                    progress.put("SHORT_NAME", node.get("SHORT_NAME"));
                    progress.put("VALUE", departName + "承办人 办理");
                    progress.put("date", node.get("CREATE_TIME"));
                    if (Integer.parseInt(supervise.get("STATUS").toString()) <= 5) {
                        if (NODE_ID_3.equals(supervise.get("CURRENT_NODE"))) {
                            progress.put("status", "0");//在办
                        } else {
                            progress.put("status", "2");//未开始
                        }
                    } else {
                        progress.put("status", "1");//办结
                    }
                    //判断任务是否存在逾期，逾期几次
                    String recordIds = dao.getTableFieldValues("tb_supervise_feedback", "RECORD_ID", new String[]{"SUPERVISE_ID", "NODE_ID", "STATUS"},
                            new Object[]{supervise.get("RECORD_ID"), node.get("NODE_ID"), 2}, ",");
                    if (StringUtils.isNotEmpty(recordIds)) {
                        progress.put("outDateStatus", '1');//有逾期反馈
                        progress.put("outDateTimes", recordIds.split(",").length);
                    } else {
                        progress.put("outDateStatus", '0');//未逾期反馈
                    }
                    //判断任务是否存在驳回，驳回几次
                    recordIds = dao.getTableFieldValues("tb_supervise_reply", "RECORD_ID", new String[]{"SUPERVISE_ID", "NODE_ID", "AGREE"},
                            new Object[]{supervise.get("RECORD_ID"), node.get("NODE_ID"), 2}, ",");
                    if (StringUtils.isNotEmpty(recordIds)) {
                        progress.put("disagreeStatus", '1');//有驳回
                        progress.put("disagreeTimes", recordIds.split(",").length);
                        //返回历史驳回意见
                        List<Map<String, Object>> replyList = this.findReplyList(superviseId, (String) node.get("NODE_ID"), "2");
                        progress.put("disagreeList", replyList);
                    } else {
                        progress.put("disagreeStatus", '0');//无驳回
                    }
                    result.add(progress);
                    if (NEED_APPROVE_FLAG.equals(node.get("NEED_APPROVE_FLAG"))) {
                        progress = new HashMap<>();
                        progress.put("temp", departName + "主任 审核");
                        result.add(progress);
                    }
                }
            }
            if (NODE_ID_4.equals(node.get("NODE_ID"))) {
                for (Map<String, Object> taskDepart : taskDeparts) {
                    String departName = (String) taskDepart.get("DEPART_NAME");
                    Map<String, Object> progress = new HashMap<>();
                    progress.put("NODE_ID", node.get("NODE_ID"));
                    progress.put("SHORT_NAME", node.get("SHORT_NAME"));
                    progress.put("VALUE", departName + "承办人 落实");
                    progress.put("date", node.get("CREATE_TIME"));
                    if (Integer.parseInt(supervise.get("STATUS").toString()) <= 7) {
                        if (NODE_ID_4.equals(supervise.get("CURRENT_NODE"))) {
                            progress.put("status", "0");//在办
                        } else {
                            progress.put("status", "2");//未开始
                        }
                    } else {
                        progress.put("status", "1");//办结
                    }
                    //判断任务是否存在逾期，逾期几次
                    String recordIds = dao.getTableFieldValues("tb_supervise_feedback", "RECORD_ID", new String[]{"SUPERVISE_ID", "NODE_ID", "STATUS"},
                            new Object[]{supervise.get("RECORD_ID"), node.get("NODE_ID"), 2}, ",");
                    if (StringUtils.isNotEmpty(recordIds)) {
                        progress.put("outDateStatus", '1');//有逾期反馈
                        progress.put("outDateTimes", recordIds.split(",").length);
                    } else {
                        progress.put("outDateStatus", '0');//未逾期反馈
                    }
                    //判断任务是否存在驳回，驳回几次
                    recordIds = dao.getTableFieldValues("tb_supervise_reply", "RECORD_ID", new String[]{"SUPERVISE_ID", "NODE_ID", "AGREE"},
                            new Object[]{supervise.get("RECORD_ID"), node.get("NODE_ID"), 2}, ",");
                    if (StringUtils.isNotEmpty(recordIds)) {
                        progress.put("disagreeStatus", '1');//有驳回
                        progress.put("disagreeTimes", recordIds.split(",").length);
                        //返回历史驳回意见
                        List<Map<String, Object>> replyList = this.findReplyList(superviseId, (String) node.get("NODE_ID"), "2");
                        progress.put("disagreeList", replyList);
                    } else {
                        progress.put("disagreeStatus", '0');//无驳回
                    }
                    result.add(progress);
                    if (NEED_APPROVE_FLAG.equals(node.get("NEED_APPROVE_FLAG"))) {
                        progress = new HashMap<>();
                        progress.put("temp", departName + "主任 审核");
                        result.add(progress);
                    }
                }
            }
            if (NODE_ID_5.equals(node.get("NODE_ID"))) {
                for (Map<String, Object> taskDepart : taskDeparts) {
                    String departName = (String) taskDepart.get("DEPART_NAME");
                    Map<String, Object> progress = new HashMap<>();
                    progress.put("NODE_ID", node.get("NODE_ID"));
                    progress.put("SHORT_NAME", node.get("SHORT_NAME"));
                    progress.put("VALUE", departName + "承办人 办结");
                    progress.put("date", node.get("CREATE_TIME"));
                    if (Integer.parseInt(supervise.get("STATUS").toString()) <= 8) {
                        if (NODE_ID_5.equals(supervise.get("CURRENT_NODE"))) {
                            progress.put("status", "0");//在办
                        } else {
                            progress.put("status", "2");//未开始
                        }
                    } else {
                        progress.put("status", "1");//办结
                    }
                    //判断任务是否存在逾期，逾期几次
                    String recordIds = dao.getTableFieldValues("tb_supervise_feedback", "RECORD_ID", new String[]{"SUPERVISE_ID", "NODE_ID", "STATUS"},
                            new Object[]{supervise.get("RECORD_ID"), node.get("NODE_ID"), 2}, ",");
                    if (StringUtils.isNotEmpty(recordIds)) {
                        progress.put("outDateStatus", '1');//有逾期反馈
                        progress.put("outDateTimes", recordIds.split(",").length);
                    } else {
                        progress.put("outDateStatus", '0');//未逾期反馈
                    }
                    //判断任务是否存在驳回，驳回几次
                    recordIds = dao.getTableFieldValues("tb_supervise_reply", "RECORD_ID", new String[]{"SUPERVISE_ID", "NODE_ID", "AGREE"},
                            new Object[]{supervise.get("RECORD_ID"), node.get("NODE_ID"), 2}, ",");
                    if (StringUtils.isNotEmpty(recordIds)) {
                        progress.put("disagreeStatus", '1');//有驳回
                        progress.put("disagreeTimes", recordIds.split(",").length);
                        //返回历史驳回意见
                        List<Map<String, Object>> replyList = this.findReplyList(superviseId, (String) node.get("NODE_ID"), "2");
                        progress.put("disagreeList", replyList);
                    } else {
                        progress.put("disagreeStatus", '0');//无驳回
                    }
                    result.add(progress);
                    if (NEED_APPROVE_FLAG.equals(node.get("NEED_APPROVE_FLAG"))) {
                        progress = new HashMap<>();
                        progress.put("temp", departName + "主任 审核");
                        result.add(progress);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 更新督办信息
     *
     * @param params
     */
    @Override
    public void updateSupervise(Map<String, Object> params) {
        //更新督办任务状态
        Map<String, Object> supervise = dao.getRecord("tb_supervise", new String[]{"RECORD_ID"}, new Object[]{params.get("SUPERVISE_ID")});
        //根据当前承办人 更新承办部门任务状态
        String userId = (String) PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID");
        Map<String, Object> task = dao.getRecord("tb_supervise_task", new String[]{"SUPERVISE_ID", "USER_ID"}, new Object[]{params.get("SUPERVISE_ID"), userId});
        String nodeId = (String) params.get("NODE_ID");
        if (NODE_ID_3.equals(nodeId)) {//办理反馈
            supervise.put("STATUS", 5);
            task.put("STATUS", 5);
        }
        if (NODE_ID_4.equals(nodeId)) {//落实反馈
            supervise.put("STATUS", 7);
            task.put("STATUS", 7);
        }
        if (NODE_ID_5.equals(nodeId)) {//办结申请
            supervise.put("STATUS", 8);
            task.put("STATUS", 8);
        }
        dao.saveOrUpdate("tb_supervise", supervise, SysConstants.ID_GENERATOR_UUID, null);
        dao.saveOrUpdate("tb_supervise_task", task, SysConstants.ID_GENERATOR_UUID, null);
    }

    /**
     * 获取承办人指定节点的最近一次反馈记录，指定排序方式
     *
     * @param recordId
     * @param nodeId
     * @param orderType
     * @return
     */
    @Override
    public Map<String, Object> getFeedbackInfo(String recordId, String nodeId, String orderType) {
        String userId = (String) PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID");
        StringBuffer sql = new StringBuffer("select f.RECORD_ID,f.FEEDBACK_CONTENT,f.FILE_URL,f.FILE_NAME,f.REMARKS,f.CREATE_TIME,r.REPLY_CONTENT ");
        sql.append("from tb_supervise_feedback f ");
        sql.append("LEFT JOIN tb_supervise_reply r on f.RECORD_ID = r.FEEDBACK_ID ");
        sql.append("where f.SUPERVISE_ID = ? and f.NODE_ID = ? ");
        if (ORDER_BY_ASC.equals(orderType)) {
            sql.append("order by f.CREATE_TIME ASC limit 0,1 ");
        }
        if (ORDER_BY_DESC.equals(orderType)) {
            sql.append("order by f.CREATE_TIME DESC limit 0,1 ");
        }
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{recordId, nodeId}, null);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取督办事项指定节点的所有批复意见
     *
     * @param recordId 督办ID
     * @param nodeId   节点ID
     * @param agree    通过/未通过
     * @return
     */
    @Override
    public List<Map<String, Object>> findReplyList(String recordId, String nodeId, String agree) {
        StringBuffer sql = new StringBuffer("select r.RECORD_ID,r.REPLY_CONTENT,r.CREATE_TIME,r.REMARKS ");
        sql.append("from tb_supervise_reply r ");
        sql.append("where r.SUPERVISE_ID = ? and r.NODE_ID = ? and r.AGREE = ? order by r.CREATE_TIME desc ");
        return dao.findBySql(sql.toString(), new Object[]{recordId, nodeId, agree}, null);
    }

    /**
     * 督办流程节点联动右侧反馈内容
     * cjr
     *
     * @param request
     */
    @Override
    public Map<String, Object> loadTakerFeedbackInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String nodeId = request.getParameter("nodeId");
        String id = request.getParameter("id");
        //根据ID获取督办事项详情
        Map<String, Object> supervise = dao.getRecord("tb_supervise", new String[]{"RECORD_ID"}, new Object[]{id});
        //获取督办部门
        String dbDepartName = (String) dao.getRecord("plat_system_depart", new String[]{"DEPART_ID"}, new Object[]{supervise.get("DEPART_ID")}).get("DEPART_NAME");
        supervise.put("DEPART_NAME", dbDepartName);
        //根据节点ID 获取最近一次反馈内容
        Map<String, Object> feedback = this.getFeedbackInfo(id, nodeId, "DESC");
        result.put("feedback", feedback);
        long intervalTime = PlatDateTimeUtil.getIntervalTime((String) supervise.get("CREATE_TIME"),
                PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
        if (intervalTime <= 72) {
            long days = (72 - intervalTime) / 24;
            long hours = (72 - intervalTime) - days * 24;
            result.put("restTime1", "还剩" + days + "天" + hours + "小时");
        } else {
            result.put("restTime1", "已逾期");
        }
        //获取落实反馈的剩余期限
        intervalTime = PlatDateTimeUtil.getIntervalTime((String) supervise.get("CREATE_TIME"),
                PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
        if (NODE_ID_4.equals(nodeId)) {
            feedback = this.getFeedbackInfo(id, "3", "ASC");
        }
        if (NODE_ID_5.equals(nodeId)) {
            feedback = this.getFeedbackInfo(id, "4", "ASC");
        }
        if (feedback != null) {
            intervalTime = PlatDateTimeUtil.getIntervalTime((String) supervise.get("CREATE_TIME"),
                    (String) feedback.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
        }
        if (intervalTime <= Integer.parseInt(String.valueOf(supervise.get("HANDLE_LIMIT"))) * 24) {
            long days = (Integer.parseInt(String.valueOf(supervise.get("HANDLE_LIMIT"))) * 24 - intervalTime) / 24;
            long hours = (Integer.parseInt(String.valueOf(supervise.get("HANDLE_LIMIT"))) * 24 - intervalTime) - days * 24;
            result.put("restTime2", "还剩" + String.valueOf(days + "天" + hours + "小时"));//获取落实反馈的剩余期限
            result.put("restTime3", "还剩" + String.valueOf(days + "天" + hours + "小时"));//获取办结反馈的剩余期限
        } else {
            result.put("restTime2", "已逾期");
            result.put("restTime3", "已逾期");
        }

        //获取督办确认内容
        Map<String, Object> confirm = takerChargeService.getRecord("tb_supervise_confirm", new String[]{"SUPERVISE_ID", "NODE_ID"}, new Object[]{supervise.get("RECORD_ID"), nodeId});
        result.put("confirm", confirm);
        //判断督办确认时限
        intervalTime = PlatDateTimeUtil.getIntervalTime((String) supervise.get("CREATE_TIME"),
                PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
        if (intervalTime <= 24) {
            long days = (24 - intervalTime) / 24;
            long hours = (24 - intervalTime) - days * 24;
            result.put("restTime4", "还剩" + String.valueOf(days + "天" + hours + "小时"));
        } else {
            result.put("restTime4", "已逾期");
        }

        return result;
    }

    /**
     * 判断当前督办事项指定节点是否存在逾期
     *
     * @param supervise
     * @return
     */
    @Override
    public boolean isOutDate(Map<String, Object> supervise, String nodeId) {
        String superviseId = (String) supervise.get("RECORD_ID");
        String createTime = (String) supervise.get("CREATE_TIME");//创建时间
        int HANDLE_LIMIT = (Integer) supervise.get("HANDLE_LIMIT");//办理时限
        Map<String, Object> confirm = dao.getRecord("tb_supervise_confirm", new String[]{"SUPERVISE_ID", "NODE_ID"}, new Object[]{superviseId, NODE_ID_2});//督办确认
        Map<String, Object> feedback1 = this.getFeedbackInfo(superviseId, NODE_ID_3, "ASC");
        Map<String, Object> feedback2 = this.getFeedbackInfo(superviseId, NODE_ID_4, "ASC");
        Map<String, Object> feedback3 = this.getFeedbackInfo(superviseId, NODE_ID_5, "ASC");
        long intervalTime1 = 0L;
        long intervalTime2 = 0L;
        long intervalTime3 = 0L;
        long intervalTime = 0L;//督办确认
        if (NODE_ID_2.equals(nodeId)) {//督办确认
            if (confirm == null) {
                intervalTime = PlatDateTimeUtil.getIntervalTime(createTime, PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                if (intervalTime <= 24) {
                    //未超期未反馈
                } else {
                    //已超期未反馈
                    return true;
                }
            } else {
                intervalTime = PlatDateTimeUtil.getIntervalTime(createTime, (String) confirm.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                if (intervalTime <= 24) {
                    //未超期已反馈
                } else {
                    //已超期已反馈
                    return true;
                }
            }
        }
        if (NODE_ID_3.equals(nodeId)) {
            if (feedback1 == null) {
                if (confirm == null) {
                    intervalTime1 = PlatDateTimeUtil.getIntervalTime(createTime, PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                } else {
                    intervalTime1 = PlatDateTimeUtil.getIntervalTime((String) confirm.get("CREATE_TIME"), PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                }
                if (intervalTime1 <= (HANDLE_LIMIT * 24 - intervalTime)) {
                    //未超期未反馈
                } else {
                    //已超期未反馈
                    return true;
                }
            } else {
                intervalTime1 = PlatDateTimeUtil.getIntervalTime(createTime, (String) feedback1.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                if (intervalTime1 <= (HANDLE_LIMIT * 24 - intervalTime)) {
                    //未超期已反馈
                } else {
                    //已超期已反馈
                    return true;
                }
            }
        }
        /*if (NODE_ID_4.equals(nodeId)) {
            if (feedback1 == null) {
                intervalTime1 = PlatDateTimeUtil.getIntervalTime(createTime, PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
            } else {
                intervalTime1 = PlatDateTimeUtil.getIntervalTime(createTime, (String) feedback1.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
            }
            if (feedback2 == null) {
                if (feedback1 == null) {
                    intervalTime2 = PlatDateTimeUtil.getIntervalTime(createTime, PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                } else {
                    intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) feedback1.get("CREATE_TIME"), PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                }
                //当前办理期限为 时限*24 - 第一次反馈耗费的时间
                //intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) feedback1.get("CREATE_TIME"), PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                if (intervalTime2 <= (HANDLE_LIMIT * 24 - intervalTime1)) {
                    //未超期
                } else {
                    //已超期
                    return true;
                }
            } else {
                intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) feedback1.get("CREATE_TIME"), (String) feedback2.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                if (intervalTime2 <= (HANDLE_LIMIT * 24 - intervalTime1)) {
                    //未超期
                } else {
                    //已超期
                    return true;
                }
            }
        }*/
        if (NODE_ID_5.equals(nodeId)) {
            if (feedback1 == null) {
                intervalTime1 = PlatDateTimeUtil.getIntervalTime(createTime, PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
            } else {
                intervalTime1 = PlatDateTimeUtil.getIntervalTime(createTime, (String) feedback1.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
            }
            /*if (feedback2 == null) {
                if (feedback1 == null) {
                    intervalTime2 = PlatDateTimeUtil.getIntervalTime(createTime, PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                } else {
                    intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) feedback1.get("CREATE_TIME"), PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                }
            } else {
                intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) feedback1.get("CREATE_TIME"), (String) feedback2.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
            }*/
            if (feedback3 == null) {
                if (feedback1 == null) {
                    intervalTime3 = PlatDateTimeUtil.getIntervalTime(createTime, PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                } else {
                    intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) feedback1.get("CREATE_TIME"), PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                }
                if (intervalTime3 <= (HANDLE_LIMIT * 24 - intervalTime2 - intervalTime1)) {
                    //未超期
                } else {
                    //已超期
                    return true;
                }
            } else {
                intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) feedback1.get("CREATE_TIME"), (String) feedback3.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                if (intervalTime3 <= (HANDLE_LIMIT * 24 - intervalTime2 - intervalTime1)) {
                    //未超期
                } else {
                    //已超期
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 承办部门负责人审批
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> takerChargeApprove(HttpServletRequest request) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        String superviseId = (String) params.get("SUPERVISE_ID");
        //获取督办事项信息、 获取部门督办任务信息
        //根据当前登录用户部门和督办编号获取督办任务ID
        Map<String, Object> task = dao.getRecord("tb_supervise_task", new String[]{"SUPERVISE_ID", "DEPART_ID"}, new Object[]{superviseId, user.get("DEPART_ID")});
        Map<String, Object> temp = dao.getRecord("tb_approve_record", new String[]{"SUPERVISE_ID", "APPROVE_NODE_ID"}, new Object[]{superviseId, params.get("NODE_ID")});
        if (temp == null) {
            temp = new HashMap<>();
        }
        temp.put("SUPERVISE_ID", superviseId);
        if (task != null) {
            temp.put("SUPERVISE_TASK_ID", task.get("RECORD_ID"));
        }
        temp.put("FEEDBACK_ID", params.get("FEEDBACK_ID"));
        temp.put("APPROVE_NODE_ID", params.get("NODE_ID"));
        temp.put("APPROVE_USER_COMPANY", user.get("SYSUSER_COMPANYID"));
        temp.put("APPROVE_USER_DEPART", user.get("DEPART_ID"));
        temp.put("APPROVE_USER_ID", PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID"));
        temp.put("APPROVE_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        temp.put("APPROVE_RESULT", params.get("APPROVE_RESULT"));//1通过 2 不通过
        temp = dao.saveOrUpdate("tb_approve_record", temp, SysConstants.ID_GENERATOR_UUID, null);
        //更新督办记录状态
        Map<String, Object> supervise = dao.getRecord("tb_supervise", new String[]{"RECORD_ID"}, new Object[]{superviseId});
        if (supervise != null) {
            if (NODE_ID_3.equals(params.get("NODE_ID"))) {
                if ("1".equals(temp.get("APPROVE_RESULT"))) {
                    //同意
                    //supervise.put("CURRENT_NODE", "5");
                    //supervise.put("STATUS", 8);
                    //task.put("STATUS", 8);
                } else {
                    supervise.put("CURRENT_NODE", "3");
                    supervise.put("STATUS", 5);
                    task.put("STATUS", 5);
                }
            }
            if (NODE_ID_5.equals(params.get("NODE_ID"))) {
                supervise.put("CURRENT_NODE", "5");
                supervise.put("STATUS", 8);
                task.put("STATUS", 8);
            }
            supervise = dao.saveOrUpdate("tb_supervise", supervise, SysConstants.ID_GENERATOR_UUID, null);
            dao.saveOrUpdate("tb_supervise_task", task, SysConstants.ID_GENERATOR_UUID, null);
        }
        //初始化下一节点任务数据
        if (NODE_ID_3.equals(params.get("NODE_ID")) && "1".equals(temp.get("APPROVE_RESULT"))) {
            Map<String, Object> nodeMap = dao.getRecord("tb_supervise_node_info", new String[]{"SUPERVISE_ID", "SUPERVISE_NO", "NODE_ID"}, new Object[]{superviseId, supervise.get("SUPERVISE_NO"), supervise.get("CURRENT_NODE")});
            if (nodeMap == null) {
                nodeMap = new HashMap<>();
            }
            nodeMap.put("SUPERVISE_ID", superviseId);
            nodeMap.put("SUPERVISE_NO", supervise.get("SUPERVISE_NO"));
            nodeMap.put("NODE_ID", supervise.get("CURRENT_NODE"));
            if (NODE_ID_3.equals(params.get("NODE_ID"))) {
                nodeMap.put("NODE_NAME", "办理反馈");
            }
            nodeMap.put("CREATE_BY", user.get("SYSUSER_ID"));
            nodeMap.put("CREATE_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            dao.saveOrUpdate("tb_supervise_node_info", nodeMap, SysConstants.ID_GENERATOR_UUID, null);
        }
        return temp;
    }
}
