package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.mq.QueueSender;
import com.housoo.platform.core.mq.TopicSender;
import com.housoo.platform.core.service.AppMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * MqService实现类
 *
 * @author 高飞
 */
@Lazy
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service("appMqService")
public class AppMqServiceImpl implements AppMqService {
    /**
     * 队列消息发送器
     */
    @Autowired
    private QueueSender queueSender;
    /**
     * 订阅消息发送器
     */
    @Autowired
    private TopicSender topicSender;

    /**
     * 发送简单的队列消息
     *
     * @param messageCode
     * @param messageContent
     */
    @Override
    public void sendSimpleQueueMessage(String messageCode, String messageContent) {
        Map<String, String> content = new HashMap<>();
        content.put("messageCode", messageCode);
        content.put("messageContent", messageContent);
        String json = JSON.toJSONString(content);
        queueSender.send("platQueue", json);
    }

    /**
     * 发送简单的订阅消息
     *
     * @param messageCode
     * @param messageContent
     */
    @Override
    public void sendSimpleTopicMessage(String messageCode, String messageContent) {
        Map<String, String> content = new HashMap<>();
        content.put("messageCode", messageCode);
        content.put("messageContent", messageContent);
        String json = JSON.toJSONString(content);
        topicSender.send("platTopic", json);
    }

    /**
     * 发送复杂的队列消息
     *
     * @param messageCode
     * @param messageContent
     */
    @Override
    public void sendComplexQueueMessage(String messageCode, Map<Object, Object> messageContent) {
        Map<String, Object> content = new HashMap<>();
        content.put("messageCode", messageCode);
        content.put("messageContent", messageContent);
        //String json = JSON.toJSONString(content);
        queueSender.sendComplexMessage("complexPlatQueue", (HashMap)content);
    }

    /**
     * 发送复杂的订阅消息
     *
     * @param messageCode
     * @param messageContent
     */
    @Override
    public void sendComplexTopicMessage(String messageCode, Map<Object, Object> messageContent) {
        Map<String, Object> content = new HashMap<>();
        content.put("messageCode", messageCode);
        content.put("messageContent", messageContent);
        //String json = JSON.toJSONString(content);
        topicSender.sendComplexMessage("complexPlatTopic", (HashMap)content);
    }

}
