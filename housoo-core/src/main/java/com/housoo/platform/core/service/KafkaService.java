package com.housoo.platform.core.service;

/**
 * KafkaService  Kafka消息发布Service相关
 *
 * @author lei
 * @date 2019-12-24
 */
public interface KafkaService {
    /**
     * kafka 消息发布
     *
     * @param topicName 主题
     * @param queueKey   key
     * @param queueValue  消息内容
     */
    void sendQueueMessage(String topicName, Object queueKey, Object queueValue);
}
