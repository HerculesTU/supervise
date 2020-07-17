package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.dao.JobLogDao;
import com.housoo.platform.core.service.JobLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述 定时器日志业务相关service实现类
 *
 * @author gf
 * @version 1.0
 * @created 2017-04-29 16:05:22
 */
@Service("jobLogService")
public class JobLogServiceImpl extends BaseServiceImpl implements JobLogService {

    /**
     * 所引入的dao
     */
    @Resource
    private JobLogDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据定时器编码保存日志
     *
     * @param scheduleCode
     */
    @Override
    public void saveJobLog(String scheduleCode) {
        Map<String, Object> schedule = dao.getRecord("PLAT_SYSTEM_SCHEDULE",
                new String[]{"SCHEDULE_CODE"}, new Object[]{scheduleCode});
        if (schedule != null) {
            String SCHEDULE_ID = (String) schedule.get("SCHEDULE_ID");
            Map<String, Object> log = new HashMap<String, Object>();
            log.put("JOBLOG_SCHEDULEID", SCHEDULE_ID);
            log.put("JOBLOG_EXETIME", PlatDateTimeUtil.formatDate(new Date(),
                    "yyyy-MM-dd HH:mm:ss"));
            dao.saveOrUpdate("PLAT_SYSTEM_JOBLOG", log, SysConstants.ID_GENERATOR_UUID, null);
        }
    }

}
