package com.housoo.platform.core.service;

/**
 * 描述 部署日志业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-04-19 17:16:44
 */
public interface DeployLogService extends BaseService {
    /**
     * 保存部署记录信息
     *
     * @param email
     * @param name
     * @param pass
     * @param nodeIds
     * @param jarPath
     */
    public void saveDeployLog(String email, String name, String pass,
                              String nodeIds, String jarPath, String fileJson);
}
