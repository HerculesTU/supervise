package com.housoo.platform.core.web.filter;

import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatPropUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IPFilter implements Filter {
    //用来存放初始化后的IP白名单列表对应的正则表达式
    private List<String> allowRegexList = new ArrayList<>();

    /**
     * init方法
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        try {
            //在过滤器初始化的时候初始化白名单列表
            initAllowIpList();
        } catch (IOException e) {
            PlatLogUtil.println(e);
        }
    }

    /**
     * 初始化允许的IP集合
     *
     * @throws IOException
     */
    public void initAllowIpList() throws IOException {
        //读取配置文件中的三种IP配置规则
        String allowIP = PlatPropUtil.getPropertyValue("config.properties", "allowIP");
        String allowIPRange = PlatPropUtil.getPropertyValue("config.properties", "allowIPRange");
        String allowIPWildcard = PlatPropUtil.getPropertyValue("config.properties", "allowIPWildcard");

        //对配置的三种方式的IP白名单进行格式校验
        if (!validate(allowIP, allowIPRange, allowIPWildcard)) {
            throw new RuntimeException("IP白名单格式定义异常!");
        }

        //将第一种方式配置的ip地址解析出来,添加到存放IP白名单的集合
        if (null != allowIP && !"".equals(allowIP.trim())) {
            String[] address = allowIP.split(",|;");

            if (null != address && address.length > 0) {
                for (String ip : address) {
                    allowRegexList.add(ip);
                }
            }
        }

        //将第二种方式配置的ip地址解析出来,添加到存放IP白名单的集合
        if (null != allowIPRange && !"".equals(allowIPRange.trim())) {
            String[] addressRanges = allowIPRange.split(",|;");

            if (null != addressRanges && addressRanges.length > 0) {
                for (String addressRange : addressRanges) {
                    String[] addressParts = addressRange.split("-");

                    if (null != addressParts && addressParts.length > 0 && addressParts.length <= 2) {
                        String from = addressParts[0];
                        String to = addressParts[1];
                        String prefix = from.substring(0, from.lastIndexOf(".") + 1);

                        int start = Integer.parseInt(from.substring(from.lastIndexOf(".") + 1, from.length()));
                        int end = Integer.parseInt(to.substring(to.lastIndexOf(".") + 1, to.length()));

                        for (int i = start; i <= end; i++) {
                            allowRegexList.add(prefix + i);
                        }

                    } else {
                        throw new RuntimeException("IP列表格式定义异常!");
                    }
                }
            }
        }

        //将第三种方式配置的ip地址解析为一条一条的正则表达式,添加到存放IP白名单集合,如对此处不明白可以先看后面的备注
        if (null != allowIPWildcard && !"".equals(allowIPWildcard.trim())) {
            String[] address = allowIPWildcard.split(",|;");

            if (null != address && address.length > 0) {
                for (String ip : address) {
                    if (ip.indexOf("*") != -1) {
                        //将*,替换成匹配单端ip地址的正则表达式
                        ip = ip.replaceAll("\\*", "(1\\\\d{1,2}|2[0-4]\\\\d|25[0-5]|\\\\d{1,2})");
                        ip = ip.replaceAll("\\.", "\\\\.");//对.进行转义
                        allowRegexList.add(ip);
                    } else {
                        throw new RuntimeException("IP白名单格式定义异常!");
                    }
                }
            }
        }
    }

    /**
     * 验证
     *
     * @param allowIP
     * @param allowIPRange
     * @param allowIPWildcard
     * @return
     */
    public boolean validate(String allowIP, String allowIPRange, String allowIPWildcard) {

        //匹配IP地址每一段的正则
        String regex = "(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})";
        //把四段用点连接起来,那就是匹配整个ip地址的表达式
        String ipRegex = regex + "\\." + regex + "\\." + regex + "\\." + regex;

        Pattern pattern = Pattern.compile("(" + ipRegex + ")|(" + ipRegex + "(,|;))*");
        //校验第一种配置方式配置的IP白名单格式是否正确
        if (null != allowIP && !"".equals(allowIP.trim())) {
            if (!this.validate(allowIP, pattern)) {
                return false;
            }
        }

        //校验第二种配置方式配置的的IP白名单格式是否正确
        if (null != allowIPRange && !"".equals(allowIPRange.trim())) {
            pattern = Pattern.compile("(" + ipRegex + ")\\-(" + ipRegex + ")|" + "((" + ipRegex + ")\\-(" + ipRegex + ")(,|;))*");
            if (!this.validate(allowIPRange, pattern)) {
                return false;
            }
        }

        //校验第三种配置方式配置的的IP白名单格式是否正确
        if (null != allowIPWildcard && !"".equals(allowIPWildcard.trim())) {
            pattern = Pattern.compile("(" + regex + "\\." + regex + "\\." + "\\*." + "\\*)|" + "(" + regex + "\\." + regex + "\\." + "\\*." + "\\*(,|;))*");
            if (!this.validate(allowIPWildcard, pattern)) {
                return false;
            }
        }
        return true;
    }

    //校验用户配置的ip列表格式是否正确
    public boolean validate(String allowIP, Pattern pattern) {
        //如果为空则不做处理
        if (null != allowIP && !"".equals(allowIP.trim())) {
            StringBuilder sb = new StringBuilder(allowIP);

            //如果用户配置的IP配置了多个,但没有以分号结尾,这里就给它加上分号
            if (!allowIP.endsWith(";")) {
                sb.append(";");
            }
            //如果不匹配
            if (!pattern.matcher(sb).matches()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        //1.获取访问者的IP地址
        String remoteAddress = request.getRemoteAddr();

        if (null == remoteAddress || "".equals(remoteAddress.trim())) {
            throw new RuntimeException("IP地址为空,拒绝访问!");
        }

        //如果白名单为空,则认为没做限制,放行
        if (null == allowRegexList || allowRegexList.size() == 0) {
            filterChain.doFilter(request, response);
            return;
        }

        //检查用户IP是否在白名单
        if (checkIp(remoteAddress)) {
            filterChain.doFilter(request, response);
            return;
        } else {
            PlatLogUtil.println("当前IP地址：" + remoteAddress + ",属于非法访问!");
            response.sendRedirect("webpages/common/custom.jsp");
            throw new RuntimeException("当前IP地址：" + remoteAddress + ",属于非法访问!");
        }
    }

    /**
     * 检查用户IP是否在白名单
     *
     * @param clientAddress
     * @return
     */
    public boolean checkIp(String clientAddress) {
        for (String regex : allowRegexList) {
            if (clientAddress.matches(regex)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {

    }

}
