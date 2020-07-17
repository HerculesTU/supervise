package com.housoo.platform.core.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 下载工具类
 *
 * @author gf
 */
public class DownUtil {

    public static void main(String[] args) {
        // 下载列表
        List<String> downloadList = new ArrayList<>();
        // 添加下载地址
        downloadList.add("http://192.168.1.30:8088/attachfiles/requestInfo/1111400001812016/1111400001812016_NWCXQQ_8be1ad5bd01796752a078fa6b0e44b62.NJWF");
        downloadList.add("http://192.168.1.30:8088/attachfiles/requestInfo/1111400001812016/1111400001812016_NWCXQQ_8cafd66a12abc2ee42e5db3a443ad4d2.NJWF");
        download(downloadList, "C:\\Users\\Administrator\\Desktop\\");
    }

    /**
     * 下载
     *
     * @param downloadList
     * @param dest
     */
    public static void download(List<String> downloadList, String dest) {

        // 线程池
        ExecutorService pool = null;
        HttpURLConnection connection = null;
        //循环下载
        try {
            for (int i = 0; i < downloadList.size(); i++) {
                int poolSize = Runtime.getRuntime().availableProcessors() * 2;
                BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(512);
                RejectedExecutionHandler policy = new ThreadPoolExecutor.DiscardPolicy();
                pool = new ThreadPoolExecutor(poolSize, poolSize, 0, TimeUnit.SECONDS, queue, policy);
                final String url = downloadList.get(i);
                String filename = getFilename(downloadList.get(i));
                Future<HttpURLConnection> future = pool.submit(new Callable<HttpURLConnection>() {
                    @Override
                    public HttpURLConnection call() throws Exception {
                        HttpURLConnection connection = null;
                        connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setConnectTimeout(10000);//连接超时时间
                        connection.setReadTimeout(10000);// 读取超时时间
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setRequestMethod("GET");
                        //断点续连,每次要算出range的范围,请参考Http 1.1协议
                        //connection.setRequestProperty("Range", "bytes=0");
                        connection.connect();
                        return connection;
                    }
                });
                connection = future.get();
                // 写入文件
                writeFile(new BufferedInputStream(connection.getInputStream()), dest, URLDecoder.decode(filename, "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != connection) {
                connection.disconnect();
            }
            if (null != pool) {
                pool.shutdown();
            }
        }
    }

    /**
     * 通过截取URL地址获得文件名
     * 注意：还有一种下载地址是没有文件后缀的，这个需要通过响应头中的
     * Content-Disposition字段 获得filename，一般格式为："attachment; filename=\xxx.exe\"
     *
     * @param url
     * @return
     */
    static String getFilename(String url) {
        return ("".equals(url) || null == url) ? "" : url.substring(url.lastIndexOf("/") + 1, url.length());
    }

    /**
     * 写入文件
     *
     * @param bufferedInputStream
     * @param dest
     * @param filename
     */
    static void writeFile(BufferedInputStream bufferedInputStream, String dest, String filename) {
        //创建本地文件
        File destfileFile = new File(dest + filename);
        if (destfileFile.exists()) {
            destfileFile.delete();
        }
        if (!destfileFile.getParentFile().exists()) {
            destfileFile.getParentFile().mkdir();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(destfileFile);
            byte[] b = new byte[1024];
            int len = 0;
            // 写入文件
            while ((len = bufferedInputStream.read(b, 0, b.length)) != -1) {
                fileOutputStream.write(b, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                if (null != bufferedInputStream) {
                    bufferedInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
