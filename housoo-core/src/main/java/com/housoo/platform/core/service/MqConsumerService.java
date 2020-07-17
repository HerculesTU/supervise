package com.housoo.platform.core.service;

import java.util.List;

/**
 * 描述 消息消费者业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-11-24 14:01:26
 */
public interface MqConsumerService extends BaseService {

    /**
     * 获取消息编码的订阅者
     *
     * @param code
     * @return
     */
    public List<String> findTopicConsumer(String code);

    /**
     * 获取队列的消费者
     *
     * @param code
     * @return
     */
    public List<String> findQueueConsumer(String code);
}
