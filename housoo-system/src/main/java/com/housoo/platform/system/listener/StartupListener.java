package com.housoo.platform.system.listener;

import com.housoo.platform.core.service.GlobalUrlService;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.common.service.CommonService;
import com.housoo.platform.core.service.ScheduleService;
import com.housoo.platform.core.service.DictionaryService;
import com.housoo.platform.core.service.ResService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;
import java.util.List;
import java.util.Map;

/**
 * 描述 继承spring的contextLoad监听器，以便启动的时候初始化系统
 *
 * @author housoo
 * @created 2017年1月8日 上午10:39:47
 */
public class StartupListener extends ContextLoaderListener {

    /**
     * 日志工具
     */
    private static Log logger = LogFactory.getLog(StartupListener.class);

    /**
     * 描述 容器初始化
     *
     * @param event
     * @created 2014年9月29日 上午11:05:12
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        //初始化APP工具类和servlet上下文对象
        PlatAppUtil.init(event.getServletContext());
        //加载客户端验证规则数据
        DictionaryService dictionaryService = (DictionaryService) PlatAppUtil.getBean("dictionaryService");
        PlatAppUtil.setValidRules(dictionaryService.getJsValidRules());
        //加载所配置的URL集合
        ResService resService = (ResService) PlatAppUtil.getBean("resService");
        PlatAppUtil.setAllResUrlSet(resService.getAllResUrl());
        //加载所有匿名访问的URL集合
        GlobalUrlService globalUrlService = (GlobalUrlService) PlatAppUtil.getBean("globalUrlService");
        PlatAppUtil.setAllAnonUrlSet(globalUrlService.getAllAnonUrl());
        //启动定时调度池
        ScheduleService scheduleService = (ScheduleService) PlatAppUtil.getBean("scheduleService");
        scheduleService.startAllSchedule();
        //获取主键列表
        CommonService commonService = (CommonService) PlatAppUtil.getBean("commonService");
        Map<String, List<String>> pkNameList = commonService.getAllPkNames();
        PlatAppUtil.setPrimaryKeyMap(pkNameList);
        //获取表字段列表
        PlatAppUtil.setTableColumnMap(commonService.getAllTableColumnNames());
    }
}
