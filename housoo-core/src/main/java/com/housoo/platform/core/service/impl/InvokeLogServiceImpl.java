package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.InvokeLogDao;
import com.housoo.platform.core.service.InvokeLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述 服务调用日志业务相关service实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-08-03 18:41:18
 */
@Service("invokeLogService")
public class InvokeLogServiceImpl extends BaseServiceImpl implements InvokeLogService {

    /**
     * 所引入的dao
     */
    @Resource
    private InvokeLogDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

}
