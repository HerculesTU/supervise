/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.workflow.dao.HistDeployDao;
import com.housoo.platform.workflow.service.HistDeployService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述 流程历史部署业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-04 14:34:24
 */
@Service("histDeployService")
public class HistDeployServiceImpl extends BaseServiceImpl implements HistDeployService {

    /**
     * 所引入的dao
     */
    @Resource
    private HistDeployDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 保存历史部署信息
     *
     * @param oldFlowDef
     */
    @Override
    public void saveHistDeploy(Map<String, Object> oldFlowDef) {
        Map<String, Object> deploy = new HashMap<String, Object>();
        deploy.putAll(oldFlowDef);
        dao.saveOrUpdate("JBPM6_HIST_DEPLOY", deploy, SysConstants.ID_GENERATOR_UUID, null);
    }
}
