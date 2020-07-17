package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.DeployNodeDao;
import com.housoo.platform.core.service.DeployNodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 描述 部署节点列表业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-04-19 16:15:13
 */
@Service("deployNodeService")
public class DeployNodeServiceImpl extends BaseServiceImpl implements DeployNodeService {

    /**
     * 所引入的dao
     */
    @Resource
    private DeployNodeDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取部署节点列表
     *
     * @param param
     * @return
     */
    @Override
    public List<Map<String, Object>> findList(String param) {
        StringBuffer sql = new StringBuffer("SELECT T.DEPLOYNODE_ID AS VALUE");
        sql.append(",T.DEPLOYNODE_NAME AS LABEL FROM PLAT_SYSTEM_DEPLOYNODE");
        sql.append(" T ORDER BY T.DEPLOYNODE_TIME ASC");
        return dao.findBySql(sql.toString(), null, null);
    }

}
