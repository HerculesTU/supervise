package com.housoo.platform.system.listener;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.service.MqConsumerService;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


/**
 * rabbitmq 消息监听器/消息消费者
 * 自定义的监听类必须实现接口：ChannelAwareMessageListener或者MessageListener
 * Created by cjr on 2019-12-24.
 */
@Component
public class RabbitQueueReceiver implements ChannelAwareMessageListener {

    /**
     * @param message 消息实体
     * @param channel 通道 参数Channel可以很方便的提供监听之外的ack通知RabbitMq服务器功能
     * @throws Exception
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        /*--------------消费者业务处理代码块-------------*/
        try {
            String content = new String(message.getBody(), "UTF-8");
            String queueName = message.getMessageProperties().getConsumerQueue();
            PlatLogUtil.println("队列名:" + queueName + ",容器监听【从RabbitMQ接收消息成功】消息内容为：" + content);
            Map temp = JSON.parseObject(content, Map.class);
            String messageCode = (String) temp.get("messageCode");
            String messageContent = (String) temp.get("messageContent");
            MqConsumerService mqConsumerService = (MqConsumerService) PlatAppUtil.getBean("mqConsumerService");
            List<String> codeList = mqConsumerService.findTopicConsumer(messageCode);
            for (String eventCode : codeList) {
                String beanId = eventCode.split("[.]")[0];
                String method = eventCode.split("[.]")[1];
                Object serviceBean = PlatAppUtil.getBean(beanId);
                if (serviceBean != null) {
                    Method invokeMethod;
                    try {
                        long start = System.currentTimeMillis();
                        invokeMethod = serviceBean.getClass().getDeclaredMethod(method, new Class[]{String.class});
                        invokeMethod.invoke(serviceBean, new Object[]{messageContent});
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        PlatLogUtil.println("消息消费耗时：" + (System.currentTimeMillis() - start) + "ms");
                    } catch (Exception e) {
                        e.printStackTrace();
                        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                        PlatLogUtil.println("消息推送失败");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();//TODO 业务处理
        }
    }
}
