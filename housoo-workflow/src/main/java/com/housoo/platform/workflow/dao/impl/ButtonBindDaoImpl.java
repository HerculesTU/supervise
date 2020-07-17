package com.housoo.platform.workflow.dao.impl;

import com.housoo.platform.core.dao.impl.BaseDaoImpl;
import com.housoo.platform.workflow.dao.ButtonBindDao;
import org.springframework.stereotype.Repository;

/**
 * 描述按钮绑定业务相关dao实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-05-07 09:49:51
 */
@Repository
public class ButtonBindDaoImpl extends BaseDaoImpl implements ButtonBindDao {

    /**
     * 获取配置的最大排序值
     *
     * @param BTNBIND_FLOWDEFID
     * @param BTNBIND_FLOWVERSION
     * @return
     */
    @Override
    public int getMaxSn(String BTNBIND_FLOWDEFID, String BTNBIND_FLOWVERSION) {
        StringBuffer sql = new StringBuffer("SELECT MAX(T.BTNBIND_SN) ");
        sql.append("FROM JBPM6_BUTTONBIND T WHERE T.BTNBIND_FLOWDEFID=? ");
        sql.append(" AND T.BTNBIND_FLOWVERSION=? ");
        int maxSn = this.getIntBySql(sql.toString(), new Object[]{BTNBIND_FLOWDEFID,
                Integer.parseInt(BTNBIND_FLOWVERSION)});
        return maxSn;
    }
}
