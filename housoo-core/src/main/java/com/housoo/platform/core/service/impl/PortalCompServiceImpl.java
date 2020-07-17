package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.PortalCompDao;
import com.housoo.platform.core.service.PortalCompService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 描述 门户组件业务相关service实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-06-12 14:39:45
 */
@Service("portalCompService")
public class PortalCompServiceImpl extends BaseServiceImpl implements PortalCompService {

    /**
     * 所引入的dao
     */
    @Resource
    private PortalCompDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据类别编码获取列表数据
     *
     * @param typeCode
     * @return
     */
    @Override
    public List<Map<String, Object>> findByCompTypeCode(String typeCode) {
        StringBuffer sql = new StringBuffer("SELECT T.COMP_ID AS VALUE,T.COMP_NAME AS LABEL,");
        sql.append("T.COMP_URL ");
        sql.append("FROM PLAT_APPMODEL_PORTALCOMP T WHERE T.COMP_TYPECODE=? ");
        sql.append("ORDER BY T.COMP_CREATETIME ASC ");
        if (StringUtils.isNotEmpty(typeCode)) {
            return dao.findBySql(sql.toString(), new Object[]{typeCode}, null);
        } else {
            return null;
        }
    }

}
