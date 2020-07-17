package com.housoo.platform.metadata.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.metadata.dao.QueryDao;
import com.housoo.platform.metadata.service.QueryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 描述 输入参数业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-08 11:11:34
 */
@Service("queryService")
public class QueryServiceImpl extends BaseServiceImpl implements QueryService {

    /**
     * 所引入的dao
     */
    @Resource
    private QueryDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据资源ID获取参数列表
     *
     * @param resId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByResId(String resId) {
        StringBuffer sql = new StringBuffer("select T.QUERY_ID,T.QUERY_CN,T.QUERY_EN,T.QUERY_NULLABLE");
        sql.append(",T.QUERY_DESC,T.QUERY_LENGTH,D.DIC_NAME,T.QUERY_VARULE");
        sql.append(" from PLAT_METADATA_QUERY T LEFT JOIN PLAT_SYSTEM_DICTIONARY D ");
        sql.append(" ON T.QUERY_VARULE=D.DIC_VALUE AND D.DIC_DICTYPE_CODE=? WHERE T.DATARES_ID=?");
        sql.append(" ORDER BY T.QUERY_TIME DESC");
        return dao.findBySql(sql.toString(), new Object[]{"javaValidRule", resId}, null);
    }

    /**
     * 判断参数是否存在
     *
     * @param resId
     * @param cn
     * @param en
     * @return
     */
    @Override
    public boolean isExistsQuery(String resId, String cn, String en) {
        StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
        sql.append("PLAT_METADATA_QUERY T WHERE T.DATARES_ID=? ");
        sql.append("AND (T.QUERY_CN=? OR T.QUERY_EN=?) ");
        int count = dao.getIntBySql(sql.toString(), new Object[]{resId, cn, en});
        if (count != 0) {
            return true;
        } else {
            return false;
        }
    }

}
