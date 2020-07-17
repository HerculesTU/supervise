package com.housoo.platform.core.service;

import com.housoo.platform.core.model.JbpmFlowInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述 附件信息业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-04-14 10:42:35
 */
public interface FileAttachService extends BaseService {
    /**
     * 批量保存附件信息记录
     *
     * @param fileListJson:附件的JSON
     * @param busTableName:涉及业务表
     * @param busRecordId:涉及业务表记录ID
     * @param typeKey:分类KEY,如果同一个业务表下没有不同的分类上传，可以不传该参数
     * @return 返回文件ID列表
     */
    public List<String> saveFileAttachs(String fileListJson,
                                        String busTableName, String busRecordId, String typeKey);

    /**
     * 删除附件记录数据
     *
     * @param busTableName
     * @param busRecordId
     * @param typeKey
     */
    public void deleteFiles(String busTableName, String busRecordId, String typeKey);

    /**
     * 获取附件列表数据
     *
     * @param busTableName:业务表名称
     * @param busRecordId:业务表记录ID
     * @param typeKey:类别KEY
     * @return
     */
    public List<Map<String, Object>> findList(String busTableName, String busRecordId, String typeKey);

    /**
     * 保存流程流转中的文书记录
     *
     * @param wordUploadJson
     * @param jbpmFlowInfo
     */
    public void saveFlowWords(String wordUploadJson, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 通用保存文书数据
     *
     * @param postParams
     * @param jbpmFlowInfo
     * @return
     */
    public Map<String, Object> saveFlowWords(Map<String, Object> postParams, JbpmFlowInfo jbpmFlowInfo);

    /**
     * 实现文件上传功能
     *
     * @param request
     * @return
     */
    public Map<String, Object> uploadFile(HttpServletRequest request, boolean isForServlet);
}
