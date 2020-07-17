package com.housoo.platform.workflow.service;

import com.housoo.platform.core.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 描述 流程绑定文书业务相关service
 *
 * @author housoo
 * @version 1.0
 * @created 2017-06-02 15:52:39
 */
public interface WordBindService extends BaseService {
    /**
     * 获取可绑定模版
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> findForSelect(String param);

    /**
     * 根据流程定义获取列表数据
     *
     * @param defId
     * @return
     */
    public List<Map<String, Object>> findByDefId(String defId);

    /**
     * 获取文件集合
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> findFilesList(Map<String, Object> map);

    /**
     * 调用客户端之后保存Word
     *
     * @param filePath
     */
    public void saveWordAfterCallClient(String filePath);
}
