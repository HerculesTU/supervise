package com.housoo.platform.appmodel.controller;

import com.housoo.platform.core.util.*;
import com.housoo.platform.core.service.FileAttachService;
import com.housoo.platform.common.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 描述 附件上传处理业务
 *
 * @author housoo
 * @created 2017年4月11日 上午11:26:25
 */
@Controller
@RequestMapping("/system/FileAttachController")
public class FileAttachController extends BaseController {

    /**
     *
     */
    @Resource
    private FileAttachService fileAttachService;

    /**
     * 文件上传
     *
     * @param request
     * @param response
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    @RequestMapping(value = "/upload")
    public void upload(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = fileAttachService.uploadFile(request, false);
        PlatStringUtil.printObjectJsonString(result, response);
    }

    /**
     * 文件下载
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        BrowserUtils.allowCrossDomain(response);
        //获取附件ID
        String fileId = request.getParameter("fileId");
        String dbfilepath = null;
        // 下载的文件名称
        String fileName = null;
        if (StringUtils.isNotEmpty(fileId)) {
            Map<String, Object> fileInfo = fileAttachService.getRecord("PLAT_SYSTEM_FILEATTACH",
                    new String[]{"FILE_ID"}, new Object[]{fileId});
            fileName = (String) fileInfo.get("FILE_NAME");
            dbfilepath = (String) fileInfo.get("FILE_PATH");
        } else {
            dbfilepath = request.getParameter("dbfilepath");
            fileName = request.getParameter("fileName");
        }
        // 获取附件的根路径
        String rootPath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        InputStream inputStream = null;
        // 激活下载操作
        OutputStream os = null;
        try {
            // 为了解决中文名称乱码问题
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            if (BrowserUtils.checkBrowse(request).contains("IE")) {
                response.setHeader("Content-Disposition", "attachment; filename=\""
                        + URLEncoder.encode(fileName, "utf-8") + "\"");
            } else {
                response.setHeader("Content-Disposition", "attachment;filename=\""
                        + new String(fileName.getBytes("gb2312"), "ISO8859-1") + "\"");
            }
            // 用于记录以完成的下载的数据量，单位是byte
            // 打开本地文件流
            inputStream = new FileInputStream(rootPath + dbfilepath);
            // 激活下载操作
            os = response.getOutputStream();
            // 循环写入输出流
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }

        } catch (UnsupportedEncodingException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (FileNotFoundException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            // 这里主要关闭。
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
    }

    /**
     * 文件上传
     *
     * @param request
     * @param response
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    @RequestMapping("/uploadUE")
    public void uploadUE(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        // 上传文件根目录文件
        String uploadRootFolder = "uploadUE";
        // 获取附件的根路径
        String rooPath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        // 获取网站的访问根路径
        String attachFileUrl = PlatPropUtil.getPropertyValue("config.properties", "attachFileUrl");
        //获取文件存储路径
        String attachFilePath = PlatPropUtil.getPropertyValue("config.properties"
                , "attachFilePath");
        // 获取当前的日期
        String currentDate = PlatDateTimeUtil.formatDate(new Date(), "YYYY-MM-dd");
        StringBuffer uploadFileFolderPath = new StringBuffer(rooPath);
        uploadFileFolderPath.append(uploadRootFolder).append("/").append(currentDate).append("/");
        // 定义存储在数据库中的文件路径
        StringBuffer dbFilePath = new StringBuffer(uploadRootFolder).append("/").append(currentDate).append("/");
        try {
            // 创建一个通用的多部分解析器
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                    request.getSession().getServletContext());
            // 判断 request 是否有文件上传,即多部分请求
            if (multipartResolver.isMultipart(request)) {
                // 转换成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                // 取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    // 取得上传文件
                    MultipartFile file = multiRequest.getFile(iter.next());
                    if (file != null) {
                        // 取得当前上传文件的文件名称
                        String originalFileName = file.getOriginalFilename();
                        String fileName = UUIDGenerator.getUUID() + "." + PlatFileUtil.getFileExt(originalFileName);
                        dbFilePath.append(fileName);
                        // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                        if (StringUtils.isNotEmpty(originalFileName)
                                && StringUtils.isNotEmpty(originalFileName.trim())) {
                            File targetFile = new File(uploadFileFolderPath.toString(), fileName);
                            if (!targetFile.exists()) {
                                targetFile.mkdirs();
                            }
                            file.transferTo(targetFile);
                        }
                        Map<String, Object> data = new HashMap<String, Object>();
                        data.put("url", attachFileUrl + dbFilePath);
                        data.put("original", originalFileName);
                        String absoltePath = attachFilePath + dbFilePath;
                        File absolteFile = new File(absoltePath);
                        data.put("type", PlatFileUtil.getFileExt(dbFilePath.toString()));
                        data.put("size", absolteFile.length());
                        result.putAll(data);
                    }
                }
            }
            result.put("state", "SUCCESS");
        } catch (IOException e) {
            result.put("state", "");
        } catch (Exception e) {
            result.put("state", "");
        }
        this.printObjectJsonString(result, response);
    }
}
