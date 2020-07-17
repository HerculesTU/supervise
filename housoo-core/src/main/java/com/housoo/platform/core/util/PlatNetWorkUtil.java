package com.housoo.platform.core.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 网络操作工具类
 */
public class PlatNetWorkUtil {

    /**
     * 创建一个cookie
     *
     * @param response
     * @param name     名称
     * @param value    值
     * @param maxAge   生命周期  以秒为单位
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
        }
        response.addCookie(cookie);
    }

    /**
     * 获取cookie的值
     *
     * @param request
     * @param name
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        //定义用户的账号
        String value = null;
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    value = cookie.getValue();
                    break;
                }
            }
        }
        return value;
    }

    /**
     * 删除一个cookie
     *
     * @param response
     * @param name
     */
    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie killMyCookie = new Cookie(name, null);
        killMyCookie.setMaxAge(0);
        killMyCookie.setPath("/");
        response.addCookie(killMyCookie);
    }
}
