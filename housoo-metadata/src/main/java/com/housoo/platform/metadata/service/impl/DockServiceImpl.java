package com.housoo.platform.metadata.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.metadata.dao.DockDao;
import com.housoo.platform.metadata.service.DockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述 服务对接申请业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-09 14:23:14
 */
@Service("dockService")
public class DockServiceImpl extends BaseServiceImpl implements DockService {

    /**
     * 所引入的dao
     */
    @Resource
    private DockDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

}
