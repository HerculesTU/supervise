package com.housoo.platform.system.listener;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.service.MqConsumerService;
import com.housoo.platform.core.util.PlatAppUtil;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 复杂队列消息消费者
 *
 * @author 高飞
 */
@Component
public class ComplexQueueReceiver implements MessageListener {

    /**
     * 接收消息
     */
    @Override
    public void onMessage(Message message) {
        try {
            Map content = (HashMap) ((ObjectMessage) message).getObject();
            String messageCode = (String) content.get("messageCode");
            Map complexQueueMessage = (HashMap) content.get("messageContent");
            MqConsumerService mqConsumerService = (MqConsumerService) PlatAppUtil.getBean("mqConsumerService");
            /*Map<String, Object> consumer = mqConsumerService.getRecord("PLAT_APPMODEL_MQCONSUMER",
                    new String[]{"MQCONSUMER_TYPE", "MQCONSUMER_CODE"}, new Object[]{1, messageCode});
            if (consumer != null) {
                String eventCode = (String) consumer.get("MQCONSUMER_JAVA");
                String beanId = eventCode.split("[.]")[0];
                String method = eventCode.split("[.]")[1];
                Object serviceBean = PlatAppUtil.getBean(beanId);
                if (serviceBean != null) {
                    Method invokeMethod;
                    try {
                        invokeMethod = serviceBean.getClass().getDeclaredMethod(method, new Class[]{Map.class});
                        invokeMethod.invoke(serviceBean, new Object[]{complexQueueMessage});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }*/
            List<String> codeList = mqConsumerService.findQueueConsumer(messageCode);
            for (String eventCode : codeList) {
                String beanId = eventCode.split("[.]")[0];
                String method = eventCode.split("[.]")[1];
                Object serviceBean = PlatAppUtil.getBean(beanId);
                if (serviceBean != null) {
                    Method invokeMethod;
                    try {
                        invokeMethod = serviceBean.getClass().getDeclaredMethod(method, new Class[]{String.class});
                        invokeMethod.invoke(serviceBean, new Object[]{JSON.toJSONString(complexQueueMessage)});
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
