package com.housoo.platform.test.framework;

import com.housoo.platform.framework.controller.LoginController;
import com.housoo.platform.test.BaseWebTestCase;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Created by cjr on 2019-12-30.
 */
public class LoginControllerTestCase extends BaseWebTestCase {
    /**
     *
     */
    @Resource
    private LoginController loginController;

    /**
     *
     */
    @Test
    public void testLogin() {
        loginController.backLogin(request, response);
    }
}
