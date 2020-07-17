package com.housoo.platform.appmodel.controller;

import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.service.WordTplService;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import org.apache.commons.lang3.StringEscapeUtils;
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
 * 描述 WORD模版业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-05-31 10:59:58
 */
@Controller
@RequestMapping("/appmodel/WordTplController")
public class WordTplController extends BaseController {
    /**
     *
     */
    @Resource
    private WordTplService wordTplService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除WORD模版数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        wordTplService.deleteRecords("PLAT_APPMODEL_WORDTPL", "TPL_ID", selectColValues.split(","));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改WORD模版数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> wordTpl = PlatBeanUtil.getMapFromRequest(request);
        wordTpl = wordTplService.saveOrUpdate("PLAT_APPMODEL_WORDTPL",
                wordTpl, SysConstants.ID_GENERATOR_UUID, null);
        wordTpl.put("success", true);
        this.printObjectJsonString(wordTpl, response);
    }

    /**
     * 跳转到WORD模版表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String TPL_ID = request.getParameter("TPL_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> wordTpl = null;
        if (StringUtils.isNotEmpty(TPL_ID)) {
            wordTpl = this.wordTplService.getRecord("PLAT_APPMODEL_WORDTPL"
                    , new String[]{"TPL_ID"}, new Object[]{TPL_ID});
            //String TPL_CONTENT = (String) wordTpl.get("TPL_CONTENT");
            //wordTpl.put("TPL_CONTENT", StringEscapeUtils.escapeHtml3(TPL_CONTENT));
        } else {
            wordTpl = new HashMap<String, Object>();
        }
        request.setAttribute("wordTpl", wordTpl);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 跳转到编辑代码界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goEditCode")
    public ModelAndView goEditCode(HttpServletRequest request) {
        String TPL_ID = request.getParameter("TPL_ID");
        Map<String, Object> wordTpl = null;
        if (StringUtils.isNotEmpty(TPL_ID)) {
            wordTpl = this.wordTplService.getRecord("PLAT_APPMODEL_WORDTPL"
                    , new String[]{"TPL_ID"}, new Object[]{TPL_ID});
            String TPL_CONTENT = (String) wordTpl.get("TPL_CONTENT");
            wordTpl.put("TPL_CONTENT", StringEscapeUtils.escapeHtml3(TPL_CONTENT));
        }
        request.setAttribute("wordTpl", wordTpl);
        return PlatUICompUtil.goDesignUI("wordtpleditform", request);
    }
}
