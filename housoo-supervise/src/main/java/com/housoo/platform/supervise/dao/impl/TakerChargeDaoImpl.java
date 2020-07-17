/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.dao.impl;

import com.housoo.platform.core.dao.impl.BaseDaoImpl;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.supervise.dao.TakerChargeDao;
import com.housoo.platform.supervise.dao.TakerDao;
import com.housoo.platform.supervise.service.SuperviseClazzService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 承办人业务相关dao实现类
 *
 * @author cjr
 * @version 1.0
 * @created 2020-04-14 15:30:11
 */
@Repository
public class TakerChargeDaoImpl extends BaseDaoImpl implements TakerChargeDao {

    @Override
    public List<Map<String, Object>> findTaskListBySuperviseId(String superviseId) {
        StringBuffer sql = new StringBuffer("select t.RECORD_ID,t.DEPART_ID,d.DEPART_NAME,t.STATUS ");
        sql.append("from tb_supervise_task t ");
        sql.append("left join plat_system_depart d on t.DEPART_ID = d.DEPART_ID ");
        sql.append("where t.SUPERVISE_ID = ? and t.SUPERVISE_NO IS NOT NULL and t.STATUS NOT IN (0,10)");
        sql.append("order by t.CREATE_TIME asc ");
        return this.findBySql(sql.toString(), new Object[]{superviseId}, null);
    }

    @Override
    public List<Map<String, Object>> findTaskNodeListBySuperviseId(String superviseId) {
        StringBuffer sql = new StringBuffer("select a.NODE_ID,a.NODE_NAME,temp.RECORD_ID,temp.NODE_CONTENT,temp.CREATE_TIME,a.SHORT_NAME,a.NEED_APPROVE_FLAG ");
        sql.append("from tb_supervise_approve a ");
        sql.append("LEFT JOIN (SELECT RECORD_ID,NODE_CONTENT,CREATE_TIME,NODE_ID from tb_supervise_node_info where SUPERVISE_ID= ? ) temp ");
        sql.append("on a.NODE_ID = temp.NODE_ID ");
        sql.append("where a.DEL_FLAG = '1' ");
        sql.append("ORDER BY a.NODE_ID ASC ");
        return this.findBySql(sql.toString(), new Object[]{superviseId}, null);
    }

}
