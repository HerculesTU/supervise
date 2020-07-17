package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.listener.QuarzJobListener;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatStringUtil;
import com.housoo.platform.core.dao.ScheduleDao;
import com.housoo.platform.core.service.ScheduleService;
import com.housoo.platform.core.util.SysConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述 定时任务业务相关service实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-27 16:37:05
 */
@Service("scheduleService")
public class ScheduleServiceImpl extends BaseServiceImpl implements ScheduleService {

    /**
     * 日志工具
     */
    private static Log logger = LogFactory.getLog(ScheduleServiceImpl.class);

    /**
     * 所引入的dao
     */
    @Resource
    private ScheduleDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据编码判断是否存在记录
     *
     * @param scheduleCode
     * @return
     */
    @Override
    public boolean isExistsSchedule(String scheduleCode) {
        int count = dao.getCountByScheduleCode(scheduleCode);
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 级联删除定时调度
     *
     * @param scheduleIds
     */
    @Override
    public void deleteCascadeJob(String scheduleIds) {
        String[] scheduleIdArray = scheduleIds.split(",");
        Scheduler scheduler = PlatAppUtil.getScheduler();
        for (String scheduleId : scheduleIdArray) {
            Map<String, Object> scheduleInfo = dao.getRecord("PLAT_SYSTEM_SCHEDULE",
                    new String[]{"SCHEDULE_ID"}, new String[]{scheduleId});
            String SCHEDULE_CODE = (String) scheduleInfo.get("SCHEDULE_CODE");
            try {
                scheduler.deleteJob(SCHEDULE_CODE, Scheduler.DEFAULT_GROUP);
            } catch (SchedulerException e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
        dao.deleteRecords("PLAT_SYSTEM_SCHEDULE", "SCHEDULE_ID", scheduleIds.split(","));
    }

    /**
     * 更新定时器的状态
     *
     * @param scheduleIds
     * @param status
     */
    @Override
    public void updateStatus(String scheduleIds, int status) {
        String[] scheduleIdArray = scheduleIds.split(",");
        Scheduler scheduler = PlatAppUtil.getScheduler();
        for (String scheduleId : scheduleIdArray) {
            Map<String, Object> scheduleInfo = dao.getRecord("PLAT_SYSTEM_SCHEDULE",
                    new String[]{"SCHEDULE_ID"}, new String[]{scheduleId});
            int SCHEDULE_STATUS = Integer.parseInt(scheduleInfo.
                    get("SCHEDULE_STATUS").toString());
            String SCHEDULE_CODE = (String) scheduleInfo.get("SCHEDULE_CODE");
            if (SCHEDULE_STATUS != status) {
                if (status == 1) {
                    this.addJob(scheduleInfo, scheduler);
                } else {
                    try {
                        scheduler.deleteJob(SCHEDULE_CODE, Scheduler.DEFAULT_GROUP);
                    } catch (SchedulerException e) {
                        PlatLogUtil.printStackTrace(e);
                    }
                }
            }
        }
        StringBuffer sql = new StringBuffer("UPDATE PLAT_SYSTEM_SCHEDULE");
        sql.append(" SET SCHEDULE_STATUS=? WHERE SCHEDULE_ID IN");
        sql.append(" ").append(PlatStringUtil.getSqlInCondition(scheduleIds));
        dao.executeSql(sql.toString(), new Object[]{status});
    }

    /**
     * 根据状态获取列表数据
     *
     * @param status
     * @return
     */
    @Override
    public List<Map<String, Object>> findByByStatus(int status) {
        StringBuffer sql = new StringBuffer("SELECT * FROM PLAT_SYSTEM_SCHEDULE");
        sql.append(" S WHERE S.SCHEDULE_STATUS=? ORDER BY S.SCHEDULE_CREATETIME ASC");
        return dao.findBySql(sql.toString(), new Object[]{status}, null);
    }

    /**
     * @param scheduleInfo
     * @return
     */
    @Override
    public Map<String, Object> saveOrUpdateCascadeJob(Map<String, Object> scheduleInfo) {
        Scheduler scheduler = PlatAppUtil.getScheduler();
        String SCHEDULE_ID = (String) scheduleInfo.get("SCHEDULE_ID");
        if (StringUtils.isNotEmpty(SCHEDULE_ID)) {
            Map<String, Object> oldSchedule = dao.getRecord("PLAT_SYSTEM_SCHEDULE",
                    new String[]{"SCHEDULE_ID"}, new Object[]{SCHEDULE_ID});
            String SCHEDULE_CODE = (String) oldSchedule.get("SCHEDULE_CODE");
            try {
                scheduler.deleteJob(SCHEDULE_CODE, Scheduler.DEFAULT_GROUP);
            } catch (SchedulerException e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
        scheduleInfo = dao.saveOrUpdate("PLAT_SYSTEM_SCHEDULE",
                scheduleInfo, SysConstants.ID_GENERATOR_UUID, null);
        SCHEDULE_ID = (String) scheduleInfo.get("SCHEDULE_ID");
        Map<String, Object> info = dao.getRecord("PLAT_SYSTEM_SCHEDULE",
                new String[]{"SCHEDULE_ID"}, new Object[]{SCHEDULE_ID});
        String status = info.get("SCHEDULE_STATUS").toString();
        int SCHEDULE_STATUS = Integer.parseInt(status);
        if (SCHEDULE_STATUS == 1) {
            this.addJob(scheduleInfo, scheduler);
        }
        return scheduleInfo;
    }

    /**
     * 创建调度任务
     *
     * @param schedule
     */
    public void addJob(Map<String, Object> schedule, Scheduler scheduler) {
        String localHostIp = PlatAppUtil.getLocalHostIp();
        String SCHEDULE_CODE = (String) schedule.get("SCHEDULE_CODE");
        String SCHEDULE_CLASSNAME = (String) schedule.get("SCHEDULE_CLASSNAME");
        String SCHEDULE_CRON = (String) schedule.get("SCHEDULE_CRON");
        String SCHEDULE_BINDIP = (String) schedule.get("SCHEDULE_BINDIP");
        boolean isAdd = false;
        if (StringUtils.isNotEmpty(SCHEDULE_BINDIP) && SCHEDULE_BINDIP.equals(localHostIp)) {
            isAdd = true;
        } else if (StringUtils.isEmpty(SCHEDULE_BINDIP)) {
            isAdd = true;
        }
        if (isAdd) {
            try {
                JobDetail jobDetail = new JobDetail(SCHEDULE_CODE,
                        Scheduler.DEFAULT_GROUP, Class.forName(SCHEDULE_CLASSNAME));
                // 创建一个每5秒执行的触发器
                CronTrigger trigger = new CronTrigger(SCHEDULE_CODE + "trigger",
                        Scheduler.DEFAULT_GROUP, SCHEDULE_CRON);
                // 设置触发器马上执行
                trigger.setStartTime(new Date());
                // 将这个定时器加入到定时调度池当中
                scheduler.scheduleJob(jobDetail, trigger);
                if (!scheduler.isStarted()) {
                    scheduler.start();
                }
            } catch (ParseException e) {
                PlatLogUtil.printStackTrace(e);
            } catch (SchedulerException e) {
                PlatLogUtil.printStackTrace(e);
            } catch (ClassNotFoundException e) {
                PlatLogUtil.printStackTrace(e);
            }
        }

    }

    /**
     * 启动定时调度池
     */
    @Override
    public void startAllSchedule() {
        List<Map<String, Object>> list = this.findByByStatus(1);
        if (list.size() > 0) {
            // 获取一个定时调度池
            Scheduler scheduler = PlatAppUtil.getScheduler();
            for (Map<String, Object> she : list) {
                this.addJob(she, scheduler);
            }
            // 启动这个定时调度池
            try {
                JobListener jobListener = new QuarzJobListener();
                scheduler.addGlobalJobListener(jobListener);
                scheduler.start();
            } catch (SchedulerException e) {
                PlatLogUtil.printStackTrace(e);
            }

        }
    }

}
