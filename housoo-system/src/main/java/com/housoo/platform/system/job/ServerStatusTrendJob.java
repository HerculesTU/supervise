package com.housoo.platform.system.job;

import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.service.ServerStatusService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author 高飞
 */
public class ServerStatusTrendJob implements Job {

    /**
     * 日志工具
     */
    private static Log logger = LogFactory.getLog(ServerStatusTrendJob.class);

    /**
     *
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	/*logger.info("开始收集服务器信息:"+PlatDateTimeUtil.
                formatDate(new Date(), "YYYY-MM-dd HH:mm:ss"));*/
        ServerStatusService serverStatusService = (ServerStatusService) PlatAppUtil.getBean("serverStatusService");
        serverStatusService.saveTrend();
    	/*logger.info("结束收集服务器信息:"+PlatDateTimeUtil.
                formatDate(new Date(), "YYYY-MM-dd HH:mm:ss"));*/
    }

}
