/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.supervise.dao.SuperviseApproveDao;
import com.housoo.platform.supervise.service.SuperviseApproveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 描述 督办环节配置业务相关service实现类
 *
 * @author cjr
 * @version 1.0
 * @created 2020-04-13 18:20:59
 */
@Service("superviseApproveService")
public class SuperviseApproveServiceImpl extends BaseServiceImpl implements SuperviseApproveService {

    /**
     * 所引入的dao
     */
    @Resource
    private SuperviseApproveDao dao;

    /**
     * 获取dao
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }

    @Override
    public List<Map<String, Object>> findAll() {
        StringBuffer sql = new StringBuffer("select T.NODE_ID,T.NODE_NAME,T.SHORT_NAME,T.RECORD_ID,T.NEED_APPROVE_FLAG ");
        sql.append("from tb_supervise_approve T where T.DEL_FLAG = '1' ");
        sql.append(" ORDER BY T.create_time ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        return list;
    }
}
