package com.housoo.platform.workflow.job;

import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.workflow.service.RemindsService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author 高飞
 * 发送流程催办信息到超期的任务人手上
 */
public class SendRemindToTimeOutJob implements Job {

    /**
     * 执行
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        RemindsService remindsService = (RemindsService) PlatAppUtil.getBean("remindsService");
        remindsService.sendAutoReminds();
    }

}
