package com.housoo.platform.supervise.service;

import com.housoo.platform.core.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 描述 督办类型业务相关service
 *
 * @author zxl
 * @version 1.0
 * @created 2020-04-13 11:28:30
 */
public interface SuperviseClazzService extends BaseService {

    /**
     * 获取所有督办类型
     */
    List<Map<String, Object>> getAllSuperviseClazz(String queryParamsJson);

}
