package com.housoo.platform.core.service;

import org.apache.poi.ss.formula.functions.T;

import java.util.HashMap;
import java.util.Map;

/**
 * MqService 封装了消息队列操作的相关方法
 *
 * @author 高飞
 */
public interface AppMqService {
    /**
     * 发送简单的队列消息
     *
     * @param messageCode
     * @param messageContent
     */
    void sendSimpleQueueMessage(String messageCode, String messageContent);

    /**
     * 发送简单的订阅消息
     *
     * @param messageCode
     * @param messageContent
     */
    void sendSimpleTopicMessage(String messageCode, String messageContent);

    /**
     * 发送复杂的队列消息
     *
     * @param messageCode
     * @param messageContent
     */
    void sendComplexQueueMessage(String messageCode, Map<Object, Object> messageContent);

    /**
     * 发送复杂的订阅消息
     *
     * @param messageCode
     * @param messageContent
     */
    void sendComplexTopicMessage(String messageCode, Map<Object, Object> messageContent);

}
