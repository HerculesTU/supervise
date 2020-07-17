package com.housoo.platform.core.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * rabbitmq 队列消息发送器
 * Created by cjr on 2019-12-24.
 */
@Lazy
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("rabbitQueueSender")
public class RabbitQueueSender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    private Connection connection;

    /**
     * AMQP.BasicProperties 提供了一个构造器，可以通过builder() 来设置一些属性；
     */
    private AMQP.BasicProperties properties;

    @PostConstruct
    public void init() {
        connection = rabbitTemplate.getConnectionFactory().createConnection();
        properties = new AMQP.BasicProperties().builder()
                .deliveryMode(2) // 传送方式
                .contentEncoding("UTF-8") // 编码方式
                .expiration("10000") // 过期时间
                .build();
    }

    /**
     * 发送一对一队列消息
     *
     * @param object 消息
     */
    public void sendSimpleMessage(Object object) {
        try {
            //1.获取连接
            //Connection connection = rabbitTemplate.getConnectionFactory().createConnection();
            //2.声明通道
            Channel channel = connection.createChannel(false);
            //3.声明(创建)队列
            //channel.queueDeclare("simpleQueue",false,false,false,null);
            //4.定义消息内容
            String message = (String) object;
            //5.发布消息
            channel.basicPublish("", "simpleQueue", properties, message.getBytes());
            System.out.println("[one] sendSimpleMessage '" + message + "'");
            //6.关闭通道和连接
            channel.close();
            connection.close();
            //rabbitTemplate.convertAndSend(object);
        } catch (Exception e) {
            //TODO
        }
    }

    /**
     * 发送一对多队列消息
     *
     * @param object 消息
     */
    public void sendWorkMessage(Object object) {
        try {
            //1.获取连接
            //Connection connection = rabbitTemplate.getConnectionFactory().createConnection();
            //2.声明通道
            Channel channel = connection.createChannel(false);
            //3.声明(创建)队列
            //channel.queueDeclare("workQueue", false, false, false, null);
            //4.定义消息内容,发布多条消息
            for (int i = 0; i < 10; i++) {
                String message = (String) object;
                //5.发布消息
                channel.basicPublish("", "workQueue", properties, message.getBytes());
                System.out.println("[one] sendWorkMessage message is '" + message + "'");
                //6.模拟发送消息延时,便于展示多个消费者竞争接受消息
                Thread.sleep(i * 10);
            }
            //7.关闭信道
            channel.close();
            //8.关闭连接
            connection.close();
            //rabbitTemplate.convertAndSend(object);
        } catch (Exception e) {
            //TODO
        }
    }

    /**
     * 路由模式发送队列消息
     *
     * @param object 消息
     */
    public void sendDirectMessage(Object object) {
        try {
            //1.获取连接
            //Connection connection = rabbitTemplate.getConnectionFactory().createConnection();
            //2.声明通道
            Channel channel = connection.createChannel(false);
            //3.声明交换器,类型为direct
            //channel.exchangeDeclare("rabbitmq-direct-exchange", "direct");
            //4.定义消息内容
            String message = (String) object;
            //5.发布消息
            channel.basicPublish("rabbitmq-direct-exchange", "directQueue", properties, message.getBytes());
            System.out.println("[one] sendDirectMessage '" + message + "'");
            //6.关闭通道和连接
            channel.close();
            connection.close();
            //rabbitTemplate.convertAndSend(routingKey,object);
        } catch (Exception e) {
            //TODO
        }
    }

    /**
     * 主题模式发送队列消息
     *
     * @param object 消息
     */
    public void sendTopicMessage(Object object) {
        try {
            //1.获取连接
            //Connection connection = rabbitTemplate.getConnectionFactory().createConnection();
            //2.声明通道
            Channel channel = connection.createChannel(false);
            //3.声明交换器,类型为topic
            //channel.exchangeDeclare("rabbitmq-topic-exchange", "topic");
            //4.定义消息内容
            String message = (String) object;
            //5.发布消息
            channel.basicPublish("rabbitmq-topic-exchange", "rabbit.avc", properties, message.getBytes());
            System.out.println("[one] sendTopicMessage '" + message + "'");
            //6.关闭通道和连接
            channel.close();
            connection.close();
        } catch (Exception e) {
            //TODO
        }
    }

    /**
     * 发布/订阅模式发送队列消息
     *
     * @param object 消息
     */
    public void sendPubSubMessage(Object object) {
        try {
            //1.获取连接
            //Connection connection = rabbitTemplate.getConnectionFactory().createConnection();
            //2.声明通道
            Channel channel = connection.createChannel(false);
            //3.声明交换器
            channel.exchangeDeclare("rabbitmq-pubSub-exchange", "fanout");
            //4.定义消息内容
            String message = (String) object;
            //5.发布消息
            channel.basicPublish("rabbitmq-pubSub-exchange", "", properties, message.getBytes());
            System.out.println("[one] sendPubSubMessage '" + message + "'");
            //6.关闭通道和连接
            channel.close();
            connection.close();
        } catch (Exception e) {
            //TODO
        }
    }

}
