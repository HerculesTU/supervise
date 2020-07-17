package com.housoo.platform.system.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

/**
 * kafka自动监听订阅
 *
 * @author lei
 * @date 2019-12-24
 */
@Component("kafkaReceiver")
public class KafkaReceiver implements MessageListener<String, String> {

    @Override
    public void onMessage(ConsumerRecord<String, String> record) {

        System.out.println("=============Listener开始消费=============");
        String topic = record.topic();
        String key = record.key();
        String value = record.value();
        long offset = record.offset();
        int partition = record.partition();
        System.out.println("-------------topic:" + topic);
        System.out.println("-------------value:" + value);
        System.out.println("-------------key:" + key);
        System.out.println("-------------offset:" + offset);
        System.out.println("-------------partition:" + partition);
        System.out.println("~~~~~~~~~~~~~Listener消费结束~~~~~~~~~~~~~");
    }


}
