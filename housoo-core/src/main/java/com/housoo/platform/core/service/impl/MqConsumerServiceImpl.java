package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.MqConsumerDao;
import com.housoo.platform.core.service.MqConsumerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述 消息消费者业务相关service实现类
 *
 * @author gf
 * @version 1.0
 * @created 2017-11-24 14:01:26
 */
@Service("mqConsumerService")
public class MqConsumerServiceImpl extends BaseServiceImpl implements MqConsumerService {

    /**
     * 所引入的dao
     */
    @Resource
    private MqConsumerDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取消息编码的订阅者
     *
     * @param code
     * @return
     */
    @Override
    public List<String> findTopicConsumer(String code) {
        return dao.findTopicConsumer(code);
    }

    /**
     * 获取队列的消费者
     *
     * @param code
     * @return
     */
    @Override
    public List<String> findQueueConsumer(String code) {
        return dao.findQueueConsumer(code);
    }

}
