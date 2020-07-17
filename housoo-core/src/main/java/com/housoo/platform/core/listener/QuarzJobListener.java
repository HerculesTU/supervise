package com.housoo.platform.core.listener;

import com.housoo.platform.core.service.JobLogService;
import com.housoo.platform.core.util.PlatAppUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * 描述
 *
 * @author housoo
 * @created 2017年4月29日 下午2:38:36
 */
public class QuarzJobListener implements JobListener {
    /**
     * 日志工具
     */
    private static Log logger = LogFactory.getLog(QuarzJobListener.class);

    @Override
    public String getName() {
        return getClass().getSimpleName();

    }

    /**
     * 在被执行之前调用该方法
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {

    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {

    }

    /**
     * 在被执行之后调用该方法
     */
    @Override
    public void jobWasExecuted(JobExecutionContext context,
                               JobExecutionException jobException) {
        String jobName = context.getJobDetail().getName();
        JobLogService jobLogService = (JobLogService) PlatAppUtil.getBean("jobLogService");
        jobLogService.saveJobLog(jobName);
        logger.info(jobName + " 已经执行");
    }

}
