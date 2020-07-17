package com.housoo.platform.supervise.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.DepartDao;
import com.housoo.platform.core.service.DepartService;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.supervise.service.DeptSyncService;
import org.apache.shiro.crypto.hash.Hash;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 部门同步实现类
 */
public class DeptSyncServiceImpl extends BaseServiceImpl implements DeptSyncService {

    /**
     * 所引入的dao
     */
    @Resource
    private DepartDao dao;

    /**
     * 部门Service
     */
    @Resource
    private DepartService departService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 同步部门
     *
     * @param messageContent 部门JSON字符串
     */
    @Override
    public void syncDept(String messageContent) {
        JSONObject deptJson = JSONObject.parseObject(messageContent);
        Map<String, Object> deptMap = new HashMap<>();
        // 同步标记 add:新增 update:修改 del:删除
        String syncFlag = deptJson.getString("status");
        // 新增
        if ("deptadd".equals(syncFlag)) {
            deptMap.put("DEPART_ID", deptJson.getString("id"));
            deptMap.put("DEPART_NAME", deptJson.getString("fullName"));
            deptMap.put("DEPART_CODE", deptJson.getString("code"));
            deptMap.put("DEPART_PARENTID", "0");
            deptMap.put("DEPART_LEVEL", deptJson.getString("ctype"));
            deptMap.put("DEPART_TREESN", deptJson.getString("sepecailIndex"));
            deptMap.put("DEPART_CREATETIME", deptJson.getString("creationDate"));
            deptMap = departService.saveOrUpdate("", deptMap, SysConstants.ID_GENERATOR_UUID, null);
        }
        // 修改
        if ("deptedit".equals(syncFlag)) {
            String deptId = deptJson.getString("id");
            deptMap = departService.getRecord("PLAT_SYSTEM_DEPART", new String[]{"DEPART_ID"}, new Object[]{deptId});
            deptMap.put("DEPART_ID", deptJson.getString("id"));
            deptMap.put("DEPART_NAME", deptJson.getString("fullName"));
            deptMap.put("DEPART_CODE", deptJson.getString("code"));
            deptMap.put("DEPART_LEVEL", deptJson.getString("ctype"));
            deptMap.put("DEPART_TREESN", deptJson.getString("sepecailIndex"));
            deptMap = departService.saveOrUpdate("", deptMap, SysConstants.ID_GENERATOR_UUID, null);
        }
        // 删除
        if ("deptdelete".equals(syncFlag)) {
            String deptIds = deptJson.getString("ids");
            String[] deptIdArr = deptIds.split(",");
            for (int i = 0; i < deptIdArr.length; i++) {
                departService.deleteDepartCascadeUser(deptIdArr[i]);
            }
        }
    }
}
