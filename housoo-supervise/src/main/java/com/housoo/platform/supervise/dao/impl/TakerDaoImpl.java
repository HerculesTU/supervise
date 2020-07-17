/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.dao.impl;

import com.housoo.platform.core.dao.impl.BaseDaoImpl;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.supervise.dao.TakerDao;
import com.housoo.platform.supervise.service.SuperviseClazzService;
import com.housoo.platform.supervise.service.TakerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 承办人业务相关dao实现类
 *
 * @author cjr
 * @version 1.0
 * @created 2020-04-14 15:30:11
 */
@Repository
public class TakerDaoImpl extends BaseDaoImpl implements TakerDao {

    /**
     * SuperviseClazzService
     */
    @Resource
    private SuperviseClazzService superviseClazzService;

    @Override
    public Map<String, Object> getTakerIndexNumericDataByYear(Map<String, Object> params) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) ALLSUM,");
        sql.append("(SELECT COUNT(*) INGSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.STATUS NOT IN (0,9,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW())) INGSUM,");
        sql.append("(SELECT COUNT(*) FINISHSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.STATUS = 9 AND YEAR(s.CREATE_TIME) = YEAR(NOW()) ) FINISHSUM,");
        sql.append("(SELECT COUNT(*) OUTDATESUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S. STATUS NOT IN (0,9,10) AND YEAR (s.CREATE_TIME) = YEAR (NOW()) AND DATE_FORMAT(DATE_ADD(s.CREATE_TIME,INTERVAL s.HANDLE_LIMIT DAY),'%Y-%m-%d %H-%i-%s') < DATE_FORMAT(now(),'%Y-%m-%d %H-%i-%s')) OUTDATESUM ");
        sql.append("FROM tb_supervise S ");
        sql.append("LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
        sql.append("WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.STATUS NOT IN (0,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) ");
        return this.getBySql(sql.toString(), new Object[]{});
    }

    @Override
    public Map<String, Object> getTakerIndexNumericDataByQuarter(Map<String, Object> params) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) ALLSUM,");
        sql.append("(SELECT COUNT(*) INGSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.STATUS NOT IN (0,9,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND QUARTER(s.CREATE_TIME) = QUARTER(NOW())) INGSUM,");
        sql.append("(SELECT COUNT(*) FINISHSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.STATUS = 9 AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND QUARTER(s.CREATE_TIME) = QUARTER(NOW())) FINISHSUM,");
        sql.append("(SELECT COUNT(*) OUTDATESUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S. STATUS NOT IN (0,9,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND QUARTER(s.CREATE_TIME) = QUARTER(NOW()) AND DATE_FORMAT(DATE_ADD(s.CREATE_TIME,INTERVAL s.HANDLE_LIMIT DAY),'%Y-%m-%d %H-%i-%s') < DATE_FORMAT(now(),'%Y-%m-%d %H-%i-%s')) OUTDATESUM ");
        sql.append("FROM tb_supervise S ");
        sql.append("LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
        sql.append("WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.STATUS NOT IN (0,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND QUARTER(s.CREATE_TIME) = QUARTER(NOW()) ");
        return this.getBySql(sql.toString(), new Object[]{});
    }

    @Override
    public Map<String, Object> getTakerIndexNumericDataByMonth(Map<String, Object> params) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) ALLSUM,");
        sql.append("(SELECT COUNT(*) INGSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.STATUS NOT IN (0,9,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND MONTH(s.CREATE_TIME) = MONTH(NOW())) INGSUM,");
        sql.append("(SELECT COUNT(*) FINISHSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.STATUS = 9 AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND MONTH(s.CREATE_TIME) = MONTH(NOW())) FINISHSUM,");
        sql.append("(SELECT COUNT(*) OUTDATESUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S. STATUS NOT IN (0,9,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND MONTH(s.CREATE_TIME) = MONTH(NOW()) AND DATE_FORMAT(DATE_ADD(s.CREATE_TIME,INTERVAL s.HANDLE_LIMIT DAY),'%Y-%m-%d %H-%i-%s') < DATE_FORMAT(now(),'%Y-%m-%d %H-%i-%s')) OUTDATESUM ");
        sql.append("FROM tb_supervise S ");
        sql.append("LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
        sql.append("WHERE T.USER_ID IN  ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.STATUS NOT IN (0,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND MONTH(s.CREATE_TIME) = MONTH(NOW()) ");
        return this.getBySql(sql.toString(), new Object[]{});
    }

    @Override
    public Map<String, Object> getTakerIndexNumericDataByDays(Map<String, Object> params) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) ALLSUM,");
        sql.append("(SELECT COUNT(*) INGSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.STATUS NOT IN (0,9,10) AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(S.CREATE_TIME)) INGSUM,");
        sql.append("(SELECT COUNT(*) FINISHSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.STATUS = 9 AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(S.CREATE_TIME)) FINISHSUM,");
        sql.append("(SELECT COUNT(*) OUTDATESUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S. STATUS NOT IN (0,9,10) AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(S.CREATE_TIME) AND DATE_FORMAT(DATE_ADD(s.CREATE_TIME,INTERVAL s.HANDLE_LIMIT DAY),'%Y-%m-%d %H-%i-%s') < DATE_FORMAT(now(),'%Y-%m-%d %H-%i-%s')) OUTDATESUM ");
        sql.append("FROM tb_supervise S ");
        sql.append("LEFT JOIN tb_supervise_task T on S.RECORD_ID = T.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
        sql.append("WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.STATUS NOT IN (0,10) AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(s.CREATE_TIME) ");
        return this.getBySql(sql.toString(), new Object[]{});
    }

    @Override
    public Map<String, Object> getTakerIndexNumericDataByRangeTime(Map<String, Object> params) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) ALLSUM,");
        sql.append("(SELECT COUNT(*) INGSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
            sql.append(" AND date_format(s.CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(s.CREATE_TIME,'%Y-%m-%d') <= ? ");
        }
        sql.append(" AND S.STATUS NOT IN (0,9,10) ) INGSUM,");
        sql.append("(SELECT COUNT(*) FINISHSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
            sql.append(" AND date_format(s.CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(s.CREATE_TIME,'%Y-%m-%d') <= ? ");
        }
        sql.append(" AND S.STATUS = 9 ) FINISHSUM,");
        sql.append("(SELECT COUNT(*) OUTDATESUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
            sql.append(" AND date_format(s.CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(s.CREATE_TIME,'%Y-%m-%d') <= ? ");
        }
        sql.append(" AND S. STATUS NOT IN (0,9,10) AND DATE_FORMAT(DATE_ADD(s.CREATE_TIME,INTERVAL s.HANDLE_LIMIT DAY),'%Y-%m-%d %H-%i-%s') < DATE_FORMAT(now(),'%Y-%m-%d %H-%i-%s')) OUTDATESUM ");
        sql.append("FROM tb_supervise S ");
        sql.append("LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
        sql.append("WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.STATUS NOT IN (0,10) ");
        if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
            sql.append("AND date_format(s.CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(s.CREATE_TIME,'%Y-%m-%d') <= ? ");
        }
        if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
            return this.getBySql(sql.toString(), new Object[]{params.get("startDate"), params.get("endDate"), params.get("startDate"), params.get("endDate"), params.get("startDate"), params.get("endDate"), params.get("startDate"), params.get("endDate")});
        } else {
            return this.getBySql(sql.toString(), new Object[]{});
        }
    }

    @Override
    public List<Map<String, Object>> findTaskListBySuperviseId(String superviseId) {
        StringBuffer sql = new StringBuffer("select t.RECORD_ID,t.DEPART_ID,d.DEPART_NAME,t.STATUS ");
        sql.append("from tb_supervise_task t ");
        sql.append("left join plat_system_depart d on t.DEPART_ID = d.DEPART_ID ");
        sql.append("where t.SUPERVISE_ID = ? and t.SUPERVISE_NO IS NOT NULL and t.STATUS NOT IN (0,10) ");
        sql.append("order by t.CREATE_TIME asc ");
        return this.findBySql(sql.toString(), new Object[]{superviseId}, null);
    }

    @Override
    public List<Map<String, Object>> findTaskNodeListBySuperviseId(String superviseId) {
        StringBuffer sql = new StringBuffer("select a.NODE_ID,a.NODE_NAME,temp.RECORD_ID,temp.NODE_CONTENT,temp.CREATE_TIME,a.SHORT_NAME,a.NEED_APPROVE_FLAG ");
        sql.append("from tb_supervise_approve a ");
        sql.append("LEFT JOIN (SELECT RECORD_ID,NODE_CONTENT,CREATE_TIME,NODE_ID from tb_supervise_node_info where SUPERVISE_ID= ? ) temp ");
        sql.append("on a.NODE_ID = temp.NODE_ID ");
        sql.append("WHERE a.DEL_FLAG = '1' ");
        sql.append("ORDER BY a.NODE_ID ASC ");
        return this.findBySql(sql.toString(), new Object[]{superviseId}, null);
    }

    @Override
    public List<Map<String, Object>> getTakerIndexPieDataByYear(Map<String, Object> params) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) ALLSUM,");
        sql.append("(SELECT COUNT(*) INGSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? ");
        sql.append("AND S.STATUS NOT IN (0,9,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW())) INGSUM,");
        sql.append("(SELECT COUNT(*) FINISHSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? ");
        sql.append("AND S.STATUS = 9 AND YEAR(s.CREATE_TIME) = YEAR(NOW()) ) FINISHSUM,");
        sql.append("(SELECT COUNT(*) OUTDATESUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? ");
        sql.append("AND S. STATUS NOT IN (0,9,10) AND YEAR (s.CREATE_TIME) = YEAR (NOW()) AND DATE_FORMAT(DATE_ADD(s.CREATE_TIME,INTERVAL s.HANDLE_LIMIT DAY),'%Y-%m-%d %H-%i-%s') < DATE_FORMAT(now(),'%Y-%m-%d %H-%i-%s')) OUTDATESUM ");
        sql.append("FROM tb_supervise S ");
        sql.append("LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
        sql.append("WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? AND S.STATUS NOT IN (0,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) ");
        //统计五种督办类型占比
        List<Map<String, Object>> clazzes = superviseClazzService.getAllSuperviseClazz(null);
        if (!clazzes.isEmpty()) {
            for (Map<String, Object> clazz : clazzes) {
                String clazzId = (String) clazz.get("VALUE");
                Map<String, Object> temp = new HashMap<>();
                temp = this.getBySql(sql.toString(), new Object[]{clazzId, clazzId, clazzId, clazzId});
                clazz.put("data", temp);
            }
            return clazzes;
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getTakerIndexPieDataByQuarter(Map<String, Object> params) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) ALLSUM,");
        sql.append("(SELECT COUNT(*) INGSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? ");
        sql.append("AND S.STATUS NOT IN (0,9,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND QUARTER(s.CREATE_TIME) = QUARTER(NOW())) INGSUM,");
        sql.append("(SELECT COUNT(*) FINISHSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? ");
        sql.append("AND S.STATUS = 9 AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND QUARTER(s.CREATE_TIME) = QUARTER(NOW())) FINISHSUM,");
        sql.append("(SELECT COUNT(*) OUTDATESUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? ");
        sql.append("AND S. STATUS NOT IN (0,9,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND QUARTER(s.CREATE_TIME) = QUARTER(NOW()) AND DATE_FORMAT(DATE_ADD(s.CREATE_TIME,INTERVAL s.HANDLE_LIMIT DAY),'%Y-%m-%d %H-%i-%s') < DATE_FORMAT(now(),'%Y-%m-%d %H-%i-%s')) OUTDATESUM ");
        sql.append("FROM tb_supervise S ");
        sql.append("LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
        sql.append("WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? AND S.STATUS NOT IN (0,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND QUARTER(s.CREATE_TIME) = QUARTER(NOW()) ");
        //统计五种督办类型占比
        List<Map<String, Object>> clazzes = superviseClazzService.getAllSuperviseClazz(null);
        if (!clazzes.isEmpty()) {
            for (Map<String, Object> clazz : clazzes) {
                String clazzId = (String) clazz.get("VALUE");
                Map<String, Object> temp = new HashMap<>();
                temp = this.getBySql(sql.toString(), new Object[]{clazzId, clazzId, clazzId, clazzId});
                clazz.put("data", temp);
            }
            return clazzes;
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getTakerIndexPieDataByMonth(Map<String, Object> params) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) ALLSUM,");
        sql.append("(SELECT COUNT(*) INGSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? AND S.STATUS NOT IN (0,9,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND MONTH(s.CREATE_TIME) = MONTH(NOW())) INGSUM,");
        sql.append("(SELECT COUNT(*) FINISHSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? AND S.STATUS = 9 AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND MONTH(s.CREATE_TIME) = MONTH(NOW())) FINISHSUM,");
        sql.append("(SELECT COUNT(*) OUTDATESUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? AND S. STATUS NOT IN (0,9,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND MONTH(s.CREATE_TIME) = MONTH(NOW()) AND DATE_FORMAT(DATE_ADD(s.CREATE_TIME,INTERVAL s.HANDLE_LIMIT DAY),'%Y-%m-%d %H-%i-%s') < DATE_FORMAT(now(),'%Y-%m-%d %H-%i-%s')) OUTDATESUM ");
        sql.append("FROM tb_supervise S ");
        sql.append("LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
        sql.append("WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? AND S.STATUS NOT IN (0,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND MONTH(s.CREATE_TIME) = MONTH(NOW()) ");
        //统计五种督办类型占比
        List<Map<String, Object>> clazzes = superviseClazzService.getAllSuperviseClazz(null);
        if (!clazzes.isEmpty()) {
            for (Map<String, Object> clazz : clazzes) {
                String clazzId = (String) clazz.get("VALUE");
                Map<String, Object> temp = new HashMap<>();
                temp = this.getBySql(sql.toString(), new Object[]{clazzId, clazzId, clazzId, clazzId});
                clazz.put("data", temp);
            }
            return clazzes;
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getTakerIndexPieDataByDays(Map<String, Object> params) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) ALLSUM,");
        sql.append("(SELECT COUNT(*) INGSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? AND S.STATUS NOT IN (0,9,10) AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(S.CREATE_TIME)) INGSUM,");
        sql.append("(SELECT COUNT(*) FINISHSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? AND S.STATUS = 9 AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(S.CREATE_TIME)) FINISHSUM,");
        sql.append("(SELECT COUNT(*) OUTDATESUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? AND S. STATUS NOT IN (0,9,10) AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(S.CREATE_TIME) AND DATE_FORMAT(DATE_ADD(s.CREATE_TIME,INTERVAL s.HANDLE_LIMIT DAY),'%Y-%m-%d %H-%i-%s') < DATE_FORMAT(now(),'%Y-%m-%d %H-%i-%s')) OUTDATESUM ");
        sql.append("FROM tb_supervise S ");
        sql.append("LEFT JOIN tb_supervise_task T on S.RECORD_ID = T.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
        sql.append("WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? AND S.STATUS NOT IN (0,10) AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(s.CREATE_TIME) ");
        //统计五种督办类型占比
        List<Map<String, Object>> clazzes = superviseClazzService.getAllSuperviseClazz(null);
        if (!clazzes.isEmpty()) {
            for (Map<String, Object> clazz : clazzes) {
                String clazzId = (String) clazz.get("VALUE");
                Map<String, Object> temp = new HashMap<>();
                temp = this.getBySql(sql.toString(), new Object[]{clazzId, clazzId, clazzId, clazzId});
                clazz.put("data", temp);
            }
            return clazzes;
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getTakerIndexPieDataByRangeTime(Map<String, Object> params) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) ALLSUM,");
        sql.append("(SELECT COUNT(*) INGSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? ");
        if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
            sql.append("AND date_format(s.CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(s.CREATE_TIME,'%Y-%m-%d') <= ? ");
        }
        sql.append("AND S.STATUS NOT IN (0,9,10) ) INGSUM,");
        sql.append("(SELECT COUNT(*) FINISHSUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? ");
        if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
            sql.append("AND date_format(s.CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(s.CREATE_TIME,'%Y-%m-%d') <= ? ");
        }
        sql.append("AND S.STATUS = 9 ) FINISHSUM,");
        sql.append("(SELECT COUNT(*) OUTDATESUM FROM tb_supervise S LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO WHERE T.USER_ID IN");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? ");
        if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
            sql.append("AND date_format(s.CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(s.CREATE_TIME,'%Y-%m-%d') <= ? ");
        }
        sql.append("AND S. STATUS NOT IN (0,9,10) AND DATE_FORMAT(DATE_ADD(s.CREATE_TIME,INTERVAL s.HANDLE_LIMIT DAY),'%Y-%m-%d %H-%i-%s') < DATE_FORMAT(now(),'%Y-%m-%d %H-%i-%s')) OUTDATESUM ");
        sql.append("FROM tb_supervise S ");
        sql.append("LEFT JOIN tb_supervise_task T on S.RECORD_ID = t.SUPERVISE_ID and T.SUPERVISE_NO = S.SUPERVISE_NO ");
        sql.append("WHERE T.USER_ID = ?  ");
        sql.append(PlatStringUtil.getSqlInCondition((String) params.get("SYSUSER_ID")));
        sql.append(" AND S.SUPERVISE_CLAZZ_ID = ? AND S.STATUS NOT IN (0,10) ");
        if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
            sql.append("AND date_format(s.CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(s.CREATE_TIME,'%Y-%m-%d') <= ? ");
        }
        List<Map<String, Object>> clazzes = superviseClazzService.getAllSuperviseClazz(null);
        if (!clazzes.isEmpty()) {
            for (Map<String, Object> clazz : clazzes) {
                String clazzId = (String) clazz.get("VALUE");
                Map<String, Object> temp = new HashMap<>();
                if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
                    temp = this.getBySql(sql.toString(), new Object[]{clazzId, params.get("startDate"), params.get("endDate"), clazzId, params.get("startDate"), params.get("endDate"), clazzId, params.get("startDate"), params.get("endDate"), clazzId, params.get("startDate"), params.get("endDate")});
                } else {
                    temp = this.getBySql(sql.toString(), new Object[]{clazzId, clazzId, clazzId, clazzId});
                }

                clazz.put("data", temp);
            }
            return clazzes;
        }
        //统计五种督办类型占比
        return null;
    }

}
