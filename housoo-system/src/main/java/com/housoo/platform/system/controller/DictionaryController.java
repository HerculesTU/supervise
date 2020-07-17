package com.housoo.platform.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.mq.QueueSender;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatFileUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.DicAttachService;
import com.housoo.platform.core.service.DicTypeService;
import com.housoo.platform.core.service.DictionaryService;

/**
 * 描述
 *
 * @author housoo
 * @created 2017年2月1日 下午6:03:29
 */
@Controller
@RequestMapping("/system/DictionaryController")
public class DictionaryController extends BaseController {

    /**
     *
     */
    @Resource
    private DictionaryService dictionaryService;
    /**
     *
     */
    @Resource
    private DicTypeService dicTypeService;
    /**
     *
     */
    @Resource
    private DicAttachService dicAttachService;

    /**
     * 更新排序值
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "updateSn")
    public void updateSn(HttpServletRequest request,
                         HttpServletResponse response) {
        String dicIds = request.getParameter("dicIds");
        dictionaryService.updateSn(dicIds.split(","));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 删除字典信息
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        dictionaryService.deleteRecords("PLAT_SYSTEM_DICTIONARY", "DIC_ID", selectColValues.split(","));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }


    /**
     * 新增或者修改字典类别
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        String ATTACH_JSON = request.getParameter("ATTACH_JSON");
        Map<String, Object> dictionary = PlatBeanUtil.getMapFromRequest(request);
        String DIC_ID = (String) dictionary.get("DIC_ID");
        if (StringUtils.isEmpty(DIC_ID)) {
            String DIC_DICTYPE_CODE = (String) dictionary.get("DIC_DICTYPE_CODE");
            int dicSn = dictionaryService.getMaxSn(DIC_DICTYPE_CODE);
            dictionary.put("DIC_SN", dicSn);
        }
        dictionary = dictionaryService.saveOrUpdate("PLAT_SYSTEM_DICTIONARY", dictionary,
                SysConstants.ID_GENERATOR_UUID, null);
        DIC_ID = (String) dictionary.get("DIC_ID");
        dicAttachService.saveDicAttachs(DIC_ID, ATTACH_JSON);
        String DIC_DICTYPE_CODE = (String) dictionary.get("DIC_DICTYPE_CODE");
        if ("VALID_RULE".equals(DIC_DICTYPE_CODE)) {
            PlatAppUtil.setValidRules(dictionaryService.getJsValidRules());
        }
        dictionary.put("success", true);
        this.printObjectJsonString(dictionary, response);
    }


    /**
     * 跳转到表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goform")
    public ModelAndView goform(HttpServletRequest request) {
        String DIC_ID = request.getParameter("DIC_ID");
        String dicTypeId = request.getParameter("dicTypeId");
        Map<String, Object> dictionary = null;
        Map<String, Object> dicType = null;
        if (StringUtils.isNotEmpty(DIC_ID)) {
            dictionary = this.dictionaryService.getRecord("PLAT_SYSTEM_DICTIONARY",
                    new String[]{"DIC_ID"}, new Object[]{DIC_ID});
            dicType = dicTypeService.getRecord("PLAT_SYSTEM_DICTYPE"
                    , new String[]{"DICTYPE_CODE"}, new Object[]{dictionary.get("DIC_DICTYPE_CODE")});
        } else {
            dictionary = new HashMap<String, Object>();
            dicType = dicTypeService.getRecord("PLAT_SYSTEM_DICTYPE"
                    , new String[]{"DICTYPE_ID"}, new Object[]{dicTypeId});
        }
        dictionary.put("DIC_DICTYPE_CODE", dicType.get("DICTYPE_CODE"));
        dictionary.put("DICTYPE_NAME", dicType.get("DICTYPE_NAME"));
        request.setAttribute("dictionary", dictionary);
        return PlatUICompUtil.goDesignUI("dictionaryform", request);
    }

    /**
     * 验证字典值是否存在
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "validDicValue")
    public void validDicValue(HttpServletRequest request,
                              HttpServletResponse response) {
        String DIC_ID = request.getParameter("DIC_ID");
        String DICTYPE_CODE = request.getParameter("DIC_DICTYPE_CODE");
        String DIC_VALUE = request.getParameter("DIC_VALUE");
        boolean isExists = dictionaryService.isExistsDic(DIC_ID, DICTYPE_CODE, DIC_VALUE);
        Map<String, String> result = new HashMap<String, String>();
        if (isExists) {
            result.put("error", "该类别下已存在该字典值,请重新输入!");
        } else {
            result.put("ok", "");
        }
        this.printObjectJsonString(result, response);
    }

    /**
     * 获取js验证规则配置信息
     *
     * @param request
     * @param response
     */
    @RequestMapping("/jsValidRules")
    public void jsValidRules(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, String[]> map = PlatAppUtil.getValidRules();
        String json = JSON.toJSONString(map);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        result.put("rules", json);
        this.printObjectJsonString(result, response);
    }
}
