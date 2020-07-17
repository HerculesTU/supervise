package com.housoo.platform.core.dao.impl;

import com.housoo.platform.core.dao.WorkdayDao;
import org.springframework.stereotype.Repository;

/**
 * 描述工作日业务相关dao实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-05-03 09:32:53
 */
@Repository
public class WorkdayDaoImpl extends BaseDaoImpl implements WorkdayDao {

    /**
     * 根据开始日期和结束日期获取工作日数量
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    @Override
    public int getWorkDayCount(String beginDate, String endDate) {
        StringBuffer sql = new StringBuffer("select count(*) from ");
        sql.append("PLAT_SYSTEM_WORKDAY W WHERE W.WORKDAY_DATE>?");
        sql.append(" AND W.WORKDAY_DATE<=? AND W.WORKDAY_SETID=2 ORDER BY W.WORKDAY_DATE ASC");
        int count = this.getIntBySql(sql.toString(), new Object[]{beginDate, endDate});
        return count;
    }
}
