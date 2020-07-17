package com.housoo.platform.workflow.service;

import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.BaseService;
import com.housoo.platform.core.model.JbpmFlowInfo;

import java.util.List;
import java.util.Map;

/**
 * 描述 按钮绑定业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-05-07 09:49:51
 */
public interface ButtonBindService extends BaseService {

    /**
     * 获取绑定的按钮列表
     *
     * @param sqlFilter
     * @param fieldInfo
     * @return
     */
    public List<Map<String, Object>> findBySqlFilter(SqlFilter sqlFilter, Map<String, Object> fieldInfo);

    /**
     * 新增或者编辑按钮绑定信息
     *
     * @param buttonBind
     * @return
     */
    public Map<String, Object> saveCascadeBind(Map<String, Object> buttonBind);

    /**
     * 获取下一个排序值
     *
     * @param BTNBIND_FLOWDEFID
     * @param BTNBIND_FLOWVERSION
     * @return
     */
    public int getNextSn(String BTNBIND_FLOWDEFID, String BTNBIND_FLOWVERSION);

    /**
     * 删除按钮绑定记录
     *
     * @param buttonBindIds
     */
    public void deleteCacadeBind(String buttonBindIds);

    /**
     * 更新排序
     *
     * @param BTNBIND_IDS
     */
    public void updateSn(String BTNBIND_IDS);

    /**
     * 获取环节所绑定的按钮列表
     *
     * @param jbpmFlowInfo
     * @return
     */
    public List<Map<String, Object>> findButtonBind(JbpmFlowInfo jbpmFlowInfo);

    /**
     * 改变按钮权限
     *
     * @param jbpmFlowInfo
     * @return
     */
    public boolean changeBindButton(JbpmFlowInfo jbpmFlowInfo);

    /**
     * 拷贝旧的按钮绑定信息
     *
     * @param oldFlowDefId
     * @param oldFlowDefVersion
     * @param newFlowDefId
     * @param newFlowDefVersion
     */
    public void copyBindButtons(String oldFlowDefId, int oldFlowDefVersion,
                                String newFlowDefId, int newFlowDefVersion);

    /**
     * 根据流程定义ID和流程版本号获取列表
     *
     * @param defId
     * @param flowVersion
     * @return
     */
    public List<Map<String, Object>> findByDefIdAndVersion(String defId, int flowVersion);

}
