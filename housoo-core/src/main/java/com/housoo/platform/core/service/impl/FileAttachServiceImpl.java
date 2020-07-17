package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.FileAttachDao;
import com.housoo.platform.core.service.FileAttachService;
import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.core.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 描述 附件信息业务相关service实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-04-14 10:42:35
 */
@Service("fileAttachService")
public class FileAttachServiceImpl extends BaseServiceImpl implements FileAttachService {
    /**
     * 禁止上传的文件类型
     */
    public static List<String> noAllowFileTypes = Arrays.asList("exe", "bat", "jsp", "html", "com", "sys", "jspx");
    /**
     * 所引入的dao
     */
    @Resource
    private FileAttachDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 获取附件列表数据
     *
     * @param busTableName:业务表名称
     * @param busRecordId:业务表记录ID
     * @param typeKey:类别KEY
     * @return
     */
    @Override
    public List<Map<String, Object>> findList(String busTableName, String busRecordId, String typeKey) {
        StringBuffer sql = new StringBuffer("SELECT * FROM PLAT_SYSTEM_FILEATTACH T ");
        sql.append("WHERE T.FILE_BUSTABLELNAME=? AND T.FILE_BUSRECORDID=? ");
        List params = new ArrayList();
        params.add(busTableName);
        params.add(busRecordId);
        if (StringUtils.isNotEmpty(typeKey)) {
            sql.append(" AND T.FILE_TYPEKEY=? ");
            params.add(typeKey);
        }
        sql.append(" ORDER BY T.FILE_CREATETIME ASC ");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), params.toArray(), null);
        return list;
    }

    /**
     * 删除附件记录数据
     *
     * @param busTableName
     * @param busRecordId
     * @param typeKey
     */
    @Override
    public void deleteFiles(String busTableName, String busRecordId, String typeKey) {
        StringBuffer sql = new StringBuffer("DELETE FROM ");
        sql.append("PLAT_SYSTEM_FILEATTACH WHERE FILE_BUSTABLELNAME=?");
        sql.append(" AND FILE_BUSRECORDID=? ");
        List params = new ArrayList();
        params.add(busTableName);
        params.add(busRecordId);
        if (StringUtils.isNotEmpty(typeKey)) {
            sql.append(" AND FILE_TYPEKEY=? ");
            params.add(typeKey);
        }
        dao.executeSql(sql.toString(), params.toArray());
    }

    /**
     * 批量保存附件信息记录
     *
     * @param fileListJson:附件的JSON
     * @param busTableName:涉及业务表
     * @param busRecordId:涉及业务表记录ID
     * @param typeKey:分类KEY,如果同一个业务表下没有不同的分类上传，可以不传该参数
     * @return 返回文件ID列表
     */
    @Override
    public List<String> saveFileAttachs(String fileListJson,
                                        String busTableName, String busRecordId, String typeKey) {
        this.deleteFiles(busTableName, busRecordId, typeKey);
        List<String> fileIds = new ArrayList<String>();
        if (StringUtils.isNotEmpty(fileListJson)) {
            List<Map> fileList = JSON.parseArray(fileListJson, Map.class);
            //获取文件存储路径
            String attachFilePath = PlatPropUtil.getPropertyValue("config.properties"
                    , "attachFilePath");
            //获取当前登录用户
            Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
            for (Map file : fileList) {
                String originalfilename = (String) file.get("originalfilename");
                String dbfilepath = (String) file.get("dbfilepath");
                String fileuploadserver = (String) file.get("fileuploadserver");
                String absoltePath = attachFilePath + dbfilepath;
                File absolteFile = new File(absoltePath);
                Map<String, Object> fileAttach = new HashMap<String, Object>();
                fileAttach.put("FILE_NAME", originalfilename);
                fileAttach.put("FILE_PATH", dbfilepath);
                fileAttach.put("FILE_TYPE", PlatFileUtil.getFileExt(dbfilepath));
                fileAttach.put("FILE_LENGTH", absolteFile.length());
                fileAttach.put("FILE_SIZE", PlatFileUtil.getFormatFileSize(absolteFile.length()));
                if (backLoginUser != null) {
                    fileAttach.put("FILE_UPLOADERID", backLoginUser.get("SYSUSER_ID"));
                    fileAttach.put("FILE_UPLOADERNAME", backLoginUser.get("SYSUSER_NAME"));
                }
                fileAttach.put("FILE_BUSTABLELNAME", busTableName);
                fileAttach.put("FILE_BUSRECORDID", busRecordId);
                fileAttach.put("FILE_TYPEKEY", typeKey);
                if (StringUtils.isNotEmpty(fileuploadserver)) {
                    fileAttach.put("FILE_UPSERVER", fileuploadserver);
                } else {
                    fileAttach.put("FILE_UPSERVER", "1");
                }
                fileAttach = dao.saveOrUpdate("PLAT_SYSTEM_FILEATTACH", fileAttach,
                        SysConstants.ID_GENERATOR_UUID, null);
                fileIds.add(fileAttach.get("FILE_ID").toString());
            }
        }

        return fileIds;
    }

    /**
     * 保存流程流转中的文书记录
     *
     * @param wordUploadJson
     * @param jbpmFlowInfo
     */
    @Override
    public void saveFlowWords(String wordUploadJson, JbpmFlowInfo jbpmFlowInfo) {
        String attachFilePath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        Map<String, Object> backLoginUser = PlatAppUtil.getBackPlatLoginUser();
        if (StringUtils.isNotEmpty(wordUploadJson)) {
            List<Map> uploadList = JSON.parseArray(wordUploadJson, Map.class);
            for (Map<String, Object> file : uploadList) {
                String ATTACH_KEY = (String) file.get("ATTACH_KEY");
                String FILE_NAME = (String) file.get("FILE_NAME");
                String FILE_PATH = (String) file.get("FILE_PATH");
                String absoltePath = attachFilePath + FILE_PATH;
                //先清除数据
                dao.deleteRecord("PLAT_SYSTEM_FILEATTACH",
                        new String[]{"SERMATER_CODE", "FILE_JBPMEXEID"},
                        new Object[]{ATTACH_KEY, jbpmFlowInfo.getJbpmExeId()});
                if (StringUtils.isNotEmpty(FILE_PATH)) {
                    File absolteFile = new File(absoltePath);
                    Map<String, Object> fileAttach = new HashMap<String, Object>();
                    fileAttach.put("FILE_NAME", FILE_NAME + "." + PlatFileUtil.getFileExt(FILE_PATH));
                    fileAttach.put("FILE_PATH", FILE_PATH);
                    fileAttach.put("FILE_TYPE", PlatFileUtil.getFileExt(FILE_PATH));
                    fileAttach.put("FILE_LENGTH", absolteFile.length());
                    fileAttach.put("FILE_SIZE", PlatFileUtil.getFormatFileSize(absolteFile.length()));
                    if (backLoginUser != null) {
                        fileAttach.put("FILE_UPLOADERID", backLoginUser.get("SYSUSER_ID"));
                        fileAttach.put("FILE_UPLOADERNAME", backLoginUser.get("SYSUSER_NAME"));
                    }
                    fileAttach.put("SERMATER_CODE", ATTACH_KEY);
                    fileAttach.put("FILE_JBPMEXEID", jbpmFlowInfo.getJbpmExeId());
                    fileAttach.put("FILE_JBPMTASKID", jbpmFlowInfo.getJbpmOperingTaskId());
                    fileAttach.put("SERMATER_CODE", ATTACH_KEY);
                    fileAttach = dao.saveOrUpdate("PLAT_SYSTEM_FILEATTACH", fileAttach,
                            SysConstants.ID_GENERATOR_UUID, null);
                }
            }
        }
    }

    /**
     * 通用保存文书数据
     *
     * @param postParams
     * @param jbpmFlowInfo
     * @return
     */
    @Override
    public Map<String, Object> saveFlowWords(Map<String, Object> postParams, JbpmFlowInfo jbpmFlowInfo) {
        String wordUploadJson = (String) postParams.get("wordUploadJson");
        if (StringUtils.isNotEmpty(wordUploadJson)) {
            this.saveFlowWords(wordUploadJson, jbpmFlowInfo);
            return postParams;
        } else {
            return postParams;
        }
    }

    /**
     * 实现文件上传功能
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> uploadFile(HttpServletRequest request, boolean isForServlet) {
        Map<String, Object> result = new HashMap<String, Object>();
        String guid = request.getParameter("guid");
        // 上传文件根目录文件
        String uploadRootFolder = request.getParameter("uploadRootFolder");
        // 获取附件的根路径
        String rooPath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        // 获取网站的访问根路径
        String attachFileUrl = PlatPropUtil.getPropertyValue("config.properties", "attachFileUrl");
        // 获取文件服务器的访问路径
        String fileServerUrl = PlatPropUtil.getPropertyValue("config.properties", "fileServerUrl");
        // 获取当前的日期
        Date date = new Date();
        //String timeFolder = PlatDateTimeUtil.formatDate(date, "yyyy/MM/dd");
        String timeFolder = PlatDateTimeUtil.formatDate(date, "yyyyMMdd");
        StringBuffer uploadFileFolderPath = new StringBuffer(rooPath);
        uploadFileFolderPath.append(uploadRootFolder).append("/").append(timeFolder).append("/");
        // 定义存储在数据库中的文件路径
        StringBuffer dbFilePath = new StringBuffer(uploadRootFolder).append("/").append(timeFolder).append("/");
        try {
            // 创建一个通用的多部分解析器
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                    request.getSession().getServletContext());
            boolean unilegalFile = false;
            // 判断 request 是否有文件上传,即多部分请求
            if (multipartResolver.isMultipart(request)) {
                // 转换成多部分request
                MultipartHttpServletRequest multiRequest = null;
                if (isForServlet) {
                    multiRequest = multipartResolver.resolveMultipart(request);
                } else {
                    multiRequest = (MultipartHttpServletRequest) request;
                }
                // 取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    // 取得上传文件
                    MultipartFile file = multiRequest.getFile(iter.next());
                    if (file != null) {
                        // 取得当前上传文件的文件名称
                        String originalFileName = file.getOriginalFilename();
                        //获取扩展名
                        String fileExt = PlatFileUtil.getFileExt(originalFileName);
                        if (noAllowFileTypes.contains(fileExt.toLowerCase())) {
                            unilegalFile = true;
                            result.put("success", "false");
                            result.put("msg", "上传失败,传入文件格式非法!");
                            break;
                        } else {
                            String fileName = UUIDGenerator.getUUID() + "." + PlatFileUtil.getFileExt(originalFileName);
                            dbFilePath.append(fileName);
                            if (request.getParameter("chunk") == null) {
                                // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                                if (StringUtils.isNotEmpty(originalFileName)
                                        && StringUtils.isNotEmpty(originalFileName.trim())) {
                                    File targetFile = new File(uploadFileFolderPath.toString(), fileName);
                                    if (!targetFile.exists()) {
                                        targetFile.mkdirs();
                                    }
                                    file.transferTo(targetFile);
                                }
                            } else {
                                int chunk = Integer.parseInt(request.getParameter("chunk")); // 当前分片
                                int chunks = Integer.parseInt(request.getParameter("chunks")); // 分片总计
                                String tempFileDir = request.getSession().getServletContext().getRealPath(
                                        uploadFileFolderPath + "Temporary/" + originalFileName + "_" + guid + "/");
                                File parentFileDir = new File(tempFileDir);
                                if (!parentFileDir.exists()) {
                                    parentFileDir.mkdirs();
                                } else {
                                    if (chunk == 0) {
                                        PlatFileUtil.deleteFile(parentFileDir);
                                    }
                                }
                                File tempPartFile = new File(parentFileDir, chunk + ".part");
                                file.transferTo(tempPartFile);
                                boolean uploadDone = true;
                                for (int i = 0; i < chunks; i++) {
                                    File partFile = new File(parentFileDir, i + ".part");
                                    if (!partFile.exists()) {
                                        uploadDone = false;
                                    }
                                }
                                if (uploadDone) {
                                    File destTempFile = new File(uploadFileFolderPath.toString());
                                    PlatFileUtil.unionFile(destTempFile, parentFileDir, fileName);
                                }
                            }
                            result.put("originalfilename", originalFileName);
                            result.put("dbfilepath", dbFilePath.toString());
                            //获取物理路径
                            String attachFilePath = PlatPropUtil.getPropertyValue("config.properties"
                                    , "attachFilePath");
                            String phsicalPath = attachFilePath + dbFilePath;
                            //获取文件后缀名
                            if ("jpg".equals(fileExt) || "jpeg".equals(fileExt) || "png".equals(fileExt)) {
                                PlatImageUtil.compressImage(phsicalPath, phsicalPath, 1000, 600);
                            }
                            if (isForServlet) {
                                result.put("getfileurl", fileServerUrl + "attachfiles/" + dbFilePath);
                            } else {
                                result.put("getfileurl", attachFileUrl + dbFilePath);
                            }

                        }
                    }
                }
            }
            if (unilegalFile) {
                result.put("success", "false");
            } else {
                result.put("success", "true");
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.put("success", "false");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", "false");
        }
        return result;
    }

}
