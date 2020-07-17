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
 * 发布/订阅消息生产者，发送消息到队列
 *
 * @author housoo
 */
@Lazy
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("topicSender")
public class TopicSender {

    /**
     * 发布/订阅消息模板
     */
    @Resource
    private JmsTemplate jmsTopicTemplate;

    /**
     * 发送简单的消息
     *
     * @param topicName
     * @param message
     */
    public void send(String topicName, final String message) {
        jmsTopicTemplate.send(topicName, new MessageCreator() {
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
        jmsTopicTemplate.send(topicName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(message);
            }
        });
    }
}
