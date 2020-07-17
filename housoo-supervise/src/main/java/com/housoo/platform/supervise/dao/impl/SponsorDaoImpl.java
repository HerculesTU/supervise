package com.housoo.platform.supervise.dao.impl;

import com.housoo.platform.core.dao.impl.BaseDaoImpl;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.supervise.dao.SponsorDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 描述 督查督办业务 立项人 相关Dao 接口
 *
 * @author zxl
 * @version 1.0
 * @created 2020-04-22 15:30:11
 */
@Repository
public class SponsorDaoImpl extends BaseDaoImpl implements SponsorDao {
    @Override
    public Map<String, Object> getSponsorIndexNumericDataByYear(Map<String, Object> params) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) ALLSUM,");
        sql.append("(SELECT COUNT(*) INGSUM FROM tb_supervise S WHERE S.USER_ID = ? ");
        sql.append("AND S.STATUS NOT IN (0,9,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW())) INGSUM,");
        sql.append("(SELECT COUNT(*) FINISHSUM FROM tb_supervise S WHERE S.USER_ID = ? ");
        sql.append("AND S.STATUS = 9 AND YEAR(s.CREATE_TIME) = YEAR(NOW()) ) FINISHSUM,");
        sql.append("(SELECT COUNT(*) OUTDATESUM FROM tb_supervise S WHERE S.USER_ID = ? ");
        sql.append("AND S. STATUS NOT IN (0,9,10) AND YEAR (s.CREATE_TIME) = YEAR (NOW()) AND DATE_FORMAT(DATE_ADD(s.CREATE_TIME,INTERVAL s.HANDLE_LIMIT DAY),'%Y-%m-%d %H-%i-%s') < DATE_FORMAT(now(),'%Y-%m-%d %H-%i-%s')) OUTDATESUM ");
        sql.append("FROM tb_supervise S ");
        sql.append("WHERE S.USER_ID = ? AND S.STATUS NOT IN (0,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) ");
        return this.getBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID"), user.get("SYSUSER_ID"), user.get("SYSUSER_ID"), user.get("SYSUSER_ID")});
    }

    @Override
    public Map<String, Object> getSponsorIndexNumericDataByQuarter(Map<String, Object> params) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) ALLSUM,");
        sql.append("(SELECT COUNT(*) INGSUM FROM tb_supervise S WHERE S.USER_ID = ? ");
        sql.append("AND S.STATUS NOT IN (0,9,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND QUARTER(s.CREATE_TIME) = QUARTER(NOW())) INGSUM,");
        sql.append("(SELECT COUNT(*) FINISHSUM FROM tb_supervise S WHERE S.USER_ID = ? ");
        sql.append("AND S.STATUS = 9 AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND QUARTER(s.CREATE_TIME) = QUARTER(NOW())) FINISHSUM,");
        sql.append("(SELECT COUNT(*) OUTDATESUM FROM tb_supervise S WHERE S.USER_ID = ? ");
        sql.append("AND S. STATUS NOT IN (0,9,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND QUARTER(s.CREATE_TIME) = QUARTER(NOW()) AND DATE_FORMAT(DATE_ADD(s.CREATE_TIME,INTERVAL s.HANDLE_LIMIT DAY),'%Y-%m-%d %H-%i-%s') < DATE_FORMAT(now(),'%Y-%m-%d %H-%i-%s')) OUTDATESUM ");
        sql.append("FROM tb_supervise S ");
        sql.append("WHERE S.USER_ID = ? AND S.STATUS NOT IN (0,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND QUARTER(s.CREATE_TIME) = QUARTER(NOW()) ");
        return this.getBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID"), user.get("SYSUSER_ID"), user.get("SYSUSER_ID"), user.get("SYSUSER_ID")});
    }

    @Override
    public Map<String, Object> getSponsorIndexNumericDataByMonth(Map<String, Object> params) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) ALLSUM,");
        sql.append("(SELECT COUNT(*) INGSUM FROM tb_supervise S WHERE S.USER_ID = ? AND S.STATUS NOT IN (0,9,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND MONTH(s.CREATE_TIME) = MONTH(NOW())) INGSUM,");
        sql.append("(SELECT COUNT(*) FINISHSUM FROM tb_supervise S WHERE S.USER_ID = ? AND S.STATUS = 9 AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND MONTH(s.CREATE_TIME) = MONTH(NOW())) FINISHSUM,");
        sql.append("(SELECT COUNT(*) OUTDATESUM FROM tb_supervise S WHERE S.USER_ID = ? AND S. STATUS NOT IN (0,9,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND MONTH(s.CREATE_TIME) = MONTH(NOW()) AND DATE_FORMAT(DATE_ADD(s.CREATE_TIME,INTERVAL s.HANDLE_LIMIT DAY),'%Y-%m-%d %H-%i-%s') < DATE_FORMAT(now(),'%Y-%m-%d %H-%i-%s')) OUTDATESUM ");
        sql.append("FROM tb_supervise S ");
        sql.append("WHERE S.USER_ID = ? AND S.STATUS NOT IN (0,10) AND YEAR(s.CREATE_TIME) = YEAR(NOW()) AND MONTH(s.CREATE_TIME) = MONTH(NOW()) ");
        return this.getBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID"), user.get("SYSUSER_ID"), user.get("SYSUSER_ID"), user.get("SYSUSER_ID")});
    }

    @Override
    public Map<String, Object> getSponsorIndexNumericDataByDays(Map<String, Object> params) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) ALLSUM,");
        sql.append("(SELECT COUNT(*) INGSUM FROM tb_supervise S WHERE S.USER_ID = ? AND S.STATUS NOT IN (0,9,10) AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(S.CREATE_TIME)) INGSUM,");
        sql.append("(SELECT COUNT(*) FINISHSUM FROM tb_supervise S WHERE S.USER_ID = ? AND S.STATUS = 9 AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(S.CREATE_TIME)) FINISHSUM,");
        sql.append("(SELECT COUNT(*) OUTDATESUM FROM tb_supervise S WHERE S.USER_ID = ? AND S. STATUS NOT IN (0,9,10) AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(S.CREATE_TIME) AND DATE_FORMAT(DATE_ADD(s.CREATE_TIME,INTERVAL s.HANDLE_LIMIT DAY),'%Y-%m-%d %H-%i-%s') < DATE_FORMAT(now(),'%Y-%m-%d %H-%i-%s')) OUTDATESUM ");
        sql.append("FROM tb_supervise S ");
        sql.append("WHERE S.USER_ID = ? AND S.STATUS NOT IN (0,10) AND DATE_SUB(CURDATE(), INTERVAL 6 DAY) <= date(s.CREATE_TIME) ");
        return this.getBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID"), user.get("SYSUSER_ID"), user.get("SYSUSER_ID"), user.get("SYSUSER_ID")});
    }

    @Override
    public Map<String, Object> getSponsorIndexNumericDataByRangeTime(Map<String, Object> params) {
        Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) ALLSUM,");
        sql.append("(SELECT COUNT(*) INGSUM FROM tb_supervise S WHERE S.USER_ID = ? ");
        if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
            sql.append("AND date_format(s.CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(s.CREATE_TIME,'%Y-%m-%d') <= ? ");
        }
        sql.append("AND S.STATUS NOT IN (0,9,10) ) INGSUM,");
        sql.append("(SELECT COUNT(*) FINISHSUM FROM tb_supervise S WHERE S.USER_ID = ? ");
        if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
            sql.append("AND date_format(s.CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(s.CREATE_TIME,'%Y-%m-%d') <= ? ");
        }
        sql.append("AND S.STATUS = 9 ) FINISHSUM,");
        sql.append("(SELECT COUNT(*) OUTDATESUM FROM tb_supervise S WHERE S.USER_ID = ? ");
        if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
            sql.append("AND date_format(s.CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(s.CREATE_TIME,'%Y-%m-%d') <= ? ");
        }
        sql.append("AND S. STATUS NOT IN (0,9,10) AND DATE_FORMAT(DATE_ADD(s.CREATE_TIME,INTERVAL s.HANDLE_LIMIT DAY),'%Y-%m-%d %H-%i-%s') < DATE_FORMAT(now(),'%Y-%m-%d %H-%i-%s')) OUTDATESUM ");
        sql.append("FROM tb_supervise S ");
        sql.append("WHERE S.USER_ID = ? AND S.STATUS NOT IN (0,10)  ");
        if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
            sql.append("AND date_format(s.CREATE_TIME,'%Y-%m-%d') >= ? AND date_format(s.CREATE_TIME,'%Y-%m-%d') <= ? ");
        }
        if (StringUtils.isNotEmpty((String) params.get("startDate")) && StringUtils.isNotEmpty((String) params.get("endDate"))) {
            return this.getBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID"), params.get("startDate"), params.get("endDate"), user.get("SYSUSER_ID"), params.get("startDate"), params.get("endDate"), user.get("SYSUSER_ID"), params.get("startDate"), params.get("endDate"), user.get("SYSUSER_ID"), params.get("startDate"), params.get("endDate")});
        } else {
            return this.getBySql(sql.toString(), new Object[]{user.get("SYSUSER_ID"), user.get("SYSUSER_ID"), user.get("SYSUSER_ID"), user.get("SYSUSER_ID")});
        }
    }
}
