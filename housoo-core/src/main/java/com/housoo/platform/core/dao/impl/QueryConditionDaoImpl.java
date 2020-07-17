package com.housoo.platform.core.dao.impl;

import com.housoo.platform.core.dao.QueryConditionDao;
import org.springframework.stereotype.Repository;

/**
 * 描述 QueryCondition业务相关dao实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-03-14 10:55:40
 */
@Repository
public class QueryConditionDaoImpl extends BaseDaoImpl implements QueryConditionDao {

    /**
     * 根据表单控件ID获取排序条件的最大值
     *
     * @param formControlId
     * @return
     */
    @Override
    public int getMaxSn(String formControlId) {
        StringBuffer sql = new StringBuffer("select max(T.querycondition_sn)");
        sql.append(" from PLAT_APPMODEL_QUERYCONDITION T ");
        sql.append("WHERE T.QUERYCONDITION_FORMCONTROLID=?");
        int maxSn = this.getIntBySql(sql.toString(),
                new Object[]{formControlId});
        return maxSn;
    }
}
