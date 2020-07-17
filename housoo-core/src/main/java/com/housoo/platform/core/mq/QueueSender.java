package com.housoo.platform.core.mq;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.HashMap;

/**
 * 队列消息生产者，发送消息到队列
 *
 * @author housoo
 */
@Lazy
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("queueSender")
public class QueueSender {
    /**
     * 队列消息模板
     */
    @Resource
    private JmsTemplate jmsTemplate;

    /**
     * 发送简单的消息
     *
     * @param queueName
     * @param message
     */
    public void send(String queueName, final String message) {
        jmsTemplate.send(queueName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });
    }

    /**
     * 发送复杂的消息
     *
     * @param topicName
     * @param message
     */
    public void sendComplexMessage(String topicName, final HashMap<Object, Object> message){
        jmsTemplate.send(topicName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(message);
            }
        });
    }
}
