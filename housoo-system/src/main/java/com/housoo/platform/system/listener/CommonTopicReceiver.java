package com.housoo.platform.system.listener;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.service.MqConsumerService;
import com.housoo.platform.core.util.PlatAppUtil;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 通用订阅/发布消息消费者
 *
 * @author housoo
 */
@Component
public class CommonTopicReceiver implements MessageListener {

    /**
     * 接收消息
     */
    @Override
    public void onMessage(Message message) {
        try {
            String content = ((TextMessage) message).getText();
            Map info = JSON.parseObject(content, Map.class);
            String messageCode = (String) info.get("messageCode");
            String messageContent = (String) info.get("messageContent");
            MqConsumerService mqConsumerService = (MqConsumerService) PlatAppUtil
                    .getBean("mqConsumerService");
            List<String> codeList = mqConsumerService.findTopicConsumer(messageCode);
            for (String eventCode : codeList) {
                String beanId = eventCode.split("[.]")[0];
                String method = eventCode.split("[.]")[1];
                Object serviceBean = PlatAppUtil.getBean(beanId);
                if (serviceBean != null) {
                    Method invokeMethod;
                    try {
                        invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                                new Class[]{String.class});
                        invokeMethod.invoke(serviceBean,
                                new Object[]{messageContent});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
