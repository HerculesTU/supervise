/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.supervise.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.DepartService;
import com.housoo.platform.core.service.FileAttachService;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.*;
import com.housoo.platform.supervise.dao.SuperviseDao;
import com.housoo.platform.supervise.service.SuperviseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * 描述 督查督办业务相关service实现类
 *
 * @author zxl
 * @version 1.0
 * @created 2020-04-14 15:30:11
 */
@Service("superviseService")
public class SuperviseServiceImpl extends BaseServiceImpl implements SuperviseService {

    /**
     * 搜索类型 年度 季度 月份 近七天
     */
    private static final String SEARCHTYPE_YEAR = "year";
    private static final String SEARCHTYPE_QUARTER = "quarter";
    private static final String SEARCHTYPE_MONTH = "month";
    private static final String SEARCHTYPE_DAYS = "days";

    /**
     * 督办节点 ID
     */
    private static final String DRAFT = "0";
    private static final String NODE_ID_1 = "1";
    private static final String NODE_ID_2 = "2";
    private static final String NODE_ID_3 = "3";
    private static final String NODE_ID_4 = "4";
    private static final String NODE_ID_5 = "5";

    /**
     * 督办节点 名称
     */
    private static final String NODE_NAME_1 = "任务发起";
    private static final String NODE_NAME_2 = "督办确认";
    private static final String NODE_NAME_3 = "办理反馈";
    private static final String NODE_NAME_4 = "落实反馈";
    private static final String NODE_NAME_5 = "办结反馈";

    /**
     * 全部 正在 待办 完成督办
     */

    private static final String STATUS_ALL = "1";
    private static final String STATUS_PROCESS = "2";
    private static final String STATUS_DRAFT = "3";
    private static final String STATUS_DONE = "4";
    /**
     * 需要审批
     */
    private static final String NEED_APPROVE_FLAG = "1";

    /**
     * 所引入的dao
     */
    @Resource
    private SuperviseDao dao;

    /**
     * 部门管理Service
     */
    @Resource
    private DepartService departService;
    /**
     * SuperviseService
     */
    @Resource
    private SuperviseService superviseService;
    /**
     *
     */
    @Resource
    private FileAttachService fileAttachService;

    /**
     * 获取dao
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 督查督办信息存储为草稿信息
     */
    @Override
    public Map<String, Object> saveDraftInfo(Map<String, Object> superviseMap) {
        superviseMap = saveTableSupervise(superviseMap);
        //附件在入库之前 先 删除所有的 附件记录
        StringBuffer sql = new StringBuffer(
                "SELECT * FROM plat_system_fileattach WHERE FILE_BUSTABLELNAME='tb_supervise' AND FILE_BUSRECORDID=? ");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{superviseMap.get("RECORD_ID")}, null);
        for (Map<String, Object> map : list) {
            superviseService.deleteRecord("plat_system_fileattach", new String[]{"FILE_ID"},
                    new Object[]{map.get("FILE_ID")});
        }
        saveTableAttachFileInfo(superviseMap);
        Map<String, Object> task =
                dao.getRecord("tb_supervise_task", new String[]{"SUPERVISE_ID"}, new Object[]{superviseMap.get("RECORD_ID")});
        Map<String, Object> superviseTaskMap = new HashMap<>();
        if (task != null) {
            superviseTaskMap.put("RECORD_ID", task.get("RECORD_ID"));
        }
        superviseTaskMap.put("SUPERVISE_ID", superviseMap.get("RECORD_ID"));
        //承办部门
        if (StringUtils.isNotEmpty((String) superviseMap.get("TAKER_DEPART_ID"))) {
            superviseTaskMap.put("DEPART_ID", superviseMap.get("TAKER_DEPART_ID"));
            //承办单位(若只是省一级 承办单位只有一个)
            superviseTaskMap.put("COMPANY_ID", superviseMap.get("COMPANY_ID"));
            //承办人(承办人只能配置一个)
            List<String> takerList = departService.findTakerListByDepartId((String) superviseMap.get("TAKER_DEPART_ID"));
            StringBuffer userIds = new StringBuffer("");
            for (int i = 0; i < takerList.size(); i++) {
                if (i > 0) {
                    userIds.append(",");
                }
                userIds.append(takerList.get(i));
            }
            superviseTaskMap.put("USER_ID", userIds);
        } else {
            superviseTaskMap.put("DEPART_ID", "");
            superviseTaskMap.put("COMPANY_ID", "");
            superviseTaskMap.put("USER_ID", "");
        }
        superviseTaskMap.put("STATUS", superviseMap.get("STATUS"));
        superviseTaskMap.put("CREATE_BY", superviseMap.get("CREATE_BY"));
        superviseTaskMap.put("CREATE_TIME", superviseMap.get("CREATE_TIME"));
        superviseService.saveOrUpdate("tb_supervise_task",
                superviseTaskMap, SysConstants.ID_GENERATOR_UUID, null);
        return superviseMap;
    }

    /**
     * 验证督查督办信息
     * 若 督查督办 需要审批 验证所选部门是否有承办人 审批人
     * 若 督察督办 不需要审批 验证所选部门是否有承办人
     *
     * @return
     */
    @Override
    public boolean verifySuperviseInfo(String departId) {
        boolean flag = true;
        //首先 验证承办部门是否设置承办人
        List<String> departList = departService.findTakerListByDepartId(departId);
        if (departList.size() != 0) {
            //再 验证 是否需要审批 以及 是否设置审批人
            flag = verifyIsApproval(departId);
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 存储督查督办信息  需要在四张表中存储信息 tb_supervise tb_supervise_task tb_supervise_node_info plat_system_fileattach
     *
     * @param supervise
     */
    @Override
    public Map<String, Object> saveSuperviseInfo(Map<String, Object> supervise) {
        String superviseNo = generateSuperviseNo();
        supervise.put("SUPERVISE_NO", superviseNo);
        supervise = saveTableSupervise(supervise);
        Map<String, Object> fileMap = saveTableAttachFileInfo(supervise);
        supervise.put("FILENAME", fileMap.get("fileNames"));
        supervise.put("FILE_URL", fileMap.get("filePaths"));
        //再次更新 tb_supervise 附件路径相关信息
        supervise = saveTableSupervise(supervise);
        saveTableSuperviseTask(supervise);
        saveTableSuperviseNode(supervise);
        return supervise;
    }

    /**
     * 存储 tb_supervise 信息
     *
     * @param superviseMap
     */
    private Map<String, Object> saveTableSupervise(Map<String, Object> superviseMap) {
        Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
        //督查督办 类型
        if (StringUtils.isNotEmpty((String) superviseMap.get("SUPERVISE_CLAZZ_ID"))) {
            Map<String, Object> clazzMap = departService.getRecord("tb_supervise_clazz",
                    new String[]{"RECORD_ID"}, new Object[]{superviseMap.get("SUPERVISE_CLAZZ_ID").toString()});
            superviseMap.put("SUPERVISE_CLAZZ", clazzMap.get("CLAZZ_NAME"));
            superviseMap.put("SUPERVISE_TYPE", clazzMap.get("CLAZZ_VALUE"));
        }
        //督办事项
        if (StringUtils.isNotEmpty((String) superviseMap.get("SUPERVISE_ITEM_ID"))) {
            Map<String, Object> itemMap = departService.getRecord("tb_supervise_item",
                    new String[]{"RECORD_ID"}, new Object[]{superviseMap.get("SUPERVISE_ITEM_ID").toString()});
            superviseMap.put("SUPERVISE_ITEM", itemMap.get("ITEM_NAME"));
        }

        //督办来源是否自定义 当前默认 非自定义 2
        //superviseMap.put("NO_CUSTOM", "2");
        //督办来源
        if (StringUtils.isNotEmpty((String) superviseMap.get("SUPERVISE_SOURCE_ID"))) {
            Map<String, Object> sourceMap = departService.getRecord("tb_supervise_source",
                    new String[]{"RECORD_ID"}, new Object[]{superviseMap.get("SUPERVISE_SOURCE_ID").toString()});
            superviseMap.put("SUPERVISE_SOURCE", sourceMap.get("SOURCE_NAME"));
        }
        //发起单位
        superviseMap.put("COMPANY_ID", backLoginUser.get("SYSUSER_COMPANYID"));
        //发起部门
        superviseMap.put("DEPART_ID", backLoginUser.get("DEPART_ID"));
        //发起人
        superviseMap.put("USER_ID", backLoginUser.get("SYSUSER_ID"));
        superviseMap.put("CREATE_BY", backLoginUser.get("SYSUSER_ID"));
        //当前节点
        if (!DRAFT.equals(superviseMap.get("STATUS"))) {
            //督办确认
            superviseMap.put("CURRENT_NODE", NODE_ID_2);
            superviseMap.put("STATUS", 3);//督办确认
        }
        //发起时间
        superviseMap.put("CREATE_TIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        //删除标记
        superviseMap.put("DEL_FLAG", "1");
        if (StringUtils.isEmpty((String) superviseMap.get("HANDLE_LIMIT"))) {
            superviseMap.put("HANDLE_LIMIT", null);
        }
        if (StringUtils.isEmpty((String) superviseMap.get("SUPERVISE_NO"))) {
            superviseMap.put("SUPERVISE_NO", null);
        }
        superviseMap = superviseService.saveOrUpdate("tb_supervise",
                superviseMap, SysConstants.ID_GENERATOR_UUID, null);
        return superviseMap;

    }

    /**
     * 存储 plat_system_fileattach 信息
     */
    private Map<String, Object> saveTableAttachFileInfo(Map<String, Object> superviseMap) {
        Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
        Map<String, Object> fileMap = new HashMap<>();
        String attachFilePath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        String fileJson = (String) superviseMap.get("fileJson");
        List<Map> fileList = JSON.parseArray(fileJson, Map.class);
        if (!StringUtils.isEmpty(fileJson)) {
            String filePaths = "";
            String fileNames = "";
            for (Map file : fileList) {
                Map<String, Object> fileAttach = new HashMap<>();
                String dbfilepath = (String) file.get("dbfilepath");
                String fileName = (String) file.get("originalfilename");
                String fileuploadserver = (String) file.get("fileuploadserver");
                String absoltePath = attachFilePath + dbfilepath;
                File absolteFile = new File(absoltePath);
                fileAttach.put("FILE_NAME", fileName);
                fileAttach.put("FILE_PATH", dbfilepath);
                fileAttach.put("FILE_TYPE", PlatFileUtil.getFileExt(dbfilepath));
                fileAttach.put("FILE_LENGTH", absolteFile.length());
                fileAttach.put("FILE_SIZE", PlatFileUtil.getFormatFileSize(absolteFile.length()));
                fileAttach.put("FILE_UPLOADERID", backLoginUser.get("SYSUSER_ID"));
                fileAttach.put("FILE_UPLOADERNAME", backLoginUser.get("SYSUSER_NAME"));
                fileAttach.put("FILE_BUSTABLELNAME", "tb_supervise");
                fileAttach.put("FILE_BUSRECORDID", superviseMap.get("RECORD_ID"));
                fileAttach.put("FILE_TYPEKEY", "supervise");
                if (StringUtils.isNotEmpty(fileuploadserver)) {
                    fileAttach.put("FILE_UPSERVER", fileuploadserver);
                } else {
                    fileAttach.put("FILE_UPSERVER", "1");
                }
                filePaths += dbfilepath + ";";
                fileNames += fileName + ";";
                Map<String, Object> superviseFileAttachMap = dao.getRecord("plat_system_fileattach",
                        new String[]{"FILE_BUSRECORDID", "FILE_PATH"},
                        new Object[]{superviseMap.get("RECORD_ID"), dbfilepath});
                if (superviseFileAttachMap == null) {
                    dao.saveOrUpdate("PLAT_SYSTEM_FILEATTACH", fileAttach,
                            SysConstants.ID_GENERATOR_UUID, null);
                }
            }
            fileMap.put("filePaths", filePaths);
            fileMap.put("fileNames", fileNames);
        }
        return fileMap;
    }

    /**
     * 存储 tb_supervise_task 信息
     */
    private Map<String, Object> saveTableSuperviseTask(Map<String, Object> superviseMap) {
        Map<String, Object> superviseTaskMap = new HashMap<>();
        Map<String, Object> task =
                dao.getRecord("tb_supervise_task", new String[]{"SUPERVISE_ID"}, new Object[]{superviseMap.get("RECORD_ID")});
        if (task != null) {
            superviseTaskMap.put("RECORD_ID", task.get("RECORD_ID"));
        }
        superviseTaskMap.put("SUPERVISE_ID", superviseMap.get("RECORD_ID"));
        //承办部门
        superviseTaskMap.put("DEPART_ID", superviseMap.get("TAKER_DEPART_ID"));
        superviseTaskMap.put("SUPERVISE_NO", superviseMap.get("SUPERVISE_NO"));
        //承办单位(若只是省一级 承办单位只有一个)
        superviseTaskMap.put("COMPANY_ID", superviseMap.get("COMPANY_ID"));
        //承办人(承办人只能配置一个)
        List<String> takerList = departService.findTakerListByDepartId((String) superviseMap.get("TAKER_DEPART_ID"));
        StringBuffer userIds = new StringBuffer("");
        for (int i = 0; i < takerList.size(); i++) {
            if (i > 0) {
                userIds.append(",");
            }
            userIds.append(takerList.get(i));
        }
        superviseTaskMap.put("USER_ID", userIds);
        superviseTaskMap.put("STATUS", superviseMap.get("STATUS"));
        superviseTaskMap.put("CREATE_BY", superviseMap.get("CREATE_BY"));
        superviseTaskMap.put("CREATE_TIME", superviseMap.get("CREATE_TIME"));
        superviseTaskMap = superviseService.saveOrUpdate("tb_supervise_task",
                superviseTaskMap, SysConstants.ID_GENERATOR_UUID, null);
        return superviseTaskMap;
    }

    /**
     * 存储 tb_supervise_node_info 信息
     */
    private Map<String, Object> saveTableSuperviseNode(Map<String, Object> superviseMap) {
        Map<String, Object> nodeMap = new HashMap<>();
        nodeMap.put("SUPERVISE_ID", superviseMap.get("RECORD_ID"));
        nodeMap.put("SUPERVISE_NO", superviseMap.get("SUPERVISE_NO"));
        nodeMap.put("SUPERVISE_NO", superviseMap.get("SUPERVISE_NO"));
        nodeMap.put("NODE_ID", NODE_ID_1);
        nodeMap.put("NODE_NAME", NODE_NAME_1);
        nodeMap.put("CREATE_BY", superviseMap.get("CREATE_BY"));
        nodeMap.put("CREATE_TIME", superviseMap.get("CREATE_TIME"));
        nodeMap = superviseService.saveOrUpdate("tb_supervise_node_info",
                nodeMap, SysConstants.ID_GENERATOR_UUID, null);
        nodeMap.put("RECORD_ID", "");
        nodeMap.put("NODE_ID", NODE_ID_2);
        nodeMap.put("NODE_NAME", NODE_NAME_2);
        nodeMap = superviseService.saveOrUpdate("tb_supervise_node_info",
                nodeMap, SysConstants.ID_GENERATOR_UUID, null);
        return nodeMap;
    }

    /**
     * 生成督查编号  编号规则  20200001
     */
    private String generateSuperviseNo() {
        String superviseNo = "";
        int year = PlatDateTimeUtil.getCurrentYear();
        StringBuffer querySql = new StringBuffer("SELECT MAX(T.SUPERVISE_NO) AS SUPERVISE_NO FROM tb_supervise_task T");
        Map<String, Object> queryInfoMap = dao.getBySql(querySql.toString(), new Object[]{});
        //判断是否为同一年
        //先判断是否为null
        if (null != queryInfoMap.get("SUPERVISE_NO")) {
            if (year == Integer.parseInt(queryInfoMap.get("SUPERVISE_NO").toString().substring(0, 4))) {
                superviseNo = (Integer.parseInt(String.valueOf(queryInfoMap.get("SUPERVISE_NO"))) + 1) + "";
            }
        } else {
            superviseNo = year + "0001";
        }
        return superviseNo;
    }

    /**
     * 验证 督察督办 各环节（办理反馈  落实反馈  办结反馈）是否需要审批
     */
    private boolean verifyIsApproval(String departId) {
        boolean flag = true;
        Map<String, Object> supervise1 = this.getRecord("tb_supervise_approve", new String[]{"NODE_NAME"}, new Object[]{NODE_NAME_3});
        Map<String, Object> supervise3 = this.getRecord("tb_supervise_approve", new String[]{"NODE_NAME"}, new Object[]{NODE_NAME_5});

        //办理反馈  落实反馈  办结反馈 三个环节中 任意一个环节需要审批 则 需要判断当前承办部门是否配置审批人
        if (NEED_APPROVE_FLAG.equals(supervise1.get("NEED_APPROVE_FLAG").toString())
                || NEED_APPROVE_FLAG.equals(supervise3.get("NEED_APPROVE_FLAG").toString())) {
            //获取 部门是否配置审批人
            List<String> departList = departService.findDirectorListByDepartId(departId);
            if (departList.size() == 0) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 根据督办事项ID初始化督办事项的督办流程
     *
     * @param request
     * @return
     */
    @Override
    public List<Map<String, Object>> initSuperviseProgress(HttpServletRequest request) {
        List<Map<String, Object>> result = new ArrayList<>();
        String superviseId = request.getParameter("SUPERVISE_ID");
        Map<String, Object> supervise = dao.getRecord("tb_supervise", new String[]{"RECORD_ID"}, new Object[]{superviseId});
        //获取督办部门
        String dbDepartName = (String) superviseService.getRecord("plat_system_depart", new String[]{"DEPART_ID"}, new Object[]{supervise.get("DEPART_ID")}).get("DEPART_NAME");
        //获取立项部门、获取承办部门、获取审核节点
        List<Map<String, Object>> taskDeparts = dao.findTaskListBySuperviseId(superviseId);
        //获取审核节点
        List<Map<String, Object>> taskNodes = dao.findTaskNodeListBySuperviseId(superviseId);
        for (Map<String, Object> node : taskNodes) {
            if (NODE_ID_1.equals(node.get("NODE_ID"))) {
                Map<String, Object> progress = new HashMap<>();
                progress.put("SHORT_NAME", node.get("SHORT_NAME"));
                progress.put("VALUE", dbDepartName + "发起");
                progress.put("date", node.get("CREATE_TIME"));
                progress.put("status", "1");//办结
                result.add(progress);
                if (NEED_APPROVE_FLAG.equals(node.get("NEED_APPROVE_FLAG"))) {
                    progress = new HashMap<>();
                    progress.put("temp", dbDepartName + "审核");
                    result.add(progress);
                }
            }
            if (NODE_ID_2.equals(node.get("NODE_ID"))) {
                for (Map<String, Object> taskDepart : taskDeparts) {
                    String departName = (String) taskDepart.get("DEPART_NAME");
                    Map<String, Object> progress = new HashMap<>();
                    progress.put("SHORT_NAME", node.get("SHORT_NAME"));
                    progress.put("VALUE", departName + "承办人接收");
                    progress.put("date", node.get("CREATE_TIME"));
                    progress.put("status", "1");//办结
                    result.add(progress);
                }
            }
            if (NODE_ID_3.equals(node.get("NODE_ID"))) {
                for (Map<String, Object> taskDepart : taskDeparts) {
                    String departName = (String) taskDepart.get("DEPART_NAME");
                    Map<String, Object> progress = new HashMap<>();
                    progress.put("SHORT_NAME", node.get("SHORT_NAME"));
                    progress.put("VALUE", departName + "承办人办理");
                    progress.put("date", node.get("CREATE_TIME"));
                    if (Integer.parseInt(supervise.get("STATUS").toString()) <= 5) {
                        if (NODE_ID_3.equals(supervise.get("CURRENT_NODE"))) {
                            progress.put("status", "0");//在办
                        } else {
                            progress.put("status", "2");//未开始
                        }
                    } else {
                        progress.put("status", "1");//办结
                    }
                    result.add(progress);
                    if (NEED_APPROVE_FLAG.equals(node.get("NEED_APPROVE_FLAG"))) {
                        progress = new HashMap<>();
                        progress.put("temp", departName + "主任审核");
                        result.add(progress);
                    }
                }
            }
            if (NODE_ID_4.equals(node.get("NODE_ID"))) {
                for (Map<String, Object> taskDepart : taskDeparts) {
                    String departName = (String) taskDepart.get("DEPART_NAME");
                    Map<String, Object> progress = new HashMap<>();
                    progress.put("SHORT_NAME", node.get("SHORT_NAME"));
                    progress.put("VALUE", departName + "承办人落实");
                    progress.put("date", node.get("CREATE_TIME"));
                    if (Integer.parseInt(supervise.get("STATUS").toString()) <= 7) {
                        if (NODE_ID_4.equals(supervise.get("CURRENT_NODE"))) {
                            progress.put("status", "0");//在办
                        } else {
                            progress.put("status", "2");//未开始
                        }
                    } else {
                        progress.put("status", "1");//办结
                    }
                    result.add(progress);
                    if (NEED_APPROVE_FLAG.equals(node.get("NEED_APPROVE_FLAG"))) {
                        progress = new HashMap<>();
                        progress.put("temp", departName + "主任审核");
                        result.add(progress);
                    }
                }
            }
            if (NODE_ID_5.equals(node.get("NODE_ID"))) {
                for (Map<String, Object> taskDepart : taskDeparts) {
                    String departName = (String) taskDepart.get("DEPART_NAME");
                    Map<String, Object> progress = new HashMap<>();
                    progress.put("SHORT_NAME", node.get("SHORT_NAME"));
                    progress.put("VALUE", departName + "承办人办结");
                    progress.put("date", node.get("CREATE_TIME"));
                    if (Integer.parseInt(supervise.get("STATUS").toString()) <= 8) {
                        if (NODE_ID_5.equals(supervise.get("CURRENT_NODE"))) {
                            progress.put("status", "0");//在办
                        } else {
                            progress.put("status", "2");//未开始
                        }
                    } else {
                        progress.put("status", "1");//办结
                    }
                    result.add(progress);
                    if (NEED_APPROVE_FLAG.equals(node.get("NEED_APPROVE_FLAG"))) {
                        progress = new HashMap<>();
                        progress.put("temp", departName + "主任审核");
                        result.add(progress);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取督办任务逾期情况
     *
     * @param userId  用户Id
     * @param clazzId 督办类型Id
     * @param status  正在督办 2  待发督办 3  完后督办 4 全部督办 1
     * @return
     */
    @Override
    public List<List<Map<String, Object>>> getSuperviseInfo(String userId, String clazzId, String status) {
        List<List<Map<String, Object>>> result = new ArrayList<>();
        List<Map<String, Object>> superviseList;
        if (STATUS_DRAFT.equals(status) || STATUS_ALL.equals(status)) {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT T1.RECORD_ID,T1.CREATE_TIME,T1.KEYWORDS,T1.TITLE,T1.SUPERVISE_CLAZZ," +
                    " T1.SUPERVISE_ITEM,T1.SUPERVISE_SOURCE FROM tb_supervise T1 WHERE T1.`STATUS`='0' AND T1.CREATE_BY=? " +
                    " AND T1.SUPERVISE_CLAZZ_ID=? " +
                    " ORDER BY T1.CREATE_TIME DESC");
            superviseList =
                    this.findBySql(sql.toString(), new Object[]{userId, clazzId}, null);
            for (Map<String, Object> map : superviseList) {
                map.put("DRAFT", "1");
            }
            for (int i = 0; i < superviseList.size(); i++) {
                List<Map<String, Object>> temp = new ArrayList<>();
                superviseList.get(i).put("DRAFT", "1");
                temp.add(superviseList.get(i));
                result.add(temp);
            }
        }
        if (!STATUS_DRAFT.equals(status)) {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT RECORD_ID,DEPART_ID,KEYWORDS,HANDLE_LIMIT FROM tb_supervise WHERE CREATE_BY=? AND SUPERVISE_CLAZZ_ID=? AND STATUS NOT IN ('10')");
            if (STATUS_DONE.equals(status)) {
                sql.append(" AND `STATUS` IN ('9') ");
            } else if (STATUS_PROCESS.equals(status)) {
                sql.append(" AND `STATUS` NOT IN ('9','0','10')  ");
            }
            sql.append("ORDER BY CREATE_TIME DESC");
            List<Map<String, Object>> superviseInfoListByUserId =
                    this.findBySql(sql.toString(), new Object[]{userId, clazzId}, null);
            for (Map<String, Object> map : superviseInfoListByUserId) {
                StringBuffer querySql = new StringBuffer();
                String recordId = map.get("RECORD_ID").toString();
                querySql.append("SELECT T4.RECORD_ID,T2.CREATE_TIME,T1.NODE_NAME,T3.DEPART_ID AS TAKER_DEPART_ID,T4.HANDLE_LIMIT,T1.NODE_ID, " +
                        " T4.DEPART_ID AS COMMIT_DEPART_ID ,T4.KEYWORDS,T4.TITLE,T4.SUPERVISE_CLAZZ,T4.SUPERVISE_ITEM,T4.STATUS,T4.SUPERVISE_NO, " +
                        " T4.CURRENT_NODE,T4.SUPERVISE_SOURCE,T4.SUPERVISE_TIME_TYPE FROM tb_supervise_approve T1 " +
                        " LEFT JOIN tb_supervise_node_info T2 ON T1.NODE_NAME=T2.NODE_NAME  AND T2.SUPERVISE_ID=? " +
                        " LEFT JOIN tb_supervise_task T3 ON T3.SUPERVISE_ID=T2.SUPERVISE_ID" +
                        " LEFT JOIN tb_supervise T4 ON T4.RECORD_ID=T2.SUPERVISE_ID WHERE T1.DEL_FLAG = '1' ");
                List<Map<String, Object>> superviseInfoByRecordId =
                        this.findBySql(querySql.toString(), new Object[]{recordId}, null);
                //发起部门
                Map<String, Object> commitMap = departService.getRecord("plat_system_depart",
                        new String[]{"DEPART_ID"}, new Object[]{map.get("DEPART_ID").toString()});
                //承办部门
                Map<String, Object> takerMap = new HashMap<>();
                if (StringUtils.isNotEmpty((String) superviseInfoByRecordId.get(0).get("RECORD_ID"))) {
                    takerMap = departService.getRecord("plat_system_depart",
                            new String[]{"DEPART_ID"}, new Object[]{superviseInfoByRecordId.get(0).get("TAKER_DEPART_ID").toString()});
                }
                for (Map<String, Object> param : superviseInfoByRecordId) {
                    //分别为 办理反馈 办结反馈 实际办理时间
                    //办理反馈 办结反馈 逾期判断 1.首先判断 固定期限:1,反馈频次:2
                    //                         固定期限  办理反馈  填写的办理时限 与 （督办确认的时间与承办人  第一次  提交的办理反馈 时间差） 比较
                    //                                  办结反馈   填写的办理时限 - 办理反馈时间  与  （办结反馈发起的时间 与 承办人 第一次 提交的办结反馈 时间差 ） 比较
                    //                         反馈频次
                    long intervalTime2 = 0;
                    long intervalTime3 = 0;
                    long intervalTime5 = 0;
                    StringBuilder feedbackSql = new StringBuilder(
                            "SELECT * FROM tb_supervise_feedback WHERE SUPERVISE_ID=? AND NODE_ID=? ORDER BY CREATE_TIME DESC");
                    List<Map<String, Object>> feedbackList3 =
                            this.findBySql(feedbackSql.toString(), new Object[]{param.get("RECORD_ID"), NODE_ID_3}, null);
                    List<Map<String, Object>> feedbackList5 =
                            this.findBySql(feedbackSql.toString(), new Object[]{param.get("RECORD_ID"), NODE_ID_5}, null);
                    StringBuilder confirmSql = new StringBuilder(
                            " SELECT * FROM tb_supervise_confirm WHERE SUPERVISE_ID=? ");
                    List<Map<String, Object>> confirmList =
                            this.findBySql(confirmSql.toString(), new Object[]{param.get("RECORD_ID")}, null);
                    if (NODE_ID_3.equals(param.get("NODE_ID"))) {
                        if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                            if (feedbackList3.size() != 0) {
                                //已反馈
                                intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) confirmList.get(0).get("CREATE_TIME"),
                                        (String) feedbackList3.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                                param.put("feedbackStatus", "1");
                            } else {
                                //未反馈
                                intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) confirmList.get(0).get("CREATE_TIME"),
                                        PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                                param.put("feedbackStatus", "2");
                            }
                        }
                    }
                    if (NODE_ID_5.equals(param.get("NODE_ID"))) {
                        if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                            if (feedbackList5.size() != 0) {
                                //已反馈
                                intervalTime5 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                        (String) feedbackList5.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                                param.put("feedbackStatus", "1");
                            } else {
                                //未反馈
                                intervalTime5 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                        PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                                param.put("feedbackStatus", "2");
                            }
                        }
                    }
                    int limitTime = 0;
                    if (null != map.get("HANDLE_LIMIT")) {
                        limitTime = (Integer) map.get("HANDLE_LIMIT");
                    }
                    //任务发起
                    if (NODE_ID_1.equals(param.get("NODE_ID"))) {
                        /*if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME")) &&
                                10 == Integer.parseInt(String.valueOf(param.get("STATUS")))) {
                            param.put("stopStatus", "1");
                        }*/
                        //判断是否需要审批
                        param.put("feedbackStatus", "1");
                        param.put("timeStatus", "完成");
                        param.put("COMMIT_DEPART_NAME", commitMap.get("DEPART_NAME"));
                        param.put("KEYWORDS", map.get("KEYWORDS"));
                    }
                    //督办确认
                    if (NODE_ID_2.equals(param.get("NODE_ID"))) {
                        if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                            /*if (10 == Integer.parseInt(String.valueOf(param.get("STATUS")))) {
                                param.put("stopStatus", "1");
                            }*/
                            //判断 督办确认逾期
                            if (confirmList.size() != 0) {
                                param.put("feedbackStatus", "1");
                                param.put("timeStatus", "完成");
                            } else {
                                intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) param.get("CREATE_TIME"),
                                        PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);

                                if (intervalTime2 > 24) {
                                    param.put("timeStatus", "逾期");
                                } else {
                                    param.put("timeStatus", "办理中");
                                }
                            }
                            param.put("TAKER_DEPART_NAME", takerMap.get("DEPART_NAME"));
                            param.put("KEYWORDS", map.get("KEYWORDS"));
                        } else {
                            param.put("timeStatus", "未办理");
                        }
                    }
                    //办理反馈
                    if (NODE_ID_3.equals(param.get("NODE_ID"))) {
                        if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                            /*if (10 == Integer.parseInt(String.valueOf(param.get("STATUS")))) {
                                param.put("stopStatus", "1");
                            }*/
                            //判断逾期否
                            if (Integer.parseInt(String.valueOf(param.get("CURRENT_NODE"))) > 3) {
                                param.put("timeStatus", "完成");
                            } else {
                                //办理反馈 若 与办理期限比较
                                if (intervalTime3 <= limitTime * 24) {
                                    param.put("timeStatus", "办理中");
                                } else {
                                    param.put("timeStatus", "逾期");
                                }
                            }
                        } else {
                            param.put("timeStatus", "未办理");
                        }
                        param.put("TAKER_DEPART_NAME", takerMap.get("DEPART_NAME"));
                        param.put("KEYWORDS", map.get("KEYWORDS"));
                    }
                    //办结反馈
                    if (NODE_ID_5.equals(param.get("NODE_ID"))) {
                        if (StringUtils.isNotEmpty((String) param.get("CREATE_TIME"))) {
                            /*if (10 == Integer.parseInt(String.valueOf(param.get("STATUS")))) {
                                param.put("stopStatus", "1");
                            }*/
                            if (9 == Integer.parseInt(String.valueOf(param.get("STATUS")))) {
                                param.put("timeStatus", "完成");
                            } else {
                                //需要 去除 第三 阶段实际 消耗时间
                                if (intervalTime5 <= (limitTime * 24 - intervalTime3)) {
                                    param.put("timeStatus", "办理中");
                                } else {
                                    param.put("timeStatus", "逾期");
                                }
                            }
                        } else {
                            param.put("timeStatus", "未办理");
                        }
                    }
                    param.put("TAKER_DEPART_NAME", takerMap.get("DEPART_NAME"));
                    param.put("KEYWORDS", map.get("KEYWORDS"));
                }
                //草稿没有文书号
                if (superviseInfoByRecordId.get(0).get("SUPERVISE_NO") != null) {
                    result.add(superviseInfoByRecordId);
                }
            }
        }
        return result;
    }

    /**
     * 根据主键ID 和 nodeId获取当前 节点是否逾期
     *
     * @param superviseId 主键ID
     * @param nodeId      节点ID
     * @return
     */
    public Map<String, Object> getOutTimeInfoBySuperviseIdAndNodeId(String superviseId, String nodeId) {
        Map<String, Object> result = new HashMap<>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T4.RECORD_ID,T2.CREATE_TIME,T1.NODE_NAME,T3.DEPART_ID AS TAKER_DEPART_ID,T4.HANDLE_LIMIT,T1.NODE_ID, " +
                " T4.DEPART_ID AS COMMIT_DEPART_ID ,T4.KEYWORDS,T4.TITLE,T4.SUPERVISE_CLAZZ,T4.SUPERVISE_ITEM,T4.STATUS,T4.SUPERVISE_NO, " +
                " T4.CURRENT_NODE,T4.SUPERVISE_SOURCE,T4.SUPERVISE_TIME_TYPE FROM tb_supervise_approve T1 " +
                " LEFT JOIN tb_supervise_node_info T2 ON T1.NODE_NAME=T2.NODE_NAME  AND T2.SUPERVISE_ID=? " +
                " LEFT JOIN tb_supervise_task T3 ON T3.SUPERVISE_ID=T2.SUPERVISE_ID" +
                " LEFT JOIN tb_supervise T4 ON T4.RECORD_ID=T2.SUPERVISE_ID WHERE T1.DEL_FLAG = '1' AND T2.NODE_ID=? ");

        List<Map<String, Object>> superviseInfoByRecordId =
                this.findBySql(sql.toString(), new Object[]{superviseId, nodeId}, null);
        int limitTime = (Integer) superviseInfoByRecordId.get(0).get("HANDLE_LIMIT");
        StringBuilder feedbackSql = new StringBuilder(
                "SELECT * FROM tb_supervise_feedback WHERE SUPERVISE_ID=? AND NODE_ID=? ORDER BY CREATE_TIME DESC");
        StringBuilder confirmSql = new StringBuilder(
                "SELECT * FROM tb_supervise_confirm WHERE SUPERVISE_ID=?  ");
        List<Map<String, Object>> confirmList =
                this.findBySql(confirmSql.toString(), new Object[]{superviseId}, null);
        List<Map<String, Object>> feedbackList3 =
                this.findBySql(feedbackSql.toString(), new Object[]{superviseId, NODE_ID_3}, null);
        List<Map<String, Object>> feedbackList5 =
                this.findBySql(feedbackSql.toString(), new Object[]{superviseId, NODE_ID_5}, null);
        if (superviseInfoByRecordId.size() != 0) {
            for (Map<String, Object> map : superviseInfoByRecordId) {
                if ("1".equals(nodeId)) {

                }
                if ("2".equals(nodeId)) {
                    //判断 督办确认逾期
                    if (confirmList.size() != 0) {
                        long intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) map.get("CREATE_TIME"),
                                (String) confirmList.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                        if (intervalTime2 <= 24) {
                            result.put("timeStatus", "完成未逾期");
                        } else {
                            result.put("timeStatus", "完成逾期");
                        }

                    } else {
                        long intervalTime2 = PlatDateTimeUtil.getIntervalTime((String) map.get("CREATE_TIME"),
                                PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                        if (intervalTime2 > 24) {
                            result.put("timeStatus", "未办理逾期");
                        } else {
                            result.put("timeStatus", "未办理未逾期");
                        }
                    }
                }
                if ("3".equals(nodeId)) {
                    if (feedbackList3.size() != 0) {
                        //判断逾期否
                        if (Integer.parseInt(String.valueOf(map.get("CURRENT_NODE"))) > 3) {
                            //已反馈
                            long intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) confirmList.get(0).get("CREATE_TIME"),
                                    (String) feedbackList3.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                            if (intervalTime3 <= limitTime * 24) {
                                result.put("timeStatus", "完成未逾期");
                            } else {
                                result.put("timeStatus", "完成逾期");
                            }
                        } else {
                            //已反馈
                            long intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) confirmList.get(0).get("CREATE_TIME"),
                                    (String) feedbackList3.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                            //办理反馈 若 与办理期限比较
                            if (intervalTime3 <= limitTime * 24) {
                                result.put("timeStatus", "办理中未逾期");
                            } else {
                                result.put("timeStatus", "办理中逾期");
                            }
                        }
                    } else {
                        long intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) confirmList.get(0).get("CREATE_TIME"),
                                PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                        if (intervalTime3 <= limitTime * 24) {
                            result.put("timeStatus", "未办理未逾期");
                        } else {
                            result.put("timeStatus", "未办理未逾期");
                        }
                    }
                }
                if ("5".equals(nodeId)) {
                    long intervalTime3 = PlatDateTimeUtil.getIntervalTime((String) confirmList.get(0).get("CREATE_TIME"),
                            (String) feedbackList3.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                    if (feedbackList5.size() != 0) {
                        //承办人反馈
                        if (9 == Integer.parseInt(String.valueOf(map.get("STATUS")))) {
                            long intervalTime5 = PlatDateTimeUtil.getIntervalTime((String) map.get("CREATE_TIME"),
                                    (String) feedbackList5.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                            if (intervalTime3 + intervalTime5 <= limitTime * 24) {
                                result.put("timeStatus", "完成未逾期");
                            } else {
                                result.put("timeStatus", "完成逾期");
                            }
                        } else {
                            long intervalTime5 = PlatDateTimeUtil.getIntervalTime((String) map.get("CREATE_TIME"),
                                    (String) feedbackList5.get(0).get("CREATE_TIME"), "yyyy-MM-dd HH:mm:ss", 2);
                            if (intervalTime3 + intervalTime5 <= limitTime * 24) {
                                result.put("timeStatus", "办理中未逾期");
                            } else {
                                result.put("timeStatus", "办理中逾期");
                            }
                        }
                    } else {
                        //承办人 未反馈
                        long intervalTime5 = PlatDateTimeUtil.getIntervalTime((String) confirmList.get(0).get("CREATE_TIME"),
                                PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", 2);
                        if (intervalTime3 + intervalTime5 <= limitTime * 24) {
                            result.put("timeStatus", "未办理未逾期");
                        } else {
                            result.put("timeStatus", "未办理未逾期");
                        }
                    }
                }
            }
        } else {
            result.put("time", "未开始");
        }
        return result;
    }


}
