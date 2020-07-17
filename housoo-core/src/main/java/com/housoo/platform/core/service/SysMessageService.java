package com.housoo.platform.core.service;

import java.util.List;
import java.util.Map;

/**
 * 描述 系统消息业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-17 21:24:14
 */
public interface SysMessageService extends BaseService {

    /**
     * @param message
     */
    public void saveSysMessage(Map<String, Object> message);

    /**
     * 判断今天是否需要再存储磁盘预警信息
     *
     * @return
     */
    public boolean isSaveCurDate();

    /**
     * @return
     */
    public List<Map<String, Object>> findNoReadList();

    /**
     * @param sYSMESSAGE_ID
     */
    public void updateSeeTime(String sYSMESSAGE_ID);

}
