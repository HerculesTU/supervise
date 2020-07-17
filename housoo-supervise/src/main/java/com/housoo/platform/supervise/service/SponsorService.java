package com.housoo.platform.supervise.service;

import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述 督查督办业务 立项人 相关Service
 *
 * @author zxl
 * @version 1.0
 * @created 2020-04-22 15:30:11
 */
public interface SponsorService extends BaseService {

    /**
     * 获取立项人首页扇形统计数据(发起督办数量)
     *
     * @param request
     * @return
     */
    List<Map<String, Object>> getSponsorIndexSectorCommitData(HttpServletRequest request);

    /**
     * 获取立项人首页扇形统计数据(进行中的督办数量)
     *
     * @param request
     * @return
     */
    List<Map<String, Object>> getSponsorIndexSectorProcessData(HttpServletRequest request);

    /**
     * 获取立项人首页扇形统计数据(结束的督办数量)
     *
     * @param request
     * @return
     */
    List<Map<String, Object>> getSponsorIndexSectorEndData(HttpServletRequest request);

    /**
     * 获取立项人首页扇形统计数据(逾期的督办数量)
     *
     * @param request
     * @return
     */
    List<Map<String, Object>> getSponsorIndexSectorOutTimeData(HttpServletRequest request);

    /**
     * 获取立项人首页数值型统计数据
     *
     * @param request
     * @return
     */
    Map<String, Object> getSponsorIndexNumericData(HttpServletRequest request);


    /**
     * 获取立项人首页柱线图统计数据
     *
     * @param request
     * @return
     */
    List<Map<String, Object>> findSponsorIndexLineData(HttpServletRequest request);

    /**
     * 获取立项人首页部门督办列表数据
     *
     * @param request
     * @return
     */
    List<Map<String, Object>> findSponsorIndexTableData(HttpServletRequest request);

    /**
     * 获取立项人首页逾期统计数据
     *
     * @param request
     * @return
     */
    Map<String, Object> findSponsorIndexOutDateData(HttpServletRequest request);


    /**
     * 获取立项人创建的督办任务列表-
     * cjr
     *
     * @param sqlFilter
     * @return
     */
    List<Map<String, Object>> createdSuperviseList(SqlFilter sqlFilter);

    /**
     * 跳转到督办事项详情页（立项人）
     *
     * @param request
     */
    void goSuperviseInfo(HttpServletRequest request);


    /**
     * 立项人处理反馈
     *
     * @param request
     * @return
     */
    Map<String, Object> sponsorHandleFeedback(HttpServletRequest request);

    /**
     * 获取当前登录人建立的所有督察督办事项
     *
     * @return
     */
    List<List<Map<String, Object>>> findSuperviseListByLoginUser(HttpServletRequest request);

    /**
     * 下载 立项人上传附件
     *
     * @param request
     * @return
     */
    Map<String, Object> downloadSponsorFile(HttpServletRequest request);

    /**
     * 加载事项办结(立项人)的数据
     *
     * @param sqlFilter
     * @return
     */
    List<Map<String, Object>> findSponsorSupEndList(SqlFilter sqlFilter);

    /**
     * 加载事项办结详情页（立项人）数据
     *
     * @param request
     */
    void goSponsorSuperviseEndInfo(HttpServletRequest request);

    /**
     * 根据指定 id 获取督办类型（用于FORM表单 自动补充 督办类型）
     *
     * @param clazz
     * @return
     */
    List<Map<String, Object>> findClazzList(String clazz);

    /**
     * 立项人终止当前项目
     */
    void stopSupervise(HttpServletRequest request);

}
