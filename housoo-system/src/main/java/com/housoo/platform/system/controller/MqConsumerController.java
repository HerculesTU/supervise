package com.housoo.platform.system.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.service.MqConsumerService;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.common.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述 消息消费者业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-11-24 14:01:26
 */
@Controller
@RequestMapping("/appmodel/MqConsumerController")
public class MqConsumerController extends BaseController {
    /**
     *
     */
    @Resource
    private MqConsumerService mqConsumerService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除消息消费者数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        mqConsumerService.deleteRecords("PLAT_APPMODEL_MQCONSUMER", "MQCONSUMER_ID", selectColValues.split(","));
        sysLogService.saveBackLog("消息中间件管理", SysLogService.OPER_TYPE_DEL,
                "删除了ID为[" + selectColValues + "]的消息消费者", request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改消息消费者数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> mqConsumer = PlatBeanUtil.getMapFromRequest(request);
        String MQCONSUMER_ID = (String) mqConsumer.get("MQCONSUMER_ID");
        if (StringUtils.isEmpty(MQCONSUMER_ID)) {
            mqConsumer.put("MQCONSUMER_CREATETIME", PlatDateTimeUtil.
                    formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        }
        mqConsumer = mqConsumerService.saveOrUpdate("PLAT_APPMODEL_MQCONSUMER",
                mqConsumer, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isNotEmpty(MQCONSUMER_ID)) {
            sysLogService.saveBackLog("消息中间件管理", SysLogService.OPER_TYPE_EDIT,
                    "修改了ID为[" + MQCONSUMER_ID + "]消息消费者", request);
        } else {
            MQCONSUMER_ID = (String) mqConsumer.get("MQCONSUMER_ID");
            sysLogService.saveBackLog("消息中间件管理", SysLogService.OPER_TYPE_ADD,
                    "新增了ID为[" + MQCONSUMER_ID + "]消息消费者", request);
        }
        mqConsumer.put("success", true);
        this.printObjectJsonString(mqConsumer, response);
    }

    /**
     * 跳转到消息消费者表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String MQCONSUMER_ID = request.getParameter("MQCONSUMER_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> mqConsumer = null;
        if (StringUtils.isNotEmpty(MQCONSUMER_ID)) {
            mqConsumer = this.mqConsumerService.getRecord("PLAT_APPMODEL_MQCONSUMER"
                    , new String[]{"MQCONSUMER_ID"}, new Object[]{MQCONSUMER_ID});
        } else {
            mqConsumer = new HashMap<String, Object>();
        }
        request.setAttribute("mqConsumer", mqConsumer);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }
}
