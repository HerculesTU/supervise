package com.housoo.platform.supervise.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.housoo.platform.core.util.PlatAppUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.supervise.service.SuperviseClazzService;

/**
 * 描述 督办类型业务相关Controller
 *
 * @author zxl
 * @version 1.0
 * @created 2020-04-13 11:28:30
 */
@Controller
@RequestMapping("/supervise/SuperviseClazzController")
public class SuperviseClazzController extends BaseController {
    /**
     * SuperviseClazzService
     */
    @Resource
    private SuperviseClazzService superviseClazzService;
    /**
     * 系统日志Service
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除督办类型数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        superviseClazzService.deleteRecords("TB_SUPERVISE_CLAZZ", "RECORD_ID", selectColValues.split(","));
        sysLogService.saveBackLog("督办类型", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的督办类型", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改督办类型数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> superviseClazz = PlatBeanUtil.getMapFromRequest(request);
        String RECORD_ID = (String) superviseClazz.get("RECORD_ID");
        Map<String, Object> loginUser = PlatAppUtil.getBackPlatLoginUser();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (StringUtils.isNotEmpty(RECORD_ID)) {
            superviseClazz.put("update_by", loginUser.get("SYSUSER_NAME"));
            superviseClazz.put("update_time", simpleDateFormat.format(new Date()));
        } else {
            superviseClazz.put("create_by", loginUser.get("SYSUSER_NAME"));
            superviseClazz.put("create_time", simpleDateFormat.format(new Date()));
            superviseClazz.put("update_by", "");
            superviseClazz.put("update_time", "");
        }
        superviseClazz = superviseClazzService.saveOrUpdate("TB_SUPERVISE_CLAZZ",
                superviseClazz, SysConstants.ID_GENERATOR_UUID, null);
        //如果是保存树形结构表数据,请调用下面的接口,而注释掉上面的接口代码
        //superviseClazz = superviseClazzService.saveOrUpdateTreeData("TB_SUPERVISE_CLAZZ",
        //        superviseClazz, SysConstants.ID_GENERATOR_UUID,null);
        if (StringUtils.isNotEmpty(RECORD_ID)) {
            sysLogService.saveBackLog("督办类型", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + RECORD_ID + "]督办类型", request);
        } else {
            RECORD_ID = (String) superviseClazz.get("RECORD_ID");
            sysLogService.saveBackLog("督办类型", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + RECORD_ID + "]督办类型", request);
        }
        superviseClazz.put("success", true);
        this.printObjectJsonString(superviseClazz, response);
    }

    /**
     * 跳转到督办类型表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String RECORD_ID = request.getParameter("RECORD_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> superviseClazz = null;
        if (StringUtils.isNotEmpty(RECORD_ID)) {
            superviseClazz = this.superviseClazzService.getRecord("TB_SUPERVISE_CLAZZ"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
        } else {
            superviseClazz = new HashMap<String, Object>();
        }
        request.setAttribute("superviseClazz", superviseClazz);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
        //如果是跳转到树形表单录入界面,请开放以下代码,,而注释掉上面的代码
        /*String RECORD_ID = request.getParameter("RECORD_ID");
        String SUPERVISECLAZZ_PARENTID = request.getParameter("SUPERVISECLAZZ_PARENTID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String,Object> superviseClazz = null;
        if(StringUtils.isNotEmpty(RECORD_ID)){
            superviseClazz = this.superviseClazzService.getRecord("TB_SUPERVISE_CLAZZ"
                    , new String[]{"RECORD_ID"}, new Object[]{RECORD_ID});
            SUPERVISECLAZZ_PARENTID = (String) superviseClazz.get("SuperviseClazz_PARENTID");
        }
        Map<String,Object> parentSuperviseClazz = null;
        if(SUPERVISECLAZZ_PARENTID.equals("0")){
            parentSuperviseClazz = new HashMap<String,Object>();
            parentSuperviseClazz.put("RECORD_ID", SUPERVISECLAZZ_PARENTID);
            parentSuperviseClazz.put("SUPERVISECLAZZ_NAME", "督办类型树");
        }else{
            parentSuperviseClazz = this.superviseClazzService.getRecord("TB_SUPERVISE_CLAZZ",
                    new String[]{"RECORD_ID"}, new Object[]{SUPERVISECLAZZ_PARENTID});
        }
        if(superviseClazz==null){
            superviseClazz = new HashMap<String,Object>();
        }
        superviseClazz.put("SUPERVISECLAZZ_PARENTID", parentSuperviseClazz.get("RECORD_ID"));
        superviseClazz.put("SUPERVISECLAZZ_PARENTNAME", parentSuperviseClazz.get("SUPERVISECLAZZ_NAME"));
        request.setAttribute("superviseClazz", superviseClazz);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);*/
    }

    /**
     * 获取所有督办类型
     */
    public void getAllSuperviseClazz() {

    }
}
