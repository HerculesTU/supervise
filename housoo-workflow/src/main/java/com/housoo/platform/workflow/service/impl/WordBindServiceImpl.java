package com.housoo.platform.workflow.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.workflow.dao.WordBindDao;
import com.housoo.platform.workflow.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 描述 流程绑定文书业务相关service实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-06-02 15:52:39
 */
@Service("wordBindService")
public class WordBindServiceImpl extends BaseServiceImpl implements WordBindService {

    /**
     * 所引入的dao
     */
    @Resource
    private WordBindDao dao;
    /**
     *
     */
    @Resource
    private ExecutionService executionService;
    /**
     *
     */
    @Resource
    private JbpmTaskService jbpmTaskService;
    /**
     *
     */
    @Resource
    private FlowDefService flowDefService;
    /**
     *
     */
    @Resource
    private FlowNodeService flowNodeService;

    /**
     *
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取可绑定模版下拉列表
     */
    @Override
    public List<Map<String, Object>> findForSelect(String param) {
        StringBuffer sql = new StringBuffer("SELECT");
        sql.append(" F.TPL_ID AS VALUE,F.TPL_NAME AS LABEL");
        sql.append(" FROM PLAT_APPMODEL_WORDTPL F ");
        return dao.findBySql(sql.toString(), null, null);
    }

    /**
     * 根据流程定义获取列表数据
     *
     * @param defId
     * @return
     */
    @Override
    public List<Map<String, Object>> findByDefId(String defId) {
        StringBuffer sql = new StringBuffer("SELECT * FROM ");
        sql.append("JBPM6_WORDBIND J WHERE J.WORDBIND_FLOWDEFID=? ");
        sql.append(" ORDER BY J.WORDBIND_CREATE_TIME ASC ");
        return dao.findBySql(sql.toString(), new Object[]{defId}, null);
    }

    /**
     * 根据流程或者环节绑定的文书
     */
    @Override
    public List<Map<String, Object>> findFilesList(Map<String, Object> map) {
        List<Map<String, Object>> list = null;
        String TASK_ID = (String) map.get("TASK_ID");
        String EXECUTION_ID = (String) map.get("EXECUTION_ID");
        Map<String, Object> execution = this.executionService.getRecord("JBPM6_EXECUTION"
                , new String[]{"EXECUTION_ID"}, new Object[]{EXECUTION_ID});
        if (execution != null && StringUtils.isNotEmpty(EXECUTION_ID)) {
            String FLOWDEF_ID = (String) execution.get("FLOWDEF_ID");
            String TASK_NODEKEY = "";
            if (StringUtils.isNotEmpty(TASK_ID)) {
                Map<String, Object> jbpmTask = jbpmTaskService.getRecord("JBPM6_TASK",
                        new String[]{"TASK_ID"}, new Object[]{TASK_ID});
                TASK_NODEKEY = (String) jbpmTask.get("TASK_NODEKEY");
            } else {
                Map<String, Object> flowDef = this.flowDefService.getRecord("JBPM6_FLOWDEF",
                        new String[]{"FLOWDEF_ID"}, new Object[]{FLOWDEF_ID});
                TASK_NODEKEY = flowNodeService.getStartFlowNode(flowDef).getNodeKey();
            }
            list = this.findBusWordList(FLOWDEF_ID, TASK_NODEKEY);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String TPL_CODE = (String) list.get(i).get("TPL_CODE");
                    String WORDBIND_RANGE = (String) list.get(i).get("WORDBIND_RANGE");
                    StringBuffer sql = new StringBuffer("");
                    sql.append("select T.FILE_NAME,T.FILE_PATH from PLAT_SYSTEM_FILEATTACH T ");
                    sql.append(" WHERE T.SERMATER_CODE=? AND T.FILE_JBPMEXEID=? ");
                    Map<String, Object> fileMap = null;
                    if (StringUtils.isNotEmpty(WORDBIND_RANGE) && "2".equals(WORDBIND_RANGE)) {
                        sql.append(" ADN T.FILE_JBPMTASKID=? ");
                        fileMap = dao.getBySql(sql.toString(), new Object[]{TPL_CODE, EXECUTION_ID, TASK_ID});
                    } else {
                        fileMap = dao.getBySql(sql.toString(), new Object[]{TPL_CODE, EXECUTION_ID});
                    }
                    if (fileMap != null) {
                        list.get(i).put("AlreadyExists", true);
                        list.get(i).put("FILE_NAME", fileMap.get("FILE_NAME"));
                        list.get(i).put("FILE_PATH", fileMap.get("FILE_PATH"));
                    } else {
                        list.get(i).put("AlreadyExists", false);
                        list.get(i).put("FILE_NAME", "");
                        list.get(i).put("FILE_PATH", "");
                    }
                }
            }
        } else {
            String FLOWDEF_ID = (String) map.get("FLOWDEF_ID");
            Map<String, Object> flowDef = this.flowDefService.getRecord("JBPM6_FLOWDEF",
                    new String[]{"FLOWDEF_ID"}, new Object[]{FLOWDEF_ID});
            String TASK_NODEKEY = flowNodeService.getStartFlowNode(flowDef).getNodeKey();
            list = this.findBusWordList(FLOWDEF_ID, TASK_NODEKEY);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).put("AlreadyExists", false);
                    list.get(i).put("FILE_NAME", "");
                    list.get(i).put("FILE_PATH", "");
                }
            }
        }
        return list;
    }

    /**
     * 根据流程定义ID 和任务KEY或者列表
     *
     * @param flowDefId
     * @param taskNodeKey
     * @return
     */
    public List<Map<String, Object>> findBusWordList(String flowDefId,
                                                     String taskNodeKey) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT T.WORDBIND_ID,T.WORDBIND_SUBTYPE,W.TPL_ID,W.TPL_NAME,W.TPL_CODE,T.WORDBIND_NODEKEYS,");
        sql.append("T.WORDBIND_RANGE FROM JBPM6_WORDBIND T LEFT JOIN PLAT_APPMODEL_WORDTPL W");
        sql.append("  ON T.WORDBIND_TPL_ID = W.TPL_ID ");
        sql.append(" where t.wordbind_flowdefid=? and t.wordbind_nodekeys like ? ");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                new Object[]{flowDefId, "%" + taskNodeKey + "%"}, null);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                String WORDBIND_NODEKEYS = (String) list.get(i).get("WORDBIND_NODEKEYS");
                Set<String> test = new HashSet<String>(Arrays.asList(WORDBIND_NODEKEYS.split(",")));
                if (test.contains(taskNodeKey)) {
                    resultList.add(list.get(i));
                }
            }
        }
        return resultList;
    }

    /**
     *
     */
    @Override
    public void saveWordAfterCallClient(String filePath) {
        String clientId = (String) PlatAppUtil.getSession().getAttribute("clientId");
        String bindId = (String) PlatAppUtil.getSession().getAttribute("bindId");
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(bindId)) {
            map.put("bindId", bindId);
            map.put("filePath", filePath);
            //WebSocketTest.sendMessage(clientId, JSON.toJSONString(map));
            PlatAppUtil.getSession().setAttribute("clientId", null);
            PlatAppUtil.getSession().setAttribute("bindId", null);
        }

    }

}
