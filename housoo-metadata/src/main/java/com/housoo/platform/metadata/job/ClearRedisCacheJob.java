package com.housoo.platform.metadata.job;

import com.housoo.platform.metadata.service.DataSerService;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.service.RedisService;
import com.housoo.platform.metadata.service.DataSerService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;
import java.util.Map;

/**
 * 清理Redis缓存的定时任务
 *
 * @author housoo
 */
public class ClearRedisCacheJob implements Job {

    /**
     * 执行
     *
     * @param context
     * @throws JobExecutionException
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        DataSerService dataService = (DataSerService) PlatAppUtil.getBean("dataSerService");
        RedisService redisService = (RedisService) PlatAppUtil.getBean("redisService");
        List<Map<String, Object>> list = dataService.findAutoClearList();
        for (Map<String, Object> data : list) {
            String DATASER_CODE = (String) data.get("DATASER_CODE");
            redisService.del(DataSerService.CACHEPRE_CODE + DATASER_CODE);
        }
    }

}
