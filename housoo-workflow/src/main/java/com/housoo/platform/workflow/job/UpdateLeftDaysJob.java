package com.housoo.platform.workflow.job;

import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.workflow.service.JbpmService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 描述
 *
 * @author 高飞
 * @created 2017年6月3日 下午5:35:55
 */
public class UpdateLeftDaysJob implements Job {

    /**
     * 更新剩余天数数据
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JbpmService jbpmService = (JbpmService) PlatAppUtil.getBean("jbpmService");
        jbpmService.updateTimeLimit();
    }

}
