package com.housoo.platform.core.mq;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

import javax.annotation.Resource;

/**
 * kafka 发送消息
 *
 * @author lei
 * @date 2019-12-24
 */
@Lazy
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("kafkaSender")
public class KafkaSender {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * kafka 消息发布
     *
     * @param topicName 主题
     * @param key  key
     * @param value  消息内容
     */
    public void send(String topicName, Object key, Object value) {
        ListenableFuture<SendResult<String, String>> listenableFuture =  kafkaTemplate.send(new ProducerRecord(topicName, key, value));
        SuccessCallback<SendResult<String, String>> successCallback = new SuccessCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                //成功业务逻辑
                System.out.println("success to send message !");
            }
        };
        //发送失败回调
        FailureCallback failureCallback = new FailureCallback() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("fail to send message");
            }
        };
        listenableFuture.addCallback(successCallback, failureCallback);
    }

}
