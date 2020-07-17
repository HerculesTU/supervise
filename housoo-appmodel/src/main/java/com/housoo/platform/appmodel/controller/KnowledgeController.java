package com.housoo.platform.appmodel.controller;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.model.PagingBean;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.core.service.KnowledgeService;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.util.LuceneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 技术知识业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-06-29 09:57:28
 */
@Controller
@RequestMapping("/appmodel/KnowledgeController")
public class KnowledgeController extends BaseController {
    /**
     *
     */
    @Resource
    private KnowledgeService knowledgeService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 删除技术知识数据
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        knowledgeService.deleteCascadeIndex(selectColValues.split(","));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 新增或者修改技术知识数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveOrUpdate")
    public void saveOrUpdate(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> knowledge = PlatBeanUtil.getMapFromRequest(request);
        knowledge = knowledgeService.saveOrUpdateCascadeIndex(knowledge);
        knowledge.put("success", true);
        this.printObjectJsonString(knowledge, response);
    }

    /**
     * 跳转到技术知识表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goForm")
    public ModelAndView goForm(HttpServletRequest request) {
        String KNOWLEDGE_ID = request.getParameter("KNOWLEDGE_ID");
        //获取设计的界面编码
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> knowledge = null;
        if (StringUtils.isNotEmpty(KNOWLEDGE_ID)) {
            knowledge = this.knowledgeService.getRecord("PLAT_APPMODEL_KNOWLEDGE"
                    , new String[]{"KNOWLEDGE_ID"}, new Object[]{KNOWLEDGE_ID});
            //String KNOWLEDGE_CONTENT = (String) knowledge.get("KNOWLEDGE_CONTENT");
            //knowledge.put("KNOWLEDGE_CONTENT", StringEscapeUtils.escapeHtml3(KNOWLEDGE_CONTENT));
        } else {
            knowledge = new HashMap<String, Object>();
        }
        request.setAttribute("knowledge", knowledge);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 跳转到文章详细页
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goDetail")
    public ModelAndView goDetail(HttpServletRequest request) {
        String KNOWLEDGE_ID = request.getParameter("KNOWLEDGE_ID");
        String RECORD_ID = request.getParameter("RECORD_ID");
        if (StringUtils.isNotEmpty(RECORD_ID)) {
            KNOWLEDGE_ID = RECORD_ID;
        }
        Map<String, Object> knowledge = this.knowledgeService.getRecord("PLAT_APPMODEL_KNOWLEDGE"
                , new String[]{"KNOWLEDGE_ID"}, new Object[]{KNOWLEDGE_ID});
        request.setAttribute("knowledge", knowledge);
        return new ModelAndView("background/appmodel/techknowledge/knowledgeform");
    }

    /**
     * 跳转到搜索界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goSearch")
    public ModelAndView goSearch(HttpServletRequest request) {
        return new ModelAndView("background/appmodel/techknowledge/searchlist");
    }

    /**
     * 自动补全知识列表
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "autoKnowledage")
    public void autoKnowledage(HttpServletRequest request,
                               HttpServletResponse response) {
        //如果自动补全的类型为2,那么获取到key之后进行判断过滤
        //String keyword = request.getParameter("keyword");
        PagingBean pb = new PagingBean(0, 100000);
        pb.setCurrentPage(1);
        List<Map<String, Object>> list = new LuceneUtil().findList(null, "1", pb);
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : list) {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("VALUE", map.get("FULLTEXT_INDEXTITLE"));
            data.put("LABEL", map.get("FULLTEXT_INDEXTITLE"));
            datas.add(data);
        }
        String json = JSON.toJSONString(datas);
        this.printJsonString(json.toLowerCase(), response);
    }

    /**
     * 获取列表的JSON数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "datagrid")
    public void datagrid(HttpServletRequest request,
                         HttpServletResponse response) {
        SqlFilter filter = new SqlFilter(request);
        String keyword = request.getParameter("keyword");
        if (!StringUtils.isNotEmpty(keyword)) {
            keyword = null;
        }
        List<Map<String, Object>> list = new LuceneUtil().findList(keyword, "1", filter.getPagingBean());
        this.printListJsonString(filter.getPagingBean(), list, response);
    }
}
