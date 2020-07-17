package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.DepartDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.CommonUIService;
import com.housoo.platform.core.service.DepartService;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.SysUserService;
import com.housoo.platform.core.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 描述 部门业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-09 09:43:59
 */
@Service("departService")
public class DepartServiceImpl extends BaseServiceImpl implements DepartService {

    /**
     * 所引入的dao
     */
    @Resource
    private DepartDao dao;
    /**
     *
     */
    @Resource
    private CommonUIService commonUIService;
    /**
     *
     */
    @Resource
    private SysUserService sysUserService;
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
     * 根据单位ID和部门编码判断是否存在部门信息
     *
     * @param companyId
     * @param departCode
     * @return
     */
    @Override
    public boolean isExistsDepart(String companyId, String departCode) {
        return dao.isExistsDepart(companyId, departCode);
    }

    /**
     * 根据单位ID获取下拉树数据
     *
     * @param companyId
     * @return
     */
    @Override
    public List<Map<String, Object>> findSelectTree(String companyId) {
        if (StringUtils.isNotEmpty(companyId)) {
            StringBuffer paramsConfig = new StringBuffer("[TABLE_NAME:PLAT_SYSTEM_DEPART]");
            paramsConfig.append("[TREE_IDANDNAMECOL:DEPART_ID,DEPART_NAME]");
            paramsConfig.append("[TREE_QUERYFIELDS:DEPART_PARENTID,DEPART_PATH]");
            paramsConfig.append("[FILTERS:DEPART_PARENTID_EQ|0,");
            paramsConfig.append("DEPART_COMPANYID_EQ|").append(companyId);
            paramsConfig.append("]");
            return commonUIService.findGenTreeSelectorDatas(paramsConfig.toString());
        } else {
            return null;
        }
    }

    /**
     * 构建用户列表数据
     *
     * @param departId
     * @return
     */
    private List<Map<String, Object>> findUserList(String departId, Set<String> needCheckIdSet,
                                                   String ROLE_IDS) {
        List<Map<String, Object>> userList = null;
        if (StringUtils.isNotEmpty(ROLE_IDS) && StringUtils.isNotEmpty(departId)) {
            userList = sysUserService.findByDepartIdAndRoleIds(departId, ROLE_IDS);
        } else if (StringUtils.isEmpty(ROLE_IDS) && StringUtils.isNotEmpty(departId)) {
            userList = sysUserService.findByDepartId(departId);
        }
        if (userList != null && userList.size() > 0) {
            for (Map<String, Object> user : userList) {
                String userId = (String) user.get("SYSUSER_ID");
                if (needCheckIdSet.contains(userId)) {
                    user.put("checked", true);
                }
                user.put("id", user.get("SYSUSER_ID"));
                user.put("name", user.get("SYSUSER_NAME"));
            }
            return userList;
        } else {
            return null;
        }
    }

    /**
     * 构建子部门的树
     *
     * @param children
     * @param needCheckIdSet
     * @param ROLE_IDS
     */
    private void constructChildUser(List<Map<String, Object>> children, Set<String> needCheckIdSet, String ROLE_IDS) {
        for (Map<String, Object> child : children) {
            String childDepId = (String) child.get("id");
            child.put("nocheck", true);
            List<Map<String, Object>> childDepList2 = (List<Map<String, Object>>) child.get("children");
            if (childDepList2 != null) {
                this.constructChildUser(childDepList2, needCheckIdSet, ROLE_IDS);
                List<Map<String, Object>> userList = this.findUserList(childDepId, needCheckIdSet, ROLE_IDS);
                if (userList != null) {
                    childDepList2.addAll(userList);
                }
            } else {
                childDepList2 = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> userList = this.findUserList(childDepId, needCheckIdSet, ROLE_IDS);
                if (userList != null) {
                    childDepList2.addAll(userList);
                    child.put("children", childDepList2);
                }
            }
        }
    }

    /**
     * 获取部门和用户的JSON字符串
     *
     * @param params
     * @return
     */
    @Override
    public String getDepartAndUserJson(Map<String, Object> params) {
        Object treeDatas = this.getTreeData(params);
        String needCheckIds = (String) params.get("needCheckIds");
        String ROLE_IDS = (String) params.get("ROLE_IDS");
        String filterRule = (String) params.get("filterRule");
        Set<String> needCheckIdSet = new HashSet<String>();
        if (StringUtils.isNotEmpty(needCheckIds)) {
            needCheckIdSet = new HashSet<String>(Arrays.asList(needCheckIds.split(",")));
        }
        if (treeDatas instanceof Map) {
            Map<String, Object> rootNode = (Map<String, Object>) treeDatas;
            String departId = (String) rootNode.get("id");
            rootNode.put("nocheck", true);
            List<Map<String, Object>> childDepList = (List<Map<String, Object>>) rootNode.get("children");
            if (childDepList == null) {
                childDepList = new ArrayList<Map<String, Object>>();
            }
            for (Map<String, Object> child : childDepList) {
                String childDepId = (String) child.get("id");
                child.put("nocheck", true);
                List<Map<String, Object>> childDepList2 = (List<Map<String, Object>>) child.get("children");
                if (childDepList2 != null) {
                    this.constructChildUser(childDepList2, needCheckIdSet, ROLE_IDS);
                    List<Map<String, Object>> userList = this.findUserList(childDepId,
                            needCheckIdSet, ROLE_IDS);
                    if (userList != null) {
                        childDepList2.addAll(userList);
                    }

                } else {
                    childDepList2 = new ArrayList<Map<String, Object>>();
                    List<Map<String, Object>> userList = this.findUserList(childDepId, needCheckIdSet, ROLE_IDS);
                    if (userList != null) {
                        childDepList2.addAll(userList);
                        child.put("children", childDepList2);
                    }
                }
            }
            if (!"0".equals(departId)) {
                List<Map<String, Object>> userList = this.findUserList(departId, needCheckIdSet, ROLE_IDS);
                if (userList != null) {
                    childDepList.addAll(userList);
                }
            }
        } else {

        }
        return JSON.toJSONString(treeDatas);
    }

    /**
     * 获取自动补全的部门和用户的数据源
     *
     * @param filter
     * @return
     */
    @Override
    public List<Map<String, Object>> findAutoDepartUser(SqlFilter filter) {
        String companyId = filter.getRequest().getParameter("Q_DEPART_COMPANYID_EQ");
        StringBuffer sql = new StringBuffer("SELECT D.DEPART_NAME AS value,");
        sql.append("D.DEPART_NAME AS label FROM PLAT_SYSTEM_DEPART D WHERE");
        sql.append(" D.DEPART_COMPANYID=? ORDER BY D.DEPART_TREESN ASC,D.DEPART_CREATETIME ASC");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> depList = dao.findBySql(sql.toString(),
                new Object[]{companyId}, null);
        list.addAll(depList);
        sql = new StringBuffer("select T.SYSUSER_NAME AS value,T.SYSUSER_NAME AS label");
        sql.append(" from PLAT_SYSTEM_SYSUSER T WHERE T.SYSUSER_COMPANYID=?");
        sql.append(" AND T.SYSUSER_STATUS!=? ORDER BY T.SYSUSER_CREATETIME ASC");
        List<Map<String, Object>> userList = dao.findBySql(sql.toString(),
                new Object[]{companyId, SysUserService.SYSUSER_STATUS_DEL}, null);
        list.addAll(userList);
        return list;
    }

    /**
     * 删除部门并且级联删除用户
     *
     * @param departId
     */
    @Override
    public void deleteDepartCascadeUser(String departId) {
        //级联更新部门的用户ID
        StringBuffer sql = new StringBuffer("UPDATE PLAT_SYSTEM_SYSUSER SET SYSUSER_DEPARTID=null");
        sql.append(" WHERE SYSUSER_DEPARTID IN (SELECT T.");
        sql.append("DEPART_ID FROM PLAT_SYSTEM_DEPART T WHERE T.DEPART_PATH LIKE ? ) ");
        dao.executeSql(sql.toString(), new Object[]{"%." + departId + ".%"});
        //级联删除部门信息
        sql = new StringBuffer("DELETE FROM PLAT_SYSTEM_DEPART ");
        sql.append("WHERE DEPART_PATH LIKE ? ");
        dao.executeSql(sql.toString(), new Object[]{"%." + departId + ".%"});
    }

    /**
     * 新增或者修改部门信息
     *
     * @param request
     * @param postParams
     * @return
     */
    @Override
    public Map<String, Object> saveOrUpdateDepart(HttpServletRequest request,
                                                  Map<String, Object> postParams) {
        String DEPART_COMPANYID = (String) postParams.get("DEPART_COMPANYID");
        if (StringUtils.isEmpty(DEPART_COMPANYID)) {
            DEPART_COMPANYID = "402848a55b556eb9015b55968e8e002c";
            postParams.put("DEPART_COMPANYID", DEPART_COMPANYID);
        }
        dao.saveOrUpdateTreeData("PLAT_SYSTEM_DEPART", postParams
                , SysConstants.ID_GENERATOR_ASSIGNED, null);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }

    /**
     * 删除部门
     *
     * @param request
     * @param postParams
     * @return
     */
    @Override
    public Map<String, Object> delDepart(HttpServletRequest request,
                                         Map<String, Object> postParams) {
        String DEPART_ID = (String) postParams.get("DEPART_ID");
        this.deleteDepartCascadeUser(DEPART_ID);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }

    /**
     * 根据filter获取网格项目
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map<String, Object>> findGridItemList(SqlFilter sqlFilter) {
        StringBuffer sql = new StringBuffer("SELECT T.DEPART_ID,T.DEPART_NAME");
        sql.append(" FROM PLAT_SYSTEM_DEPART T ");
        String selectedDeptIds = sqlFilter.getRequest().getParameter("selectedRecordIds");
        String iconfont = sqlFilter.getRequest().getParameter("iconfont");
        String itemconf = sqlFilter.getRequest().getParameter("itemconf");
        Map<String, String> getGridItemConf = PlatUICompUtil.getGridItemConfMap(itemconf);
        if (StringUtils.isNotEmpty(selectedDeptIds)) {
            sql.append(" WHERE T.DEPART_ID IN ");
            sql.append(PlatStringUtil.getSqlInCondition(selectedDeptIds));
            sql.append(" ORDER BY T.DEPART_CREATETIME DESC");
            List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
            list = PlatUICompUtil.getGridItemList("DEPART_ID", iconfont, getGridItemConf, list);
            return list;
        } else {
            return null;
        }
    }

    /**
     * 新增或者修改部门接口
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveOrUpdateDep(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> depart = PlatBeanUtil.getMapFromRequest(request);
        //获取前端传递过来的字段变更JSON
        String formfieldModifyArrayJson = request.getParameter("formfieldModifyArrayJson");
        //解析成修改字段数组
        List<Map> formfieldModifyArray = JSON.parseArray(formfieldModifyArrayJson, Map.class);
        String DEPART_ID = (String) depart.get("DEPART_ID");
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        depart = this.saveOrUpdateTreeData("PLAT_SYSTEM_DEPART",
                depart, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isNotEmpty(DEPART_ID)) {
            sysLogService.saveBackLog("单位部门管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + DEPART_ID + "]的部门信息", request, formfieldModifyArray, null, null);
        } else {
            DEPART_ID = (String) depart.get("DEPART_ID");
            sysLogService.saveBackLog("单位部门管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + DEPART_ID + "]的部门信息", request, formfieldModifyArray, null, null);
        }
        result.put("success", true);
        return result;
    }

    /**
     * 删除部门接口
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> deleteDepartCascadeUser(HttpServletRequest request) {
        String depId = request.getParameter("treeNodeId");
        String sql = PlatDbUtil.getDiskSqlContent("system/depart/001", null);
        List<List<String>> depList = dao.findListBySql(sql + PlatStringUtil.getSqlInCondition(depId), null, null);
        sysLogService.saveBackLog("单位部门管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + depId + "]的部门信息", request, null, "所属单位,部门表主键值,部门名称,部门编码", depList);
        this.deleteDepartCascadeUser(depId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }

    /**
     * 批量保存用户的部门信息
     */
    @Override
    public void saveUsers(String depart_id, String checkUserIds) {
        StringBuffer clearSql = new StringBuffer("");
        clearSql.append(" update PLAT_SYSTEM_SYSUSER set SYSUSER_DEPARTID='' where SYSUSER_DEPARTID=?  ");
        dao.executeSql(clearSql.toString(), new Object[]{depart_id});
        StringBuffer sql = new StringBuffer("");
        sql.append("update PLAT_SYSTEM_SYSUSER set SYSUSER_DEPARTID=? ");
        sql.append(" WHERE SYSUSER_ID IN ").append(
                PlatStringUtil.getValueArray(checkUserIds));
        dao.executeSql(sql.toString(), new Object[]{depart_id});

    }

    /**
     * 获取部门下的用户和无部门的用户数据
     */
    @Override
    public String getDepartAndUserAndAllJson(Map params) {
        Object treeDatas = this.getTreeData(params);
        String needCheckIds = (String) params.get("needCheckIds");
        String ROLE_IDS = (String) params.get("ROLE_IDS");
        String filterRule = (String) params.get("filterRule");
        Set<String> needCheckIdSet = new HashSet<String>();
        if (StringUtils.isNotEmpty(needCheckIds)) {
            needCheckIdSet = new HashSet<String>(Arrays.asList(needCheckIds.split(",")));
        }
        if (treeDatas instanceof Map) {
            Map<String, Object> rootNode = (Map<String, Object>) treeDatas;
            String departId = (String) rootNode.get("id");
            rootNode.put("nocheck", true);
            List<Map<String, Object>> childDepList = (List<Map<String, Object>>) rootNode.get("children");
            if (childDepList == null) {
                childDepList = new ArrayList<Map<String, Object>>();
            }
            for (Map<String, Object> child : childDepList) {
                String childDepId = (String) child.get("id");
                child.put("nocheck", true);
                List<Map<String, Object>> childDepList2 = (List<Map<String, Object>>) child.get("children");
                if (childDepList2 != null) {
                    this.constructChildUser(childDepList2, needCheckIdSet, ROLE_IDS);
                    /*=============cjr 20200417修改，若父节点ID不为0，则不获取父节点下的用户============*/
                    if ("0".equals(params.get("loadRootId"))) {
                        List<Map<String, Object>> userList = this.findUserList(childDepId,
                                needCheckIdSet, ROLE_IDS);
                        if (userList != null) {
                            childDepList2.addAll(userList);
                        }
                    }
                    /*List<Map<String, Object>> userList = this.findUserList(childDepId,
                            needCheckIdSet, ROLE_IDS);
                    if (userList != null) {
                        childDepList2.addAll(userList);
                    }*/
                } else {
                    childDepList2 = new ArrayList<Map<String, Object>>();
                    List<Map<String, Object>> userList = this.findUserList(childDepId, needCheckIdSet, ROLE_IDS);
                    if (userList != null) {
                        childDepList2.addAll(userList);
                        child.put("children", childDepList2);
                    }
                }
            }
            if (!"0".equals(departId)) {
                List<Map<String, Object>> userList = this.findUserList(departId, needCheckIdSet, ROLE_IDS);
                if (userList != null) {
                    childDepList.addAll(userList);
                }
            }
            String DEPART_COMPANYID = (String) params.get("Q_DEPART_COMPANYID_EQ");
            Map<String, Object> otherNode = new HashMap<String, Object>();
            List<Map<String, Object>> otherUserList = this.findNoDepartUser(DEPART_COMPANYID, needCheckIdSet, ROLE_IDS);
            otherNode.put("id", "-999999");
            otherNode.put("name", "未分配部门");
            otherNode.put("nocheck", true);
            if (otherUserList != null && otherUserList.size() > 0) {
                childDepList.add(otherNode);
                otherNode.put("children", otherUserList);
            }
        } else {

        }
        return JSON.toJSONString(treeDatas);
    }

    /**
     * 找到没有部门的用户
     *
     * @param DEPART_COMPANYID
     * @param needCheckIdSet
     * @param ROLE_IDS
     * @return
     */
    private List<Map<String, Object>> findNoDepartUser(String DEPART_COMPANYID,
                                                       Set<String> needCheckIdSet, String ROLE_IDS) {
        List<Map<String, Object>> userList = null;
        userList = sysUserService.findNoDepartIdByCompanyIdAndRoleIds(DEPART_COMPANYID, ROLE_IDS);
        if (userList != null && userList.size() > 0) {
            for (Map<String, Object> user : userList) {
                String userId = (String) user.get("SYSUSER_ID");
                if (needCheckIdSet.contains(userId)) {
                    user.put("checked", true);
                }
                user.put("id", user.get("SYSUSER_ID"));
                user.put("name", user.get("SYSUSER_NAME"));
            }
            return userList;
        } else {
            return null;
        }
    }

    /**
     * 分配用户接口
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> grantUsers(HttpServletRequest request) {
        String DEPART_ID = request.getParameter("DEPART_ID");
        String checkUserIds = request.getParameter("checkUserIds");
        this.saveUsers(DEPART_ID, checkUserIds);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }

    /**
     * 保存用户分管部门信息中间表
     *
     * @param
     * @param
     */
    @Override
    public void saveDepartDirector(String departId, String directorId, String directorName) {
        Map<String, Object> depart = this.getRecord("plat_system_depart", new String[]{"DEPART_ID"}, new Object[]{departId});
        if (depart != null) {
            depart.put("PRINCIPAL_USER1", directorId);
            dao.saveOrUpdate("plat_system_depart", depart, SysConstants.ID_GENERATOR_UUID, null);
        }
    }

    /**
     * 根据DepartId获取Director
     *
     * @param departId
     * @return
     */
    @Override
    public List<String> findDirectorListByDepartId(String departId) {
        return dao.findDirectorListByDepartId(departId);
    }

    /**
     * 设置部门主任
     * 根据filter获取用户网格项目
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map<String, Object>> findDeptDirectorList(SqlFilter sqlFilter) {
        List<Map<String, Object>> list = new ArrayList<>();
        StringBuffer sql = new StringBuffer(this.getUserInfoPreSql());
        List<Object> params = new ArrayList<>();
        String selectedUserIds = sqlFilter.getRequest().getParameter("selectedRecordIds");
        String iconFont = sqlFilter.getRequest().getParameter("iconfont");
        String itemConf = sqlFilter.getRequest().getParameter("itemconf");
        Map<String, String> getGridItemConf = PlatUICompUtil.getGridItemConfMap(itemConf);
        if (StringUtils.isNotEmpty(selectedUserIds)) {
            sql.append(" AND U.SYSUSER_ID IN ");
            sql.append(PlatStringUtil.getSqlInCondition(selectedUserIds));
            list = dao.findBySql(sql.toString(), params.toArray(), null);
            list = PlatUICompUtil.getGridItemList("SYSUSER_ID", iconFont, getGridItemConf, list);
        }
        return list;
    }

    /**
     * 保存部门承办人信息
     *
     * @param departId
     * @param takerId
     * @param takerName
     */
    @Override
    public void saveDepartTaker(String departId, String takerId, String takerName) {
        Map<String, Object> depart = this.getRecord("plat_system_depart", new String[]{"DEPART_ID"}, new Object[]{departId});
        if (depart != null) {
            depart.put("UNDERTAKE_USER", takerId);
            dao.saveOrUpdate("plat_system_depart", depart, SysConstants.ID_GENERATOR_UUID, null);
        }
    }

    /**
     * 根据DepartId获取Taker
     *
     * @param departId
     * @return
     */
    @Override
    public List<String> findTakerListByDepartId(String departId) {
        return dao.findTakerListByDepartId(departId);
    }

    /**
     * 设置部门承办人
     * 根据filter获取用户网格项目
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map<String, Object>> findDeptTakerList(SqlFilter sqlFilter) {
        List<Map<String, Object>> list = new ArrayList<>();
        StringBuffer sql = new StringBuffer(this.getUserInfoPreSql());
        List<Object> params = new ArrayList<>();
        String selectedUserIds = sqlFilter.getRequest().getParameter("selectedRecordIds");
        String iconFont = sqlFilter.getRequest().getParameter("iconfont");
        String itemConf = sqlFilter.getRequest().getParameter("itemconf");
        Map<String, String> getGridItemConf = PlatUICompUtil.getGridItemConfMap(itemConf);
        if (StringUtils.isNotEmpty(selectedUserIds)) {
            sql.append(" AND U.SYSUSER_ID IN ");
            sql.append(PlatStringUtil.getSqlInCondition(selectedUserIds));
            list = dao.findBySql(sql.toString(), params.toArray(), null);
            list = PlatUICompUtil.getGridItemList("SYSUSER_ID", iconFont, getGridItemConf, list);
        }
        return list;
    }

    /**
     * 根据用户ID获取分管的部门
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> findDepartListByUserId(String userId) {
        return dao.findDepartListByUserId(userId);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public String getUserInfoPreSql() {
        StringBuffer sql = new StringBuffer(" SELECT U.SYSUSER_ID,U.SYSUSER_ACCOUNT,U.SYSUSER_NAME,");
        sql.append(" U.SYSUSER_MOBILE,D.DEPART_ID,D.DEPART_NAME,C.COMPANY_ID,C.COMPANY_NAME ");
        sql.append(" FROM PLAT_SYSTEM_SYSUSER U LEFT JOIN PLAT_SYSTEM_COMPANY C ");
        sql.append(" ON U.SYSUSER_COMPANYID=C.COMPANY_ID LEFT JOIN PLAT_SYSTEM_DEPART D ");
        sql.append(" ON U.SYSUSER_DEPARTID=D.DEPART_ID ");
        sql.append(" WHERE U.SYSUSER_ACCOUNT NOT IN ('aqbmadmin','aqsjy','admin','yw') AND U.SYSUSER_STATUS=1 ");
        return sql.toString();
    }
}
