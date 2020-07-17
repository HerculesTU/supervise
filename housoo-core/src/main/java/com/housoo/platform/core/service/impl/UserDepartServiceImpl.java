package com.housoo.platform.core.service.impl;


import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.UserDepartDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.UserDepartService;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 描述 保存用户分管部门业务相关service实现类
 *
 * @author wh
 * @version 1.0
 * @created 2019-05-06 15:29:55
 */
@Service("userDepartService")
public class UserDepartServiceImpl extends BaseServiceImpl implements UserDepartService {

    /**
     * 所引入的dao
     */
    @Resource
    private UserDepartDao dao;
    /**
     * 所引入的dao
     */
    @Resource
    private SysLogService sysLogService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    @Override
    public List<Map<String, Object>> findGridItemList(SqlFilter sqlFilter) {
        StringBuffer sql = new StringBuffer("select * from PLAT_SYSTEM_DEPART C WHERE 1=1 ");
        List<Object> params = new ArrayList<Object>();
        String selectedUserIds = sqlFilter.getRequest().getParameter("selectedRecordIds");
        String iconfont = sqlFilter.getRequest().getParameter("iconfont");
        String itemconf = sqlFilter.getRequest().getParameter("itemconf");
        Map<String, String> getGridItemConf = PlatUICompUtil.getGridItemConfMap(itemconf);
        if (StringUtils.isNotEmpty(selectedUserIds)) {
            sql.append(" AND C.DEPART_ID in ");
            sql.append(PlatStringUtil.getSqlInCondition(selectedUserIds));
            // params.add("" + selectedUserIds + "");
            List<Map<String, Object>> list = dao.findBySql(sql.toString(), params.toArray(), null);
            list = PlatUICompUtil.getGridItemList("DEPART_ID", iconfont, getGridItemConf, list);
            return list;
        } else {
            return null;
        }
    }

    /**
     * 保存用户分管部门信息中间表
     *
     * @param userId
     * @param departId
     */
    @Override
    public void saveDepart(String userId, String departId) {
        dao.deleteRecords("TB_USER_DEPART", "SYSUSER_ID", new String[]{userId});
        StringBuffer sql = new StringBuffer("insert into TB_USER_DEPART ");
        sql.append("(SYSUSER_ID,DEPART_ID) values(?,?) ");
        if (StringUtils.isNotEmpty(departId)) {
            String departIdArray[] = departId.split(",");
            for (int i = 0; i < departIdArray.length; i++) {
                dao.executeSql(sql.toString(), new Object[]{userId, departIdArray[i]});
            }
        }
    }


    /**
     * 根据部门Id获取分管领导信息
     *
     * @param departId
     * @return
     */
    @Override
    public List<Map<String, Object>> getDepartLeader(String departId) {
        List<Map<String, Object>> departLeader = new ArrayList<>();
        StringBuffer sql = new StringBuffer(" SELECT UD.SYSUSER_ID ID,U.SYSUSER_NAME NAME FROM TB_USER_DEPART UD ");
        sql.append(" LEFT JOIN PLAT_SYSTEM_SYSUSER U ");
        sql.append(" ON UD.SYSUSER_ID = U.SYSUSER_ID ");
        sql.append(" WHERE ud.DEPART_ID = ? ");
        departLeader = this.dao.findBySql(sql.toString(), new Object[]{departId}, null);
        return departLeader;
    }

    /**
     * 保存用户分管部门信息（安全审核回调接口）
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveUserDepart(HttpServletRequest request) {
        String userId = request.getParameter("SYSUSER_ID");
        String checkDepartIds = request.getParameter("checkUserIds");
        dao.deleteRecords("TB_USER_DEPART", "SYSUSER_ID", new String[]{userId});
        StringBuffer sql = new StringBuffer("insert into TB_USER_DEPART ");
        sql.append("(SYSUSER_ID,DEPART_ID) values(?,?) ");
        List<List<String>> deptIdList = new ArrayList<>();
        if (StringUtils.isNotEmpty(checkDepartIds)) {
            String departIdArray[] = checkDepartIds.split(",");
            deptIdList.add(Arrays.asList(departIdArray));
            for (int i = 0; i < departIdArray.length; i++) {
                dao.executeSql(sql.toString(), new Object[]{userId, departIdArray[i]});
            }
        }
        sysLogService.saveBackLog("系统用户管理", SysLogService.OPER_TYPE_EDIT,
                "为用户ID为[" + userId + "]的用户,设置了分管部门", request, null,
                "部门ID", deptIdList);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }

}
