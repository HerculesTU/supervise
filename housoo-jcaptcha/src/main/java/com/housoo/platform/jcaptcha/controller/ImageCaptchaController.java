package com.housoo.platform.jcaptcha.controller;

import com.housoo.platform.common.controller.BaseController;
import com.housoo.platform.core.service.GlobalConfigService;
import com.housoo.platform.core.util.CaptchaServiceSingleton;
import com.housoo.platform.jcaptcha.utils.CaptchaUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * 描述
 *
 * @author housoo
 * @version 1.0
 * @created 2018-01-22 14:31:15
 */
@Controller
@RequestMapping("/jcaptcha/ImageCaptchaController")
public class ImageCaptchaController extends BaseController {
    // 默认长度
    private static final int DEFAULT_LEN = 4;
    // 默认宽度
    private static final int DEFAULT_WIDTH = 130;
    // 默认高度
    private static final int DEFAULT_HEIGHT = 48;
    /*
    获取系统全局配置service
     */
    @Resource
    private GlobalConfigService globalConfigService;

    /**
     * 获取图片验证码
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/image")
    public void image(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        //获取验证码类型
        Map<String, Object> globalConfig = globalConfigService.getFirstConfigMap();
        String verifyCodeType = (String) globalConfig.get("CONFIG_VERIFY_CODE_TYPE");
        //系统自带(字母+数字)
        if ("1".equals(verifyCodeType)) {
            SpecCaptcha specCaptcha = new SpecCaptcha(DEFAULT_WIDTH,DEFAULT_HEIGHT,DEFAULT_LEN);
            CaptchaUtil.out(specCaptcha,request, response);
        }
        //算术验证
        if ("2".equals(verifyCodeType)) {
            ArithmeticCaptcha arithmeticCaptcha = new ArithmeticCaptcha(DEFAULT_WIDTH,DEFAULT_HEIGHT,DEFAULT_LEN);
            arithmeticCaptcha.setLen(2);  // 几位数运算，默认是两位
            arithmeticCaptcha.getArithmeticString();  // 获取运算的公式：3+2=?
            arithmeticCaptcha.text();  // 获取运算的结果：5
            CaptchaUtil.out(arithmeticCaptcha,request, response);
        }
        //汉字验证码
        if ("3".equals(verifyCodeType)) {
            ChineseCaptcha chineseCaptcha = new ChineseCaptcha(DEFAULT_WIDTH,DEFAULT_HEIGHT,DEFAULT_LEN);
            CaptchaUtil.out(chineseCaptcha,request, response);
        }
        //Gif验证码
        if ("4".equals(verifyCodeType)) {
            GifCaptcha gifCaptcha = new GifCaptcha(DEFAULT_WIDTH,DEFAULT_HEIGHT,DEFAULT_LEN);
            CaptchaUtil.out(gifCaptcha,request, response);
        }

    }
}
