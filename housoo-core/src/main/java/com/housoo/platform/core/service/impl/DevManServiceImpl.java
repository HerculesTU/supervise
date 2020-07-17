package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.DevManDao;
import com.housoo.platform.core.service.DevManService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述 开发者信息业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-04-19 14:37:38
 */
@Service("devManService")
public class DevManServiceImpl extends BaseServiceImpl implements DevManService {

    /**
     * 所引入的dao
     */
    @Resource
    private DevManDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

}
