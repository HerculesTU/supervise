/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.workflow.dao.FieldModifyDao;
import com.housoo.platform.workflow.service.FieldModifyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 字段修改业务相关service实现类
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-11-02 09:40:47
 */
@Service("fieldModifyService")
public class FieldModifyServiceImpl extends BaseServiceImpl implements FieldModifyService {

    /**
     * 所引入的dao
     */
    @Resource
    private FieldModifyDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 保存字段修改日志
     *
     * @param postParams
     * @param jbpmFlowInfo
     */
    @Override
    public void saveFieldModify(Map<String, Object> postParams,
                                JbpmFlowInfo jbpmFlowInfo) {
        Map<String, Object> backLoginUser = null;
        try {
            backLoginUser = PlatAppUtil.getBackPlatLoginUser();
        } catch (Exception e) {
            PlatLogUtil.doNothing(e);
        }
        if (backLoginUser != null && "false".equals(jbpmFlowInfo.getJbpmIsTempSave())) {
            String EFLOW_EXEID = jbpmFlowInfo.getJbpmExeId();
            String EFLOW_BUSRECORDID = jbpmFlowInfo.getJbpmMainTableRecordId();
            if (StringUtils.isNotEmpty(EFLOW_EXEID) && StringUtils.isNotEmpty(EFLOW_BUSRECORDID)) {
                //获取JSON
                String FLOW_FORMFIELD_MODIFYJSON = (String) postParams.get("FLOW_FORMFIELD_MODIFYJSON");
                if (StringUtils.isNotEmpty(FLOW_FORMFIELD_MODIFYJSON)) {
                    List<Map> fieldList = JSON.parseArray(FLOW_FORMFIELD_MODIFYJSON, Map.class);
                    for (Map field : fieldList) {
                        String FIELD_CN = (String) field.get("FIELD_CN");
                        String FIELD_EN = (String) field.get("FIELD_EN");
                        String FIELD_OLDVALUE = (String) field.get("FIELD_OLDVALUE");
                        String FIELD_NEWVALUE = (String) field.get("FIELD_NEWVALUE");
                        if (StringUtils.isNotEmpty(FIELD_CN) && StringUtils.isNotEmpty(FIELD_OLDVALUE)
                                && !FIELD_NEWVALUE.equals(FIELD_OLDVALUE)) {
                            Map<String, Object> modifyLog = new HashMap<String, Object>();
                            modifyLog.put("FIELDMODIFY_USERID", backLoginUser.get("SYSUSER_ID"));
                            modifyLog.put("FIELDMODIFY_USERNAME", backLoginUser.get("SYSUSER_NAME"));
                            modifyLog.put("FIELDMODIFY_EXEID", EFLOW_EXEID);
                            modifyLog.put("FIELDMODIFY_EN", FIELD_EN);
                            modifyLog.put("FIELDMODIFY_CN", FIELD_CN);
                            modifyLog.put("FIELDMODIFY_BEFORE", FIELD_OLDVALUE);
                            modifyLog.put("FIELDMODIFY_AFTER", FIELD_NEWVALUE);
                            modifyLog.put("FIELDMODIFY_TIME", PlatDateTimeUtil.
                                    formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                            modifyLog.put("FIELDMODIFY_NODEKEY", jbpmFlowInfo.getJbpmOperingNodeKey());
                            modifyLog.put("FIELDMODIFY_NODENAME", jbpmFlowInfo.getJbpmOperingNodeName());
                            dao.saveOrUpdate("JBPM6_FIELDMODIFY", modifyLog, SysConstants.ID_GENERATOR_UUID, null);
                        }
                    }
                }
            }
        }
    }

}
