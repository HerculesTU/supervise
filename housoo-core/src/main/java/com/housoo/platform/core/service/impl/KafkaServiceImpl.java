package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.mq.KafkaSender;
import com.housoo.platform.core.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * KafkaService  Kafka消息发布Service实现类
 *
 * @author lei
 * @date 2019-12-24
 */
@Lazy
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service("kafkaService")
public class KafkaServiceImpl implements KafkaService {

    @Autowired
    private KafkaSender kafkaSender;

    /***
     * kafka 消息发布
     *
     * @param topicName 主题
     * @param queueKey  key
     * @param queueValue  消息内容
     */
    @Override
    public void sendQueueMessage(String topicName, Object queueKey, Object queueValue) {
        kafkaSender.send(topicName, queueKey, queueValue);
    }

}
