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
import com.housoo.platform.supervise.dao.TakerDao;
import com.housoo.platform.supervise.service.SuperviseApproveService;
import com.housoo.platform.supervise.service.SuperviseClazzService;
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
@Service("takerService")
public class TakerServiceImpl extends BaseServiceImpl implements TakerService {

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
    private TakerDao dao;

    /**
     * 部门管理Service
     */
    @Resource
    private DepartService departService;
    /**
     * SuperviseService
     */
    @Resource
    private TakerService takerService;
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
        String userId = (String) PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID");
        StringBuffer sql = new StringBuffer("SELECT s.RECORD_ID,s.SUPERVISE_NO,s.SUPERVISE_CLAZZ_ID,s.SUPERVISE_SOURCE,s.SUPERVISE_TYPE,s.SUPERVISE_CLAZZ,s.SUPERVISE_ITEM_ID,SUPERVISE_ITEM,s.TITLE,s.KEYWORDS,d.DEPART_NAME,s.CREATE_TIME,s.CURRENT_NODE,s.HANDLE_LIMIT,s.STATUS ");
        sql.append("FROM tb_supervise s ");
        sql.append("LEFT JOIN tb_supervise_task t on s.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
        sql.append("LEFT JOIN plat_system_depart d on s.DEPART_ID = d.DEPART_ID ");
        sql.append("where t.USER_ID = ? AND YEAR (s.CREATE_TIME) = YEAR (NOW()) ");
        sql.append("AND s.SUPERVISE_CLAZZ_ID = ? and s.STATUS IN ");
        sql.append(PlatStringUtil.getSqlInCondition(request.getParameter("status")));
        sql.append("ORDER BY s.CREATE_TIME DESC ");
        List<Map<String, Object>> list = superviseClazzService.findBySql(sql.toString(), new Object[]{userId, request.getParameter("clazzId")}, null);
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
     * 获取承办人首页数值型统计数据
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> getTakerIndexNumericData(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        //获取当前登录人的部门ID
        String departId = (String) user.get("DEPART_ID");
        String ROLECODES = (String) user.get("ROLECODES");
        if (StringUtils.isNotEmpty(departId) && StringUtils.isNotEmpty(ROLECODES) && ROLECODES.contains("deptLeader")) {
            //获取当前用户部门下的承办角色用户
            StringBuffer sql = new StringBuffer("select GROUP_CONCAT(u.SYSUSER_ID) SYSUSER_ID ");
            sql.append("from plat_system_sysuser u ");
            sql.append("LEFT JOIN plat_system_sysuserrole ur on u.SYSUSER_ID = ur.SYSUSER_ID ");
            sql.append("LEFT JOIN plat_system_role r on ur.ROLE_ID = r.ROLE_ID ");
            sql.append("where r.ROLE_CODE = 'commonuser' and u.SYSUSER_DEPARTID = ? ");
            Map<String, Object> users = dao.getBySql(sql.toString(), new Object[]{departId});
            if (users == null) {
                params.put("SYSUSER_ID", user.get("SYSUSER_ID"));
            } else if (StringUtils.isNotEmpty((String) users.get("SYSUSER_ID"))) {
                params.put("SYSUSER_ID", users.get("SYSUSER_ID"));
            }
        } else {
            params.put("SYSUSER_ID", user.get("SYSUSER_ID"));
        }
        String type = (String) params.get("type");
        if (SEARCHTYPE_YEAR.equals(type)) {
            result = dao.getTakerIndexNumericDataByYear(params);
        }
        if (SEARCHTYPE_QUARTER.equals(type)) {
            result = dao.getTakerIndexNumericDataByQuarter(params);
        }
        if (SEARCHTYPE_MONTH.equals(type)) {
            result = dao.getTakerIndexNumericDataByMonth(params);
        }
        if (SEARCHTYPE_DAYS.equals(type)) {
            result = dao.getTakerIndexNumericDataByDays(params);
        }
        if (StringUtils.isEmpty(type)) {
            result = dao.getTakerIndexNumericDataByRangeTime(params);
        }
        //统计逾期
        List<Map<String, Object>> outDateData = this.getTakerIndexOutDateNumericData(params);
        result.put("outDateData", outDateData);
        return result;
    }

    /**
     * 获取承办人逾期的督办数量
     *
     * @param params
     * @return
     */
    private List<Map<String, Object>> getTakerIndexOutDateNumericData(Map<String, Object> params) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        List<Map<String, Object>> superviseList = new ArrayList<>();
        String type = (String) params.get("type");
        List<Map<String, Object>> clazzs = superviseClazzService.getAllSuperviseClazz(null);
        if (clazzs != null) {
            for (Map<String, Object> clazz : clazzs) {
                int count = 0;
                String clazzId = (String) clazz.get("VALUE");
                //获取承办人承办的各个类型的所有督办事项
                StringBuffer sql = new StringBuffer("select s.RECORD_ID,s.CREATE_TIME,s.HANDLE_LIMIT,s.STATUS ");
                sql.append("from tb_supervise s ");
                sql.append("LEFT JOIN tb_supervise_task t on s.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
                sql.append("where t.USER_ID = ? AND s.SUPERVISE_CLAZZ_ID= ? ");
                sql.append("AND s.STATUS NOT IN (0) ");
                if (SEARCHTYPE_YEAR.equals(type)) {
                    sql.append("AND YEAR (s.CREATE_TIME) = YEAR (NOW()) ");
                }
                if (SEARCHTYPE_QUARTER.equals(type)) {
                    sql.append("AND YEAR (s.CREATE_TIME) = YEAR (NOW()) AND QUARTER(s.CREATE_TIME) = QUARTER(NOW()) ");
                }
                if (SEARCHTYPE_MONTH.equals(type)) {
                    sql.append("AND YEAR (s.CREATE_TIME) = YEAR (NOW()) AND MONTH(s.CREATE_TIME) = MONTH(NOW()) ");
                }
                if (SEARCHTYPE_DAYS.equals(type)) {
                    sql.append("AND YEAR (s.CREATE_TIME) = YEAR (NOW()) AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(s.CREATE_TIME) ");
                }
                if (StringUtils.isEmpty(type) && StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
                    sql.append("AND date_format(s.CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(s.CREATE_TIME,'%Y-%m-%d') <= ? ");
                    superviseList = dao.findBySql(sql.toString(), new Object[]{params.get("SYSUSER_ID"), clazzId, params.get("startDate"), params.get("endDate")}, null);
                } else {
                    superviseList = dao.findBySql(sql.toString(), new Object[]{params.get("SYSUSER_ID"), clazzId}, null);
                }
                if (superviseList.size() > 0) {
                    for (Map<String, Object> supervise : superviseList) {
                        boolean flag = this.isOutDate(supervise);
                        if (flag) {
                            count++;
                        }
                    }
                }
                clazz.put("count", count);
            }
        }
        return clazzs;
    }

    /**
     * 20200511 新增
     * 获取承办人首页饼图统计数据
     *
     * @param request
     * @return
     */
    @Override
    public List<Map<String, Object>> getTakerIndexPieData(HttpServletRequest request) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        //获取当前登录人的部门ID
        String departId = (String) user.get("DEPART_ID");
        String ROLECODES = (String) user.get("ROLECODES");
        if (StringUtils.isNotEmpty(departId) && StringUtils.isNotEmpty(ROLECODES) && ROLECODES.contains("deptLeader")) {
            //获取当前用户所负责部门下的的承办角色用户
            StringBuffer sql = new StringBuffer("select GROUP_CONCAT(u.SYSUSER_ID) SYSUSER_ID ");
            sql.append("from plat_system_sysuser u ");
            sql.append("LEFT JOIN plat_system_sysuserrole ur on u.SYSUSER_ID = ur.SYSUSER_ID ");
            sql.append("LEFT JOIN plat_system_role r on ur.ROLE_ID = r.ROLE_ID ");
            sql.append("where r.ROLE_CODE = 'commonuser' and u.SYSUSER_DEPARTID = ?  ");
            Map<String, Object> users = dao.getBySql(sql.toString(), new Object[]{departId});
            if (users == null) {
                params.put("SYSUSER_ID", user.get("SYSUSER_ID"));
            } else if (StringUtils.isNotEmpty((String) users.get("SYSUSER_ID"))) {
                params.put("SYSUSER_ID", users.get("SYSUSER_ID"));
            }
        } else {
            params.put("SYSUSER_ID", user.get("SYSUSER_ID"));
        }
        String type = (String) params.get("type");
        if (SEARCHTYPE_YEAR.equals(type)) {
            result = dao.getTakerIndexPieDataByYear(params);
        }
        if (SEARCHTYPE_QUARTER.equals(type)) {
            result = dao.getTakerIndexPieDataByQuarter(params);
        }
        if (SEARCHTYPE_MONTH.equals(type)) {
            result = dao.getTakerIndexPieDataByMonth(params);
        }
        if (SEARCHTYPE_DAYS.equals(type)) {
            result = dao.getTakerIndexPieDataByDays(params);
        }
        if (StringUtils.isEmpty(type)) {
            result = dao.getTakerIndexPieDataByRangeTime(params);
        }
        return result;
    }

    /**
     * 获取承办人首页柱图统计数据
     *
     * @param request
     * @return
     */
    @Override
    public List<Map<String, Object>> findTakerIndexChartData(HttpServletRequest request) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        String userId = (String) user.get("SYSUSER_ID");
        //获取当前登录人的部门ID
        String departId = (String) user.get("DEPART_ID");
        String ROLECODES = (String) user.get("ROLECODES");
        if (StringUtils.isNotEmpty(departId) && StringUtils.isNotEmpty(ROLECODES) && ROLECODES.contains("deptLeader")) {
            //获取当前用户部门下的承办角色用户
            StringBuffer sql = new StringBuffer("select GROUP_CONCAT(u.SYSUSER_ID) SYSUSER_ID ");
            sql.append("from plat_system_sysuser u ");
            sql.append("LEFT JOIN plat_system_sysuserrole ur on u.SYSUSER_ID = ur.SYSUSER_ID ");
            sql.append("LEFT JOIN plat_system_role r on ur.ROLE_ID = r.ROLE_ID ");
            sql.append("where r.ROLE_CODE = 'commonuser' and u.SYSUSER_DEPARTID = ? ");
            Map<String, Object> users = dao.getBySql(sql.toString(), new Object[]{departId});
            if (users != null && StringUtils.isNotEmpty((String) users.get("SYSUSER_ID"))) {
                userId = (String) users.get("SYSUSER_ID");
            }
        }
        StringBuffer sql = new StringBuffer("");
        List<Map<String, Object>> clazzs = superviseClazzService.getAllSuperviseClazz(null);
        if (clazzs != null) {
            for (Map<String, Object> clazz : clazzs) {
                String clazzId = (String) clazz.get("VALUE");
                String status = request.getParameter("status");
                //1.获取承办人承办的所有督办事项
                sql = new StringBuffer("select s.RECORD_ID,s.CREATE_TIME,s.HANDLE_LIMIT,s.STATUS ");
                sql.append("from tb_supervise s ");
                sql.append("LEFT JOIN tb_supervise_task t on s.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
                sql.append("where t.USER_ID IN ");
                sql.append(PlatStringUtil.getSqlInCondition(userId));
                sql.append(" AND s.SUPERVISE_CLAZZ_ID = ? AND YEAR (s.CREATE_TIME) = YEAR (NOW()) ");
                sql.append("AND s.STATUS IN ");
                sql.append(PlatStringUtil.getSqlInCondition(status));
                List<Map<String, Object>> superviseList = dao.findBySql(sql.toString(), new Object[]{clazzId}, null);
                //2.判断各督办事项的各个节点是否存在逾期反馈
                int all = 0, ing = 0, finish = 0;
                int all1 = 0, ing1 = 0, finish1 = 0;
                if (!superviseList.isEmpty()) {
                    for (Map<String, Object> supervise : superviseList) {
                        boolean flag = this.isOutDate(supervise);
                        if (flag) {
                            all1++;
                            if ("9".equals(String.valueOf(supervise.get("STATUS")))) {
                                finish1++;
                            } else {
                                ing1++;
                            }
                        } else {
                            all++;
                            if ("9".equals(String.valueOf(supervise.get("STATUS")))) {
                                finish++;
                            } else {
                                ing++;
                            }
                        }
                    }
                }
                Map<String, Object> temp = new HashMap<>();
                temp.put("ALLSUM", all);
                temp.put("FINISHSUM", finish);
                temp.put("INGSUM", ing);
                clazz.put("normalData", temp);
                temp = new HashMap<>();
                temp.put("ALLSUM", all1);
                temp.put("FINISHSUM", finish1);
                temp.put("INGSUM", ing1);
                clazz.put("outDateData", temp);
            }
        }
        return clazzs;
    }

    /**
     * 获取承办人首页督办列表数据
     *
     * @param request
     * @return
     */
    @Override
    public List<Map<String, Object>> findTakerIndexTableData(HttpServletRequest request) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        String userId = (String) user.get("SYSUSER_ID");
        //获取当前登录人的部门ID
        String departId = (String) user.get("DEPART_ID");
        String ROLECODES = (String) user.get("ROLECODES");
        if (StringUtils.isNotEmpty(departId) && StringUtils.isNotEmpty(ROLECODES) && ROLECODES.contains("deptLeader")) {
            //获取当前用户部门下的承办角色用户
            StringBuffer sql = new StringBuffer("select GROUP_CONCAT(u.SYSUSER_ID) SYSUSER_ID ");
            sql.append("from plat_system_sysuser u ");
            sql.append("LEFT JOIN plat_system_sysuserrole ur on u.SYSUSER_ID = ur.SYSUSER_ID ");
            sql.append("LEFT JOIN plat_system_role r on ur.ROLE_ID = r.ROLE_ID ");
            sql.append("where r.ROLE_CODE = 'commonuser' and u.SYSUSER_DEPARTID = ? ");
            Map<String, Object> users = dao.getBySql(sql.toString(), new Object[]{departId});
            if (users != null && StringUtils.isNotEmpty((String) users.get("SYSUSER_ID"))) {
                userId = (String) users.get("SYSUSER_ID");
            }
        }
        //1.根据状态字典值
        StringBuffer sql = new StringBuffer("SELECT a.RECORD_ID,a.NODE_NAME,a.NODE_ID ");
        sql.append("FROM tb_supervise_approve a where a.DEL_FLAG = '1' ");
        sql.append("ORDER BY a.NODE_ID ASC ");
        List<Map<String, Object>> nodeList = superviseClazzService.findBySql(sql.toString(), null, null);
        if (nodeList != null) {
            for (Map<String, Object> node : nodeList) {
                String nodeId = (String) node.get("NODE_ID");
                List<Map<String, Object>> clazzs = superviseClazzService.getAllSuperviseClazz(null);
                if (clazzs != null) {
                    for (Map<String, Object> clazz : clazzs) {
                        String clazzId = (String) clazz.get("VALUE");
                        sql = new StringBuffer("select COUNT(*) COUNT ");
                        sql.append("from tb_supervise s ");
                        sql.append("LEFT JOIN tb_supervise_task t on s.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
                        sql.append("WHERE S.STATUS NOT IN (0,10) and t.USER_ID IN ");
                        sql.append(PlatStringUtil.getSqlInCondition(userId));
                        sql.append(" and YEAR(s.CREATE_TIME) = YEAR(NOW()) and s.CURRENT_NODE = ? and s.SUPERVISE_CLAZZ_ID = ? ");
                        Map<String, Object> temp = dao.getBySql(sql.toString(), new Object[]{nodeId, clazzId});
                        if (temp == null) {
                            clazz.put("count", 0);
                        } else {
                            clazz.put("count", temp.get("COUNT"));
                        }
                    }
                }
                node.put("clazzs", clazzs);
            }
        }
        return nodeList;
    }

    /**
     * 获取承办人首页逾期统计数据
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> findTakerIndexOutDateData(HttpServletRequest request) {
        int dbfk = 0, blfk = 0, lsfk = 0, bjfk = 0, dbqr = 0;
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        String userId = (String) user.get("SYSUSER_ID");
        //获取当前登录人的部门ID
        String departId = (String) user.get("DEPART_ID");
        String ROLECODES = (String) user.get("ROLECODES");
        if (StringUtils.isNotEmpty(departId) && StringUtils.isNotEmpty(ROLECODES) && ROLECODES.contains("deptLeader")) {
            //获取当前用户部门下的承办角色用户
            StringBuffer sql = new StringBuffer("select GROUP_CONCAT(u.SYSUSER_ID) SYSUSER_ID ");
            sql.append("from plat_system_sysuser u ");
            sql.append("LEFT JOIN plat_system_sysuserrole ur on u.SYSUSER_ID = ur.SYSUSER_ID ");
            sql.append("LEFT JOIN plat_system_role r on ur.ROLE_ID = r.ROLE_ID ");
            sql.append("where r.ROLE_CODE = 'commonuser' and u.SYSUSER_DEPARTID = ? ");
            Map<String, Object> users = dao.getBySql(sql.toString(), new Object[]{departId});
            if (users != null && StringUtils.isNotEmpty((String) users.get("SYSUSER_ID"))) {
                userId = (String) users.get("SYSUSER_ID");
            }
        }
        //1.获取承办人承办的所有督办事项
        StringBuffer sql = new StringBuffer("select s.RECORD_ID,s.CREATE_TIME,s.HANDLE_LIMIT ");
        sql.append("from tb_supervise s ");
        sql.append("LEFT JOIN tb_supervise_task t on s.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
        sql.append("where t.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition(userId));
        sql.append(" AND s.STATUS NOT IN (0) AND YEAR (s.CREATE_TIME) = YEAR (NOW()) ");
        List<Map<String, Object>> superviseList = dao.findBySql(sql.toString(), new Object[]{}, null);
        //2.判断各督办事项的各个节点是否存在逾期反馈
        if (!superviseList.isEmpty()) {
            for (Map<String, Object> supervise : superviseList) {
                String superviseId = (String) supervise.get("RECORD_ID");
                String createTime = (String) supervise.get("CREATE_TIME");//创建时间
                int HANDLE_LIMIT = (Integer) supervise.get("HANDLE_LIMIT");//办理时限

                Map<String, Object> confirm = dao.getRecord("tb_supervise_confirm", new String[]{"SUPERVISE_ID", "NODE_ID"}, new Object[]{superviseId, NODE_ID_2});//督办确认
                long intervalTime = 0L;//督办确认
                if (confirm == null) {
                    intervalTime = PlatDateTimeUtil.getIntervalTime(createTime, PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                    if (intervalTime <= 24) {
                        //未超期
                    } else {
                        //已超期
                        dbqr++;
                    }
                } else {
                    intervalTime = PlatDateTimeUtil.getIntervalTime(createTime, (String) confirm.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                    if (intervalTime <= 24) {
                        //未超期
                    } else {
                        //已超期已反馈
                        dbqr++;
                    }
                }

                //获取办理反馈节点的第一次反馈时间
                Map<String, Object> feedback1 = this.getFeedbackInfo(superviseId, "3", "ASC");
                long intervalTime1 = 0L;
                //未反馈
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
                        blfk++;
                    }
                } else {
                    //已反馈
                    intervalTime1 = PlatDateTimeUtil.getIntervalTime(createTime, (String) feedback1.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                    if (intervalTime1 <= (HANDLE_LIMIT * 24 - intervalTime)) {
                        //未超期已反馈
                    } else {
                        //已超期已反馈
                        blfk++;
                    }
                }

                //获取落实反馈节点的第一次反馈时间
                /*Map<String, Object> feedback2 = this.getFeedbackInfo(superviseId, "4", "ASC");
                long intervalTime2 = 0L;
                //未反馈
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
                        lsfk++;
                    }
                } else {
                    //已反馈
                    intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) feedback1.get("CREATE_TIME"), (String) feedback2.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                    if (intervalTime2 <= (HANDLE_LIMIT * 24 - intervalTime1)) {
                        //未超期
                    } else {
                        //已超期反馈
                        lsfk++;
                    }
                }*/

                //获取落实反馈节点的第一次反馈时间
                Map<String, Object> feedback3 = this.getFeedbackInfo(superviseId, "5", "ASC");
                long intervalTime3 = 0L;
                //未反馈
                if (feedback3 == null) {
                    if (feedback1 == null) {
                        intervalTime3 = PlatDateTimeUtil.getIntervalTime(createTime, PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                    } else {
                        intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) feedback1.get("CREATE_TIME"), PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                    }
                    //当前办理期限为 时限*24 - 第二次反馈耗费的时间

                    if (intervalTime3 <= (HANDLE_LIMIT * 24 - intervalTime1)) {
                        //未超期
                    } else {
                        //已超期
                        bjfk++;
                    }
                } else {
                    //已反馈
                    intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) feedback1.get("CREATE_TIME"), (String) feedback3.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                    if (intervalTime3 <= (HANDLE_LIMIT * 24 - intervalTime1)) {
                        //未超期
                    } else {
                        //已超期反馈
                        bjfk++;
                    }
                }

            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("dbfk", dbfk);
        result.put("blfk", blfk);
        result.put("lsfk", lsfk);
        result.put("bjfk", bjfk);
        result.put("dbqr", dbqr);
        return result;
    }

    /**
     * 承办人提交反馈
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> takerHandleFeedback(HttpServletRequest request) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        String superviseId = (String) params.get("SUPERVISE_ID");
        //获取督办事项信息、 获取部门督办任务信息
        Map<String, Object> temp = new HashMap<>();
        temp.put("SUPERVISE_ID", superviseId);
        //根据当前登录用户部门和督办编号获取督办任务ID
        Map<String, Object> task = dao.getRecord("tb_supervise_task", new String[]{"SUPERVISE_ID", "USER_ID"}, new Object[]{superviseId, user.get("SYSUSER_ID")});
        if (task != null) {
            temp.put("SUPERVISE_TASK_ID", task.get("RECORD_ID"));
        }
        temp.put("FEEDBACK_CONTENT", params.get("FEEDBACK_CONTENT"));
        temp.put("FILE_URL", params.get("FILE_URL"));
        temp.put("FILE_NAME", params.get("FILE_NAME"));
        temp.put("NODE_ID", params.get("NODE_ID"));
        temp.put("CREATE_BY", PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID"));
        temp.put("CREATE_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        temp.put("REMARKS", params.get("REMARKS"));
        temp.put("DEL_FLAG", 1);
        temp.put("STATUS", params.get("STATUS"));
        temp = dao.saveOrUpdate("tb_supervise_feedback", temp, SysConstants.ID_GENERATOR_UUID, null);
        //更新督办记录状态
        Map<String, Object> supervise = dao.getRecord("tb_supervise", new String[]{"RECORD_ID"}, new Object[]{superviseId});
        if (supervise != null) {
            if ("5".equals(supervise.get("STATUS").toString())) {//办理反馈
                supervise.put("STATUS", 4);//办理反馈待负责人审批
            }
            if ("8".equals(supervise.get("STATUS").toString())) {//办结反馈
                supervise.put("STATUS", 6);//办结反馈待负责人审批
            }
            dao.saveOrUpdate("tb_supervise", supervise, SysConstants.ID_GENERATOR_UUID, null);
        }
        return temp;
    }

    /**
     * 获取承办人待办结的任务列表
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map<String, Object>> findTakerSupEndList(SqlFilter sqlFilter) {
        String userId = (String) PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID");
        String keywords = sqlFilter.getRequest().getParameter("KEYWORDS");
        String title = sqlFilter.getRequest().getParameter("TITLE");
        String status = sqlFilter.getRequest().getParameter("status");
        StringBuffer sql = new StringBuffer("SELECT s.RECORD_ID,s.TITLE,s.SUPERVISE_NO,s.SUPERVISE_CLAZZ_ID,s.SUPERVISE_SOURCE,s.SUPERVISE_TYPE,s.SUPERVISE_CLAZZ,s.SUPERVISE_ITEM_ID,SUPERVISE_ITEM,s.KEYWORDS,d.DEPART_NAME,s.STATUS ");
        sql.append("FROM tb_supervise s ");
        sql.append("LEFT JOIN tb_supervise_task t on s.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
        sql.append("LEFT JOIN plat_system_depart d on s.DEPART_ID = d.DEPART_ID ");
        sql.append("where t.USER_ID = ? AND YEAR (s.CREATE_TIME) = YEAR (NOW()) ");
        sql.append("AND s.CURRENT_NODE = '5' ");
        sql.append("and s.STATUS IN ");
        sql.append(PlatStringUtil.getSqlInCondition(status));
        if (StringUtils.isNotEmpty(keywords) && StringUtils.isNotEmpty(title)) {
            sql.append(" AND s.KEYWORDS LIKE ? AND s.TITLE LIKE ? ");
            sql.append(" ORDER BY s.CREATE_TIME DESC ");
            return superviseClazzService.findBySql(sql.toString(), new Object[]{userId, "%" + keywords + "%", "%" + title + "%"}, sqlFilter.getPagingBean());
        } else if (StringUtils.isNotEmpty(keywords) && StringUtils.isEmpty(title)) {
            sql.append(" AND s.KEYWORDS LIKE ? ");
            sql.append(" ORDER BY s.CREATE_TIME DESC ");
            return superviseClazzService.findBySql(sql.toString(), new Object[]{userId, "%" + keywords + "%"}, sqlFilter.getPagingBean());
        } else if (StringUtils.isEmpty(keywords) && StringUtils.isNotEmpty(title)) {
            sql.append(" AND s.TITLE LIKE ? ");
            sql.append(" ORDER BY s.CREATE_TIME DESC ");
            return superviseClazzService.findBySql(sql.toString(), new Object[]{userId, "%" + title + "%"}, sqlFilter.getPagingBean());
        } else {
            sql.append(" ORDER BY s.CREATE_TIME DESC ");
            return superviseClazzService.findBySql(sql.toString(), new Object[]{userId}, sqlFilter.getPagingBean());
        }
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
        String dbDepartName = (String) takerService.getRecord("plat_system_depart", new String[]{"DEPART_ID"}, new Object[]{supervise.get("DEPART_ID")}).get("DEPART_NAME");
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
                    progress.put("temp", dbDepartName + "负责人 审核");
                    result.add(progress);
                }
            }
            if (NODE_ID_2.equals(node.get("NODE_ID"))) {
                for (Map<String, Object> taskDepart : taskDeparts) {
                    String departName = (String) taskDepart.get("DEPART_NAME");
                    Map<String, Object> progress = new HashMap<>();
                    progress.put("NODE_ID", node.get("NODE_ID"));
                    progress.put("SHORT_NAME", node.get("SHORT_NAME"));
                    progress.put("VALUE", departName + "承办人 确认");
                    progress.put("date", node.get("CREATE_TIME"));
                    if (Integer.parseInt(supervise.get("STATUS").toString()) <= 3) {
                        if (NODE_ID_2.equals(supervise.get("CURRENT_NODE"))) {
                            progress.put("status", "0");//在办
                        } else {
                            progress.put("status", "2");//未开始
                        }
                    } else {
                        progress.put("status", "1");//办结
                    }
                    result.add(progress);
                    if (NEED_APPROVE_FLAG.equals(node.get("NEED_APPROVE_FLAG"))) {
                        progress = new HashMap<>();
                        progress.put("temp", dbDepartName + "负责人 审核");
                        result.add(progress);
                    }
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
                        progress.put("VALUE", departName + "负责人 审核");
                        //获取承办部门负责人审核记录
                        Map<String, Object> approve = dao.getRecord("tb_approve_record", new String[]{"SUPERVISE_ID", "APPROVE_NODE_ID"}, new Object[]{superviseId, node.get("NODE_ID")});
                        if (approve == null) {
                            progress.put("date", null);
                            progress.put("status", "2");
                        } else {
                            progress.put("date", approve.get("APPROVE_TIME"));
                            progress.put("status", "1");
                        }
                        if ("4".equals(supervise.get("STATUS").toString())) {
                            progress.put("status", "0");
                        }
                        progress.put("isApproveNode", true);
                        progress.put("NODE_ID", node.get("NODE_ID"));
                        result.add(progress);

                        //单独新增承办人提交的节点
                        progress = new HashMap<>();
                        progress.put("VALUE", departName + "承办人 提交");
                        if (approve == null) {
                            progress.put("date", null);
                            progress.put("status", "2");
                        } else {
                            progress.put("date", approve.get("APPROVE_TIME"));
                            progress.put("status", "1");
                        }
                        progress.put("NODE_ID", node.get("NODE_ID"));
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
                        progress.put("temp", departName + "负责人 审核");
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
                        progress.put("VALUE", departName + "负责人 审核");
                        //获取承办部门负责人审核记录
                        Map<String, Object> approve = dao.getRecord("tb_approve_record", new String[]{"SUPERVISE_ID", "APPROVE_NODE_ID"}, new Object[]{superviseId, node.get("NODE_ID")});
                        if (approve == null) {
                            progress.put("date", null);
                            progress.put("status", "2");
                        } else {
                            progress.put("date", approve.get("APPROVE_TIME"));
                            progress.put("status", "1");
                        }
                        if ("6".equals(supervise.get("STATUS").toString())) {
                            progress.put("status", "0");
                        }
                        progress.put("isApproveNode", true);
                        progress.put("NODE_ID", node.get("NODE_ID"));
                        result.add(progress);

                        //单独新增承办人提交的节点
                        progress = new HashMap<>();
                        progress.put("VALUE", departName + "承办人 提交");
                        if (approve == null) {
                            progress.put("date", null);
                            progress.put("status", "2");
                        } else {
                            progress.put("date", approve.get("APPROVE_TIME"));
                            progress.put("status", "1");
                        }
                        progress.put("NODE_ID", node.get("NODE_ID"));
                        result.add(progress);
                    }
                }
            }
        }
        if ("9".equals(supervise.get("STATUS").toString())) {
            Map<String, Object> progress = new HashMap<>();
            progress.put("VALUE", dbDepartName + " 办结");
            progress.put("date", supervise.get("UPDATE_TIME"));
            progress.put("status", "1");
            result.add(progress);
        } else {
            Map<String, Object> progress = new HashMap<>();
            progress.put("VALUE", dbDepartName + " 办结");
            progress.put("date", null);
            progress.put("status", "2");
            result.add(progress);
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
        Map<String, Object> supervise = takerService.getRecord("tb_supervise", new String[]{"RECORD_ID"}, new Object[]{params.get("SUPERVISE_ID")});
        //根据当前承办人 更新承办部门任务状态
        String userId = (String) PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID");
        Map<String, Object> task = takerService.getRecord("tb_supervise_task", new String[]{"SUPERVISE_ID", "USER_ID"}, new Object[]{params.get("SUPERVISE_ID"), userId});
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
        sql.append("where f.SUPERVISE_ID = ? and f.CREATE_BY = ? and f.NODE_ID = ? ");
        if (ORDER_BY_ASC.equals(orderType)) {
            sql.append("order by f.CREATE_TIME ASC limit 0,1 ");
        }
        if (ORDER_BY_DESC.equals(orderType)) {
            sql.append("order by f.CREATE_TIME DESC limit 0,1 ");
        }
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{recordId, userId, nodeId}, null);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 保存反馈文件上传记录
     *
     * @param params
     */
    @Override
    public void saveFileAttach(Map<String, Object> params) {
        Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
        String attachFilePath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        String originalfilename = (String) params.get("FILE_NAME");
        String dbfilepath = (String) params.get("FILE_URL");
        String fileuploadserver = (String) params.get("fileuploadserver");
        String absolutePath = attachFilePath + dbfilepath;
        File absoluteFile = new File(absolutePath);
        Map<String, Object> fileAttach = new HashMap<String, Object>();
        fileAttach.put("FILE_NAME", originalfilename);
        fileAttach.put("FILE_PATH", dbfilepath);
        fileAttach.put("FILE_TYPE", PlatFileUtil.getFileExt(dbfilepath));
        fileAttach.put("FILE_LENGTH", absoluteFile.length());
        fileAttach.put("FILE_SIZE", PlatFileUtil.getFormatFileSize(absoluteFile.length()));
        if (backLoginUser != null) {
            fileAttach.put("FILE_UPLOADERID", backLoginUser.get("SYSUSER_ID"));
            fileAttach.put("FILE_UPLOADERNAME", backLoginUser.get("SYSUSER_NAME"));
        }
        fileAttach.put("FILE_BUSTABLELNAME", "tb_supervise_feedback");
        fileAttach.put("FILE_BUSRECORDID", params.get("RECORD_ID"));
        fileAttach.put("FILE_TYPEKEY", "supervise");
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(fileuploadserver)) {
            fileAttach.put("FILE_UPSERVER", fileuploadserver);
        } else {
            fileAttach.put("FILE_UPSERVER", "1");
        }
        dao.saveOrUpdate("PLAT_SYSTEM_FILEATTACH", fileAttach, SysConstants.ID_GENERATOR_UUID, null);
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
        Map<String, Object> supervise = takerService.getRecord("tb_supervise", new String[]{"RECORD_ID"}, new Object[]{id});
        //获取督办部门
        String dbDepartName = (String) takerService.getRecord("plat_system_depart", new String[]{"DEPART_ID"}, new Object[]{supervise.get("DEPART_ID")}).get("DEPART_NAME");
        supervise.put("DEPART_NAME", dbDepartName);
        //根据节点ID 获取最近一次反馈内容
        Map<String, Object> feedback = this.takerService.getFeedbackInfo(id, nodeId, "DESC");
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
            feedback = this.takerService.getFeedbackInfo(id, "3", "ASC");
        }
        if (NODE_ID_5.equals(nodeId)) {
            feedback = this.takerService.getFeedbackInfo(id, "4", "ASC");
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
        Map<String, Object> confirm = takerService.getRecord("tb_supervise_confirm", new String[]{"SUPERVISE_ID", "NODE_ID"}, new Object[]{supervise.get("RECORD_ID"), nodeId});
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
     * 判断当前督办事项是否存在逾期
     *
     * @param supervise
     * @return
     */
    @Override
    public boolean isOutDate(Map<String, Object> supervise) {
        String superviseId = (String) supervise.get("RECORD_ID");
        String createTime = (String) supervise.get("CREATE_TIME");//创建时间
        int HANDLE_LIMIT = (Integer) supervise.get("HANDLE_LIMIT");//办理时限

        Map<String, Object> confirm = dao.getRecord("tb_supervise_confirm", new String[]{"SUPERVISE_ID", "NODE_ID"}, new Object[]{superviseId, NODE_ID_2});//督办确认
        long intervalTime = 0L;//督办确认
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

        //获取办理反馈节点的第一次反馈时间
        Map<String, Object> feedback1 = this.getFeedbackInfo(superviseId, NODE_ID_3, "ASC");
        long intervalTime1 = 0L;
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
            intervalTime1 = PlatDateTimeUtil.getIntervalTime(createTime, (String) confirm.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
            if (intervalTime1 <= (HANDLE_LIMIT * 24 - intervalTime)) {
                //未超期已反馈
            } else {
                //已超期已反馈
                return true;
            }
        }
        //获取落实反馈节点的第一次反馈时间
        /*Map<String, Object> feedback2 = this.getFeedbackInfo(superviseId, NODE_ID_4, "ASC");
        long intervalTime2 = 0L;
        if (feedback2 == null) {
            if (feedback1 == null) {
                intervalTime2 = PlatDateTimeUtil.getIntervalTime(createTime, PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
            } else {
                intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) feedback1.get("CREATE_TIME"), PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
            }
            //当前办理期限为 时限*24 - 前一次反馈耗费的时间
            if (intervalTime2 <= (HANDLE_LIMIT * 24 - intervalTime1)) {
                //未超期
            } else {
                //已超期
                return true;
            }
        } else {
            //已反馈
            intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) feedback1.get("CREATE_TIME"), (String) feedback2.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
            if (intervalTime2 <= (HANDLE_LIMIT * 24 - intervalTime1)) {
                //未超期
            } else {
                //已超期反馈
                return true;
            }
        }*/
        //获取办结反馈节点的第一次反馈时间
        Map<String, Object> feedback3 = this.getFeedbackInfo(superviseId, NODE_ID_5, "ASC");
        long intervalTime3 = 0L;
        if (feedback3 == null) {
            if (feedback1 == null) {
                intervalTime3 = PlatDateTimeUtil.getIntervalTime(createTime, PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
            } else {
                intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) feedback1.get("CREATE_TIME"), PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
            }
            //当前办理期限为 时限*24 - 前两次反馈耗费的时间
            if (intervalTime3 <= (HANDLE_LIMIT * 24 - intervalTime1)) {
                //未超期
            } else {
                //已超期
                return true;
            }
        } else {
            intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) feedback1.get("CREATE_TIME"), (String) feedback3.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
            if (intervalTime3 <= (HANDLE_LIMIT * 24 - intervalTime1)) {
                //未超期
            } else {
                //已超期反馈
                return true;
            }
        }
        return false;
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
                intervalTime1 = PlatDateTimeUtil.getIntervalTime(createTime, (String) confirm.get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
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
     * 承办人督办确认
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> takerConfirmSupervise(HttpServletRequest request) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        String superviseId = (String) params.get("SUPERVISE_ID");
        //获取督办事项信息、 获取部门督办任务信息
        //根据当前登录用户部门和督办编号获取督办任务ID
        Map<String, Object> task = dao.getRecord("tb_supervise_task", new String[]{"SUPERVISE_ID", "DEPART_ID"}, new Object[]{superviseId, user.get("DEPART_ID")});
        Map<String, Object> temp = dao.getRecord("tb_supervise_confirm", new String[]{"SUPERVISE_ID", "NODE_ID"}, new Object[]{superviseId, params.get("NODE_ID")});
        if (temp == null) {
            temp = new HashMap<>();
        }
        temp.put("SUPERVISE_ID", superviseId);
        if (task != null) {
            temp.put("SUPERVISE_TASK_ID", task.get("RECORD_ID"));
        }
        temp.put("NODE_ID", params.get("NODE_ID"));
        temp.put("DEPART_ID", user.get("DEPART_ID"));
        temp.put("CREATE_BY", PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID"));
        temp.put("CREATE_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        temp.put("REMARKS", params.get("REMARKS"));
        temp.put("STATUS", params.get("STATUS"));
        temp.put("IS_ACCEPT", params.get("IS_ACCEPT"));
        temp = dao.saveOrUpdate("tb_supervise_confirm", temp, SysConstants.ID_GENERATOR_UUID, null);
        //更新督办记录状态
        Map<String, Object> supervise = dao.getRecord("tb_supervise", new String[]{"RECORD_ID"}, new Object[]{superviseId});
        if (supervise != null) {
            if ("1".equals(temp.get("IS_ACCEPT"))) {
                //接收
                supervise.put("CURRENT_NODE", "3");
                supervise.put("STATUS", 5);
                task.put("STATUS", 5);
            } else {
                //拒收
                supervise.put("STATUS", 10);
                task.put("STATUS", 10);
            }
            supervise = dao.saveOrUpdate("tb_supervise", supervise, SysConstants.ID_GENERATOR_UUID, null);
            dao.saveOrUpdate("tb_supervise_task", task, SysConstants.ID_GENERATOR_UUID, null);
        }
        //初始化下一节点任务数据
        Map<String, Object> nodeMap = new HashMap<>();
        nodeMap.put("SUPERVISE_ID", superviseId);
        nodeMap.put("SUPERVISE_NO", supervise.get("SUPERVISE_NO"));
        nodeMap.put("NODE_ID", supervise.get("CURRENT_NODE"));
        if (NODE_ID_2.equals(params.get("NODE_ID"))) {
            nodeMap.put("NODE_NAME", "办理反馈");
        }
        nodeMap.put("CREATE_BY", user.get("SYSUSER_ID"));
        nodeMap.put("CREATE_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        dao.saveOrUpdate("tb_supervise_node_info", nodeMap, SysConstants.ID_GENERATOR_UUID, null);
        return temp;
    }
}
