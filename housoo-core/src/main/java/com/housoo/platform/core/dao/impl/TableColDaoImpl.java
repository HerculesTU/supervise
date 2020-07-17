package com.housoo.platform.core.dao.impl;

import com.housoo.platform.core.dao.TableColDao;
import org.springframework.stereotype.Repository;

/**
 * 描述表格列业务相关dao实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-03-30 15:16:45
 */
@Repository
public class TableColDaoImpl extends BaseDaoImpl implements TableColDao {

    /**
     * 根据表单控件ID获取最大排序值
     *
     * @param formControlId
     * @return
     */
    @Override
    public int getMaxSn(String formControlId) {
        StringBuffer sql = new StringBuffer("SELECT MAX(T.TABLECOL_ORERSN) FROM ");
        sql.append("PLAT_APPMODEL_TABLECOL T WHERE T.TABLECOL_FORMCONTROLID=?");
        int maxSn = this.getIntBySql(sql.toString(), new Object[]{formControlId});
        return maxSn;
    }
}
