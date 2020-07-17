package com.housoo.platform.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.poi.util.StringUtil;

import com.housoo.platform.core.util.PlatHttpUtil;
import com.housoo.platform.core.util.UUIDGenerator;

/**
 * @author 胡裕
 *
 * 
 */
public class PlatHttpUtilTestCase {

    /**
     * @param args
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public static void main(String[] args) throws ClientProtocolException, IOException {
        Map<String,Object> postParams= new HashMap<String,Object>();
        postParams.put("guid", UUIDGenerator.getUUID());
        postParams.put("uploadRootFolder","deploylog");
        postParams.put("DEVMAN_EMAIL", "332437849@qq.com");
        postParams.put("DEVMAN_PASS", "evecom@123");
        String result = PlatHttpUtil.uploadFile("http://127.0.0.1/system/DeployLogController/uploaddeploy.do",
                "d:/20180420.jar", postParams);
        System.out.println(result);
    }

}
