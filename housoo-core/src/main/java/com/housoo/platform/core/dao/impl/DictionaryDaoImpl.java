package com.housoo.platform.core.dao.impl;

import com.housoo.platform.core.util.PlatCollectionUtil;
import com.housoo.platform.core.dao.DictionaryDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author 高飞
 * @created 2017年2月1日 下午6:01:47
 */
@Repository
public class DictionaryDaoImpl extends BaseDaoImpl implements DictionaryDao {

    /**
     * 根据类别编码获取最大排序
     *
     * @param typeCode
     * @return
     */
    @Override
    public int getMaxSn(String typeCode) {
        StringBuffer sql = new StringBuffer("SELECT MAX(");
        sql.append("DIC_SN) FROM PLAT_SYSTEM_DICTIONARY WHERE ");
        sql.append("DIC_DICTYPE_CODE=? ");
        int maxSn = this.getIntBySql(sql.toString()
                , new Object[]{typeCode});
        return maxSn + 1;
    }

    /**
     * 判断一个字典值是否存在
     *
     * @param dicId
     * @param typeCode
     * @param dicValue
     * @return
     */
    @Override
    public boolean isExistsDic(String dicId, String typeCode, String dicValue) {
        List<Object> params = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
        sql.append("PLAT_SYSTEM_DICTIONARY D WHERE D.DIC_DICTYPE_CODE=? ");
        sql.append("AND D.DIC_VALUE=? ");
        params.add(typeCode);
        params.add(dicValue);
        if (StringUtils.isNotEmpty(dicId)) {
            Map<String, Object> dicinfo = this.getRecord("PLAT_SYSTEM_DICTIONARY",
                    new String[]{"DIC_ID"}, new Object[]{dicId});
            sql.append(" AND D.DIC_VALUE!=? ");
            params.add(dicinfo.get("DIC_VALUE"));
        }
        int count = this.getIntBySql(sql.toString(), params.toArray());
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 描述 更新排序字段
     *
     * @param dicIds
     * @author housoo
     * @created 2014年10月3日 下午12:54:23
     */
    @Override
    public void updateSn(String[] dicIds) {
        int[] oldSns = new int[dicIds.length];
        StringBuffer sql = new StringBuffer("select DIC_SN FROM PLAT_SYSTEM_DICTIONARY ").append(" WHERE DIC_ID=? ");
        for (int i = 0; i < dicIds.length; i++) {
            int dicSn = this.getIntBySql(sql.toString(), new Object[]{dicIds[i]});
            oldSns[i] = dicSn;
        }
        int[] newSns = PlatCollectionUtil.sortByDesc(oldSns);
        StringBuffer updateSql = new StringBuffer("update PLAT_SYSTEM_DICTIONARY ")
                .append(" SET DIC_SN=? WHERE DIC_ID=? ");
        for (int i = 0; i < dicIds.length; i++) {
            getJdbcTemplate().update(updateSql.toString(), new Object[]{newSns[i], dicIds[i]});
        }
    }

    /**
     * 获取字典的值
     *
     * @param typeCode
     * @param dicName
     * @return
     */
    @Override
    public String getDictionaryValue(String typeCode, String dicName) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT T.DIC_VALUE FROM PLAT_SYSTEM_DICTIONARY T ");
        sql.append(" WHERE T.DIC_DICTYPE_CODE=? AND T.DIC_NAME=? ");
        return this.getUniqueObj(sql.toString(), new Object[]{typeCode, dicName}, String.class);
    }
}
