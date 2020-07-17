package com.housoo.platform.metadata.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.metadata.dao.DataCatalogDao;
import com.housoo.platform.metadata.service.DataCatalogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述 数据目录业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-07 10:53:23
 */
@Service("dataCatalogService")
public class DataCatalogServiceImpl extends BaseServiceImpl implements DataCatalogService {

    /**
     * 所引入的dao
     */
    @Resource
    private DataCatalogDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据目录ID级联删除子孙目录
     *
     * @param catalogId
     */
    @Override
    public void deleteCascadeChild(String catalogId) {
        StringBuffer sql = new StringBuffer("DELETE FROM PLAT_METADATA_CATALOG ");
        sql.append("WHERE CATALOG_PATH LIKE ? ");
        dao.executeSql(sql.toString(), new Object[]{"%." + catalogId + ".%"});
    }

    /**
     * 根据资源ID获取目录id集合
     *
     * @param resId
     * @return
     */
    @Override
    public List<String> findCatalogIds(String resId) {
        return dao.findCatalogIds(resId);
    }
}
