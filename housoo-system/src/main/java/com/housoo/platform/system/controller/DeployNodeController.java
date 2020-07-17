package com.housoo.platform.system.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.DeployNodeService;

/**
 * 描述 部署节点列表业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2018-04-19 16:15:13
 */
@Controller
@RequestMapping("/system/DeployNodeController")
public class DeployNodeController extends BaseController {
    /**
     *
     */
    @Resource
    private DeployNodeService deployNodeService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除部署节点列表数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        deployNodeService.deleteRecords("PLAT_SYSTEM_DEPLOYNODE", "DEPLOYNODE_ID", selectColValues.split(","));
        sysLogService.saveBackLog("自动化部署管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的部署节点列表", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改部署节点列表数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> deployNode = PlatBeanUtil.getMapFromRequest(request);
        String DEPLOYNODE_ID = (String) deployNode.get("DEPLOYNODE_ID");
        if (StringUtils.isEmpty(DEPLOYNODE_ID)) {
            deployNode.put("DEPLOYNODE_TIME", PlatDateTimeUtil.formatDate(
                    new Date(), "yyyy-MM-dd HH:mm:ss"));
        }
        deployNode = deployNodeService.saveOrUpdate("PLAT_SYSTEM_DEPLOYNODE",
                deployNode, SysConstants.ID_GENERATOR_UUID, null);
        deployNode.put("success", true);
        this.printObjectJsonString(deployNode, response);
    }

    /**
     * 跳转到部署节点列表表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String DEPLOYNODE_ID = request.getParameter("DEPLOYNODE_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> deployNode = null;
        if (StringUtils.isNotEmpty(DEPLOYNODE_ID)) {
            deployNode = this.deployNodeService.getRecord("PLAT_SYSTEM_DEPLOYNODE"
                    , new String[]{"DEPLOYNODE_ID"}, new Object[]{DEPLOYNODE_ID});
        } else {
            deployNode = new HashMap<String, Object>();
        }
        request.setAttribute("deployNode", deployNode);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
