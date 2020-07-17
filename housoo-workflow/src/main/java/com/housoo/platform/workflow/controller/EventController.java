/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.controller;

import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.workflow.service.EventService;
import com.housoo.platform.workflow.service.NodeBindService;
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
 * 描述 流程事件业务相关Controller
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-05-08 09:43:22
 */
@Controller
@RequestMapping("/workflow/EventController")
public class EventController extends BaseController {
    /**
     *
     */
    @Resource
    private EventService eventService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;
    /**
     *
     */
    @Resource
    private NodeBindService nodeBindService;

    /**
     * 删除流程事件数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        eventService.deleteRecords("JBPM6_EVENT", "EVENT_ID", selectColValues.split(","));
        sysLogService.saveBackLog("流程定义管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的流程事件", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改流程事件数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> event = PlatBeanUtil.getMapFromRequest(request);
        event = eventService.saveEventCascade(event);
        event.put("success", true);
        this.printObjectJsonString(event, response);
    }

    /**
     * 跳转到流程事件表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String EVENT_ID = request.getParameter("EVENT_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        String FLOWDEF_ID = request.getParameter("FLOWDEF_ID");
        String FLOWDEF_VERSION = request.getParameter("FLOWDEF_VERSION");
        Map<String, Object> event = null;
        if (StringUtils.isNotEmpty(EVENT_ID)) {
            event = this.eventService.getRecord("JBPM6_EVENT"
                    , new String[]{"EVENT_ID"}, new Object[]{EVENT_ID});
            List<Map<String, Object>> bindNodeList = nodeBindService.findBindNodeList(FLOWDEF_ID,
                    Integer.parseInt(FLOWDEF_VERSION), EVENT_ID, NodeBindService.BINDTYPE_EVENT);
            StringBuffer NODE_KEYS = new StringBuffer("");
            StringBuffer NODE_NAMES = new StringBuffer("");
            for (int i = 0; i < bindNodeList.size(); i++) {
                if (i > 0) {
                    NODE_KEYS.append(",");
                    NODE_NAMES.append(",");
                }
                NODE_KEYS.append(bindNodeList.get(i).get("NODE_KEY"));
                NODE_NAMES.append(bindNodeList.get(i).get("NODE_NAME"));
            }
            event.put("EVENT_NODEKEYS", NODE_KEYS.toString());
            event.put("EVENT_NODENAMES", NODE_NAMES.toString());
        } else {
            event = new HashMap<String, Object>();
            event.put("EVENT_FLOWDEFID", FLOWDEF_ID);
            event.put("EVENT_FLOWVERSION", FLOWDEF_VERSION);
        }
        event.put("FLOWDEF_ID", FLOWDEF_ID);
        event.put("FLOWDEF_VERSION", FLOWDEF_VERSION);
        request.setAttribute("event", event);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
