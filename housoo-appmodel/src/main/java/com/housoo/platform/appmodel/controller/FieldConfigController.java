package com.housoo.platform.appmodel.controller;

import com.housoo.platform.core.service.FieldConfigService;
import com.housoo.platform.common.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 描述 FieldConfig业务相关Controller
 *
 * @author housoo
 * @version 1.0
 * @created 2017-03-02 10:54:42
 */
@Controller
@RequestMapping("/appmodel/FieldConfigController")
public class FieldConfigController extends BaseController {
    /**
     *
     */
    @Resource
    private FieldConfigService fieldConfigService;

}
