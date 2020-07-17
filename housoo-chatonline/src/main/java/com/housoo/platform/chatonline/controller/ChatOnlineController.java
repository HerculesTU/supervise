package com.housoo.platform.chatonline.controller;

import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.*;
import com.housoo.platform.core.service.ChatOnlineService;
import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 描述 用户分组业务相关Controller
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-07-13 11:00:59
 */
@Controller
@RequestMapping("/chatonline/ChatOnlineController")
public class ChatOnlineController extends BaseController {
    /**
     *
     */
    @Resource
    private ChatOnlineService chatOnlineService;
    /**
     *
     */
    @Resource
    private SysUserService sysUserService;
    /**
     *
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * 描述
     *
     * @param request
     * @param response
     * @created 2017年7月13日 下午2:58:48
     */
    @RequestMapping(params = "initList")
    public void initList(HttpServletRequest request,
                         HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> data = this.chatOnlineService.getInitData();
        result.put("code", 0);
        result.put("msg", "");
        result.put("data", data);
        this.printObjectJsonString(result, response);
    }

    /**
     * 描述
     *
     * @param request
     * @param response
     * @created 2017年7月14日 下午12:22:16
     */
    @RequestMapping(params = "updateSign")
    public void updateSign(HttpServletRequest request,
                           HttpServletResponse response) {
        Map<String, Object> sysUser = PlatBeanUtil.getMapFromRequest(request);
        sysUserService.saveOrUpdate("PLAT_SYSTEM_SYSUSER",
                sysUser, SysConstants.ID_GENERATOR_UUID, null);
        sysUser.put("success", true);
        this.printObjectJsonString(sysUser, response);
    }

    /**
     * 描述
     *
     * @param request
     * @param response
     * @created 2017年7月14日 下午12:22:16
     */
    @RequestMapping(params = "getNoReadMsg")
    public void getNoReadMsg(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> sysUser = PlatBeanUtil.getMapFromRequest(request);
        this.chatOnlineService.updateNoReadMsgUser(sysUser);
        result.put("success", true);
        this.printObjectJsonString(result, response);
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
    @RequestMapping(params = "uploadImg")
    public void uploadImg(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        // 上传文件根目录文件
        String uploadRootFolder = "chatUpload";
        // 获取附件的根路径
        String rooPath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        // 获取网站的访问根路径
        String attachFileUrl = PlatPropUtil.getPropertyValue("config.properties", "attachFileUrl");
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
                        data.put("src", attachFileUrl + dbFilePath);
                        data.put("name", originalFileName);
                        result.put("data", data);
                    }
                }
            }
            result.put("code", 0);
            result.put("msg", "");
        } catch (IOException e) {
            result.put("code", 1);
        } catch (Exception e) {
            result.put("code", 1);
        }
        this.printObjectJsonString(result, response);
    }

    /**
     * 描述
     *
     * @param request
     * @return
     * @created 2017年7月15日 下午4:57:17
     */
    @RequestMapping("/showChatLog")
    public ModelAndView showChatLog(HttpServletRequest request) {
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        request.setAttribute("id", id);
        request.setAttribute("type", type);
        return new ModelAndView("background/chatonline/showChatLog");
    }

    /**
     * 描述
     *
     * @param request
     * @param response
     * @created 2017年7月15日 下午7:41:42
     */
    @RequestMapping(params = "getChatLog")
    public void getChatLog(HttpServletRequest request,
                           HttpServletResponse response) {
        SqlFilter filter = new SqlFilter(request);
        List<Map<String, Object>> list = chatOnlineService.findBySqlFilter(filter);
        this.printListJsonString(filter.getPagingBean(), list, response);
    }

    /**
     * 描述
     *
     * @param request
     * @return
     * @created 2017年7月15日 下午8:42:13
     */
    @RequestMapping("/find")
    public ModelAndView find(HttpServletRequest request) {
        return new ModelAndView("background/chatonline/find");
    }

    /**
     * 描述
     *
     * @param request
     * @param response
     * @created 2017年7月16日 上午11:31:34
     */
    @RequestMapping(params = "showSearchGroupResult")
    public void showSearchGroupResult(HttpServletRequest request,
                                      HttpServletResponse response) {
        String val = request.getParameter("val");
        List<Map<String, Object>> list = chatOnlineService.findSearchGroupResult(val);
        this.printObjectJsonString(list, response);
    }

    /**
     * 描述
     *
     * @param request
     * @param response
     * @created 2017年7月16日 下午12:05:26
     */
    @RequestMapping(params = "showMyGroupResult")
    public void showMyGroupResult(HttpServletRequest request,
                                  HttpServletResponse response) {
        List<Map<String, Object>> list = chatOnlineService.findMyGroupResult();
        this.printObjectJsonString(list, response);
    }

    /**
     * 描述
     *
     * @param request
     * @param response
     * @created 2017年7月16日 下午2:50:47
     */
    @RequestMapping(params = "applyAddGroup")
    public void applyAddGroup(HttpServletRequest request,
                              HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String id = request.getParameter("id");
        boolean b = chatOnlineService.isCanAddGroup(id);
        if (b) {
            result.put("success", true);
            result.put("msg", "加群成功");
        } else {
            result.put("success", false);
            result.put("msg", "已在该群里面，请不要重复添加！");
        }
        this.printObjectJsonString(result, response);
    }

    /**
     * 描述
     *
     * @param request
     * @param response
     * @created 2017年7月16日 下午3:12:59
     */
    @RequestMapping(params = "getMembers")
    public void getMembers(HttpServletRequest request,
                           HttpServletResponse response) {
        String id = request.getParameter("id");
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> data = this.chatOnlineService.getMembers(id);
        result.put("code", 0);
        result.put("msg", "");
        result.put("data", data);
        this.printObjectJsonString(result, response);
    }

    /**
     * 描述 退出
     *
     * @param request
     * @param response
     * @created 2017年7月16日 下午4:16:30
     */
    @RequestMapping(params = "removeGroup")
    public void removeGroup(HttpServletRequest request,
                            HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> userMap = PlatAppUtil.getBackPlatLoginUser();
        String systemuserId = userMap.get("SYSUSER_ID").toString();
        String id = request.getParameter("id");
        chatOnlineService.removeGroup(id, systemuserId);
        result.put("success", true);
        result.put("msg", "退群成功");

        this.printObjectJsonString(result, response);
    }

    /**
     * 描述
     *
     * @param request
     * @param response
     * @created 2017年7月16日 下午4:39:18
     */
    @RequestMapping(params = "getOutGroup")
    public void getOutGroup(HttpServletRequest request,
                            HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String systemuserId = request.getParameter("userid");
        String id = request.getParameter("id");
        chatOnlineService.removeGroup(id, systemuserId);
        result.put("success", true);
        result.put("msg", "踢出成功");

        this.printObjectJsonString(result, response);
    }

    /**
     * 描述
     *
     * @param request
     * @param response
     * @created 2017年7月16日 下午4:44:29
     */
    @RequestMapping(params = "removeAllGroup")
    public void removeAllGroup(HttpServletRequest request,
                               HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String id = request.getParameter("id");
        chatOnlineService.removeAllGroup(id);
        result.put("success", true);
        result.put("msg", "解散群成功");

        this.printObjectJsonString(result, response);
    }

    /**
     * 描述
     *
     * @param request
     * @param response
     * @created 2017年7月16日 下午2:50:47
     */
    @RequestMapping(params = "invitationGroup")
    public void invitationGroup(HttpServletRequest request,
                                HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String id = request.getParameter("id");
        String checkUserIds = request.getParameter("checkUserIds");
        List<Map<String, Object>> list = chatOnlineService.invitationGroup(id, checkUserIds);
        result.put("list", list);
        result.put("success", true);
        result.put("msg", "邀请成功");

        this.printObjectJsonString(result, response);
    }
}
