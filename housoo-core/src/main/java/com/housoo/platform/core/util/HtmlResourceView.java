package com.housoo.platform.core.util;

import org.springframework.web.servlet.view.InternalResourceView;

import java.io.File;
import java.util.Locale;

/**
 * @author gf
 * @date 2019/11/12
 */
public class HtmlResourceView extends InternalResourceView {

    @Override
    public boolean checkResource(Locale locale) {
        File file = new File(this.getServletContext().getRealPath("/") + getUrl());
        // 判断该页面是否存在
        return file.exists();
    }
}
