package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.CommonCodeDao;
import com.housoo.platform.core.service.CommonCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述 代码块业务相关service实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-05-14 09:15:53
 */
@Service("commonCodeService")
public class CommonCodeServiceImpl extends BaseServiceImpl implements CommonCodeService {

    /**
     * 所引入的dao
     */
    @Resource
    private CommonCodeDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

}
