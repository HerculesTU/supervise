package com.housoo.platform.core.dao.impl;

import com.housoo.platform.core.dao.MqConsumerDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述消息消费者业务相关dao实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-11-24 14:01:26
 */
@Repository
public class MqConsumerDaoImpl extends BaseDaoImpl implements MqConsumerDao {

    /**
     * 获取消息编码的订阅者
     *
     * @param code
     * @return
     */
    @Override
    public List<String> findTopicConsumer(String code) {
        StringBuffer sql = new StringBuffer("SELECT T.MQCONSUMER_JAVA");
        sql.append(" FROM PLAT_APPMODEL_MQCONSUMER T WHERE ");
        sql.append(" T.MQCONSUMER_TYPE = 2 AND T.MQCONSUMER_CODE=? ");
        List<String> list = this.getJdbcTemplate().queryForList(sql.toString(),
                String.class, new Object[]{code});
        return list;
    }

    /**
     * 获取队列的消费者
     *
     * @param code
     * @return
     */
    @Override
    public List<String> findQueueConsumer(String code) {
        StringBuffer sql = new StringBuffer("SELECT T.MQCONSUMER_JAVA");
        sql.append(" FROM PLAT_APPMODEL_MQCONSUMER T WHERE ");
        sql.append(" T.MQCONSUMER_TYPE = 1 AND T.MQCONSUMER_CODE=? ");
        List<String> list = this.getJdbcTemplate().queryForList(sql.toString(),
                String.class, new Object[]{code});
        return list;
    }
}
