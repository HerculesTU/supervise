package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.mq.RabbitQueueSender;
import com.housoo.platform.core.service.RabbitMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMqService 实现类
 * Created by cjr on 2019-12-24.
 */
@Lazy
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service("rabbitMqService")
public class RabbitMqServiceImpl implements RabbitMqService {

    /**
     * rabbitmq 队列消息发送器
     */
    @Autowired
    private RabbitQueueSender rabbitQueueSender;

    /**
     * 发送一对一队列消息
     *
     * @param messageCode
     * @param messageContent
     */
    @Override
    public void sendSimpleQueueMessage(String messageCode, String messageContent) {
        Map<String, String> content = new HashMap<String, String>();
        content.put("messageCode", messageCode);
        content.put("messageContent", messageContent);
        String json = JSON.toJSONString(content);
        rabbitQueueSender.sendSimpleMessage(json);
    }

    /**
     * 发送一对多队列消息
     *
     * @param messageCode
     * @param messageContent
     */
    @Override
    public void sendWorkQueueMessage(String messageCode, String messageContent) {
        Map<String, String> content = new HashMap<String, String>();
        content.put("messageCode", messageCode);
        content.put("messageContent", messageContent);
        String json = JSON.toJSONString(content);
        rabbitQueueSender.sendWorkMessage(json);
    }

    /**
     * 路由模式发送队列消息
     *
     * @param messageCode
     * @param messageContent
     */
    @Override
    public void sendDirectQueueMessage(String messageCode, String messageContent) {
        Map<String, String> content = new HashMap<String, String>();
        content.put("messageCode", messageCode);
        content.put("messageContent", messageContent);
        String json = JSON.toJSONString(content);
        rabbitQueueSender.sendDirectMessage(json);
    }

    /**
     * 主题模式发送队列消息
     *
     * @param messageCode
     * @param messageContent
     */
    @Override
    public void sendTopicQueueMessage(String messageCode, String messageContent) {
        Map<String, String> content = new HashMap<String, String>();
        content.put("messageCode", messageCode);
        content.put("messageContent", messageContent);
        String json = JSON.toJSONString(content);
        rabbitQueueSender.sendTopicMessage(json);
    }

    /**
     * 发布订阅模式发送队列消息
     *
     * @param messageCode
     * @param messageContent
     */
    @Override
    public void sendPubSubQueueMessage(String messageCode, String messageContent) {
        Map<String, String> content = new HashMap<String, String>();
        content.put("messageCode", messageCode);
        content.put("messageContent", messageContent);
        String json = JSON.toJSONString(content);
        rabbitQueueSender.sendPubSubMessage(json);
    }
}
