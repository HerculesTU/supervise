package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatDbUtil;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.core.dao.CompanyDao;
import com.housoo.platform.core.service.CompanyService;
import com.housoo.platform.core.service.SysLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 单位业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-09 09:43:59
 */
@Service("companyService")
public class CompanyServiceImpl extends BaseServiceImpl implements CompanyService {

    /**
     * 所引入的dao
     */
    @Resource
    private CompanyDao dao;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据单位ID级联删除相关的数据库表信息
     *
     * @param companyId
     */
    @Override
    public void deleteCompanyCacasdeAssocial(String companyId) {
        //级联删除部门信息
        StringBuffer sql = new StringBuffer("DELETE FROM PLAT_SYSTEM_DEPART ");
        sql.append(" WHERE DEPART_COMPANYID=? ");
        dao.executeSql(sql.toString(), new Object[]{companyId});
        //更新用户的部门字段和单位为空
        sql = new StringBuffer("UPDATE PLAT_SYSTEM_SYSUSER SET SYSUSER_COMPANYID=null");
        sql.append(",SYSUSER_DEPARTID=null WHERE SYSUSER_COMPANYID=? ");
        dao.executeSql(sql.toString(), new Object[]{companyId});
        //级联删除单位信息
        sql = new StringBuffer("DELETE FROM PLAT_SYSTEM_COMPANY  ");
        sql.append("WHERE COMPANY_PATH LIKE ? ");
        dao.executeSql(sql.toString(), new Object[]{"%." + companyId + ".%"});
    }

    /**
     * 新增或者修改单位信息表
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveOrUpdateCompany(HttpServletRequest request) {
        Map<String, Object> company = PlatBeanUtil.getMapFromRequest(request);
        //获取前端传递过来的字段变更JSON
        String formfieldModifyArrayJson = request.getParameter("formfieldModifyArrayJson");
        //解析成修改字段数组
        List<Map> formfieldModifyArray = JSON.parseArray(formfieldModifyArrayJson, Map.class);
        String COMPANY_ID = (String) company.get("COMPANY_ID");
        company = this.saveOrUpdateTreeData("PLAT_SYSTEM_COMPANY",
                company, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isNotEmpty(COMPANY_ID)) {
            sysLogService.saveBackLog("单位部门管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + COMPANY_ID + "]单位信息", request, formfieldModifyArray, null, null);
        } else {
            COMPANY_ID = (String) company.get("COMPANY_ID");
            sysLogService.saveBackLog("单位部门管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + COMPANY_ID + "]单位信息", request, formfieldModifyArray, null, null);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }

    /**
     * 删除单位信息
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> delCompany(HttpServletRequest request) {
        String companyId = request.getParameter("treeNodeId");
        String sql = PlatDbUtil.getDiskSqlContent("system/company/001", null);
        //获取你需要展现的被删除的单位数据列表
        List<List<String>> comList = dao.findListBySql(sql + PlatStringUtil.getSqlInCondition(companyId), null, null);
        //调用明细版的插入日志API。delColNames这个参数代表日志中被删除的列字段名字,而comList是和它对应的被删除数据列表
        sysLogService.saveBackLog("单位部门管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + companyId + "]的单位信息", request, null, "单位表主键值,单位名称", comList);
        this.deleteCompanyCacasdeAssocial(companyId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }

}
