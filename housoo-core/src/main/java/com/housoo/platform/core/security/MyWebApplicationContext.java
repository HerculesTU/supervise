package com.housoo.platform.core.security;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * 描述
 *
 * @author housoo
 * @created 2017年4月4日 下午1:26:07
 */
public class MyWebApplicationContext extends XmlWebApplicationContext {
    @Override
    protected DefaultListableBeanFactory createBeanFactory() {
        DefaultListableBeanFactory beanFactory = super.createBeanFactory();
        beanFactory.setAllowRawInjectionDespiteWrapping(true);
        return beanFactory;
    }
}
