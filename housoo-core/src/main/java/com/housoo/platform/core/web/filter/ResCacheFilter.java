package com.housoo.platform.core.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author housoo
 * <p>
 * 2017年10月13日
 */
public class ResCacheFilter implements Filter {
    /**
     *
     */
    private FilterConfig filterConfig;

    /**
     *
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    /**
     *
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        //1.获取用户想访问的资源
        String uri = request.getRequestURI();
        //2.得到用户想访问的资源的后缀名
        String ext = uri.substring(uri.lastIndexOf(".") + 1);
        //得到资源需要缓存的时间
        String time = filterConfig.getInitParameter(ext);
        if (time != null) {
            long t = Long.parseLong(time) * 3600 * 1000;
            //设置浏览器缓存
            response.setDateHeader("expires", System.currentTimeMillis() + t);
        } else {
            //设置浏览器不缓存
            response.setHeader("Expires", "-1");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
