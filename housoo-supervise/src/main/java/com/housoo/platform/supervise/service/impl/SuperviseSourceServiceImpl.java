/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.supervise.dao.SuperviseSourceDao;
import com.housoo.platform.supervise.service.SuperviseSourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 描述 督办来源业务相关service实现类
 *
 * @author cjr
 * @version 1.0
 * @created 2020-04-13 11:14:38
 */
@Service("superviseSourceService")
public class SuperviseSourceServiceImpl extends BaseServiceImpl implements SuperviseSourceService {

    /**
     * 所引入的dao
     */
    @Resource
    private SuperviseSourceDao dao;

    /**
     * 获取dao
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取可选的下拉督办来源数据
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> findForSelect(String params) {
        StringBuffer sql = new StringBuffer("select T.record_id AS VALUE,T.source_name AS LABEL ");
        sql.append("from tb_supervise_source T where T.DEL_FLAG = '1' ");
        sql.append(" ORDER BY T.create_time ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        return list;
    }
}
