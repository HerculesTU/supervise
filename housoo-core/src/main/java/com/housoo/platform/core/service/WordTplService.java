package com.housoo.platform.core.service;

import java.util.Map;

/**
 * 描述 WORD模版业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-05-31 10:59:58
 */
public interface WordTplService extends BaseService {
    /**
     * 根据模版编码和参数生成模版
     *
     * @param tplCode
     * @param params
     * @return
     */
    public String genWordByTplCodeAndParams(String tplCode, Map<String, Object> params);
}
