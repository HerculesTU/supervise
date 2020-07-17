package com.housoo.platform.system.listener;

import com.housoo.platform.core.service.MqConsumerService;
import com.housoo.platform.core.util.PlatAppUtil;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 复杂订阅/发布消息消费者
 *
 * @author 高飞
 */
@Component
public class ComplexTopicReceiver implements MessageListener {

    /**
     * 接收消息
     */
    @Override
    public void onMessage(Message message) {
        try {
            Map content = (HashMap) ((ObjectMessage) message).getObject();
            String messageCode = (String) content.get("messageCode");
            Map complexTopicMessage = (HashMap) content.get("messageContent");
            MqConsumerService mqConsumerService = (MqConsumerService) PlatAppUtil.getBean("mqConsumerService");
            List<String> codeList = mqConsumerService.findTopicConsumer(messageCode);
            for (String eventCode : codeList) {
                String beanId = eventCode.split("[.]")[0];
                String method = eventCode.split("[.]")[1];
                Object serviceBean = PlatAppUtil.getBean(beanId);
                if (serviceBean != null) {
                    Method invokeMethod;
                    try {
                        invokeMethod = serviceBean.getClass().getDeclaredMethod(method, new Class[]{Map.class});
                        invokeMethod.invoke(serviceBean, new Object[]{complexTopicMessage});
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
