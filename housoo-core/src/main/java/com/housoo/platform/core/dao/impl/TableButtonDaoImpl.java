package com.housoo.platform.core.dao.impl;

import com.housoo.platform.core.dao.TableButtonDao;
import org.springframework.stereotype.Repository;

/**
 * 描述表格按钮业务相关dao实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-03-17 10:25:49
 */
@Repository
public class TableButtonDaoImpl extends BaseDaoImpl implements TableButtonDao {

    /**
     * 根据表单控件ID获取最大排序值
     *
     * @param formControlId
     * @return
     */
    @Override
    public int getMaxSn(String formControlId) {
        StringBuffer sql = new StringBuffer("SELECT max(T.TABLEBUTTON_SN)");
        sql.append(" FROM PLAT_APPMODEL_TABLEBUTTON T ");
        sql.append("WHERE T.TABLEBUTTON_FORMCONTROLID=? ");
        int maxSn = this.getIntBySql(sql.toString(), new Object[]{formControlId});
        return maxSn;
    }
}
