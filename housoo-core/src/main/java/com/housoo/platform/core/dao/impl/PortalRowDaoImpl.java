package com.housoo.platform.core.dao.impl;

import com.housoo.platform.core.dao.PortalRowDao;
import org.springframework.stereotype.Repository;

/**
 * 描述门户行业务相关dao实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-06-08 15:07:52
 */
@Repository
public class PortalRowDaoImpl extends BaseDaoImpl implements PortalRowDao {

    /**
     * 根据主题ID获取最大排序值
     *
     * @param themeId
     * @return
     */
    @Override
    public int getMaxSn(String themeId) {
        StringBuffer sql = new StringBuffer("SELECT MAX(T.ROW_SN)");
        sql.append(" FROM PLAT_APPMODEL_PORTALROW T ");
        sql.append("WHERE T.ROW_THEMEID=? ");
        return this.getIntBySql(sql.toString(), new Object[]{themeId});
    }
}
