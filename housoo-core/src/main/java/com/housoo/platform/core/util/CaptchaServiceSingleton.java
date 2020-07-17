package com.housoo.platform.core.util;

import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * 描述
 *
 * @author housoo
 * @version 1.0
 * @created 2018-01-22 14:31:15
 */
public class CaptchaServiceSingleton {
    /**
     *
     */
    private static ImageCaptchaService instance = new DefaultManageableImageCaptchaService(
            new FastHashMapCaptchaStore(), new GMailEngine(), 180,
            100000, 75000);

    /**
     * 获取ImageCaptchaService实例
     *
     * @return
     */
    public static ImageCaptchaService getInstance() {
        return instance;
    }
}
