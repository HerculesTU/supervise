package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.EmailLogDao;
import com.housoo.platform.core.service.EmailLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 描述 邮件发送日志业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-12-20 15:45:42
 */
@Service("emailLogService")
public class EmailLogServiceImpl extends BaseServiceImpl implements EmailLogService {

    /**
     * 所引入的dao
     */
    @Resource
    private EmailLogDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

}
