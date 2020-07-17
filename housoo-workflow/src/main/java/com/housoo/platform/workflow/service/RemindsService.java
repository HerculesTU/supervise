/*
 * Copyright (c) 2005, 2014, housoo Technology Co.,Ltd. All rights reserved.
 * housoo PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.housoo.platform.workflow.service;

import com.housoo.platform.core.service.BaseService;

/**
 * 描述 催办信息业务相关service
 *
 * @author 胡裕
 * @version 1.0
 * @created 2017-11-16 08:50:10
 */
public interface RemindsService extends BaseService {

    /**
     * 发送催办意见
     *
     * @param REMINDS_EXEIDS
     * @param REMINDS_CONTENT
     * @return
     */
    public boolean sendReminds(String REMINDS_EXEIDS, String REMINDS_CONTENT);

    /**
     * 自动发送催办
     */
    public void sendAutoReminds();
}
