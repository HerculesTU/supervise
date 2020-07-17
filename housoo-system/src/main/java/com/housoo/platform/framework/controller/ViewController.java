package com.housoo.platform.framework.controller;

import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.SysLogService;
import com.housoo.platform.core.service.SysUserService;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatFileUtil;
import com.housoo.platform.core.util.PlatUICompUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述
 *
 * @author 高飞
 * @created 2017年1月24日 下午2:11:56
 */
@Controller
@RequestMapping("/framework/ViewController")
public class ViewController extends BaseController {

    @Resource
    private SysLogService sysLogService;
    @Resource
    private SysUserService sysUserService;
    /**
     * 跳转到后台框架主页
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "main")
    public ModelAndView main(HttpServletRequest request) {
        //判断是否首次登录\密码为初始密码
        if (sysUserService.checkFirstLogin(PlatAppUtil.getBackPlatLoginUser())) {
            request.setAttribute("firstLogin", "yes");
        }
        String subsyscode = request.getParameter("subsyscode");
        request.setAttribute("subsyscode", subsyscode);
        return new ModelAndView("background/framework/main");
    }


    /**
     * 跳转到目标界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "view")
    public ModelAndView view(HttpServletRequest request) {
        String UI_DESIGNCODE = request.getParameter("UI_DESIGNCODE");
        Map<String, Object> params = PlatBeanUtil.getMapFromRequest(request);
        request.setAttribute("params", params);
        return PlatUICompUtil.goDesignUI(UI_DESIGNCODE, request);
    }

    /**
     * 删除文件
     *
     * @param request
     * @param response
     */
    @RequestMapping("/deleteUploadFile")
    public void deleteUploadFile(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String filename = request.getParameter("filename");
        String filePath = request.getSession().getServletContext()
                .getRealPath("/Upload/") + filename;
        File fileDir = new File(filePath);
        if (fileDir.exists()) {
            PlatFileUtil.deleteFile(fileDir);
        }
        result.put("success", "true");
        this.printObjectJsonString(result, response);
    }

    /**
     * 浏览器下载
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "browserDownload")
    public ModelAndView browserDownload(HttpServletRequest request) {
        return new ModelAndView("background/framework/browserDownload");
    }
}
