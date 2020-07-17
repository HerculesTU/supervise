package com.housoo.platform.core.web.filter;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 跨域请求过滤器
 *
 * @author 高飞
 * @date 2019.04.01
 */
public class CrossDomainFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CrossDomainFilter.class);

    private Properties properties = new Properties();

    private FilterConfig config;

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void destroy() {
        properties = null;
        this.config = null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 得到请求的uri和url
        String requestUri = req.getRequestURI();
        String requestUrl = req.getRequestURL().toString();
        //log.info(" requestUri:{}, requestUrl:{} ", requestUri, requestUrl);

        // 跨域处理
        this.crossDomain(req, resp, requestUrl, chain);
    }

    /**
     * 处理跨域问题
     */
    private void crossDomain(HttpServletRequest request, HttpServletResponse response, String url, FilterChain chain) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "accept,content-type");
        response.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST,DELETE,PUT");
        chain.doFilter(request, response);
    }

}