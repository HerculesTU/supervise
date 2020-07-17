package com.housoo.platform.metadata.service;

import com.housoo.platform.core.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 描述 服务请求日志业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-10 17:03:49
 */
public interface DatalogService extends BaseService {
    /**
     * 获取当日调用次数
     *
     * @param DATALOG_SECODE
     * @param DATALOG_GR
     * @return
     */
    public int getCount(String DATALOG_SECODE, String DATALOG_GR);

    /**
     * 保存请求日志
     *
     * @param request
     * @param requestIp
     * @param servicecode
     * @param grantcode
     * @param result
     * @param datalog
     * @param DATALOG_ERLOG
     */
    public void saveDataLog(HttpServletRequest request, String requestIp,
                            String servicecode, String grantcode, Map<String, Object> result,
                            Map<String, Object> datalog, String DATALOG_ERLOG);
}
