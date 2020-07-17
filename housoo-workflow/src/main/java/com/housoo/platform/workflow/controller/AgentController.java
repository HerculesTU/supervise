package com.housoo.platform.workflow.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.workflow.service.AgentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述 工作委托业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-27 16:49:15
 */
@Controller
@RequestMapping("/workflow/AgentController")
public class AgentController extends BaseController {
    /**
     *
     */
    @Resource
    private AgentService agentService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除工作委托数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        agentService.deleteRecords("JBPM6_AGENT", "AGENT_ID", selectColValues.split(","));
        sysLogService.saveBackLog("工作委托管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的工作委托", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改工作委托数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> agent = PlatBeanUtil.getMapFromRequest(request);
        String AGENT_ID = (String) agent.get("AGENT_ID");
        String AGENT_USERID_LABELS = (String) agent.get("AGENT_USERID_LABELS");
        String AGENT_PROXYERID_LABELS = (String) agent.get("AGENT_PROXYERID_LABELS");
        String AGENT_DEFIDS_LABELS = (String) agent.get("AGENT_DEFIDS_LABELS");
        agent.put("AGENT_USERNAME", AGENT_USERID_LABELS);
        agent.put("AGENT_PROXYERNAME", AGENT_PROXYERID_LABELS);
        agent.put("AGENT_DEFNAMES", AGENT_DEFIDS_LABELS);
        if (StringUtils.isEmpty(AGENT_ID)) {
            String AGENT_USERID = (String) agent.get("AGENT_USERID");
            boolean isExistConfig = agentService.isExistsConfig(AGENT_USERID);
            if (isExistConfig) {
                agent.put("success", false);
                agent.put("msg", "该委托人已经有代理人,不允许重复创建!");
                this.printObjectJsonString(agent, response);
                return;
            }
            Map<String, Object> curUser = PlatAppUtil.getBackPlatLoginUser();
            agent.put("AGENT_CREATORID", curUser.get("SYSUSER_ID"));

        }
        agent = agentService.saveOrUpdate("JBPM6_AGENT",
                agent, SysConstants.ID_GENERATOR_UUID, null);
        agent.put("success", true);
        this.printObjectJsonString(agent, response);
    }

    /**
     * 跳转到工作委托表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String AGENT_ID = request.getParameter("AGENT_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> agent = null;
        if (StringUtils.isNotEmpty(AGENT_ID)) {
            agent = this.agentService.getRecord("JBPM6_AGENT"
                    , new String[]{"AGENT_ID"}, new Object[]{AGENT_ID});
        } else {
            agent = new HashMap<String, Object>();
            Map<String, Object> backloginUser = PlatAppUtil.getBackPlatLoginUser();
            agent.put("AGENT_USERID", backloginUser.get("SYSUSER_ID"));
            agent.put("AGENT_USERNAME", backloginUser.get("SYSUSER_NAME"));
        }
        request.setAttribute("agent", agent);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 跳转到选择流程定义界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goSelectDef")
    public ModelAndView goSelectDef(HttpServletRequest request) {
        String AGENT_ID = request.getParameter("AGENT_ID");
        String selectedRecordIds = "";
        if (StringUtils.isNotEmpty(AGENT_ID)) {
            Map<String, Object> agent = this.agentService.
                    getRecord("JBPM6_AGENT", new String[]{"AGENT_ID"}, new Object[]{AGENT_ID});
            selectedRecordIds = (String) agent.get("AGENT_DEFIDS");
        }
        request.setAttribute("selectedRecordIds", selectedRecordIds.toString());
        return PlatUICompUtil.goDesignUI("flowdefselector", request);
    }
}
