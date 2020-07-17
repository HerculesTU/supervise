package com.housoo.platform.system.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.housoo.platform.core.service.AppMqService;
import com.housoo.platform.core.util.PlatAppUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.housoo.platform.core.util.PlatDateTimeUtil;

/**
 * 描述 测试定时任务
 *
 * @author 高飞
 * @created 2017年4月29日 下午2:25:53
 */
public class HelloJob implements Job {

    /**
     * 日志工具
     */
    private static Log logger = LogFactory.getLog(HelloJob.class);

    /**
     * 执行
     *
     * @param context
     * @throws JobExecutionException
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        AppMqService appMqService1 = (AppMqService) PlatAppUtil.getBean("appMqService");

        String messageCode1 = "001";
        String messageContent1 = "这是一条简单队列消息";
        appMqService1.sendSimpleQueueMessage(messageCode1, messageContent1);
        logger.info("消费了简单队列消息:当前时间是:" + PlatDateTimeUtil.
                formatDate(new Date(), "YYYY-MM-dd HH:mm:ss"));

        String messageCode2 = "002";
        Map<String,Object> messageMap = new HashMap<>();
        messageMap.put("complexQueueMessage", "这是一条复杂队列消息");
        appMqService1.sendComplexQueueMessage(messageCode2, (HashMap)messageMap);
        logger.info("消费了简单队列消息:当前时间是:" + PlatDateTimeUtil.
                formatDate(new Date(), "YYYY-MM-dd HH:mm:ss"));


        String messageCode3 = "003";
        String messageContent3 = "这是一条简单订阅消息";
        appMqService1.sendSimpleTopicMessage(messageCode3, messageContent3);
        logger.info("消费了简单订阅消息:当前时间是:" + PlatDateTimeUtil.
                formatDate(new Date(), "YYYY-MM-dd HH:mm:ss"));

        String messageCode4 = "004";
        Map<String,Object> messageMap2 = new HashMap<>();
        messageMap2.put("complexTopicMessage", "这是一条复杂订阅消息");
        appMqService1.sendComplexTopicMessage(messageCode4, (HashMap)messageMap2);
        logger.info("消费了复杂订阅消息:当前时间是:" + PlatDateTimeUtil.
                formatDate(new Date(), "YYYY-MM-dd HH:mm:ss"));

    }

}
