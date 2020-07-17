package com.housoo.platform.supervise.controller;

import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.DesignService;
import com.housoo.platform.core.util.PlatUICompUtil;
import com.housoo.platform.supervise.service.SponsorService;
import com.housoo.platform.supervise.service.SuperviseClazzService;
import com.housoo.platform.supervise.service.SuperviseMqService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 督查督办业务 立项人 相关Controller
 *
 * @author zxl
 * @version 1.0
 * @created 2020-04-22 15:30:11
 */
@Controller
@RequestMapping("/supervise/SponsorController")
public class SponsorController extends BaseController {
    /**
     * sponsorService
     */
    @Resource
    private SponsorService sponsorService;
    /**
     * superviseClazzService
     */
    @Resource
    private SuperviseClazzService superviseClazzService;
    /**
     * designService
     */
    @Resource
    private DesignService designService;
    /**
     * SuperviseMqService
     */
    @Resource
    private SuperviseMqService superviseMqService;

    /*--------------------------------立项人首页统计相关------------------------------------*/


    /**
     * 获取立项人首页扇形统计数据(发起督办数量)
     */
    @RequestMapping(params = "getSponsorIndexSectorCommitData")
    public void getSponsorIndexSectorCommitData(HttpServletRequest request,
                                                HttpServletResponse response) {
        List<Map<String, Object>> result = this.sponsorService.getSponsorIndexSectorCommitData(request);
        this.printObjectJsonString(result, response);
    }

    /**
     * 获取立项人首页扇形统计数据(进行中督办数量)
     */
    @RequestMapping(params = "getSponsorIndexSectorProcessData")
    public void getSponsorIndexSectorProcessData(HttpServletRequest request,
                                                 HttpServletResponse response) {
        List<Map<String, Object>> result = this.sponsorService.getSponsorIndexSectorProcessData(request);
        this.printObjectJsonString(result, response);
    }

    /**
     * 获取立项人首页扇形统计数据(结束的督办数量)
     */
    @RequestMapping(params = "getSponsorIndexSectorEndData")
    public void getSponsorIndexSectorEndData(HttpServletRequest request,
                                             HttpServletResponse response) {
        List<Map<String, Object>> result = this.sponsorService.getSponsorIndexSectorEndData(request);
        this.printObjectJsonString(result, response);
    }

    /**
     * 获取立项人首页扇形统计数据(逾期的督办数量)
     */
    @RequestMapping(params = "getSponsorIndexSectorOutTimeData")
    public void getSponsorIndexSectorOutTimeData(HttpServletRequest request,
                                                 HttpServletResponse response) {
        List<Map<String, Object>> result = this.sponsorService.getSponsorIndexSectorOutTimeData(request);
        this.printObjectJsonString(result, response);
    }

    /**
     * 获取立项人首页数值型统计数据
     * cjr
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "getSponsorIndexNumericData")
    public void getSponsorIndexNumericData(HttpServletRequest request,
                                           HttpServletResponse response) {
        Map<String, Object> result = this.sponsorService.getSponsorIndexNumericData(request);
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 获取立项人首页柱线图统计数据
     * cjr
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "findSponsorIndexLineData")
    public void findSponsorIndexLineData(HttpServletRequest request,
                                         HttpServletResponse response) {
        List<Map<String, Object>> result = this.sponsorService.findSponsorIndexLineData(request);
        this.printObjectJsonString(result, response);
    }

    /**
     * 获取立项人首页部门督办列表数据
     * cjr
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "findSponsorIndexTableData")
    public void findSponsorIndexTableData(HttpServletRequest request,
                                          HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> clazz = superviseClazzService.getAllSuperviseClazz(null);
        List<Map<String, Object>> data = this.sponsorService.findSponsorIndexTableData(request);
        result.put("success", true);
        result.put("clazz", clazz);
        result.put("data", data);
        this.printObjectJsonString(result, response);
    }

    /**
     * 获取立项人首页逾期统计数据
     * cjr
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "findSponsorIndexOutDateData")
    public void findSponsorIndexOutDateData(HttpServletRequest request,
                                            HttpServletResponse response) {
        Map<String, Object> result = this.sponsorService.findSponsorIndexOutDateData(request);
        if (result == null || result.size() == 0) {
            result = new HashMap<>();
            result.put("dbfk", 0);
            result.put("blfk", 0);
            result.put("dbqr", 0);
            result.put("bjfk", 0);
        }
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到督办事项列表页（立项人）
     *
     * @param request
     */
    @RequestMapping(params = "goSuperviseList")
    public ModelAndView goSuperviseList(HttpServletRequest request) {
        String clazz = request.getParameter("clazz");
        request.setAttribute("clazz", clazz);
        return new ModelAndView("background/supervise/supMatter");
    }

    /**
     * 跳转到督办事项详情页（立项人）
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goSuperviseInfo")
    public ModelAndView goSuperviseInfo(HttpServletRequest request) {
        sponsorService.goSuperviseInfo(request);
        request.setAttribute("parentFrameId", request.getParameter("parentFrameId"));
        return new ModelAndView("background/supervise/reply");
    }

    /**
     * 获取立项人创建的督办任务列表
     * cjr
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "createdSuperviseList")
    public void createdSuperviseList(HttpServletRequest request,
                                     HttpServletResponse response) {
        SqlFilter sqlFilter = new SqlFilter(request);
        List<Map<String, Object>> list = this.sponsorService.createdSuperviseList(sqlFilter);
        this.printListJsonString(sqlFilter.getPagingBean(), list, response);
    }


    /**
     * 立项人处理反馈
     * cjr
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "sponsorHandleFeedback")
    public void sponsorRejectFeedback(HttpServletRequest request,
                                      HttpServletResponse response) {
        Map<String, Object> result = this.sponsorService.sponsorHandleFeedback(request);
        //推送到消息队列
        /*Map<Object, Object> pushInfo = superviseMqService.pushSponsorHandleInfo(result);
        if ((Boolean) pushInfo.get("success")) {
            result.put("success", true);
        } else {
            result.put("msg", "消息推送失败！");
            result.put("success", false);
        }*/
        result.put("success", true);
        result.put("parentFrameId", request.getParameter("parentFrameId"));
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到事项办结列表页（立项人）
     *
     * @param request
     */
    @RequestMapping(params = "goSponsorSupEndList")
    public ModelAndView goSponsorSupEnd(HttpServletRequest request) {
        return new ModelAndView("background/supervise/supEndList");
    }

    /**
     * 跳转到事项办结 督办详情页（立项人）
     *
     * @param request
     */
    @RequestMapping(params = "goSponsorSupEndInfo")
    public ModelAndView goSponsorSupEndInfo(HttpServletRequest request) {
        return new ModelAndView("background/supervise/supEnd");
    }


    /**
     * 获取当前登录人建立的所有督察督办事项
     */
    @RequestMapping(params = "findSuperviseListByLoginUser")
    public void findSuperviseListByLoginUser(HttpServletRequest request,
                                             HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        List<List<Map<String, Object>>> superviseInfo = sponsorService.findSuperviseListByLoginUser(request);
        result.put("superviseInfo", superviseInfo);
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 下载 立项人上传附件
     * zxl
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "downloadSponsorFile")
    public void downloadSponsorFile(HttpServletRequest request,
                                    HttpServletResponse response) {
        Map<String, Object> result = sponsorService.downloadSponsorFile(request);
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到生成的UI界面(督查督办 查看草稿)
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goGenUiViewDraft")
    public ModelAndView goGenUiViewDraft(HttpServletRequest request) {
        String id = request.getParameter("recordId");
        Map<String, Object> supervise = designService.getRecord("tb_supervise",
                new String[]{"RECORD_ID"}, new Object[]{id});
        request.setAttribute("supervise", supervise);
        Map<String, Object> task = designService.getRecord("tb_supervise_task",
                new String[]{"SUPERVISE_ID"}, new Object[]{id});
        request.setAttribute("task", task);
        String DESIGN_CODE = request.getParameter("DESIGN_CODE");
        request.setAttribute("parentFrameId", request.getParameter("parentFrameId"));
        return PlatUICompUtil.goDesignUI(DESIGN_CODE, request);
    }

    /**
     * 跳转到生成的UI界面(督查督办 新建表单)
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goGenUiView")
    public ModelAndView goGenUiView(HttpServletRequest request) {
        String clazz = request.getParameter("clazz");
        Map<String, Object> supervise = new HashMap<>();
        supervise.put("SUPERVISE_CLAZZ_ID", clazz);
        request.setAttribute("SUPERVISE_CLAZZ_ID", clazz);
        request.setAttribute("supervise", supervise);
        String DESIGN_CODE = request.getParameter("DESIGN_CODE");
        request.setAttribute("parentFrameId", request.getParameter("parentFrameId"));
        return PlatUICompUtil.goDesignUI(DESIGN_CODE, request);
    }

    /**
     * 加载事项办结(立项人)的数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "findSponsorSupEndList")
    public void findSponsorSupEndList(HttpServletRequest request,
                                      HttpServletResponse response) {
        SqlFilter sqlFilter = new SqlFilter(request);
        List<Map<String, Object>> result = sponsorService.findSponsorSupEndList(sqlFilter);
        this.printListJsonString(sqlFilter.getPagingBean(), result, response);
    }

    /**
     * 加载事项办结详情页（立项人）数据
     */
    @RequestMapping(params = "goSponsorSuperviseEndInfo")
    public ModelAndView goSponsorSuperviseEndInfo(HttpServletRequest request,
                                                  HttpServletResponse response) {
        request.setAttribute("parentFrameId", request.getParameter("parentFrameId"));
        sponsorService.goSponsorSuperviseEndInfo(request);
        return new ModelAndView("background/supervise/supEnd");
    }

    /**
     * 根据主键ID 获取 主表信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "findSuperviseInfoById")
    public void findSuperviseInfoById(HttpServletRequest request,
                                      HttpServletResponse response) {
        Map<String, Object> result =
                sponsorService.getRecord("tb_supervise", new String[]{"RECORD_ID"},
                        new Object[]{request.getParameter("id")});
        this.printObjectJsonString(result, response);

    }

    /**
     * 立项人终止当前任务
     */
    @RequestMapping(params = "stopSupervise")
    public void stopSupervise(HttpServletRequest request,
                              HttpServletResponse response) {
        String superviseId = request.getParameter("superviseId");
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> supervise = sponsorService.getRecord("tb_supervise", new String[]{"RECORD_ID"}, new Object[]{superviseId});
        if ("9".equals(String.valueOf(supervise.get("STATUS")))) {
            result.put("success", false);
            result.put("msg", "当前督办任务已结束");
        } else if ("0".equals(String.valueOf(supervise.get("STATUS")))) {
            result.put("success", false);
            result.put("msg", "草稿任务不可终止");
        } else {
            sponsorService.stopSupervise(request);
            result.put("success", true);
        }
        result.put("parentFrameId", request.getParameter("parentFrameId"));
        this.printObjectJsonString(result, response);
    }

}
