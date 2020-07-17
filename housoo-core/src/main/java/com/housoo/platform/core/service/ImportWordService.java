package com.housoo.platform.core.service;

/**
 * 描述 word导入业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2018-04-10 17:26:07
 */
public interface ImportWordService extends BaseService {

    /**
     * @param dbfilepath
     * @return
     */
    public String getWordHtmlContent(String dbfilepath);

}
