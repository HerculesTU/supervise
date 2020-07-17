/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.workflow.dao.FlowBtnDao;
import com.housoo.platform.workflow.service.FlowBtnService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 描述 流程按钮业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-07 07:09:57
 */
@Service("flowBtnService")
public class FlowBtnServiceImpl extends BaseServiceImpl implements FlowBtnService {

    /**
     * 所引入的dao
     */
    @Resource
    private FlowBtnDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取可选列表的按钮
     *
     * @param param
     * @return
     */
    @Override
    public List<Map<String, Object>> findByForSelect(String param) {
        StringBuffer sql = new StringBuffer("SELECT");
        sql.append(" T.FLOWBTN_ID AS VALUE,T.FLOWBTN_NAME AS LABEL,");
        sql.append("T.FLOWBTN_ICON,T.FLOWBTN_COLOR,T.FLOWBTN_CLICKFN,");
        sql.append("T.FLOWBTN_CLICKCONTENT FROM ");
        sql.append(" JBPM6_FLOWBTN T ORDER BY T.FLOWBTN_CREATETIME ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        for (Map<String, Object> btn : list) {
            String FLOWBTN_CLICKCONTENT = (String) btn.get("FLOWBTN_CLICKCONTENT");
            btn.put("FLOWBTN_CLICKCONTENT", StringEscapeUtils.escapeHtml3(FLOWBTN_CLICKCONTENT));
        }
        return list;
    }
}
