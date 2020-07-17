package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.ServerStatusDao;
import com.housoo.platform.core.service.ServerStatusService;
import com.housoo.platform.core.service.DictionaryService;
import com.housoo.platform.core.service.SysMessageService;
import com.housoo.platform.core.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 服务器硬件监控信息业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-03-20 16:00:36
 */
@Service("serverStatusService")
public class ServerStatusServiceImpl extends BaseServiceImpl implements ServerStatusService {
    /**
     *
     */
    @Resource
    private SysMessageService sysMessageService;
    /**
     *
     */
    @Resource
    private DictionaryService dictionaryService;

    /**
     * 所引入的dao
     */
    @Resource
    private ServerStatusDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     *
     */
    @Override
    public Map postGetIntervalInfo(String nodes) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> deployNode = this.dao.
                getRecord("PLAT_SYSTEM_DEPLOYNODE",
                        new String[]{"DEPLOYNODE_ID"},
                        new Object[]{nodes});
        String DEPLOYNODE_URL = (String) deployNode.get("DEPLOYNODE_URL");
        String getInfoUrl = DEPLOYNODE_URL + "/appmodel/ServerStatusController/getIntervalInfo.do";
        Map<String, Object> postParams = new HashMap<String, Object>();
        try {
            String result = PlatHttpUtil.sendPostParams(getInfoUrl, postParams);
            if (result != null) {
                resultMap = JSON.parseObject(result, Map.class);
            }
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
        return resultMap;
    }

    /**
     *
     */
    @Override
    public Map<String, Object> getServerStatusInfo() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> osInfo = PlatSystemInfoUtil.getOSInfo();
        List<Map<String, Object>> cpuInfoList = PlatSystemInfoUtil.getCpuTotal();
        Map<String, Object> physicalMemory = PlatSystemInfoUtil.getPhysicalMemory();
        Map<String, Object> jvmInfo = PlatSystemInfoUtil.getJVMInfo();
        List<Map<String, Object>> fileInfoList = PlatSystemInfoUtil.getFileInfo();
        DecimalFormat df = new DecimalFormat("#0.00");
        if (fileInfoList != null) {
            float allTotal = 0;
            float allUsed = 0;
            for (int i = 0; i < fileInfoList.size(); i++) {
                float usageTotal = Float.parseFloat(fileInfoList.get(i).get("usageTotal").toString());
                float usageUsed = Float.parseFloat(fileInfoList.get(i).get("usageUsed").toString());
                allTotal += usageTotal;
                allUsed += usageUsed;
            }
            if (allTotal != 0) {
                String usePercent = df.format((allUsed * 100) / allTotal);
                resultMap.put("usePercent", usePercent);
            }

        }
        resultMap.put("osInfo", osInfo);
        resultMap.put("cpuInfo", cpuInfoList.get(0));
        resultMap.put("cpuSize", PlatSystemInfoUtil.getCpuCount());
        resultMap.put("physicalMemory", physicalMemory);
        resultMap.put("jvmInfo", jvmInfo);
        resultMap.put("fileInfoList", fileInfoList);
        return resultMap;
    }

    /**
     *
     */
    @Override
    public Map<String, Object> postGetServerStatusInfo(String nodes) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> deployNode = this.dao.
                getRecord("PLAT_SYSTEM_DEPLOYNODE",
                        new String[]{"DEPLOYNODE_ID"},
                        new Object[]{nodes});
        String DEPLOYNODE_URL = (String) deployNode.get("DEPLOYNODE_URL");
        String getInfoUrl = DEPLOYNODE_URL + "/appmodel/ServerStatusController/getServerStatusInfo.do";
        Map<String, Object> postParams = new HashMap<String, Object>();
        try {
            String result = PlatHttpUtil.sendPostParams(getInfoUrl, postParams);
            if (result != null) {
                resultMap = JSON.parseObject(result, Map.class);
            }
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
        return resultMap;
    }

    /**
     * 保存系统情况
     */
    @Override
    public void saveTrend() {
        Map<String, Object> trend = new HashMap<String, Object>();
        trend.put("TREND_CREATETIME", PlatDateTimeUtil.formatDate(new Date(),
                "yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> result = this.getServerStatusInfo();
        String diskUsePercent = (String) result.get("usePercent");
        trend.put("TREND_DISKUSEPERCENT", diskUsePercent);
        Map<String, Object> cpuPerc = PlatSystemInfoUtil.getEasyCpuPerc();
        String cpuPercCombined = cpuPerc.get("cpuPercCombined").toString().replace("%", "");
        trend.put("TREND_CPUPERCCOMBINED", cpuPercCombined);
        Map<String, Object> jvmInfo = PlatSystemInfoUtil.getJVMInfo();
        String jvmUsedMemoryPre = (String) jvmInfo.get("usedMemoryPre");
        trend.put("TREND_JVMUSEDMEMORYPRE", jvmUsedMemoryPre);
        Map<String, Object> menoryMap = PlatSystemInfoUtil.getPhysicalMemory();
        String usedMemoryPre = (String) menoryMap.get("UsedMemoryPre");
        trend.put("TREND_USEDMEMORYPRE", usedMemoryPre);
        dao.saveOrUpdate("PLAT_APPMODEL_SERVERTREND",
                trend, SysConstants.ID_GENERATOR_UUID, null);

        String dicVal = dictionaryService.getDictionaryValue("warnLineNumber", "阈值");
        double dicIntV = 50;
        if (StringUtils.isNotEmpty(dicVal)) {
            dicIntV = Double.parseDouble(dicVal);
        }
        double diskUsePercentNum = Double.parseDouble(diskUsePercent);
        if (dicIntV < diskUsePercentNum) {
            boolean b = sysMessageService.isSaveCurDate();
            if (!b) {
                Map<String, Object> message = new HashMap<String, Object>();
                message.put("SYSMESSAGE_TITLE", "系统磁盘空间不足！");
                message.put("SYSMESSAGE_CONTENT", "系统磁盘空间不足！磁盘空间使用率已达到"
                        + diskUsePercentNum + "%,已经超过"
                        + dicIntV + "%,请及时处理！");
                message.put("SYSMESSAGE_TYPE", "3");
                message.put("USER_IDS", "297ecf1263571a070163572c6a80002f,"
                        + "297ecf1263571a070163572d7b860051,402848a55b6547ec015b6547ec760000,"
                        + "297ecf1263571a070163572e4c8c0075");
                sysMessageService.saveSysMessage(message);
            }
        }

    }

}
