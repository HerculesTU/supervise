package com.housoo.platform.system.controller;

import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.DepartService;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.UserDepartService;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.util.SysConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 保存用户分管部门业务相关Controller
 *
 * @author wh
 * @version 1.0
 * @created 2019-05-06 15:29:55
 */
@Controller
@RequestMapping("/system/UserDepartController")
public class UserDepartController extends BaseController {

    /**
     *
     */
    @Resource
    private DepartService departService;
    /**
     * userDepartService
     */
    @Resource
    private UserDepartService userDepartService;

    /**
     * 系统日志Service
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除部门分管负责人数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request, HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        userDepartService.deleteRecords("TB_USER_DEPART", "SYSUSER_ID", selectColValues.split(","));
        sysLogService.saveBackLog("业务管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的部门分管负责人", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改部门分管负责人数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> userDepart = PlatBeanUtil.getMapFromRequest(request);
        String SYSUSER_ID = (String) userDepart.get("SYSUSER_ID");
        userDepart = userDepartService.saveOrUpdate("TB_USER_DEPART",
                userDepart, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //userDepart = userDepartService.saveOrUpdateTreeData("TB_USER_DEPART",
        //userDepart,SysConstants.ID_GENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(SYSUSER_ID)) {
            sysLogService.saveBackLog("业务管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + SYSUSER_ID + "]部门分管负责人", request);
        } else {
            SYSUSER_ID = (String) userDepart.get("SYSUSER_ID");
            sysLogService.saveBackLog("业务管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + SYSUSER_ID + "]部门分管负责人", request);
        }
        userDepart.put("success", true);
        this.printObjectJsonString(userDepart, response);
    }

    /**
     * 跳转到部门分管负责人表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String SYSUSER_ID = request.getParameter("SYSUSER_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> userDepart = null;
        if (StringUtils.isNotEmpty(SYSUSER_ID)) {
            userDepart = this.userDepartService.getRecord("TB_USER_DEPART"
                    , new String[]{"SYSUSER_ID"}, new Object[]{SYSUSER_ID});
        } else {
            userDepart = new HashMap
                    <String, Object>();
        }
        request.setAttribute("userDepart", userDepart);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*
        String SYSUSER_ID = request.getParameter("SYSUSER_ID");
        String USERDEPART_PARENTID = request.getParameter("USERDEPART_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map <String,Object> userDepart = null;
        if(StringUtils.isNotEmpty(SYSUSER_ID)){
            userDepart = this.userDepartService.getRecord("TB_USER_DEPART"
            ,new String[]{"SYSUSER_ID"},new Object[]{SYSUSER_ID});
            USERDEPART_PARENTID = (String) userDepart.get("UserDepart_PARENTID");
        }
        Map <String,Object> parentUserDepart = null;
        if(USERDEPART_PARENTID.equals("0")){
            parentUserDepart = new HashMap
            <String,Object>();
            parentUserDepart.put("SYSUSER_ID",USERDEPART_PARENTID);
            parentUserDepart.put("USERDEPART_NAME","部门分管负责人树");
            }else{
            parentUserDepart = this.userDepartService.getRecord("TB_USER_DEPART",
            new String[]{"SYSUSER_ID"}, new Object[]{USERDEPART_PARENTID});
        }
        if(userDepart==null){
            userDepart = new HashMap
            <String,Object>();
        }
        userDepart.put("USERDEPART_PARENTID",parentUserDepart.get("SYSUSER_ID"));
        userDepart.put("USERDEPART_PARENTNAME",parentUserDepart.get("USERDEPART_NAME"));
        request.setAttribute("userDepart", userDepart);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        */
    }

    /**
     * 跳转到用户分管部门设置页面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goUserDepartGrant")
    public ModelAndView goUserDepartGrant(HttpServletRequest request) {
        String SYSUSER_ID = request.getParameter("SYSUSER_ID");
        Map<String, Object> user = this.departService.getRecord("PLAT_SYSTEM_SYSUSER"
                , new String[]{"SYSUSER_ID"}, new Object[]{SYSUSER_ID});
        String COMPANY_ID = user.get("SYSUSER_COMPANYID").toString();
        List<String> userIds = departService.findDepartListByUserId(SYSUSER_ID);
        StringBuffer selectedRecordIds = new StringBuffer("");
        for (int i = 0; i < userIds.size(); i++) {
            if (i > 0) {
                selectedRecordIds.append(",");
            }
            selectedRecordIds.append(userIds.get(i));
        }
        request.setAttribute("selectedRecordIds", selectedRecordIds.toString());
        request.setAttribute("COMPANY_ID", COMPANY_ID);
        return PlatUICompUtil.goDesignUI("userDepart", request);
    }

    /**
     * 领导分管部门设置
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "grantUserDepart")
    public void grantUserDepart(HttpServletRequest request, HttpServletResponse response) {
        String SYSUSER_ID = request.getParameter("SYSUSER_ID");
        String checkUserIds = request.getParameter("checkUserIds");
        userDepartService.saveDepart(SYSUSER_ID, checkUserIds);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 根据部门ID获取分管领导信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "getDepartLeader")
    public void getDepartLeader(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        String departId = request.getParameter("applyDeptId");
        /*Map<String, Object> departLeader = userDepartService.getDepartLeader(departId);
        if (departLeader != null) {

            result.put("success", true);
            result.put("departLeader", departLeader);
        } else {
            result.put("success", false);
        }*/
        this.printObjectJsonString(result, response);

    }
}
