package com.housoo.platform.core.dao.impl;

import com.housoo.platform.core.dao.ScheduleDao;
import org.springframework.stereotype.Repository;

/**
 * 描述定时任务业务相关dao实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-27 16:37:05
 */
@Repository
public class ScheduleDaoImpl extends BaseDaoImpl implements ScheduleDao {

    /**
     * 根据编码获取数量
     *
     * @param scheduleCode
     * @return
     */
    @Override
    public int getCountByScheduleCode(String scheduleCode) {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
        sql.append("PLAT_SYSTEM_SHEDULE S WHERE S.SHEDULE_CODE=?");
        int count = this.getIntBySql(sql.toString()
                , new Object[]{scheduleCode});
        return count;
    }
}
