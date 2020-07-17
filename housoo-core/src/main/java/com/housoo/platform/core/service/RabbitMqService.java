package com.housoo.platform.core.service;

/**
 * RabbitMqService 封装了rabbitmq消息队列操作的相关方法
 * Created by cjr on 2019-12-24.
 */
public interface RabbitMqService {
    /**
     * 发送一对一队列消息
     *
     * @param messageCode
     * @param messageContent
     */
    void sendSimpleQueueMessage(String messageCode, String messageContent);

    /**
     * 发送一对多队列消息
     *
     * @param messageCode
     * @param messageContent
     */
    void sendWorkQueueMessage(String messageCode, String messageContent);

    /**
     * 路由模式发送队列消息
     *
     * @param messageCode
     * @param messageContent
     */
    void sendDirectQueueMessage(String messageCode, String messageContent);


    /**
     * 主题模式发送队列消息
     *
     * @param messageCode
     * @param messageContent
     */
    void sendTopicQueueMessage(String messageCode, String messageContent);

    /**
     * 发布订阅模式发送队列消息
     *
     * @param messageCode
     * @param messageContent
     */
    void sendPubSubQueueMessage(String messageCode, String messageContent);
}
