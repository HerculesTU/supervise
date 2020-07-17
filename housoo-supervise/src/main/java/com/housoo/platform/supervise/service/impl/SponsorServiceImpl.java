package com.housoo.platform.supervise.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.DepartService;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.*;
import com.housoo.platform.supervise.dao.SponsorDao;
import com.housoo.platform.supervise.service.SponsorService;
import com.housoo.platform.supervise.service.SuperviseClazzService;
import org.apache.commons.lang.StringUtils;
import org.aspectj.apache.bcel.generic.IF_ACMPEQ;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 描述 督查督办业务 立项人 相关Service 接口
 *
 * @author zxl
 * @version 1.0
 * @created 2020-04-22 15:30:11
 */
@Service("sponsorService")
public class SponsorServiceImpl extends BaseServiceImpl implements SponsorService {

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
     * 全部 正在 待办 完成督办
     */

    private static final String STATUS_ALL = "1";
    private static final String STATUS_PROCESS = "2";
    private static final String STATUS_DRAFT = "3";
    private static final String STATUS_DONE = "4";

    /**
     * 所引入的dao
     */
    @Resource
    private SponsorDao dao;

    /**
     * 部门 departService
     */
    @Resource
    private DepartService departService;
    /**
     * 督办类型 superviseClazzService
     */
    @Resource
    private SuperviseClazzService superviseClazzService;

    /**
     * 立项人 sponsorService
     */
    @Resource
    private SponsorService sponsorService;

    /**
     * 获取dao
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取立项人首页扇形统计数据 (发起督办数量)
     *
     * @param request
     * @return
     */
    @Override
    public List<Map<String, Object>> getSponsorIndexSectorCommitData(HttpServletRequest request) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        String type = (String) params.get("type");
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer();
        if (SEARCHTYPE_YEAR.equals(type)) {
            sql.append(" SELECT T1.SUPERVISE_CLAZZ,COUNT(*) AS NUM FROM tb_supervise T1  WHERE T1.CREATE_BY=? AND " +
                    " T1.`STATUS` NOT IN (0,10) AND  YEAR(T1.CREATE_TIME) = YEAR(NOW()) GROUP BY T1.SUPERVISE_CLAZZ ");
            result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);
        }
        if (SEARCHTYPE_QUARTER.equals(type)) {
            sql.append(" SELECT T1.SUPERVISE_CLAZZ,COUNT(*) AS NUM FROM tb_supervise T1  WHERE T1.CREATE_BY=? AND " +
                    " T1.`STATUS` NOT IN (0,10) AND  YEAR(T1.CREATE_TIME) = YEAR(NOW()) AND QUARTER(T1.CREATE_TIME) = QUARTER(NOW())" +
                    " GROUP BY T1.SUPERVISE_CLAZZ ");
            result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);
        }
        if (SEARCHTYPE_MONTH.equals(type)) {
            sql.append(" SELECT T1.SUPERVISE_CLAZZ,COUNT(*) AS NUM FROM tb_supervise T1  WHERE T1.CREATE_BY=? AND " +
                    " T1.`STATUS` NOT IN (0,10) AND  YEAR(T1.CREATE_TIME) = YEAR(NOW()) AND MONTH(T1.CREATE_TIME) = MONTH(NOW())" +
                    " GROUP BY T1.SUPERVISE_CLAZZ ");
            result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);
        }
        if (SEARCHTYPE_DAYS.equals(type)) {
            sql.append(" SELECT T1.SUPERVISE_CLAZZ,COUNT(*) AS NUM FROM tb_supervise T1  WHERE T1.CREATE_BY=? AND " +
                    " T1.`STATUS` NOT IN (0,10) AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(T1.CREATE_TIME)  " +
                    " GROUP BY T1.SUPERVISE_CLAZZ ");
            result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);
        }
        if (StringUtils.isEmpty(type)) {
            sql.append(" SELECT T1.SUPERVISE_CLAZZ,COUNT(*) AS NUM FROM tb_supervise T1  WHERE T1.CREATE_BY=? AND " +
                    " T1.`STATUS` NOT IN (0,10) ");
            if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
                sql.append(" AND date_format(T1.CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(T1.CREATE_TIME,'%Y-%m-%d') <= ? ");
            }
            sql.append(" GROUP BY T1.SUPERVISE_CLAZZ ");
            if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
                result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID"), params.get("startDate"), params.get("endDate")}, null);
            } else {
                result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);

            }
        }
        if (result.size() == 0) {
            StringBuffer sqlQuery = new StringBuffer("SELECT T.RECORD_ID ,T.CLAZZ_NAME FROM tb_supervise_clazz T  ");
            List<Map<String, Object>> clazzList = dao.findBySql(sqlQuery.toString(), new Object[]{}, null);
            for (Map<String, Object> param : clazzList) {
                Map<String, Object> map = new HashMap<>();
                map.put("SUPERVISE_CLAZZ", param.get("CLAZZ_NAME"));
                map.put("NUM", 0);
                result.add(map);
            }
        }
        return result;
    }

    /**
     * 获取立项人首页扇形统计数据(进行中的督办数量)
     *
     * @param request
     * @return
     */
    @Override
    public List<Map<String, Object>> getSponsorIndexSectorProcessData(HttpServletRequest request) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        String type = (String) params.get("type");
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer();
        if (SEARCHTYPE_YEAR.equals(type)) {
            sql.append(" SELECT T1.SUPERVISE_CLAZZ,COUNT(*) AS NUM FROM tb_supervise T1  WHERE T1.CREATE_BY=? AND " +
                    " T1.`STATUS` NOT IN (0,9,10) AND  YEAR(T1.CREATE_TIME) = YEAR(NOW()) GROUP BY T1.SUPERVISE_CLAZZ ");
            result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);
        }
        if (SEARCHTYPE_QUARTER.equals(type)) {
            sql.append(" SELECT T1.SUPERVISE_CLAZZ,COUNT(*) AS NUM FROM tb_supervise T1  WHERE T1.CREATE_BY=? AND " +
                    " T1.`STATUS` NOT IN (0,9,10) AND  YEAR(T1.CREATE_TIME) = YEAR(NOW()) AND QUARTER(T1.CREATE_TIME) = QUARTER(NOW())" +
                    " GROUP BY T1.SUPERVISE_CLAZZ ");
            result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);
        }
        if (SEARCHTYPE_MONTH.equals(type)) {
            sql.append(" SELECT T1.SUPERVISE_CLAZZ,COUNT(*) AS NUM FROM tb_supervise T1  WHERE T1.CREATE_BY=? AND " +
                    " T1.`STATUS` NOT IN (0,9,10) AND  YEAR(T1.CREATE_TIME) = YEAR(NOW()) AND MONTH(T1.CREATE_TIME) = MONTH(NOW())" +
                    " GROUP BY T1.SUPERVISE_CLAZZ ");
            result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);
        }
        if (SEARCHTYPE_DAYS.equals(type)) {
            sql.append(" SELECT T1.SUPERVISE_CLAZZ,COUNT(*) AS NUM FROM tb_supervise T1  WHERE T1.CREATE_BY=? AND " +
                    " T1.`STATUS` NOT IN (0,9,10) AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(T1.CREATE_TIME)  " +
                    " GROUP BY T1.SUPERVISE_CLAZZ ");
            result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);
        }
        if (StringUtils.isEmpty(type)) {
            sql.append(" SELECT T1.SUPERVISE_CLAZZ,COUNT(*) AS NUM FROM tb_supervise T1  WHERE T1.CREATE_BY=? AND " +
                    " T1.`STATUS` NOT IN (0,9,10) ");
            if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
                sql.append(" AND date_format(T1.CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(T1.CREATE_TIME,'%Y-%m-%d') <= ? ");
            }
            sql.append(" GROUP BY T1.SUPERVISE_CLAZZ ");
            if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
                result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID"), params.get("startDate"), params.get("endDate")}, null);
            } else {
                result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);
            }
        }
        if (result.size() == 0) {
            StringBuffer sqlQuery = new StringBuffer("SELECT T.RECORD_ID ,T.CLAZZ_NAME FROM tb_supervise_clazz T  ");
            List<Map<String, Object>> clazzList = dao.findBySql(sqlQuery.toString(), new Object[]{}, null);
            for (Map<String, Object> param : clazzList) {
                Map<String, Object> map = new HashMap<>();
                map.put("SUPERVISE_CLAZZ", param.get("CLAZZ_NAME"));
                map.put("NUM", 0);
                result.add(map);
            }
        }
        return result;
    }

    /**
     * 获取立项人首页扇形统计数据(结束的督办数量)
     *
     * @param request
     * @return
     */
    @Override
    public List<Map<String, Object>> getSponsorIndexSectorEndData(HttpServletRequest request) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        String type = (String) params.get("type");
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer();
        if (SEARCHTYPE_YEAR.equals(type)) {
            sql.append(" SELECT T1.SUPERVISE_CLAZZ,COUNT(*) AS NUM FROM tb_supervise T1  WHERE T1.CREATE_BY=? AND " +
                    " T1.`STATUS` = 9 AND  YEAR(T1.CREATE_TIME) = YEAR(NOW()) GROUP BY T1.SUPERVISE_CLAZZ ");
            result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);
        }
        if (SEARCHTYPE_QUARTER.equals(type)) {
            sql.append(" SELECT T1.SUPERVISE_CLAZZ,COUNT(*) AS NUM FROM tb_supervise T1  WHERE T1.CREATE_BY=? AND " +
                    " T1.`STATUS` = 9 AND  YEAR(T1.CREATE_TIME) = YEAR(NOW()) AND QUARTER(T1.CREATE_TIME) = QUARTER(NOW())" +
                    " GROUP BY T1.SUPERVISE_CLAZZ ");
            result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);
        }
        if (SEARCHTYPE_MONTH.equals(type)) {
            sql.append(" SELECT T1.SUPERVISE_CLAZZ,COUNT(*) AS NUM FROM tb_supervise T1  WHERE T1.CREATE_BY=? AND " +
                    " T1.`STATUS` = 9 AND  YEAR(T1.CREATE_TIME) = YEAR(NOW()) AND MONTH(T1.CREATE_TIME) = MONTH(NOW())" +
                    " GROUP BY T1.SUPERVISE_CLAZZ ");
            result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);
        }
        if (SEARCHTYPE_DAYS.equals(type)) {
            sql.append(" SELECT T1.SUPERVISE_CLAZZ,COUNT(*) AS NUM FROM tb_supervise T1  WHERE T1.CREATE_BY=? AND " +
                    " T1.`STATUS` = 9 AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(T1.CREATE_TIME)  " +
                    " GROUP BY T1.SUPERVISE_CLAZZ ");
            result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);
        }
        if (StringUtils.isEmpty(type)) {
            sql.append(" SELECT T1.SUPERVISE_CLAZZ,COUNT(*) AS NUM FROM tb_supervise T1  WHERE T1.CREATE_BY=? AND " +
                    " T1.`STATUS` = 9 ");
            if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
                sql.append(" AND date_format(T1.CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(T1.CREATE_TIME,'%Y-%m-%d') <= ? ");
            }
            sql.append(" GROUP BY T1.SUPERVISE_CLAZZ ");
            if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
                result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID"), params.get("startDate"), params.get("endDate")}, null);
            } else {
                result = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);
            }
        }
        if (result.size() == 0) {
            StringBuffer sqlQuery = new StringBuffer("SELECT T.RECORD_ID ,T.CLAZZ_NAME FROM tb_supervise_clazz T  ");
            List<Map<String, Object>> clazzList = dao.findBySql(sqlQuery.toString(), new Object[]{}, null);
            for (Map<String, Object> param : clazzList) {
                Map<String, Object> map = new HashMap<>();
                map.put("SUPERVISE_CLAZZ", param.get("CLAZZ_NAME"));
                map.put("NUM", 0);
                result.add(map);
            }
        }
        return result;
    }

    /**
     * 获取立项人首页扇形统计数据(逾期的督办数量)
     *
     * @param request
     * @return
     */
    @Override
    public List<Map<String, Object>> getSponsorIndexSectorOutTimeData(HttpServletRequest request) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        StringBuffer sql = new StringBuffer("SELECT T.RECORD_ID ,T.CLAZZ_NAME FROM tb_supervise_clazz T  ");
        List<Map<String, Object>> clazzList = dao.findBySql(sql.toString(), new Object[]{}, null);
        for (Map<String, Object> map : clazzList) {
            Map<String, Object> resultMap = new HashMap<>();
            params.put("CLAZZ_ID", map.get("RECORD_ID"));
            params.put("sector", "1");
            Map<String, Object> outTimeNumericData = getOutTimeNumericData(params);
            resultMap.put("SUPERVISE_CLAZZ", map.get("CLAZZ_NAME"));
            resultMap.put("NUM", outTimeNumericData.get("count"));
            result.add(resultMap);
        }
        return result;
    }

    /**
     * 获取立项人首页数值型统计数据
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> getSponsorIndexNumericData(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        String type = (String) params.get("type");
        if (SEARCHTYPE_YEAR.equals(type)) {
            result = dao.getSponsorIndexNumericDataByYear(params);
            Map<String, Object> outTimeNumericData = getOutTimeNumericData(params);
            result.put("OUTDATESUM", outTimeNumericData.get("count"));
        }
        if (SEARCHTYPE_QUARTER.equals(type)) {
            result = dao.getSponsorIndexNumericDataByQuarter(params);
            Map<String, Object> outTimeNumericData = getOutTimeNumericData(params);
            result.put("OUTDATESUM", outTimeNumericData.get("count"));

        }
        if (SEARCHTYPE_MONTH.equals(type)) {
            result = dao.getSponsorIndexNumericDataByMonth(params);
            Map<String, Object> outTimeNumericData = getOutTimeNumericData(params);
            result.put("OUTDATESUM", outTimeNumericData.get("count"));
        }
        if (SEARCHTYPE_DAYS.equals(type)) {
            result = dao.getSponsorIndexNumericDataByDays(params);
            Map<String, Object> outTimeNumericData = getOutTimeNumericData(params);
            result.put("OUTDATESUM", outTimeNumericData.get("count"));
        }
        if (StringUtils.isEmpty(type)) {
            result = dao.getSponsorIndexNumericDataByRangeTime(params);
            Map<String, Object> outTimeNumericData = getOutTimeNumericData(params);
            result.put("OUTDATESUM", outTimeNumericData.get("count"));
        }
        return result;
    }

    /**
     * 首页统计 逾期数量 （统计正在进行中的督办任务）
     */
    private Map<String, Object> getOutTimeNumericData(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> superviseList = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        //获取 当前登录人 的所有进行中的任务
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        sql.append(" SELECT RECORD_ID,CURRENT_NODE,STATUS FROM tb_supervise WHERE CREATE_BY =? AND `STATUS` NOT IN (0,9,10) ");
        String type = (String) params.get("type");
        if (SEARCHTYPE_YEAR.equals(type)) {
            sql.append(" AND YEAR(CREATE_TIME) = YEAR(NOW()) ");
        }
        if (SEARCHTYPE_QUARTER.equals(type)) {
            sql.append(" AND YEAR(CREATE_TIME) = YEAR(NOW()) AND QUARTER(CREATE_TIME) = QUARTER(NOW()) ");
        }
        if (SEARCHTYPE_MONTH.equals(type)) {
            sql.append(" AND YEAR(CREATE_TIME) = YEAR(NOW()) AND MONTH(CREATE_TIME) = MONTH(NOW()) ");
        }
        if (SEARCHTYPE_DAYS.equals(type)) {
            sql.append(" AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(CREATE_TIME) ");
        }
        if (StringUtils.isEmpty(type)) {
            if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
                sql.append(" AND date_format(CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(CREATE_TIME,'%Y-%m-%d') <= ? ");
            }
        }
        //若相等 则是 扇形逾期数据统计
        if ("1".equals((String) params.get("sector"))) {
            sql.append(" AND SUPERVISE_CLAZZ_ID=? ");
        }
        if ("1".equals((String) params.get("sector"))) {
            if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
                superviseList = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID"), params.get("startDate"), params.get("endDate"), params.get("CLAZZ_ID")}, null);
            } else {
                superviseList = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID"), params.get("CLAZZ_ID")}, null);
            }
        } else {
            if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
                superviseList = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID"), params.get("startDate"), params.get("endDate")}, null);
            } else {
                superviseList = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);
            }
        }

        if (superviseList.size() != 0) {
            //计数
            int count = 0;
            for (Map<String, Object> map : superviseList) {
                boolean flag = true;
                StringBuffer querySql = new StringBuffer();
                String recordId = map.get("RECORD_ID").toString();
                querySql.append("SELECT T4.RECORD_ID,T2.CREATE_TIME,T1.NODE_NAME,T3.DEPART_ID AS TAKER_DEPART_ID,T4.HANDLE_LIMIT,T1.NODE_ID, " +
                        " T4.DEPART_ID AS COMMIT_DEPART_ID ,T4.KEYWORDS,T4.TITLE,T4.SUPERVISE_CLAZZ,T4.SUPERVISE_ITEM,T4.STATUS,T4.SUPERVISE_NO, " +
                        " T4.CURRENT_NODE,T4.SUPERVISE_SOURCE FROM tb_supervise_approve T1 " +
                        " LEFT JOIN tb_supervise_node_info T2 ON T1.NODE_NAME=T2.NODE_NAME  AND T2.SUPERVISE_ID=? " +
                        " LEFT JOIN tb_supervise_task T3 ON T3.SUPERVISE_ID=T2.SUPERVISE_ID" +
                        " LEFT JOIN tb_supervise T4 ON T4.RECORD_ID=T2.SUPERVISE_ID WHERE T1.DEL_FLAG = '1' ");
                List<Map<String, Object>> superviseInfoByRecordId =
                        sponsorService.findBySql(querySql.toString(), new Object[]{recordId}, null);
                for (Map<String, Object> param : superviseInfoByRecordId) {
                    if (flag) {
                        //分别为 任务发起 督办确认 办理反馈 三个节点 实际办理时间
                        long intervalTime2 = 0;
                        long intervalTime3 = 0;
                        long intervalTime5 = 0;
                        StringBuilder feedbackSql = new StringBuilder(
                                "SELECT T2.RECORD_ID,T2.CREATE_TIME FROM tb_supervise_reply T1 " +
                                        " LEFT JOIN tb_supervise_feedback T2 ON T1.FEEDBACK_ID = T2.RECORD_ID " +
                                        " WHERE T1.SUPERVISE_ID = ? " +
                                        " AND T1.NODE_ID = ? ORDER BY T2.CREATE_TIME DESC ");
                        List<Map<String, Object>> feedbackList3 =
                                sponsorService.findBySql(feedbackSql.toString(), new Object[]{param.get("RECORD_ID"), NODE_ID_3}, null);
                        List<Map<String, Object>> feedbackList4 =
                                sponsorService.findBySql(feedbackSql.toString(), new Object[]{param.get("RECORD_ID"), NODE_ID_4}, null);
                        List<Map<String, Object>> feedbackList5 =
                                sponsorService.findBySql(feedbackSql.toString(), new Object[]{param.get("RECORD_ID"), NODE_ID_5}, null);
                        StringBuilder confirmSql = new StringBuilder(
                                " SELECT * FROM tb_supervise_confirm WHERE SUPERVISE_ID=? ");
                        List<Map<String, Object>> confirmList =
                                this.findBySql(confirmSql.toString(), new Object[]{param.get("RECORD_ID")}, null);

                        if (NODE_ID_3.equals(param.get("NODE_ID"))) {
                            if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                                if (feedbackList3.size() != 0) {
                                    intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                            (String) feedbackList3.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                                } else {
                                    intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                            PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                                }
                            }
                        }

                        if (NODE_ID_5.equals(param.get("NODE_ID"))) {
                            if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                                if (feedbackList5.size() != 0) {
                                    intervalTime5 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                            (String) feedbackList5.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                                } else {
                                    intervalTime5 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                            PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                                }
                            }
                        }
                        int limitTime = 0;
                        if (null != param.get("HANDLE_LIMIT")) {
                            limitTime = (Integer) param.get("HANDLE_LIMIT");
                        }
                        if (NODE_ID_2.equals(param.get("NODE_ID"))) {
                            if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                                if (confirmList.size() != 0) {
                                    intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                            (String) confirmList.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                                    if (intervalTime2 > 24) {
                                        count++;
                                        flag = false;
                                        break;
                                    }
                                } else {
                                    intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                            PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                                    if (intervalTime2 > 24) {
                                        count++;
                                        flag = false;
                                        break;
                                    }
                                }
                            }
                        }
                        //办理反馈
                        if (NODE_ID_3.equals(param.get("NODE_ID"))) {
                            if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                                //办理反馈节点 默认 3天时间
                                if (intervalTime3 > limitTime * 24) {
                                    count++;
                                    flag = false;
                                    break;
                                }
                            }
                        }
                        //办结反馈
                        if (NODE_ID_5.equals(param.get("NODE_ID"))) {
                            if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                                //需要 去除 第三 第四阶段实际 消耗时间 intervalTime3 intervalTime4
                                if (intervalTime5 > (limitTime * 24 - intervalTime3)) {
                                    count++;
                                    flag = false;
                                    break;
                                }
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
            result.put("count", count);
        } else {
            result.put("count", "0");
        }
        return result;
    }

    /**
     * 获取立项人首页柱线图统计数据
     *
     * @param request
     * @return
     */
    @Override
    public List<Map<String, Object>> findSponsorIndexLineData(HttpServletRequest request) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        //1.根据用户所在单位获取部门列表
        StringBuffer sql = new StringBuffer("SELECT d.DEPART_ID,d.DEPART_NAME ");
        sql.append("FROM plat_system_depart d ");
        /*sql.append("RIGHT JOIN tb_supervise_task t on d.DEPART_ID = t.DEPART_ID ");*/
        sql.append("WHERE d.DEPART_COMPANYID = ? ORDER BY d.DEPART_TREESN ASC");
        List<Map<String, Object>> departList = departService.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_COMPANYID")}, null);
        if (departList != null) {
            for (Map<String, Object> depart : departList) {
                String departId = (String) depart.get("DEPART_ID");
                String status = request.getParameter("status");
                sql = new StringBuffer("SELECT ");
                sql.append("CASE WHEN s.`STATUS`!=0 THEN COUNT(*) ELSE 0 END AS 'ALLSUM',");
                sql.append("CASE WHEN s.`STATUS`=9 THEN COUNT(*) ELSE 0 END AS 'FINISHSUM',");
                sql.append("CASE WHEN s.`STATUS` BETWEEN 0 and 9 THEN COUNT(*) ELSE 0 END AS 'INGSUM' ");
                sql.append("FROM tb_supervise s ");
                sql.append("LEFT JOIN tb_supervise_task t on s.RECORD_ID = t.SUPERVISE_ID and t.SUPERVISE_NO = s.SUPERVISE_NO ");
                sql.append("LEFT JOIN plat_system_depart d on t.DEPART_ID = d.DEPART_ID ");
                sql.append("WHERE S.`STATUS` in ");
                sql.append(PlatStringUtil.getSqlInCondition(status));
                sql.append("and YEAR(s.CREATE_TIME) = YEAR(NOW()) and s.USER_ID = ? and t.DEPART_ID = ? ");
                sql.append("GROUP BY t.DEPART_ID,s.`STATUS` ");
                List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID"), departId}, null);
                int all = 0, ing = 0, finish = 0;
                Map<String, Object> temp = new HashMap<>();
                if (list != null) {
                    for (Map<String, Object> count : list) {
                        all += Integer.parseInt(count.get("ALLSUM").toString());
                        ing += Integer.parseInt(count.get("INGSUM").toString());
                        finish += Integer.parseInt(count.get("FINISHSUM").toString());
                    }
                }
                temp.put("ALLSUM", all);
                temp.put("FINISHSUM", finish);
                temp.put("INGSUM", ing);
                depart.put("data", temp);
            }
        }
        return departList;
    }

    /**
     * 获取立项人首页部门督办列表数据
     *
     * @param request
     * @return
     */
    @Override
    public List<Map<String, Object>> findSponsorIndexTableData(HttpServletRequest request) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        //1.根据用户所在单位获取部门列表
        StringBuffer sql = new StringBuffer("SELECT d.DEPART_ID,d.DEPART_NAME ");
        sql.append("FROM plat_system_depart d ");
        sql.append("WHERE d.DEPART_COMPANYID = ? ORDER BY d.DEPART_TREESN ASC ");
        List<Map<String, Object>> departList = departService.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_COMPANYID")}, null);
        if (departList != null) {
            for (Map<String, Object> depart : departList) {
                String departId = (String) depart.get("DEPART_ID");
                List<Map<String, Object>> clazzs = superviseClazzService.getAllSuperviseClazz(null);
                if (clazzs != null) {
                    for (Map<String, Object> clazz : clazzs) {
                        String clazzId = (String) clazz.get("VALUE");
                        sql = new StringBuffer("select COUNT(*) COUNT ");
                        sql.append("from tb_supervise s ");
                        sql.append("LEFT JOIN tb_supervise_task t on s.RECORD_ID = t.SUPERVISE_ID and t.SUPERVISE_NO = s.SUPERVISE_NO ");
                        sql.append("WHERE S.STATUS NOT IN (0,10)and s.USER_ID = ? and YEAR(s.CREATE_TIME) = YEAR(NOW()) and t.DEPART_ID= ? and s.SUPERVISE_CLAZZ_ID = ? ");
                        Map<String, Object> temp = dao.getBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID"), departId, clazzId});
                        if (temp == null) {
                            clazz.put("count", 0);
                        } else {
                            clazz.put("count", temp.get("COUNT"));
                        }
                    }
                }
                depart.put("clazzs", clazzs);
            }
        }
        return departList;
    }

    /**
     * 获取立项人首页逾期统计数据
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> findSponsorIndexOutDateData(HttpServletRequest request) {
        //督办反馈 督办确认  办理反馈 落实反馈 办结反馈
        int dbfk = 0;
        int dbqr = 0;
        int blfk = 0;
        int bjfk = 0;
        StringBuffer sql = new StringBuffer();
        Map<String, Object> result = new HashMap<>();
        //获取 当前登录人 的所有进行中的任务
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        sql.append(" SELECT RECORD_ID,CURRENT_NODE,STATUS FROM tb_supervise WHERE CREATE_BY =? AND `STATUS` NOT IN (0,9,10) ");
        List<Map<String, Object>> superviseList = dao.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID")}, null);
        if (superviseList.size() != 0) {
            for (Map<String, Object> map : superviseList) {
                StringBuffer querySql = new StringBuffer();
                String recordId = map.get("RECORD_ID").toString();
                querySql.append("SELECT T4.RECORD_ID,T2.CREATE_TIME,T1.NODE_NAME,T3.DEPART_ID AS TAKER_DEPART_ID,T4.HANDLE_LIMIT,T1.NODE_ID, " +
                        " T4.DEPART_ID AS COMMIT_DEPART_ID ,T4.KEYWORDS,T4.TITLE,T4.SUPERVISE_CLAZZ,T4.SUPERVISE_ITEM,T4.STATUS,T4.SUPERVISE_NO, " +
                        " T4.CURRENT_NODE,T4.SUPERVISE_SOURCE FROM tb_supervise_approve T1 " +
                        " LEFT JOIN tb_supervise_node_info T2 ON T1.NODE_NAME=T2.NODE_NAME  AND T2.SUPERVISE_ID=? " +
                        " LEFT JOIN tb_supervise_task T3 ON T3.SUPERVISE_ID=T2.SUPERVISE_ID" +
                        " LEFT JOIN tb_supervise T4 ON T4.RECORD_ID=T2.SUPERVISE_ID WHERE T1.DEL_FLAG = '1' ");
                List<Map<String, Object>> superviseInfoByRecordId =
                        sponsorService.findBySql(querySql.toString(), new Object[]{recordId}, null);
                for (Map<String, Object> param : superviseInfoByRecordId) {
                    //分别为 任务发起 督办确认 办理反馈 三个节点 实际办理时间
                    long intervalTime2 = 0;
                    long intervalTime3 = 0;
                    long intervalTime5 = 0;
                    StringBuilder feedbackSql = new StringBuilder(
                            "SELECT T2.RECORD_ID,T2.CREATE_TIME FROM tb_supervise_reply T1 " +
                                    " LEFT JOIN tb_supervise_feedback T2 ON T1.FEEDBACK_ID = T2.RECORD_ID " +
                                    " WHERE T1.SUPERVISE_ID = ? " +
                                    " AND T1.NODE_ID = ? ORDER BY T2.CREATE_TIME DESC ");
                    List<Map<String, Object>> feedbackList3 =
                            sponsorService.findBySql(feedbackSql.toString(), new Object[]{param.get("RECORD_ID"), NODE_ID_3}, null);
                    List<Map<String, Object>> feedbackList4 =
                            sponsorService.findBySql(feedbackSql.toString(), new Object[]{param.get("RECORD_ID"), NODE_ID_4}, null);
                    List<Map<String, Object>> feedbackList5 =
                            sponsorService.findBySql(feedbackSql.toString(), new Object[]{param.get("RECORD_ID"), NODE_ID_5}, null);
                    StringBuilder confirmSql = new StringBuilder(
                            "SELECT * FROM tb_supervise_confirm WHERE SUPERVISE_ID=?  ");
                    List<Map<String, Object>> confirmList =
                            this.findBySql(confirmSql.toString(), new Object[]{param.get("RECORD_ID")}, null);
                    if (NODE_ID_3.equals(param.get("NODE_ID"))) {
                        if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                            if (feedbackList3.size() != 0) {
                                intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                        (String) feedbackList3.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                            } else {
                                intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                        PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                            }
                        }
                    }
                    if (NODE_ID_5.equals(param.get("NODE_ID"))) {
                        if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                            if (feedbackList5.size() != 0) {
                                intervalTime5 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                        (String) feedbackList5.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                            } else {
                                intervalTime5 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                        PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                            }
                        }
                    }
                    int limitTime = 0;
                    if (null != param.get("HANDLE_LIMIT")) {
                        limitTime = (Integer) param.get("HANDLE_LIMIT");
                    }
                    if (NODE_ID_2.equals(param.get("NODE_ID"))) {
                        if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                            if (confirmList.size() != 0) {
                                intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                        (String) confirmList.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                                if (intervalTime2 > 24) {
                                    dbqr++;
                                }
                            } else {
                                intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                        PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                                if (intervalTime2 > 24) {
                                    dbqr++;
                                }
                            }
                        }
                    }
                    //办理反馈
                    if (NODE_ID_3.equals(param.get("NODE_ID"))) {
                        if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                            //办理反馈节点
                            if (intervalTime3 > limitTime * 24) {
                                blfk++;
                            }
                        }
                    }

                    //办结反馈
                    if (NODE_ID_5.equals(param.get("NODE_ID"))) {
                        if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                            //需要 去除 第三 第四阶段实际 消耗时间 intervalTime3 intervalTime4
                            if (intervalTime5 > (limitTime * 24 - intervalTime3)) {
                                bjfk++;
                            }
                        }
                    }

                }
            }
            result.put("dbfk", dbfk);
            result.put("blfk", blfk);
            result.put("bjfk", bjfk);
            result.put("dbqr", dbqr);
        }
        return result;
    }

    /**
     * 获取立项人创建的督办任务列表
     * cjr
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map<String, Object>> createdSuperviseList(SqlFilter sqlFilter) {
        String userId = (String) PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID");
        Map<String, Object> params = sqlFilter.getQueryParams();
        //督办单状态：全部、待发（草稿）、办结
        String status = (String) params.get("status");
        //督办类型：日常管理、重点工作、重点工程、重点技改、重大改革
        String type = (String) params.get("supervise_type");
        List<Map<String, Object>> list = new ArrayList<>();
        StringBuffer sql = new StringBuffer("select s.RECORD_ID,s.SUPERVISE_NO,s.SUPERVISE_CLAZZ,s.TITLE,s.SUPERVISE_CONTENT,s.KEYWORDS,s.CREATE_TIME,s.DEPART_ID,d.DEPART_NAME,s.STATUS ");
        sql.append("from tb_supervise s ");
        sql.append("LEFT JOIN plat_system_depart d on s.DEPART_ID = d.DEPART_ID ");
        sql.append("LEFT JOIN tb_supervise_clazz c on s.SUPERVISE_CLAZZ_ID = c.RECORD_ID ");
        sql.append("where s.USER_ID = ? and s.SUPERVISE_TYPE = ? ");
        if (StringUtils.isNotEmpty(status)) {
            sql.append("and s.status = ? ");
            list = dao.findBySql(sql.toString(), new Object[]{userId, type, status}, sqlFilter.getPagingBean());
        } else {
            list = dao.findBySql(sql.toString(), new Object[]{userId, type}, sqlFilter.getPagingBean());
        }
        return list;
    }

    /**
     * 跳转到督办事项详情页（立项人）
     *
     * @param request
     */
    @Override
    public void goSuperviseInfo(HttpServletRequest request) {
        String id = request.getParameter("id");
        String nodeId = request.getParameter("nodeId");
        request.setAttribute("nodeId", nodeId);
        StringBuffer querySql = new StringBuffer();
        querySql.append("SELECT T4.RECORD_ID,T2.CREATE_TIME,T1.NODE_NAME,T3.DEPART_ID AS TAKER_DEPART_ID,T4.HANDLE_LIMIT,T2.NODE_ID, " +
                " T4.DEPART_ID AS COMMIT_DEPART_ID ,T4.KEYWORDS,T4.TITLE,T4.SUPERVISE_CLAZZ,T4.SUPERVISE_ITEM,T4.STATUS,T4.SUPERVISE_NO,T4.NO_CUSTOM, " +
                " T4.CURRENT_NODE,T4.FILE_URL,T1.SHORT_NAME,T4.SUPERVISE_SOURCE,T4.FILENAME,T4.SUPERVISE_CONTENT,T3.RECORD_ID AS TASK_ID FROM tb_supervise_approve T1 " +
                " LEFT JOIN tb_supervise_node_info T2 ON T1.NODE_NAME=T2.NODE_NAME  AND T2.SUPERVISE_ID=?  " +
                " LEFT JOIN tb_supervise_task T3 ON T3.SUPERVISE_ID=T2.SUPERVISE_ID" +
                " LEFT JOIN tb_supervise T4 ON T4.RECORD_ID=T2.SUPERVISE_ID WHERE T1.NODE_ID=? AND T1.DEL_FLAG = '1'");
        Map<String, Object> superviseInfo =
                sponsorService.getBySql(querySql.toString(), new Object[]{id, nodeId});

        if (StringUtils.isNotEmpty((String) superviseInfo.get("RECORD_ID"))) {
            Map<String, Object> takerMap = departService.getRecord("plat_system_depart",
                    new String[]{"DEPART_ID"}, new Object[]{superviseInfo.get("TAKER_DEPART_ID").toString()});
            superviseInfo.put("TAKER_DEPART_NAME", takerMap.get("DEPART_NAME"));
            Map<String, Object> commitMap = departService.getRecord("plat_system_depart",
                    new String[]{"DEPART_ID"}, new Object[]{superviseInfo.get("COMMIT_DEPART_ID").toString()});
            superviseInfo.put("COMMIT_DEPART_NAME", commitMap.get("DEPART_NAME"));
        }
        int limitTime = 0;
        if (null != superviseInfo.get("HANDLE_LIMIT")) {
            limitTime = (Integer) superviseInfo.get("HANDLE_LIMIT");
        }
        //根据当前节点 获取 逾期信息
        //办理反馈
        if (NODE_ID_2.equals(request.getParameter("nodeId"))) {
            StringBuilder confirmSql = new StringBuilder(
                    "SELECT * FROM tb_supervise_confirm WHERE SUPERVISE_ID=?  ");
            List<Map<String, Object>> confirmList =
                    this.findBySql(confirmSql.toString(), new Object[]{superviseInfo.get("RECORD_ID")}, null);
            if (confirmList.size() != 0) {
                long intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) superviseInfo.get("CREATE_TIME"),
                        (String) confirmList.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                if (intervalTime2 <= 24) {
                    superviseInfo.put("timeStatus1", "完成");
                }
            } else {
                long intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) superviseInfo.get("CREATE_TIME"),
                        PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                if (intervalTime2 > 24) {
                    superviseInfo.put("timeStatus1", "已逾期");
                } else {
                    superviseInfo.put("timeStatus1", "还剩" + (24 - intervalTime2) + "小时");
                }
            }
        }
        if (NODE_ID_3.equals(request.getParameter("nodeId"))) {
            long intervalTime = PlatDateTimeUtil.getIntervalTime((String) superviseInfo.get("CREATE_TIME"),
                    PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
            if (intervalTime <= limitTime) {
                //办理中 完成 两种状态
                if (Integer.parseInt(String.valueOf(superviseInfo.get("CURRENT_NODE"))) > 3) {
                    superviseInfo.put("timeStatus1", "完成");
                } else {
                    long days = limitTime - intervalTime;
                    long hours = (limitTime - intervalTime) * 24 - days * 24;
                    superviseInfo.put("timeStatus1", "还剩" + days + "天" + hours + "小时");
                }
            } else {
                superviseInfo.put("timeStatus1", "已逾期");
            }

        }

        request.setAttribute("superviseInfo", superviseInfo);
        //获取反馈信息(只需获取最新一条)
        Map<String, Object> feedbackMap = new HashMap<>();
        List<Map<String, Object>> feedbackInfoList = this.getFeedbackInfo(id, request.getParameter("nodeId"));
        if (feedbackInfoList.size() == 0 || StringUtils.isEmpty((String) feedbackInfoList.get(0).get("APPROVE_ID")) ||
                "2".equals(String.valueOf(feedbackInfoList.get(0).get("APPROVE_RESULT")))) {
            //承办人尚未提交 部门负责人尚未审批 办结反馈申请
            feedbackMap.put("dataStatus", "1");
        } else {
            feedbackMap = feedbackInfoList.get(0);
            if (StringUtils.isNotEmpty((String) feedbackInfoList.get(0).get("RECORD_ID"))) {
                //承办人 有最新反馈 回显承办人反馈信息 并且立项人已回复 回显回复信息 禁用按钮  状态为1页面启用 只读按钮
                feedbackMap.put("dataStatus", "1");
            } else {
                //承办人 有最新反馈  回显承办人反馈信息 立项人无回复
                feedbackMap.put("dataStatus", "2");
            }
        }
        request.setAttribute("feedback", feedbackMap);
    }

    /**
     * 获取反馈信息
     *
     * @param recordId
     * @param nodeId
     * @return
     */
    private List<Map<String, Object>> getFeedbackInfo(String recordId, String nodeId) {
        StringBuffer querySql = new StringBuffer();
        querySql.append("SELECT T1.RECORD_ID AS FEEDBACK_ID,T1.CREATE_TIME,T1.FEEDBACK_CONTENT,T1.FILE_URL,T1.REMARKS," +
                " T2.RECORD_ID,T2.CREATE_TIME AS END_TIME ,T1.FILE_NAME,T1.FILE_URL,T2.REPLY_CONTENT,T2.AGREE,T3.RECORD_ID AS APPROVE_ID,T3.APPROVE_RESULT " +
                "  FROM tb_supervise_feedback T1 \n" +
                " LEFT JOIN tb_supervise_reply T2 ON T1.RECORD_ID=T2.FEEDBACK_ID " +
                " LEFT JOIN tb_approve_record T3 ON T1.RECORD_ID = T3.FEEDBACK_ID AND T1.NODE_ID=T3.APPROVE_NODE_ID " +
                " WHERE T1.SUPERVISE_ID=? AND T1.NODE_ID=? ORDER BY T1.CREATE_TIME DESC");
        List<Map<String, Object>> superviseInfoByRecordId =
                sponsorService.findBySql(querySql.toString(), new Object[]{recordId, nodeId}, null);
        return superviseInfoByRecordId;
    }

    /**
     * 立项人处理反馈
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> sponsorHandleFeedback(HttpServletRequest request) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        String isPass = (String) params.get("isPass");
        String nodeId = (String) params.get("NODE_ID");
        String superviseId = (String) params.get("SUPERVISE_ID");
        String FEEDBACK_ID = (String) params.get("FEEDBACK_ID");
        //获取督办事项信息、 获取部门督办任务信息
        Map<String, Object> temp = new HashMap<>();
        temp.put("SUPERVISE_ID", superviseId);
        //根据当前登录用户部门和督办编号获取督办任务ID
        Map<String, Object> task = dao.getRecord("tb_supervise_task", new String[]{"SUPERVISE_ID"}, new Object[]{superviseId});
        if (task != null) {
            temp.put("SUPERVISE_TASK_ID", task.get("RECORD_ID"));
        }
        temp.put("REPLY_CONTENT", params.get("REPLY_CONTENT"));
        temp.put("FEEDBACK_ID", FEEDBACK_ID);
        temp.put("CREATE_BY", PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID"));
        temp.put("CREATE_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        temp.put("DEL_FLAG", 1);
        temp.put("AGREE", isPass);
        temp.put("NODE_ID", nodeId);
        Map<String, Object> replyMap = dao.saveOrUpdate("tb_supervise_reply", temp, SysConstants.ID_GENERATOR_UUID, null);
        //更新 tb_supervise STATUS CURRENT_NODE 至下一节点
        if ("1".equals(isPass)) {
            Map<String, Object> superviseMap = dao.getRecord("tb_supervise", new String[]{"RECORD_ID"}, new Object[]{superviseId});
            //需要判断 当前节点是否为 最终节点 9
            if (!"9".equals(superviseMap.get("STATUS"))) {
                if (NODE_ID_3.equals(superviseMap.get("CURRENT_NODE"))) {
                    superviseMap.put("STATUS", 8);
                } else if (NODE_ID_5.equals(superviseMap.get("CURRENT_NODE"))) {
                    superviseMap.put("STATUS", 9);
                }
            }
            if (!NODE_ID_5.equals(superviseMap.get("CURRENT_NODE"))) {
                superviseMap.put("CURRENT_NODE", Integer.parseInt((String) superviseMap.get("CURRENT_NODE")) + 2);
            } else {
                //办结反馈节点 更新 任务办结时间等
                superviseMap.put("UPDATE_BY", user.get("SYSUSER_ID"));
                superviseMap.put("UPDATE_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                superviseMap.put("SUPERVISE_COMMENT", params.get("REPLY_CONTENT"));
            }
            superviseMap = dao.saveOrUpdate("tb_supervise", superviseMap, SysConstants.ID_GENERATOR_UUID, null);
            //更新tb_supervise_task  STATUS 至下一节点
            if (!"9".equals(superviseMap.get("STATUS"))) {
                task.put("STATUS", superviseMap.get("STATUS"));
            }
            task = dao.saveOrUpdate("tb_supervise_task", task, SysConstants.ID_GENERATOR_UUID, null);
            //在tb_supervise_node_info 中插入数据
            if (!NODE_ID_5.equals(nodeId)) {
                Map<String, Object> nodeMap = new HashMap<>();
                nodeMap.put("SUPERVISE_ID", superviseId);
                nodeMap.put("SUPERVISE_NO", task.get("SUPERVISE_NO"));
                nodeMap.put("NODE_ID", superviseMap.get("CURRENT_NODE"));
                nodeMap.put("NODE_NAME", "办结反馈");
                nodeMap.put("CREATE_BY", user.get("SYSUSER_ID"));
                nodeMap.put("CREATE_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                dao.saveOrUpdate("tb_supervise_node_info", nodeMap, SysConstants.ID_GENERATOR_UUID, null);
            }
        }
        return replyMap;
    }


    /**
     * 获取当前登录人建立的所有督察督办事项
     * 默认展示全部督办
     * 可以根据前台选择 的全部 正在 待办 完结督办 四种状态 切换
     * <p>
     * 判断 督办确认逾期 1.若确认时间 超过24小时 无论是否完成 页面均显示逾期
     * 2.若确实时间未超时 且 未办理 显示 办理中
     * 3.一切正常 显示完成
     *
     * @return
     */
    @Override
    public List<List<Map<String, Object>>> findSuperviseListByLoginUser(HttpServletRequest request) {

        String status = request.getParameter("status");
        String clazzId = request.getParameter("clazz");
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        List<List<Map<String, Object>>> result = new ArrayList<>();
        List<Map<String, Object>> superviseList;
        if (STATUS_DRAFT.equals(status) || STATUS_ALL.equals(status)) {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT T1.RECORD_ID,T1.CREATE_TIME,T1.KEYWORDS,T1.TITLE,T1.SUPERVISE_CLAZZ," +
                    " T1.SUPERVISE_ITEM,T1.SUPERVISE_SOURCE FROM tb_supervise T1 WHERE T1.`STATUS`='0' AND T1.CREATE_BY=? " +
                    " AND T1.SUPERVISE_CLAZZ_ID=? " +
                    " ORDER BY T1.CREATE_TIME DESC");
            superviseList =
                    sponsorService.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID"), clazzId}, null);
            for (Map<String, Object> map : superviseList) {
                map.put("DRAFT", "1");
            }
            for (int i = 0; i < superviseList.size(); i++) {
                List<Map<String, Object>> temp = new ArrayList<>();
                superviseList.get(i).put("DRAFT", "1");
                temp.add(superviseList.get(i));
                result.add(temp);
            }
        }
        if (!STATUS_DRAFT.equals(status)) {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT RECORD_ID,DEPART_ID,KEYWORDS,HANDLE_LIMIT FROM tb_supervise WHERE CREATE_BY=? AND SUPERVISE_CLAZZ_ID=? ");
            if (STATUS_DONE.equals(status)) {
                sql.append(" AND `STATUS`= '9' ");
            } else if (STATUS_PROCESS.equals(status)) {
                sql.append(" AND `STATUS` NOT IN ('9','0')  ");
            }
            sql.append("ORDER BY CREATE_TIME DESC");
            List<Map<String, Object>> superviseInfoListByUserId =
                    sponsorService.findBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID"), clazzId}, null);

            for (Map<String, Object> map : superviseInfoListByUserId) {
                StringBuffer querySql = new StringBuffer();
                String recordId = map.get("RECORD_ID").toString();
                querySql.append("SELECT T4.RECORD_ID,T2.CREATE_TIME,T1.NODE_NAME,T3.DEPART_ID AS TAKER_DEPART_ID,T4.HANDLE_LIMIT,T1.NODE_ID, " +
                        " T4.DEPART_ID AS COMMIT_DEPART_ID ,T4.KEYWORDS,T4.TITLE,T4.SUPERVISE_CLAZZ,T4.SUPERVISE_ITEM,T4.STATUS,T4.SUPERVISE_NO, " +
                        " T4.CURRENT_NODE,T4.SUPERVISE_SOURCE,T4.SUPERVISE_TIME_TYPE FROM tb_supervise_approve T1 " +
                        " LEFT JOIN tb_supervise_node_info T2 ON T1.NODE_NAME=T2.NODE_NAME  AND T2.SUPERVISE_ID=? " +
                        " LEFT JOIN tb_supervise_task T3 ON T3.SUPERVISE_ID=T2.SUPERVISE_ID" +
                        " LEFT JOIN tb_supervise T4 ON T4.RECORD_ID=T2.SUPERVISE_ID WHERE T1.DEL_FLAG = '1'");
                //发起部门
                List<Map<String, Object>> superviseInfoByRecordId =
                        sponsorService.findBySql(querySql.toString(), new Object[]{recordId}, null);
                Map<String, Object> commitMap = departService.getRecord("plat_system_depart",
                        new String[]{"DEPART_ID"}, new Object[]{map.get("DEPART_ID").toString()});
                //承办部门
                Map<String, Object> takerMap = new HashMap<>();
                if (StringUtils.isNotEmpty((String) superviseInfoByRecordId.get(0).get("RECORD_ID"))) {
                    takerMap = departService.getRecord("plat_system_depart",
                            new String[]{"DEPART_ID"}, new Object[]{superviseInfoByRecordId.get(0).get("TAKER_DEPART_ID").toString()});
                }
                for (Map<String, Object> param : superviseInfoByRecordId) {
                    //分别为 办理反馈 办结反馈 实际办理时间
                    //办理反馈 办结反馈 逾期判断 1.首先判断 固定期限:1,反馈频次:2
                    //                         固定期限  办理反馈  填写的办理时限 与 （督办确认的时间与承办人  第一次  提交的办理反馈 时间差） 比较
                    //                                  办结反馈   填写的办理时限 - 办理反馈时间  与  （办结反馈发起的时间 与 承办人 第一次 提交的办结反馈 时间差 ） 比较
                    //                         反馈频次
                    long intervalTime2 = 0;
                    long intervalTime3 = 0;
                    long intervalTime5 = 0;
                    StringBuilder feedbackSql = new StringBuilder(
                            "SELECT * FROM tb_supervise_feedback WHERE SUPERVISE_ID=? AND NODE_ID=? ORDER BY CREATE_TIME DESC");
                    List<Map<String, Object>> feedbackList3 =
                            sponsorService.findBySql(feedbackSql.toString(), new Object[]{param.get("RECORD_ID"), NODE_ID_3}, null);
                    List<Map<String, Object>> feedbackList5 =
                            sponsorService.findBySql(feedbackSql.toString(), new Object[]{param.get("RECORD_ID"), NODE_ID_5}, null);
                    if (NODE_ID_3.equals(param.get("NODE_ID"))) {
                        if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                            if ("1".equals(param.get("SUPERVISE_TIME_TYPE"))) {
                                if (feedbackList3.size() != 0) {
                                    intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                            (String) feedbackList3.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                                    param.put("feedbackStatus", "1");
                                } else {
                                    intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                            PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                                    param.put("feedbackStatus", "2");
                                }
                            }
                        }
                    }
                    if (NODE_ID_5.equals(param.get("NODE_ID"))) {
                        if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                            if (feedbackList5.size() != 0) {
                                param.put("feedbackStatus", "1");
                                intervalTime5 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                        (String) feedbackList5.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                            } else {
                                intervalTime5 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                        PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                                param.put("feedbackStatus", "2");
                            }
                        }
                    }
                    int limitTime = 0;
                    if (null != param.get("HANDLE_LIMIT")) {
                        limitTime = (Integer) param.get("HANDLE_LIMIT");
                    }
                    //任务发起
                    if (NODE_ID_1.equals(param.get("NODE_ID"))) {
                        //判断是否需要审批
                        param.put("feedbackStatus", "1");
                        param.put("timeStatus", "完成");
                        param.put("COMMIT_DEPART_NAME", commitMap.get("DEPART_NAME"));
                        param.put("KEYWORDS", map.get("KEYWORDS"));
                    }
                    //督办确认
                    if (NODE_ID_2.equals(param.get("NODE_ID"))) {
                        //判断是否需要审批
                        param.put("feedbackStatus", "1");
                        StringBuilder confirmSql = new StringBuilder(
                                "SELECT * FROM tb_supervise_confirm WHERE SUPERVISE_ID=? AND NODE_ID=? ");
                        List<Map<String, Object>> confirmList =
                                this.findBySql(confirmSql.toString(), new Object[]{param.get("RECORD_ID"), NODE_ID_2}, null);
                        //判断 督办确认逾期 1.若确认时间 超过24小时 无论是否完成 页面均显示逾期
                        //               2.若确实时间未超时 且 未办理 显示 办理中
                        //               3.一切正常 显示完成
                        if (confirmList.size() != 0) {
                            //确认时间 是否超过24小时
                            intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                    (String) confirmList.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                            if (intervalTime2 > 24) {
                                param.put("timeStatus", "逾期");
                            } else {
                                param.put("timeStatus", "完成");
                            }
                        } else {
                            intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                    PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                            if (intervalTime2 > 24) {
                                param.put("timeStatus", "逾期");
                            } else {
                                param.put("timeStatus", "未办理");
                            }
                        }
                        param.put("TAKER_DEPART_NAME", takerMap.get("DEPART_NAME"));
                        param.put("KEYWORDS", map.get("KEYWORDS"));
                    }
                    //办理反馈
                    if (NODE_ID_3.equals(param.get("NODE_ID"))) {
                        if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                            //判断逾期否
                            if (Integer.parseInt(String.valueOf(param.get("CURRENT_NODE"))) > 3) {
                                param.put("timeStatus", "完成");
                            } else {
                                if (intervalTime3 <= 72) {
                                    param.put("timeStatus", "办理中");
                                } else {
                                    param.put("timeStatus", "逾期");
                                }
                            }
                        } else {
                            param.put("timeStatus", "未办理");
                        }
                        param.put("TAKER_DEPART_NAME", takerMap.get("DEPART_NAME"));
                        param.put("KEYWORDS", map.get("KEYWORDS"));
                    }
                    //办结反馈
                    if (NODE_ID_5.equals(param.get("NODE_ID"))) {
                        if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                            if (9 == Integer.parseInt(String.valueOf(param.get("STATUS")))) {
                                param.put("timeStatus", "完成");
                            } else {
                                //需要 去除 第三 第四阶段实际 消耗时间 intervalTime3 intervalTime4
                                if (intervalTime5 <= (limitTime * 24 - intervalTime3)) {
                                    param.put("timeStatus", "办理中");
                                } else {
                                    param.put("timeStatus", "逾期");
                                }
                            }
                        } else {
                            param.put("timeStatus", "未办理");
                        }
                    }
                    param.put("TAKER_DEPART_NAME", takerMap.get("DEPART_NAME"));
                    param.put("KEYWORDS", map.get("KEYWORDS"));
                }
                //草稿没有文书号
                if (superviseInfoByRecordId.get(0).get("SUPERVISE_NO") != null) {
                    result.add(superviseInfoByRecordId);
                }
            }
        }
        return result;
    }

    /**
     * 下载 立项人上传附件
     *
     * @param request
     */
    @Override
    public Map<String, Object> downloadSponsorFile(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        List<String> downloadList = new ArrayList<>();
        String recordId = request.getParameter("recordId");
        //根据ID获取督办事项详情
        StringBuffer sql = new StringBuffer("SELECT FILE_PATH FROM plat_system_fileattach T1 WHERE T1.FILE_BUSRECORDID=? ");
        List<Map<String, Object>> superviseList = sponsorService.findBySql(sql.toString(), new Object[]{recordId}, null);
        if (superviseList.size() == 0) {
            result.put("success", false);
            result.put("msg", "当前督察督办任务未上传附件");
        } else {
            String srcPath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
            String srcDownPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + File.separator + "attachfiles" + File.separator;
            String zipFileName = PlatDateTimeUtil.formatDate(new Date(), "yyyyMMddHHmmss");
            List<String> fileUrlList = new ArrayList<>();
            for (Map<String, Object> map : superviseList) {
                fileUrlList.add(srcPath + map.get("FILE_PATH"));
            }
            if (fileUrlList.size() > 0) {
                String destPath = request.getSession().getServletContext().getRealPath("/") + "attachfiles" + File.separator + "download";
                String zipPath = null;
                try {
                    zipPath = PlatZipUtil.listToZip(fileUrlList, zipFileName, destPath);
                    downloadList.add(srcDownPath + "download" + File.separator + PlatFileUtil.getNamePart(zipPath));
                    result.put("success", true);
                    result.put("list", downloadList);
                    result.put("msg", "文件下载成功！");

                } catch (IOException e) {
                    result.put("success", false);
                    result.put("msg", "文件下载失败！");
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 加载事项办结(立项人)的数据
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map<String, Object>> findSponsorSupEndList(SqlFilter sqlFilter) {
        String userId = (String) PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID");
        String KEYWORDS = sqlFilter.getRequest().getParameter("KEYWORDS");
        String TITLE = sqlFilter.getRequest().getParameter("TITLE");
        String status = sqlFilter.getRequest().getParameter("status");
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T1.RECORD_ID,T1.SUPERVISE_NO,T1.SUPERVISE_CLAZZ,T1.TITLE,T1.KEYWORDS,T3.DEPART_NAME FROM tb_supervise T1" +
                " LEFT JOIN tb_supervise_task T2 ON T1.RECORD_ID=T2.SUPERVISE_ID" +
                " LEFT JOIN plat_system_depart T3 ON T2.DEPART_ID=T3.DEPART_ID" +
                " WHERE T1.CURRENT_NODE='5' AND T1.CREATE_BY=? AND T1.STATUS IN ");
        sql.append(PlatStringUtil.getSqlInCondition(status));
        if (StringUtils.isNotEmpty(KEYWORDS) && StringUtils.isNotEmpty(TITLE)) {
            sql.append("AND T1.KEYWORDS LIKE ? AND T1.TITLE LIKE ? ");
            sql.append("ORDER BY T1.CREATE_TIME DESC ");
            return superviseClazzService.findBySql(sql.toString(), new Object[]{userId, "%" + KEYWORDS + "%", "%" + TITLE + "%"}, sqlFilter.getPagingBean());
        } else if (StringUtils.isNotEmpty(KEYWORDS) && StringUtils.isEmpty(TITLE)) {
            sql.append("AND T1.KEYWORDS LIKE ? ");
            sql.append("ORDER BY T1.CREATE_TIME DESC ");
            return superviseClazzService.findBySql(sql.toString(), new Object[]{userId, "%" + KEYWORDS + "%"}, sqlFilter.getPagingBean());
        } else if (StringUtils.isEmpty(KEYWORDS) && StringUtils.isNotEmpty(TITLE)) {
            sql.append("AND T1.TITLE LIKE ? ");
            sql.append("ORDER BY T1.CREATE_TIME DESC ");
            return superviseClazzService.findBySql(sql.toString(), new Object[]{userId, "%" + TITLE + "%"}, sqlFilter.getPagingBean());
        } else {
            sql.append("ORDER BY T1.CREATE_TIME DESC ");
            return superviseClazzService.findBySql(sql.toString(), new Object[]{userId}, sqlFilter.getPagingBean());
        }
    }

    /**
     * 加载事项办结详情页（立项人）数据
     *
     * @param request
     */
    @Override
    public void goSponsorSuperviseEndInfo(HttpServletRequest request) {
        String recordId = request.getParameter("recordId");
        request.setAttribute("nodeId", "5");
        //根据ID获取督办事项详情
        Map<String, Object> supervise = sponsorService.getRecord("tb_supervise", new String[]{"RECORD_ID"}, new Object[]{recordId});
        //获取承办部门
        Map<String, Object> task = sponsorService.getRecord("tb_supervise_task", new String[]{"SUPERVISE_ID"}, new Object[]{recordId});
        supervise.put("TASK_ID", task.get("RECORD_ID"));
        String departNameTaker = (String) sponsorService.getRecord("plat_system_depart", new String[]{"DEPART_ID"}, new Object[]{task.get("DEPART_ID")}).get("DEPART_NAME");
        supervise.put("TAKER_DEPART_NAME", departNameTaker);
        //获取发起部门
        String departNameSponsor = (String) sponsorService.getRecord("plat_system_depart", new String[]{"DEPART_ID"}, new Object[]{supervise.get("DEPART_ID")}).get("DEPART_NAME");
        supervise.put("SPONSOR_DEPART_NAME", departNameSponsor);
        //获取最新的 承办人 反馈信息
        //未办结
        List<Map<String, Object>> feedbackInfoList = this.getFeedbackInfo(recordId, "5");
        Map<String, Object> endMap = new HashMap<>();
        if (feedbackInfoList.size() == 0 || StringUtils.isEmpty((String) feedbackInfoList.get(0).get("APPROVE_ID")) ||
                "2".equals((String) feedbackInfoList.get(0).get("APPROVE_RESULT"))) {
            //承办人尚未提交 部门负责人尚未审批 部门负责人未同意 办结反馈申请
            endMap.put("status", "1");
        } else {
            //判断此条状态是否处于完结状态（办结反馈无驳回）
            //发起人已经回复 此任务已办结
            if (StringUtils.isNotEmpty((String) feedbackInfoList.get(0).get("RECORD_ID"))) {
                endMap.put("status", "2");
                //获取办结时间
                endMap.put("CREATE_TIME", feedbackInfoList.get(0).get("CREATE_TIME"));
                endMap.put("END_TIME", feedbackInfoList.get(0).get("END_TIME"));
                endMap.put("FEEDBACK_CONTENT", feedbackInfoList.get(0).get("FEEDBACK_CONTENT"));
                endMap.put("REMARKS", feedbackInfoList.get(0).get("REMARKS"));
                endMap.put("FILE_NAME", feedbackInfoList.get(0).get("FILE_NAME"));
                endMap.put("FILE_URL", feedbackInfoList.get(0).get("FILE_URL"));
                endMap.put("ACCEPT_TIME", feedbackInfoList.get(0).get("CREATE_TIME"));
                endMap.put("RECORD_ID", feedbackInfoList.get(0).get("FEEDBACK_ID"));
                endMap.put("REPLY_CONTENT", feedbackInfoList.get(0).get("REPLY_CONTENT"));
            } else {
                endMap.put("status", "3");
                //获取办结申请相关内容
                endMap.put("CREATE_TIME", feedbackInfoList.get(0).get("CREATE_TIME"));
                endMap.put("FEEDBACK_CONTENT", feedbackInfoList.get(0).get("FEEDBACK_CONTENT"));
                endMap.put("REMARKS", feedbackInfoList.get(0).get("REMARKS"));
                endMap.put("FILE_NAME", feedbackInfoList.get(0).get("FILE_NAME"));
                endMap.put("FILE_URL", feedbackInfoList.get(0).get("FILE_URL"));
                endMap.put("ACCEPT_TIME", feedbackInfoList.get(0).get("CREATE_TIME"));
                endMap.put("RECORD_ID", feedbackInfoList.get(0).get("FEEDBACK_ID"));
            }

        }
        //获取 办理反馈 落实反馈 相关信息
        List<Map<String, Object>> feedbackInfoList3 = this.getFeedbackInfo(recordId, "3");

        Map<String, Object> feedbackInfo3 = feedbackInfoList3.get(0);

        feedbackInfo3.put("ACCEPT_TIME", feedbackInfoList3.get(feedbackInfoList3.size() - 1).get("CREATE_TIME"));

        request.setAttribute("supervise", supervise);
        request.setAttribute("endMap", endMap);
        request.setAttribute("feedbackInfo3", feedbackInfo3);

    }


    /**
     * 根据指定 id 获取督办类型（用于FORM表单 自动补充 督办类型）
     *
     * @param clazz
     * @return
     * @created 2016年3月27日 上午11:16:25
     */
    @Override
    public List<Map<String, Object>> findClazzList(String clazz) {
        StringBuffer sql = new StringBuffer("SELECT T.RECORD_ID AS VALUE,T.CLAZZ_NAME AS LABEL FROM tb_supervise_clazz T WHERE T.RECORD_ID=? ");
        return dao.findBySql(sql.toString(), new Object[]{clazz}, null);
    }

    /**
     * 立项人终止当前项目
     */
    @Override
    public void stopSupervise(HttpServletRequest request) {
        String superviseId = request.getParameter("superviseId");
        Map<String, Object> superviseMap = new HashMap<>();
        superviseMap.put("RECORD_ID", superviseId);
        superviseMap.put("STATUS", "10");
        this.saveOrUpdate("TB_SUPERVISE",
                superviseMap, SysConstants.ID_GENERATOR_UUID, null);

    }


}
