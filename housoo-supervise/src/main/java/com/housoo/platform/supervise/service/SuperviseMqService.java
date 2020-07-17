/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.service;

import com.housoo.platform.core.service.BaseService;

import java.util.Map;


/**
 * 描述 督办消息推送业务相关service
 *
 * @author cjr
 * @version 1.0
 * @created 2020-04-14 15:30:11
 */
public interface SuperviseMqService extends BaseService {

    /**
     * 承办人提交反馈推送消息到消息队列
     * cjr
     *
     * @return
     */
    Map<Object, Object> pushTakerHandleInfo(Map<String, Object> params);

    /**
     * 立项人 创建 督办任务推送消息到消息队列
     * zxl
     *
     * @return
     */
    Map<Object, Object> pushSponsorCreateInfo(Map<String, Object> supervise);

    /**
     * 立项人 批复 督办任务推送消息到消息队列
     * zxl
     *
     * @return
     */
    Map<Object, Object> pushSponsorHandleInfo(Map<String, Object> params);
}
