package com.housoo.platform.core.util;

/**
 * @author 封装WebService的工具类
 * 2017年9月16日
 */
public class PlatWebServiceUtil {
    /**
     * 试用cxf来调用服务
     *
     * @param webWsdl
     * @param namespace
     * @param method
     * @param params
     * @return
     */
    public static String callByCxf(String webWsdl, String namespace, String method, Object[] params) {
        try {
            // 动态工厂
            org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory dcf =
                    org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory.newInstance();
            // 接口地址
            org.apache.cxf.endpoint.Client client = dcf.createClient(webWsdl);
            // 动态调用
            Object[] returnObjects = client.invoke(method, params);
            return returnObjects[0].toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
