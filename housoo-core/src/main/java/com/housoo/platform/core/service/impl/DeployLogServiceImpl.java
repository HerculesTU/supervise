package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.util.*;
import com.housoo.platform.core.service.FileAttachService;
import com.housoo.platform.core.dao.DeployLogDao;
import com.housoo.platform.core.service.DeployLogService;
import com.housoo.platform.core.service.DeployNodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述 部署日志业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-04-19 17:16:44
 */
@Service("deployLogService")
public class DeployLogServiceImpl extends BaseServiceImpl implements DeployLogService {

    /**
     * 所引入的dao
     */
    @Resource
    private DeployLogDao dao;
    /**
     *
     */
    @Resource
    private DeployNodeService deployNodeService;
    /**
     *
     */
    @Resource
    private FileAttachService fileAttachService;


    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 保存部署记录信息
     *
     * @param email
     * @param name
     * @param pass
     * @param nodeIds
     * @param jarPath
     */
    @Override
    public void saveDeployLog(String email, String name, String pass,
                              String nodeIds, String jarPath, String fileJson) {
        for (String nodeId : nodeIds.split(",")) {
            Map<String, Object> deployNode = deployNodeService.
                    getRecord("PLAT_SYSTEM_DEPLOYNODE",
                            new String[]{"DEPLOYNODE_ID"},
                            new Object[]{nodeId});
            String DEPLOYNODE_URL = (String) deployNode.get("DEPLOYNODE_URL");
            String deployUrl = DEPLOYNODE_URL + "/system/DeployLogController/uploadDeploy.do";
            Map<String, Object> postParams = new HashMap<String, Object>();
            postParams.put("guid", UUIDGenerator.getUUID());
            postParams.put("uploadRootFolder", "deployLog");
            postParams.put("DEVMAN_EMAIL", email);
            postParams.put("DEVMAN_PASS", pass);
            String rootPath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
            PlatHttpUtil.uploadFile(deployUrl, rootPath + jarPath, postParams);
        }
        //保存上线记录
        Map<String, Object> deployLog = new HashMap<String, Object>();
        deployLog.put("DEPLOYLOG_EMAIL", email);
        deployLog.put("DEPLOYLOG_NAME", name);
        deployLog.put("DEPLOYLOG_TIME", PlatDateTimeUtil.
                formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        deployLog.put("DEPLOYLOG_NODES", nodeIds);
        deployLog = this.saveOrUpdate("PLAT_SYSTEM_DEPLOYLOG", deployLog,
                SysConstants.ID_GENERATOR_UUID, null);
        String busRecordId = (String) deployLog.get("DEPLOYLOG_ID");
        fileAttachService.saveFileAttachs(fileJson, "PLAT_SYSTEM_DEPLOYLOG", busRecordId, null);
    }

}
